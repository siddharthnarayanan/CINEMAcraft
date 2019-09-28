package net.minecraft.src;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

/**
 * Creates a new thread to count the key press time
 * @author Cody	Cahoon
 * @version 2014.05.26
 */
public class ChangeGameTypeThread implements Runnable {

	private String player;
	
	public void run() {	
		// DO WE NEED THIS?
		//System.out.println(Minecraft.getMinecraft().theWorld.getPlayerEntityByName(player));
		/*while (Minecraft.getMinecraft().theWorld.getPlayerEntityByName(player) == null)
		{}
		String[] playersInGame = MinecraftServer.getServer().getAllUsernames();
		EntityPlayerMP playerMP = CommandBase.func_82359_c(Minecraft.getMinecraft().thePlayer, player);
		if (playerMP != null && !Vars.playerToRoles.containsKey(playerMP.getEntityName()))
		{			
			playerMP.setGameType(EnumGameType.OPERA);			
		}*/
		
	} 
	
	/**
	 * The constructor for the new thread to deal with the input
	 * of commands from PD
	 * @param inputSocket The socket already created, passed in from the CommandOpenSocket class
	 */
	public ChangeGameTypeThread(String playerName)
	{
		this.player = playerName;
	}
	
}