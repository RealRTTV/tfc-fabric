package ca.rttv.terra.firma.craft.chest;

import ca.rttv.terra.firma.craft.TFCBlockEntityType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestLidAnimator;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.client.block.ChestAnimationProgress;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SmallChestBlockEntity extends LootableContainerBlockEntity implements ChestAnimationProgress {
   private       DefaultedList<ItemStack> inventory   = DefaultedList.ofSize(18, ItemStack.EMPTY);
   private final ChestLidAnimator         lidAnimator = new ChestLidAnimator();
   
   public SmallChestBlockEntity(BlockPos pos, BlockState state) {
      super(TFCBlockEntityType.SMALL_CHEST, pos, state);
   }
   
   public static void clientTick(World world, BlockPos pos, BlockState state, BlockEntity instance) {
      ((SmallChestBlockEntity) instance).lidAnimator.step();
   }
   
   @Override
   protected DefaultedList<ItemStack> getInvStackList() {
      return this.inventory;
   }
   
   @Override
   protected void setInvStackList(DefaultedList<ItemStack> list) {
      this.inventory = list;
   }
   
   @Override
   public float getAnimationProgress(float tickDelta) {
      return this.lidAnimator.getProgress(tickDelta);
   }
   
   @Override
   protected Text getContainerName() {
      return Text.of("Small Chest");
   }
   
   @Override
   protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
      return new SmallChestScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(this.world, this.pos), this);
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
   public void onOpen(PlayerEntity player) {
      if (this.removed || player.isSpectator()) {
         return;
      }
   
      playSound(this.world, this.pos, this.getCachedState(), SoundEvents.BLOCK_CHEST_OPEN);
   }
   
   @Override
   public boolean onSyncedBlockEvent(int type, int data) {
      if (type == 1) {
         this.lidAnimator.setOpen(data > 0);
         return true;
      }
      return super.onSyncedBlockEvent(type, data);
   }
   
   @Override
   public void onClose(PlayerEntity player) {
      if (this.removed || player.isSpectator()) {
         return;
      }
      
      playSound(this.world, this.pos, this.getCachedState(), SoundEvents.BLOCK_CHEST_CLOSE);
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
