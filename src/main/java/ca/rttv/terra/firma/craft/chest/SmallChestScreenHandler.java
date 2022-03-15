package ca.rttv.terra.firma.craft.chest;

import ca.rttv.terra.firma.craft.TFCScreenHandlerType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;

public class SmallChestScreenHandler extends ScreenHandler {
   public SmallChestScreenHandler(int syncId, PlayerInventory inventory) {
      this(syncId, inventory, ScreenHandlerContext.EMPTY);
   }
   
   public SmallChestScreenHandler(int syncId, PlayerInventory inventory, ScreenHandlerContext context) {
      super(TFCScreenHandlerType.SMALL_CHEST, syncId);
   }
   
   @Override
   public boolean canUse(PlayerEntity player) {
      return true;
   }
}
