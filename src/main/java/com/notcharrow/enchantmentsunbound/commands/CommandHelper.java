package com.notcharrow.enchantmentsunbound.commands;

import com.mojang.brigadier.context.CommandContext;
import com.notcharrow.enchantmentsunbound.helper.TextFormat;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.locale.Language;

import java.util.List;
import java.util.Map;

public class CommandHelper {
	public static void registerCommands() {
		//ConfigCommand.register();
		ConfigInfoCommand.register();
	}

	public static void printPageInfo(CommandContext<CommandSourceStack> context, List<Map.Entry<String, Integer>> capList, int pageNumber) {
		for (int i = 10 * (pageNumber - 1); i < Math.min(capList.size(), 10 * pageNumber); i++) {
			Map.Entry<String, Integer> entry = capList.get(i);

			String id = entry.getKey();
			String namespace = id.split(":")[0];
			String name = id.split(":")[1];
			String translatedName = Language.getInstance().getOrDefault("enchantment." + namespace + "." + name, id);
			int level = entry.getValue();

			context.getSource().sendSystemMessage(
					TextFormat.styledText(translatedName + ": " + level)
			);
		}
	}
}
