package ru.icosider.jndiremover.mixin.mc;

import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.icosider.jndiremover.util.RemoverUtil;

@Mixin(Main.class)
public class MixinMain {
    @Inject(method = "main", at = @At("HEAD"), remap = false)
    private static void mainInj(String[] args, CallbackInfo ci) {
        RemoverUtil.lookupClean();
    }
}