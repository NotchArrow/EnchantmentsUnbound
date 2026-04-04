package com.notcharrow.enchantmentsunbound.mixin;

import com.notcharrow.enchantmentsunbound.config.ConfigManager;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin (MerchantOffer.class)
public abstract class MerchantOfferMixin {

	@Inject(
			method = "<init>(Lnet/minecraft/world/item/trading/ItemCost;Ljava/util/Optional;Lnet/minecraft/world/item/ItemStack;IIZIIFI)V",
			at = @At("RETURN")
	)
	private void editTrade(ItemCost baseCostA, Optional costB, ItemStack result,
						   int _uses, int maxUses, boolean rewardExp, int specialPriceDiff, int demand, float priceMultiplier, int xp, CallbackInfo ci) {
		if (ConfigManager.config.modifyVillagers) {
			if (result.getItem() == Items.ENCHANTED_BOOK) {
				ItemEnchantments enchantments = result.get(DataComponents.STORED_ENCHANTMENTS);
				RandomSource random = RandomSource.create(baseCostA.count());
				for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
					int level;
					if (ConfigManager.config.useGlobalVillagerCap) {
						level = random.nextIntBetweenInclusive(
								1,
								ConfigManager.config.globalVillagerCap
						);
					} else {
						level = random.nextIntBetweenInclusive(
								1,
								ConfigManager.config.enchantmentVillagerCaps.getOrDefault(
										entry.getKey().getRegisteredName(),
										entry.getKey().value().getMaxLevel()
								)
						);
					}
					result.enchant(entry.getKey(), level);
				}
			}
		}
	}
}