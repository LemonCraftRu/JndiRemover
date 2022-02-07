package ru.icosider.jndiremover;

import net.minecraftforge.fml.common.Mod;

import static ru.icosider.jndiremover.JndiRemover.MOD_ID;

@Mod(modid = MOD_ID, name = "JndiRemover", version = "2.0.2", acceptableRemoteVersions = "*")
public class JndiRemover {
    public static final String MOD_ID = "jndirem";

    @Mod.Instance(MOD_ID)
    public static JndiRemover instance;
}