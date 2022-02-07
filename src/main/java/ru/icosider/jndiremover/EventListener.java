package ru.icosider.jndiremover;

import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = JndiRemover.MOD_ID)
public class EventListener {
    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public static void onChat(ServerChatEvent e) {
        if (e.getMessage().isEmpty())
            e.setCanceled(true);
    }
}