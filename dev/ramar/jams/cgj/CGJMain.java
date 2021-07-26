package dev.ramar.jams.cgj;



import dev.ramar.e2.rendering.awt.AWTViewPort;
import dev.ramar.e2.rendering.*;
import dev.ramar.e2.rendering.drawing.stateless.RectDrawer.RectMods;
import dev.ramar.e2.structures.WindowSettings;

import dev.ramar.e2.rendering.drawing.stateful.TextShape;

import dev.ramar.jams.cgj.guis.*;

import java.util.*;
import java.io.*;

import dev.ramar.e2.rendering.drawing.stateful.*;

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


        try
        {
            /* PRE-GAME CACHING */
            vp.draw.image.loadToCache(getClass(), "/resources/textures/walls/wall.png", "walls.generic.south");

        }
        catch(IOException e) 
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }



        /*
        Game starting!
        */

        GameGUI gg = new GameGUI();
        SyncPointMaker spm = new SyncPointMaker();

        vp.guis.requestGUI(spm);

        Tests tests = new Tests();

        tests.setup(vp, gg.fum);

        // tests.scaleTest(vp.window.width(), vp.window.height());

	}


    public void waitForClose()
    {
        vp.waitForClose();
    }
}