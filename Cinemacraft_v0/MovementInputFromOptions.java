package net.minecraft.src;

public class MovementInputFromOptions extends MovementInput
{
    private GameSettings gameSettings;

    public MovementInputFromOptions(GameSettings par1GameSettings)
    {
        this.gameSettings = par1GameSettings;
    }

    public boolean updatePlayerMoveState()
    {
        this.moveStrafe = 0.0F;
        this.moveForward = 0.0F;
        
        boolean ret = false;

        if (this.gameSettings.keyBindForward.pressed)
        {
            ++this.moveForward;
            ret = true;
        }

        if (this.gameSettings.keyBindBack.pressed)
        {
            --this.moveForward;
            ret = true;
        }

        if (this.gameSettings.keyBindLeft.pressed)
        {
            ++this.moveStrafe;
            ret = true;
        }

        if (this.gameSettings.keyBindRight.pressed)
        {
            --this.moveStrafe;
            ret = true;
        }

        this.jump = this.gameSettings.keyBindJump.pressed;
        this.sneak = this.gameSettings.keyBindSneak.pressed;

        if (this.sneak)
        {
            this.moveStrafe = (float)((double)this.moveStrafe * 0.3D);
            this.moveForward = (float)((double)this.moveForward * 0.3D);
        }
        return ret;
    }
}
