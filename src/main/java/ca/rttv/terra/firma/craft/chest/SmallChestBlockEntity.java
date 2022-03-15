package ca.rttv.terra.firma.craft.chest;

import ca.rttv.terra.firma.craft.TFCBlockEntityType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.client.block.ChestAnimationProgress;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SmallChestBlockEntity extends LockableContainerBlockEntity implements ChestAnimationProgress {
   private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(18, ItemStack.EMPTY);
   
   public SmallChestBlockEntity(BlockPos pos, BlockState state) {
      super(TFCBlockEntityType.SMALL_CHEST, pos, state);
   }
   
   @Override
   public void clear() {
      this.inventory = DefaultedList.ofSize(18, ItemStack.EMPTY);
   }
   
   @Override
   public float getAnimationProgress(float tickDelta) {
      return 1.0f;
   }
   
   @Override
   protected Text getContainerName() {
      return Text.of("Small Chest");
   }
   
   @Override
   protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
      return new SmallChestScreenHandler(syncId, playerInventory);
   }
   
   @Override
   public boolean hasCustomName() {
      return super.hasCustomName();
   }
   
   @Override
   public int size() {
      return this.inventory.size();
   }
   
   @Override
   public boolean isEmpty() {
      return this.inventory.isEmpty();
   }
   
   @Override
   public ItemStack getStack(int slot) {
      return this.inventory.get(slot);
   }
   
   @Override
   public ItemStack removeStack(int slot, int amount) {
      return Inventories.splitStack(this.inventory, slot, amount);
   }
   
   @Override
   public ItemStack removeStack(int slot) {
      return Inventories.removeStack(this.inventory, slot);
   }
   
   @Override
   public void setStack(int slot, ItemStack stack) {
      this.inventory.set(slot, stack);
   }
   
   @Override
   public int getMaxCountPerStack() {
      return super.getMaxCountPerStack();
   }
   
   @Override
   public boolean canPlayerUse(PlayerEntity player) {
      if (this.world.getBlockEntity(this.pos) != this) {
         return false;
      }
      return !(player.squaredDistanceTo((double)this.pos.getX() + 0.5, (double)this.pos.getY() + 0.5, (double)this.pos.getZ() + 0.5) > 64.0);
   }
   
   @Override
   public void onOpen(PlayerEntity player) {
      if (this.removed || player.isSpectator()) {
         return;
      }
   
      playSound(this.world, this.pos, this.getCachedState(), SoundEvents.BLOCK_CHEST_OPEN);
   }
   
   @Override
   public void onClose(PlayerEntity player) {
      if (this.removed || player.isSpectator()) {
         return;
      }
      
      playSound(this.world, this.pos, this.getCachedState(), SoundEvents.BLOCK_CHEST_CLOSE);
   }
   
   @Override
   public boolean isValid(int slot, ItemStack stack) {
      return super.isValid(slot, stack); // todo: item max count system
   }
   
   @Override // write **to** nbtcompound
   public void writeNbt(NbtCompound nbt) {
      super.writeNbt(nbt);
      Inventories.writeNbt(nbt, this.inventory);
   }
   
   @Override // read **from** nbtcompound
   public void readNbt(NbtCompound nbt) {
      super.readNbt(nbt);
      this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
      Inventories.readNbt(nbt, this.inventory);
   }
   
   static void playSound(World world, BlockPos pos, BlockState state, SoundEvent soundEvent) {
      double d = (double)pos.getX() + 0.5;
      double e = (double)pos.getY() + 0.5;
      double f = (double)pos.getZ() + 0.5;
      world.playSound(null, d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5f, world.random.nextFloat() * 0.1f + 0.9f);
   }
}
