package notcharrow.enchantmentsunbound.helper;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TextFormat {
	public static Text styledText(String message) {
		MutableText styledText = Text.literal(message);
		Style textStyle = Style.EMPTY.withItalic(true).withColor(Formatting.GREEN);
		styledText.setStyle(textStyle);
		return styledText;
	}
}
