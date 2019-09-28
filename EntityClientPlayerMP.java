package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class EntityClientPlayerMP extends EntityPlayerSP
{
    public NetClientHandler sendQueue;
    private double oldPosX;

    /** Old Minimum Y of the bounding box */
    private double oldMinY;
    private double oldPosY;
    private double oldPosZ;
    private float oldRotationYaw;
    private float oldRotationPitch;

    /** Check if was on ground last update */
    private boolean wasOnGround = false;

    /** should the player stop sneaking? */
    private boolean shouldStopSneaking = false;
    private boolean wasSneaking = false;
    private int field_71168_co = 0;

    /** has the client player's health been set? */
    private boolean hasSetHealth = false;

    public EntityClientPlayerMP(Minecraft par1Minecraft, World par2World, Session par3Session, NetClientHandler par4NetClientHandler)
    {
        super(par1Minecraft, par2World, par3Session, 0);
        this.sendQueue = par4NetClientHandler;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        return false;
    }

    /**
     * Heal living entity (param: amount of half-hearts)
     */
    public void heal(int par1) {}

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (this.worldObj.blockExists(MathHelper.floor_double(this.posX), 0, MathHelper.floor_double(this.posZ)))
        {
            super.onUpdate();
            this.sendMotionUpdates();
        }
    }

    /**
     * Send updated motion and position information to the server
     */
    public void sendMotionUpdates()
    {
        boolean var1 = this.isSprinting();

        if (var1 != this.wasSneaking)
        {
            if (var1)
            {
                this.sendQueue.addToSendQueue(new Packet19EntityAction(this, 4));
            }
            else
            {
                this.sendQueue.addToSendQueue(new Packet19EntityAction(this, 5));
            }

            this.wasSneaking = var1;
        }

        boolean var2 = this.isSneaking();

        if (var2 != this.shouldStopSneaking)
        {
            if (var2)
            {
                this.sendQueue.addToSendQueue(new Packet19EntityAction(this, 1));
            }
            else
            {
                this.sendQueue.addToSendQueue(new Packet19EntityAction(this, 2));
            }

            this.shouldStopSneaking = var2;
        }

        double var3 = this.posX - this.oldPosX;
        double var5 = this.boundingBox.minY - this.oldMinY;
        double var7 = this.posZ - this.oldPosZ;
        double var9 = (double)(this.rotationYaw - this.oldRotationYaw);
        double var11 = (double)(this.rotationPitch - this.oldRotationPitch);
        boolean var13 = var3 * var3 + var5 * var5 + var7 * var7 > 9.0E-4D || this.field_71168_co >= 20;
        boolean var14 = var9 != 0.0D || var11 != 0.0D;

        if (this.ridingEntity != null)
        {
            this.sendQueue.addToSendQueue(new Packet13PlayerLookMove(this.motionX, -999.0D, -999.0D, this.motionZ, this.rotationYaw, this.rotationPitch, this.onGround));
            var13 = false;
        }
        else if (var13 && var14)
        {
            this.sendQueue.addToSendQueue(new Packet13PlayerLookMove(this.posX, this.boundingBox.minY, this.posY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround));
        }
        else if (var13)
        {
            this.sendQueue.addToSendQueue(new Packet11PlayerPosition(this.posX, this.boundingBox.minY, this.posY, this.posZ, this.onGround));
        }
        else if (var14)
        {
            this.sendQueue.addToSendQueue(new Packet12PlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
        }
        else
        {
            this.sendQueue.addToSendQueue(new Packet10Flying(this.onGround));
        }

        ++this.field_71168_co;
        this.wasOnGround = this.onGround;

        if (var13)
        {
            this.oldPosX = this.posX;
            this.oldMinY = this.boundingBox.minY;
            this.oldPosY = this.posY;
            this.oldPosZ = this.posZ;
            this.field_71168_co = 0;
        }

        if (var14)
        {
            this.oldRotationYaw = this.rotationYaw;
            this.oldRotationPitch = this.rotationPitch;
        }
    }

    /**
     * Called when player presses the drop item key
     */
    public EntityItem dropOneItem(boolean par1)
    {
        int var2 = par1 ? 3 : 4;
        this.sendQueue.addToSendQueue(new Packet14BlockDig(var2, 0, 0, 0, 0));
        return null;
    }

    /**
     * Joins the passed in entity item with the world. Args: entityItem
     */
    protected void joinEntityItemWithWorld(EntityItem par1EntityItem) {}

    /**
     * Sends a chat message from the player. Args: chatMessage
     */
    public void sendChatMessage(String par1Str)
    {
    	String[] message = par1Str.split(" ");
    	if (message.length > 2)
    	{   
    		if (message[1].equals("@warn"))
    		{
    			//empty
    		}
    		else if (message[1].equals("@mouth"))
    		{
    			//System.out.println("got @mouth");
    			if (message.length == 4)  // why is this 4 ?
    			{	System.out.println("message length is :   " + message.length + ", and message[0] is " + message[0] +  "message[1] is " + message[1] + "message[2] is " + message[2] +  "message[3] is " + message[3] );
	    			String username = message[2];
	    			int mouthState = Integer.parseInt(message[3]);
	    			if (Vars.doesOperaCraftPlayerExist(username))						//checks if the user name is in player's OperaCraftPlayer list
	    			{
	    				Vars.changeOperaCraftMouthState(username, mouthState);			//changes the player's mouth to the specified value
	    				int mouthValue = Vars.getOperaCraftPlayerMouthState(username);    				
	    				ItemStack block = Vars.getHeadBlock(username, mouthValue);
	    				//EntityPlayerMP mouth_target = Minecraft.getMinecraft().getIntegratedServer().getConfigurationManager().getPlayerForUsername(Vars.getCameraByValue(Vars.cameraViewPortValue));
	    				if (Vars.isServer)
	    				{
	    					EntityPlayerMP mouth_target = Minecraft.getMinecraft().getIntegratedServer().getConfigurationManager().getPlayerForUsername(username);
	    					if (mouth_target != null)
	    					{
	    						//EntityPlayer is_visible = Minecraft.getMinecraft().theWorld.getPlayerEntityByName(username);
	    						mouth_target.setCurrentItemOrArmor(4, block);
	    						
	    						this.sendQueue.addToSendQueue(new Packet5PlayerInventory(mouth_target.entityId, 4, block));
	    						//mouth_target.playerNetServerHandler.sendPacketToPlayer(new Packet5PlayerInventory(Minecraft.getMinaft().theWorld.getPlayerEntityByName(username).entityId, 4, block));
	    						//mouth_target.playerNetServerHandler.sendPacketToPlayer(new Packet5PlayerInventory(mouth_target.entityId, 4, block));
	    					}
	    				}
	    				
	    				/*Minecraft.getMinecraft().theWorld.getPlayerEntityByName(username).setCurrentItemOrArmor(4, block);
	                   	EntityPlayer player = Minecraft.getMinecraft().theWorld.getPlayerEntityByName(username);
						if (player instanceof EntityPlayerMP)
						{
							((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(new Packet5PlayerInventory(Minecraft.getMinecraft().theWorld.getPlayerEntityByName(username).entityId, 4, block));
						}*/
					}
	    			else
	    			{
	    				if (Minecraft.getMinecraft().theWorld.getPlayerEntityByName(username) != null)
	    				{
	    					Vars.addNamesToDatabase(username);
	    				}
	    			}
    			}
    		}
    		else if (message[1].equals("@arm"))
    		{
    			//System.out.println("Getting arm message");
    			
    			if (message.length == 4)
    				{
    				//System.out.println("message length is :   " + message.length + ", and message[0] is " + message[0] +  "message[1] is " + message[1] + "message[2] is " + message[2] +  "message[3] is " + message[3] );																//keyword is @arm followed by a number
    				//System.out.println("This is where the stuff changes");
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
    	}
		this.sendQueue.addToSendQueue(new Packet3Chat(par1Str));   
    }

    /**
     * Swings the item the player is holding.
     */
    public void swingItem()
    {
        super.swingItem();
        this.sendQueue.addToSendQueue(new Packet18Animation(this, 1));
    }

    public void respawnPlayer()
    {
        this.sendQueue.addToSendQueue(new Packet205ClientCommand(1));
    }

    /**
     * Deals damage to the entity. If its a EntityPlayer then will take damage from the armor first and then health
     * second with the reduced value. Args: damageAmount
     */
    protected void damageEntity(DamageSource par1DamageSource, int par2)
    {
        if (!this.isEntityInvulnerable())
        {
            this.setEntityHealth(this.getHealth() - par2);
        }
    }

    /**
     * sets current screen to null (used on escape buttons of GUIs)
     */
    public void closeScreen()
    {
        this.sendQueue.addToSendQueue(new Packet101CloseWindow(this.openContainer.windowId));
        this.func_92015_f();
    }

    public void func_92015_f()
    {
        this.inventory.setItemStack((ItemStack)null);
        super.closeScreen();
    }

    /**
     * Updates health locally.
     */
    public void setHealth(int par1)
    {
        if (this.hasSetHealth)
        {
            super.setHealth(par1);
        }
        else
        {
            this.setEntityHealth(par1);
            this.hasSetHealth = true;
        }
    }

    /**
     * Adds a value to a statistic field.
     */
    public void addStat(StatBase par1StatBase, int par2)
    {
        if (par1StatBase != null)
        {
            if (par1StatBase.isIndependent)
            {
                super.addStat(par1StatBase, par2);
            }
        }
    }

    /**
     * Used by NetClientHandler.handleStatistic
     */
    public void incrementStat(StatBase par1StatBase, int par2)
    {
        if (par1StatBase != null)
        {
            if (!par1StatBase.isIndependent)
            {
                super.addStat(par1StatBase, par2);
            }
        }
    }

    /**
     * Sends the player's abilities to the server (if there is one).
     */
    public void sendPlayerAbilities()
    {
        this.sendQueue.addToSendQueue(new Packet202PlayerAbilities(this.capabilities));
    }

    public boolean func_71066_bF()
    {
        return true;
    }
}
