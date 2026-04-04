package com.notcharrow.enchantmentsunbound;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;

public class EnchantmentsUnboundClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		Style textStyle = Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE).withBold(true);

		ResourceLoader.registerBuiltinPack(
				Identifier.fromNamespaceAndPath("enchantments-unbound", "enchantment_numbers"),
				FabricLoader.getInstance().getModContainer("enchantments-unbound").orElseThrow(),
				Component.literal("Enchantment Numbers").setStyle(textStyle),
				PackActivationType.NORMAL
		);
	}
}
