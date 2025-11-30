package com.notcharrow.enchantmentsunbound.helper;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TextFormat {
	public static Text styledText(String message) {
		String[] messageParts = message.split(":");
		MutableText styledText = Text.literal(messageParts[0] + ":");
		Style textStyle = Style.EMPTY.withBold(true).withColor(Formatting.GOLD);
		styledText.setStyle(textStyle);

		MutableText styledNumber = Text.literal(messageParts[1]);
		textStyle = Style.EMPTY.withBold(true).withColor(Formatting.LIGHT_PURPLE);
		styledNumber.setStyle(textStyle);

		styledText.append(styledNumber);
		return styledText;
	}

	public static Text enchantCapText(String message) {
		String[] messageParts = message.split(":");
		MutableText enchantCapText = Text.literal(messageParts[0] + ":");
		Style textStyle = Style.EMPTY.withBold(false).withColor(Formatting.GOLD);
		enchantCapText.setStyle(textStyle);

		MutableText styledNumber = Text.literal(messageParts[1]);
		textStyle = Style.EMPTY.withBold(true).withColor(Formatting.LIGHT_PURPLE);
		styledNumber.setStyle(textStyle);

		enchantCapText.append(styledNumber);
		return enchantCapText;
	}

	public static Text costInfoText(String message) {
		MutableText costInfoText = Text.literal(message);
		Style textStyle = Style.EMPTY.withColor(Formatting.GREEN);
		costInfoText.setStyle(textStyle);
		return costInfoText;
	}

	public static Text miscInfoText(String message) {
		MutableText miscInfoText = Text.literal(message);
		Style textStyle = Style.EMPTY.withColor(Formatting.AQUA);
		miscInfoText.setStyle(textStyle);
		return miscInfoText;
	}
}
