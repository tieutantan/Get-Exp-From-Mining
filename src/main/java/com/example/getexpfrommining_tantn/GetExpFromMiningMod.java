package com.example.getexpfrommining_tantn;

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

    public static final String MODID = "getexpfrommining_tantn";
    private static final int XP_PER_BLOCK = 3;

    @SuppressWarnings("unchecked")
    private static final TagKey<Block>[] STONE_TAGS = new TagKey[]{
            BlockTags.BASE_STONE_OVERWORLD,
            BlockTags.BASE_STONE_NETHER,
            BlockTags.STONE_ORE_REPLACEABLES,
            BlockTags.DEEPSLATE_ORE_REPLACEABLES
    };

    public GetExpFromMiningMod(final IEventBus modEventBus) {
        NeoForge.EVENT_BUS.addListener(GetExpFromMiningMod::onBlockBreak);
    }

    private static void onBlockBreak(final BlockEvent.BreakEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer sp)) return;
        if (event.isCanceled()) return;
        if (sp.isCreative()) return;
        if (XP_PER_BLOCK <= 0) return;

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
        ExperienceOrb.award(sl, spot, XP_PER_BLOCK);
    }
}
