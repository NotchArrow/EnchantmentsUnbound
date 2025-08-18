package com.notcharrow.enchantmentsunbound.mixin;

import com.notcharrow.enchantmentsunbound.config.ConfigManager;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.Property;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.notcharrow.enchantmentsunbound.helper.UnboundHelper.*;

@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin {

	@Shadow @Final private Property levelCost;
	@Shadow @Nullable private String newItemName;

	@Inject(method = "updateResult", at = @At("TAIL"))
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

		if (hasOverleveledEnchants(outputEnchants, leftEnchants, rightEnchants)
		|| levelCost.get() > 39
		|| levelCost.get() < 1) {

			ItemStack output = createOutput(outputEnchants, leftEnchants, rightEnchants, leftInput, this.newItemName);

			self.getSlot(2).setStack(output);
			int currentRepairCost = leftInput.getOrDefault(DataComponentTypes.REPAIR_COST, 0);
			int newRepairCost = AnvilScreenHandler.getNextCost(currentRepairCost);
			// System.out.println(currentRepairCost + " " + newRepairCost);
			output.set(DataComponentTypes.REPAIR_COST, newRepairCost);

			if (ConfigManager.config.staticCost) {
				levelCost.set(Math.max(ConfigManager.config.levelCost, 1));
			}
			levelCost.set(Math.min(ConfigManager.config.maxLevelCost, levelCost.get()));
		}
	}

	@ModifyConstant(
			method = "updateResult",
			constant = @Constant(intValue = 40)
	)
	private int raiseTooExpensiveLimit(int original) {
		return Integer.MAX_VALUE;
	}
}