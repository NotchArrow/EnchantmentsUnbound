package com.notcharrow.enchantmentsunbound;

import com.notcharrow.enchantmentsunbound.config.ConfigManager;
import com.notcharrow.enchantmentsunbound.helper.TextFormat;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class EnchantmentsUnbound implements ModInitializer {

	@Override
	public void onInitialize() {
		ConfigManager.loadConfig();

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
				if (player.currentScreenHandler instanceof AnvilScreenHandler anvil) {
					int cost = anvil.getLevelCost();
					player.sendMessage(TextFormat.styledText("✩ EU ✩ Cost: " + cost), true);
				}
			}
		});
	}
}