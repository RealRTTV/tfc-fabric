package ca.rttv.terra.firma.craft.mixin;

import ca.rttv.terra.firma.craft.TFCBlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin( BlockEntityRendererFactories.class )
public abstract class BlockEntityRendererFactoriesMixin {
   static {
      BlockEntityRendererFactories.register(TFCBlockEntityType.SMALL_CHEST, ChestBlockEntityRenderer::new);
   }
}
