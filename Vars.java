package net.minecraft.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
/**
 * A class holding many variables to help deal with arm states, mouth states,
 * camera views, projector views
 * @author Cody Cahoon
 * @author Ico Bukvic
 * @version 2015.10.05
 * 
 */
public class Vars {
	//public static String playersName = "";
	/**
	 * The name of the projector
	 */
	static String projectorName = "";
	
	/**
	 * For rendering textures
	 */
	static boolean firstTime = true;
	
	/**
	 * For rendering textures (actual texture)
	 */
	static String oldTexture = "";
	
	/**
	 * Error message for reading
	 */
	static final String errorRead = "§cError when reading.";
	
	/**
	 * Error message for writing AND reading
	 */	
	static final String errorReadWrite = "§cError when reading/writing.";
	
	/**
	 * Error message for not a number
	 */
	static final String errorNumber = "§cMust be a number.";
	
	/**
	 * Error message for writing
	 */
	static final String errorWrite = "§cError when writing.";

	/**
	 * Path to the txt files
	 */
	static String folderPath;
	
	/**
	 * Path to the mouth folder
	 */
	static String mouthsPath;
	
	/**
	 * Path for the mappings to the heads
	 */
	static String playerPath;
		
	/**
	 * The path to the teleport locations
	 */
	static String teleportPath ;
		
	/**
	 * The path to the color values and names
	 */
	static String nicknamesColorPath ;
	
	/**
	 * The path to the roles
	 */
	static String rolesPath;
	
	/**
	 * The path to the error messages
	 */
	static String failPath;

	/**
	 * The path to skins
	 */
	static String skinsPath;
	
	/**
	 * The path to cloaks
	 */
	static String cloaksPath;
	
	/**
	 * Used to request server status (mouths, arms, fade) once client is loaded
	 */
	static boolean clientInit = false;
	
	/**
	 * List of booleans to determine which fade to set
	 */
	static boolean[] fadeList = new boolean[16];
	
	/**
	 * Boolean if player is the projector
	 */	
	public static boolean isProjector = false;
	
	/**
	 * Boolean if player is the server (post-1.8.1)
	 */	
	public static boolean isServer = false;
	
	/**
	 * Boolean if player is an actor
	 */
	public static boolean isActor = false;
	
	/**
	 * Boolean if player is a camera man
	 */
	public static boolean isCamera = false;
	
	/**
	 * Boolean is player is a spectator
	 */
	public static boolean isSpectator = true;
	
	/**
	 * The value of the camera the projector is on
	 */
	static int cameraViewPortValue = 0;
	
	/**
	 * Used to measure how much time has transpired since the view request was made
	 */
	static long viewTimeDelta = 0;
	
	/**
	 * Value is used to hide the in game gui
	 * This is toggled by pressing 'h'
	 */
	static boolean projectorView = true;	
	
	/**
	 * The port number opened to receive messages from PD
	 */
	static int portNumber = -1;
	
	/**
	 * ICommandSender to get the EntityPlayerMP when watching the view
	 * Used when switching views between cameras
	 */
	static ICommandSender commandSenderServer = null;
	
	/**
	 * The array of OperaCraftPlayer's to deal with arm states, mouth states, and names
	 */
	static OperaCraftPlayer[] players = new OperaCraftPlayer[25];
	
	//CAMERA ROLES
	/**
	 * The map of camera values to players
	 */
	static HashMap<Integer, String> cameraValues = new HashMap<Integer, String>();
	
	/**
	 * The map of players to their camera values
	 */
	static HashMap<String, Integer> cameraReverseValues = new HashMap<String, Integer>();
	
	//PLAYER ROLES	
	/**
	 * The map of players to roles
	 */
	static HashMap<String, String> playerToRoles = new HashMap<String, String>();
	
	//PLAYER COLORS
	/**
	 * The map of players to color
	 */
	static HashMap<String, String> playerToColors = new HashMap<String, String>();
	
	//PLAYER NICKNAMES
	/**
	 * The map of players to nicknames
	 */
	static HashMap<String, String> playerToNickName = new HashMap<String, String>();
	
	//COLOR CODE FORMATS
	/**
	 * Map of colors to their codes
	 */
	static HashMap<String, String> colorCodes = new HashMap<String, String>();
	
	//PLAYERS to MOUTH
	/**
	 * Map of player1, player2, to username
	 */
	static HashMap<String, String> playerToUsername = new HashMap<String, String>();
	
