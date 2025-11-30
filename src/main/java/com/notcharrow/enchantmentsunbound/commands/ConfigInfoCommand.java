package com.notcharrow.enchantmentsunbound.commands;

import com.mojang.brigadier.context.CommandContext;
import com.notcharrow.enchantmentsunbound.config.ConfigManager;
import com.notcharrow.enchantmentsunbound.helper.TextFormat;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;

import java.util.LinkedHashMap;
import java.util.Map;

import static net.minecraft.server.command.CommandManager.literal;


public class ConfigInfoCommand {
	public static void register() {
		CommandRegistrationCallback.EVENT.register((
				dispatcher, registry, environment) -> {
			dispatcher.register(
				literal("eu")
					.then(literal("configinfo")
						.then(literal("caps")
							.executes(ConfigInfoCommand::executeCaps))
						.then(literal("cost")
							.executes(ConfigInfoCommand::executeCost))
						.then(literal("misc")
							.executes(ConfigInfoCommand::executeMisc))));
		});
	}

	private static int executeCaps(CommandContext<ServerCommandSource> context) {
		Map<String, Integer> capMap = generateCapMap();
		context.getSource().sendMessage(TextFormat.enchantCapText("Enchantment: Custom Level Cap"));

		if (ConfigManager.config.overwriteVanillaEnchants) {
			if (ConfigManager.config.useGlobalVanillaCap) {
				context.getSource().sendMessage(TextFormat.enchantCapText("Global Vanilla Cap: " + ConfigManager.config.customEnchantCap));
			} else {
				for (Map.Entry<String, Integer> entry: capMap.entrySet()) {
					context.getSource().sendMessage(TextFormat.enchantCapText(entry.getKey() +  ": " + entry.getValue()));
				}
			}
		}
		if (ConfigManager.config.overwriteCustomEnchants) {
			context.getSource().sendMessage(TextFormat.enchantCapText("Custom Enchantment Cap: " + ConfigManager.config.customEnchantCap));
		}

		return 1;
	}

	public static Map<String, Integer> generateCapMap() {
		Map<String, Integer> map = new LinkedHashMap<>();
		map.put("Aqua Affinity", ConfigManager.config.aqua_affinity);
		map.put("Bane of Arthropods", ConfigManager.config.bane_of_arthropods);
		map.put("Binding Curse", ConfigManager.config.binding_curse);
		map.put("Blast Protection", ConfigManager.config.blast_protection);
		map.put("Breach", ConfigManager.config.breach);
		map.put("Channeling", ConfigManager.config.channeling);
		map.put("Density", ConfigManager.config.density);
		map.put("Depth Strider", ConfigManager.config.depth_strider);
		map.put("Efficiency", ConfigManager.config.efficiency);
		map.put("Feather Falling", ConfigManager.config.feather_falling);
		map.put("Fire Aspect", ConfigManager.config.fire_aspect);
		map.put("Fire Protection", ConfigManager.config.fire_protection);
		map.put("Flame", ConfigManager.config.flame);
		map.put("Fortune", ConfigManager.config.fortune);
		map.put("Frost Walker", ConfigManager.config.frost_walker);
		map.put("Impaling", ConfigManager.config.impaling);
		map.put("Infinity", ConfigManager.config.infinity);
		map.put("Knockback", ConfigManager.config.knockback);
		map.put("Looting", ConfigManager.config.looting);
		map.put("Loyalty", ConfigManager.config.loyalty);
		map.put("Luck of the Sea", ConfigManager.config.luck_of_the_sea);
		map.put("Lure", ConfigManager.config.lure);
		map.put("Mending", ConfigManager.config.mending);
		map.put("Multishot", ConfigManager.config.multishot);
		map.put("Piercing", ConfigManager.config.piercing);
		map.put("Power", ConfigManager.config.power);
		map.put("Projectile Protection", ConfigManager.config.projectile_protection);
		map.put("Protection", ConfigManager.config.protection);
		map.put("Punch", ConfigManager.config.punch);
		map.put("Quick Charge", ConfigManager.config.quick_charge);
		map.put("Respiration", ConfigManager.config.respiration);
		map.put("Riptide", ConfigManager.config.riptide);
		map.put("Sharpness", ConfigManager.config.sharpness);
		map.put("Silk Touch", ConfigManager.config.silk_touch);
		map.put("Smite", ConfigManager.config.smite);
		map.put("Soul Speed", ConfigManager.config.soul_speed);
		map.put("Sweeping", ConfigManager.config.sweeping);
		map.put("Swift Sneak", ConfigManager.config.swift_sneak);
		map.put("Thorns", ConfigManager.config.thorns);
		map.put("Unbreaking", ConfigManager.config.unbreaking);
		map.put("Vanishing Curse", ConfigManager.config.vanishing_curse);
		map.put("Wind Burst", ConfigManager.config.wind_burst);
		return map;
	}

	private static int executeCost(CommandContext<ServerCommandSource> context) {
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

		map.put("Any enchantment can go on any item", ConfigManager.config.itemEnchantConflicts);
		map.put("Color coded anvil renaming is enabled", ConfigManager.config.colorCodedRenaming);
		return map;
	}
}