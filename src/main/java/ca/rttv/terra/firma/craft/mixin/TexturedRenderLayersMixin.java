package ca.rttv.terra.firma.craft.mixin;

import ca.rttv.terra.firma.craft.chest.SmallChestBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( TexturedRenderLayers.class )
public abstract class TexturedRenderLayersMixin {
   @Inject( method = "getChestTexture(Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/block/enums/ChestType;Z)Lnet/minecraft/client/util/SpriteIdentifier;", at = @At( "HEAD" ), cancellable = true )
   private static void getChestTexture(BlockEntity blockEntity, ChestType type, boolean christmas, CallbackInfoReturnable<SpriteIdentifier> cir) {
      if (blockEntity instanceof SmallChestBlockEntity smallChest) {
         cir.setReturnValue(getChestTexture(type,
                                            new SpriteIdentifier(new Identifier("textures/atlas/blocks.png"), new Identifier("tfc", "block/wood/chests/normal/" + smallChest.getWoodType())),
                                            new SpriteIdentifier(new Identifier("textures/atlas/blocks.png"), new Identifier("tfc", "block/wood/chests/normal_left/" + smallChest.getWoodType())),
                                            new SpriteIdentifier(new Identifier("textures/atlas/blocks.png"), new Identifier("tfc", "block/wood/chests/normal_right/" + smallChest.getWoodType()))));
      }
   }
   
   @Shadow
   private static SpriteIdentifier getChestTexture(ChestType type, SpriteIdentifier single, SpriteIdentifier left, SpriteIdentifier right) {
      return null;
   }
}
