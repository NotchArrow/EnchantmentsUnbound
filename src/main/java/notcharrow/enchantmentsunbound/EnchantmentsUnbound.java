package notcharrow.enchantmentsunbound;

import net.fabricmc.api.ModInitializer;

import notcharrow.enchantmentsunbound.config.ConfigManager;

public class EnchantmentsUnbound implements ModInitializer {

	@Override
	public void onInitialize() {
		ConfigManager.loadConfig();
	}
}