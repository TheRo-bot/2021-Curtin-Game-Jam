package dev.ramar.jams.cgj.guis;

import dev.ramar.jams.cgj.updating.updatables.*;
import dev.ramar.jams.cgj.updating.updatables.shapes.*;

import dev.ramar.e2.rendering.control.KeyController.KeyCombo;
import dev.ramar.e2.rendering.control.KeyController.KeyListener;

import dev.ramar.e2.rendering.drawing.stateful.*;
import dev.ramar.e2.rendering.drawing.stateless.RectDrawer.*;
import dev.ramar.e2.rendering.drawing.stateless.ImageDrawer.*;
import dev.ramar.e2.rendering.drawing.stateless.TextDrawer.*;
import dev.ramar.e2.rendering.GUI;
import dev.ramar.e2.rendering.Image;

import java.util.List;
import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;


import java.io.*;
import java.net.*;

import org.json.simple.*;
import org.json.simple.parser.*;

public class SyncPointMaker extends GameGUI
{

    private static final String JSON_PATH = "D:/Users/Robot/D - File Gateway/Local/Documents/Private/Coding/RAMAR/Engine R2/jar/syncMakerJSON.json";

    List<UpdatableShape> shapes = new ArrayList<>();

    List<UpdatableShape> ui = new ArrayList<>();


    public boolean requestAccess(GUI g)
    {
        return true && super.requestAccess(g);
    }


    public void prepSwapTo(GUI g)
    {
        super.prepSwapTo(g);
        for( UpdatableShape us : ui )
        {
            viewport.draw.stateful.shapes.remove(us);
        }
    }

    private boolean showUI = true;


