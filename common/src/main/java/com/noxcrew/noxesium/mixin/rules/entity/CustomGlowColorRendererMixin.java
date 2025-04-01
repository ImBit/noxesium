package com.noxcrew.noxesium.mixin.rules.entity;

import com.noxcrew.noxesium.feature.entity.ExtraEntityData;
import java.awt.Color;
import java.util.Optional;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class CustomGlowColorRendererMixin {


    @Inject(method = "getTeamColor", at = @At("RETURN"), cancellable = true)
    public void injectChangeColorValue(CallbackInfoReturnable<Integer> cir) {
        Entity entity = ((Entity)(Object)this);
        Player player = Minecraft.getInstance().player;

        if (player == null) {
            return;
        }

        Optional<Color> customGlowColor = entity.noxesium$getExtraData(ExtraEntityData.CUSTOM_GLOW_COLOR);
        customGlowColor.ifPresent(color -> cir.setReturnValue(color.getRGB()));
    }
}