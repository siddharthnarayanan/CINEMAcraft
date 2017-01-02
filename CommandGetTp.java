package net.minecraft.src;

import java.io.*;
import net.minecraft.client.Minecraft;

/**
 * Creates a command to get the teleport location
 * @author Cody Cahoon
 * @version 2013.05.19
 *
 */
public class CommandGetTp extends CommandBase {
	/**
	 * Sets the command word for this command to 'tpget'
	 */
	public String getCommandName() {
		return "tpget";
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
	 * @param var2 The words which go along with the command (which location number)
	 */
	public void processCommand(ICommandSender var1, String[] var2){	
		
		File inputFile = new File(Vars.teleportPath);
		BufferedReader reader;

		if (var2.length == 1)
		{
			try
			{
				int locationNumber = Integer.parseInt(var2[0]);
				try 
				{
					reader = new BufferedReader(new FileReader(inputFile));					
					String line;
					boolean switched = false, flag = false;
					String color = "§f";
					while ( (line = reader.readLine()) != null)
					{
						String[] breakUp = line.split("=");
						if (Integer.parseInt(breakUp[0]) == locationNumber)
						{			
							flag = true;
							String readable = String.format("%s%d - %s -> (%.2f ,%.2f ,%.2f ,%.2f ,%.2f)", color, Integer.parseInt(breakUp[0]),breakUp[1], Double.parseDouble(breakUp[2]), Double.parseDouble(breakUp[3]), Double.parseDouble(breakUp[4]), Float.parseFloat(breakUp[5]), Float.parseFloat(breakUp[6]));
							var1.sendChatToPlayer(readable);		
							switched = !switched;
						}
						color = switched ? "§e" : "§f";
					}
					if (!flag)
						var1.sendChatToPlayer(String.format("§cNo location §e%s's", locationNumber));
					
					//closes reader
					reader.close();		
					}
				catch(IOException ex)
				{
					var1.sendChatToPlayer(Vars.errorRead);
				}
			}
			catch(Exception e)
			{
				String name = var2[0];			
				try 
				{
					reader = new BufferedReader(new FileReader(inputFile));					
					String line;
					boolean switched = false, flag = false;
					String color = "§f";
					while ( (line = reader.readLine()) != null)
					{
						String[] breakUp = line.split("=");
						if (breakUp[1].equals(name))
						{			
							flag = true;
							String readable = String.format("%s%d - %s -> (%.2f ,%.2f ,%.2f ,%.2f ,%.2f)", color, Integer.parseInt(breakUp[0]),breakUp[1], Double.parseDouble(breakUp[2]), Double.parseDouble(breakUp[3]), Double.parseDouble(breakUp[4]), Float.parseFloat(breakUp[5]), Float.parseFloat(breakUp[6]));
							var1.sendChatToPlayer(readable);		
							switched = !switched;
						}
						color = switched ? "§e" : "§f";
					}
					if (!flag)
						var1.sendChatToPlayer(String.format("§cNo entries for §e%s", name));
					
					//closes reader
					reader.close();		
					}
				catch(IOException ex)
				{
					var1.sendChatToPlayer(Vars.errorRead);
				}
			}
			
		}
		else if (var2.length == 2)
		{
			try
			{
				int locationNumber = Integer.parseInt(var2[0]);	
				try 
				{
					reader = new BufferedReader(new FileReader(inputFile));		
					String line;
					boolean foundEntry = false;
					while (	(line = reader.readLine()) != null)
					{
						String[] breakUp = line.split("=");
						if (Integer.parseInt(breakUp[0]) == locationNumber && breakUp[1].equals(var2[1]))
						{						
							String readable = String.format("§e%d - %s -> (%.2f ,%.2f ,%.2f ,%.2f ,%.2f)", Integer.parseInt(breakUp[0]),breakUp[1], Double.parseDouble(breakUp[2]), Double.parseDouble(breakUp[3]), Double.parseDouble(breakUp[4]), Float.parseFloat(breakUp[5]), Float.parseFloat(breakUp[6]));
							var1.sendChatToPlayer(readable);
							foundEntry = true;
							break;
						}
					}
					//close reader
					reader.close();
					
					if (!foundEntry)
						var1.sendChatToPlayer(String.format("§cNo entry for §e%s§c at Location §e%d", var2[1], locationNumber));
				}
				catch (IOException ex)
				{
					var1.sendChatToPlayer(Vars.errorRead);
				}
			}				
			catch (Exception e)
			{
				var1.sendChatToPlayer(Vars.errorNumber);
			}
		}
	}	
}