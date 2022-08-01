package com.IamMusavaRibica.ScammerChecker;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid="scammerchecker", name = "Scammer Checker", version = "1.1")
public class MainFile {
    private static httputils httpUtils;

    public static httputils getHttpUtils() {
        return httpUtils;
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        httpUtils = new httputils();
    	ClientCommandHandler.instance.registerCommand(new CommandScamCheck());
    }
    
    public static void sendMessage(String text) {
        // Post the event to the event bus so other mods can pick it up (and maybe cancel it)
        ClientChatReceivedEvent event = new ClientChatReceivedEvent((byte) 1, new ChatComponentText(text));
        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(event.message);
        }
    }
    
    public static void sendErrorMessage(String errorText) {
        sendMessage(EnumChatFormatting.RED + "Error: " + errorText);
    }
}
