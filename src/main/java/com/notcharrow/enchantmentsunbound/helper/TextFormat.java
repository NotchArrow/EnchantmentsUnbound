package com.notcharrow.enchantmentsunbound.helper;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;

public class TextFormat {
	public static MutableComponent styledText(String message) {
		String[] messageParts = message.split(":");
		MutableComponent styledText = Component.literal(messageParts[0] + ":").withStyle(ChatFormatting.YELLOW);
		MutableComponent styledNumber = Component.literal(messageParts[1]).withStyle(ChatFormatting.GOLD);

		styledText.append(styledNumber);
		return styledText;
	}

	public static MutableComponent anvilText(Player player, AnvilMenu anvilMenu) {
		int levelCost = anvilMenu.getCost();
		int xpLevel = player.experienceLevel;
		MutableComponent text = Component.translatable("text.enchantments-unbound.ui.anvilText", levelCost);

		if (xpLevel >= levelCost || player.isCreative()) {
			text.withStyle(ChatFormatting.GREEN);
		} else {
			text.withStyle(ChatFormatting.RED);
		}
		return text;
	}
}
