package net.minecraft.src;

import java.io.*;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;

/**
 * Adds the commands, new heads to the world, and the new keys
 * 
 * @author Cody Cahoon
 * @author Ivica Ico Bukvic
 * @version 2015.09.28
 * 
 */
public class mod_OperaCraft extends BaseMod {
	
	public static int idStart = 159; //was 159
	
	/**
	 * Creates a binding for the gui to hide for the camerman
	 */
	private KeyBinding hideHotbar = new KeyBinding("Projector Mode", 35);
	/**
	 * The letter ' j' places the arms to normal stance
	 */
	private KeyBinding armDown = new KeyBinding("Arm Down", 36);
	/**
	 * The letter 'k' places the left arm up
	 */
	private KeyBinding leftArmOut = new KeyBinding("Left Arm Out", 37);
	/**
	 * The letter 'l' places the left arm into the body
	 */
	private KeyBinding rightArmOut = new KeyBinding("Right Arm Out", 38);
	/**
	 * The letter 'u'
	 */
	private KeyBinding armsUp = new KeyBinding("Arms Up", 22);
	/**
	 * The letter 'i'
	 */
	private KeyBinding leftArmAngle = new KeyBinding("Left Arm Angled", 23);
	/**
	 * The letter 'o'
	 */
	private KeyBinding rightArmAngle = new KeyBinding("Right Arm Angled", 24);

	/**
	 * The letter 'y'
	 */
	
	private KeyBinding hideActorGUI = new KeyBinding("Hide Helper", 21);


	
	/**
	 * Returns the version
	 * 
	 * @return The version
	 */
	public String getVersion() {
		return "2.2.5";
		//TODO: reset leg rotation when setting kinectposition to 1 and all rotations when setting kinectposition to 0
	}

	/**
	 * Adds the changes to Minecraft
	 */
	public void load() {
		
		Vars.folderPath = Minecraft.getAppDir("minecraft").toString() + "/OPERAcraft";
		Vars.rolesPath = Vars.folderPath + "/roles.txt";
		Vars.teleportPath = Vars.folderPath + "/tp_locations.txt";
		Vars.nicknamesColorPath = Vars.folderPath + "/nicknames_colors.txt";
		Vars.playerPath = Vars.folderPath + "/players.txt";
		Vars.mouthsPath = Vars.folderPath + "/mouths";
		Vars.failPath = Vars.folderPath + "/errors.txt";
		Vars.skinsPath = Vars.folderPath + "/skins";
		Vars.cloaksPath = Vars.folderPath + "/cloaks";
		new File(Vars.folderPath).mkdir();
		new File(Vars.mouthsPath).mkdir();
		new File(Vars.skinsPath).mkdir();
		new File(Vars.cloaksPath).mkdir();
		try
		{
			new File(Vars.rolesPath).createNewFile();
			new File(Vars.teleportPath).createNewFile();
			new File(Vars.nicknamesColorPath).createNewFile();
			new File(Vars.playerPath).createNewFile();
			//new File(Vars.failPath).createNewFile();
		}
		catch (Exception e)
		{
			
		}
		
		System.out.println(Vars.mouthsPath);
		new OnOpenUpdate("spectator").run(); // we use generic user name here to create blocks, then update later with the real user name when we get to the menu
		
		//the commands
		ModLoader.addCommand(new CommandOpenSocket());//done
		ModLoader.addCommand(new CommandColorText());//done
		ModLoader.addCommand(new CommandTpAll());//done
		ModLoader.addCommand(new CommandSwitchViewPort());//done
		ModLoader.addCommand(new CommandShowPort());//done
		ModLoader.addCommand(new CommandSetTp());//done
		ModLoader.addCommand(new CommandGetTp());//done
		ModLoader.addCommand(new CommandClearTp());//done
		ModLoader.addCommand(new CommandTpOne());//done
		
		
				
		ModLoader.registerKey(this, this.hideHotbar, false);
		ModLoader.addLocalization("Hide Gui", "Hide Gui");
		ModLoader.setInGameHook(this, true, true);		
		
		ModLoader.registerKey(this, this.leftArmOut, false);
		ModLoader.addLocalization("KeyNameHere", "KeyNameHere");
		ModLoader.setInGameHook(this, true, false);	
		
		ModLoader.registerKey(this, this.armDown, false);
		ModLoader.addLocalization("KeyNameHere", "KeyNameHere");
		ModLoader.setInGameHook(this, true, false);	
		
		ModLoader.registerKey(this, this.rightArmOut, false);
		ModLoader.addLocalization("KeyNameHere", "KeyNameHere");
		ModLoader.setInGameHook(this, true, false);				
		
		ModLoader.registerKey(this, this.armsUp, false);
		ModLoader.addLocalization("KeyNameHere", "KeyNameHere");
		ModLoader.setInGameHook(this, true, false);	
		
		ModLoader.registerKey(this, this.leftArmAngle, false);
		ModLoader.addLocalization("KeyNameHere", "KeyNameHere");
		ModLoader.setInGameHook(this, true, false);	
		
		ModLoader.registerKey(this, this.rightArmAngle, false);
		ModLoader.addLocalization("KeyNameHere", "KeyNameHere");
		ModLoader.setInGameHook(this, true, false);	
		
		ModLoader.registerKey(this, this.hideActorGUI, false);
		ModLoader.addLocalization("KeyNameHere", "KeyNameHere");
		ModLoader.setInGameHook(this, true, false);	
		
		
				
	}

