package com.notcharrow.enchantmentsunbound.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

	/**
	 * @author NotchArrow
	 * @reason Allow enchantment combos like Mending + Infinity
	 */
	@Overwrite
	public static boolean canBeCombined(RegistryEntry<Enchantment> first, RegistryEntry<Enchantment> second) {
		return true;
	}
}