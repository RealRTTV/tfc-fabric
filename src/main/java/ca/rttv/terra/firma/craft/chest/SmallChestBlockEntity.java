package ca.rttv.terra.firma.craft.chest;

import ca.rttv.terra.firma.craft.TFCBlockEntityType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.ChestLidAnimator;
import net.minecraft.block.enums.ChestType;
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
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class SmallChestBlockEntity extends ChestBlockEntity implements ChestAnimationProgress {
   private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(18, ItemStack.EMPTY);
   private final ChestLidAnimator lidAnimator;
   private final String woodType;
   
   public SmallChestBlockEntity(BlockPos pos, BlockState state, String woodType) {
      super(TFCBlockEntityType.SMALL_CHEST, pos, state);
      lidAnimator = new ChestLidAnimator();
      this.woodType = woodType;
   }
   
   public static void clientTick(World world, BlockPos pos, BlockState state, ChestBlockEntity blockEntity) {
//      System.out.println(blockEntity.lidAnimator.getProgress(0.6f));
      blockEntity.lidAnimator.step();
   }
   
   @Override
   public float getAnimationProgress(float tickDelta) {
//      System.out.println(tickDelta + ": " + this.lidAnimator.getProgress(tickDelta));
      return this.lidAnimator.getProgress(tickDelta);
   }
   
   @Override
   protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
      return new SmallChestScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(this.world, this.pos), this);
   }
   
   @Override
   protected Text getContainerName() {
      return Text.of("Small Chest");
   }
   
   public String getWoodType() {
      return woodType;
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
   public boolean onSyncedBlockEvent(int type, int data) {
//      System.out.println("onSyncedBlockEvent " + data);
      if (type == 1) {
         this.lidAnimator.setOpen(data > 0);
         return true;
      }
      return false;
   }
   
   @Override
   public boolean hasCustomName() {
      return super.hasCustomName();
   }
   
   @Override
   public void onClose(PlayerEntity player) {
      if (this.removed || player.isSpectator() || this.getCachedState().get(SmallChestBlock.CHEST_TYPE) == ChestType.LEFT) {
         return;
      }
      
      onSyncedBlockEvent(1, 0);
      playSound(this.world, this.pos, this.getCachedState(), SoundEvents.BLOCK_CHEST_CLOSE);
   }
   
   public static void playSound(World world, BlockPos pos, BlockState state, SoundEvent soundEvent) {
      double d = (double) pos.getX() + 0.5;
      double e = (double) pos.getY() + 0.5;
      double f = (double) pos.getZ() + 0.5;
      if (state.get(SmallChestBlock.CHEST_TYPE) == ChestType.RIGHT) {
         Vec3f vec3f = state.get(SmallChestBlock.FACING).rotateCounterclockwise(Axis.Y).getUnitVector();
         d += (double) vec3f.getX() / 2.0d;
         e += (double) vec3f.getY() / 2.0d;
         f += (double) vec3f.getZ() / 2.0d;
      }
      world.playSound(null, d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5f, world.random.nextFloat() * 0.1f + 0.9f);
   }
   
   @Override
   public void onOpen(PlayerEntity player) {
      if (this.removed || player.isSpectator() || this.getCachedState().get(SmallChestBlock.CHEST_TYPE) == ChestType.LEFT) {
         return;
      }
      
      onSyncedBlockEvent(1, 1);
      playSound(this.world, this.pos, this.getCachedState(), SoundEvents.BLOCK_CHEST_OPEN);
   }
   
   @Override // read **from** nbtcompound
   public void readNbt(NbtCompound nbt) {
      super.readNbt(nbt);
      this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
      Inventories.readNbt(nbt, this.inventory);
   }
   
   @Override
   public int size() {
      return this.inventory.size();
   }
   
   @Override // write **to** nbtcompound
   public void writeNbt(NbtCompound nbt) {
      super.writeNbt(nbt);
      Inventories.writeNbt(nbt, this.inventory);
   }
}
