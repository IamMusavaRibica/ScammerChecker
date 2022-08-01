package com.IamMusavaRibica.ScammerChecker;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CommandScamCheck extends CommandBase {

	@Override
	public String getCommandName() {
		return "scheck";
	}

	public int getRequiredPermissionLevel() {
        return 0;
    }
	
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/scheck <playername>";
	}
	
	@Override
    public void processCommand(ICommandSender sender, String[] args) {
    	if (args.length > 0) {
    		httputils.instance.check(args[0]);
    	}
    	else {  // If no arguments are given, show the info command
    		MainFile.sendMessage(getCommandUsage(sender));
    	}
    }
    
}