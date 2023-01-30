package org.duvetmc.risugami.mixin;

import net.minecraft.*;
import org.duvetmc.risugami.Public;
import org.duvetmc.risugami.Static;
import org.duvetmc.risugami.impl.C215Duck;
import org.duvetmc.risugami.risuimpl.ModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.class_215.class)
public class C215Mixin implements C215Duck {
	@Shadow @Public
	private class_447 field_1098;

	@Shadow @Public
	private int field_1095;

	@Static
	public boolean cfgGrassFix = true;

	@Public // Using this to access in clinit
	private static float[][] redstoneColors = new float[16][];

	@Public // to avoid local slot `0` from being used as `this`
	private static void setRedstoneColors(float[][] colors) {
		redstoneColors = colors;
	}

	static {
		for (int i = 0; i < redstoneColors.length; i++) {
			float f = i / 15.0F;
			float f1 = f * 0.6F + 0.4F;
			if (i == 0) {
				f = 0.0F;
			}

			float f2 = f * f * 0.7F - 0.5F;
			float f3 = f * f * 0.6F - 0.7F;
			if (f2 < 0.0F) {
				f2 = 0.0F;
			}

			if (f3 < 0.0F) {
				f3 = 0.0F;
			}

			redstoneColors[i] = new float[]{f1, f2, f3};
		}
	}

