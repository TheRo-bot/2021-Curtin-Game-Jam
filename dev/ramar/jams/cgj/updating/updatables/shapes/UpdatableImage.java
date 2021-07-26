package dev.ramar.jams.cgj.updating.updatables.shapes;

import dev.ramar.jams.cgj.updating.updatables.UpdatableShape;


import dev.ramar.e2.rendering.drawing.stateful.ImageShape;
import dev.ramar.e2.rendering.drawing.stateless.ImageDrawer.ImageMods;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.structures.Vec2;

import org.json.simple.*;
import org.json.simple.parser.*;

public class UpdatableImage extends UpdatableShape
{
    private ImageShape is;

    private Vec2 pos = new Vec2(0, 0);

    public UpdatableImage()
    {
        super();
    }


    public UpdatableImage(ImageShape is)
    {
        this.is = is;
    }

    public UpdatableImage(double x, double y)
    {
        withPos(x,y);
    }


    public UpdatableImage withPos(double x, double y)
    {
        pos.set(x, y);
        return this;
    }


    public ImageMods getMod()
    {
        if( is != null )
            return is.getMod();
        return null;
    }


    public void drawAt(double x, double y, ViewPort vp)
    {
        is.drawAt(x + pos.getX(), y + pos.getY(), vp);
    }   


    public void toJson(JSONObject obj, ViewPort vp)
    {
        System.out.println("toJson Image");
        if( obj.get("images") == null )
            obj.put("images", new JSONArray());

        JSONArray imageList = (JSONArray)obj.get("images");

        JSONObject thisObj = new JSONObject();

        String imName = vp.draw.image.getNameOf(is.getImage());
        thisObj.put("image", imName == null ? "unknown" : imName);

        thisObj.put("offX", getMod().modX(0));
        thisObj.put("offY", getMod().modY(0));
        thisObj.put("offX", getMod().modX(0));
        

        imageList.add(thisObj);

    }



}   