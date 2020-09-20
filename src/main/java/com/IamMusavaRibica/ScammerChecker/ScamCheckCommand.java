package com.IamMusavaRibica.ScammerChecker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

public class ScamCheckCommand extends CommandBase {

	@Override
	public String getCommandName() {
		return "scammerchecker";
	}
	
	public List<String> getCommandAliases() {
        return Collections.singletonList("scheck");
    }
	
	public int getRequiredPermissionLevel() {
        return 0;
    }
	
	@Override
	public String getCommandUsage(ICommandSender sender) {
		// There has to be a better way of doing this.
		return EnumChatFormatting.DARK_GREEN + "" + EnumChatFormatting.STRIKETHROUGH + "--------------" + EnumChatFormatting.DARK_GREEN + "<" + EnumChatFormatting.GOLD + "" + EnumChatFormatting.BOLD + " ScammerChecker " +
				EnumChatFormatting.DARK_GREEN + "" + EnumChatFormatting.DARK_GREEN + ">" + EnumChatFormatting.STRIKETHROUGH + "--------------" + "\n" +
				EnumChatFormatting.DARK_GRAY + " - " + EnumChatFormatting.GOLD + "/scheck <username>" + EnumChatFormatting.DARK_GRAY + " - " + EnumChatFormatting.YELLOW + "Check if a player is a Skyblock scammer" + "\n" +
				EnumChatFormatting.DARK_RED + "" + EnumChatFormatting.BOLD + "| " + EnumChatFormatting.RED + "Thank you for using this mod :)" + EnumChatFormatting.DARK_RED + "" + EnumChatFormatting.BOLD + " | " +
				EnumChatFormatting.RED + "Made by musava_ribica" + EnumChatFormatting.DARK_RED + "" + EnumChatFormatting.BOLD + " |" + "\n" +
		        EnumChatFormatting.DARK_GREEN + "" + EnumChatFormatting.STRIKETHROUGH + "--------------------------------------------";
	}
	
	@Override
    public void processCommand(ICommandSender sender, String[] args) {
    	if (args.length > 0) {
    		try {
    			// This is the whole function of requests and checks
    			
    			MainFile.sendMessage(EnumChatFormatting.AQUA + "Checking player " + args[0] + "...");
    			URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + args[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                
                StringBuilder response = new StringBuilder();
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                }
                
                Integer code = connection.getResponseCode();
                Integer successCode = 200;
                Integer noContentCode = 204;
                Integer forbiddenCode = 403;
                
                System.out.println("Connection response code: " + code);
                connection.disconnect();
                
                if (code.equals(noContentCode)) {
                	MainFile.sendErrorMessage("No content returned. Have you entered a correct username?");
                }
                else if (code.equals(forbiddenCode)) {  // Not tested
                	MainFile.sendErrorMessage("403 Forbidden. Please wait before checking again.");
                }
                else if (code.equals(successCode)) {
                	JsonObject response_json = new Gson().fromJson(response.toString(), JsonObject.class);
                    String uuid = response_json.get("id").getAsString();
                    System.out.println("Received the UUID. Its: " + uuid);
                    
                    // Now that we have the UUID , check for scams
        			URL scams_url = new URL("https://raw.githubusercontent.com/skyblockz/pricecheckbot/master/scammer.json");
                    HttpURLConnection connection2 = (HttpURLConnection) scams_url.openConnection();
                    connection2.setRequestMethod("GET");
                    
                    StringBuilder response2 = new StringBuilder();
                    BufferedReader rd2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
                    String line2;
                    
                    while ((line2 = rd2.readLine()) != null) {
                        response2.append(line2);
                    }
                    
                    JsonObject scammers = new Gson().fromJson(response2.toString(), JsonObject.class);
                    JsonElement scammer_info = scammers.get(uuid);
                    if (scammer_info != null) {
                    	System.out.println("This is the scammer info: " + scammer_info.getAsJsonObject());
                    	MainFile.sendMessage(EnumChatFormatting.RED + "Player " + response_json.get("name").getAsString() + " is a known scammer. You might want to /ignore add him.");
                    	
                    	String reason = scammer_info.getAsJsonObject().get("reason").getAsString();
                    	MainFile.sendMessage(EnumChatFormatting.RED + "Reason: " + reason);
                    }          	
                    else {
                    	MainFile.sendMessage(EnumChatFormatting.GREEN + "Player " + response_json.get("name").getAsString() + " is not a known scammer.");
                    	MainFile.sendMessage(EnumChatFormatting.YELLOW + "However, you should still be careful when trading with this player.");
                    }
                }
    			
    			
    		}
    		catch(Exception e) {  // If an error occurs with the checking
    			System.out.println("Catched exception while checking for scammer: " + e);
    			MainFile.sendErrorMessage("An error occured while trying to check for scams done by this player.");
    		}
    	}
    	else {  // If no arguments are given, show the info command
    		MainFile.sendMessage(getCommandUsage(sender));
    	}
    }
    
}