package org.duvetmc.risugami.mixin;

import net.minecraft.class_1;
import net.minecraft.class_214;
import net.minecraft.class_274;
import org.duvetmc.risugami.risuimpl.ModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(class_274.class)
public class C274Mixin {
    @Shadow private Map<Class<? extends class_1>, class_214> field_1358;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/Map;values()Ljava/util/Collection;"))
    private void init(CallbackInfo ci) {
        ModLoader.AddAllRenderers(field_1358);
    }
}
