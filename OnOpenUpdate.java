package net.minecraft.src;

import java.io.*;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

/**
 * Loads your role
 * @author Cody Cahoon
 * @version 2013.05.19
 *
 */
public class OnOpenUpdate {
	
	private String localName;
	
	public OnOpenUpdate(String name)
	{
		this.localName = name;
	}

	/**
	 * Processes the command
	 * @param var1 The player sending the command
	 * @param var2 The words which go along with the command (which location number)
	 */
	public void run(){	
		
		BufferedReader reader;
		
		this.clearMaps();

		
		//roles
		File inputFile = new File(Vars.rolesPath);
		try 
		{
			reader = new BufferedReader(new FileReader(inputFile));					
			String line;
			while ( (line = reader.readLine()) != null)
			{
				String[] breakUp = line.split("=");
				if (breakUp.length == 3)
				{
					String username = breakUp[0], role = breakUp[1], cameraNumber = breakUp[2];
					if (role.equals("camera"))
					{
						try
						{
							int cameraValue = Integer.parseInt(cameraNumber);
							Vars.cameraValues.put(cameraValue, username);
							Vars.cameraReverseValues.put(username, cameraValue);
							Vars.playerToRoles.put(username, "camera");
							this.localPlayer(this.localName, username, role); //localName was Vars.playersName
						}
						catch(Exception e)
						{
							//not a number
						}
					}
				}
				else if (breakUp.length == 2)
				{
					String username = breakUp[0], role = breakUp[1];					
					if (checkRole(role))
					{
						this.localPlayer(this.localName, username, role); //localName was Vars.playersName
						Vars.playerToRoles.put(username, role);	
						if (role.equals("projector"))
							Vars.projectorName = username;
					}
				}
			}				
			//closes reader
			reader.close();		
		}
		catch(IOException ex)
		{
			System.out.println(Vars.errorRead);
		}						
				
		//nicknames/colors
		inputFile = new File(Vars.nicknamesColorPath);
		try 
		{
			reader = new BufferedReader(new FileReader(inputFile));					
			String line;
			while ( (line = reader.readLine()) != null)
			{
				String[] breakUp = line.split("=");
				if (breakUp.length == 3)						
				{
					String username = breakUp[0], nickname = breakUp[1], color = breakUp[2];
					Vars.playerToNickName.put(username, nickname);
					Vars.playerToColors.put(username, color);
					System.out.println(username + " " + nickname + " " + color);
				}
				else if (breakUp.length == 2)
				{						
					String username = breakUp[0], nickname = breakUp[1];						
					Vars.playerToNickName.put(username, nickname);
				}
			}				
			//closes reader
			reader.close();		
		}
		catch(IOException ex)
		{
			System.out.println(Vars.errorRead);
		}	
		
		//player to user name and mouth amount
		inputFile = new File(Vars.playerPath);
		try 
		{
			reader = new BufferedReader(new FileReader(inputFile));					
			String line;
			while ( (line = reader.readLine()) != null)
			{
				String[] breakUp = line.split("=");
				if (breakUp.length == 3 && breakUp[0].length() > 6 && breakUp[0].substring(0, 6).equals("player"))						
				{
					String playerName = breakUp[0], username = breakUp[1];
					int headCount = Integer.parseInt(breakUp[2]);
					Vars.playerToUsername.put(playerName, username);
					Vars.playerToUsernameReverse.put(username, playerName);
					Vars.playerNumberOfMouths.put(playerName, headCount);
					mod_OperaCraft.createBlocks(playerName, headCount);
				}
			}				
			//closes reader
			reader.close();		
		}
		catch(IOException ex)
		{
			System.out.println(Vars.errorRead);
		}			
		
		/*
		String[] playersInGame = MinecraftServer.getServer().getAllUsernames();
		for (int i = 0; i < playersInGame.length; i++) 
		{
			EntityPlayerMP playerMP = func_82359_c(var1, playersInGame[i]);
			if (playerMP != null && !Vars.playerToRoles.containsKey(playersInGame[i]))
			{
				playerMP.setInvisible(true);
				playerMP.setGameType(EnumGameType.OPERA);	
				playerMP.setCanPickUpLoot(false);
			}
			else
			{
				playerMP.setInvisible(false);
			}
		}*/
	}
	
	
	/**
	 * Checks if the role is actor, camera, or projector
	 * @param role The player's role
	 * @return true if actor/camera/projector
	 */
	private boolean checkRole(String role)
	{
		return role.toLowerCase().equals("actor") || role.toLowerCase().equals("camera") || role.toLowerCase().equals("projector") || role.toLowerCase().equals("server") || role.toLowerCase().equals("spectator") ? true : false;
	}	
	
	private void localPlayer(String local, String username, String role)
	{	
		System.out.println("localPlayer:" + " username=" + username + " role=" + role + " local.equals()=" + local.equals("") + " local.equals(username)=" + local.equals(username) + " localName=<" + localName + ">");
		//Vars.debug += "localPlayer:" + " username=" + username + " role=" + role + " local.equals()=" + local.equals("") + " local.equals(username)=" + local.equals(username) + " localName=<" + localName + ">\n";
		if (!local.equals("") && local.equals(username))
		{	
			System.out.println("...got in");
			//Vars.debug += "...got in\n";
			Vars.isServer = false;
			Vars.isProjector = false;
			Vars.isCamera = false;
			Vars.isActor = false;
			Vars.isSpectator = false;
			if (role.equals("server"))
			{
				Vars.isServer = true;
				Vars.projectorView = true;
			}
			else if (role.equals("projector"))
				Vars.isProjector = true;
			else if (role.equals("camera"))
				Vars.isCamera = true;
			else if (role.equals("actor"))
				Vars.isActor = true;
			else
				Vars.isSpectator = true;
		}		
	}
	
	/**
	 * Empties all of the maps storing information
	 */
	private void clearMaps()
	{
		Vars.cameraValues.clear();
		Vars.cameraReverseValues.clear();
		Vars.playerToRoles.clear();
		Vars.playerToColors.clear();		
		Vars.playerToNickName.clear();
		Vars.playerToUsername.clear();
		Vars.playerToUsernameReverse.clear();
		Vars.playerNumberOfMouths.clear();
	}
}