	/**
	 * Map of username to player1, player2, etc.
	 */
	static HashMap<String, String> playerToUsernameReverse = new HashMap<String, String>();
	
	/**
	 * Number of mouths per user
	 */
	static HashMap<String, Integer> playerNumberOfMouths = new HashMap<String, Integer>();
	
	/**
	 * ArrayList to hold all spectators who are currently following projector view (only used by integrated server, in this case projector)
	 */
	static ArrayList<String> spectatorsFollowingProjector = new ArrayList<String>();
	
	/**
	 * ArrayList to hold all of the ItemStack[] arrays
	 */
	static ArrayList<ItemStack[]> mouthList = new ArrayList<ItemStack[]>();
	
	static ArrayList<String> changedGameTypeList = new ArrayList<String>();
	/**
	 * To change the splash ;)
	 */
	static boolean splash = false;
	
	/**
	 * Initializes the maps and OperaCraftPlayers once I am unsure when this originally will get
	 * called, which is why I have a boolean just to do it once
	 */
	private static boolean hasBeenInitialized = false;
		
	/**
	 * The minimum index of each array of heads is 0
	 */
	private static final int minIndex = 0;
	
	/**
	 * Boolean for the Actor's to hide/show the arm position options
	 */
	static boolean hideActorGUI = true;
	
	/**
	 * Boolean for spectator to follow projector (only used by spectators):
	 * 		0: off
	 * 		1: initiate on
	 * 		2: on
	 * 	   -1: initiate off
	 */
	static int followProjector = 0;
	
	/**
	 * Boolean for determining if the Player is typing a chat message
	 * Used to not trigger arm movements while in chat
	 */
	static boolean isInChat = false;
	
	/**
	 * Used for keeping track of the active camera among all users, not just projector
	 * 0 = no camera (default, projector is freely roaming);
	 */
	static String currentCamera = "freeroam";
	
	/**
	 * Used for dumping debug info into client log when running without console
	 */
	public static String debug = "";
	
	/**
	 * Stores OPERAcraft mod version
	 */
	public static String version = "null";
	
	/**
	 * Prevents redundant reopening of the port
	 */
	public static boolean portOpen = false;
	
	
	
	/*
	 * Is Kinect initialized
	 */

	public static boolean kinectInit = false;
	
	/*
	 * Whether to mirror Kinect arms
	 */	
	public static int kinectMirror = 1;
	
	/*
	 * Whether to enable Kinect affecting the avatar position
	 */
	public static int kinectPosition = 2;
	
	/*
	 * Whether we should track Kinect head rotation
	 */
	public static int kinectHead = 1;
	
	/*
	 * Whether to enable Kinect affecting the avatar position (0 = enabled, it is incremented to 10 whenever person moves or teleports, to allow for new value to set in, on 1 it captures last known location and 0 it resumes)
	 */
	public static int kinectPosCounter = 10;
	
	/**
	 * Initializes the map of player user names, the map of reverse player user names, and
	 * initializes all the OperaCraftPlayers into the players array
	 */
	public static void initialize() {
		if (!hasBeenInitialized) {			
			colorCodesIntoMap();			
			initializeOperaCraftPlayers();
			hasBeenInitialized = true;	
		}
	}		
	
	/**
	 * Adds the names into two maps in order for the chat to display the correct color for each player
	 */
	private static void colorCodesIntoMap()
	{			
		// the colors to their codes
		colorCodes.put("black", "§0");
		colorCodes.put("darkblue", "§1");
		colorCodes.put("darkgreen", "§2");
		colorCodes.put("darkaqua", "§3");
		colorCodes.put("darkred", "§4");
		colorCodes.put("darkpurple", "§5");
		colorCodes.put("gold", "§6");
		colorCodes.put("gray", "§7");
		colorCodes.put("darkgray", "§8");
		colorCodes.put("blue", "§9");
		colorCodes.put("green", "§a");
		colorCodes.put("aqua", "§b");
		colorCodes.put("red", "§c");
		colorCodes.put("lightpurple", "§d");
		colorCodes.put("yellow", "§e");
		colorCodes.put("white", "§f");		
	}	
	
