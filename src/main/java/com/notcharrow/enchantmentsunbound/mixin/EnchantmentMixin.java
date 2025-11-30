package com.notcharrow.enchantmentsunbound.mixin;

import com.notcharrow.enchantmentsunbound.config.ConfigManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

	@Inject(method = "isAcceptableItem", at = @At("TAIL"), cancellable = true)
	public void isAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (!ConfigManager.config.itemEnchantConflicts) {
			cir.setReturnValue(true);
		}
	}
}