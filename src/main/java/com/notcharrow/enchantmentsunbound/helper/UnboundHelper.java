package com.notcharrow.enchantmentsunbound.helper;

import com.notcharrow.enchantmentsunbound.config.ConfigManager;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.StringHelper;

import java.util.Objects;
import java.util.Set;

public class UnboundHelper {
	public static void getEnchantments(ItemStack itemStack, Object2IntMap<RegistryEntry<Enchantment>> enchantments) {
		enchantments.clear();

		if (!itemStack.isEmpty()) {
			Set<Object2IntMap.Entry<RegistryEntry<Enchantment>>> itemEnchantments = itemStack.getItem() == Items.ENCHANTED_BOOK
					? Objects.requireNonNull(itemStack.get(DataComponentTypes.STORED_ENCHANTMENTS)).getEnchantmentEntries()
					: Objects.requireNonNull(itemStack.get(DataComponentTypes.ENCHANTMENTS)).getEnchantmentEntries();

			for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : itemEnchantments) {
				enchantments.put(entry.getKey(), entry.getIntValue());
			}
		}
	}

	public static boolean hasOverleveledEnchants(Object2IntMap<RegistryEntry<Enchantment>> outputEnchants,
												 Object2IntMap<RegistryEntry<Enchantment>> leftEnchants,
												 Object2IntMap<RegistryEntry<Enchantment>> rightEnchants) {
		boolean overleveled = false;
		for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : outputEnchants.object2IntEntrySet()) {
			Enchantment enchantment = entry.getKey().value();
			int level = entry.getIntValue();
			if (level > enchantment.getMaxLevel()) {
				overleveled = true;
				break;
			}
		}
		for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : leftEnchants.object2IntEntrySet()) {
			Enchantment enchantment = entry.getKey().value();
			int level = entry.getIntValue();
			if (level > enchantment.getMaxLevel()) {
				overleveled = true;
				break;
			}
		}
		for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : rightEnchants.object2IntEntrySet()) {
			Enchantment enchantment = entry.getKey().value();
			int level = entry.getIntValue();
			if (level > enchantment.getMaxLevel()) {
				overleveled = true;
				break;
			}
		}
		return overleveled;
	}

	public static ItemStack createOutput(Object2IntMap<RegistryEntry<Enchantment>> outputEnchants,
										 Object2IntMap<RegistryEntry<Enchantment>> leftEnchants,
										 Object2IntMap<RegistryEntry<Enchantment>> rightEnchants,
										 ItemStack leftInput,
										 String newName) {
		ItemStack output = leftInput.copy();
		if (StringHelper.isBlank(newName)) {
			output.remove(DataComponentTypes.CUSTOM_NAME);
		} else {
			output.set(DataComponentTypes.CUSTOM_NAME, Text.literal(newName));
		}
		for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry: outputEnchants.object2IntEntrySet()) {
			RegistryEntry<Enchantment> enchantment = entry.getKey();
			output.addEnchantment(enchantment, Math.min(entry.getIntValue(), serverHardCap(entry, enchantment)));
		}
		for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry: leftEnchants.object2IntEntrySet()) {
			RegistryEntry<Enchantment> enchantment = entry.getKey();
			output.addEnchantment(enchantment, Math.min(entry.getIntValue(), serverHardCap(entry, enchantment)));
		}
		for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry: rightEnchants.object2IntEntrySet()) {
			RegistryEntry<Enchantment> enchantment = entry.getKey();
			output.addEnchantment(enchantment, Math.min(entry.getIntValue(), serverHardCap(entry, enchantment)));
		}
		return output;
	}

	public static int serverHardCap(Object2IntMap.Entry<RegistryEntry<Enchantment>> entry, RegistryEntry<Enchantment> enchantment) {
		if (!ConfigManager.config.overwriteVanillaEnchants) {
			return enchantment.value().getMaxLevel();
		}
		if (ConfigManager.config.useGlobalVanillaCap) {
			return ConfigManager.config.globalVanillaCap;
		}
		String enchantmentID = enchantment.getIdAsString();
		enchantmentID = enchantmentID.replace("minecraft:", "");
		return switch (enchantmentID) {
			case "aqua_affinity" -> ConfigManager.config.aqua_affinity;
			case "bane_of_arthropods" -> ConfigManager.config.bane_of_arthropods;
			case "binding_curse" -> ConfigManager.config.binding_curse;
			case "blast_protection" -> ConfigManager.config.blast_protection;
			case "breach" -> ConfigManager.config.breach;
			case "channeling" -> ConfigManager.config.channeling;
			case "density" -> ConfigManager.config.density;
			case "depth_strider" -> ConfigManager.config.depth_strider;
			case "efficiency" -> ConfigManager.config.efficiency;
			case "feather_falling" -> ConfigManager.config.feather_falling;
			case "fire_aspect" -> ConfigManager.config.fire_aspect;
			case "fire_protection" -> ConfigManager.config.fire_protection;
			case "flame" -> ConfigManager.config.flame;
			case "fortune" -> ConfigManager.config.fortune;
			case "frost_walker" -> ConfigManager.config.frost_walker;
			case "impaling" -> ConfigManager.config.impaling;
			case "infinity" -> ConfigManager.config.infinity;
			case "knockback" -> ConfigManager.config.knockback;
			case "looting" -> ConfigManager.config.looting;
			case "loyalty" -> ConfigManager.config.loyalty;
			case "luck_of_the_sea" -> ConfigManager.config.luck_of_the_sea;
			case "lure" -> ConfigManager.config.lure;
			case "mending" -> ConfigManager.config.mending;
			case "multishot" -> ConfigManager.config.multishot;
			case "piercing" -> ConfigManager.config.piercing;
			case "power" -> ConfigManager.config.power;
			case "projectile_protection" -> ConfigManager.config.projectile_protection;
			case "protection" -> ConfigManager.config.protection;
			case "punch" -> ConfigManager.config.punch;
			case "quick_charge" -> ConfigManager.config.quick_charge;
			case "respiration" -> ConfigManager.config.respiration;
			case "riptide" -> ConfigManager.config.riptide;
			case "sharpness" -> ConfigManager.config.sharpness;
			case "silk_touch" -> ConfigManager.config.silk_touch;
			case "smite" -> ConfigManager.config.smite;
			case "soul_speed" -> ConfigManager.config.soul_speed;
			case "sweeping_edge" -> ConfigManager.config.sweeping;
			case "swift_sneak" -> ConfigManager.config.swift_sneak;
			case "thorns" -> ConfigManager.config.thorns;
			case "unbreaking" -> ConfigManager.config.unbreaking;
			case "vanishing_curse" -> ConfigManager.config.vanishing_curse;
			case "wind_burst" -> ConfigManager.config.wind_burst;
			default -> ConfigManager.config.overwriteCustomEnchants ? ConfigManager.config.customEnchantCap : entry.getKey().value().getMaxLevel();
		};
	}
}
