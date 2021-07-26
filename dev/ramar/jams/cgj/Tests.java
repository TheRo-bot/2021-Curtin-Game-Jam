package dev.ramar.jams.cgj;

import dev.ramar.e2.rendering.ViewPort;
import dev.ramar.e2.rendering.drawing.stateful.ImageShape;
import dev.ramar.e2.rendering.control.KeyController.KeyListener;
import dev.ramar.e2.rendering.control.KeyController.KeyCombo;

import dev.ramar.jams.cgj.updating.updatables.shapes.*;
import dev.ramar.jams.cgj.updating.*;

import java.io.IOException;

import dev.ramar.e2.rendering.Image;

public class Tests
{


    public Tests()
    {

    }

    ViewPort vp = null;
    FastUpdateManager fum = null;

    public void setup(ViewPort vp, FastUpdateManager fum)
    {
        this.vp = vp;
        this.fum = fum;
    }

    private double xSc = 1, ySc = 1;

    public void updatablesTest()
    {
        try
        {
            vp.draw.image.loadToCache(getClass(), "/resources/textures/walls/wall.png", "wall");
            ImageShape is = new ImageShape(vp.draw.image.get("wall"));
            is.getMod().withAlignment(1, 1);
            new Thread(() ->
            {
                try
                {
                    while(true)
                    {
                        is.getMod().withScale(xSc, ySc);
                        xSc -= 0.001;
                        ySc -= 0.001;
                        Thread.sleep(10);
                    }
                }
                catch(InterruptedException e) {}
            }).start();
            UpdatableImage im = new UpdatableImage(is);
            vp.draw.stateful.shapes.add(im);
        }
        catch(IOException e) 
        {
            System.out.println("updatablesTest IOE: " + e.getMessage());
        }

        vp.window.keys.bind(new KeyCombo("bruh").withChar('h'), new KeyListener()
        {
            public void onPress(KeyCombo kc)
            {

            }

            public void onRelease(KeyCombo kc)
            {
                if( kc.getName().equals("bruh"))
                    System.out.println(xSc + ", " + ySc);
            }
        });
    }




    public void scaleTest(int fakeW, int fakeH)
    {
        final int moveAm = 10;
        final int waitTime = 10;
        Runnable width_up = () ->
        {
            try
            {
                while(true)
                {
                    vp.setLogicalWidth(vp.getLogicalWidth() + moveAm);
                    Thread.sleep(waitTime);
                }
            }
            catch(InterruptedException e) {}
        };

        Runnable width_down = () ->
        {
            try
            {
                while(true)
                {
                    vp.setLogicalWidth(vp.getLogicalWidth() - moveAm);
                    Thread.sleep(waitTime);
                }
            }
            catch(InterruptedException e) {}
        };

        Runnable height_up = () ->
        {
            try
            {
                while(true)
                {
                    vp.setLogicalHeight(vp.getLogicalHeight() + moveAm);
                    Thread.sleep(waitTime);
                }
            }
            catch(InterruptedException e) {}

        };

        Runnable height_down = () ->
        {
            try
            {
                while(true)
                {
                    vp.setLogicalHeight(vp.getLogicalHeight() - moveAm);
                    Thread.sleep(waitTime);
                }
            }
            catch(InterruptedException e) {}
        };



        KeyListener kl = new KeyListener()
        {
            private Thread wUp, wDown, hUp, hDown;

            public void onPress(KeyCombo kc)
            {
                switch(kc.getName())
                {
                    case "width_up":
                        wUp = new Thread(width_up);
                        wUp.start();
                        break;
                    case "width_down":
                        wDown = new Thread(width_down);
                        wDown.start();
                        break;
                    case "height_up":
                        hUp = new Thread(height_up);
                        hUp.start();
                        break;
                    case "height_down":
                        hDown = new Thread(height_down);
                        hDown.start();
                        break;


                }
            }

            public void onRelease(KeyCombo kc)
            {
                switch(kc.getName())
                {
                    case "width_up":
                        wUp.interrupt();
                        break;
                    case "width_down":
                        wDown.interrupt();
                        break;
                    case "height_up":
                        hUp.interrupt();
                        break;
                    case "height_down":
                        hDown.interrupt();
                        break;
                }
                System.out.println("logical size: (" + vp.getLogicalWidth() + ", " + vp.getLogicalHeight() + ")");
            }
        };

        vp.window.keys.bind(new KeyCombo("height_up").withChar('i'), kl);
        vp.window.keys.bind(new KeyCombo("height_down").withChar('k'), kl);
        vp.window.keys.bind(new KeyCombo("width_up").withChar('l'), kl);
        vp.window.keys.bind(new KeyCombo("width_down").withChar('j'), kl);


        vp.setLogicalWidth(fakeW);
        vp.setLogicalHeight(fakeH);
    }

    public void imageTest()
    {
        try
        {
            vp.draw.image.loadToCache(getClass(), "/resources/test.png", "test");
        }
        catch(IOException e) {}
        Image i = vp.draw.image.get("test");
        ImageShape is = new ImageShape(i);

        is.getMod().withAlignment(1, 1);
        vp.draw.stateful.shapes.add(is);

    }
}