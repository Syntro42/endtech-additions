package io.github.samipourquoi.endtech.mixin;

import io.github.samipourquoi.endtech.helpers.StatsAccessor;
import net.minecraft.block.Block;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockTags.class)
public class MixinBlockTags {
    @Inject(method = "register", at = @At("HEAD"))
    private static void registerBlockTagStats(String id, CallbackInfoReturnable<TagKey<Block>> cir) {
        StatsAccessor.registerTagStat("mined_" + id);
        StatsAccessor.registerTagStat("used_" + id);
        StatsAccessor.registerTagStat("crafted_" + id);
    }
}
