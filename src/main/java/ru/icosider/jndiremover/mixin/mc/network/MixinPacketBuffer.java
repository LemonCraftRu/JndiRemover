package ru.icosider.jndiremover.mixin.mc.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.icosider.jndiremover.JndiRemover;

import java.nio.charset.StandardCharsets;

@Mixin(PacketBuffer.class)
public abstract class MixinPacketBuffer {
    @Shadow
    public abstract PacketBuffer writeVarInt(int length);

    @Shadow
    public abstract ByteBuf writeBytes(byte[] bytes);

    @Inject(method = "writeString", at = @At("HEAD"), cancellable = true)
    public void writeStringInj(String text, CallbackInfoReturnable<PacketBuffer> ci) {
        if (JndiRemover.matchJndi(text)) {
            final byte[] bytes = JndiRemover.HEART.getBytes(StandardCharsets.UTF_8);
            writeVarInt(bytes.length);
            writeBytes(bytes);
            ci.setReturnValue(ci.getReturnValue());
        }
    }

    @Inject(method = "readString", at = @At("RETURN"), cancellable = true)
    public void readStringInj(int maxLength, CallbackInfoReturnable<String> ci) {
        ci.setReturnValue(JndiRemover.replaceJndi(ci.getReturnValue()));
    }
}