package net.minecraft.src;

import net.minecraft.client.Minecraft;

/**
 * Colors the words sent by a specific player
 * @author Cody Cahoon
 * @version 2013.05.13
 *
 */
public class CommandColorText extends CommandBase {

	/**
	 * The message to display after the user name
	 */
	private String longMessage;
	/**
	 * Not accessible by calling the command
	 */
	public String getCommandName() {
		return null;
	}	

	/**
	 * Processes the command by changing the color and the user name 
	 * Need to change above user name
	 * @param var1 The Command Sender
	 * @param var2 The arguments that are input after the command word
	 */
	public void processCommand(ICommandSender var1, String[] var2) {
		this.getLongMessage(var2);		
		var1.sendChatToPlayer(this.getUsername(var2)
				+ this.longMessage);
		
		
	}

	/**
	 * Gets the long message from the input arguments
	 * Does not add the characters m,1,2,0 to the output text
	 * @param args The words to output
	 */
	private void getLongMessage(String[] args)
	{
		String msg = "";
		for (int i = 1; i < args.length; i++) 
		{
			msg += args[i] + " ";	
		}
		longMessage = msg;
	}

	/**
	 * Uses the first input to determine what the chat will look like
	 * @param args The first word is going to be used, but the entire word array is input
	 * @return the user name with the edited name and color
	 */
	private String getUsername(String[] args)
	{
		if (args[0].equals("null"))
			return "";
		else if (Vars.playerToColors.containsKey(args[0]))
		{
			if (Vars.colorCodes.containsKey(Vars.playerToColors.get(args[0])))		
				return Vars.colorCodes.get(Vars.playerToColors.get(args[0])) + args[0] + ": ";
			return args[0] + ": ";
		}
		else
			return args[0] + ": ";
		
	}
	/** THIS IS NOT TRUE ANYMORE BUT IS USEFUL FOR KNOWING COLORS
	 * Code		Color				Player Name			Username
	 * §0		Black				
	 * §1		Dark Blue
	 * §2		Dark Green
	 * §3		Dark Aqua
	 * §4		Dark Red
	 * §5		Purple				Mortimer			icat05
	 * §6		Gold
	 * §7		Gray
	 * §8		Dark Gray
	 * §9		Blue
	 * §a		Green				Cody				CodyCahoon
	 * §b		Aqua				Marcus				icat01
	 * §c		Red					Regina				icat02
	 * §d		Light Purple							
	 * §e		Yellow				X					icat03
	 * §f		White				Lilith				icat04
	 * 
	 */
	
	/**
	 * Checks if the message is longer than 0 characters
	 * @return true if long
	 */
	private boolean isLongMessage()
	{
		return(this.longMessage.equals("")) ? false : true;		
	}
}