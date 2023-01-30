package org.duvetmc.risugami.mixin;

import net.minecraft.class_229;
import net.minecraft.class_230;
import net.minecraft.class_3;
import net.minecraft.class_843;
import org.duvetmc.risugami.risuimpl.ModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_843.class)
public class C843Mixin {
    @Shadow private class_3 field_3727;

    @Inject(method = "method_790", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_230;method_790(Lnet/minecraft/class_229;)V"))
    private void takenFromFurnace(class_229 c229, CallbackInfo ci) {
        ModLoader.TakenFromFurnace(field_3727, c229);
    }
}
