package com.example.getexpfrommining_tantn;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.item.ExperienceOrb;
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

    // Gom các tag "đá" vào mảng tĩnh để check nhanh và dễ mở rộng.
    @SuppressWarnings("unchecked")
    private static final TagKey<Block>[] STONE_TAGS = new TagKey[]{
            BlockTags.BASE_STONE_OVERWORLD,
            BlockTags.BASE_STONE_NETHER,
            BlockTags.STONE_ORE_REPLACEABLES,
            BlockTags.DEEPSLATE_ORE_REPLACEABLES
    };

    public GetExpFromMiningMod(final IEventBus modEventBus) {
        // Đăng ký handler lên game event bus; handler tự lọc server-side.
        NeoForge.EVENT_BUS.addListener(GetExpFromMiningMod::onBlockBreak);
    }

    /**
     * Thưởng EXP khi phá block stone-like.
     * Thứ tự kiểm tra: rẻ → đắt để tối ưu.
     */
    private static void onBlockBreak(final BlockEvent.BreakEvent event) {
        // Lọc server-side + event bị hủy + creative + XP=0
        if (!(event.getPlayer() instanceof ServerPlayer sp)) return;
        if (event.isCanceled()) return;
        if (sp.isCreative()) return;
        if (XP_PER_BLOCK <= 0) return;

        // Lấy state một lần, bỏ sớm nếu là air
        final BlockState state = event.getState();
        if (state.isAir()) return;

        // Kiểm tra "stone-like" bằng vòng for trên mảng tĩnh
        boolean isStoneLike = false;
        for (final TagKey<Block> tag : STONE_TAGS) {
            if (state.is(tag)) { isStoneLike = true; break; }
        }
        if (!isStoneLike) return;

        // Award XP bằng orb ngay sát chân người chơi → nhặt tức thì, Mending kích hoạt
        final ServerLevel sl = sp.serverLevel();
        final Vec3 spot = new Vec3(sp.getX(), sp.getBoundingBox().minY + 0.10, sp.getZ());
        ExperienceOrb.award(sl, spot, XP_PER_BLOCK);
    }
}
