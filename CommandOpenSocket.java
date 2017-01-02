package net.minecraft.src;

import java.io.*;
import java.net.*;
import net.minecraft.client.Minecraft;

/**
 * Creates the command to open the socket to receive messages from PD
 * This command opens a new thread to receive messages from PD and interpret them
 * @author Cody Cahoon
 * @version 2013.11.20
 *
 */
public class CommandOpenSocket extends CommandBase {

	/**
	 * The current minecraft instance
	 */
	Minecraft client = ModLoader.getMinecraftInstance();
	
	/**
	 * The integer for the port
	 */
	int port;
	
	/**
	 * Sets the command word for this command to 'openport'
	 */
	public String getCommandName() {
		return "openport";
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
	 * @param var2 The words which go along with the command (can specify which port)
	 */
	public void processCommand(ICommandSender var1, String[] var2) {		
		try {
			DatagramSocket dsocket;
			if (var2.length == 0)
			{
				port = 7000;
			}
			else
			{
				port = Integer.parseInt(var2[0]);
			}
			if (port < 0)
			{
				var1.sendChatToPlayer("Port number is invalid.");
			}
			else
			{
			dsocket = new DatagramSocket(port);
				var1.sendChatToPlayer("Port " + port + " opened.");
				Vars.portNumber = port;
				(new Thread(new OpenSocketThread(dsocket))).start();
			} }catch (Exception e) {
				System.err.println(e);
			}
	}	
}