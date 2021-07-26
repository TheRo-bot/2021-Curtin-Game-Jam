package dev.ramar.jams.cgj.characters.pcs;

import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.rendering.control.KeyController.KeyListener;
import dev.ramar.e2.rendering.control.KeyController.KeyCombo;

import dev.ramar.jams.cgj.guis.GameGUI;
import dev.ramar.jams.cgj.updating.FastUpdater;

import dev.ramar.jams.cgj.characters.Attributes;

import dev.ramar.e2.rendering.ViewPort;
import dev.ramar.e2.rendering.drawing.stateful.Rect;
import dev.ramar.e2.rendering.GUI;

import java.util.Set;
import java.util.HashSet;

public class Player implements FastUpdater
{
    private static final Constants constants = new Constants();
    public static class Constants
    {
        public Constants() {}

        public static final String WALK_UP    = "player-walk-up";
        public static final String WALK_DOWN  = "player-walk-down";
        public static final String WALK_LEFT  = "player-walk-left";
        public static final String WALK_RIGHT = "player-walk-right";

    }


    private Vec2 pos;
    private Vec2 vel;

    private Attributes attr = new Attributes()
    {
        private double maxSpeed    = 1000.0,
                       playerSpeed = 500.0,
                       drag = 20.0;


        @Override
        public Object getAttr(String s)
        {
            switch(s)
            {
                case "max speed":
                    return maxSpeed;
                case "player speed":
                    return playerSpeed;
                case "drag":
                    return drag;
            }
            super.getAttr(s);

            return null;
        }


        @Override
        public void changeAttr(String s, Object o)
        {
            switch(s)
            {
                case "max speed":
                    maxSpeed = asDouble(o);                    
                    break;
                case "player speed":
                    playerSpeed = asDouble(o);
                    break;
                case "drag":
                    drag = asDouble(o);
                    break;

                default:
                    super.changeAttr(s, o);
            }
        }

        public double asDouble(Object o)
        {
            return (double)o;
        }
    };

    private Set<String> actions = new HashSet<>();

    private Rect testRect = new Rect(-5, -5, 10, 10);
        

    private KeyListener movementListener = new KeyListener()
    {
        public void onPress(KeyCombo kc)
        {
            synchronized(actions)
            {
                actions.add(kc.getName());
                // System.out.println(actions);
            }
        }

        public void onRelease(KeyCombo kc)
        {
            synchronized(actions)
            {
                actions.remove(kc.getName());
                // System.out.println(actions);
            }
        }
    };


    public Player()
    {
        pos = new Vec2(0, 0);
        vel = new Vec2(0, 0);
        
        testRect.getMod()
            .withColour(150, 150, 150, 255)
            .withFill()
        ;
    }


    public Player withPos(double x, double y)
    {
        pos.set(x, y);
        return this;
    }


    public Player withVel(double xv, double yv)
    {
        vel.set(xv, yv);
        return this;
    }

    private ViewPort viewport = null;

    /*
    Method: attachToViewPort
     - attaches everything that needs to be attached about this object
       to a viewport
    */
    public void attachToGUI(GUI g)
    {
        ViewPort vp = g.getViewPort();
        if( vp != null )
        {
            viewport = vp;
            vp.window.keys.bind(new KeyCombo(Constants.WALK_UP)
                .withChar('w'), movementListener);
            vp.window.keys.bind(new KeyCombo(Constants.WALK_DOWN)
                .withChar('s'), movementListener);
            vp.window.keys.bind(new KeyCombo(Constants.WALK_LEFT)
                .withChar('a'), movementListener);
            vp.window.keys.bind(new KeyCombo(Constants.WALK_RIGHT)
                .withChar('d'), movementListener);
            vp.draw.stateful.shapes.add(testRect);
        }

        if( g instanceof GameGUI)
        {
            GameGUI gg = (GameGUI)g;
            gg.fum.toUpdate.add(this);
        }
    }

    /*
    Method: detachFromViewPort
     - detaches everything that needs to be detached about this object
       from a viewport. should revert attachToViewPort()
    */
    public void detachFromGUI(GUI g)
    {
        viewport = null;
        ViewPort vp = g.getViewPort();
        if( vp != null )
        {
            vp.window.keys.unbind(new KeyCombo(Constants.WALK_UP)
                .withChar('w'), movementListener);
            vp.window.keys.unbind(new KeyCombo(Constants.WALK_DOWN)
                .withChar('s'), movementListener);
            vp.window.keys.unbind(new KeyCombo(Constants.WALK_LEFT)
                .withChar('a'), movementListener);
            vp.window.keys.unbind(new KeyCombo(Constants.WALK_RIGHT)
                .withChar('d'), movementListener);

            vp.draw.stateful.shapes.remove(testRect);
        }

        if( g instanceof GameGUI)
        {
            GameGUI gg = (GameGUI)g;
            gg.fum.toUpdate.remove(this);
        }
    }

