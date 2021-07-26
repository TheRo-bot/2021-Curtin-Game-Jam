package dev.ramar.jams.cgj.guis;

import dev.ramar.e2.rendering.GUI;
import dev.ramar.e2.rendering.Image;
import dev.ramar.e2.rendering.TempDrawable;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.structures.SyncPoint;


import dev.ramar.jams.cgj.characters.pcs.Player;
import dev.ramar.jams.cgj.syncpoints.rooms.*;

import dev.ramar.jams.cgj.updating.FastUpdateManager;

import java.io.IOException;


public class GameGUI extends GUI
{
    private boolean continueDraw = false;

    private SyncPoint currPoint = null;

    protected Player player;

    public final FastUpdateManager fum;

    public GameGUI()
    {  
        fum = new FastUpdateManager();
        fum.start();
        player = new Player();
    }

    private void loadDefaultSyncPoint()
    {
        currPoint = new SP_Room1(this);
    }


    public boolean requestAccess(GUI g)
    {
        return true;
    }


    public void prepSwapTo(GUI g)
    {
        continueDraw = false;
        player.detachFromGUI(this);
    }


    public void startDrawing()
    {
        continueDraw = true;
        if( currPoint == null )
            loadDefaultSyncPoint();

        currPoint.setup();
        currPoint.load();

        player.attachToGUI(this);

    
    }

}