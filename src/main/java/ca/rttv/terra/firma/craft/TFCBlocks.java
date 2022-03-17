package ca.rttv.terra.firma.craft;

import ca.rttv.terra.firma.craft.chest.SmallChestBlock;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;

public class TFCBlocks {
   public static final Block ACACIA_CHEST = register("tfc:wood/chests/acacia", new SmallChestBlock(Settings.of(Material.WOOD).strength(2.5f).sounds(BlockSoundGroup.WOOD), "acacia"));
   public static final Block BIRCH_CHEST  = register("tfc:wood/chests/birch",  new SmallChestBlock(Settings.of(Material.WOOD).strength(2.5f).sounds(BlockSoundGroup.WOOD), "birch" ));
   public static final Block OAK_CHEST    = register("tfc:wood/chests/oak",    new SmallChestBlock(Settings.of(Material.WOOD).strength(2.5f).sounds(BlockSoundGroup.WOOD), "oak"   ));
   public static final Block SPRUCE_CHEST = register("tfc:wood/chests/spruce", new SmallChestBlock(Settings.of(Material.WOOD).strength(2.5f).sounds(BlockSoundGroup.WOOD), "spruce"));
   
   @SuppressWarnings( "ResultOfMethodCallIgnored" )
   public static void init() {
      ACACIA_CHEST.getClass();
      OAK_CHEST.getClass();
      BIRCH_CHEST.getClass();
      SPRUCE_CHEST.getClass();
   }
   
   private static Block register(String id, Block block) {
      return Registry.register(Registry.BLOCK, id, block);
   }
}
