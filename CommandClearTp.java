package net.minecraft.src;

import java.io.*;

import net.minecraft.client.Minecraft;

/**
 * Creates a command to clear the teleport locations
 * @author Cody Cahoon
 * @version 2014.05.21
 *
 */
public class CommandClearTp extends CommandBase {
	
	private File inputFile;
	private File tempFile;
	
	/**
	 * Sets the command word for this command to 'tpclear'
	 */
	public String getCommandName() {
		return "tpclear";
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
		
		inputFile = new File(Vars.teleportPath);
		tempFile = new File(inputFile.getAbsolutePath() + ".tmp");

		
		//clears all of the teleport locations
		if (var2.length == 1 && var2[0].equals("all"))
		{
			this.clearAll(var1);	
		}
		
		//clears all the teleport locations at location x
		else if (var2.length == 1)
		{
			try
			{
				this.clearAllXLocations(var1, var2[0]);
			}
			catch (Exception e)
			{
				this.clearAllNameLocations(var1, var2[0]);		
			}
		}		
		
		else if (var2.length == 2)
		{
			try
			{
				int locationNumber = Integer.parseInt(var2[0]);
				boolean faster = false;
				try 
				{									
					BufferedReader reader = new BufferedReader(new FileReader(inputFile));
					PrintWriter writer = new PrintWriter(new FileWriter(tempFile));

					String line;
					while ( (line = reader.readLine()) != null)
					{
						String[] breakUp = line.split("=");
						if (!faster && Integer.parseInt(breakUp[0]) == locationNumber && breakUp[1].equals(var2[1]))
						{
							faster = true;
						}
						else
						{
							writer.println(line);
						}							
					}
					writer.close();
					reader.close();
					inputFile.delete();
					tempFile.renameTo(inputFile);
				}
				catch (IOException e){
					System.out.println(e.getMessage());
				}
				String readable = String.format("§eLocation %d for %s has been cleared.", locationNumber, var2[1]);
				if (!faster)					
					readable = String.format("§eLocation %d for %s already did not exist.", locationNumber, var2[1]);				
				var1.sendChatToPlayer(readable);
			}
			catch (Exception e)
			{
				var1.sendChatToPlayer("§cFormat should be /tpclear <number> <playername>");
			}
		}		
		else
		{
			var1.sendChatToPlayer("§cFormat should be /tpclear <number> <playername>");
		}
	}	
	
	/**
	 * Clears all of the teleport locations
	 * @param commandSender The player issuing the command
	 */
	private void clearAll(ICommandSender commandSender)
	{
		File tempFile = new File(inputFile.getAbsolutePath() + ".tmp");
		try 
		{
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));	
			PrintWriter writer = new PrintWriter(new FileWriter(tempFile));
			String line;
			while ( (line = reader.readLine()) != null)
			{					
				String[] breakUp = line.split("=");
				writer.println(breakUp[0] + "=");	
			}		
			//closes reader
			reader.close();
			writer.close();
		}
		catch(IOException e)
		{
			commandSender.sendChatToPlayer(Vars.errorReadWrite);
		}		
	}
	
	/**
	 * Clears all X locations (a number)
	 * @param commandSender The player issuing the command
	 * @param firstEntry The string entered
	 */
	private void clearAllXLocations(ICommandSender commandSender, String firstEntry)
	{
		int locationNumber = Integer.parseInt(firstEntry);
		try 
		{						
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			PrintWriter writer = new PrintWriter(new FileWriter(tempFile));

			String line;
			while ( (line = reader.readLine()) != null)
			{
				String[] breakUp = line.split("=");
				if (Integer.parseInt(breakUp[0]) != locationNumber)						
					writer.println(line);
			}
			
			//closes the writer
			writer.close();
			
			//closes the reader
			reader.close();			
			
			inputFile.delete();
			tempFile.renameTo(inputFile);
		}
		catch (IOException e){
			commandSender.sendChatToPlayer(Vars.errorReadWrite);
			return;
		}
		String readable = String.format("§eAll location §c%d§e's have been cleared", locationNumber);				
		commandSender.sendChatToPlayer(readable);	
	}
	
	/**
	 * Clears all of Name's locations
	 * @param commandSender The player issuing the command
	 * @param firstEntry The first word entered
	 */
	private void clearAllNameLocations(ICommandSender commandSender, String firstEntry)
	{
		String name = firstEntry;
		try 
		{						
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			PrintWriter writer = new PrintWriter(new FileWriter(tempFile));

			String line;
			while ( (line = reader.readLine()) != null)
			{
				String[] breakUp = line.split("=");
				if (!breakUp[1].equals(name))						
					writer.println(line);						
			}
			
			//closes the writer
			writer.close();
			
			//closes the reader
			reader.close();			
			
			inputFile.delete();
			tempFile.renameTo(inputFile);
		}
		catch (IOException ex){
			commandSender.sendChatToPlayer(Vars.errorReadWrite);
			return;
		}
		String readable = String.format("§eAll location's for §c%s§e have been cleared", name);				
		commandSender.sendChatToPlayer(readable);	
	}
}