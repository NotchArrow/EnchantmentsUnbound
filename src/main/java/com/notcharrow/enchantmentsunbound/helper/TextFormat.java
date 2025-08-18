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
}
