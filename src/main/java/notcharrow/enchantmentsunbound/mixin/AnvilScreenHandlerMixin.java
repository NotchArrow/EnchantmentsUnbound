package notcharrow.enchantmentsunbound.mixin;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.Property;
import notcharrow.enchantmentsunbound.config.ConfigManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin {

	@Inject(method = "updateResult", at = @At("HEAD"), cancellable = true)
	private void onUpdateResult(CallbackInfo ci) {
		AnvilScreenHandler self = (AnvilScreenHandler)(Object)this;

		ItemStack leftInput = self.getSlot(0).getStack();
		ItemStack rightInput = self.getSlot(1).getStack();

		if (leftInput.isEmpty() || rightInput.isEmpty()) {
			return;
		}

		if (!(leftInput.getItem() == Items.ENCHANTED_BOOK ||
				leftInput.isIn(ItemTags.DURABILITY_ENCHANTABLE))) {
			return;
		}
		if (!(rightInput.getItem() == Items.ENCHANTED_BOOK ||
				rightInput.isIn(ItemTags.DURABILITY_ENCHANTABLE))) {
			return;
		}

		Object2IntMap<RegistryEntry<Enchantment>> leftEnchants = new Object2IntArrayMap<>();
		Object2IntMap<RegistryEntry<Enchantment>> rightEnchants = new Object2IntArrayMap<>();

		getEnchantments(leftInput, leftEnchants);
		getEnchantments(rightInput, rightEnchants);

		Object2IntMap<RegistryEntry<Enchantment>> outputEnchants = new Object2IntArrayMap<>();

		for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry: leftEnchants.object2IntEntrySet()) {
			RegistryEntry<Enchantment> enchantmentEntry = entry.getKey();
			Enchantment enchantment = enchantmentEntry.value();
			int level = entry.getIntValue();

			for (Object2IntMap.Entry<RegistryEntry<Enchantment>> otherEntry : rightEnchants.object2IntEntrySet()) {
				if (otherEntry.getKey().value() == enchantment && otherEntry.getIntValue() == level) {
					outputEnchants.put(enchantmentEntry, level + 1);
				}
			}
		}

		if (hasOverleveledEnchants(outputEnchants, leftEnchants, rightEnchants)) {
			ItemStack output = leftInput.copy();
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

			self.getSlot(2).setStack(output);

			Property levelCost = ((AnvilScreenHandlerAccessorMixin) self).getLevelCost();
			levelCost.set(Math.max((Math.min(ConfigManager.config.levelCost, 39)), 1)); // get config value, but less than 39 and at least 1

			ci.cancel();
		}
	}

	@Unique
	private static void getEnchantments(ItemStack itemStack, Object2IntMap<RegistryEntry<Enchantment>> enchantments) {
		enchantments.clear();

		if (!itemStack.isEmpty()) {
			Set<Object2IntMap.Entry<RegistryEntry<Enchantment>>> itemEnchantments = itemStack.getItem() == Items.ENCHANTED_BOOK
					? itemStack.getOrDefault(DataComponentTypes.STORED_ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT).getEnchantmentEntries()
					: itemStack.getEnchantments().getEnchantmentEntries();

			for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : itemEnchantments) {
				enchantments.put(entry.getKey(), entry.getIntValue());
			}
		}
	}

	@Unique
	private static boolean hasOverleveledEnchants(Object2IntMap<RegistryEntry<Enchantment>> outputEnchants,
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

	@Unique
	private static int serverHardCap(Object2IntMap.Entry<RegistryEntry<Enchantment>> entry, RegistryEntry<Enchantment> enchantment) {
		String enchantmentID = enchantment.getIdAsString();
		enchantmentID = enchantmentID.replace("minecraft:", "");
		return switch (enchantmentID) {
			case "aqua_affinity" -> ConfigManager.config.aqua_affinity;
			case "bane_of_arthropods" -> ConfigManager.config.bane_of_arthropods;
			case "binding_curse" -> ConfigManager.config.binding_curse;
			case "blast_protection" -> ConfigManager.config.blast_protection;
			case "channeling" -> ConfigManager.config.channeling;
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
			case "sweeping" -> ConfigManager.config.sweeping;
			case "thorns" -> ConfigManager.config.thorns;
			case "unbreaking" -> ConfigManager.config.unbreaking;
			case "vanishing_curse" -> ConfigManager.config.vanishing_curse;
			default -> entry.getKey().value().getMaxLevel();
		};
	}
}