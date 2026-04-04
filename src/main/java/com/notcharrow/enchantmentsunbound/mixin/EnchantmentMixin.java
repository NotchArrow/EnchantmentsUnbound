package com.notcharrow.enchantmentsunbound.mixin;

import com.notcharrow.enchantmentsunbound.config.ConfigManager;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

	@Inject(method = "areCompatible", at = @At("TAIL"), cancellable = true)
	private static void canBeCombined(Holder<Enchantment> enchantment, Holder<Enchantment> other, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(true);
	}

	@Inject(method = "canEnchant", at = @At("TAIL"), cancellable = true)
	private void isAcceptableItem(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
		if (!ConfigManager.config.itemEnchantConflicts) {
			cir.setReturnValue(true);
		}
	}
}