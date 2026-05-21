package dev.saereth.aatiers;

import de.ellpeck.actuallyadditions.mod.blocks.BlockDisplayStand;

public class TieredDisplayStandBlock extends BlockDisplayStand implements ITieredDisplayStand {
    private final Tier tier;

    public TieredDisplayStandBlock(Tier tier) {
        super(false);
        this.tier = tier;
    }

    @Override
    public Tier aatiers$tier() {
        return tier;
    }
}
