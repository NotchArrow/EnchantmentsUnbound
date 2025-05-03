package notcharrow.enchantmentsunbound.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

	@ModifyReturnValue(
			method = "canBeCombined(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/registry/entry/RegistryEntry;)Z",
			at = @At("RETURN")
	)
	private static boolean alwaysTrue(boolean original) {
		return true;
	}
}
