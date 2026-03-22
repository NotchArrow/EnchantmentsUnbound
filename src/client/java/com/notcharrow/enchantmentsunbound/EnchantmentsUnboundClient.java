package com.notcharrow.enchantmentsunbound;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class EnchantmentsUnboundClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		Style textStyle = Style.EMPTY.withColor(Formatting.DARK_PURPLE).withBold(true);

		ResourceLoader.registerBuiltinPack(
				Identifier.of("enchantments-unbound", "enchantment_numbers"),
				FabricLoader.getInstance().getModContainer("enchantments-unbound").orElseThrow(),
				Text.literal("Enchantment Numbers").setStyle(textStyle),
				PackActivationType.NORMAL
		);
	}
}