    /*
    Method: doMovement
     - Calculates how much to move dependent on how much time (delta)
       has passed
     - delta is a double where 1.0 represents 1 second, vel, maxSpeed,
       drag, etc. all define how much movement in one second. This means
       that we can use delta like a percentage; doMovement(0.5) == half of
       a second of movement, means 50% of all vel/maxSpeed/etc.
    */
    protected void doMovement(double delta)
    {
        double maxX = 1000,
               maxY = 1000;

        double drag = attr.asDouble(attr.getAttr("drag"));
        double maxSpeed = attr.asDouble(attr.getAttr("player speed"));

        // ensure we're not going faster than maxSpeed

        double xMove = vel.getX() < 0 ? Math.max(vel.getX(), -maxX) :
                                        Math.min(vel.getX(),  maxX);         

        double yMove = vel.getY() < 0 ? Math.max(vel.getY(), -maxY) :
                                        Math.min(vel.getY(),  maxY);

        // The distance the player is moving is defined by the hypotenuse of the
        // two lengths vel.x and vel.y. modify xMove and yMove

        double hyp = Math.sqrt(xMove * xMove + yMove * yMove);

        if( xMove != 0 )
        {
            double xTheta = Math.cos(xMove / hyp);
            double newCalc = Math.cos(xTheta) * xMove;

            xMove = newCalc;
        }

        if( yMove != 0 )
        {
            double yTheta = Math.cos(yMove / hyp);
            double newCalc = Math.cos(yTheta) * yMove;

            yMove = newCalc;
        }


        // modify xMove / yMove by how long it's been since the last update

        double deltaXMove = xMove * delta,
               deltaYMove = yMove * delta;

        // calculate how drag will effect us

        deltaXMove *= drag / (1 + delta);
        deltaYMove *= drag / (1 + delta);

        // remove the distance we're travelling from vel and apply it to pos

        // consoleOutput("Moving (" + deltaXMove + ", " + deltaYMove + ") from " + vel);

        // lower our velocity by how much we're moving

        vel.set(vel.getX() - deltaXMove,
                vel.getY() - deltaYMove);

        vel.set(vel.getX() / (1 + delta),
                vel.getY() / (1 + delta));

        // increase our position by how much we're moving
        pos.set(pos.getX() + deltaXMove,
                pos.getY() + deltaYMove);
    }


    private void moveUp(double speed, double delta)
    {
        double maxSpeed = attr.asDouble(attr.getAttr("max speed"));

        vel.setY(Math.max(vel.getY() + -speed * delta,
                          vel.getY() + -maxSpeed));
    }

    private void moveDown(double speed, double delta)
    {
        double maxSpeed = attr.asDouble(attr.getAttr("max speed"));

        vel.setY(Math.min(vel.getY() + speed * delta,
                          vel.getY() + maxSpeed * delta));
    }

    private void moveRight(double speed, double delta)
    {
        double maxSpeed = attr.asDouble(attr.getAttr("max speed"));

        vel.setX(Math.min(vel.getX() + speed * delta,
                          vel.getX() + maxSpeed * delta));
    }


    private void moveLeft(double speed, double delta)
    {
        double maxSpeed = attr.asDouble(attr.getAttr("max speed"));

         vel.setX(Math.max(vel.getX() + -speed * delta,
                           vel.getX() + -maxSpeed * delta));

    }

    /* FastUpdater implementation
    -==-----------------------------
    */

    public void update(long ns)
    {
        double delta = ns * 0.00000001;
        synchronized(actions)
        {
            for( String s : actions )
            {
                switch(s)
                {
                    case Constants.WALK_UP:
                        moveUp(attr.asDouble(attr.getAttr("player speed")), delta);
                        break;

                    case Constants.WALK_DOWN:
                        moveDown(attr.asDouble(attr.getAttr("player speed")), delta);
                        break;

                    case Constants.WALK_RIGHT:
                        moveRight(attr.asDouble(attr.getAttr("player speed")), delta);
                        break;

                    case Constants.WALK_LEFT:
                        moveLeft(attr.asDouble(attr.getAttr("player speed")), delta);
                        break;

                }
            }
        }


        doMovement(delta);
        testRect.setX((int)pos.getX());
        testRect.setY((int)pos.getY());


        if( viewport != null )
        {
            viewport.setCenterX((int)-pos.getX());
            viewport.setCenterY((int)-pos.getY());
        }
    }


    public boolean stopUpdating()
    {
        return false;
    }

}