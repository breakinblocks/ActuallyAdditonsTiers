package dev.saereth.aatiers;

public enum Tier {
    IRON("iron", 400_000, 5_000),
    GOLD("gold", 1_000_000, 15_000),
    DIAMOND("diamond", 4_000_000, 40_000),
    NETHERITE("netherite", 8_000_000, 100_000);

    private final String name;
    private final int defaultCapacity;
    private final int defaultMaxInput;

    Tier(String name, int defaultCapacity, int defaultMaxInput) {
        this.name = name;
        this.defaultCapacity = defaultCapacity;
        this.defaultMaxInput = defaultMaxInput;
    }

    public String getName() {
        return name;
    }

    public int getDefaultCapacity() {
        return defaultCapacity;
    }

    public int getDefaultMaxInput() {
        return defaultMaxInput;
    }
}
