package com.IamMusavaRibica.ScammerChecker;

import com.IamMusavaRibica.ScammerChecker.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION)
public class MainFile {
	
	
	@Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
		
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
    	ClientCommandHandler.instance.registerCommand(new ScamCheckCommand());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
    	
    }
    
    public static void sendMessage(String text) {
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
