package dev.saereth.aatiers.mixin;

import de.ellpeck.actuallyadditions.mod.tile.CustomEnergyStorage;
import de.ellpeck.actuallyadditions.mod.tile.TileEntityDisplayStand;
import dev.saereth.aatiers.AATiersConfig;
import dev.saereth.aatiers.ITieredDisplayStand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityDisplayStand.class)
public abstract class TileEntityDisplayStandMixin {

    @Shadow
    @Mutable
    @Final
    public CustomEnergyStorage storage;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void aatiers$applyTierStorage(BlockPos pos, BlockState state, CallbackInfo ci) {
        if (state.getBlock() instanceof ITieredDisplayStand tier) {
            AATiersConfig.TierValues values = AATiersConfig.getValues(tier.aatiers$tier());
            this.storage = new CustomEnergyStorage(values.capacity(), values.maxInput(), 0);
        }
    }
}
