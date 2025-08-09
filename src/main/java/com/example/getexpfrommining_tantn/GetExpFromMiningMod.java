package com.example.getexpfrommining_tantn;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.minecraft.tags.BlockTags;

/**
 * Mod: Get EXP from Mining
 * +1 EXP khi người chơi phá block thuộc nhóm "stone-like".
 */
@Mod(GetExpFromMiningMod.MODID)
public final class GetExpFromMiningMod {

    public static final String MODID = "getexpfrommining_tantn";
    private static final int XP_PER_BLOCK = 1;

    public GetExpFromMiningMod(final IEventBus modEventBus) {
        // Đăng ký handler lên game event bus; handler tự lọc server-side.
        NeoForge.EVENT_BUS.addListener(GetExpFromMiningMod::onBlockBreak);
    }

    /**
     * Thưởng EXP khi phá block stone-like.
     * Thứ tự kiểm tra theo chiến lược early-return rẻ → đắt:
     * 1) Người chơi phải là ServerPlayer (lọc client)
     * 2) Event không bị hủy
     * 3) State không phải air
     * 4) Bỏ qua Creative (giữ nguyên hành vi hiện tại)
     * 5) Kiểm tra tag stone-like
     */
    private static void onBlockBreak(final BlockEvent.BreakEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer sp)) {
            return;
        }
        if (event.isCanceled()) {
            return;
        }

        final BlockState state = event.getState();
        if (state.isAir()) {
            return;
        }
        if (sp.isCreative()) {
            return;
        }

        // Chỉ khi cần mới kiểm tra tag (đắt hơn các check trên)
        final boolean isStoneLike =
                state.is(BlockTags.BASE_STONE_OVERWORLD) ||
                state.is(BlockTags.BASE_STONE_NETHER) ||
                state.is(BlockTags.STONE_ORE_REPLACEABLES) ||
                state.is(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        if (!isStoneLike) {
            return;
        }

        sp.giveExperiencePoints(XP_PER_BLOCK);
    }
}
