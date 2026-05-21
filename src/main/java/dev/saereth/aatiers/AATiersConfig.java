package dev.saereth.aatiers;

import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.EnumMap;
import java.util.Map;

public final class AATiersConfig {
    public record TierValues(int capacity, int maxInput) {}

    private static final Map<Tier, ModConfigSpec.IntValue> CAPACITY = new EnumMap<>(Tier.class);
    private static final Map<Tier, ModConfigSpec.IntValue> MAX_INPUT = new EnumMap<>(Tier.class);
    private static final Map<Tier, TierValues> CACHE = new EnumMap<>(Tier.class);

    public static final ModConfigSpec SPEC;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.comment("Per-tier display stand energy values. Changes take effect on world reload.").push("tiers");
        for (Tier tier : Tier.values()) {
            builder.push(tier.getName());
            CAPACITY.put(tier, builder
                    .comment("Maximum energy buffer for the " + tier.getName() + " display stand (FE).")
                    .defineInRange("capacity", tier.getDefaultCapacity(), 1, Integer.MAX_VALUE));
            MAX_INPUT.put(tier, builder
                    .comment("Maximum energy input rate for the " + tier.getName() + " display stand (FE/tick).")
                    .defineInRange("maxInput", tier.getDefaultMaxInput(), 1, Integer.MAX_VALUE));
            builder.pop();
        }
        builder.pop();
        SPEC = builder.build();
    }

    public static TierValues getValues(Tier tier) {
        TierValues cached = CACHE.get(tier);
        if (cached != null) return cached;
        if (!SPEC.isLoaded()) {
            return new TierValues(tier.getDefaultCapacity(), tier.getDefaultMaxInput());
        }
        TierValues fresh = new TierValues(CAPACITY.get(tier).get(), MAX_INPUT.get(tier).get());
        CACHE.put(tier, fresh);
        return fresh;
    }

    public static void onLoad(ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC) {
            CACHE.clear();
        }
    }

    private AATiersConfig() {}
}
