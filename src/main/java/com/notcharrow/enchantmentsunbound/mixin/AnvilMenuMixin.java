package com.notcharrow.enchantmentsunbound.mixin;

import com.notcharrow.enchantmentsunbound.config.ConfigManager;
import com.notcharrow.enchantmentsunbound.helper.AnvilScreenHandlerPlayerAccess;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

import static com.notcharrow.enchantmentsunbound.helper.UnboundHelper.*;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {

	@Shadow @Final private DataSlot cost;
	@Shadow @Nullable private String itemName;

	@Inject(method = "createResult", at = @At("TAIL"))
	private void onUpdateResult(CallbackInfo ci) {
		AnvilMenu self = (AnvilMenu)(Object)this;

		ItemStack leftInput = self.getSlot(0).getItem();
		ItemStack rightInput = self.getSlot(1).getItem();

		if (leftInput.isEmpty()) {
			return;
		}
		if (rightInput.isEmpty()) {
			if (ConfigManager.config.lowRenamingCost) {
				cost.set(1);
			}
			return;
		}

		if (!(leftInput.getItem() == Items.ENCHANTED_BOOK ||
				leftInput.is(ItemTags.DURABILITY_ENCHANTABLE))) {
			return;
		}
		if (!(rightInput.getItem() == Items.ENCHANTED_BOOK ||
				rightInput.is(ItemTags.DURABILITY_ENCHANTABLE))) {
			return;
		}

		Object2IntMap<Holder<Enchantment>> leftEnchants = new Object2IntArrayMap<>();
		Object2IntMap<Holder<Enchantment>> rightEnchants = new Object2IntArrayMap<>();

		getEnchantments(leftInput, leftEnchants);
		getEnchantments(rightInput, rightEnchants);

		Object2IntMap<Holder<Enchantment>> outputEnchants = new Object2IntArrayMap<>();

		for (Object2IntMap.Entry<Holder<Enchantment>> entry: leftEnchants.object2IntEntrySet()) {
			Holder<Enchantment> enchantmentEntry = entry.getKey();
			Enchantment enchantment = enchantmentEntry.value();
			int level = entry.getIntValue();

			for (Object2IntMap.Entry<Holder<Enchantment>> otherEntry : rightEnchants.object2IntEntrySet()) {
				if (otherEntry.getKey().value() == enchantment && otherEntry.getIntValue() == level) {
					outputEnchants.put(enchantmentEntry, level + 1);
				}
			}
		}

		ItemStack output = createOutput(outputEnchants, leftEnchants, rightEnchants, leftInput, this.itemName);

		self.getSlot(2).set(output);
		int currentRepairCost = leftInput.getOrDefault(DataComponents.REPAIR_COST, 0);
		int newRepairCost = AnvilMenu.calculateIncreasedRepairCost(currentRepairCost);
		output.set(DataComponents.REPAIR_COST, newRepairCost);

		if (ConfigManager.config.staticCost) {
			cost.set(Math.max(ConfigManager.config.levelCost, 1));
		} else if (ConfigManager.config.useXpPerEnchantLevel) {
			int newCost = 0;
			for (Object2IntMap.Entry<Holder<Enchantment>> entry: combined.object2IntEntrySet()) {
				Holder<Enchantment> enchantment = entry.getKey();
				if (!ConfigManager.config.itemEnchantConflicts || output.canBeEnchantedWith(enchantment, EnchantingContext.ACCEPTABLE)
						|| output.getItem() == Items.ENCHANTED_BOOK) {
					newCost += Math.min(entry.getIntValue(), getAnvilCap(enchantment)) * ConfigManager.config.xpPerEnchantLevel;
				}
			}
			cost.set(Math.max(newCost, 1));
		}
		cost.set(Math.min(ConfigManager.config.maxLevelCost, cost.get()));

		if (cost.get() > 39 && ConfigManager.config.showTooltipMessage) {
			ItemLore existingLore = output.get(DataComponents.LORE);
			List<Component> lore = new ArrayList<>();
			if (existingLore != null) {
				lore.addAll(existingLore.lines());
			}
			if (((AnvilScreenHandlerPlayerAccess) self).getPlayer().experienceLevel >= cost.get()) {
				lore.add(Component.literal("Level Cost: " + cost.get() + " levels.").withStyle(ChatFormatting.GREEN));
				lore.add(Component.literal("You can still take the output!").withStyle(ChatFormatting.GREEN));
			} else {
				lore.add(Component.literal("Level Cost: " + cost.get() + " levels.").withStyle(ChatFormatting.RED));
			}
			output.set(DataComponents.LORE, new ItemLore(lore));
		}
	}

	@Inject(method = "onTake", at = @At("HEAD"))
	private void removeTempLore(Player player, ItemStack stack, CallbackInfo ci) {
		ItemLore existingLore = stack.get(DataComponents.LORE);
		List<Component> lore = new ArrayList<>();
		if (existingLore != null) {
			for (Component line: existingLore.lines()) {
				String lineString = line.getString();
				if (!lineString.contains("Level Cost:") && !lineString.contains("You can still take the output!")) {
					lore.add(line);
				}
			}
		}
		stack.remove(DataComponents.LORE);
		stack.set(DataComponents.LORE, new ItemLore(lore));
		/*
		player.getEntityWorld().playSound(null, player.getX(), player.getY(), player.getZ(),
				SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1.0F, 1.5F);
		ci.cancel();
		// TODO Reimplement logic, potential to add infinite anvil durability :O along with fancy sounds/effects etc...
		/*
		if (!player.isInCreativeMode()) {
            player.addExperienceLevels(-this.levelCost.get());
        }

        if (this.repairItemUsage > 0) {
            ItemStack itemStack = this.input.getStack(1);
            if (!itemStack.isEmpty() && itemStack.getCount() > this.repairItemUsage) {
                itemStack.decrement(this.repairItemUsage);
                this.input.setStack(1, itemStack);
            } else {
                this.input.setStack(1, ItemStack.EMPTY);
            }
        } else if (!this.keepSecondSlot) {
            this.input.setStack(1, ItemStack.EMPTY);
        }

        this.levelCost.set(0);
        if (player instanceof ServerPlayerEntity serverPlayerEntity) {
            if (!StringHelper.isBlank(this.newItemName) && !this.input.getStack(0).getName().getString().equals(this.newItemName)) {
                serverPlayerEntity.getTextStream().filterText(this.newItemName);
            }
        }

        this.input.setStack(0, ItemStack.EMPTY);
        this.context.run((world, pos) -> {
            BlockState blockState = world.getBlockState(pos);
            if (!player.isInCreativeMode() && blockState.isIn(BlockTags.ANVIL) && player.getRandom().nextFloat() < 0.12F) {
                BlockState blockState2 = AnvilBlock.getLandingState(blockState);
                if (blockState2 == null) {
                    world.removeBlock(pos, false);
                    world.syncWorldEvent(1029, pos, 0);
                } else {
                    world.setBlockState(pos, blockState2, 2);
                    world.syncWorldEvent(1030, pos, 0);
                }
            } else {
                world.syncWorldEvent(1030, pos, 0);
            }

        });
		 */
	}

	@ModifyConstant(
			method = "createResult",
			constant = @Constant(intValue = 40)
	)
	private int raiseTooExpensiveLimit(int original) {
		return Integer.MAX_VALUE;
	}

	@Inject(method = "calculateIncreasedRepairCost", at = @At("TAIL"), cancellable = true)
	private static void calculateIncreasedRepairCost(int baseCost, CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue((int) Math.min((long) (baseCost * ConfigManager.config.levelCostScalingMultiplier + 1), 2147483647L));
	}

	@Inject(method = "setItemName", at = @At("TAIL"))
	public void setNewItemName(String name, CallbackInfoReturnable<Boolean> cir) {
		if (ConfigManager.config.colorCodedRenaming && name.contains("&")) {

			// https://minecraft.wiki/w/Formatting_codes

			MutableComponent nameText = Component.literal("");

			List<ChatFormatting> activeFormats = new ArrayList<>();
			for (int i = 0; i < name.length(); i++) {
				char c = name.charAt(i);

				// detect format code
				if (c == '&' && i + 1 < name.length()) {
					char codeChar = name.charAt(i + 1);
					ChatFormatting fmt = ChatFormatting.getByCode(codeChar);
					if (fmt != null) {
						activeFormats.add(fmt);
					}
					i++; // skip char code
					continue;
				}

				MutableComponent charText = Component.literal(String.valueOf(c));
				for (ChatFormatting fmt : activeFormats) {
					charText = charText.withStyle(fmt);
				}

				nameText.append(charText);
			}

			AnvilMenu anvil = ((AnvilMenu) (Object) this);
			ItemStack itemStack = anvil.getSlot(2).getItem();
			itemStack.set(DataComponents.CUSTOM_NAME, nameText);
			anvil.setItem(2, 1, itemStack);
		}

	}
}