package net.minecraft.src;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;

/**
 * Creates a new thread to count the key press time
 * @author Cody	Cahoon
 * @version 2014.05.26
 */
public class OperacraftKeyThread implements Runnable {

	private int key = -1;
	
	private String chat = "";
	
	public void run() {	
	
		long orig = System.currentTimeMillis();
		while (Keyboard.isKeyDown(this.key)){			
		}
		//how many seconds it is held
		double time = ((double) (System.currentTimeMillis() - orig) )/ 1000;
		String send = this.chat + " "+time;
		ModLoader.getMinecraftInstance().thePlayer.sendChatMessage(send);		
	}
	
	/**
	 * The constructor for the new thread to deal with the input
	 * of commands from PD
	 * @param inputSocket The socket already created, passed in from the CommandOpenSocket class
	 */
	public OperacraftKeyThread(int whichKey, String chatMessage)
	{
		this.key = whichKey;
		this.chat = chatMessage;
	}
	
}