    private void initialiseUI()
    {
        viewport.window.keys.bind(new KeyCombo("toggle ui").withChar('h'), 
            new KeyListener()
            {

                public void onPress(KeyCombo kc)
                {

                }

                public void onRelease(KeyCombo kc)
                {
                    toggleUI();
                }


                private void toggleUI()
                {
                    showUI = ! showUI;
                    System.out.println("UI toggle " + (showUI ? "on" : "off") + "!" );
                    if(! showUI )
                        for( UpdatableShape us : ui )
                            viewport.draw.stateful.shapes.remove(us);
                    else
                        for( UpdatableShape us : ui )
                            viewport.draw.stateful.shapes.add(us);
                }
            }
        );



        /*
        LOOKUP_ GRID
        -===---------
         here's the grid stuff chief :)
        */

        viewport.window.keys.bind(new KeyCombo("toggle grid").withChar('g'), 
            new KeyListener()
            {
                // xGap and yGap directly relate to the gap
                int xGap = viewport.window.width() / 10, yGap = viewport.window.height() / 10, thickness = 3;
                private boolean gridOn = false;

                List<Rect> gridrects = new ArrayList<>();

                public void onPress(KeyCombo kc)
                {   

                }

                public void onRelease(KeyCombo kc)
                {
                    gridOn = !gridOn;
                    toggleGrid(gridOn);
                }

                private void setupGrid()
                {
                    RectMods modTemplate = new RectMods()
                        .withColour(0, 0, 255, 255)
                        .withFill()
                        .withOffsetOnly(true)

                    ;

                    for( int ii = 0; ii < (int)(viewport.window.width() / xGap); ii++ )
                    {
                        Rect r = new Rect(xGap * ii, 0, thickness, viewport.window.height());
                        r.getMod().set(modTemplate);
                        gridrects.add(r);
                    }
                    for( int ii = 0; ii < (int)(viewport.window.height() / yGap); ii++ )
                    {
                        Rect r = new Rect(0, yGap * ii, viewport.window.width(), thickness);
                        r.getMod().set(modTemplate);
                        gridrects.add(r);
                    }   
                }

                private void toggleGrid(boolean state)
                {
                    System.out.println("Toggle Grid " + (state ? "on" : "off") + "!");

                    if( gridrects.isEmpty() )
                        setupGrid();

                    if( state )
                    {
                        for( Rect r : gridrects )
                            viewport.draw.stateful.shapes.add(r);
                    }
                    else
                    {
                        for( Rect r : gridrects )
                            viewport.draw.stateful.shapes.remove(r);
                    }
                }
            }
        );


        UpdatableRect saveRect = new UpdatableRect(0, 0, 150, 30);
        UpdatableText saveText = new UpdatableText("Save to JSON");


        saveRect.getMod()
            .withColour(255, 0, 0, 255).withFill()
            .withOffset(50, 50)
            .withOffsetOnly(true)
        ;

        double textX = saveRect.getMod().modX(saveRect.getRect().getW() / 2);
        double textY = saveRect.getMod().modY(saveRect.getRect().getH() / 2);

        saveText.getMod()
            .withColour(255, 255, 255, 255)
            .withOffset(textX, textY)
            .withAlignment(0, 0)
            .withOffsetOnly(true)
            .withSize(16)
        ;



        ui.add(saveRect);
        ui.add(saveText);


        UpdatableRect loadRect = new UpdatableRect(0, 0, 150, 30);
        UpdatableText loadText = new UpdatableText("Load to JSON");

        loadRect.getMod()
            .withColour(0, 255, 0, 255).withFill()
            .withOffsetOnly(true)
            .withOffset(50, 80);
        ;

        textX = loadRect.getMod().modX(loadRect.getRect().getW() / 2);
        textY = loadRect.getMod().modY(loadRect.getRect().getH() / 2);

        loadText.getMod()
            .withColour(255, 255, 255, 255)
            .withOffset(textX, textY)
            .withAlignment(0, 0)
            .withOffsetOnly(true)
            .withSize(16)
        ;

        ui.add(loadRect);
        ui.add(loadText);

        viewport.window.mouse.onRelease.add((int b, double x, double y) ->
        {   
            System.out.println("Click [" + x + ", " + y + "]");
            if( showUI )
            {

                Rect saveR = saveRect.getRect();
                if( x > saveR.getX() - viewport.window.width()  / 2 && x < saveR.getX() + saveR.getW() - viewport.window.width()  / 2
                &&  y > saveR.getY() - viewport.window.height() / 2 && y < saveR.getY() + saveR.getH() - viewport.window.height() / 2)
                    onClick("json save");

                Rect loadR = loadRect.getRect();
                if( x > loadR.getX() - viewport.window.width()  / 2  && x < loadR.getX() + loadR.getW() - viewport.window.width()  / 2 
                &&  y > loadR.getY() - viewport.window.height() / 2  && y < loadR.getY() + loadR.getH() - viewport.window.height() / 2 )
                    onClick("json load");

            }

        });

        Map<String, Image> copyCache = viewport.draw.image.getCacheCopy();
        int ii = 0;
        for( String s : copyCache.keySet() )
        {
            UpdatableText ut = new UpdatableText(s);
            ut.getMod()
                .withColour(255, 255, 255, 255)
                .withOffsetOnly(true)
                .withOffset(30, ii * 50)
                .withAlignment(1, 0)
            ;

            ui.add(ut);
            UpdatableRect ur = new UpdatableRect(0, 0, 40, 40);

        }


      /*  UpdatableRect testRect = new UpdatableRect(0, 0, 30, 30);
        testRect.getMod()
            .withColour(255, 255, 255, 100)
            .withFill()
        ;
        shapes.add(testRect);*/

        Image i = viewport.draw.image.get("walls.generic.south");
        if( i == null )
            throw new NullPointerException("walls.generic.south (image cache) should be loaded");
        UpdatableImage testImage = new UpdatableImage(new ImageShape(i));
        testImage.getMod()

            .withScale(0.25, 0.25)
        ;

        UpdatableImage im2 = new UpdatableImage(new ImageShape(i));
        im2.getMod()
            .withOffset(400, 0)
            .withScale(0.25, 0.25)
        ;
        
        shapes.add(testImage);
        shapes.add(im2);

    }


    public void onClick(String name)
    {
        System.out.println("on click " + name);

        switch(name)
        {
            case "json save":
                System.out.println("saving");
                saveShapesToJSONFile(JSON_PATH);
                break;

            case "json load":
                System.out.println("loading");
                loadShapesFromJSONFile(JSON_PATH);
                break;
        }
    }   


    public void startDrawing()
    {
        super.startDrawing();

        if( ui.isEmpty() )
            initialiseUI();

        System.out.println("UI Initialised: " + shapes.size() + " shapes loaded.");
        for( UpdatableShape us : ui )
            viewport.draw.stateful.shapes.add(us);

        for( UpdatableShape us : shapes )
            viewport.draw.stateful.shapes.add(us);
    }


