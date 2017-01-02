package net.minecraft.src;

import java.io.*;
import net.minecraft.client.Minecraft;

/**
 * Creates a command to set the teleport location to where ever you are standing 
 * with a specified value
 * @author Cody Cahoon
 * @version 2013.05.19
 *
 */
public class CommandSetTp extends CommandBase {
	
	/**
	 * Sets the command word for this command to 'tpset'
	 */
	public String getCommandName() {
		return "tpset";
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
		//argument should be a number, followed by the playername
		if (var2.length == 2)
		{
			//gets the player issuing the command
			EntityPlayer player = Minecraft.getMinecraft().theWorld.getPlayerEntityByName(var2[1]);
			int locationNumber;
			if (player != null)
			{
				try
				{
					locationNumber = Integer.parseInt(var2[0]);
				}
				catch (Exception e)
				{
					var1.sendChatToPlayer(Vars.errorNumber);
					var1.sendChatToPlayer("§cFormat should be /tpset <number> <playername>");
					return;
				}
				
				double xCoord = player.posX;
				double yCoord = player.posY - (var2[1].equals(Vars.projectorName) ? 1.62 : 0.0);
				double zCoord = player.posZ;
				
				float yaw = player.rotationYaw;
				float pitch = player.rotationPitch;
				
				try 
				{
					String newEntry = locationNumber + "=" + var2[1] + "=" + xCoord + "=" + yCoord + "=" + zCoord + "=" + yaw + "=" + pitch;
					
					File inputFile = new File(Vars.teleportPath);
					File tempFile = new File(inputFile.getAbsolutePath() + ".tmp");

					BufferedReader reader = new BufferedReader(new FileReader(inputFile));
					PrintWriter writer = new PrintWriter(new FileWriter(tempFile));

					String line;
					boolean faster = false;
					while ( (line = reader.readLine()) != null)
					{
						String[] breakUp = line.split("=");
						if (!faster && Integer.parseInt(breakUp[0]) == locationNumber && breakUp[1].equals(var2[1]))
						{
							writer.println(newEntry);	
							faster = true;
						}
						else
							writer.println(line);
					}
					if (!faster)
						writer.println(newEntry);
					
					//close write
					writer.close();
					//close read
					reader.close();
					
					//remove old file
					inputFile.delete();
					
					//rename new file
					tempFile.renameTo(inputFile);
				}
				catch (IOException e){
					var1.sendChatToPlayer(Vars.errorReadWrite);
					return;
				}
				String readable = String.format("§e%d - %s -> (%.2f ,%.2f ,%.2f ,%.2f ,%.2f)", locationNumber, var2[1], xCoord, yCoord, zCoord, yaw, pitch);
				var1.sendChatToPlayer(readable);
			}
			else
			{
				var1.sendChatToPlayer(String.format("§cUnable to locate player %s", var2[1]));
			}
		}
		else
		{
			var1.sendChatToPlayer("§cFormat should be /tpset <number> <playername>");
		}
	}	
}