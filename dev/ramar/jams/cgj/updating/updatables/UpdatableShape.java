package dev.ramar.jams.cgj.updating.updatables;

import dev.ramar.jams.cgj.updating.*;


import dev.ramar.e2.rendering.drawing.stateful.Shape;

import dev.ramar.e2.rendering.ViewPort;

import org.json.simple.*;
import org.json.simple.parser.*;

public abstract class UpdatableShape extends Shape implements FastUpdater
{


    public UpdatableShape()
    {

    }

    /* FastUpdater implementation
    -===----------------------------
    */

    protected boolean doUpdate = true;


    public void update(long ns)
    {
        
    }

    public boolean stopUpdating()
    {
        return !doUpdate;
    }

    public abstract void toJson(JSONObject job, ViewPort vp);


}