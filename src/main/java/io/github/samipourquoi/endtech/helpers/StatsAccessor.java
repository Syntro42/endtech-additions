package io.github.samipourquoi.endtech.helpers;

import io.github.samipourquoi.endtech.mixin.MixinStats;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;

/**
 * We can't access a static field/method added via a Mixin.
 * However we do need to access to {@link MixinStats} DIG static
 * field (for instance). <p>
 * To solve that issue, we use that class to make a ‘bridge',
 * allowing us to get the needed static fields.
 *
 * @see MixinStats
 * @author samipourquoi
 */
public class StatsAccessor {
    public static Identifier DIG;
    public static Identifier PICKS;
    public static Identifier SHOVELS;
    public static Identifier AXES;
    public static Identifier HOES;

    public static HashMap<String, Identifier> CUSTOM_TAGS = new HashMap<>();

    public static void registerTagStat(String path) {
        Identifier id = new Identifier(path);

        if (!Registry.CUSTOM_STAT.containsId(id)) {
            Registry.register(Registry.CUSTOM_STAT, path, id);
            Stats.CUSTOM.getOrCreateStat(id);
            CUSTOM_TAGS.put(path, id);
        }
    }

    public static boolean isCustomStat(Stat<?> stat) {
        return StatsAccessor.CUSTOM_TAGS.containsValue(stat.getValue())||
                StatsAccessor.PICKS.equals(stat.getValue()) ||
                StatsAccessor.SHOVELS.equals(stat.getValue()) ||
                StatsAccessor.AXES.equals(stat.getValue()) ||
                StatsAccessor.HOES.equals(stat.getValue()) ||
                StatsAccessor.DIG.equals(stat.getValue());
    }
}
