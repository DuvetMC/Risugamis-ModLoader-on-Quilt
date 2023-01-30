package org.duvetmc.risugami.mixin;

import net.minecraft.class_229;
import net.minecraft.class_3;
import net.minecraft.class_672;
import org.duvetmc.risugami.risuimpl.ModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_672.class)
public class C672Mixin {
    @Shadow private class_3 field_3567;

    @Inject(method = "method_790", at = @At(value = "CONSTANT", args = "intValue=0"))
    private void addCraftingCallback(class_229 item, CallbackInfo ci) {
        ModLoader.TakenFromCrafting(field_3567, item);
    }
}
