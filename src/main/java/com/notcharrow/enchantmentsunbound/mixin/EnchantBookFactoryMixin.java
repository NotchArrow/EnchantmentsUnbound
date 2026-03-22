package com.notcharrow.enchantmentsunbound.mixin;

import com.notcharrow.enchantmentsunbound.config.ConfigManager;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin (TradeOffers.EnchantBookFactory.class)
public abstract class EnchantBookFactoryMixin {

	@Inject(
			method = "create(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/random/Random;)Lnet/minecraft/village/TradeOffer;",
			at = @At(
					value = "TAIL"
			),
			cancellable = true)
	private void editTrade(Entity entity, Random random, CallbackInfoReturnable<TradeOffer> cir) {
		if (ConfigManager.config.modifyVillagers) {
			TradeOffer offer = cir.getReturnValue();
			ItemEnchantmentsComponent enchantmentsComponent = offer.getSellItem().get(DataComponentTypes.STORED_ENCHANTMENTS);
			if (enchantmentsComponent != null) {
				Set<RegistryEntry<Enchantment>> enchantments = enchantmentsComponent.getEnchantments();
				ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
				for (RegistryEntry<Enchantment> enchantment : enchantments) {
					int level;
					if (ConfigManager.config.useGlobalVillagerCap) {
						level = random.nextBetween(1, ConfigManager.config.globalVillagerCap);
					} else {
						level = random.nextBetween(1,
								ConfigManager.config.enchantmentVillagerCaps.getOrDefault(enchantment.getIdAsString(), enchantment.value().getMaxLevel())
						);
					}
					enchantedBook.addEnchantment(enchantment, level);
				}
				cir.setReturnValue(new TradeOffer(
						offer.getFirstBuyItem(),
						offer.getSecondBuyItem(),
						enchantedBook,
						offer.getUses(),
						offer.getMaxUses(),
						offer.getMerchantExperience(),
						offer.getPriceMultiplier(),
						offer.getDemandBonus()
				));
			}
		}
	}
}