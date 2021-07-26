package dev.ramar.jams.cgj.syncpoints.rooms;

import dev.ramar.e2.structures.SyncPoint;
import dev.ramar.e2.rendering.drawing.stateful.*;
import dev.ramar.e2.rendering.GUI;
import dev.ramar.e2.rendering.ViewPort;

import java.util.List;
import java.util.ArrayList;

import dev.ramar.e2.rendering.drawing.stateful.Shape;

public class SP_Room1 extends SyncPoint
{
    private GUI currGUI;

    private List<Shape> shapes = new ArrayList<>();
    private boolean setup = false;


    public SP_Room1(GUI g)
    {
        setGUI(g);
    }

    public void setGUI(GUI g)
    {
        currGUI = g;
    }



    public void setup()
    {
        if( !setup )
        {
/*            Rect r = new Rect(-15, -15, 30, 30);
            r.getMod()
                .withColour(255, 255, 255, 255)
                .withFill()
            ;
            shapes.add(r);
            */
            setup = true;
        }
    }


    public void load()
    {
        if( currGUI.getViewPort() != null )
        {
            ViewPort vp = currGUI.getViewPort();
            for( Shape s : shapes )
                vp.draw.stateful.shapes.add(s);
        }
        else
            System.out.println("Warning! No Viewport for attached gui!");
    }   

    public void unload()
    {
        if( currGUI.getViewPort() != null )
        {
            ViewPort vp = currGUI.getViewPort();
            for( Shape s : shapes )
                vp.draw.stateful.shapes.remove(s);
        }
    }


    public void unloadTo(SyncPoint sp)
    {
        unload();
    }


    public String getID()
    {
        return "CGJ_Room1";
    }

}