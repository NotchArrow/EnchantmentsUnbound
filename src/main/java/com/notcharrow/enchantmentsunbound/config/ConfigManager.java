package com.notcharrow.enchantmentsunbound.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

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

	public static void updateEnchantmentLists(RegistryAccess registryManager) {
		Registry<Enchantment> registry = registryManager.lookupOrThrow(Registries.ENCHANTMENT);

		for (Enchantment enchantment: registry) {
			String id = Objects.requireNonNull(registry.getKey(enchantment)).toString();

			if (!ConfigManager.config.enchantmentAnvilCaps.containsKey(id)) {
				if (id.contains("minecraft:")) {
					ConfigManager.config.enchantmentAnvilCaps.put(id, 255);
				} else {
					ConfigManager.config.enchantmentAnvilCaps.put(id, enchantment.getMaxLevel());
				}
			}

			if (!ConfigManager.config.enchantmentVillagerCaps.containsKey(id)) {
				if (id.contains("minecraft:")) {
					ConfigManager.config.enchantmentVillagerCaps.put(id, 10);
				} else {
					ConfigManager.config.enchantmentVillagerCaps.put(id, enchantment.getMaxLevel());
				}
			}
		}
		ConfigManager.saveConfig();
	}
}