package ru.icosider.jndiremover.mixin.mc;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.icosider.jndiremover.util.RemoverUtil;

@Mixin(MinecraftServer.class)
public class MixinServer {
    @Inject(method = "main",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/server/dedicated/DedicatedServer;startServerThread()V"
            ),
            remap = false
    )
    private static void mainInj(String[] args, CallbackInfo ci) {
        RemoverUtil.lookupClean();
    }
}