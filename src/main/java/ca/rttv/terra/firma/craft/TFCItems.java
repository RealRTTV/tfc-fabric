package ca.rttv.terra.firma.craft;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TFCItems {
   public static final Item ACACIA_CHEST = register(TFCBlocks.ACACIA_CHEST, ItemGroup.DECORATIONS);
   public static final Item ASH_CHEST = register(TFCBlocks.ASH_CHEST, ItemGroup.DECORATIONS);
   public static final Item BIRCH_CHEST = register(TFCBlocks.BIRCH_CHEST, ItemGroup.DECORATIONS);
   public static final Item KAPOK_CHEST = register(TFCBlocks.KAPOK_CHEST, ItemGroup.DECORATIONS);
   public static final Item OAK_CHEST = register(TFCBlocks.OAK_CHEST, ItemGroup.DECORATIONS);
   public static final Item SPRUCE_CHEST = register(TFCBlocks.SPRUCE_CHEST, ItemGroup.DECORATIONS);
   
   private static Item register(String id, Item item) {
      return Registry.register(Registry.ITEM, id, item);
   }
   
   @SuppressWarnings( "SameParameterValue" )
   private static Item register(Block block, ItemGroup group) {
      return register(new BlockItem(block, (new Settings()).group(group)));
   }
   
   private static Item register(BlockItem item) {
      return register(item.getBlock(), item);
   }
   
   protected static Item register(Block block, Item item) {
      return register(Registry.BLOCK.getId(block), item);
   }
   
   private static Item register(Identifier id, Item item) {
      if (item instanceof BlockItem) {
         ((BlockItem)item).appendBlocks(Item.BLOCK_ITEMS, item);
      }
      
      return Registry.register(Registry.ITEM, id, item);
   }
   
   @SuppressWarnings( "ResultOfMethodCallIgnored" )
   public static void init() {
      ACACIA_CHEST.getClass();
      ASH_CHEST.getClass();
      BIRCH_CHEST.getClass();
      KAPOK_CHEST.getClass();
      OAK_CHEST.getClass();
      SPRUCE_CHEST.getClass();
   }
}
