package com.notcharrow.enchantmentsunbound.mixin;

import com.notcharrow.enchantmentsunbound.config.ConfigManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

	@Inject(method = "canBeCombined", at = @At("TAIL"), cancellable = true)
	private static void canBeCombined(RegistryEntry<Enchantment> first, RegistryEntry<Enchantment> second, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(true);
	}

	@Inject(method = "isAcceptableItem", at = @At("TAIL"), cancellable = true)
	private void isAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (!ConfigManager.config.itemEnchantConflicts) {
			cir.setReturnValue(true);
		}
	}
}