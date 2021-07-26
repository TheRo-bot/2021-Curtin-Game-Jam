package dev.ramar.jams.cgj.updating.updatables.shapes;

import dev.ramar.jams.cgj.updating.updatables.UpdatableShape;


import dev.ramar.e2.rendering.drawing.stateful.*;
import dev.ramar.e2.rendering.drawing.stateless.RectDrawer.RectMods;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.structures.Vec2;

import org.json.simple.*;
import org.json.simple.parser.*;
public class UpdatableRect extends UpdatableShape
{
    private Rect rect = new Rect(0, 0, 0, 0);

    public UpdatableRect()
    {
    }

    public UpdatableRect(double x, double y)
    {
        rect.setX(x);
        rect.setY(y);
    }

    public UpdatableRect(double x, double y, double w, double h)
    {
        rect.setX(x);
        rect.setY(y);
        rect.setW(w);
        rect.setH(h);
    }

    public Rect getRect()
    {
        return rect;
    }

    public RectMods getMod()
    {
        return rect.getMod();
    }



    /* FastUpdater implementation
    -===----------------------------
    */

    public void update(long ns)
    {

    }



    public void toJson(JSONObject obj, ViewPort vp)
    {
        System.out.println("toJson Rect");

        if( obj.get("rects") == null )
            obj.put("rects", new JSONArray());

        JSONArray innerObj = (JSONArray)obj.get("rects");

        JSONObject thisObj = new JSONObject();

        thisObj.put("x", rect.getX());
        thisObj.put("y", rect.getY());
        thisObj.put("w", rect.getW());
        thisObj.put("h", rect.getH());
        thisObj.put("offX", getMod().modX(0));
        thisObj.put("offY", getMod().modY(0));

        thisObj.put("colour", new JSONObject());
        JSONObject colourArr = (JSONObject)thisObj.get("colour");
        colourArr.put("r", getMod().getR());
        colourArr.put("g", getMod().getG());
        colourArr.put("b", getMod().getB());
        colourArr.put("a", getMod().getA());

        thisObj.put("withFill", getMod().doFill());
        thisObj.put("offsetOnly", !getMod().isOffsetAllowed());


        innerObj.add(thisObj);

    }

    public void drawAt(double x, double y, ViewPort vp)
    {
        rect.drawAt(x, y, vp);
    }

}   