package net.minecraft.src;

import net.minecraft.client.Minecraft;

/**
 * Creates a command to show all of the camera men to the projector
 * @author Cody Cahoon
 * @version 2013.11.08
 *
 */
public class CommandShowPort extends CommandBase {

	/**
	 * Sets the command word for this command to 'showport'
	 */
	public String getCommandName() {
		return "showport";
	}

	/**
	 * Returns true if the given command sender is allowed to use this command.
	 */
	public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
		if (!Vars.playerToRoles.containsKey(par1ICommandSender.getCommandSenderName()))
			return false;
		return true;
	}

	/**
	 * Processes the command
	 * @param var1 The player sending the command
	 * @param var2 The words which go along with the command (not needed)
	 */
	public void processCommand(ICommandSender var1, String[] var2) {	
		if (Vars.portNumber == - 1)
		{
			var1.sendChatToPlayer("No port opened.");
		}
		else
		{
			var1.sendChatToPlayer("Port " + Vars.portNumber + " is open.");
		}
	}	
}