	/**
	 * Adds all the keys to the game
	 */
	public void keyboardEvent(KeyBinding event)
	{
		if (!Vars.isInChat)
		{
			if (event == this.hideHotbar && !Vars.isServer)
			{	
				Vars.projectorView = !Vars.projectorView;
			}
			else if (event == this.hideActorGUI)
			{
				Vars.hideActorGUI = !Vars.hideActorGUI;
			}
			else if (event == this.armsUp)
			{
				// we expose this key for spectators to use it to follow projector
				if (Vars.isSpectator)
				{
					if (Vars.followProjector == 0)
						Vars.followProjector = 1;
					else if (Vars.followProjector == 2)
						Vars.followProjector = 3;
					//System.out.println("Vars.followProjector=" + Vars.followProjector);
				}
				else
				{
					(new Thread(new OperacraftKeyThread(Keyboard.KEY_U, "@arm 3"))).start();
				}
			}
		}
		if (!Vars.isInChat && !Vars.isSpectator)
		{						
			if (event == this.armDown)
			{
				(new Thread(new OperacraftKeyThread(Keyboard.KEY_J, "@arm 0"))).start();
			}
			else if (event == this.leftArmOut)
			{
				(new Thread(new OperacraftKeyThread(Keyboard.KEY_K, "@arm 1"))).start();
			}
			else if (event == this.rightArmOut)
			{
				(new Thread(new OperacraftKeyThread(Keyboard.KEY_L, "@arm 2"))).start();
			}
			else if (event == this.leftArmAngle)
			{
				(new Thread(new OperacraftKeyThread(Keyboard.KEY_I, "@arm 4"))).start();
			}
			else if (event == this.rightArmAngle)
			{
				(new Thread(new OperacraftKeyThread(Keyboard.KEY_O, "@arm 5"))).start();
			}		
		}		
	}
	
	/**
	 * Creates blocks and adds them to an array
	 * @param playerName the playersname (player1, player2)
	 * @param amount the number of mouths to create
	 */
	public static void createBlocks(String playerName, int amount)
	{
		System.out.println("mod_OperaCraft createBlocks " + playerName);
		int whichPlayer = Integer.parseInt(playerName.substring(playerName.length() - 1));
		ItemStack mouths[] = new ItemStack[amount];
		for (int i = 0; i < amount; i++)
		{
			Block block = createNewBlock(i, playerName);
			ModLoader.registerBlock(block);
			mouths[i] = new ItemStack(block);
			idStart++;
		}
		setVarsItemStack(mouths);
	}
	
	private static Block createNewBlock(int count, String player)
	{
		return new BlockHead(idStart, Material.cloth, count, player);
	}
	
	private static void setVarsItemStack(ItemStack[] entry)
	{
		Vars.mouthList.add(entry);
	}
}