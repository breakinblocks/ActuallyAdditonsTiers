package dev.saereth.aatiers;

import de.ellpeck.actuallyadditions.mod.blocks.ActuallyBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.Map;

public final class ModRegistry {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AATiers.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AATiers.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AATiers.MOD_ID);

    public static final Map<Tier, DeferredBlock<TieredDisplayStandBlock>> STANDS = new EnumMap<>(Tier.class);
    public static final Map<Tier, DeferredItem<BlockItem>> STAND_ITEMS = new EnumMap<>(Tier.class);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = TABS.register(
            "main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.aatiers"))
                    .icon(() -> new ItemStack(STAND_ITEMS.get(Tier.DIAMOND).get()))
                    .displayItems((params, output) -> {
                        for (Tier tier : Tier.values()) {
                            output.accept(STAND_ITEMS.get(tier).get());
                        }
                    })
                    .build()
    );

    static {
        for (Tier tier : Tier.values()) {
            String id = tier.getName() + "_display_stand";
            DeferredBlock<TieredDisplayStandBlock> block = BLOCKS.register(id, () -> new TieredDisplayStandBlock(tier));
            STANDS.put(tier, block);
            STAND_ITEMS.put(tier, ITEMS.register(id, () -> new BlockItem(block.get(), new Item.Properties())));
        }
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        TABS.register(bus);
        bus.addListener(ModRegistry::onAddValidBlocks);
    }

    private static void onAddValidBlocks(BlockEntityTypeAddBlocksEvent event) {
        Block[] blocks = STANDS.values().stream()
                .map(DeferredBlock::get)
                .toArray(Block[]::new);
        event.modify(ActuallyBlocks.DISPLAY_STAND.getTileEntityType(), blocks);
    }

    private ModRegistry() {}
}
