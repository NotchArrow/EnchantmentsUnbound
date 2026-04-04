package com.notcharrow.enchantmentsunbound.helper;

import com.notcharrow.enchantmentsunbound.config.ConfigManager;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class UnboundHelper {

	public static Object2IntMap<Holder<Enchantment>> combined = new Object2IntArrayMap<>();

	public static void getEnchantments(ItemStack itemStack, Object2IntMap<Holder<Enchantment>> enchantments) {
		enchantments.clear();

		if (!itemStack.isEmpty()) {
			Set<Object2IntMap.Entry<Holder<Enchantment>>> itemEnchantments = itemStack.getItem() == Items.ENCHANTED_BOOK
					? Objects.requireNonNull(itemStack.get(DataComponents.STORED_ENCHANTMENTS)).entrySet()
					: Objects.requireNonNull(itemStack.get(DataComponents.ENCHANTMENTS)).entrySet();

			for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments) {
				enchantments.put(entry.getKey(), entry.getIntValue());
			}
		}
	}

	public static ItemStack createOutput(
			Object2IntMap<Holder<Enchantment>> outputEnchants,
			Object2IntMap<Holder<Enchantment>> leftEnchants,
			Object2IntMap<Holder<Enchantment>> rightEnchants,
			ItemStack leftInput,
			String newName
	) {
		ItemStack output = leftInput.copy();

		if (StringUtil.isBlank(newName)) {
			output.remove(DataComponents.CUSTOM_NAME);
		} else {
			output.set(DataComponents.CUSTOM_NAME, Component.literal(newName));
		}

		combined = new Object2IntArrayMap<>();

		for (Object2IntMap<Holder<Enchantment>> map : List.of(leftEnchants, rightEnchants, outputEnchants)) {
			for (var entry : map.object2IntEntrySet()) {
				combined.merge(entry.getKey(), entry.getIntValue(), Integer::max);
			}
		}

		List<Holder<Enchantment>> toRemove = new ArrayList<>();
		List<Holder<Enchantment>> enchants = new ArrayList<>(combined.keySet());

		for (int i = 0; i < enchants.size(); i++) {
			Holder<Enchantment> e1 = enchants.get(i);
			for (int j = i + 1; j < enchants.size(); j++) {
				Holder<Enchantment> e2 = enchants.get(j);
				if (!canCombineAccordingToGroups(e1, e2)) {
					// Remove the second one in the conflict pair
					toRemove.add(e2);
				}
			}
		}

		for (Holder<Enchantment> rem : toRemove) {
			combined.remove(rem);
		}

		for (Object2IntMap.Entry<Holder<Enchantment>> entry: combined.object2IntEntrySet()) {
			Holder<Enchantment> enchantment = entry.getKey();
			if (!ConfigManager.config.itemEnchantConflicts || output.canBeEnchantedWith(enchantment, EnchantingContext.ACCEPTABLE)
			|| output.getItem() == Items.ENCHANTED_BOOK) {
				output.enchant(enchantment, Math.min(entry.getIntValue(), getAnvilCap(enchantment)));
			}
		}

		return output;
	}

	private static boolean canCombineAccordingToGroups(Holder<Enchantment> a, Holder<Enchantment> b) {
		String id1 = a.getRegisteredName().replace("minecraft:", "");
		String id2 = b.getRegisteredName().replace("minecraft:", "");

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

	public static int getAnvilCap(Holder<Enchantment> enchantment) {
		if (!ConfigManager.config.useCustomAnvilCap) {
			return enchantment.value().getMaxLevel();
		}
		if (ConfigManager.config.useGlobalAnvilCap) {
			return ConfigManager.config.globalAnvilCap;
		}

		return ConfigManager.config.enchantmentAnvilCaps.getOrDefault(enchantment.getRegisteredName(), enchantment.value().getMaxLevel());
	}

	public static String formatIdToName(String id) {
		String path = id.contains(":") ? id.split(":")[1] : id;
		String[] words = path.split("_");
		StringBuilder sb = new StringBuilder();
		for (String word : words) {
			if (!word.isEmpty()) {
				sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
			}
		}
		return sb.toString().trim();
	}
}
