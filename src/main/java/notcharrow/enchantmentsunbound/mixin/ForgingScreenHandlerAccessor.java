package notcharrow.enchantmentsunbound.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ForgingScreenHandler;
import notcharrow.enchantmentsunbound.helper.AnvilScreenHandlerPlayerAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ForgingScreenHandler.class)
public abstract class ForgingScreenHandlerAccessor implements AnvilScreenHandlerPlayerAccess {
	@Shadow
	protected PlayerEntity player;

	@Override
	public PlayerEntity getPlayer() {
		return player;
	}
}
