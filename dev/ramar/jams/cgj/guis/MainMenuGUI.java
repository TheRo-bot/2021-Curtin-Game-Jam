package dev.ramar.jams.cgj.guis;

import dev.ramar.e2.rendering.GUI;
import dev.ramar.e2.rendering.TempDrawable;
import dev.ramar.e2.rendering.ViewPort;


public class MainMenuGUI extends GUI
{
    private boolean continueDraw = false;

    public MainMenuGUI()
    {

    }


    public boolean requestAccess(GUI g)
    {
        return true;
    }


    public void prepSwapTo(GUI g)
    {
        continueDraw = false;
    }


    public void startDrawing()
    {
        System.out.println("MainMenuGUI start!");
        continueDraw = true;
        viewport.draw.stateless.temp.add(new TempDrawable()
        {
            public boolean continueDraw()
            {
                return continueDraw;
            }

            public void onDrawStop() {}

            int width = 100, height = 50;
            public void drawAt(double xOff, double yOff, ViewPort vp)
            {
                vp.draw.stateless.rect.withMod().withColour(255, 255, 255, 255).withFill();
                vp.draw.stateless.rect.poslen(vp.window.width()/2 - width/2,
                                              vp.window.height()/2 - height/2,
                                              width, height);
            }

        });
    }


}