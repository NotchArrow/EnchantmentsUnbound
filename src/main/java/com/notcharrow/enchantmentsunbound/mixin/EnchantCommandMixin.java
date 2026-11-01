package com.notcharrow.enchantmentsunbound.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.notcharrow.enchantmentsunbound.config.ConfigManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.server.commands.EnchantCommand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Collection;

@Mixin(EnchantCommand.class)
public abstract class EnchantCommandMixin {

	@Definition(id = "level", local = @Local(type = int.class, name = "level", argsOnly = true))
	@Definition(id = "enchantment", local = @Local(type = Enchantment.class, name = "enchantment"))
	@Definition(id = "getMaxLevel", method = "Lnet/minecraft/world/item/enchantment/Enchantment;getMaxLevel()I")
	@Expression("level > enchantment.getMaxLevel()")
	@ModifyExpressionValue(method = "enchant", at = @At("MIXINEXTRAS:EXPRESSION"))
	private static boolean allowOverleveled(boolean original) {
		return original && !ConfigManager.config.useCustomCommandCap;
	}

	@ModifyVariable(method = "enchant", name = "level", at = @At("HEAD"), argsOnly = true)
	private static int adjustLevel(int level, final CommandSourceStack source, final Collection<? extends Entity> targets, final Holder<Enchantment> enchantmentHolder) {
		if (ConfigManager.config.useCustomCommandCap) {
			if (ConfigManager.config.useGlobalCommandCap) {
				return Math.min(level, ConfigManager.config.globalCommandCap);
			} else {
				int maxLevel = ConfigManager.config.enchantmentCommandCaps.getOrDefault(
						enchantmentHolder.getRegisteredName(),
						enchantmentHolder.value().getMaxLevel()
				);
				return Math.min(level, maxLevel);
			}
		}
		return level;
	}
}
