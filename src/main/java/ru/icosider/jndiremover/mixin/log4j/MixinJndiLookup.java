package ru.icosider.jndiremover.mixin.log4j;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.lookup.JndiLookup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = JndiLookup.class, remap = false)
public class MixinJndiLookup {
    @Inject(
            method = "lookup(Lorg/apache/logging/log4j/core/LogEvent;Ljava/lang/String;)Ljava/lang/String;",
            at = @At("HEAD"),
            remap = false
    )
    public void lookupInj(final LogEvent event, final String key, CallbackInfoReturnable<String> ci) {
        ci.setReturnValue(null);
    }
}