package com.notcharrow.enchantmentsunbound.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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
	int levelCost = screen.getMenu().getCost();
	int xpLevel = player.experienceLevel;
	MutableComponent text = Component.literal("Enchantment Cost: " + levelCost);

	if (xpLevel >= levelCost) {
		text.withStyle(ChatFormatting.GREEN);
	} else {
		text.withStyle(ChatFormatting.RED);
	}
	TOO_EXPENSIVE_TEXT = text;
	}
}