	/**
	 * @author sschr15, Notch, and whoever-the-hec made Risugami's ModLoader
	 * @reason Redstone colors
	 */
	@Overwrite
	public boolean method_1511(class_18 uu1, int i1, int j1, int k1) {
		class_17 c17 = class_17.field_274;
		int i = this.field_1098.method_1052(i1, j1, k1);
		int j = uu1.method_877(1, i);
		if (this.field_1095 >= 0) {
			j = this.field_1095;
		}

		float f = uu1.method_249(this.field_1098, i1, j1, k1);
		float[] afloat = redstoneColors[i];
		float f1 = afloat[0];
		float f2 = afloat[1];
		float f3 = afloat[2];
		c17.method_211(f * f1, f * f2, f * f3);
		int k = (j & 15) << 4;
		int l = j & 240;
		double d0 = (float)k / 256.0F;
		double d1 = ((float)k + 15.99F) / 256.0F;
		double d2 = (float)l / 256.0F;
		double d3 = ((float)l + 15.99F) / 256.0F;
		boolean flag = class_490.method_1740(this.field_1098, i1 - 1, j1, k1, 1)
			|| !this.field_1098.method_471(i1 - 1, j1, k1) && class_490.method_1740(this.field_1098, i1 - 1, j1 - 1, k1, -1);
		boolean flag1 = class_490.method_1740(this.field_1098, i1 + 1, j1, k1, 3)
			|| !this.field_1098.method_471(i1 + 1, j1, k1) && class_490.method_1740(this.field_1098, i1 + 1, j1 - 1, k1, -1);
		boolean flag2 = class_490.method_1740(this.field_1098, i1, j1, k1 - 1, 2)
			|| !this.field_1098.method_471(i1, j1, k1 - 1) && class_490.method_1740(this.field_1098, i1, j1 - 1, k1 - 1, -1);
		boolean flag3 = class_490.method_1740(this.field_1098, i1, j1, k1 + 1, 0)
			|| !this.field_1098.method_471(i1, j1, k1 + 1) && class_490.method_1740(this.field_1098, i1, j1 - 1, k1 + 1, -1);
		if (!this.field_1098.method_471(i1, j1 + 1, k1)) {
			if (this.field_1098.method_471(i1 - 1, j1, k1) && class_490.method_1740(this.field_1098, i1 - 1, j1 + 1, k1, -1)) {
				flag = true;
			}

			if (this.field_1098.method_471(i1 + 1, j1, k1) && class_490.method_1740(this.field_1098, i1 + 1, j1 + 1, k1, -1)) {
				flag1 = true;
			}

			if (this.field_1098.method_471(i1, j1, k1 - 1) && class_490.method_1740(this.field_1098, i1, j1 + 1, k1 - 1, -1)) {
				flag2 = true;
			}

			if (this.field_1098.method_471(i1, j1, k1 + 1) && class_490.method_1740(this.field_1098, i1, j1 + 1, k1 + 1, -1)) {
				flag3 = true;
			}
		}

		float f4 = (float)(i1);
		float f5 = (float)(i1 + 1);
		float f6 = (float)(k1);
		float f7 = (float)(k1 + 1);
		byte b0 = 0;
		if ((flag || flag1) && !flag2 && !flag3) {
			b0 = 1;
		}

		if ((flag2 || flag3) && !flag1 && !flag) {
			b0 = 2;
		}

		if (b0 != 0) {
			d0 = (float)(k + 16) / 256.0F;
			d1 = ((float)(k + 16) + 15.99F) / 256.0F;
			d2 = (float)l / 256.0F;
			d3 = ((float)l + 15.99F) / 256.0F;
		}

		if (b0 == 0) {
			if (flag1 || flag2 || flag3) {
				if (!flag) {
					f4 += 0.3125F;
				}

				if (!flag) {
					d0 += 0.01953125;
				}

				if (!flag1) {
					f5 -= 0.3125F;
				}

				if (!flag1) {
					d1 -= 0.01953125;
				}

				if (!flag2) {
					f6 += 0.3125F;
				}

				if (!flag2) {
					d2 += 0.01953125;
				}

				if (!flag3) {
					f7 -= 0.3125F;
				}

				if (!flag3) {
					d3 -= 0.01953125;
				}
			}

			c17.method_149(f5, (float)j1 + 0.015625F, f7, d1, d3);
			c17.method_149(f5, (float)j1 + 0.015625F, f6, d1, d2);
			c17.method_149(f4, (float)j1 + 0.015625F, f6, d0, d2);
			c17.method_149(f4, (float)j1 + 0.015625F, f7, d0, d3);
			c17.method_211(f, f, f);
			c17.method_149(f5, (float)j1 + 0.015625F, f7, d1, d3 + 0.0625);
			c17.method_149(f5, (float)j1 + 0.015625F, f6, d1, d2 + 0.0625);
			c17.method_149(f4, (float)j1 + 0.015625F, f6, d0, d2 + 0.0625);
			c17.method_149(f4, (float)j1 + 0.015625F, f7, d0, d3 + 0.0625);
		} else if (b0 == 1) {
			c17.method_149(f5, (float)j1 + 0.015625F, f7, d1, d3);
			c17.method_149(f5, (float)j1 + 0.015625F, f6, d1, d2);
			c17.method_149(f4, (float)j1 + 0.015625F, f6, d0, d2);
			c17.method_149(f4, (float)j1 + 0.015625F, f7, d0, d3);
			c17.method_211(f, f, f);
			c17.method_149(f5, (float)j1 + 0.015625F, f7, d1, d3 + 0.0625);
			c17.method_149(f5, (float)j1 + 0.015625F, f6, d1, d2 + 0.0625);
			c17.method_149(f4, (float)j1 + 0.015625F, f6, d0, d2 + 0.0625);
			c17.method_149(f4, (float)j1 + 0.015625F, f7, d0, d3 + 0.0625);
		} else if (b0 == 2) {
			c17.method_149(f5, (float)j1 + 0.015625F, f7, d1, d3);
			c17.method_149(f5, (float)j1 + 0.015625F, f6, d0, d3);
			c17.method_149(f4, (float)j1 + 0.015625F, f6, d0, d2);
			c17.method_149(f4, (float)j1 + 0.015625F, f7, d1, d2);
			c17.method_211(f, f, f);
			c17.method_149(f5, (float)j1 + 0.015625F, f7, d1, d3 + 0.0625);
			c17.method_149(f5, (float)j1 + 0.015625F, f6, d0, d3 + 0.0625);
			c17.method_149(f4, (float)j1 + 0.015625F, f6, d0, d2 + 0.0625);
			c17.method_149(f4, (float)j1 + 0.015625F, f7, d1, d2 + 0.0625);
		}

		if (!this.field_1098.method_471(i1, j1 + 1, k1)) {
			double d4 = (float)(k + 16) / 256.0F;
			double d5 = ((float)(k + 16) + 15.99F) / 256.0F;
			double d6 = (float)l / 256.0F;
			double d7 = ((float)l + 15.99F) / 256.0F;
			if (this.field_1098.method_471(i1 - 1, j1, k1) && this.field_1098.method_1011(i1 - 1, j1 + 1, k1) == class_18.field_1382.field_24) {
				c17.method_211(f * f1, f * f2, f * f3);
				c17.method_149((float)i1 + 0.015625F, (float)(j1 + 1) + 0.021875F, k1 + 1, d5, d6);
				c17.method_149((float)i1 + 0.015625F, j1, k1 + 1, d4, d6);
				c17.method_149((float)i1 + 0.015625F, j1, k1, d4, d7);
				c17.method_149((float)i1 + 0.015625F, (float)(j1 + 1) + 0.021875F, k1, d5, d7);
				c17.method_211(f, f, f);
				c17.method_149((float)i1 + 0.015625F, (float)(j1 + 1) + 0.021875F, k1 + 1, d5, d6 + 0.0625);
				c17.method_149((float)i1 + 0.015625F, j1, k1 + 1, d4, d6 + 0.0625);
				c17.method_149((float)i1 + 0.015625F, j1, k1, d4, d7 + 0.0625);
				c17.method_149((float)i1 + 0.015625F, (float)(j1 + 1) + 0.021875F, k1, d5, d7 + 0.0625);
			}

			if (this.field_1098.method_471(i1 + 1, j1, k1) && this.field_1098.method_1011(i1 + 1, j1 + 1, k1) == class_18.field_1382.field_24) {
				c17.method_211(f * f1, f * f2, f * f3);
				c17.method_149((float)(i1 + 1) - 0.015625F, j1, k1 + 1, d4, d7);
				c17.method_149((float)(i1 + 1) - 0.015625F, (float)(j1 + 1) + 0.021875F, k1 + 1, d5, d7);
				c17.method_149((float)(i1 + 1) - 0.015625F, (float)(j1 + 1) + 0.021875F, k1, d5, d6);
				c17.method_149((float)(i1 + 1) - 0.015625F, j1, k1, d4, d6);
				c17.method_211(f, f, f);
				c17.method_149((float)(i1 + 1) - 0.015625F, j1, k1 + 1, d4, d7 + 0.0625);
				c17.method_149((float)(i1 + 1) - 0.015625F, (float)(j1 + 1) + 0.021875F, k1 + 1, d5, d7 + 0.0625);
				c17.method_149((float)(i1 + 1) - 0.015625F, (float)(j1 + 1) + 0.021875F, k1, d5, d6 + 0.0625);
				c17.method_149((float)(i1 + 1) - 0.015625F, j1, k1, d4, d6 + 0.0625);
			}

			if (this.field_1098.method_471(i1, j1, k1 - 1) && this.field_1098.method_1011(i1, j1 + 1, k1 - 1) == class_18.field_1382.field_24) {
				c17.method_211(f * f1, f * f2, f * f3);
				c17.method_149(i1 + 1, j1, (float)k1 + 0.015625F, d4, d7);
				c17.method_149(i1 + 1, (float)(j1 + 1) + 0.021875F, (float)k1 + 0.015625F, d5, d7);
				c17.method_149(i1, (float)(j1 + 1) + 0.021875F, (float)k1 + 0.015625F, d5, d6);
				c17.method_149(i1, j1, (float)k1 + 0.015625F, d4, d6);
				c17.method_211(f, f, f);
				c17.method_149(i1 + 1, j1, (float)k1 + 0.015625F, d4, d7 + 0.0625);
				c17.method_149(i1 + 1, (float)(j1 + 1) + 0.021875F, (float)k1 + 0.015625F, d5, d7 + 0.0625);
				c17.method_149(i1, (float)(j1 + 1) + 0.021875F, (float)k1 + 0.015625F, d5, d6 + 0.0625);
				c17.method_149(i1, j1, (float)k1 + 0.015625F, d4, d6 + 0.0625);
			}

			if (this.field_1098.method_471(i1, j1, k1 + 1) && this.field_1098.method_1011(i1, j1 + 1, k1 + 1) == class_18.field_1382.field_24) {
				c17.method_211(f * f1, f * f2, f * f3);
				c17.method_149(i1 + 1, (float)(j1 + 1) + 0.021875F, (float)(k1 + 1) - 0.015625F, d5, d6);
				c17.method_149(i1 + 1, j1, (float)(k1 + 1) - 0.015625F, d4, d6);
				c17.method_149(i1, j1, (float)(k1 + 1) - 0.015625F, d4, d7);
				c17.method_149(i1, (float)(j1 + 1) + 0.021875F, (float)(k1 + 1) - 0.015625F, d5, d7);
				c17.method_211(f, f, f);
				c17.method_149(i1 + 1, (float)(j1 + 1) + 0.021875F, (float)(k1 + 1) - 0.015625F, d5, d6 + 0.0625);
				c17.method_149(i1 + 1, j1, (float)(k1 + 1) - 0.015625F, d4, d6 + 0.0625);
				c17.method_149(i1, j1, (float)(k1 + 1) - 0.015625F, d4, d7 + 0.0625);
				c17.method_149(i1, (float)(j1 + 1) + 0.021875F, (float)(k1 + 1) - 0.015625F, d5, d7 + 0.0625);
			}
		}

		return true;
	}

