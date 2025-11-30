package com.notcharrow.enchantmentsunbound;

import com.notcharrow.enchantmentsunbound.config.ConfigManager;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class EnchantmentsUnboundModMenu implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return this::createConfigScreen;
	}

	private Screen createConfigScreen(Screen parent) {
		ConfigBuilder builder = ConfigBuilder.create()
				.setParentScreen(parent)
				.setTitle(Text.of("Enchantments Unbound Config"));

		ConfigCategory general = builder.getOrCreateCategory(Text.of("General Settings"));

		addBoolean(general, "Use Level Cost Per Enchantment", "Anvil transaction cost will equal the total enchantment " +
				"level of the item times the cost per level",
				ConfigManager.config.useXpPerEnchantLevel,
				value -> ConfigManager.config.useXpPerEnchantLevel = value);

		addIntField(general, "Level Cost Per Enchantment Level", "The level cost per enchantment present on the anvil output",
				ConfigManager.config.xpPerEnchantLevel,
				value -> ConfigManager.config.xpPerEnchantLevel = value,
				1, Integer.MAX_VALUE);

		addBoolean(general, "Static Cost", "Use static cost instead of vanilla scaling",
				ConfigManager.config.staticCost,
				value -> ConfigManager.config.staticCost = value);

		addIntField(general, "Level Cost", "The level cost if Static Cost is enabled",
				ConfigManager.config.levelCost,
				value -> ConfigManager.config.levelCost = value,
				1, Integer.MAX_VALUE);

		addDoubleField(general, "Scaling Cost Multiplier", "The multiplier to increase the level cost by each transaction " +
						"(vanilla default: 2.0)",
				ConfigManager.config.levelCostScalingMultiplier,
				value -> ConfigManager.config.levelCostScalingMultiplier = value,
				1, Integer.MAX_VALUE);

		addIntField(general, "Max Level Cost", "The maximum amount of levels that a transaction can cost",
				ConfigManager.config.maxLevelCost,
				value -> ConfigManager.config.maxLevelCost = value,
				1, Integer.MAX_VALUE);

		addBoolean(general, "Overwrite Vanilla Enchants", "Overwrite vanilla enchantment max levels",
				ConfigManager.config.overwriteVanillaEnchants,
				value -> ConfigManager.config.overwriteVanillaEnchants = value);

		addBoolean(general, "Use Global Vanilla Cap", "Use global cap for vanilla enchants",
				ConfigManager.config.useGlobalVanillaCap,
				value -> ConfigManager.config.useGlobalVanillaCap = value);

		addIntField(general, "Global Vanilla Cap", "Cap for all vanilla enchantments if enabled",
				ConfigManager.config.globalVanillaCap,
				value -> ConfigManager.config.globalVanillaCap = value,
				1, 255);

		addBoolean(general, "Overwrite Custom Enchants", "Overwrite custom enchantment max levels",
				ConfigManager.config.overwriteCustomEnchants,
				value -> ConfigManager.config.overwriteCustomEnchants = value);

		addIntField(general, "Custom Enchant Cap", "Global custom enchantment cap if enabled",
				ConfigManager.config.customEnchantCap,
				value -> ConfigManager.config.customEnchantCap = value,
				1, 255);


		// Enchant Caps Category
		ConfigCategory enchants = builder.getOrCreateCategory(Text.of("Enchant Caps"));

		addIntField(enchants, "Aqua Affinity", "",
				ConfigManager.config.aqua_affinity,
				value -> ConfigManager.config.aqua_affinity = value,
				1, 255);
		addIntField(enchants, "Bane of Arthropods", "",
				ConfigManager.config.bane_of_arthropods,
				value -> ConfigManager.config.bane_of_arthropods = value,
				1, 255);
		addIntField(enchants, "Binding Curse", "",
				ConfigManager.config.binding_curse,
				value -> ConfigManager.config.binding_curse = value,
				1, 255);
		addIntField(enchants, "Blast Protection", "",
				ConfigManager.config.blast_protection,
				value -> ConfigManager.config.blast_protection = value,
				1, 255);
		addIntField(enchants, "Breach", "",
				ConfigManager.config.breach,
				value -> ConfigManager.config.breach = value,
				1, 255);
		addIntField(enchants, "Channeling", "",
				ConfigManager.config.channeling,
				value -> ConfigManager.config.channeling = value,
				1, 255);
		addIntField(enchants, "Density", "",
				ConfigManager.config.density,
				value -> ConfigManager.config.density = value,
				1, 255);
		addIntField(enchants, "Depth Strider", "",
				ConfigManager.config.depth_strider,
				value -> ConfigManager.config.depth_strider = value,
				1, 255);
		addIntField(enchants, "Efficiency", "",
				ConfigManager.config.efficiency,
				value -> ConfigManager.config.efficiency = value,
				1, 255);
		addIntField(enchants, "Feather Falling", "",
				ConfigManager.config.feather_falling,
				value -> ConfigManager.config.feather_falling = value,
				1, 255);
		addIntField(enchants, "Fire Aspect", "",
				ConfigManager.config.fire_aspect,
				value -> ConfigManager.config.fire_aspect = value,
				1, 255);
		addIntField(enchants, "Fire Protection", "",
				ConfigManager.config.fire_protection,
				value -> ConfigManager.config.fire_protection = value,
				1, 255);
		addIntField(enchants, "Flame", "",
				ConfigManager.config.flame,
				value -> ConfigManager.config.flame = value,
				1, 255);
		addIntField(enchants, "Fortune", "",
				ConfigManager.config.fortune,
				value -> ConfigManager.config.fortune = value,
				1, 255);
		addIntField(enchants, "Frost Walker", "",
				ConfigManager.config.frost_walker,
				value -> ConfigManager.config.frost_walker = value,
				1, 255);
		addIntField(enchants, "Impaling", "",
				ConfigManager.config.impaling,
				value -> ConfigManager.config.impaling = value,
				1, 255);
		addIntField(enchants, "Infinity", "",
				ConfigManager.config.infinity,
				value -> ConfigManager.config.infinity = value,
				1, 255);
		addIntField(enchants, "Knockback", "",
				ConfigManager.config.knockback,
				value -> ConfigManager.config.knockback = value,
				1, 255);
		addIntField(enchants, "Looting", "",
				ConfigManager.config.looting,
				value -> ConfigManager.config.looting = value,
				1, 255);
		addIntField(enchants, "Loyalty", "",
				ConfigManager.config.loyalty,
				value -> ConfigManager.config.loyalty = value,
				1, 255);
		addIntField(enchants, "Luck of the Sea", "",
				ConfigManager.config.luck_of_the_sea,
				value -> ConfigManager.config.luck_of_the_sea = value,
				1, 255);
		addIntField(enchants, "Lure", "",
				ConfigManager.config.lure,
				value -> ConfigManager.config.lure = value,
				1, 255);
		addIntField(enchants, "Mending", "",
				ConfigManager.config.mending,
				value -> ConfigManager.config.mending = value,
				1, 255);
		addIntField(enchants, "Multishot", "",
				ConfigManager.config.multishot,
				value -> ConfigManager.config.multishot = value,
				1, 255);
		addIntField(enchants, "Piercing", "",
				ConfigManager.config.piercing,
				value -> ConfigManager.config.piercing = value,
				1, 255);
		addIntField(enchants, "Power", "",
				ConfigManager.config.power,
				value -> ConfigManager.config.power = value,
				1, 255);
		addIntField(enchants, "Projectile Protection", "",
				ConfigManager.config.projectile_protection,
				value -> ConfigManager.config.projectile_protection = value,
				1, 255);
		addIntField(enchants, "Protection", "",
				ConfigManager.config.protection,
				value -> ConfigManager.config.protection = value,
				1, 255);
		addIntField(enchants, "Punch", "",
				ConfigManager.config.punch,
				value -> ConfigManager.config.punch = value,
				1, 255);
		addIntField(enchants, "Quick Charge", "",
				ConfigManager.config.quick_charge,
				value -> ConfigManager.config.quick_charge = value,
				1, 255);
		addIntField(enchants, "Respiration", "",
				ConfigManager.config.respiration,
				value -> ConfigManager.config.respiration = value,
				1, 255);
		addIntField(enchants, "Riptide", "",
				ConfigManager.config.riptide,
				value -> ConfigManager.config.riptide = value,
				1, 255);
		addIntField(enchants, "Sharpness", "",
				ConfigManager.config.sharpness,
				value -> ConfigManager.config.sharpness = value,
				1, 255);
		addIntField(enchants, "Silk Touch", "",
				ConfigManager.config.silk_touch,
				value -> ConfigManager.config.silk_touch = value,
				1, 255);
		addIntField(enchants, "Smite", "",
				ConfigManager.config.smite,
				value -> ConfigManager.config.smite = value,
				1, 255);
		addIntField(enchants, "Soul Speed", "",
				ConfigManager.config.soul_speed,
				value -> ConfigManager.config.soul_speed = value,
				1, 255);
		addIntField(enchants, "Sweeping", "",
				ConfigManager.config.sweeping,
				value -> ConfigManager.config.sweeping = value,
				1, 255);
		addIntField(enchants, "Swift Sneak", "",
				ConfigManager.config.swift_sneak,
				value -> ConfigManager.config.swift_sneak = value,
				1, 255);
		addIntField(enchants, "Thorns", "",
				ConfigManager.config.thorns,
				value -> ConfigManager.config.thorns = value,
				1, 255);
		addIntField(enchants, "Unbreaking", "",
				ConfigManager.config.unbreaking,
				value -> ConfigManager.config.unbreaking = value,
				1, 255);
		addIntField(enchants, "Vanishing Curse", "",
				ConfigManager.config.vanishing_curse,
				value -> ConfigManager.config.vanishing_curse = value,
				1, 255);
		addIntField(enchants, "Wind Burst", "",
				ConfigManager.config.wind_burst,
				value -> ConfigManager.config.wind_burst = value,
				1, 255);


		ConfigCategory conflicts = builder.getOrCreateCategory(Text.of("Enchant Conflicts"));

		addBoolean(conflicts, "Damage Enchants No Conflict", "Sharpness, Smite, and Bane of Arthropods aren't exclusive",
				ConfigManager.config.damageConflicts,
				value -> ConfigManager.config.damageConflicts = value);

		addBoolean(conflicts, "Protection Enchants No Conflict", "Protection, Blast, Projectile, and Fire Protection aren't exclusive",
				ConfigManager.config.protectionConflicts,
				value -> ConfigManager.config.protectionConflicts = value);

		addBoolean(conflicts, "Bow Enchants No Conflict", "Infinity and Mending aren't exclusive",
				ConfigManager.config.bowConflicts,
				value -> ConfigManager.config.bowConflicts = value);

		addBoolean(conflicts, "Boot Enchants No Conflict", "Depth Strider and Frost Walker aren't exclusive",
				ConfigManager.config.bootConflicts,
				value -> ConfigManager.config.bootConflicts = value);

		addBoolean(conflicts, "Trident Enchants No Conflict", "Riptide doesn't conflict with Channeling and Loyalty",
				ConfigManager.config.tridentConflicts,
				value -> ConfigManager.config.tridentConflicts = value);

		addBoolean(conflicts, "Crossbow Enchants No Conflict", "Multishot and Piercing aren't exclusive",
				ConfigManager.config.crossbowConflicts,
				value -> ConfigManager.config.crossbowConflicts = value);

		addBoolean(conflicts, "Tool Enchants No Conflict", "Silk Touch and Fortune aren't exclusive",
				ConfigManager.config.toolConflicts,
				value -> ConfigManager.config.toolConflicts = value);


		ConfigCategory misc = builder.getOrCreateCategory(Text.of("Other Settings"));

		addBoolean(misc, "Enchantment Exclusivity", "Only allow tool enchantments on tools, armor enchantments on armor, etc.",
				ConfigManager.config.itemEnchantConflicts,
				value -> ConfigManager.config.itemEnchantConflicts = value);

		addBoolean(misc, "Color Codes in Anvil Renaming", "Allow item names to use color codes using the '&' symbol",
				ConfigManager.config.colorCodedRenaming,
				value -> ConfigManager.config.colorCodedRenaming = value);

		addBoolean(misc, "1 Level Renaming Cost", "Cap item renaming cost at 1 experience level",
				ConfigManager.config.lowRenamingCost,
				value -> ConfigManager.config.lowRenamingCost = value);

		addBoolean(misc, "Actionbar Cost Display", "Should the level cost of transactions display above the hotbar",
				ConfigManager.config.showActionbarMessage,
				value -> ConfigManager.config.showActionbarMessage = value);

		addBoolean(misc, "Tooltip Cost Display", "Should the level cost of transactions display in the item tooltip when above 39",
				ConfigManager.config.showTooltipMessage,
				value -> ConfigManager.config.showTooltipMessage = value);

		return builder.build();
	}

	private void addBoolean(ConfigCategory category, String label, String tooltip, boolean currentValue, Consumer<Boolean> onSave) {
		category.addEntry(
				ConfigBuilder.create().entryBuilder().startBooleanToggle(Text.of(label), currentValue)
						.setTooltip(Text.of(tooltip))
						.setDefaultValue(currentValue)
						.setSaveConsumer(onSave)
						.build()
		);
	}

	private void addIntField(ConfigCategory category, String label, String tooltip, int currentValue, Consumer<Integer> onSave, int min, int max) {
		category.addEntry(
				ConfigBuilder.create().entryBuilder().startIntField(Text.of(label), currentValue)
						.setTooltip(Text.of(tooltip))
						.setDefaultValue(currentValue)
						.setSaveConsumer(newValue -> {
							if (newValue < min) newValue = min;
							if (newValue > max) newValue = max;
							onSave.accept(newValue);
							ConfigManager.saveConfig();
						})
						.build()
		);
	}

	private void addDoubleField(ConfigCategory category, String label, String tooltip, double currentValue, Consumer<Double> onSave, double min, double max) {
		category.addEntry(
				ConfigBuilder.create().entryBuilder().startDoubleField(Text.of(label), currentValue)
						.setTooltip(Text.of(tooltip))
						.setDefaultValue(currentValue)
						.setSaveConsumer(newValue -> {
							if (newValue < min) newValue = min;
							if (newValue > max) newValue = max;
							onSave.accept(newValue);
							ConfigManager.saveConfig();
						})
						.build()
		);
	}
}
