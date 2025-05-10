package me.imgalvin.noelytra.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

@Mixin(PlayerInventory.class)
public class NoElytraMixin {
	@Inject(method = "setStack", at = @At("TAIL"))
	private void onSetStack(int slot, ItemStack stack, CallbackInfo ci) {
		if (stack.getItem() == Items.ELYTRA) {
			// Replace Elytra with empty stack
			((PlayerInventory) (Object) this).setStack(slot, ItemStack.EMPTY);
		}
	}
}