package ca.rttv.terra.firma.craft.chest;

import ca.rttv.terra.firma.craft.TFCBlockEntityType;
import net.minecraft.block.AbstractChestBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.DoubleBlockProperties.PropertySource;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings( "deprecation" )
public class SmallChestBlock extends AbstractChestBlock<SmallChestBlockEntity> implements Waterloggable {
   private static final    DirectionProperty FACING       = HorizontalFacingBlock.FACING;
   private static final    BooleanProperty   WATERLOGGED  = Properties.WATERLOGGED;
   private static final    VoxelShape        SINGLE_SHAPE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 14.0, 15.0);
   
   public SmallChestBlock(Settings settings) {
      super(settings, () -> TFCBlockEntityType.SMALL_CHEST);
      this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(WATERLOGGED, false));
   }
   
   @Override
   public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
      return new SmallChestBlockEntity(pos, state);
   }
   
   @Override
   public PropertySource<? extends ChestBlockEntity> getBlockEntitySource(BlockState state, World world, BlockPos pos, boolean ignoreBlocked) {
      return DoubleBlockProperties.PropertyRetriever::getFallback;
   }
   
   @Override
   public BlockState getPlacementState(ItemPlacementContext ctx) {
      Direction direction = ctx.getPlayerFacing().getOpposite();
      FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
      if (direction.getAxis().isVertical()) {
         direction = Direction.NORTH;
      }
      return this.getDefaultState().with(FACING, direction).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
   }
   
   @Override
   public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
      return SINGLE_SHAPE;
   }
   
   @Override
   public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
      if (state.get(WATERLOGGED)) {
         world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
      }
      return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
   }
   
   @Override
   public FluidState getFluidState(BlockState state) {
      if (state.get(WATERLOGGED)) {
         return Fluids.WATER.getStill(false);
      }
      return super.getFluidState(state);
   }
   
   public BlockEntityType<?> getExpectedEntityType() {
      return this.entityTypeRetriever.get();
   }
   
   @Override
   public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
      if (state.isOf(newState.getBlock())) {
         return;
      }
      BlockEntity blockEntity = world.getBlockEntity(pos);
      if (blockEntity instanceof SmallChestBlockEntity) {
         ItemScatterer.spawn(world, pos, (SmallChestBlockEntity)blockEntity);
      }
   }
   
   @Override
   @Nullable
   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
      return world.isClient ? SmallChestBlock.checkType(type, this.getExpectedEntityType(), SmallChestBlockEntity::clientTick) : null;
   }
   
   @Override
   public BlockRenderType getRenderType(BlockState state) {
      return BlockRenderType.MODEL;
   }
   
   @Override
   protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
      builder.add(FACING, WATERLOGGED);
   }
   
   @Override
   public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
      if (world.isClient) {
         return ActionResult.SUCCESS;
      }
      
      BlockPos above = pos.up();
      if (world.getBlockState(above).isSolidBlock(world, above)) {
         return ActionResult.success(world.isClient);
      }
      
      player.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, playerEntity) -> new SmallChestScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos), (Inventory) world.getBlockEntity(pos) /* block entity to inventory is a magic trick */), Text.of("")));
      player.incrementStat(Stats.OPEN_CHEST);
      PiglinBrain.onGuardedBlockInteracted(player, true);
      return ActionResult.CONSUME;
   }
}
