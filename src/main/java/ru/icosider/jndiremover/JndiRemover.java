package ru.icosider.jndiremover;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import static ru.icosider.jndiremover.JndiRemover.MOD_ID;

@Mod(modid = MOD_ID, name = "JndiRemover", version = "1.0.2.1", acceptableRemoteVersions = "*")
public class JndiRemover {
    public static final String MOD_ID = "jndirem";

    @Mod.Instance(MOD_ID)
    public static JndiRemover instance;

    @Mod.EventHandler
    public void pre(FMLPreInitializationEvent e) {
        EventListener.INSTANCE.register();
    }
}