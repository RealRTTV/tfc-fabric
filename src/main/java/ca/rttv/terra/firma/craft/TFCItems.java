package ca.rttv.terra.firma.craft;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TFCItems {
   public static final Item SMALL_CHEST = register(TFCBlocks.SMALL_CHEST, ItemGroup.TRANSPORTATION);
   
   private static Item register(String id, Item item) {
      return Registry.register(Registry.ITEM, id, item);
   }
   
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
   
   public static void init() {
      SMALL_CHEST.getClass();
   }
}
