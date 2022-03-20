package io.github.samipourquoi.endtech.mixin;

import io.github.samipourquoi.endtech.helpers.StatsAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Mixin(Block.class)
public abstract class MixinBlock {
    @Shadow @Deprecated public abstract RegistryEntry.Reference<Block> getRegistryEntry();

    @Inject(method = "afterBreak", at = @At("HEAD"))
    private void incrementMinedStats(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack, CallbackInfo info) {
        // stat farms!
        if (!this.equals(Blocks.ICE)) {
            player.incrementStat(StatsAccessor.DIG);

            if (stack.getItem() instanceof PickaxeItem) {
                player.incrementStat(StatsAccessor.PICKS);
            } else if (stack.getItem() instanceof AxeItem) {
                player.incrementStat(StatsAccessor.AXES);
            } else if (stack.getItem() instanceof ShovelItem) {
                player.incrementStat(StatsAccessor.SHOVELS);
            } else if (stack.getItem() instanceof HoeItem) {
                player.incrementStat(StatsAccessor.HOES);
            }
        }

        Iterator<TagKey<Block>> tag = this.getRegistryEntry().streamTags().iterator();
        while (tag.hasNext()) {
            Identifier statTagID = StatsAccessor.CUSTOM_TAGS.get("mined_" + tag.next().id().getPath());
            player.incrementStat(statTagID);
        }
    }

    @Inject(method = "onPlaced", at = @At("HEAD"))
    private void incrementUsedStats(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
        Iterator<TagKey<Block>> tag = this.getRegistryEntry().streamTags().iterator();
        while (tag.hasNext()) {
            if (world.isClient()) break;

            Identifier statTagID = StatsAccessor.CUSTOM_TAGS.get("used_" + tag.next().id().getPath());

            if (placer instanceof PlayerEntity) {
                ((PlayerEntity) placer).incrementStat(statTagID);
            }
        }
    }
}
