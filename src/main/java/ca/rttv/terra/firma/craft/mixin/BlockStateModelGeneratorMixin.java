package ca.rttv.terra.firma.craft.mixin;

import ca.rttv.terra.firma.craft.TFCBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateModelGenerator.BuiltinModelPool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( BlockStateModelGenerator.class )
public abstract class BlockStateModelGeneratorMixin {
   @Shadow public abstract BuiltinModelPool registerBuiltin(Block block, Block particleBlock);
   
   @Inject(method = "register", at = @At("TAIL"))
   private void register(CallbackInfo ci) {
      this.registerBuiltin(TFCBlocks.OAK_CHEST, Blocks.OBSIDIAN).includeWithoutItem(TFCBlocks.OAK_CHEST);
      this.registerBuiltin(TFCBlocks.ACACIA_CHEST, Blocks.OBSIDIAN).includeWithoutItem(TFCBlocks.ACACIA_CHEST);
      this.registerBuiltin(TFCBlocks.BIRCH_CHEST, Blocks.OBSIDIAN).includeWithoutItem(TFCBlocks.BIRCH_CHEST);
      this.registerBuiltin(TFCBlocks.SPRUCE_CHEST, Blocks.OBSIDIAN).includeWithoutItem(TFCBlocks.SPRUCE_CHEST);
   }
}
