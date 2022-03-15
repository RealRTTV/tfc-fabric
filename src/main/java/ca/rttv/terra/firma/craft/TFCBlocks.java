package ca.rttv.terra.firma.craft;

import ca.rttv.terra.firma.craft.chest.SmallChestBlock;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;

public class TFCBlocks {
   public static final Block SMALL_CHEST = register("tfc:small_chest", new SmallChestBlock(Settings.of(Material.WOOD).strength(2.5f).sounds(BlockSoundGroup.WOOD)));
   
   private static Block register(String id, Block block) {
      return Registry.register(Registry.BLOCK, id, block);
   }
   
   public static void init() {
      SMALL_CHEST.getClass();
   }
}
