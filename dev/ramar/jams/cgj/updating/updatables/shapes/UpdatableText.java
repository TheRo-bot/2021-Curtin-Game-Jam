package dev.ramar.jams.cgj.updating.updatables.shapes;

import dev.ramar.e2.rendering.ViewPort;
import dev.ramar.e2.rendering.drawing.stateful.TextShape;
import dev.ramar.e2.rendering.drawing.stateless.TextDrawer.TextMods;

import dev.ramar.jams.cgj.updating.updatables.*;

import org.json.simple.*;
import org.json.simple.parser.*;

public class UpdatableText extends UpdatableShape
{
    private TextShape text;

    public UpdatableText()
    {
        text = new TextShape();
    }

    public UpdatableText(String s)
    {
        text = new TextShape(s);
    }


    public TextShape getText()
    {
        return text;
    }

    public TextMods getMod()
    {
        return text.getMod();
    }



    /* FastUpdater implementation
    -===----------------------------
    */

    public void update(long ns)
    {

    }



    public void toJson(JSONObject obj, ViewPort vp)
    {
        System.out.println("toJson Text");

        if( obj.get("texts") == null )
            obj.put("texts", new JSONArray());

        JSONArray innerObj = (JSONArray)obj.get("texts");

        JSONObject thisObj = new JSONObject();

        innerObj.add(thisObj);

    }

    public void drawAt(double x, double y, ViewPort vp)
    {
        text.drawAt(x, y, vp);
    }
}