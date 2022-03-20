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
import net.minecraft.state.StateManager;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(Block.class)
public abstract class MixinBlock {
    @Shadow @Final protected StateManager<Block, BlockState> stateManager;

    @Shadow protected abstract Block asBlock();

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

        Map<Identifier, Tag<Block>> tags = BlockTags.getTagGroup().getTags();
        tags.forEach((identifier, tag) -> {
            if (tag.contains(this.asBlock())) {
                Identifier statTagID = StatsAccessor.CUSTOM_TAGS.get("mined_" + identifier.getPath());
                player.incrementStat(statTagID);
            }
        });
    }

    @Inject(method = "onPlaced", at = @At("HEAD"))
    private void incrementUsedStats(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
        if (!world.isClient()) {
            Map<Identifier, Tag<Block>> tags = BlockTags.getTagGroup().getTags();

            tags.forEach((identifier, tag) -> {
                if (tag.contains(this.asBlock())) {
                    Identifier statTagID = StatsAccessor.CUSTOM_TAGS.get("used_" + identifier.getPath());

                    if (placer instanceof PlayerEntity) {
                        ((PlayerEntity) placer).incrementStat(statTagID);
                    }
                }
            });
        }
    }
}
