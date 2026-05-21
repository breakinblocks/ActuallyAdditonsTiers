package dev.saereth.aatiers;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(AATiers.MOD_ID)
public final class AATiers {
    public static final String MOD_ID = "aatiers";

    public AATiers(IEventBus modBus, ModContainer container) {
        ModRegistry.register(modBus);
        modBus.addListener(AATiersConfig::onLoad);
        container.registerConfig(ModConfig.Type.SERVER, AATiersConfig.SPEC);
    }
}
