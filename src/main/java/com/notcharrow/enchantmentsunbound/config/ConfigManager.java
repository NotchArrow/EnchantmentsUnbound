package com.notcharrow.enchantmentsunbound.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final File CONFIG_FILE = new File("config", "enchantmentsunbound.json");

	public static EnchantmentsUnboundConfig config;

	public static void loadConfig() {
		if (!CONFIG_FILE.exists()) {
			config = new EnchantmentsUnboundConfig();
			saveConfig();
			return;
		}
		try (FileReader reader = new FileReader(CONFIG_FILE)) {
			config = GSON.fromJson(reader, EnchantmentsUnboundConfig.class);
		} catch (IOException e) {
			e.printStackTrace();
			config = new EnchantmentsUnboundConfig();
		}
	}

	public static void saveConfig() {
		try {
			CONFIG_FILE.getParentFile().mkdirs();
			try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
				GSON.toJson(config, writer);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void updateEnchantmentLists(DynamicRegistryManager registryManager) {
		Registry<Enchantment> registry = registryManager.getOrThrow(RegistryKeys.ENCHANTMENT);

		for (var entry: registry.getEntrySet()) {
			String id = entry.getKey().getValue().toString();
			if (!ConfigManager.config.enchantmentAnvilCaps.containsKey(id)) {
				if (id.contains("minecraft:")) {
					ConfigManager.config.enchantmentAnvilCaps.put(id, 255);
				} else {
					Enchantment enchantment = entry.getValue();
					ConfigManager.config.enchantmentAnvilCaps.put(id, enchantment.getMaxLevel());
				}
			}
		}
		ConfigManager.saveConfig();
	}
}