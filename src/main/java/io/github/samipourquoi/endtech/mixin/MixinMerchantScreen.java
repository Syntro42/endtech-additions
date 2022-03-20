package io.github.samipourquoi.endtech.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.samipourquoi.endtech.helpers.PerfectTradeHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MerchantScreen.class)
public class MixinMerchantScreen extends Screen {
     private static final Identifier PERFECT_TRADES_TEXTURE = new Identifier("textures/gui/container/perfect_trades.png");

    // Just to avoid IDE errors
    protected MixinMerchantScreen(Text title) {
        super(title);
    }

    @Inject(method = "renderArrow", at = @At("TAIL"))
    private void renderArrow(MatrixStack matrices, TradeOffer tradeOffer, int i, int j, CallbackInfo ci) {
        if (((PerfectTradeHelper) tradeOffer).isPerfectTrade()) {
            RenderSystem.setShaderTexture(0, PERFECT_TRADES_TEXTURE);

            if (!tradeOffer.isDisabled()) {
                drawTexture(matrices, i + 5 + 35 + 20, j + 3, this.getZOffset(), 0.0F, 0.0F, 10, 9, 20, 9);
            } else {
                drawTexture(matrices, i + 5 + 35 + 20, j + 3, this.getZOffset(), 10.0F, 0.0F, 10, 9, 20, 9);
            }
        }
    }
}
