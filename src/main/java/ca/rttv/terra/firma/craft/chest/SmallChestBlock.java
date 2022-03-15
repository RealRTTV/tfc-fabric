package ca.rttv.terra.firma.craft.chest;

import ca.rttv.terra.firma.craft.TFCBlockEntityType;
import net.minecraft.block.AbstractChestBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.DoubleBlockProperties.PropertySource;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SmallChestBlock extends AbstractChestBlock<SmallChestBlockEntity> {
   
   public static final Logger LOGGER = LogManager.getLogger();
   
   public SmallChestBlock(Settings settings) {
      super(settings, () -> TFCBlockEntityType.SMALL_CHEST);
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
   public BlockRenderType getRenderType(BlockState state) {
      return BlockRenderType.MODEL;
   }
   
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
