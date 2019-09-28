package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiNewChat extends Gui
{
    /** The Minecraft instance. */
    private final Minecraft mc;

    /** A list of messages previously sent through the chat GUI */
    private final List sentMessages = new ArrayList();

    /** Chat lines to be displayed in the chat box */
    private final List chatLines = new ArrayList();
    private final List field_96134_d = new ArrayList();
    private int field_73768_d = 0;
    private boolean field_73769_e = false;

    public GuiNewChat(Minecraft par1Minecraft)
    {
        this.mc = par1Minecraft;
    }

    public void drawChat(int par1)
    {
        if (this.mc.gameSettings.chatVisibility != 2)
        {
            int var2 = this.func_96127_i();
            boolean var3 = false;
            int var4 = 0;
            int var5 = this.field_96134_d.size();
            float var6 = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;

            if (var5 > 0)
            {
                if (this.getChatOpen())
                {
                    var3 = true;
                }

                float var7 = this.func_96131_h();
                int var8 = MathHelper.ceiling_float_int((float)this.func_96126_f() / var7);
                GL11.glPushMatrix();
                GL11.glTranslatef(2.0F, 20.0F, 0.0F);
                GL11.glScalef(var7, var7, 1.0F);
                int var9;
                int var11;
                int var14;

                for (var9 = 0; var9 + this.field_73768_d < this.field_96134_d.size() && var9 < var2; ++var9)
                {
                    ChatLine var10 = (ChatLine)this.field_96134_d.get(var9 + this.field_73768_d);

                    if (var10 != null)
                    {
                        var11 = par1 - var10.getUpdatedCounter();

                        if (var11 < 200 || var3)
                        {
                            double var12 = (double)var11 / 200.0D;
                            var12 = 1.0D - var12;
                            var12 *= 10.0D;

                            if (var12 < 0.0D)
                            {
                                var12 = 0.0D;
                            }

                            if (var12 > 1.0D)
                            {
                                var12 = 1.0D;
                            }

                            var12 *= var12;
                            var14 = (int)(255.0D * var12);

                            if (var3)
                            {
                                var14 = 255;
                            }

                            var14 = (int)((float)var14 * var6);
                            ++var4;

                            if (var14 > 3)
                            {
                                byte var15 = 0;
                                int var16 = -var9 * 9;
                                drawRect(var15, var16 - 9, var15 + var8 + 4, var16, var14 / 2 << 24);
                                GL11.glEnable(GL11.GL_BLEND);
                                String var17 = var10.getChatLineString();

                                if (!this.mc.gameSettings.chatColours)
                                {
                                    var17 = StringUtils.stripControlCodes(var17);
                                }

                                this.mc.fontRenderer.drawStringWithShadow(var17, var15, var16 - 8, 16777215 + (var14 << 24));
                            }
                        }
                    }
                }

                if (var3)
                {
                    var9 = this.mc.fontRenderer.FONT_HEIGHT;
                    GL11.glTranslatef(-3.0F, 0.0F, 0.0F);
                    int var18 = var5 * var9 + var5;
                    var11 = var4 * var9 + var4;
                    int var20 = this.field_73768_d * var11 / var5;
                    int var13 = var11 * var11 / var18;

                    if (var18 != var11)
                    {
                        var14 = var20 > 0 ? 170 : 96;
                        int var19 = this.field_73769_e ? 13382451 : 3355562;
                        drawRect(0, -var20, 2, -var20 - var13, var19 + (var14 << 24));
                        drawRect(2, -var20, 1, -var20 - var13, 13421772 + (var14 << 24));
                    }
                }

                GL11.glPopMatrix();
            }
        }
    }

    /**
     * Clears the chat.
     */
    public void clearChatMessages()
    {
        this.field_96134_d.clear();
        this.chatLines.clear();
        this.sentMessages.clear();
    }
    
	/**
	 * 
	 * IMPORTED FROM CommandColorText
	 * Gets the long message from the input arguments
	 * Does not add the characters m,1,2,0 to the output text
	 * @param args The words to output
	 */
	private String getLongMessage(String[] args)
	{
		String msg = "";
		for (int i = 1; i < args.length; i++) 
		{
			msg += args[i] + " ";	
		}
		return msg;
	}
	
	/**
	 * IMPORTED FROM CommandColorText
	 * Uses the first input to determine what the chat will look like
	 * @param args The first word is going to be used, but the entire word array is input
	 * @return the user name with the edited name and color
	 */
	private String getAndColorUsername(String[] args)
	{
		if (args[0].equals("null"))
			return "";
		else if (Vars.playerToColors.containsKey(args[0]))
		{
			if (Vars.colorCodes.containsKey(Vars.playerToColors.get(args[0])))		
				return Vars.colorCodes.get(Vars.playerToColors.get(args[0])) + args[0] + ": ";
			return args[0] + ": ";
		}
		else
			return args[0] + ": ";
		
	}
		
    /**
     * takes a String and prints it to chat
     * @param par1Str The message to print
     */
    public void printChatMessage(String par1Str)
    {
    	
    	String[] words = par1Str.split(" ");
    	/*if (words.length > 0 && words[words.length - 1].length() > 4 && words[words.length - 1].substring(0, 5).equals("game!"))
    	{
    		//System.out.println("CHECK IF NEED TO KICK: " + par1Str);
    		//words[0] = "/kick " + words[0].substring(2, words[0].length() - 2);
    		//System.out.println(words[0]);
    		//Minecraft.getMinecraft().getIntegratedServer().executeCommand(words[0]);    		
    	}
    	else*/ if (par1Str.length() >= 5 && par1Str.substring(0, 5).equals("Local"))
    	{
    		this.printChatMessageWithOptionalDeletion(par1Str, 0);
    	}
    	else
    	{
	    	boolean displayWords = true, isWarning = false;								//whether to display the message
	    	String[] message = par1Str.split(" ");   									//splits the message by spaces

	    	//String usernameWhoSent = message[0].substring(1, message[0].length() - 1);
    		//System.out.println("\n-------------" + par1Str + " || " + message.length);
    		if (message.length > 2)
	    	{
    			if (message[1].equals("@kinectmirror"))
    			{
    				int var = Integer.parseInt(message[2]);
    				if (var < 0)
    					var = 0;
    				if (var > 1)
    					var = 1;
    				Vars.kinectMirror = var;
    			}
    			else if (message[1].equals("@kinectposition"))
    			{
    				int var = Integer.parseInt(message[2]);
    				if (var < 0)
    					var = 0;
    				if (var > 2)
    					var = 2;
    				Vars.kinectPosition = var;
    				Vars.kinectPosCounter = 10;
    				//TODO: reset rotation values
    				
    				if (var == 1)
    				{
	    		        for (int i = 0; i < Vars.players.length; i++)
	    		        {
	    		        	if (!Vars.players[i].getUsername().equals("notassigned"))
	    		        	{
	    		        		String username = Vars.players[i].getUsername();
	    		        		Vars.changeKinectLegAngles(username, 0, 0, 0, 0, 0, 0);
	    		        	}
	    		        }
    				}
    				else if (var == 0)
    				{
	    		        for (int i = 0; i < Vars.players.length; i++)
	    		        {
	    		        	if (!Vars.players[i].getUsername().equals("notassigned"))
	    		        	{
	    		        		String username = Vars.players[i].getUsername();
	    		        		Vars.changeKinectHeadAngle(username, 0, 0, 0);
	    		        		Vars.changeKinectArmAngles(username, 0, 0, 0, 0, 0, 0);
	    		        		Vars.changeKinectShoulderAngles(username, 0, 0, 0, 0);
	    		        		Vars.changeKinectTorsoPos(username, 0, 0, 0, 0);
	    		        		Vars.changeKinectLegAngles(username, 0, 0, 0, 0, 0, 0);
	    		        	}
	    		        }
    				}
    				
    				//System.out.println("BLAH");
    			}
    			else if (message[1].equals("@kinecthead"))
    			{
    				int var = Integer.parseInt(message[2]);
    				if (var < 0)
    					var = 0;
    				if (var > 1)
    					var = 1;
    				Vars.kinectHead = var;
    				//TODO: reset rotation values
    		        for (int i = 0; i < Vars.players.length; i++)
    		        {
    		        	if (!Vars.players[i].getUsername().equals("notassigned"))
    		        	{
    		        		String username = Vars.players[i].getUsername();
    		        		Vars.changeKinectHeadAngle(username, 0, 0, 0);
    		        	}
    		        }
    			}
    			else if (message[1].equals("@tpspec"))
        		{
        			//System.out.println(par1Str);
        			String senderUsername = message[0].substring(1, message[0].length() - 1);
        			if (Vars.projectorName != "" && Vars.isServer) //only do this if we are on the client who has integrated server running
        			{
        				//System.out.println("+++ " + message[0] + " " + message[1] + " " + message[2]);
        				/* message is:
        					1: <username>
        					2: @tpspec
        					3: target_username
        					4: optional:toggle follow
        				*/
        				boolean teleportMe = false;
        				// check if we have optional 4th parameter (to determine whether we should follow the projector)
        				if (message.length > 3)
        				{
        					int follow = Integer.parseInt(message[3]); // 0 for off, 2 for on
        					if (follow == 2)
        					{
        						//if we are not already following camera
        						if (!Vars.spectatorsFollowingProjector.contains(senderUsername))
        						{
        	    					teleportMe = true;
        	    					Vars.spectatorsFollowingProjector.add(senderUsername);
        						}
        					}
        					else if (follow == 0)
        					{
        						Vars.spectatorsFollowingProjector.remove(senderUsername);
        					}
        				}
        				else
        				{
        					teleportMe = true;
        				}
        				if (teleportMe)
        				{
        					
    	    				//CommandServerTp tp = new CommandServerTp();
    	        			//String[] setAndValue = {message[2]};
    	        			Minecraft.getMinecraft().getIntegratedServer().executeCommand("/tp " + senderUsername + " " + Vars.projectorName);
        				}
        			}
        			displayWords = false;
        		}
        		else if (message[1].equals("@updatetp"))
        		{
        			
        			if (Vars.projectorName != "" && Vars.isServer) //only do this if we are on the client who has integrated server running
        			{
    	    			//update tp location
    					/* message is:
    						1: <target_username>
    						2: @updatetp
    						3: username
    					 */
    	    			String target = message[0].substring(1, message[0].length() - 1);
    	    			Minecraft.getMinecraft().getIntegratedServer().executeCommand("/tp " + Vars.projectorName + " " + target);
        			}	
    	    		displayWords = false;
        		}
        		else if (message[1].equals("@view"))
	    		{
					if (Vars.isServer)
					{
						Vars.cameraViewPortValue = Integer.parseInt(message[2]);
						String camera = Vars.getCameraByValue(Vars.cameraViewPortValue);
						String[] viewWords = {camera};
						//do we need this? it seems to make system less stable
						//Minecraft.getMinecraft().getIntegratedServer().executeCommand("/tp " + Vars.projectorName + " " + Vars.projectorName);
						CommandSwitchViewPort switchPort = new CommandSwitchViewPort();
						switchPort.processCommand(Minecraft.getMinecraft().thePlayer, viewWords);
					}
					//System.out.println("@view old=" + Vars.currentCamera +" new=" + Integer.parseInt(messageFromPD[1]) + " newname=" + Vars.getCameraByValue(Integer.parseInt(messageFromPD[1])) + "\n");
					Vars.currentCamera = Vars.getCameraByValue(Integer.parseInt(message[2]));
        			
	    			/*if (message[message.length -1] != Vars.currentCamera)
	    			{
	    				Vars.currentCamera = message[message.length-1];
	    			}*/
	    			displayWords = false;
	    		}
	    		else if (message[1].equals("@warn"))
	    		{
	    			isWarning = true;
	    			if (Vars.isProjector || Vars.isSpectator)
	    			{
	    				displayWords = false;
	    			}
	    			else
	    			{
	    				String finalMessage = "�c";
	    				for (int i = 2; i < message.length; i++)
	    				{
	    					finalMessage += message[i];
	    					if (i != message.length - 1)
	    					{
	    						finalMessage += " ";
	    					}
	    				}
	    				par1Str = finalMessage;
	    			}
	    		}
	    		else if (message[1].equals("@tpone"))
	    		{
	    			displayWords = false;
	    			if (Vars.isServer)
	    			{
	    				Minecraft.getMinecraft().getIntegratedServer().executeCommand(String.format("tpone %s %s", message[2], message[3]));
	    			}
	    		}
	    		else if (message[1].equals("@text"))
	    		{
	    			//System.out.println("+++GOT @text" + par1Str + " " + message.length);
	    			displayWords = false;
	    			// this is where @text messages will also land--show them only if we are a Spectator
	    			if (Vars.isSpectator)
	    			{
	    				String[] wordsToPrintToChat = new String[message.length - 2];
	    				wordsToPrintToChat[0] = message[2];
	    				for (int i = 3; i < message.length; i++)
	    				{
	    					wordsToPrintToChat[i - 2] = message[i];
	    				}
	    				String msg = this.getAndColorUsername(wordsToPrintToChat) + this.getLongMessage(wordsToPrintToChat);
	    				//System.out.println("***" + msg);
	    				par1Str = msg;
	    				clearChatMessages();
	    				if (message.length > 3) // size of 3 is when it is an empty message which is used to clear the subtitles
	    					this.printChatMessageWithOptionalDeletion(msg, 0);
	    			}
	    		}
	    		else if (message[1].equals("@mouth"))
	    		{
	    			if (message.length == 4) /// //need to check on length why is it 4 ? does it need to be 5 ?
	    			{
		    			displayWords = false;
		    			String username = message[2];
		    			int mouthState = Integer.parseInt(message[3]);
		    			if (!Vars.doesOperaCraftPlayerExist(username)) //checks if the user name is in player's OperaCraftPlayer list and if not adds them to the list
		    			{
		    				if (Minecraft.getMinecraft().theWorld.getPlayerEntityByName(username) != null)
		    				{
		    					Vars.addNamesToDatabase(username);
		    				}
		    			}
		    			if (Vars.doesOperaCraftPlayerExist(username) && Vars.playerToRoles.containsKey(username) && Vars.playerToRoles.get(username).equals("actor"))						
		    			{			
		    				Vars.changeOperaCraftMouthState(username, mouthState);			//changes the player's mouth to the specified value
		    				int mouthValue = Vars.getOperaCraftPlayerMouthState(username);    				
		    				ItemStack block = Vars.getHeadBlock(username, mouthValue);
		    				if (block != null)
		    				{
		    					EntityPlayer player = null;
		    					if (Minecraft.getMinecraft().theWorld.getPlayerEntityByName(username) != null)
		    					{
				    				Minecraft.getMinecraft().theWorld.getPlayerEntityByName(username).setCurrentItemOrArmor(4, block);    				
				                   	player = Minecraft.getMinecraft().theWorld.getPlayerEntityByName(username);
		    					}
								if (player != null && player instanceof EntityPlayerMP)
								{
									((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(new Packet5PlayerInventory(Minecraft.getMinecraft().theWorld.getPlayerEntityByName(username).entityId, 4, block));
								}
		    				}
		    			}
	    			}
	    		}
	    		/*else if (message[1].equals("@arm"))
	    		{
	    			if (message.length == 5) /// //need to check on length why is it 4 ? does it need to be 5 ?
	    			{	System.out.println("message length is :   " + message.length + ", and message[0] is " + message[0] +  "message[1] is " + message[1] + "message[2] is " + message[2] +  "message[3] is " + message[3] );
		    			
		    		{
	    				String username = message[2];
		    			int armState = Integer.parseInt(message[3]);						//retrieves the value or position of the arm
		    			double time = Double.parseDouble(message[4]); /// TODO: how to figure time out ?
		    			if (Vars.doesOperaCraftPlayerExist(username))						//checks if the user name is in player's OperaCraftPlayer list
		    			{
		    				Vars.changeOperaCraftArmState(username, armState, time);				//changes the player's arm to the specified value
		    			}
	    			}
	    		
		    			displayWords = false;
		    			String username = message[2];
		    			int mouthState = Integer.parseInt(message[3]);
		    			if (!Vars.doesOperaCraftPlayerExist(username)) //checks if the user name is in player's OperaCraftPlayer list and if not adds them to the list
		    			{
		    				if (Minecraft.getMinecraft().theWorld.getPlayerEntityByName(username) != null)
		    				{
		    					Vars.addNamesToDatabase(username);
		    				}
		    			} //KINECTODO this is a way to check who needs to have their position/mouth/body state updated
		    			if (Vars.doesOperaCraftPlayerExist(username) && Vars.playerToRoles.containsKey(username) && Vars.playerToRoles.get(username).equals("actor"))						
		    			{			
		    				
			    			int armState = Integer.parseInt(message[3]);						//retrieves the value or position of the arm
			    			double time = Double.parseDouble(message[4]); /// TODO: how to figure time out ?
			    			if (Vars.doesOperaCraftPlayerExist(username))						//checks if the user name is in player's OperaCraftPlayer list
			    			{
			    				Vars.changeOperaCraftArmState(username, armState, time);				//changes the player's arm to the specified value
			    			}
		    				
		    			}
	    			}
	    		}*/
	    		else if (message[1].equals("@arm"))
	    		{	    			
	    			displayWords = false;
	    			if (message.length == 4)
	    				{		
	    				String username = message[0].substring(1, message[0].length() - 1);	//retrieves the user name from the first position in the message array
		    			int armState = Integer.parseInt(message[2]);						//retrieves the value or position of the arm
		    			double time = Double.parseDouble(message[3]);
		    			if (Vars.doesOperaCraftPlayerExist(username))						//checks if the user name is in player's OperaCraftPlayer list
		    			{
		    				Vars.changeOperaCraftArmState(username, armState, time);				//changes the player's arm to the specified value
		    			}
	    			}
	    			else if (message.length == 5) 	// this happens when a client connects to the server and server issues arm position changes
	    											// and therefore needs to send username rather than use projector's username
	    			{
	    				String username = message[2];
		    			int armState = Integer.parseInt(message[3]);						//retrieves the value or position of the arm
		    			double time = Double.parseDouble(message[4]);
		    			if (Vars.doesOperaCraftPlayerExist(username))						//checks if the user name is in player's OperaCraftPlayer list
		    			{
		    				Vars.changeOperaCraftArmState(username, armState, time);				//changes the player's arm to the specified value
		    			}
	    			}
	    		}
	    		else if (message[1].equals("@karm"))
	    		{
	    			//FORMAT: <USERNAME> @karm Target_UserName L_arm_angle L_arm_vec_X L_arm_vec_Z R_arm_angle R_arm_vec_X R_arm_vec_Z
	    			double RAng, RVecX, RVecZ, LAng, LVecX, LVecZ;
	    			displayWords = false;
	    			if (message.length == 9)
	    			{
	    				String username = message[2];	//retrieves the user name from the third position in the message array (first one is the server sending the message)
	    				if (Vars.kinectMirror == 1)
	    				{
		    				RAng = Double.parseDouble(message[3]);
		    				RVecX = Double.parseDouble(message[4]);
		    				RVecZ = Double.parseDouble(message[5]);
		    				LAng = Double.parseDouble(message[6]);
		    				LVecX = Double.parseDouble(message[7]);
		    				LVecZ = Double.parseDouble(message[8]);	    					
	    				}
	    				else
	    				{
		    				LAng = Double.parseDouble(message[3]);
		    				LVecX = Double.parseDouble(message[4]);
		    				LVecZ = -Double.parseDouble(message[5]);
		    				RAng = Double.parseDouble(message[6]);
		    				RVecX = Double.parseDouble(message[7]);
		    				RVecZ = -Double.parseDouble(message[8]);	    					
	    				}

		    			if (Vars.doesOperaCraftPlayerExist(username)) //checks if the user name is in player's OperaCraftPlayer list
		    			{
		    				Vars.changeKinectArmAngles(username, LAng, LVecX, LVecZ, RAng, RVecX, RVecZ); //changes the player's kinect arm angles to the specified value
		    			}
	    			}
	    		}
	    		else if(message[1].equals("@kshoulder"))
	    		{
	    			double  LY = 0, LZ = 0, RY = 0, RZ = 0;
	    			displayWords = false;
	    			if (message.length == 7){
	    				String username = message[2];
	    				
	    				LY = Double.parseDouble(message[3]);
	    				LZ = Double.parseDouble(message[4]);
	    				
	    				RY = Double.parseDouble(message[5]);
	    				RZ = Double.parseDouble(message[6]);
	    				
	    				if (Vars.doesOperaCraftPlayerExist(username)){
	    					Vars.changeKinectShoulderAngles(username, LY, LZ, RY, RZ);
	    				}
	    			}
	    			
	    			
	    			
	    			
	    		}
	    		else if (message[1].equals("@ktorso"))
	    		{
	    			//FORMAT: <USERNAME> @ktorso Target_UserName X Y Z TorsoRotation
	    			double X, Y, Z, TorRot;
	    			displayWords = false;
	    			if (message.length == 7)
	    			{
	    				String username = message[2];	//retrieves the user name from the third position in the message array (first one is the server sending the message)
	    			
		    			X = Double.parseDouble(message[3]);
		    			Y = Double.parseDouble(message[4]);
		    			Z = Double.parseDouble(message[5]);
		    			TorRot = Double.parseDouble(message[6]);
		    			
		    			if (Vars.doesOperaCraftPlayerExist(username)) //checks if the user name is in player's OperaCraftPlayer list
		    			{
		    				Vars.changeKinectTorsoPos(username, X, Y, Z, TorRot); //changes the player's kinect torso position and yaw to the specified value
		    			}
	    			}
	    		}
	    		else if (message[1].equals("@kleg"))
	    		{
	    			//FORMAT: <USERNAME> @kleg Target_UserName L_leg_angle L_leg_vec_X L_leg_vec_Z R_leg_angle R_leg_vec_X R_leg_vec_Z
	    			double RAng=0, RVecX=0, RVecZ=0, LAng=0, LVecX=0, LVecZ=0;
	    			displayWords = false;
	    			if (message.length == 9)
	    			{
	    				String username = message[2];
	    				
	    				if (Vars.kinectPosition >= 1)
	    				{
		    				if (Vars.kinectMirror == 1)
		    				{
			    				RAng = Double.parseDouble(message[3]);
			    				RVecX = Double.parseDouble(message[4]);
			    				RVecZ = Double.parseDouble(message[5]);
			    				LAng = Double.parseDouble(message[6]);
			    				LVecX = Double.parseDouble(message[7]);
			    				LVecZ = Double.parseDouble(message[8]);
		    				}
		    				else
		    				{
			    				LAng = Double.parseDouble(message[3]);
			    				LVecX = Double.parseDouble(message[4]);
			    				LVecZ = -Double.parseDouble(message[5]);
			    				RAng = Double.parseDouble(message[6]);
			    				RVecX = Double.parseDouble(message[7]);
			    				RVecZ = -Double.parseDouble(message[8]);    					
		    				}
	    				}
		    				
		    			if (Vars.doesOperaCraftPlayerExist(username)) //checks if the user name is in player's OperaCraftPlayer list
		    			{
		    				Vars.changeKinectLegAngles(username, LAng, LVecX, LVecZ, RAng, RVecX, RVecZ); //changes the player's kinect leg angles to the specified value
		    			}	    				
	    			}
	    		}
	    		else if (message[1].equals("@khead"))
	    		{
	    			//FORMAT: <USERNAME> @khead Target_Username Pitch Yaw Roll
	    			double P, Y, R;
	    			displayWords = false;
	    			if (message.length == 6)
	    			{
	    				String username = message[2];	//retrieves the user name from the third position in the message array (first one is the server sending the message)
	    			
		    			P = Double.parseDouble(message[3]);
		    			Y = Double.parseDouble(message[4]);
		    			R = Double.parseDouble(message[5]);
		    			
		    			if (Vars.doesOperaCraftPlayerExist(username)) //checks if the user name is in player's OperaCraftPlayer list
		    			{
		    				Vars.changeKinectHeadAngle(username, P, Y, R); //changes the player's kinect head angles to the specified value
		    			}
	    			}	    			
	    		}
	    		else if (message[1].equals("@fade") && (Vars.isSpectator || Vars.isProjector))
        		{
        			// we use this to reflect the fade values on all clients, so that
        			// the spectators can observe projector fades when following the projector
        			if (message.length == 3)
        			{
    	    			int fadeNum = Integer.parseInt(message[2]);
    	    			int i;
    	    			for (i = 0; i < 16; i++)
    	    			{
    	    				Vars.fadeList[i] = false;
    	    			}
    	    			for (i = fadeNum; i >= 0; i--)
    	    			{
    	    				Vars.fadeList[i] = true;
    	    			}
        			}
        			displayWords = false;
	    		}
		    	
		    	if (displayWords)						
		    	{      		
		    		if (isWarning && !Vars.isProjector)
		    		{
		    			this.printChatMessageWithOptionalDeletion(par1Str, 0);				//originally the only line in this method
		    		}
		    		else if (Vars.isProjector)
		    		{
		    			clearChatMessages();
		    			this.printChatMessageWithOptionalDeletion(par1Str, 0);
		    		}
		    		else if (Vars.isServer)
		    		{
		    			this.printChatMessageWithOptionalDeletion(par1Str, 0);
		    		}
		    	}
		        isWarning = false;
	    	}
	    	else
	    	{
	    		// message length is 1 or 2
	    		if (message.length == 2 && message[1].equals("@status"))
	    		{
	    			if (Vars.isServer)
	    			{
		    			// if we are a projector and one of our clients just requested a status (mouths/arms/fade/etc.)
		    	        /*
		    	         * DONE:	
		    	         * 	+mouths
		    	         * 	+arm positions
		    	         * 	+fade in/out (then also add this to spectators who can follow the camera) OK
		    	         * 
		    	         * OPERACRAFT-TODO: send other update packets to the new player to keep track of:
		    	         * 	+anything else?
		    	         */
		    	        //if (!Vars.players[1].getUsername().equals("notassigned")) // if we are the only one on the server don't do this
		    	        //{
		    		        for (int i = 0; i < Vars.players.length; i++)
		    		        {
		    		        	if (!Vars.players[i].getUsername().equals("notassigned"))
		    		        	{
		    		        		Minecraft.getMinecraft().thePlayer.sendChatMessage(String.format("@mouth %s %s", Vars.players[i].getUsername(), Vars.players[i].getMouthState()));
		    		        		Minecraft.getMinecraft().thePlayer.sendChatMessage(String.format("@arm %s %s 0", Vars.players[i].getUsername(), Vars.players[i].getArmState()));
		    		        	}
		    		        }
		    		        if (!Vars.players[1].getUsername().equals("notassigned")) //if the second player is not yet initialized, this means we're alone
		    		        	Minecraft.getMinecraft().thePlayer.sendChatMessage(String.format("@fade %d", Vars.getFadeNum()));
		    	        //}
	    			}
	    		}
	    		// this is a short message with fewer than two args and without username
    			// we exclude empty @warn messages since we have already processed both of these that are not empty above
	    		//System.out.println("Short message: " + par1Str);
	    		else
	    		{
	    			if (Vars.isProjector)
	    			{
	    				//System.out.println("PROJ OR SPECT");
	    				clearChatMessages();
	    				if (message.length > 1 && !message[1].equals("@warn"))
	    				{
	    					this.printChatMessageWithOptionalDeletion(par1Str, 0);
	    				}
	    			}
	    			else
	    			{
	    				this.printChatMessageWithOptionalDeletion(par1Str, 0);
	    			}
	    		}
	    	}
    	}
    }

    
    /**
     * prints the String to Chat. If the ID is not 0, deletes an existing Chat Line of that ID from the GUI
     */
    public void printChatMessageWithOptionalDeletion(String par1Str, int par2)
    {
    	// make internal chat invisible for all (was only for projector by adding if (Vars.isProjector...
    	if (!Vars.isServer && (par1Str.startsWith("<") || par1Str.startsWith("" + EnumChatFormatting.GRAY + "" + EnumChatFormatting.ITALIC + "[")))
    	{
    		return;
    	}
        this.func_96129_a(par1Str, par2, this.mc.ingameGUI.getUpdateCounter(), false);
        this.mc.getLogAgent().logInfo("[CHAT] " + par1Str);
    }

    private void func_96129_a(String par1Str, int par2, int par3, boolean par4)
    {
        boolean var5 = this.getChatOpen();
        boolean var6 = true;

        if (par2 != 0)
        {
            this.deleteChatLine(par2);
        }

        Iterator var7 = this.mc.fontRenderer.listFormattedStringToWidth(par1Str, MathHelper.floor_float((float)this.func_96126_f() / this.func_96131_h())).iterator();

        while (var7.hasNext())
        {
            String var8 = (String)var7.next();

            if (var5 && this.field_73768_d > 0)
            {
                this.field_73769_e = true;
                this.scroll(1);
            }

            if (!var6)
            {
                var8 = " " + var8;
            }

            var6 = false;
            this.field_96134_d.add(0, new ChatLine(par3, var8, par2));
        }

        while (this.field_96134_d.size() > 100)
        {
            this.field_96134_d.remove(this.field_96134_d.size() - 1);
        }

        if (!par4)
        {
            this.chatLines.add(0, new ChatLine(par3, par1Str.trim(), par2));

            while (this.chatLines.size() > 100)
            {
                this.chatLines.remove(this.chatLines.size() - 1);
            }
        }
    }

    public void func_96132_b()
    {
        this.field_96134_d.clear();
        this.resetScroll();

        for (int var1 = this.chatLines.size() - 1; var1 >= 0; --var1)
        {
            ChatLine var2 = (ChatLine)this.chatLines.get(var1);
            this.func_96129_a(var2.getChatLineString(), var2.getChatLineID(), var2.getUpdatedCounter(), true);
        }
    }

    /**
     * Gets the list of messages previously sent through the chat GUI
     */
    public List getSentMessages()
    {
        return this.sentMessages;
    }

    /**
     * Adds this string to the list of sent messages, for recall using the up/down arrow keys
     */
    public void addToSentMessages(String par1Str)
    {
        if (this.sentMessages.isEmpty() || !((String)this.sentMessages.get(this.sentMessages.size() - 1)).equals(par1Str))
        {
            this.sentMessages.add(par1Str);
        }
    }

    /**
     * Resets the chat scroll (executed when the GUI is closed)
     */
    public void resetScroll()
    {
        this.field_73768_d = 0;
        this.field_73769_e = false;
    }

    /**
     * Scrolls the chat by the given number of lines.
     */
    public void scroll(int par1)
    {
        this.field_73768_d += par1;
        int var2 = this.field_96134_d.size();

        if (this.field_73768_d > var2 - this.func_96127_i())
        {
            this.field_73768_d = var2 - this.func_96127_i();
        }

        if (this.field_73768_d <= 0)
        {
            this.field_73768_d = 0;
            this.field_73769_e = false;
        }
    }

    public ChatClickData func_73766_a(int par1, int par2)
    {
        if (!this.getChatOpen())
        {
            return null;
        }
        else
        {
            ScaledResolution var3 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
            int var4 = var3.getScaleFactor();
            float var5 = this.func_96131_h();
            int var6 = par1 / var4 - 3;
            int var7 = par2 / var4 - 25;
            var6 = MathHelper.floor_float((float)var6 / var5);
            var7 = MathHelper.floor_float((float)var7 / var5);

            if (var6 >= 0 && var7 >= 0)
            {
                int var8 = Math.min(this.func_96127_i(), this.field_96134_d.size());

                if (var6 <= MathHelper.floor_float((float)this.func_96126_f() / this.func_96131_h()) && var7 < this.mc.fontRenderer.FONT_HEIGHT * var8 + var8)
                {
                    int var9 = var7 / (this.mc.fontRenderer.FONT_HEIGHT + 1) + this.field_73768_d;
                    return new ChatClickData(this.mc.fontRenderer, (ChatLine)this.field_96134_d.get(var9), var6, var7 - (var9 - this.field_73768_d) * this.mc.fontRenderer.FONT_HEIGHT + var9);
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
    }

    /**
     * Adds a message to the chat after translating to the client's locale.
     */
    public void addTranslatedMessage(String par1Str, Object ... par2ArrayOfObj)
    {
        this.printChatMessage(StringTranslate.getInstance().translateKeyFormat(par1Str, par2ArrayOfObj));
    }

    /**
     * @return {@code true} if the chat GUI is open
     */
    public boolean getChatOpen()
    {
        return this.mc.currentScreen instanceof GuiChat;
    }

    /**
     * finds and deletes a Chat line by ID
     */
    public void deleteChatLine(int par1)
    {
        Iterator var2 = this.field_96134_d.iterator();
        ChatLine var3;

        do
        {
            if (!var2.hasNext())
            {
                var2 = this.chatLines.iterator();

                do
                {
                    if (!var2.hasNext())
                    {
                        return;
                    }

                    var3 = (ChatLine)var2.next();
                }
                while (var3.getChatLineID() != par1);

                var2.remove();
                return;
            }

            var3 = (ChatLine)var2.next();
        }
        while (var3.getChatLineID() != par1);

        var2.remove();
    }

    public int func_96126_f()
    {
        return func_96128_a(this.mc.gameSettings.chatWidth);
    }

    public int func_96133_g()
    {
        return func_96130_b(this.getChatOpen() ? this.mc.gameSettings.chatHeightFocused : this.mc.gameSettings.chatHeightUnfocused);
    }

    public float func_96131_h()
    {
        return this.mc.gameSettings.chatScale;
    }

    public static final int func_96128_a(float par0)
    {
        short var1 = 320;
        byte var2 = 40;
        return MathHelper.floor_float(par0 * (float)(var1 - var2) + (float)var2);
    }

    public static final int func_96130_b(float par0)
    {
        short var1 = 180;
        byte var2 = 20;
        return MathHelper.floor_float(par0 * (float)(var1 - var2) + (float)var2);
    }

    public int func_96127_i()
    {
        return this.func_96133_g() / 9;
    }
    
}
