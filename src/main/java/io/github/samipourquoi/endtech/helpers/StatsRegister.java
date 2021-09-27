package io.github.samipourquoi.endtech.helpers;

import net.minecraft.block.Block;
import net.minecraft.stat.Stats;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class StatsRegister {
    private static final HashMap<String, Identifier> CUSTOM_TAGS = new HashMap<>();

    public static void registerCustomTagStats() {
        Map<Identifier, Tag<Block>> blockTags = BlockTags.getTagGroup().getTags();
        blockTags.forEach((blockIdentified, tag) -> {
            registerTagStats("mined_" + blockIdentified.getPath());
            registerTagStats("used_" + blockIdentified.getPath());
            registerTagStats("crafted_" + blockIdentified.getPath());
        });

        StatsAccessor.CUSTOM_TAGS = CUSTOM_TAGS;
    }

    private static void registerTagStats(String path) {
        Identifier id = new Identifier(path);

        if (Registry.CUSTOM_STAT.containsId(id) == false) {
            Registry.register(Registry.CUSTOM_STAT, path, id);
            CUSTOM_TAGS.put(path, id);
        }
    }
}
