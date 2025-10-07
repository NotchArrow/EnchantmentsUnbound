package com.notcharrow.enchantmentsunbound.mixin;

import com.notcharrow.enchantmentsunbound.config.ConfigManager;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

import static com.notcharrow.enchantmentsunbound.helper.UnboundHelper.createOutput;
import static com.notcharrow.enchantmentsunbound.helper.UnboundHelper.getEnchantments;

@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin {

	@Shadow @Final private Property levelCost;
	@Shadow @Nullable private String newItemName;

	@Inject(method = "updateResult", at = @At("TAIL"))
	private void onUpdateResult(CallbackInfo ci) {
		AnvilScreenHandler self = (AnvilScreenHandler)(Object)this;

		ItemStack leftInput = self.getSlot(0).getStack();
		ItemStack rightInput = self.getSlot(1).getStack();

		if (leftInput.isEmpty()) {
			return;
		}
		if (rightInput.isEmpty()) {
			if (ConfigManager.config.lowRenamingCost) {
				levelCost.set(1);
			}
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

		ItemStack output = createOutput(outputEnchants, leftEnchants, rightEnchants, leftInput, this.newItemName);

		self.getSlot(2).setStack(output);
		int currentRepairCost = leftInput.getOrDefault(DataComponentTypes.REPAIR_COST, 0);
		int newRepairCost = AnvilScreenHandler.getNextCost(currentRepairCost);
		output.set(DataComponentTypes.REPAIR_COST, newRepairCost);

		if (ConfigManager.config.staticCost) {
			levelCost.set(Math.max(ConfigManager.config.levelCost, 1));
		}
		levelCost.set(Math.min(ConfigManager.config.maxLevelCost, levelCost.get()));

		if (levelCost.get() > 39 && ConfigManager.config.showTooltipMessage) {
			LoreComponent existingLore = output.get(DataComponentTypes.LORE);
			List<Text> lore = new ArrayList<>();
			if (existingLore != null) {
				lore.addAll(existingLore.lines());
			}
			lore.add(Text.literal("Level Cost: " + levelCost.get() + " levels.").formatted(Formatting.GREEN));
			lore.add(Text.literal("You can still take the output!").formatted(Formatting.GREEN));
			output.set(DataComponentTypes.LORE, new LoreComponent(lore));
		}
	}

	@Inject(method = "onTakeOutput", at = @At("HEAD"))
	private void removeTempLore(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
		LoreComponent existingLore = stack.get(DataComponentTypes.LORE);
		List<Text> lore = new ArrayList<>();
		if (existingLore != null) {
			for (Text line: existingLore.lines()) {
				String lineString = line.getString();
				if (!lineString.contains("Level Cost:") && !lineString.contains("You can still take the output!")) {
					lore.add(line);
				}
			}
		}
		stack.remove(DataComponentTypes.LORE);
		stack.set(DataComponentTypes.LORE, new LoreComponent(lore));
	}

	@ModifyConstant(
			method = "updateResult",
			constant = @Constant(intValue = 40)
	)
	private int raiseTooExpensiveLimit(int original) {
		return Integer.MAX_VALUE;
	}

	@ModifyVariable(
			method = "updateResult",
			at = @At(value = "STORE", ordinal = 0), // bl4 = enchantment.isAcceptableItem()
			ordinal = 0
	)
	private boolean alwaysTrue(boolean bl4) {
		return true;
	}

	/**
	 * @author NotchArrow
	 * @reason Allowing customization of vanilla scaling to better fit the needs of users
	 */
	@Overwrite
	public static int getNextCost(int cost) {
		return (int) Math.min((long) (cost * ConfigManager.config.levelCostScalingMultiplier + 1), 2147483647L);
	}

	@Inject(method = "setNewItemName", at = @At("TAIL"))
	public void setNewItemName(String newItemName, CallbackInfoReturnable<Boolean> cir) {
		if (ConfigManager.config.colorCodedRenaming && newItemName.contains("&")) {

			// https://minecraft.wiki/w/Formatting_codes

			MutableText nameText = Text.literal("");

			List<Formatting> activeFormats = new ArrayList<>();
			for (int i = 0; i < newItemName.length(); i++) {
				char c = newItemName.charAt(i);

				// detect format code
				if (c == '&' && i + 1 < newItemName.length()) {
					char codeChar = newItemName.charAt(i + 1);
					Formatting fmt = Formatting.byCode(codeChar);
					if (fmt != null) {
						activeFormats.add(fmt);
					}
					i++; // skip char code
					continue;
				}

				MutableText charText = Text.literal(String.valueOf(c));
				for (Formatting fmt : activeFormats) {
					charText = charText.formatted(fmt);
				}

				nameText.append(charText);
			}

			AnvilScreenHandler anvil = ((AnvilScreenHandler) (Object) this);
			ItemStack itemStack = anvil.getSlot(2).getStack();
			itemStack.set(DataComponentTypes.CUSTOM_NAME, nameText);
			anvil.setStackInSlot(2, 1, itemStack);
		}

	}
}