	@Redirect(
		method = "method_2255",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/class_18;method_734(Lnet/minecraft/class_447;IIII)I"),
		slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/class_215;field_3234:F"), to = @At("RETURN"))
	)
	private int ensureCfgGrassFix(class_18 c18, class_447 c447, int i, int j, int k, int l) {
		return cfgGrassFix ? -1 : c18.method_734(c447, i, j, k, l);
	}

	@Redirect(
		method = "method_1413",
		at = @At(value = "FIELD", target = "Lnet/minecraft/class_215;field_3594:Z")
	)
	private boolean actuallyCfgGrassFix() {
		return cfgGrassFix;
	}

	@Inject(
			method = "method_539",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/class_18;method_490()I", ordinal = 0),
			cancellable = true
	)
	private void modLoaderHaxOrSomethingLikeThat(class_18 i, int f, float par3, CallbackInfo ci) {
		int ret = i.method_490();
		int[] shouldGoNormally = new int[] {
				0, 1, 2, 6, 10, 11, 13, 16
		};
		for (int i1 : shouldGoNormally) {
			if (ret == i1) {
				return;
			}
		}
		ci.cancel();
		ModLoader.RenderInvBlock((class_215) (Object) this, i, f, ret);
	}

	/**
	 * @author sschr15, Notch, & Risugami's ModLoader dev team
	 * @reason simplify all the things (or something)
	 */
	@Overwrite
	public static boolean method_1716(int i) {
		return i == 0 || i == 10 || i == 11 || i == 13 || ModLoader.RenderBlockIsItemFull3D(i);
	}

	@Override
	public void setGrassFix(boolean grassFix) {
		cfgGrassFix = grassFix;
	}

	@Override
	public boolean getGrassFix() {
		return cfgGrassFix;
	}
}
