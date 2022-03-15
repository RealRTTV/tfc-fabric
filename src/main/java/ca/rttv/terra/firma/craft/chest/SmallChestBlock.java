package ca.rttv.terra.firma.craft.chest;

import ca.rttv.terra.firma.craft.TFCBlockEntityType;
import net.minecraft.block.AbstractChestBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.DoubleBlockProperties.PropertySource;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
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
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class SmallChestBlock extends AbstractChestBlock<SmallChestBlockEntity> implements Waterloggable {
   
   public static final    DirectionProperty FACING       = Properties.FACING;
   public static final    BooleanProperty   WATERLOGGED  = Properties.WATERLOGGED;
   protected static final VoxelShape        SINGLE_SHAPE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 14.0, 15.0);
   
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
   
   @SuppressWarnings( "deprecation" )
   @Override
   public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
      return SINGLE_SHAPE;
   }
   
   @Override
   public BlockRenderType getRenderType(BlockState state) {
      return BlockRenderType.MODEL;
   }
   
   @Override
   protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
      builder.add(FACING, WATERLOGGED);
   }
   
   @SuppressWarnings( "deprecation" )
   @Override
   public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
      
      BlockPos above = pos.up();
      if (world.getBlockState(above).isSolidBlock(world, above)) {
         return ActionResult.success(world.isClient);
      }
      
      if (world.isClient) {
         return ActionResult.SUCCESS;
      }
      
      player.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, playerEntity) -> new SmallChestScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos), (Inventory) world.getBlockEntity(pos) /* block entity to inventory is a magic trick */), Text.of("")));
      player.incrementStat(Stats.OPEN_CHEST);
      PiglinBrain.onGuardedBlockInteracted(player, true);
      return ActionResult.CONSUME;
   }
}
