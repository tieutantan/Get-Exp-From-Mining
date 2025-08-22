package com.example.tantn_getexpfromnature;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;

/**
 * Mod: Get EXP from Mining
 * +XP khi người chơi phá block thuộc nhóm "stone-like".
 */
@Mod(GetExpFromMiningMod.MODID)
public final class GetExpFromMiningMod {

    public static final String MODID = "tantn_getexpfromnature";
    private static final int XP_PER_STONE_BLOCK = 2;
    private static final int XP_PER_TREE_BLOCK = 1;

    @SuppressWarnings("unchecked")
    private static final TagKey<Block>[] STONE_TAGS = new TagKey[]{
            BlockTags.BASE_STONE_OVERWORLD,
            BlockTags.BASE_STONE_NETHER,
            BlockTags.STONE_ORE_REPLACEABLES,
            BlockTags.DEEPSLATE_ORE_REPLACEABLES
    };

    @SuppressWarnings("unchecked")
    private static final TagKey<Block>[] TREE_TAGS = new TagKey[]{
            BlockTags.LOGS,
            BlockTags.LEAVES
    };

    public GetExpFromMiningMod(final IEventBus modEventBus) {
        NeoForge.EVENT_BUS.addListener(GetExpFromMiningMod::onStoneBlockBreak);
        NeoForge.EVENT_BUS.addListener(GetExpFromMiningMod::onTreeBlockBreak);
    }

    private static void onStoneBlockBreak(final BlockEvent.BreakEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer sp)) return;
        if (event.isCanceled()) return;
        if (sp.isCreative()) return;
        if (XP_PER_STONE_BLOCK <= 0) return;

        final BlockState state = event.getState();
        if (state.isAir()) return;

        boolean isStoneLike = false;
        for (final TagKey<Block> tag : STONE_TAGS) {
            if (state.is(tag)) { isStoneLike = true; break; }
        }
        if (!isStoneLike) return;

        final ServerLevel sl = sp.serverLevel();

        // Lấy hướng nhìn chuẩn hóa
        final Vec3 lookDir = sp.getLookAngle().normalize();

        // Tính vị trí spawn: trước mặt 0.5 block, cao hơn mặt đất 0.4 block
        final Vec3 spot = new Vec3(
            sp.getX() + lookDir.x * 0.5,
            sp.getBoundingBox().minY + 0.4,
            sp.getZ() + lookDir.z * 0.5
        );

        // Spawn orb ở vị trí vừa tính
        ExperienceOrb.award(sl, spot, XP_PER_STONE_BLOCK);
    }

    private static void onTreeBlockBreak(final BlockEvent.BreakEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer sp)) return;
        if (event.isCanceled()) return;
        if (sp.isCreative()) return;
        if (XP_PER_TREE_BLOCK <= 0) return;

        final BlockState state = event.getState();
        if (state.isAir()) return;

        boolean isTreeLike = false;
        for (final TagKey<Block> tag : TREE_TAGS) {
            if (state.is(tag)) { isTreeLike = true; break; }
        }
        if (!isTreeLike) return;

        final ServerLevel sl = sp.serverLevel();

        // Lấy hướng nhìn chuẩn hóa
        final Vec3 lookDir = sp.getLookAngle().normalize();

        // Tính vị trí spawn: trước mặt 0.5 block, cao hơn mặt đất 0.4 block
        final Vec3 spot = new Vec3(
            sp.getX() + lookDir.x * 0.5,
            sp.getBoundingBox().minY + 0.4,
            sp.getZ() + lookDir.z * 0.5
        );

        // Spawn orb ở vị trí vừa tính
        ExperienceOrb.award(sl, spot, XP_PER_TREE_BLOCK);
    }
}
