package dev.ramar.jams.cgj.updating;


import java.util.List;
import java.util.ArrayList;

public class FastUpdateManager
{

    public FastUpdateManager()
    {

    }




    /* FastUpdateManager
    -===-------------------
    */
    

    public static class FastUpdaters
    {
        private List<SimpleFastUpdater> sfus = java.util.Collections.synchronizedList(
            new ArrayList<SimpleFastUpdater>());


        public FastUpdaters()
        {

        }

        public void add(SimpleFastUpdater sfu)
        {
            sfus.add(sfu);
            System.out.println("add! " + sfu + " " + sfus.size());
        }

        public void remove(SimpleFastUpdater sfu)
        {
            sfus.remove(sfu);
        }

    }


    /* FastUpdateManager inner thread
    -===--------------------------------
    */
    private Thread inner;

    public final FastUpdaters toUpdate = new FastUpdaters();

    public void start()
    {
        if( inner == null )
        {
            inner = new Thread(innerTask, "Fast Update Manager");
            inner.start();
        }
    }

    public void stop()
    {
        if( inner != null )
        {
            inner.interrupt();
            try
            {
                inner.join();
            }
            catch(InterruptedException e) {}
        }
    }

    public void interrupt()
    {
        if( inner != null )
            inner.interrupt();
    }


    private long lastPass = 0;

    private long getUpdateTime()
    {
        return System.nanoTime() - lastPass;
    }


    private Runnable innerTask = () ->
    {
        List<FastUpdater> toRemove = new ArrayList<>();

        while(true)
        {
            lastPass = System.nanoTime();

            for( SimpleFastUpdater sfu : toUpdate.sfus )
            {
                if( sfu instanceof FastUpdater )
                {
                    FastUpdater fu = (FastUpdater)sfu;
                    if( fu.stopUpdating() )
                        toRemove.add(fu);
                    else
                        fu.update(getUpdateTime());
                }
                else
                    sfu.update(getUpdateTime());
            }

            synchronized(toUpdate.sfus)
            {
                for( FastUpdater fu : toRemove )
                    toUpdate.sfus.remove(fu);
            }


        }
    };




}