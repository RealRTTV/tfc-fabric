package ca.rttv.terra.firma.craft;

import ca.rttv.terra.firma.craft.chest.SmallChestScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.ScreenHandlerType.Factory;
import net.minecraft.util.registry.Registry;

public class TFCScreenHandlerType {
   
   public static final ScreenHandlerType<SmallChestScreenHandler> SMALL_CHEST = register("tfc:chest", SmallChestScreenHandler::new);
   
   @SuppressWarnings( "ResultOfMethodCallIgnored" )
   public static void init() {
      SMALL_CHEST.getClass();
   }
   
   private static <T extends ScreenHandler> ScreenHandlerType<T> register(String id, Factory<T> factory) {
      return Registry.register(Registry.SCREEN_HANDLER, id, new ScreenHandlerType<>(factory));
   }
}
