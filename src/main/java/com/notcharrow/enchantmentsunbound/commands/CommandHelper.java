package com.notcharrow.enchantmentsunbound.commands;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.server.command.ServerCommandSource;

import java.util.List;

public class CommandHelper {
	public static void registerCommands() {
		//ConfigCommand.register();
		ConfigInfoCommand.register();
	}

	public static SuggestionProvider<ServerCommandSource> createSuggestionProvider(List<String> suggestions) {
		return (context, builder) -> {
			for (String suggestion : suggestions) {
				builder.suggest(suggestion);
			}
			return builder.buildFuture();
		};
	}
}
