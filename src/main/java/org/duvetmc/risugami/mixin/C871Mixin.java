package org.duvetmc.risugami.mixin;

import net.minecraft.class_14;
import net.minecraft.class_379;
import net.minecraft.class_871;
import org.duvetmc.risugami.risuimpl.ModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_871.class)
public abstract class C871Mixin implements class_379 {
    @Shadow private class_379 field_3865;

    @Shadow private class_14 field_3869;

    @Inject(method = "method_1087", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_381;method_1694()V"))
    private void populateChunk(class_379 c379, int i, int j, CallbackInfo ci) {
        ModLoader.PopulateChunk(field_3865, i, j, field_3869);
    }
}
