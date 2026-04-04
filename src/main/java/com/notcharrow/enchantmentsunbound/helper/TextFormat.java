package com.notcharrow.enchantmentsunbound.helper;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

public class TextFormat {
	public static MutableComponent styledText(String message) {
		String[] messageParts = message.split(":");
		MutableComponent styledText = Component.literal(messageParts[0] + ":");
		Style textStyle = Style.EMPTY.withBold(true).withColor(ChatFormatting.GOLD);
		styledText.setStyle(textStyle);

		MutableComponent styledNumber = Component.literal(messageParts[1]);
		textStyle = Style.EMPTY.withBold(true).withColor(ChatFormatting.LIGHT_PURPLE);
		styledNumber.setStyle(textStyle);

		styledText.append(styledNumber);
		return styledText;
	}

	public static MutableComponent enchantCapText(String message) {
		String[] messageParts = message.split(":");
		MutableComponent enchantCapText = Component.literal(messageParts[0] + ":");
		Style textStyle = Style.EMPTY.withBold(false).withColor(ChatFormatting.GOLD);
		enchantCapText.setStyle(textStyle);

		MutableComponent styledNumber = Component.literal(messageParts[1]);
		textStyle = Style.EMPTY.withBold(true).withColor(ChatFormatting.LIGHT_PURPLE);
		styledNumber.setStyle(textStyle);

		enchantCapText.append(styledNumber);
		return enchantCapText;
	}

	public static MutableComponent costInfoText(String message) {
		MutableComponent costInfoText = Component.literal(message);
		Style textStyle = Style.EMPTY.withColor(ChatFormatting.GREEN);
		costInfoText.setStyle(textStyle);
		return costInfoText;
	}

	public static MutableComponent miscInfoText(String message) {
		MutableComponent miscInfoText = Component.literal(message);
		Style textStyle = Style.EMPTY.withColor(ChatFormatting.AQUA);
		miscInfoText.setStyle(textStyle);
		return miscInfoText;
	}
}
