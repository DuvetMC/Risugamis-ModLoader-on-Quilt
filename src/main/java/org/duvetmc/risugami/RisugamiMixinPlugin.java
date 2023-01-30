package org.duvetmc.risugami;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class RisugamiMixinPlugin implements IMixinConfigPlugin {
	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return false;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		// Some transformations cannot be applied in Mixin itself, so they're done manually here.
		// This is the case for the @Static annotation (see Static.java).
		for (FieldNode field : targetClass.fields) {
			if (field.visibleAnnotations != null) {
				for (int i = 0; i < field.visibleAnnotations.size(); i++) {
					if (field.visibleAnnotations.get(i).desc.equals("Lorg/duvetmc/risugami/Static;")) {
						field.visibleAnnotations.remove(i--);
						field.access |= Opcodes.ACC_STATIC;
					}
				}
			}
		}
	}
}
