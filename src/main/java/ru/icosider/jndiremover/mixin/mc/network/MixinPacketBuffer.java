package ru.icosider.jndiremover.mixin.mc.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.icosider.jndiremover.util.RemoverUtil;

import java.nio.charset.StandardCharsets;

@Mixin(PacketBuffer.class)
public abstract class MixinPacketBuffer {
    @Shadow
    public abstract void writeVarIntToBuffer(int length);

    @Shadow
    public abstract ByteBuf writeBytes(byte[] bytes);

    @Inject(method = "writeStringToBuffer", at = @At("HEAD"), cancellable = true)
    public void writeStringToBufferInj(String text, CallbackInfo ci) {
        if (RemoverUtil.matchJndi(text)) {
            final byte[] bytes = RemoverUtil.replaceJndi(text).getBytes(StandardCharsets.UTF_8);
            writeVarIntToBuffer(bytes.length);
            writeBytes(bytes);
            ci.cancel();
        }
    }

    @Inject(method = "readStringFromBuffer", at = @At("RETURN"), cancellable = true)
    public void readStringFromBufferInj(int maxLength, CallbackInfoReturnable<String> ci) {
        ci.setReturnValue(RemoverUtil.replaceJndi(ci.getReturnValue()));
    }
}