package ca.rttv.terra.firma.craft;

import ca.rttv.terra.firma.craft.chest.SmallChestBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class TFCBlockEntityType {
   
   public static final BlockEntityType<SmallChestBlockEntity> SMALL_CHEST = BlockEntityType.create("tfc:chest", BlockEntityType.Builder.create((BlockPos pos, BlockState state) -> new SmallChestBlockEntity(pos, state, "oak"), TFCBlocks.OAK_CHEST, TFCBlocks.SPRUCE_CHEST, TFCBlocks.BIRCH_CHEST, TFCBlocks.ACACIA_CHEST));
   
   @SuppressWarnings( "ResultOfMethodCallIgnored" )
   public static void init() {
      SMALL_CHEST.getClass();
   }
}
