package ca.rttv.terra.firma.craft.chest;

import ca.rttv.terra.firma.craft.TFCBlockEntityType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.ChestLidAnimator;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.block.ChestAnimationProgress;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.DoubleInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
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
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class SmallChestBlockEntity extends ChestBlockEntity implements ChestAnimationProgress {
   private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(18, ItemStack.EMPTY);
   public final ChestLidAnimator lidAnimator;
   public final ViewerCountManager stateManager = new ViewerCountManager() {
      protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
         SmallChestBlockEntity.playSound(world, pos, state, SoundEvents.BLOCK_CHEST_OPEN);
      }
   
      protected void onContainerClose(World world, BlockPos pos, BlockState state) {
         SmallChestBlockEntity.playSound(world, pos, state, SoundEvents.BLOCK_CHEST_CLOSE);
      }
   
      protected void onViewerCountUpdate(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
         System.out.println("VCU: " + newViewerCount);
         SmallChestBlockEntity.this.onInvOpenOrClose(world, pos, state, oldViewerCount, newViewerCount);
      }
   
      protected boolean isPlayerViewing(PlayerEntity player) {
         if (!(player.currentScreenHandler instanceof SmallChestScreenHandler || player.currentScreenHandler instanceof DoubleSmallChestScreenHandler)) {
            return false;
         } else {
            Inventory inventory = player.currentScreenHandler instanceof SmallChestScreenHandler ? ((SmallChestScreenHandler) player.currentScreenHandler).inventory : ((DoubleSmallChestScreenHandler) player.currentScreenHandler).inventory;
            return inventory == SmallChestBlockEntity.this || inventory instanceof DoubleInventory && ((DoubleInventory)inventory).isPart(SmallChestBlockEntity.this);
         }
      }
   };
   private final String woodType;
   
   public SmallChestBlockEntity(BlockPos pos, BlockState state, String woodType) {
      super(TFCBlockEntityType.SMALL_CHEST, pos, state);
      this.lidAnimator = new ChestLidAnimator();
      this.woodType = woodType;
   }
   
   public void onClientTick(World world, BlockPos pos, BlockState state) {
      this.lidAnimator.step();
   }
   
   @Override
   public float getAnimationProgress(float tickDelta) {
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
      System.out.println("dat: " + data);
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
      System.out.println("clos: 3");
      this.stateManager.closeContainer(player, this.world, this.pos, this.getCachedState());
   }
   
   public static void playSound(World world, BlockPos pos, BlockState state, SoundEvent soundEvent) {
      if (state.get(SmallChestBlock.CHEST_TYPE) == ChestType.LEFT) {
         return;
      }
      double d = (double) pos.getX() + 0.5;
      double e = (double) pos.getY() + 0.5;
      double f = (double) pos.getZ() + 0.5;
      if (state.get(SmallChestBlock.CHEST_TYPE) == ChestType.RIGHT) {
         Vec3f directionVec = state.get(SmallChestBlock.FACING).rotateCounterclockwise(Axis.Y).getUnitVector();
         d += (double) directionVec.getX() / 2.0d;
         e += (double) directionVec.getY() / 2.0d;
         f += (double) directionVec.getZ() / 2.0d;
      }
      world.playSound(null, d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5f, world.random.nextFloat() * 0.1f + 0.9f);
   }
   
   @Override
   public void onScheduledTick() {
      // removed cuz it broke stuff, probably breaks more stuff having it removed tho
   }
   
   @Override
   public void onOpen(PlayerEntity player) {
      System.out.println("open: 2");
      this.stateManager.openContainer(player, this.world, this.pos, this.getCachedState());
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
   
   @Override
   protected void onInvOpenOrClose(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
      System.out.println("nVC: " + newViewerCount);
      world.addSyncedBlockEvent(pos, state.getBlock(), 1, newViewerCount);
   }
}
