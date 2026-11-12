package com.notcharrow.enchantmentsunbound;

import com.notcharrow.enchantmentsunbound.config.ConfigManager;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

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
				.setTitle(Component.translatable("text.enchantments-unbound.title"))
				.setSavingRunnable(ConfigManager::saveConfig);

		addGeneralSettings(builder);
		addAnvilCaps(builder);
		addVillagerCaps(builder);
		addEnchantmentCommandCaps(builder);
		addConflictSettings(builder);
		addOtherSettings(builder);

		return builder.build();
	}

	private static void addBoolean(ConfigCategory category, MutableComponent label, MutableComponent tooltip, boolean currentValue, Consumer<Boolean> onSave) {
		category.addEntry(
				ConfigBuilder.create().entryBuilder().startBooleanToggle(label, currentValue)
						.setTooltip(tooltip)
						.setDefaultValue(currentValue)
						.setSaveConsumer(onSave)
						.build()
		);
	}

	private static void addIntField(ConfigCategory category, MutableComponent label, MutableComponent tooltip, int currentValue, Consumer<Integer> onSave, int min, int max) {
		category.addEntry(
				ConfigBuilder.create().entryBuilder().startIntField(label, currentValue)
						.setTooltip(tooltip)
						.setDefaultValue(currentValue)
						.setSaveConsumer(newValue -> {
							if (newValue < min) newValue = min;
							if (newValue > max) newValue = max;
							onSave.accept(newValue);
						})
						.build()
		);
	}

	private static void addDoubleField(ConfigCategory category, MutableComponent label, MutableComponent tooltip, double currentValue, Consumer<Double> onSave, double min, double max) {
		category.addEntry(
				ConfigBuilder.create().entryBuilder().startDoubleField(label, currentValue)
						.setTooltip(tooltip)
						.setDefaultValue(currentValue)
						.setSaveConsumer(newValue -> {
							if (newValue < min) newValue = min;
							if (newValue > max) newValue = max;
							onSave.accept(newValue);
						})
						.build()
		);
	}

	private static void addGeneralSettings(ConfigBuilder builder) {
		ConfigCategory general = builder.getOrCreateCategory(Component.translatable("text.enchantments-unbound.category.general"));

		addBoolean(
				general,
				Component.translatable("text.enchantments-unbound.config.useXpPerEnchantLevel"),
				Component.translatable("text.enchantments-unbound.config.useXpPerEnchantLevel.tooltip"),
				ConfigManager.config.useXpPerEnchantLevel,
				value -> ConfigManager.config.useXpPerEnchantLevel = value
		);

		addIntField(general,
				Component.translatable("text.enchantments-unbound.config.xpPerEnchantLevel"),
				Component.translatable("text.enchantments-unbound.config.xpPerEnchantLevel.tooltip"),
				ConfigManager.config.xpPerEnchantLevel,
				value -> ConfigManager.config.xpPerEnchantLevel = value,
				1, Integer.MAX_VALUE);

		addBoolean(general,
				Component.translatable("text.enchantments-unbound.config.staticCost"),
				Component.translatable("text.enchantments-unbound.config.staticCost.tooltip"),
				ConfigManager.config.staticCost,
				value -> ConfigManager.config.staticCost = value);

		addIntField(general,
				Component.translatable("text.enchantments-unbound.config.levelCost"),
				Component.translatable("text.enchantments-unbound.config.levelCost.tooltip"),
				ConfigManager.config.levelCost,
				value -> ConfigManager.config.levelCost = value,
				1, Integer.MAX_VALUE);

		addDoubleField(general,
				Component.translatable("text.enchantments-unbound.config.levelCostScalingMultiplier"),
				Component.translatable("text.enchantments-unbound.config.levelCostScalingMultiplier.tooltip"),
				ConfigManager.config.levelCostScalingMultiplier,
				value -> ConfigManager.config.levelCostScalingMultiplier = value,
				1, Integer.MAX_VALUE);

		addIntField(general,
				Component.translatable("text.enchantments-unbound.config.maxLevelCost"),
				Component.translatable("text.enchantments-unbound.config.maxLevelCost.tooltip"),
				ConfigManager.config.maxLevelCost,
				value -> ConfigManager.config.maxLevelCost = value,
				1, Integer.MAX_VALUE);
	}

	private static void addAnvilCaps(ConfigBuilder builder) {
		ConfigCategory anvilEnchantCaps = builder.getOrCreateCategory(
				Component.translatable("text.enchantments-unbound.category.anvilEnchantCaps"));
		ConfigEntryBuilder entryBuilder = builder.entryBuilder();

		addBoolean(anvilEnchantCaps,
				Component.translatable("text.enchantments-unbound.config.useCustomAnvilCap"),
				Component.translatable("text.enchantments-unbound.config.useCustomAnvilCap.tooltip"),
				ConfigManager.config.useCustomAnvilCap,
				value -> ConfigManager.config.useCustomAnvilCap = value);

		addBoolean(anvilEnchantCaps,
				Component.translatable("text.enchantments-unbound.config.useGlobalAnvilCap"),
				Component.translatable("text.enchantments-unbound.config.useGlobalAnvilCap.tooltip"),
				ConfigManager.config.useGlobalAnvilCap,
				value -> ConfigManager.config.useGlobalAnvilCap = value);

		addIntField(anvilEnchantCaps,
				Component.translatable("text.enchantments-unbound.config.globalAnvilCap"),
				Component.translatable("text.enchantments-unbound.config.globalAnvilCap.tooltip"),
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

			SubCategoryBuilder subCategory = entryBuilder.startSubCategory(Component.translatable(modId));

			for (Map.Entry<String, Integer> entry : group.getValue()) {
				String id = entry.getKey();

				subCategory.add(entryBuilder.startIntField(Component.translatable(id), entry.getValue())
						.setTooltip(Component.translatable("text.enchantments-unbound.config.enchantmentAnvilCap.tooltip", id))
						.setDefaultValue(255)
						.setMin(1).setMax(255)
						.setSaveConsumer(newValue -> ConfigManager.config.enchantmentAnvilCaps.put(id, newValue))
						.build()
				);
			}

			anvilEnchantCaps.addEntry(subCategory.build());
		}
	}

	private static void addVillagerCaps(ConfigBuilder builder) {
		ConfigCategory villagerEnchantCaps = builder.getOrCreateCategory(
				Component.translatable("text.enchantments-unbound.category.villagerEnchantCaps"));
		ConfigEntryBuilder entryBuilder = builder.entryBuilder();

		addBoolean(villagerEnchantCaps,
				Component.translatable("text.enchantments-unbound.config.modifyVillagers"),
				Component.translatable("text.enchantments-unbound.config.modifyVillagers.tooltip"),
				ConfigManager.config.modifyVillagers,
				value -> ConfigManager.config.modifyVillagers = value);

		addBoolean(villagerEnchantCaps,
				Component.translatable("text.enchantments-unbound.config.useGlobalVillagerCap"),
				Component.translatable("text.enchantments-unbound.config.useGlobalVillagerCap.tooltip"),
				ConfigManager.config.useGlobalVillagerCap,
				value -> ConfigManager.config.useGlobalVillagerCap = value);

		addIntField(villagerEnchantCaps,
				Component.translatable("text.enchantments-unbound.config.globalVillagerCap"),
				Component.translatable("text.enchantments-unbound.config.globalVillagerCap.tooltip"),
				ConfigManager.config.globalVillagerCap,
				value -> ConfigManager.config.globalVillagerCap = value,
				1, 255);

		Map<String, List<Map.Entry<String, Integer>>> groupedEnchants = new TreeMap<>();
		for (Map.Entry<String, Integer> entry : ConfigManager.config.enchantmentVillagerCaps.entrySet()) {
			String namespace = entry.getKey().contains(":") ? entry.getKey().split(":")[0] : "minecraft";
			groupedEnchants.computeIfAbsent(namespace, key -> new ArrayList<>()).add(entry);
		}

		for (Map.Entry<String, List<Map.Entry<String, Integer>>> group : groupedEnchants.entrySet()) {
			String modId = group.getKey();

			SubCategoryBuilder subCategory = entryBuilder.startSubCategory(Component.translatable(modId));

			for (Map.Entry<String, Integer> entry : group.getValue()) {
				String id = entry.getKey();

				subCategory.add(entryBuilder.startIntField(Component.translatable(id), entry.getValue())
						.setTooltip(Component.translatable("text.enchantments-unbound.config.enchantmentVillagerCap.tooltip", id))
						.setDefaultValue(10)
						.setMin(1).setMax(255)
						.setSaveConsumer(newValue -> ConfigManager.config.enchantmentVillagerCaps.put(id, newValue))
						.build()
				);
			}

			villagerEnchantCaps.addEntry(subCategory.build());
		}
	}

	private static void addEnchantmentCommandCaps(ConfigBuilder builder) {
		ConfigCategory enchantmentCommandCaps = builder.getOrCreateCategory(
				Component.translatable("text.enchantments-unbound.category.enchantmentCommandCaps"));
		ConfigEntryBuilder entryBuilder = builder.entryBuilder();

		addBoolean(enchantmentCommandCaps,
				Component.translatable("text.enchantments-unbound.config.useCustomCommandCap"),
				Component.translatable("text.enchantments-unbound.config.useCustomCommandCap.tooltip"),
				ConfigManager.config.useCustomCommandCap,
				value -> ConfigManager.config.useCustomCommandCap = value);

		addBoolean(enchantmentCommandCaps,
				Component.translatable("text.enchantments-unbound.config.useGlobalCommandCap"),
				Component.translatable("text.enchantments-unbound.config.useGlobalCommandCap.tooltip"),
				ConfigManager.config.useGlobalCommandCap,
				value -> ConfigManager.config.useGlobalCommandCap = value);

		addIntField(enchantmentCommandCaps,
				Component.translatable("text.enchantments-unbound.config.globalCommandCap"),
				Component.translatable("text.enchantments-unbound.config.globalCommandCap.tooltip"),
				ConfigManager.config.globalCommandCap,
				value -> ConfigManager.config.globalCommandCap = value,
				1, 255);

		Map<String, List<Map.Entry<String, Integer>>> groupedEnchants = new TreeMap<>();
		for (Map.Entry<String, Integer> entry : ConfigManager.config.enchantmentCommandCaps.entrySet()) {
			String namespace = entry.getKey().contains(":") ? entry.getKey().split(":")[0] : "minecraft";
			groupedEnchants.computeIfAbsent(namespace, key -> new ArrayList<>()).add(entry);
		}

		for (Map.Entry<String, List<Map.Entry<String, Integer>>> group : groupedEnchants.entrySet()) {
			String modId = group.getKey();

			SubCategoryBuilder subCategory = entryBuilder.startSubCategory(Component.translatable(modId));

			for (Map.Entry<String, Integer> entry : group.getValue()) {
				String id = entry.getKey();

				subCategory.add(entryBuilder.startIntField(Component.translatable(id), entry.getValue())
						.setTooltip(Component.translatable("text.enchantments-unbound.config.enchantmentCommandCap.tooltip", id))
						.setDefaultValue(255)
						.setMin(1).setMax(255)
						.setSaveConsumer(newValue -> ConfigManager.config.enchantmentCommandCaps.put(id, newValue))
						.build()
				);
			}

			enchantmentCommandCaps.addEntry(subCategory.build());
		}
	}

	private static void addConflictSettings(ConfigBuilder builder) {
		ConfigCategory conflicts = builder.getOrCreateCategory(
				Component.translatable("text.enchantments-unbound.category.conflicts"));

		addBoolean(conflicts,
				Component.translatable("text.enchantments-unbound.config.damageConflicts"),
				Component.translatable("text.enchantments-unbound.config.damageConflicts.tooltip"),
				ConfigManager.config.damageConflicts,
				value -> ConfigManager.config.damageConflicts = value);

		addBoolean(conflicts,
				Component.translatable("text.enchantments-unbound.config.protectionConflicts"),
				Component.translatable("text.enchantments-unbound.config.protectionConflicts.tooltip"),
				ConfigManager.config.protectionConflicts,
				value -> ConfigManager.config.protectionConflicts = value);

		addBoolean(conflicts,
				Component.translatable("text.enchantments-unbound.config.bowConflicts"),
				Component.translatable("text.enchantments-unbound.config.bowConflicts.tooltip"),
				ConfigManager.config.bowConflicts,
				value -> ConfigManager.config.bowConflicts = value);

		addBoolean(conflicts,
				Component.translatable("text.enchantments-unbound.config.bootConflicts"),
				Component.translatable("text.enchantments-unbound.config.bootConflicts.tooltip"),
				ConfigManager.config.bootConflicts,
				value -> ConfigManager.config.bootConflicts = value);

		addBoolean(conflicts,
				Component.translatable("text.enchantments-unbound.config.tridentConflicts"),
				Component.translatable("text.enchantments-unbound.config.tridentConflicts.tooltip"),
				ConfigManager.config.tridentConflicts,
				value -> ConfigManager.config.tridentConflicts = value);

		addBoolean(conflicts,
				Component.translatable("text.enchantments-unbound.config.crossbowConflicts"),
				Component.translatable("text.enchantments-unbound.config.crossbowConflicts.tooltip"),
				ConfigManager.config.crossbowConflicts,
				value -> ConfigManager.config.crossbowConflicts = value);

		addBoolean(conflicts,
				Component.translatable("text.enchantments-unbound.config.toolConflicts"),
				Component.translatable("text.enchantments-unbound.config.toolConflicts.tooltip"),
				ConfigManager.config.toolConflicts,
				value -> ConfigManager.config.toolConflicts = value);
	}

	private static void addOtherSettings(ConfigBuilder builder) {
		ConfigCategory misc = builder.getOrCreateCategory(
				Component.translatable("text.enchantments-unbound.category.misc"));

		addBoolean(misc,
				Component.translatable("text.enchantments-unbound.config.infiniteAnvilDurability"),
				Component.translatable("text.enchantments-unbound.config.infiniteAnvilDurability.tooltip"),
				ConfigManager.config.infiniteAnvilDurability,
				value -> ConfigManager.config.infiniteAnvilDurability = value);

		addBoolean(misc,
				Component.translatable("text.enchantments-unbound.config.itemEnchantConflicts"),
				Component.translatable("text.enchantments-unbound.config.itemEnchantConflicts.tooltip"),
				ConfigManager.config.itemEnchantConflicts,
				value -> ConfigManager.config.itemEnchantConflicts = value);

		addBoolean(misc,
				Component.translatable("text.enchantments-unbound.config.colorCodedRenaming"),
				Component.translatable("text.enchantments-unbound.config.colorCodedRenaming.tooltip", "&"),
				ConfigManager.config.colorCodedRenaming,
				value -> ConfigManager.config.colorCodedRenaming = value);

		addBoolean(misc,
				Component.translatable("text.enchantments-unbound.config.lowRenamingCost"),
				Component.translatable("text.enchantments-unbound.config.lowRenamingCost.tooltip"),
				ConfigManager.config.lowRenamingCost,
				value -> ConfigManager.config.lowRenamingCost = value);

		addBoolean(misc,
				Component.translatable("text.enchantments-unbound.config.showActionbarMessage"),
				Component.translatable("text.enchantments-unbound.config.showActionbarMessage.tooltip"),
				ConfigManager.config.showActionbarMessage,
				value -> ConfigManager.config.showActionbarMessage = value);

		addBoolean(misc,
				Component.translatable("text.enchantments-unbound.config.showTooltipMessage"),
				Component.translatable("text.enchantments-unbound.config.showTooltipMessage.tooltip"),
				ConfigManager.config.showTooltipMessage,
				value -> ConfigManager.config.showTooltipMessage = value);
	}
}
