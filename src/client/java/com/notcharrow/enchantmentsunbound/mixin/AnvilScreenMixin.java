package com.notcharrow.enchantmentsunbound.mixin;

import com.notcharrow.enchantmentsunbound.helper.TextFormat;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreen.class)
public class AnvilScreenMixin {
	@Shadow @Final @Mutable private static Component TOO_EXPENSIVE_TEXT;
	@Shadow @Final private Player player;

	@Inject(method = "containerTick", at = @At("TAIL"))
	private void handledScreenTick(CallbackInfo ci) {
		AnvilScreen screen = ((AnvilScreen) (Object) this);
		TOO_EXPENSIVE_TEXT = TextFormat.anvilText(player, screen.getMenu());
	}
}
