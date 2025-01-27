package io.github.samipourquoi.endtech.mixin;

import io.github.samipourquoi.endtech.helpers.StatsAccessor;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {
    @Shadow public abstract Item getItem();

    @Inject(method = "onCraft", at = @At("HEAD"))
    private void incrementCraftedStats(World world, PlayerEntity player, int amount, CallbackInfo ci) {
        Identifier itemID = Registry.ITEM.getId(this.getItem());
        if (itemID.getPath().equals("air")) return;

        Block block = Registry.BLOCK.get(itemID);

        Iterator<TagKey<Block>> key = block.getRegistryEntry().streamTags().iterator();
        while (key.hasNext()) {
            if (world.isClient) break;

            Identifier statTagID = StatsAccessor.CUSTOM_TAGS.get("crafted_" + key.next().id().getPath());
            player.increaseStat(statTagID, amount);
        }
    }
}