	/**
	 * Initializes all the OperaCraftPlayer's (25 max) to have the name
	 * not assigned and default values
	 */
	private static void initializeOperaCraftPlayers()
	{
		for (int i = 0;i < players.length; i++)
		{
			players[i] = new OperaCraftPlayer();
		}
	}
	
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------OPERACRAFT PLAYER SECTION--------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Adds the name to the database of users
	 * @param newUser The new user wanting to be added
	 */
	public static void addNamesToDatabase(String newUser)
	{			
		if (!searchNameDatabase(newUser) && findNextEmptySpot() != -1)
		{			
			players[findNextEmptySpot()] = new OperaCraftPlayer(newUser);			
		}			
	}
	
	/**
	 * Adds the other players in the game to the clients array of OperaCraftPlayer
	 */
	public static void addOlderPlayers()
	{
		String[] usernames = Minecraft.getMinecraft().getIntegratedServer().getAllUsernames();
		for (String name : usernames)
		{
			addNamesToDatabase(name);
		}
	}
	
	/**
	 * Finds the next empty spot in an array
	 * @param arrayOfNames The array to search for an empty spot
	 * @return the empty spot
	 */
	public static int findNextEmptySpot()
	{
		for (int i = 0;i < players.length; i++)
		{
			if (players[i].getUsername().equals("notassigned"))
			{
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Searches through the database of players in the game to see
	 * if the name already exists.
	 * This is method is called when using addNameToDatabase.
	 * @param name The name to search for
	 * @return true if the player is in the database
	 */
	public static boolean searchNameDatabase(String name)
	{			
		for (int i = 0; i < players.length; i++)
		{		
			create();
			if (name.equals(players[i].getUsername()))
			{	
				return true;
			}
		}
		return false;
	}
	
	/**
	 * (Not needed anymore)
	 * Outputs all the names of the players
	 * Only one list is kept. Whenever a new player joins the game, the name is
	 * added to the host's list. When the command /names is typed the list
	 * shown is of the host's.
	 * @return A string of all the players in the game
	 */
	public static String outprintPlayerNames()
	{
		String temp = "";
		for (int i = 0;i < players.length; i++)
		{
			temp+= i + " " + players[i].getUsername() + " ";
		}
		return temp;
	}	
	
	/**
	 * Checks if the player exists in the array of OperaCraftPlayer's
	 * @param user The player user name to check for
	 * @return true if in the array, false if not
	 */
	public static boolean doesOperaCraftPlayerExist(String user)
	{
		if (hasBeenInitialized) {
			for (OperaCraftPlayer player : players)
			{
				if (player.getUsername().equals(user))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Calls the initialize method if it has not been called yet
	 */
	public static void create()
	{
		if (!hasBeenInitialized)
		{
			initialize();
		}
	}
	
	

	/**
	 * Gets the OperaCraftPlayer
	 * @return The pointer to OperaCraftPlayer
	 */
	public static OperaCraftPlayer getOperaCraftPlayer(String user)
	{
		if (hasBeenInitialized) {
			for (OperaCraftPlayer player : players)
			{
				if (player.getUsername().equals(user))
				{
					return player;
				}
			}
		}
		return null;
	}
	
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
//------------------------------------------------------------------------FADE SECTION------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	
    public static int getFadeNum()
    {
		int fadeNum = 0;
		for (int j = 0; j < Vars.fadeList.length; j++)
		{
			if (Vars.fadeList[j])
			{
				fadeNum = j;
			}
			else if (!Vars.fadeList[j])
			{
				break;
			}
		}
		return fadeNum;
    }
	
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
//------------------------------------------------------------------------ARM SECTION------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	
	/**
	 * Changes the state of the arm of a specific OperaCraftPlayer
	 * This method is only called if the doesOperaCraftExist method is true
	 * @param userName The name to change arm state
	 * @param armState The arm state to change to
	 * @param time The rate of change of the arm state
	 */	
	public static void changeOperaCraftArmState(String userName, int armState, double time)
	{
		if (hasBeenInitialized) {
			for (OperaCraftPlayer player : players)
			{
				if (player.getUsername().equals(userName))
				{
					player.setArmState(armState, time);
					break;
				}
			}
		}
	}	
	
	
	
	
	public static void changeKinectArmAngles(String userName, double LA, double LVX, double LVZ, double RA, double RLX, double RLZ)
	{
		//System.out.println("changeKinectArmAngles " + userName);
		if (hasBeenInitialized) {
			for (OperaCraftPlayer player : players)
			{
				if (player.getUsername().equals(userName))
				{
					player.LArmRot= (float)Math.toRadians(LA);
					player.LArmOrthoX = (float)LVX;
					player.LArmOrthoZ = (float)LVZ;
					player.RArmRot = (float)Math.toRadians(RA);
					player.RArmOrthoX = (float)RLX;
					player.RArmOrthoZ = (float)RLZ;
					
					break;
				}
			}
		}
	}


	public static void changeKinectShoulderAngles(String userName,  double LY, double LZ, double RY, double RZ)
	{
		//System.out.println("changeKinectArmAngles " + userName);
		if (hasBeenInitialized) {
			for (OperaCraftPlayer player : players)
			{
				if (player.getUsername().equals(userName))
				{
					if (Vars.kinectMirror == 0)
					{
						player.RShoulderY = (float)RY;
						player.RShoulderZ = (float)RZ;
						
						player.LShoulderY = (float)LY;
						player.LShoulderZ = (float)LZ;
					}
					else
					{
						player.RShoulderY = (float)LY;
						player.RShoulderZ = (float)LZ;
						
						player.LShoulderY = (float)RY;
						player.LShoulderZ = (float)RZ;
					}

					break;
				}
			}
		}
	}
	
	
	
	/**
	 * Returns the arm state of the OperaCraftPlayer with a specified name
	 * @param user The user to search for
	 * @return The integer value for the current arm state
	 */	
	public static int getOperaCraftPlayerArmState(String user)
	{
		if (hasBeenInitialized) {
			for (OperaCraftPlayer player : players)
			{
				if (player.getUsername().equals(user))
				{
					return player.getArmState();
				}
			}
		}
		return -1;
	}

	
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------TORSO SECTION----------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------

	public static void changeKinectTorsoPos(String userName, double X, double Y, double Z, double R)
	{
		//System.out.println("changeKinectTorsoPos " + userName);
		if (hasBeenInitialized) {
			for (OperaCraftPlayer player : players)
			{
				if (player.getUsername().equals(userName))
				{
					player.TorsoX = (float)X;
					player.TorsoY = (float)Y;
					player.TorsoZ = (float)Z;
					player.TorsoRot= (float)Math.toRadians(R);
					//System.out.println("BLAH");
					
					break;
				}
			}
		}
	}
	
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------LEG SECTION----------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Changes the state of the leg of a specific OperaCraftPlayer
	 * This method is only called if the doesOperaCraftExist method is true
	 * @param userName The name to change leg state
	 * @param armState The leg state to change to
	 * @param time The rate of change of the leg state
	 */	
	public static void changeKinectLegAngles(String userName, double LA, double LVX, double LVZ, double RA, double RLX, double RLZ)
	{
		//System.out.println("changeKinectArmAngles " + userName);
		if (hasBeenInitialized) {
			for (OperaCraftPlayer player : players)
			{
				if (player.getUsername().equals(userName))
				{
					player.LLegRot= (float)Math.toRadians(LA);
					player.LLegOrthoX = (float)LVX;
					player.LLegOrthoZ = (float)LVZ;
					player.RLegRot = (float)Math.toRadians(RA);
					player.RLegOrthoX = (float)RLX;
					player.RLegOrthoZ = (float)RLZ;
					
					break;
				}
			}
		}
	}
	
	

	/**
	 * Returns the leg state of the OperaCraftPlayer with a specified name
	 * @param user The user to search for
	 * @return The integer value for the current leg state
	 */	
	public static int getOperaCraftPlayerLegState(String user)
	{
		if (hasBeenInitialized) {
			for (OperaCraftPlayer player : players)
			{
				if (player.getUsername().equals(user))
				{
					return player.getLegState();
				}
			}
		}
		return -1;
	}
	
	public static void changeOperaCraftLegStateAngles(String userName, float XL, float ZL, float XR, float ZR)
	{
		System.out.println("username " + userName);
		System.out.println("size of players " +players.length);
		if (hasBeenInitialized) {
			for (OperaCraftPlayer player : players)
			{
				System.out.println("User " + player.getUsername());
				if (player.getUsername().equals(userName))
				{
					player.adjustLegs(XL,  ZL, XR, ZR);
					System.out.println("Value of angles in Vars. java XL" + XL +
	    					 " ZL: "+ ZL + " XR: "+ XR + " ZR: "+ ZR);
					break;
				}
			}
		}
	}
	
	
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------SHOULDER SECTION----------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	
	/**
	 * Changes the state of the shoulder of a specific OperaCraftPlayer
	 * This method is only called if the doesOperaCraftExist method is true
	 * @param userName The name to change shoulder state
	 * @param armState The shoulder state to change to
	 * @param time The rate of change of the shoulder state
	 */	
	public static void changeOperaCraftShoulderState(String userName, int shoulderState, double time)
	{
		if (hasBeenInitialized) {
			for (OperaCraftPlayer player : players)
			{
				if (player.getUsername().equals(userName))
				{
					player.setArmState(shoulderState, time);
					break;
				}
			}
		}
	}	
	

	/**
	 * Returns the shoulder state of the OperaCraftPlayer with a specified name
	 * @param user The user to search for
	 * @return The integer value for the current shoulder state
	 */	
	public static int getOperaCraftPlayerShoulderState(String user)
	{
		if (hasBeenInitialized) {
			for (OperaCraftPlayer player : players)
			{
				if (player.getUsername().equals(user))
				{
					return player.getShoulderState();
				}
			}
		}
		return -1;
	}
	
	
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------HEAD SECTION-----------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
		

	public static void changeKinectHeadAngle(String userName, double X, double Y, double Z)
	{
		//System.out.println("changeKinectTorsoPos " + userName);
		if (hasBeenInitialized) {
			for (OperaCraftPlayer player : players)
			{
				if (player.getUsername().equals(userName))
				{
					if (Vars.kinectMirror == 1)
					{
						player.headPitch = -(float)Math.toRadians(X);
						player.headYaw = (float)Math.toRadians(Y);
						player.headRoll = -(float)Math.toRadians(Z);
					}
					else
					{
						player.headPitch = -(float)Math.toRadians(X);
						player.headYaw = -(float)Math.toRadians(Y);
						player.headRoll = (float)Math.toRadians(Z);						
					}
					//System.out.println("Head updated");
					
					break;
				}
			}
		}
	}

	
	
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
//------------------------------------------------------------------------MOUTH SECTION----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Sets the mouth state of the character inputed (real user name is used)	 * 
	 * @param name The user name to change
	 * @param value Which helmet to change to (0 is default)
	 * @return The specific player's new mouth
	 */
	public static ItemStack getHeadBlock(String name, int value) {		
		if (value < 0 || (Vars.playerToUsernameReverse.containsKey(name) && Vars.playerNumberOfMouths.get(Vars.playerToUsernameReverse.get(name)) <= value))
			value = 0;	
		if (Vars.playerToUsernameReverse.containsKey(name))
		{
			String playerN = Vars.playerToUsernameReverse.get(name);
			int n = Integer.parseInt(playerN.substring(playerN.length() - 1));
			return Vars.mouthList.get(n)[value];
		}
		else
			return null;		
	}
	
	/**
	 * Changes the mouth state of the specified OperaCraftPlayer
	 * @param userName The user to search for
	 * @param mouthState The mouth state to change
	 */
	public static void changeOperaCraftMouthState(String userName, int mouthState)
	{
		if (hasBeenInitialized) {
			for (OperaCraftPlayer player : players)
			{
				if (player.getUsername().equals(userName))
				{
					player.setMouthState(mouthState);
					break;
				}
			}
		}
	}
	
	/**
	 * Returns the mouth state of the OperaCraftPlayer with a specified name
	 * @param user The user to search for
	 * @return The integer value for the current mouth state
	 */
	public static int getOperaCraftPlayerMouthState(String user)
	{
		if (hasBeenInitialized) {
			for (OperaCraftPlayer player : players)
			{
				if (player.getUsername().equals(user))
				{
					return player.getMouthState();
				}
			}
		}
		return -1;
	}	
	
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
//------------------------------------------------------------------------CAMERAMAN SECTION----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	/**
	 * Gets the camera man name based of 0 (none) 1 - 5
	 * @param value Which camera to get or to let go
	 * @return The string of the camera
	 */
	public static String getCameraByValue(int value)
	{
		if (value == 0 || !Vars.cameraValues.containsKey(value))
		{
			return "freeroam";
		}
		else
		{
			return Vars.cameraValues.get(value);
		}
	}
	
	public static int getCameraByName(String value)
	{
		if (Vars.cameraReverseValues.containsKey(value))
		{
			return Vars.cameraReverseValues.get(value);
		}
		else
		{
			return 0;
		}		
	}
}
