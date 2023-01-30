package org.duvetmc.risugami.mixin;

import net.minecraft.class_227;
import net.minecraft.class_229;
import net.minecraft.class_3;
import org.duvetmc.risugami.risuimpl.ModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_227.class)
public class C227Mixin {
    @Shadow public class_229 field_1156;

    @Inject(method = "method_1073", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_14;method_613(Lnet/minecraft/class_1;Ljava/lang/String;FF)V"))
    private void pickUpItem(class_3 args, CallbackInfo ci) {
        ModLoader.OnItemPickup(args, field_1156);
    }
}
