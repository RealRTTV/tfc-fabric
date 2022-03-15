package ca.rttv.terra.firma.craft.chest;

import ca.rttv.terra.firma.craft.TFCBlockEntityType;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SmallChestBlock extends ChestBlock {
   public SmallChestBlock(Settings settings) {
      super(settings, () -> TFCBlockEntityType.SMALL_CHEST);
   }
   
   @Override
   public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
      return new SmallChestBlockEntity(pos, state);
   }
   
   @Override
   public BlockRenderType getRenderType(BlockState state) {
      return BlockRenderType.MODEL;
   }
   
   @Override
   public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
      BlockEntity blockEntity = world.getBlockEntity(pos);
      if (blockEntity instanceof SmallChestBlockEntity) {
         return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new SmallChestScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos)), Text.of(""));
      }
      return null;
   }
}
