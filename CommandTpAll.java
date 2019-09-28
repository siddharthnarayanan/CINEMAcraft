package net.minecraft.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
/**
 * Changes the location of every player based on entered location value
 * So far none of the locations have been altered due to the map not being finished
 * There are two types of people in the game, the camera man/camera men and the players
 * We want to make sure that the camera man/camera men go to different locations and 
 * the players go to their acting locations
 * @author Cody Cahoon
 * @version 2013.05.13
 *
 */
public class CommandTpAll extends CommandBase {

	/**
	 * Which location to teleport to
	 * This is not really needed but instead of using a parameter in the
	 * get x,y,z methods, I just created a field
	 */
	private int locationNumber;
	
	private int x, y, z;
	private float yaw;
	
	/**
	 * The word to teleport all players (tpall)
	 * 
	 * @return the command word
	 */
	public String getCommandName() {
		return "tpall";
	}
	
	/**
	 * Returns true if the given command sender is allowed to use this command.
	 * @param par1CommandSender The command sender
	 * @return true All players can use it
	 */
	public boolean canCommandSenderUserCommand(ICommandSender par1CommandSender)
	{
		if (!Vars.playerToRoles.containsKey(par1CommandSender.getCommandSenderName()))
			return false;
		return true;
	}
	
	/**
	 * Processes the Command
	 * 
	 * @param commandSender
	 *            The person issuing the command, does not matter for this
	 *            command
	 * @param wordArguments
	 *            The position 0 of the array should be the location value
	 */
	public void processCommand(ICommandSender var1, String[] var2) {
		String[] playersInGame = MinecraftServer.getServer().getAllUsernames();
		if (var2[0].equals("here"))
		{
			EntityPlayer command = Minecraft.getMinecraft().theWorld.getPlayerEntityByName(var1.getCommandSenderName());
			for (int i = 0; i < playersInGame.length; i++) 
			{
				EntityPlayerMP playerMP = func_82359_c(var1, playersInGame[i]);
				if (playerMP != null && command != null)
				{
					playerMP.playerNetServerHandler.setPlayerLocation(command.posX, command.posY, command.posZ, command.rotationYaw, command.rotationPitch);
				}
			}
		}
		else
		{
			locationNumber = Integer.parseInt(var2[0]);
			
			//Projector part (DISABLED AFTER SPIKE ADDITION BELOW -- NEEDS TESTING)
			
			/*int previousCameraValue = -1;		
			if (Vars.cameraViewPortValue != 0 && Vars.isServer)
			{
				previousCameraValue = Vars.cameraViewPortValue;
				Vars.cameraViewPortValue = 0;
			}*/
			
			
			
			for (int i = 0; i < playersInGame.length; i++) {
				//EntityPlayerMP playerMP = func_82359_c(var1, playersInGame[i]); DOES THIS NEED TO BE MORE ROBUST?
				EntityPlayerMP playerMP = func_82359_c(var1, playersInGame[i]);
				if (playerMP != null)
				{
					//tpall is invoked on the server/projector, so we don't have to check if we are the projector
					if (Vars.playerToRoles.containsKey(playerMP.getEntityName()) && Vars.playerToRoles.get(playerMP.getEntityName()).equals("camera") && Vars.cameraViewPortValue == Vars.cameraReverseValues.get(playerMP.getEntityName()))
					{
						//in case this is the camera projector is following, we spike this moment to prevent projector teleporting
						// until this teleportation has been taken care of
						Vars.viewTimeDelta = Minecraft.getSystemTime();
					}
					
					File inputFile = new File(Vars.teleportPath);
					BufferedReader reader;
				
					try 
					{
						reader = new BufferedReader(new FileReader(inputFile));					
						String line = reader.readLine();
						while (line != null)
						{
							String[] breakUp = line.split("=");
							if (breakUp[1].equals(playerMP.getEntityName()) && Integer.parseInt(breakUp[0]) == locationNumber)						
							{				
								double x = Double.parseDouble(breakUp[2]);							
								double y = Double.parseDouble(breakUp[3]);
								double z = Double.parseDouble(breakUp[4]);
								float yaw = Float.parseFloat(breakUp[5]);
								float pitch = Float.parseFloat(breakUp[6]);
	
								playerMP.playerNetServerHandler.setPlayerLocation(x, y, z, yaw, pitch);
								// OPERAcraft TODO: CONSIDER ADDING THIS AS IT MAY STREAMLINE TELEPORTING
								/*if (Vars.playerToRoles.containsKey(playerMP.getEntityName()) && Vars.playerToRoles.get(playerMP.getEntityName()).equals("camera") && Vars.cameraViewPortValue == Vars.cameraReverseValues.get(playerMP.getEntityName()))
								{
									System.out.println("HERE");
									EntityPlayerMP projector = func_82359_c(var1, Vars.projectorName);
									if (projector != null)
									{
										projector.playerNetServerHandler.setPlayerLocation(x, y, z, yaw, pitch);
									}
								}*/
								break;
							}
							line = reader.readLine();
						}
						
						//close reader
						reader.close();
					}
					catch(IOException e)
					{
						var1.sendChatToPlayer(Vars.errorRead);
					}
					
				}			
				/*
				 * x
				 * y
				 * z
				 * angle left and right (spin) (yaw) 
				 * 0 - south 
				 * 90 - west 
				 * 180 - north
				 * 270 - east 
				 * angle up and down (nod) (pitch)
				 * 90 - straight down
				 * -90 - straight up
				 */
			}
			
			//Projector again (DISABLED AFTER SPIKE ADDITION ABOVE -- NEEDS TESTING)
			/*if (previousCameraValue != -1)
			{
				for (int i = 0; i < playersInGame.length; i++) {
					EntityPlayerMP playerMP = func_82359_c(var1, playersInGame[i]);
					if (playerMP != null && playerMP.getEntityName().equals(Vars.projectorName))
					{
						EntityPlayer playerToTrack = Minecraft.getMinecraft().theWorld.getPlayerEntityByName(Vars.getCameraByValue(previousCameraValue));
						playerMP.playerNetServerHandler.setPlayerLocation(playerToTrack.posX, playerToTrack.posY, playerToTrack.posZ, playerToTrack.rotationYaw, playerToTrack.rotationPitch);
					}
				}
				Vars.cameraViewPortValue = previousCameraValue;
			}*/		
		}		
	}
}