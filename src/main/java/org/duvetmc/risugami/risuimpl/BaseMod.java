package org.duvetmc.risugami.risuimpl;

import java.util.Map;
import java.util.Random;
import net.minecraft.class_1;
import net.minecraft.class_123;
import net.minecraft.class_14;
import net.minecraft.class_18;
import net.minecraft.class_214;
import net.minecraft.class_215;
import net.minecraft.class_229;
import net.minecraft.class_3;
import net.minecraft.class_447;
import net.minecraft.class_49;
import net.minecraft.client.Minecraft;

public abstract class BaseMod {
    public int AddFuel(int id) {
        return 0;
    }

    public void AddRenderer(Map<Class<? extends class_1>, class_214> renderers) {
    }

    public boolean DispenseEntity(class_14 world, double x, double y, double z, int xVel, int zVel, class_229 item) {
        return false;
    }

    public void GenerateNether(class_14 world, Random random, int chunkX, int chunkZ) {
    }

    public void GenerateSurface(class_14 world, Random random, int chunkX, int chunkZ) {
    }

    public void KeyboardEvent(class_123 event) {
    }

    public void ModsLoaded() {
    }

    public boolean OnTickInGame(Minecraft game) {
        return false;
    }

    public boolean OnTickInGUI(Minecraft game, class_49 gui) {
        return false;
    }

    public void RegisterAnimation(Minecraft game) {
    }

    public void RenderInvBlock(class_215 renderer, class_18 block, int metadata, int modelID) {
    }

    public boolean RenderWorldBlock(class_215 renderer, class_447 world, int x, int y, int z, class_18 block, int modelID) {
        return false;
    }

    public void TakenFromCrafting(class_3 player, class_229 item) {
    }

    public void TakenFromFurnace(class_3 player, class_229 item) {
    }

    public void OnItemPickup(class_3 player, class_229 item) {
    }

    @Override
    public String toString() {
        return this.getClass().getName() + " " + this.Version();
    }

    public abstract String Version();
}
