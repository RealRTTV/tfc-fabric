package ca.rttv.terra.firma.craft.chest;

import ca.rttv.terra.firma.craft.TFCBlockEntityType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.util.math.BlockPos;

public class SmallChestBlockEntity extends ChestBlockEntity {
   public SmallChestBlockEntity(BlockPos pos, BlockState state) {
      super(TFCBlockEntityType.SMALL_CHEST, pos, state);
   }
}
