package com.notcharrow.enchantmentsunbound.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.notcharrow.enchantmentsunbound.config.ConfigManager;
import com.notcharrow.enchantmentsunbound.helper.TextFormat;
import com.notcharrow.enchantmentsunbound.helper.UnboundHelper;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;

import java.util.*;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class ConfigInfoCommand {
	public static void register() {
		CommandRegistrationCallback.EVENT.register((
				dispatcher, registry, environment) -> {
			dispatcher.register(
				literal("eu")
					.then(literal("configinfo")
						.then(literal("anvilcaps")
							.then(argument("Page Number", IntegerArgumentType.integer())
								.executes(ConfigInfoCommand::executeAnvilCaps)))
						.then(literal("anvilcost")
							.executes(ConfigInfoCommand::executeAnvilCost))
						.then(literal("misc")
							.executes(ConfigInfoCommand::executeMisc))));
		});
	}

	private static int executeAnvilCaps(CommandContext<ServerCommandSource> context) {
		TreeMap<String, Integer> capMap = ConfigManager.config.enchantmentAnvilCaps;
		List<Map.Entry<String, Integer>> capList = new ArrayList<>(capMap.entrySet());
		int pageNumber = IntegerArgumentType.getInteger(context, "Page Number");
		int totalPages = (int) Math.ceil(capList.size() / 10.0);
		pageNumber = Math.clamp(pageNumber, 1, totalPages);

		context.getSource().sendMessage(TextFormat.enchantCapText("Enchantment: Custom Anvil Level Cap (Page " + pageNumber + " / " + totalPages + ")"));

		if (ConfigManager.config.useCustomAnvilCap) {
			if (ConfigManager.config.useGlobalAnvilCap) {
				context.getSource().sendMessage(TextFormat.enchantCapText("Global Anvil Cap: " + ConfigManager.config.globalAnvilCap));
			} else {
				for (int i = 10 * (pageNumber - 1); i < Math.min(capList.size(), 10 * pageNumber); i++) {
					var entry = capList.get(i);
					String name = UnboundHelper.formatIdToName(entry.getKey());
					String modId = entry.getKey().split(":")[0];
					if (!Objects.equals(modId, "minecraft")) {
						name = "(" + UnboundHelper.formatIdToName(modId) + ") " + name;
					}
					context.getSource().sendMessage(TextFormat.enchantCapText(name +  ": " + entry.getValue()));
				}
			}
		}

		return 1;
	}

	private static int executeAnvilCost(CommandContext<ServerCommandSource> context) {
		if (ConfigManager.config.staticCost) {
			context.getSource().sendMessage(TextFormat.costInfoText("All anvil transactions cost " +
					ConfigManager.config.levelCost + " levels."));
		} else if (ConfigManager.config.useXpPerEnchantLevel) {
			context.getSource().sendMessage(TextFormat.costInfoText("Anvil transactions will cost " + ConfigManager.config.xpPerEnchantLevel +
					" levels per enchantment level on the item."));
			context.getSource().sendMessage(TextFormat.costInfoText("Ex: Sharp VI, Unbreaking IV = 10 x " + ConfigManager.config.xpPerEnchantLevel +
					" levels = " + 10 * ConfigManager.config.xpPerEnchantLevel + " levels."));
		} else {
			context.getSource().sendMessage(TextFormat.costInfoText("Anvil transaction cost increases by " +
					ConfigManager.config.levelCostScalingMultiplier + "x every transaction, with a maximum cost of " +
					ConfigManager.config.maxLevelCost + " levels."));
		}

		return 1;
	}

	private static int executeMisc(CommandContext<ServerCommandSource> context) {
		Map<String, Boolean> miscMap = generateMiscMap();

		for (Map.Entry<String, Boolean> entry: miscMap.entrySet()) {
			if (entry.getValue()) {
				context.getSource().sendMessage(TextFormat.miscInfoText(entry.getKey()));
			}
		}
		return 1;
	}

	private static Map<String, Boolean> generateMiscMap() {
		Map<String, Boolean> map = new LinkedHashMap<>();
		map.put("Damage enchantment conflicts are removed", ConfigManager.config.damageConflicts);
		map.put("Protection enchantment conflicts are removed", ConfigManager.config.protectionConflicts);
		map.put("Bow enchantment conflicts are removed", ConfigManager.config.bowConflicts);
		map.put("Boot enchantment conflicts are removed", ConfigManager.config.bootConflicts);
		map.put("Trident enchantment conflicts are removed", ConfigManager.config.tridentConflicts);
		map.put("Crossbow enchantment conflicts are removed", ConfigManager.config.crossbowConflicts);
		map.put("Tool enchantment conflicts are removed", ConfigManager.config.toolConflicts);

		map.put("Any enchantment can go on any item", !ConfigManager.config.itemEnchantConflicts);
		map.put("Color coded anvil renaming is enabled", ConfigManager.config.colorCodedRenaming);
		return map;
	}
}