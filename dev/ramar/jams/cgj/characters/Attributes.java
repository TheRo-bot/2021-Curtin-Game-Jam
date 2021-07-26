package dev.ramar.jams.cgj.characters;


public class Attributes
{
    public Object getAttr(String s)
    {

        return null;
    }

    public void changeAttr(String s, Object o)
    {

    }




    public double asDouble(Object o)
    {
        return (double)o;
    }

    public int asInt(Object o)
    {
        return (int)o;
    }

    public String asString(Object o)
    {
        if( o instanceof String )
            return (String)o;

        return null;
    }
}