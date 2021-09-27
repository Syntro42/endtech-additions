package io.github.samipourquoi.endtech.mixin;

import io.github.samipourquoi.endtech.helpers.StatsAccessor;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Registers the custom stats. Current list is:
 * <ul>
 *     <li><strong>minecraft.custom:minecraft.dig</strong> total of blocks mined, exclude ice</li>
 *     <li><strong>minecraft.custom:minecraft.picks</strong> total of blocks mined with a pickaxe</li>
 *     <li><strong>minecraft.custom:minecraft.shovels</strong> total of blocks mined with a shovel</li>
 *     <li><strong>minecraft.custom:minecraft.axes</strong> total of blocks mined with a axe</li>
 *     <li><strong>minecraft.custom:minecraft.hoes</strong> total of blocks mined with a hoe</li>
 * </ul>
 */
@Mixin(Stats.class)
public abstract class MixinStats {
    private static final Identifier DIG;
    private static final Identifier PICKS;
    private static final Identifier SHOVELS;
    private static final Identifier AXES;
    private static final Identifier HOES;

    static {
        // Registers the custom stats
        DIG = Registry.register(Registry.CUSTOM_STAT,"dig", new Identifier("dig"));
        PICKS = Registry.register(Registry.CUSTOM_STAT,"picks", new Identifier("picks"));
        SHOVELS = Registry.register(Registry.CUSTOM_STAT,"shovels", new Identifier("shovels"));
        AXES = Registry.register(Registry.CUSTOM_STAT,"axes", new Identifier("axes"));
        HOES = Registry.register(Registry.CUSTOM_STAT,"hoes", new Identifier("hoes"));

        // Pass through the custom stats to a class, allowing
        // us to retrieve these fields later on.
        StatsAccessor.DIG = DIG;
        StatsAccessor.PICKS = PICKS;
        StatsAccessor.SHOVELS = SHOVELS;
        StatsAccessor.AXES = AXES;
        StatsAccessor.HOES = HOES;
    }
}
