package com.notcharrow.enchantmentsunbound;

import net.fabricmc.api.ModInitializer;
import com.notcharrow.enchantmentsunbound.config.ConfigManager;

public class EnchantmentsUnbound implements ModInitializer {

	@Override
	public void onInitialize() {
		ConfigManager.loadConfig();
	}
}