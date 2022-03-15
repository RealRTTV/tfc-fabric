package ca.rttv.terra.firma.craft.chest;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class SmallChestScreen extends HandledScreen<SmallChestScreenHandler> {
   
   public SmallChestScreen(SmallChestScreenHandler handler, PlayerInventory inventory, Text title) {
      super(handler, inventory, title);
   }
   
   @Override
   protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
      DiffuseLighting.disableGuiDepthLighting();
   }
}
