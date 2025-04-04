package com.noxcrew.noxesium.mixin.feature;

import static com.noxcrew.noxesium.api.NoxesiumReferences.BUKKIT_COMPOUND_ID;
import static com.noxcrew.noxesium.api.NoxesiumReferences.IMMOVABLE_TAG;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for preventing items dropped from the hotbar.
 * This only prevent items with the {@link com.noxcrew.noxesium.api.NoxesiumReferences#IMMOVABLE_TAG} from being moved.
 */
@Mixin(Inventory.class)
public abstract class UnmovableInventoryMixin {

    @Inject(
            method = "removeFromSelected",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"),
            cancellable = true)
    public void preventMovingImmovables(
            final boolean bl, final CallbackInfoReturnable<ItemStack> cir, @Local final ItemStack stack) {
        final CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) return;
        final CompoundTag tag = data.getUnsafe();
        if (tag == null) return;

        final CompoundTag bukkit = tag.getCompound(BUKKIT_COMPOUND_ID).orElse(null);
        if (bukkit == null || !bukkit.contains(IMMOVABLE_TAG)) return;

        cir.setReturnValue(ItemStack.EMPTY);
    }
}
