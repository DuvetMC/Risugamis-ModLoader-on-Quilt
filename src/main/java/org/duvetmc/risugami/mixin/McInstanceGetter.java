package org.duvetmc.risugami.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface McInstanceGetter {
    @Accessor("field_3407")
    static Minecraft getInstance() {
        throw new AssertionError();
    }
}
