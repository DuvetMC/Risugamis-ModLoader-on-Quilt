package org.duvetmc.risugami.risuimpl;

import net.minecraft.class_97;
import net.minecraft.client.Minecraft;

public class EntityRendererProxy extends class_97 {
    private Minecraft game;

    public EntityRendererProxy(Minecraft minecraft) {
        super(minecraft);
        this.game = minecraft;
    }

    public void method_1490(float f1) {
        super.method_1490(f1);
        ModLoader.OnTick(this.game);
    }
}
