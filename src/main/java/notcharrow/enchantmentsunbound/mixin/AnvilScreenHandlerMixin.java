package notcharrow.enchantmentsunbound.mixin;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.*;
import net.minecraft.registry.entry.RegistryEntry;
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

		Item item = leftInput.getItem();
		if (!(item instanceof EnchantedBookItem ||
				item instanceof ArmorItem ||
				item instanceof ToolItem ||
				item instanceof SwordItem ||
				item instanceof BowItem ||
				item instanceof CrossbowItem ||
				item instanceof TridentItem ||
				item instanceof ShieldItem ||
				item instanceof ElytraItem)) {
			return;
		}
		if (!(rightInput.getItem() instanceof EnchantedBookItem)) {
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
				output.addEnchantment(enchantment, entry.getIntValue());
			}
			for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry: leftEnchants.object2IntEntrySet()) {
				RegistryEntry<Enchantment> enchantment = entry.getKey();
				output.addEnchantment(enchantment, entry.getIntValue());
			}
			for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry: rightEnchants.object2IntEntrySet()) {
				RegistryEntry<Enchantment> enchantment = entry.getKey();
				output.addEnchantment(enchantment, entry.getIntValue());
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
}