package ca.rttv.terra.firma.craft;

import ca.rttv.terra.firma.craft.chest.SmallChestBlockEntity;
import net.minecraft.block.entity.BlockEntityType;

public class TFCBlockEntityType {
   
   public static final BlockEntityType<SmallChestBlockEntity> SMALL_CHEST = BlockEntityType.create("tfc:small_chest", BlockEntityType.Builder.create(SmallChestBlockEntity::new, TFCBlocks.SMALL_CHEST));
   
   public static void init() {
      SMALL_CHEST.getClass();
   }
}
