package com.notcharrow.enchantmentsunbound;

import com.notcharrow.enchantmentsunbound.commands.CommandHelper;
import com.notcharrow.enchantmentsunbound.config.ConfigManager;
import com.notcharrow.enchantmentsunbound.helper.TextFormat;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AnvilMenu;

public class EnchantmentsUnbound implements ModInitializer {

	@Override
	public void onInitialize() {
		ConfigManager.loadConfig();
		CommandHelper.registerCommands();

		ServerLifecycleEvents.SERVER_STARTED.register(((server) -> {
			ConfigManager.updateEnchantmentLists(server.registryAccess());
		}));
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(((server, resourceManager, b) -> {
			ConfigManager.updateEnchantmentLists(server.registryAccess());
		}));

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if (ConfigManager.config.showActionbarMessage) {
				for (ServerPlayer player : server.getPlayerList().getPlayers()) {
					if (player.containerMenu instanceof AnvilMenu anvil) {
						int cost = anvil.getCost();
						player.sendSystemMessage(TextFormat.styledText("✩ EU ✩ Cost: " + cost), true);
					}
				}
			}
		});
	}
}