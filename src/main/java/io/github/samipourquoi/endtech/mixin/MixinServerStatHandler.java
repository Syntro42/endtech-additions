package io.github.samipourquoi.endtech.mixin;

import io.github.samipourquoi.endtech.helpers.StatsAccessor;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerStatHandler.class)
public abstract class MixinServerStatHandler extends StatHandler {
    @Redirect(method = "sendStats", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2IntMap;put(Ljava/lang/Object;I)I"))
    private int dontPutCustomStat(Object2IntMap<Stat<?>> object2IntMap, Object object, int i) {
        Stat<?> stat = (Stat<?>) object;

        if (StatsAccessor.isCustomStat(stat)) {
            return object2IntMap.defaultReturnValue();
        } else {
            return object2IntMap.put(stat, this.getStat(stat));
        }
    }
}