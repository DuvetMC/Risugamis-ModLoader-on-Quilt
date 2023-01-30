package org.duvetmc.risugami.mixin;

import net.minecraft.class_452;
import org.duvetmc.risugami.risuimpl.BaseMod;
import org.duvetmc.risugami.risuimpl.ModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(class_452.class)
public class C452Mixin {
    @ModifyVariable(method = "<init>", index = 5, at = @At(value = "CONSTANT", args = "stringValue="))
    private String injectModloaderInfo(String empty3) {
        StringBuilder sb = new StringBuilder(empty3);
        sb.append("Mods loaded: ")
                .append(ModLoader.getLoadedMods().size() + 1)
                .append("\n");

        sb.append("ModLoader b1.7.3 (on Quilt)\n");

        for (BaseMod baseMod : ModLoader.getLoadedMods()) {
            sb.append(baseMod.getClass().getName())
                    .append(" ")
                    .append(baseMod.Version())
                    .append("\n");
        }

        return sb.toString();
    }
}
