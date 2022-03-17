package ca.rttv.terra.firma.craft.chest;

import ca.rttv.terra.firma.craft.TFCBlockEntityType;
import java.util.Optional;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.DoubleInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SmallChestBlock extends ChestBlock implements Waterloggable {
   private final        String                                                                                              woodType;
   private static final DoubleBlockProperties.PropertyRetriever<SmallChestBlockEntity, Optional<Inventory>>                 INVENTORY_RETRIEVER = new DoubleBlockProperties.PropertyRetriever<>() {
      public Optional<Inventory> getFallback() {
         return Optional.empty();
      }
      
      public Optional<Inventory> getFrom(SmallChestBlockEntity chest) {
         return Optional.of(chest);
      }
      
      public Optional<Inventory> getFromBoth(SmallChestBlockEntity firstChest, SmallChestBlockEntity secondChest) {
         return Optional.of(new DoubleInventory(firstChest, secondChest));
      }
   };
   private static final DoubleBlockProperties.PropertyRetriever<SmallChestBlockEntity, Optional<NamedScreenHandlerFactory>> NAME_RETRIEVER      = new DoubleBlockProperties.PropertyRetriever<>() {
      public Optional<NamedScreenHandlerFactory> getFallback() {
         return Optional.empty();
      }
      
      public Optional<NamedScreenHandlerFactory> getFrom(SmallChestBlockEntity chest) {
         return Optional.of(chest);
      }
      
      public Optional<NamedScreenHandlerFactory> getFromBoth(SmallChestBlockEntity firstChest, SmallChestBlockEntity secondChest) {
         final Inventory inventory = new DoubleInventory(firstChest, secondChest);
         return Optional.of(new NamedScreenHandlerFactory() {
            @Nullable
            public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
               if (firstChest.checkUnlocked(playerEntity) && secondChest.checkUnlocked(playerEntity)) {
                  firstChest.checkLootInteraction(playerInventory.player);
                  secondChest.checkLootInteraction(playerInventory.player);
                  return new DoubleSmallChestScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(firstChest.getWorld(), firstChest.getPos()), inventory);
               } else {
                  return null;
               }
            }
            
            public Text getDisplayName() {
               return Text.of("Small Chest");
            }
         });
      }
   };
   
   
   public SmallChestBlock(Settings settings, String woodType) {
      super(settings, () -> TFCBlockEntityType.SMALL_CHEST);
      this.woodType = woodType;
      this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(WATERLOGGED, false).with(CHEST_TYPE, ChestType.SINGLE));
   }
   
   @Override
   public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
      return new SmallChestBlockEntity(pos, state, woodType);
   }
   
   public BlockEntityType<? extends ChestBlockEntity> getExpectedEntityType() {
      return this.entityTypeRetriever.get();
   }
   
   @Override
   public BlockRenderType getRenderType(BlockState state) {
      return BlockRenderType.ENTITYBLOCK_ANIMATED;
   }
   
   @Override
   @Nullable
   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
      return world.isClient ? checkType(type, TFCBlockEntityType.SMALL_CHEST, SmallChestBlockEntity::clientTick) : null;
   }
   
   @Override
   public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
      if (world.isClient) {
         return ActionResult.SUCCESS;
      }
      
      if (ChestBlock.isChestBlocked(world, pos)) {
         return ActionResult.success(world.isClient);
      }
      if (state.get(CHEST_TYPE) == ChestType.SINGLE) {
         player.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, playerEntity) -> new SmallChestScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos), (Inventory) world.getBlockEntity(pos) /* block entity to inventory is like a magic trick */),
                                                                      Text.of("Small Chest")));
      } else {
         player.openHandledScreen(createScreenHandlerFactory(state, world, pos));
      }
      player.incrementStat(Stats.OPEN_CHEST);
      PiglinBrain.onGuardedBlockInteracted(player, true);
      return ActionResult.CONSUME;
   }
   
   @Nullable
   public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
      SmallChestBlockEntity[] blocks = getDoubleChestBlocks(state, world, pos);
      Optional<NamedScreenHandlerFactory> handler = NAME_RETRIEVER.getFromBoth(blocks[0], blocks[1]);
      if (handler.isEmpty()) {
         return null;
      }
      return handler.get();
   }
   
   private SmallChestBlockEntity[] getDoubleChestBlocks(BlockState state, World world, BlockPos pos) {
      if (state.get(CHEST_TYPE) == ChestType.RIGHT) {
         return new SmallChestBlockEntity[] { (SmallChestBlockEntity) world.getBlockEntity(pos),
                                              getPartnerBlock(state, world, pos) };
      } else {
         return new SmallChestBlockEntity[] { getPartnerBlock(state, world, pos),
                                              (SmallChestBlockEntity) world.getBlockEntity(pos) };
      }
   }
   
   public SmallChestBlockEntity getPartnerBlock(BlockState state, World world, BlockPos pos) {
      return state.get(CHEST_TYPE) == ChestType.LEFT ? (SmallChestBlockEntity) world.getBlockEntity(pos.offset(state.get(FACING).rotateClockwise(Axis.Y))) : (SmallChestBlockEntity) world.getBlockEntity(pos.offset(state.get(FACING).rotateCounterclockwise(Axis.Y)));
   }
}
