package ca.rttv.terra.firma.craft.mixin;

import ca.rttv.terra.firma.craft.TFCScreenHandlerType;
import ca.rttv.terra.firma.craft.chest.SmallChestScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import org.spongepowered.asm.mixin.Mixin;

@Mixin( HandledScreens.class )
public abstract class HandledScreensMixin {
   static {
      HandledScreens.register(TFCScreenHandlerType.SMALL_CHEST, SmallChestScreen::new);
   }
}
