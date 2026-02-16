package com.notcharrow.enchantmentsunbound;

import com.notcharrow.enchantmentsunbound.config.ConfigManager;
import com.notcharrow.enchantmentsunbound.helper.UnboundHelper;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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

		addGeneralSettings(builder);
		addAnvilCaps(builder);
		addConflictSettings(builder);
		addOtherSettings(builder);

		return builder.build();
	}

	private static void addBoolean(ConfigCategory category, String label, String tooltip, boolean currentValue, Consumer<Boolean> onSave) {
		category.addEntry(
				ConfigBuilder.create().entryBuilder().startBooleanToggle(Text.of(label), currentValue)
						.setTooltip(Text.of(tooltip))
						.setDefaultValue(currentValue)
						.setSaveConsumer(onSave)
						.build()
		);
	}

	private static void addIntField(ConfigCategory category, String label, String tooltip, int currentValue, Consumer<Integer> onSave, int min, int max) {
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

	private static void addDoubleField(ConfigCategory category, String label, String tooltip, double currentValue, Consumer<Double> onSave, double min, double max) {
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

	private static void addGeneralSettings(ConfigBuilder builder) {
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
	}

	private static void addAnvilCaps(ConfigBuilder builder) {
		ConfigCategory anvilEnchantCaps = builder.getOrCreateCategory(Text.of("Anvil Enchant Caps"));
		ConfigEntryBuilder entryBuilder = builder.entryBuilder();

		addBoolean(anvilEnchantCaps, "Overwrite Anvil Enchantment Caps", "Overwrite anvil enchantment max levels",
				ConfigManager.config.useCustomAnvilCap,
				value -> ConfigManager.config.useCustomAnvilCap = value);

		addBoolean(anvilEnchantCaps, "Use Global Anvil Cap", "Use global cap for anvil enchants",
				ConfigManager.config.useGlobalAnvilCap,
				value -> ConfigManager.config.useGlobalAnvilCap = value);

		addIntField(anvilEnchantCaps, "Global Anvil Cap", "Cap for all anvil enchantments if enabled",
				ConfigManager.config.globalAnvilCap,
				value -> ConfigManager.config.globalAnvilCap = value,
				1, 255);

		Map<String, List<Map.Entry<String, Integer>>> groupedEnchants = new TreeMap<>();
		for (Map.Entry<String, Integer> entry : ConfigManager.config.enchantmentAnvilCaps.entrySet()) {
			String namespace = entry.getKey().contains(":") ? entry.getKey().split(":")[0] : "minecraft";
			groupedEnchants.computeIfAbsent(namespace, key -> new ArrayList<>()).add(entry);
		}

		for (Map.Entry<String, List<Map.Entry<String, Integer>>> group : groupedEnchants.entrySet()) {
			String modId = group.getKey();
			String categoryName = modId.equals("minecraft") ? "Vanilla" : UnboundHelper.formatIdToName(modId);

			SubCategoryBuilder subCategory = entryBuilder.startSubCategory(Text.of(categoryName));

			for (Map.Entry<String, Integer> entry : group.getValue()) {
				String id = entry.getKey();
				String name = UnboundHelper.formatIdToName(id);

				subCategory.add(entryBuilder.startIntField(Text.of(name), entry.getValue())
						.setTooltip(Text.of("Enchantment Level Cap in the Anvil (" + id + ")"))
						.setDefaultValue(255)
						.setMin(1).setMax(255)
						.setSaveConsumer(newValue -> ConfigManager.config.enchantmentAnvilCaps.put(id, newValue))
						.build()
				);
			}

			anvilEnchantCaps.addEntry(subCategory.build());
		}
	}

	private static void addConflictSettings(ConfigBuilder builder) {
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
	}

	private static void addOtherSettings(ConfigBuilder builder) {
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
	}
}
