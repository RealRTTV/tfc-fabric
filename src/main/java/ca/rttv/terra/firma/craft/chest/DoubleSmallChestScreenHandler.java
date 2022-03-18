package ca.rttv.terra.firma.craft.chest;

import ca.rttv.terra.firma.craft.TFCScreenHandlerType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class DoubleSmallChestScreenHandler extends ScreenHandler {
   public final Inventory            inventory;
   
   public DoubleSmallChestScreenHandler(int syncId, PlayerInventory inventory) {
      this(syncId, inventory, ScreenHandlerContext.create(inventory.player.getWorld(), new BlockPos(inventory.player.raycast(inventory.player.isCreative() ? 5.0d : 4.0d, 0, false).getPos())));
   }
   
   public DoubleSmallChestScreenHandler(int syncId, PlayerInventory inventory, ScreenHandlerContext context) {
      this(syncId, inventory, context, new SimpleInventory(36));
   }
   
   public DoubleSmallChestScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, Inventory inventory) {
      super(TFCScreenHandlerType.DOUBLE_SMALL_CHEST, syncId);
      this.inventory = inventory;
      inventory.onOpen(playerInventory.player);
      for (int i = 0; i < 3; ++i) {
         for (int j = 0; j < 9; ++j) {
            this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 103 + i * 18));
         }
      }
      for (int i = 0; i < 9; ++i) {
         this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 161));
      }
      
      for (int i = 0; i < 4; i++) {
         for (int j = 0; j < 9; j++) {
            this.addSlot(new Slot(this.inventory, (i * 9) + j, 8 + j * 18, 18 + i * 18));
         }
      }
   }
   
   @Override
   public boolean canUse(PlayerEntity player) {
      return true;
   }
   
   @Override
   public ItemStack transferSlot(PlayerEntity player, int index) {
      ItemStack itemStack = ItemStack.EMPTY;
      Slot slot = this.slots.get(index);
      if (slot != null && slot.hasStack()) {
         ItemStack itemStack2 = slot.getStack();
         itemStack = itemStack2.copy();
         if (index < this.inventory.size() ? !this.insertItem(itemStack2, this.inventory.size(), this.slots.size(), true) : !this.insertItem(itemStack2, 0, this.inventory.size(), false)) {
            return ItemStack.EMPTY;
         }
         if (itemStack2.isEmpty()) {
            slot.setStack(ItemStack.EMPTY);
         } else {
            slot.markDirty();
         }
      }
      return itemStack;
   }
   
   @Override
   public void close(PlayerEntity player) {
      super.close(player);
      this.inventory.onClose(player);
   }
}
