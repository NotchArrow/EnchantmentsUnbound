package com.notcharrow.enchantmentsunbound.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.notcharrow.enchantmentsunbound.config.ConfigManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import java.util.*;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;


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
						.then(literal("villagercaps")
							.then(argument("Page Number", IntegerArgumentType.integer())
								.executes(ConfigInfoCommand::executeVillagerCaps)))
						.then(literal("enchantcaps")
							.then(argument("Page Number", IntegerArgumentType.integer())
								.executes(ConfigInfoCommand::executeCommandCaps)))
						.then(literal("anvilcost")
							.executes(ConfigInfoCommand::executeAnvilCost))
						.then(literal("misc")
							.executes(ConfigInfoCommand::executeMisc))));
		});
	}

	private static int executeAnvilCaps(CommandContext<CommandSourceStack> context) {
		TreeMap<String, Integer> capMap = ConfigManager.config.enchantmentAnvilCaps;
		List<Map.Entry<String, Integer>> capList = new ArrayList<>(capMap.entrySet());
		int pageNumber = IntegerArgumentType.getInteger(context, "Page Number");
		int totalPages = (int) Math.ceil(capList.size() / 10.0);
		pageNumber = Math.clamp(pageNumber, 1, totalPages);

		context.getSource().sendSystemMessage(
				Component.translatable("text.enchantments-unbound.command.anvilCaps.title", pageNumber, totalPages)
						.withStyle(ChatFormatting.YELLOW, ChatFormatting.UNDERLINE)
		);

		if (ConfigManager.config.useCustomAnvilCap) {
			if (ConfigManager.config.useGlobalAnvilCap) {
				context.getSource().sendSystemMessage(
						Component.translatable("text.enchantments-unbound.command.anvilCaps.global",
								ConfigManager.config.globalAnvilCap).withStyle(ChatFormatting.YELLOW)
				);
			} else {
				CommandHelper.printPageInfo(context, capList, pageNumber);
			}
		} else {
			context.getSource().sendSystemMessage(
					Component.translatable("text.enchantments-unbound.command.anvilCaps.disabled").withStyle(ChatFormatting.YELLOW)
			);
		}

		return 1;
	}

	private static int executeVillagerCaps(CommandContext<CommandSourceStack> context) {
		TreeMap<String, Integer> capMap = ConfigManager.config.enchantmentVillagerCaps;
		List<Map.Entry<String, Integer>> capList = new ArrayList<>(capMap.entrySet());
		int pageNumber = IntegerArgumentType.getInteger(context, "Page Number");
		int totalPages = (int) Math.ceil(capList.size() / 10.0);
		pageNumber = Math.clamp(pageNumber, 1, totalPages);

		context.getSource().sendSystemMessage(
				Component.translatable("text.enchantments-unbound.command.villagerCaps.title", pageNumber, totalPages)
						.withStyle(ChatFormatting.YELLOW, ChatFormatting.UNDERLINE)
		);

		if (ConfigManager.config.modifyVillagers) {
			if (ConfigManager.config.useGlobalVillagerCap) {
				context.getSource().sendSystemMessage(
						Component.translatable("text.enchantments-unbound.command.villagerCaps.global",
								ConfigManager.config.globalVillagerCap).withStyle(ChatFormatting.YELLOW)
				);
			} else {
				CommandHelper.printPageInfo(context, capList, pageNumber);
			}
		} else {
			context.getSource().sendSystemMessage(
					Component.translatable("text.enchantments-unbound.command.villagerCaps.disabled").withStyle(ChatFormatting.YELLOW)
			);
		}

		return 1;
	}

	private static int executeCommandCaps(CommandContext<CommandSourceStack> context) {
		TreeMap<String, Integer> capMap = ConfigManager.config.enchantmentCommandCaps;
		List<Map.Entry<String, Integer>> capList = new ArrayList<>(capMap.entrySet());
		int pageNumber = IntegerArgumentType.getInteger(context, "Page Number");
		int totalPages = (int) Math.ceil(capList.size() / 10.0);
		pageNumber = Math.clamp(pageNumber, 1, totalPages);

		context.getSource().sendSystemMessage(
				Component.translatable("text.enchantments-unbound.command.commandCaps.title", pageNumber, totalPages)
						.withStyle(ChatFormatting.YELLOW, ChatFormatting.UNDERLINE)
		);

		if (ConfigManager.config.useCustomCommandCap) {
			if (ConfigManager.config.useGlobalCommandCap) {
				context.getSource().sendSystemMessage(
						Component.translatable("text.enchantments-unbound.command.commandCaps.global",
								ConfigManager.config.globalCommandCap).withStyle(ChatFormatting.YELLOW)
				);
			} else {
				CommandHelper.printPageInfo(context, capList, pageNumber);
			}
		} else {
			context.getSource().sendSystemMessage(
					Component.translatable("text.enchantments-unbound.command.commandCaps.disabled").withStyle(ChatFormatting.YELLOW)
			);
		}

		return 1;
	}

	private static int executeAnvilCost(CommandContext<CommandSourceStack> context) {
		if (ConfigManager.config.staticCost) {
			context.getSource().sendSystemMessage(
					Component.translatable("text.enchantments-unbound.command.anvilCost.static",
							ConfigManager.config.levelCost).withStyle(ChatFormatting.YELLOW)
			);
		} else if (ConfigManager.config.useXpPerEnchantLevel) {
			context.getSource().sendSystemMessage(
					Component.translatable("text.enchantments-unbound.command.anvilCost.perEnchant",
							ConfigManager.config.xpPerEnchantLevel).withStyle(ChatFormatting.YELLOW)
			);

			context.getSource().sendSystemMessage(
					Component.translatable("text.enchantments-unbound.command.anvilCost.example",
							ConfigManager.config.xpPerEnchantLevel,
							10 * ConfigManager.config.xpPerEnchantLevel).withStyle(ChatFormatting.YELLOW)
			);
		} else {
			context.getSource().sendSystemMessage(
					Component.translatable("text.enchantments-unbound.command.anvilCost.scaling",
							ConfigManager.config.levelCostScalingMultiplier,
							ConfigManager.config.maxLevelCost).withStyle(ChatFormatting.YELLOW)
			);
		}

		return 1;
	}

	private static int executeMisc(CommandContext<CommandSourceStack> context) {
		Map<String, Boolean> miscMap = generateMiscMap();

		for (Map.Entry<String, Boolean> entry : miscMap.entrySet()) {
			if (entry.getValue()) {
				context.getSource().sendSystemMessage(Component.translatable(entry.getKey()).withStyle(ChatFormatting.YELLOW));
			}
		}

		return 1;
	}

	private static Map<String, Boolean> generateMiscMap() {
		Map<String, Boolean> map = new LinkedHashMap<>();

		map.put("text.enchantments-unbound.command.misc.damageConflicts", ConfigManager.config.damageConflicts);
		map.put("text.enchantments-unbound.command.misc.protectionConflicts", ConfigManager.config.protectionConflicts);
		map.put("text.enchantments-unbound.command.misc.bowConflicts", ConfigManager.config.bowConflicts);
		map.put("text.enchantments-unbound.command.misc.bootConflicts", ConfigManager.config.bootConflicts);
		map.put("text.enchantments-unbound.command.misc.tridentConflicts", ConfigManager.config.tridentConflicts);
		map.put("text.enchantments-unbound.command.misc.crossbowConflicts", ConfigManager.config.crossbowConflicts);
		map.put("text.enchantments-unbound.command.misc.toolConflicts", ConfigManager.config.toolConflicts);

		map.put("text.enchantments-unbound.command.misc.anyItem", !ConfigManager.config.itemEnchantConflicts);
		map.put("text.enchantments-unbound.command.misc.colorRenaming", ConfigManager.config.colorCodedRenaming);
		map.put("text.enchantments-unbound.command.misc.infiniteDurability", ConfigManager.config.infiniteAnvilDurability);

		return map;
	}
}