package net.minecraft.src;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

import net.minecraft.client.Minecraft;

/**
 * Creates a new thread to read messages
 * @author Cody	Cahoon
 * @version 2013.11.20
 */
public class OpenSocketThread implements Runnable {

	private DatagramSocket socket;	
	
	public void run() {		
		byte[] buffer = new byte[2048];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);		
		while (true) {
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String msg = new String(buffer, 0, packet.getLength());
			// removes the end semicolon in the message from PD
			msg = msg.substring(0, msg.length() - 2);
			// splits the words based on spaces
			String[] words = msg.split(" ");
			// reads the message
			this.interpretMessage(words);
			packet.setLength(buffer.length);
		}		
	}
	
	/**
	 * The constructor for the new thread to deal with the input
	 * of commands from PD
	 * @param inputSocket The socket already created, passed in from the CommandOpenSocket class
	 */
	public OpenSocketThread(DatagramSocket inputSocket)
	{
		this.socket = inputSocket;
	}	
	
	/**
	 * Reads the message sent from pd-extended
	 * @param messageFromPD The String[] of words sent from pd
	 */
	private void interpretMessage(String[] messageFromPD)
	{
		if (messageFromPD.length == 0)
		{
			//do nothing
		}
		//if we are sending generic minecraft messages that start with a slash (e.g. /time set 0)
		else if (messageFromPD[0].startsWith("/"))
		{
			String mCommand = Arrays.toString(messageFromPD);
			mCommand = mCommand.replace(",", "");
			mCommand = mCommand.substring(1, mCommand.length() - 1);
			Minecraft.getMinecraft().getIntegratedServer().executeCommand(mCommand);
		}
		//if there is a @warn message sent
		else if (messageFromPD.length > 1 && messageFromPD[0].equals("@warn"))
		{
			String finalMessage = "";
			for (int i = 0; i < messageFromPD.length; i++)
			{
				finalMessage += messageFromPD[i];
				if (i != messageFromPD.length - 1)				
					finalMessage += " ";				
			}
			Minecraft.getMinecraft().thePlayer.sendChatMessage(finalMessage);
		}		
		else if (messageFromPD.length == 2)
		{
			//sets the time of day
			if (messageFromPD[0].equals("@time"))
			{
				CommandTime time = new CommandTime();
				String[] setAndValue = {"set", messageFromPD[1]};
				time.processCommand(Minecraft.getMinecraft().thePlayer, setAndValue);
			}
			
			else if (messageFromPD[0].equals("@view"))
			{
				Vars.viewTimeDelta = Minecraft.getSystemTime();
				//disabled in 1.7.0 -- moved to GuiNewChat
				/*if (messageFromPD.length > 1 && messageFromPD[0].equals("@view"))
				{
					if (Vars.isProjector)
					{
						Vars.cameraViewPortValue = Integer.parseInt(messageFromPD[1]);
						String camera = Vars.getCameraByValue(Vars.cameraViewPortValue);
						String[] words = {camera};
						CommandSwitchViewPort switchPort = new CommandSwitchViewPort();
						switchPort.processCommand(Minecraft.getMinecraft().thePlayer, words);
					}
					//System.out.println("@view old=" + Vars.currentCamera +" new=" + Integer.parseInt(messageFromPD[1]) + " newname=" + Vars.getCameraByValue(Integer.parseInt(messageFromPD[1])) + "\n");
					Vars.currentCamera = Vars.getCameraByValue(Integer.parseInt(messageFromPD[1]));
				}*/
				
				String finalMessage = "";
				for (int i = 0; i < messageFromPD.length; i++)
				{
					finalMessage += messageFromPD[i];
					if (i != messageFromPD.length - 1)				
						finalMessage += " ";
				}
				finalMessage += " " + Vars.currentCamera;
				Minecraft.getMinecraft().thePlayer.sendChatMessage(finalMessage);
			}
			//tpall to a certain location
			else if (messageFromPD[0].equals("@tpall"))
			{
				Minecraft.getMinecraft().getIntegratedServer().executeCommand(String.format("tpall %s", messageFromPD[1]));
			}
			//color for the players
			else if (messageFromPD[1].equals("@text") && messageFromPD[0].equals("null"))
			{
				//System.out.println("GOT NULL MESSAGE");
				String[] wordsToPrintToChat = {"null"};
				String wordsToSend = "@text null";
				CommandColorText color = new CommandColorText();
				color.processCommand(Minecraft.getMinecraft().thePlayer, wordsToPrintToChat);
				
				// send this to Spectators as this is usually used to clear the chat log (clear the subtitles)
				Minecraft.getMinecraft().thePlayer.sendChatMessage(wordsToSend);
			}
		}
		else if (messageFromPD.length >= 3)
		{			
			//for mouth movement
			if (messageFromPD[1].equals("@mouth"))
			{				
				Minecraft.getMinecraft().thePlayer.sendChatMessage(String.format("@mouth %s %s", messageFromPD[0], messageFromPD[2]));				
			}
			//for teleporting one character at a time
			else if (messageFromPD[0].equals("@tpone") && messageFromPD.length == 3)
			{
				//new experimental way
				Minecraft.getMinecraft().thePlayer.sendChatMessage(String.format("@tpone %s %s", messageFromPD[1], messageFromPD[2]));
				//old way that works (may be asynchronous and therefore potentially unstable)
				//Minecraft.getMinecraft().getIntegratedServer().executeCommand(String.format("tpone %s %s", messageFromPD[1], messageFromPD[2]));
			}
			//for subtitles
			else if (messageFromPD[1].equals("@text"))
			{
				String[] wordsToPrintToChat = new String[messageFromPD.length - 1];
				String wordsToSend = "";
				wordsToPrintToChat[0] = messageFromPD[0];
				wordsToSend += "@text " + messageFromPD[0] + " ";
				for (int i = 2; i < messageFromPD.length; i++)
				{
					wordsToPrintToChat[i - 1] = messageFromPD[i];
					wordsToSend += messageFromPD[i] + (i < messageFromPD.length - 1 ? " " : "");
				}
				CommandColorText color = new CommandColorText();
				color.processCommand(Minecraft.getMinecraft().thePlayer, wordsToPrintToChat);
				// now send the same message to everyone else (e.g. spectators)
				Minecraft.getMinecraft().thePlayer.sendChatMessage(wordsToSend);
			}
			//for fading the screen
			else if (messageFromPD[1].equals("@fade"))
			{
				String username = Minecraft.getMinecraft().thePlayer.getEntityName();
				if (username.equals(messageFromPD[0]))
				{
					int fadeNum = Integer.parseInt(messageFromPD[2]);
					this.turnOffAllFades();
					this.turnOnAllFadesStartingAt(fadeNum);
				}
				// also send the same message to all players (even though only spectators will get it)
				Minecraft.getMinecraft().thePlayer.sendChatMessage(String.format("@fade %s", messageFromPD[2]));
			}
		}
	}
	/**
	 * Turns off all the fades to interpret the next fade
	 */
	private void turnOffAllFades()
	{
		for (int i =0; i < 16; i++)
		{
			Vars.fadeList[i] = false;
		}
	}
	/**
	 * Turns on all the fades from the number entered to 0
	 * @param num The fade level
	 */
	private void turnOnAllFadesStartingAt(int num)
	{
		for (int i = num; i >= 0; i--)
		{
			Vars.fadeList[i] = true;
		}
	}	
}