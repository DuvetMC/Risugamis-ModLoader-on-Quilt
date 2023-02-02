package org.duvetmc.risugami.mixin;

import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Contract;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface McInstanceGetter {
    @Contract(pure = true, value = " -> !null")
    @SuppressWarnings("Contract") // IntelliJ doesn't understand that this is a mixin.
    @Accessor("field_3407")
    static Minecraft getInstance() {
        throw new AssertionError();
    }
}
