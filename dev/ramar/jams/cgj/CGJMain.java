package dev.ramar.jams.cgj;



import dev.ramar.e2.rendering.awt.AWTViewPort;
import dev.ramar.e2.rendering.*;
import dev.ramar.e2.rendering.drawing.stateless.RectDrawer.RectMods;
import dev.ramar.e2.structures.WindowSettings;



import java.util.*;


public class CGJMain
{


	public static void setup()
	{
		Main.Entrypoints.addEntrypoint((String[] args) ->
        {
        	CGJMain cgj = new CGJMain();
            cgj.waitForClose();
        	System.out.println("E2 close!");
        });
	}

    ViewPort vp;
    Random rd = new Random();

	public CGJMain()
	{
        vp = new AWTViewPort();
        vp.init(new WindowSettings(1280, 720, false, "Curtin Game Jam 2021 Ramarien"));
        vp.start();


        vp.guis.requestGUI(new GUI()
        {
            @Override
            public boolean requestAccess(GUI g)
            {
                return true;

            }

            @Override
            public void prepSwapTo(GUI g)
            {

            }


            public void initiateGUI(ViewPort vp)
            {
                super.initiateGUI(vp);
                viewport.draw.stateless.perm.add((double x, double y, ViewPort thisVP) ->
                {
                    thisVP.draw.stateless.rect.withMod().withColour(255, 255, 0, 255).withFill();
                    thisVP.draw.stateless.rect.poslen(30, 30, 20, 20);
                });
            }

        });

        GUI[] guis = new GUI[]
        {   
            new GUI()
            {
                @Override
                public boolean requestAccess(GUI g)
                {
                    return true;
                }

                @Override
                public void prepSwapTo(GUI g)
                {

                }


                public void initiateGUI(ViewPort vp)
                {
                    super.initiateGUI(vp);
                    viewport.draw.stateless.perm.add((double x, double y, ViewPort thisVP) ->
                    {
                        thisVP.draw.stateless.rect.withMod().withColour(255, 255, 0, 255).withFill();
                        thisVP.draw.stateless.rect.poslen(30, 30, 20, 20);
                    });
                }
            }, 
            new GUI()
            {
                @Override
                public boolean requestAccess(GUI g)
                {
                    return true;
                }

                @Override
                public void prepSwapTo(GUI g)
                {

                }


                public void initiateGUI(ViewPort vp)
                {
                    super.initiateGUI(vp);
                    viewport.draw.stateless.perm.add((double x, double y, ViewPort thisVP) ->
                    {
                        thisVP.draw.stateless.rect.withMod().withColour(255, 0, 255, 255).withFill();
                        thisVP.draw.stateless.rect.poslen(30, 30, 20, 20);
                    });
                }
            }
        };

        while(true)
        {
            for( int ii = 0; ii < guis.length; ii++ )
            {
                vp.guis.requestGUI(guis[ii]);
                try
                {
                    Thread.sleep(1500);
                }
                catch(InterruptedException e)
                {}
            }
        }

	}


    public void waitForClose()
    {
        vp.waitForClose();
    }
}