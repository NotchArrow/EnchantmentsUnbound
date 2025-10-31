package com.notcharrow.enchantmentsunbound.helper;

import com.notcharrow.enchantmentsunbound.config.ConfigManager;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.text.Text;
import net.minecraft.util.StringHelper;

import java.util.ArrayList;
import java.util.List;
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

	public static ItemStack createOutput(
			Object2IntMap<RegistryEntry<Enchantment>> outputEnchants,
			Object2IntMap<RegistryEntry<Enchantment>> leftEnchants,
			Object2IntMap<RegistryEntry<Enchantment>> rightEnchants,
			ItemStack leftInput,
			String newName
	) {
		ItemStack output = leftInput.copy();
		EnchantmentHelper.apply(output, (components) ->
				components.remove((enchantment) -> true));

		if (StringHelper.isBlank(newName)) {
			output.remove(DataComponentTypes.CUSTOM_NAME);
		} else {
			output.set(DataComponentTypes.CUSTOM_NAME, Text.literal(newName));
		}

		Object2IntMap<RegistryEntry<Enchantment>> combined = new Object2IntArrayMap<>();

		combined.putAll(leftEnchants);
		combined.putAll(rightEnchants);
		combined.putAll(outputEnchants);

		List<RegistryEntry<Enchantment>> toRemove = new ArrayList<>();
		List<RegistryEntry<Enchantment>> enchants = new ArrayList<>(combined.keySet());

		for (int i = 0; i < enchants.size(); i++) {
			RegistryEntry<Enchantment> e1 = enchants.get(i);
			for (int j = i + 1; j < enchants.size(); j++) {
				RegistryEntry<Enchantment> e2 = enchants.get(j);
				if (!canCombineAccordingToGroups(e1, e2)) {
					// Remove the second one in the conflict pair
					toRemove.add(e2);
				}
			}
		}

		for (RegistryEntry<Enchantment> rem : toRemove) {
			combined.remove(rem);
		}

		for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry: combined.object2IntEntrySet()) {
			RegistryEntry<Enchantment> enchantment = entry.getKey();
			if (!ConfigManager.config.itemEnchantConflicts || output.canBeEnchantedWith(enchantment, EnchantingContext.ACCEPTABLE)
			|| output.getItem() == Items.ENCHANTED_BOOK) {
				output.addEnchantment(enchantment, Math.min(entry.getIntValue(), serverHardCap(entry, enchantment)));
			}
		}

		return output;
	}

	private static boolean canCombineAccordingToGroups(RegistryEntry<Enchantment> a, RegistryEntry<Enchantment> b) {
		String id1 = a.getIdAsString().replace("minecraft:", "");
		String id2 = b.getIdAsString().replace("minecraft:", "");

		// Damage group
		if (in(id1, "sharpness", "smite", "bane_of_arthropods") &&
				in(id2, "sharpness", "smite", "bane_of_arthropods")) {
			return ConfigManager.config.damageConflicts;
		}

		// Protection group
		if (in(id1, "protection", "blast_protection", "projectile_protection", "fire_protection") &&
				in(id2, "protection", "blast_protection", "projectile_protection", "fire_protection")) {
			return ConfigManager.config.protectionConflicts;
		}

		// Boots
		if (in(id1, "depth_strider", "frost_walker") &&
				in(id2, "depth_strider", "frost_walker")) {
			return ConfigManager.config.bootConflicts;
		}

		// Bow
		if (in(id1, "infinity", "mending") && in(id2, "infinity", "mending")) {
			return ConfigManager.config.bowConflicts;
		}

		// Trident
		if (id1.equals("riptide") && in(id2, "channeling", "loyalty")
				|| id2.equals("riptide") && in(id1, "channeling", "loyalty")) {
			return ConfigManager.config.tridentConflicts;
		}

		// Crossbow
		if (in(id1, "multishot", "piercing") && in(id2, "multishot", "piercing")) {
			return ConfigManager.config.crossbowConflicts;
		}

		// Tools
		if (in(id1, "silk_touch", "fortune") && in(id2, "silk_touch", "fortune")) {
			return ConfigManager.config.toolConflicts;
		}

		return true;
	}

	private static boolean in(String id, String... options) {
		for (String opt : options) if (id.equals(opt)) return true;
		return false;
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
