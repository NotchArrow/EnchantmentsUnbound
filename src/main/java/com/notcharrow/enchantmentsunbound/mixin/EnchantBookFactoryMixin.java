package com.notcharrow.enchantmentsunbound.mixin;

import com.notcharrow.enchantmentsunbound.config.ConfigManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin (TradeOffers.EnchantBookFactory.class)
public abstract class EnchantBookFactoryMixin {

	@Redirect(
			method = "create(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/random/Random;)Lnet/minecraft/village/TradeOffer;",
			at = @At(
					value = "NEW",
					target = "net/minecraft/enchantment/EnchantmentLevelEntry"
			)
	)
	private EnchantmentLevelEntry redirectEnchantmentCreation(RegistryEntry<Enchantment> registryEntry, int level, ServerWorld world, Entity entity, Random random) {

		if (ConfigManager.config.modifyVillagers) {
			if (ConfigManager.config.useGlobalVillagerCap) {
				level = random.nextBetween(1, ConfigManager.config.globalVillagerCap);
			} else {
				level = random.nextBetween(1,
						ConfigManager.config.enchantmentVillagerCaps.getOrDefault(registryEntry.getIdAsString(), registryEntry.value().getMaxLevel())
				);
			}
		}
		return new EnchantmentLevelEntry(registryEntry, level);
	}
}