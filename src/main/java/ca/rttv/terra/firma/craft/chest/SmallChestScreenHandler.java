package ca.rttv.terra.firma.craft.chest;

import ca.rttv.terra.firma.craft.TFCScreenHandlerType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;

public class SmallChestScreenHandler extends ScreenHandler {
   private final ScreenHandlerContext context;
   private final Inventory inventory;
   
   public SmallChestScreenHandler(int syncId, PlayerInventory inventory) {
      this(syncId, inventory, ScreenHandlerContext.EMPTY);
   }
   
   public SmallChestScreenHandler(int syncId, PlayerInventory inventory, ScreenHandlerContext context) {
      this(syncId, inventory, context, new SimpleInventory(18));
   }
   
   public SmallChestScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, Inventory inventory) {
      super(TFCScreenHandlerType.SMALL_CHEST, syncId);
      this.context = context;
      this.inventory = inventory;
      inventory.onOpen(playerInventory.player);
      for (int i = 0; i < 3; ++i) {
         for (int j = 0; j < 9; ++j) {
            this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 67 + i * 18));
         }
      }
      for (int i = 0; i < 9; ++i) {
         this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 125));
      }
      
      for (int i = 0; i < 2; i++) {
         for (int j = 0; j < 9; j++) {
            this.addSlot(new Slot(this.inventory, (i * 9) + j, 8 + j * 18, 18 + i * 18){
               @Override
               public int getMaxItemCount() {
                  return 32;
               }
            });
         }
      }
   }
   
   @Override
   public boolean canUse(PlayerEntity player) {
      return true;
   }
   
   @Override
   public void close(PlayerEntity player) {
      super.close(player);
      this.inventory.onClose(player);
   }
}
