package com.notcharrow.enchantmentsunbound.mixin;

import com.notcharrow.enchantmentsunbound.helper.AnvilScreenHandlerPlayerAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ItemCombinerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemCombinerMenu.class)
public abstract class ItemCombinerMenuAccessor implements AnvilScreenHandlerPlayerAccess {
	@Shadow
	protected Player player;

	@Override
	public Player getPlayer() {
		return player;
	}
}
