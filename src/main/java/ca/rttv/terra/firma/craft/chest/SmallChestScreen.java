package ca.rttv.terra.firma.craft.chest;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SmallChestScreen extends HandledScreen<SmallChestScreenHandler> {
   
   public static final Identifier TEXTURE = new Identifier("tfc", "textures/gui/small_chest.png");
   
   public SmallChestScreen(SmallChestScreenHandler handler, PlayerInventory inventory, Text title) {
      super(handler, inventory, title);
      backgroundHeight = 149;
   }
   
   @Override
   protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
      int i = (this.width - this.backgroundWidth) / 2;
      int j = (this.height - this.backgroundHeight) / 2;
      RenderSystem.setShader(GameRenderer::getPositionTexShader);
      RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
      RenderSystem.setShaderTexture(0, TEXTURE);
      this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
      
   }
   
   @Override
   protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
   
   }
   
   @Override
   public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
      this.renderBackground(matrices);
      super.render(matrices, mouseX, mouseY, delta);
      this.drawMouseoverTooltip(matrices, mouseX, mouseY);
   }
}
