package org.duvetmc.risugami.mixin;

import net.minecraft.class_14;
import net.minecraft.class_229;
import net.minecraft.class_676;
import net.minecraft.class_708;
import org.duvetmc.risugami.risuimpl.ModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(class_708.class)
public class C708Mixin {
    @Inject(method = "method_2242", at = @At(value = "FIELD", target = "Lnet/minecraft/class_229;field_1166:I", ordinal = 0), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void allowEntityStops(class_14 c14, int p1, int p2, int p3, Random random, CallbackInfo ci, // Standard args, below are locals
                                  int i, int j, int k, class_676 c676, class_229 c229, double d0, double d1, double d2
    ) {
        boolean b = ModLoader.DispenseEntity(c14, d0, d1, d2, j, k, c229);
        if (b) {
            ci.cancel();
        }
    }
}
