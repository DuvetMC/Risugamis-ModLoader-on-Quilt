package org.duvetmc.risugami.mixin;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(class_365.class)
public abstract class C365Mixin extends class_262 implements class_267 {
    @Shadow public int field_1747;

    @Shadow protected abstract boolean method_1109();

    @Shadow public int field_1748;

    @Shadow protected abstract int method_1178(class_229 arg);

    @Shadow private class_229[] field_1746;

    @Shadow public abstract boolean method_1108();

    @Shadow public int field_1749;

    @Shadow public abstract void method_1562();

    /**
     * @author Risugami
     * @reason Crafting remainders
     */
    @Overwrite
    public void method_1104() {
        boolean flag = this.field_1747 > 0;
        boolean flag1 = false;
        if (this.field_1747 > 0) {
            --this.field_1747;
        }

        if (!this.field_1904.field_2364) {
            if (this.field_1747 == 0 && this.method_1109()) {
                this.field_1748 = this.field_1747 = this.method_1178(this.field_1746[1]);
                if (this.field_1747 > 0) {
                    flag1 = true;
                    if (this.field_1746[1] != null) {
                        if (this.field_1746[1].method_684().method_2179()) {
                            this.field_1746[1] = new class_229(this.field_1746[1].method_684().method_2178());
                        } else {
                            --this.field_1746[1].field_1164;
                        }
                        if (this.field_1746[1].field_1164 == 0) {
                            this.field_1746[1] = null;
                        }
                    }
                }
            }

            if (this.method_1108() && this.method_1109()) {
                ++this.field_1749;
                if (this.field_1749 == 200) {
                    this.field_1749 = 0;
                    this.method_1562();
                    flag1 = true;
                }
            } else {
                this.field_1749 = 0;
            }

            if (flag != this.field_1747 > 0) {
                flag1 = true;
                class_367.method_1580(this.field_1747 > 0, this.field_1904, this.field_1905, this.field_1906, this.field_1907);
            }
        }

        if (flag1) {
            this.method_1213();
        }
    }
}
