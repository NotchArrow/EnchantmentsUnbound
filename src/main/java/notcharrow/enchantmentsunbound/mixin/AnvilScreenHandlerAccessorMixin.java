package notcharrow.enchantmentsunbound.mixin;

import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AnvilScreenHandler.class)
public interface AnvilScreenHandlerAccessorMixin {
	@Accessor("levelCost")
	Property getLevelCost();
}

