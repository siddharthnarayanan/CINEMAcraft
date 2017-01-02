package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;
import java.io.*;

class ThreadDownloadImage extends Thread
{
    /** The URL of the image to download. */
    final String location;

    /** The image buffer to use. */
    final IImageBuffer buffer;

    /** The image data. */
    final ThreadDownloadImageData imageData;

    ThreadDownloadImage(ThreadDownloadImageData par1, String par2Str, IImageBuffer par3IImageBuffer)
    {
        this.imageData = par1;
        this.location = par2Str;
        this.buffer = par3IImageBuffer;
    }

    public void run()
    {
        HttpURLConnection var1 = null;
        System.out.println("ThreadDownloadImage");
        
        // moved from the Exception var 6 below as we want local skins no matter what
        try {
        	String[] breakUp = this.location.split("/");
        	System.out.println(this.location);
        	if (this.location.contains("Skins"))
        	{
            	System.out.println("Trying to load skin file " + Vars.skinsPath + "/" + breakUp[breakUp.length - 1]);
            	//Vars.debug += "Trying to load skin file " + Vars.skinsPath + "/" + breakUp[breakUp.length - 1] + "\n";
            	this.imageData.image = ImageIO.read(new File(Vars.skinsPath + "/" + breakUp[breakUp.length - 1]));
            	return;        		
        	}
        	else if (this.location.contains("Cloaks"))
        	{
            	System.out.println("Trying to load cloak file " + Vars.cloaksPath + "/" + breakUp[breakUp.length - 1]);
            	//Vars.debug += "Trying to load cloak file " + Vars.cloaksPath + "/" + breakUp[breakUp.length - 1] + "\n";
            	this.imageData.image = ImageIO.read(new File(Vars.cloaksPath + "/" + breakUp[breakUp.length - 1]));
            	return;        		
        	}

        }
        catch(IOException ex)
        {
        	System.out.println("No local file found...");
        	//Vars.debug += "No local file found...\n";
        }
        /*
        try
        {
        	System.out.println("Trying texture online");
            URL var2 = new URL(this.location);
            var1 = (HttpURLConnection)var2.openConnection();
            var1.setDoInput(true);
            var1.setDoOutput(false);
            var1.connect();

            if (var1.getResponseCode() / 100 == 4)
            {
                return;
            }

            if (this.buffer == null)
            {
                this.imageData.image = ImageIO.read(var1.getInputStream());
            }
            else
            {
                this.imageData.image = this.buffer.parseUserSkin(ImageIO.read(var1.getInputStream()));
            }
        }
        catch (Exception var6)
        {
            var6.printStackTrace();
        }
        finally
        {
            var1.disconnect();
        }
        */
    }
}