    public void saveShapesToJSONFile(String path)
    {
        if( path != null )
        {
            try
            {
                JSONObject obj = new JSONObject();

                for( UpdatableShape us : shapes )
                    us.toJson(obj, viewport);

                PrintWriter pw = new PrintWriter(path);
                String output = obj.toString();
                pw.print(output);
                pw.flush();
                System.out.println(output);
                pw.close();
            }
            catch(IOException e)
            {
                System.out.println("-- Exception! " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    public void loadShapesFromJSONFile(String path)
    {
        if( path != null )
        {
            String fileContents = "";
            try
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));

                char thisChar = (char)br.read();
                while((int)thisChar != -1 && (int)thisChar != 65535)
                {
                    fileContents += thisChar;

                    thisChar = (char)br.read();
                }
            }
            catch(IOException e) 
            {
                e.printStackTrace();
            }

            System.out.println("read file: '" + fileContents + "'");
            try
            {
                JSONParser parser = new JSONParser();
                JSONObject jobject = (JSONObject)parser.parse(fileContents);
                loadFromJSON(jobject);
            }
            catch(ParseException e)
            {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }

    }   


    public void loadFromJSON(JSONObject obj)
    {
        int count = 0;        
        // rects
        if( obj.get("rects") != null )
        {
            JSONArray arr = (JSONArray)obj.get("rects");
            if( arr != null )
            {
                for( Object o : arr )
                {
                    UpdatableRect ur = makeRectWithJSON((JSONObject)o);
                    if( ur != null )
                    {
                        shapes.add(ur);
                        viewport.draw.stateful.shapes.add(ur);
                        count++;
                    }
                    // UpdatableRect ur = new UpdatableRect()System.out.println(o);
                }
            }

            arr = (JSONArray)obj.get("images");
            if( arr != null )
            {
                for( Object o : arr )
                {
                    UpdatableImage ui = makeImageWithJSON((JSONObject)o);
                    if( ui != null )
                    {
                        shapes.add(ui);
                        viewport.draw.stateful.shapes.add(ui);
                        count++;
                    }
                }
            }

            arr = (JSONArray)obj.get("texts");
            if( arr != null )
            {
                for( Object o : arr )
                {
                    UpdatableText ut = makeTextWithJSON((JSONObject)o);
                    if( ut != null )
                    {
                        shapes.add(ut);
                        viewport.draw.stateful.shapes.add(ut);
                        count++;
                    }
                }
            }
        }
        System.out.println("loaded " + count + " shapes from JSON");
    }   


    private UpdatableRect makeRectWithJSON(JSONObject o)
    {
        Double x = 0.0, y = 0.0, w = 0.0, h = 0.0, xOff = 0.0, yOff = 0.0;
        int colR = 255, colG = 255, colB = 255, colA = 255;

        Boolean withFill = false, offsetOnly = false;

        Double jX = (Double)o.get("x");
        Double jY = (Double)o.get("y");
        Double jW = (Double)o.get("w");
        Double jH = (Double)o.get("h");

        boolean mandatoryArgsFailed = false;

        if( jX != null ) x = jX; else mandatoryArgsFailed = true;
        if( jY != null ) y = jY; else mandatoryArgsFailed = true;
        if( jW != null ) w = jW; else mandatoryArgsFailed = true;
        if( jH != null ) h = jH; else mandatoryArgsFailed = true;

        if( mandatoryArgsFailed )
            return null;

        JSONObject colours = (JSONObject)o.get("colour");
        if( colours != null )
        {
            Integer cR = (Integer)o.get("r");
            Integer cG = (Integer)o.get("g");
            Integer cB = (Integer)o.get("b");
            Integer cA = (Integer)o.get("a");

            if( cR != null ) colR = cR;
            if( cG != null ) colG = cG;
            if( cB != null ) colB = cB;
            if( cA != null ) colA = cA;
        }

        Double offX = (Double)o.get("offX");
        Double offY = (Double)o.get("offY");

        if( offX != null ) xOff = offX;
        if( offY != null ) yOff = offY;

        Boolean fill = (Boolean)o.get("withFill");
        Boolean offsets = (Boolean)o.get("offsetsOnly");

        if( fill != null ) withFill = fill;
        if( offsets != null ) offsetOnly = offsets;


        UpdatableRect ur = new UpdatableRect(jX, jY, jW, jH);
        ur.getMod()
            .withOffset(offX, offY)
            .withColour(colR, colG, colB, colA)
            .withOffsetOnly(offsetOnly)
        ;  

        if( withFill ) ur.getMod().withFill(); else ur.getMod().withoutFill();

        return ur;
    }


    private UpdatableImage makeImageWithJSON(JSONObject o)
    {


        return null;
    }   


    private UpdatableText makeTextWithJSON(JSONObject o)
    {
        return null;
    }



}