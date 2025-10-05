package com.notcharrow.enchantmentsunbound.mixin;

import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreen.class)
public class AnvilScreenMixin {
	@Shadow @Final @Mutable private static Text TOO_EXPENSIVE_TEXT;
	@Shadow @Final private PlayerEntity player;

	@Inject(method = "onSlotUpdate", at = @At("TAIL"))
	private void tick(CallbackInfo ci) {

	AnvilScreen screen = ((AnvilScreen) (Object) this);
	int levelCost = screen.getScreenHandler().getLevelCost();
	int xpLevel = player.experienceLevel;
	MutableText text = Text.literal("Enchantment Cost: " + levelCost);

	if (xpLevel >= levelCost) {
		text.formatted(Formatting.GREEN);
	} else {
		text.formatted(Formatting.RED);
	}
	TOO_EXPENSIVE_TEXT = text;
	}
}
