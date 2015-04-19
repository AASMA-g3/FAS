package pt.ulisboa.aasma.fas.j2d;


public class MainLoop implements Runnable
{
    public static final int DEFAULT_UPS = 80;
    public static final int DEFAULT_NO_DELAYS_PER_YIELD = 16;
    public static final int DEFAULT_MAX_FRAME_SKIPS = 5;

    private LoopSteps game;
    private long desiredUpdateTime;
    private boolean running;

    private long afterTime;
    private long beforeTime = System.currentTimeMillis();

    private long overSleepTime = 0;
    private long excessTime = 0;

    private int noDelaysPerYield = DEFAULT_NO_DELAYS_PER_YIELD;
    private int maxFrameSkips = DEFAULT_MAX_FRAME_SKIPS;

    int noDelays = 0;    

    /**
     * Create a new MainLoop object.
     * 
     * @param loopSteps The LoopSteps that will be controlled by this loop.
     * @param ups Number of desired updates per second.
     * @param maxFrameSkips Maximum number of frame that can be skipped if the
     *        graphical hardware is not fast enough to follow the desired ups.
     * @param noDelaysPerYield If the hardware is not fast enough to allow a
     *        dellay between two frames, the delay will be enforced in this
     *        counter, so other threads can process their actions.
     */
    public MainLoop(LoopSteps loopSteps,
            int ups,
            int maxFrameSkips,
            int noDelaysPerYield)
    {
        super();

        if (ups < 1)
            throw new IllegalArgumentException("You must display at least one frame per second!");

        if (ups > 1000)
            ups = 1000;

        this.game = loopSteps;
        this.desiredUpdateTime = 1000000000L / ups;
        this.running = true;

        this.maxFrameSkips = maxFrameSkips;
        this.noDelaysPerYield = noDelaysPerYield;
    }

    /**
     * Create a new MainLoop object.
     * 
     * @param loopSteps The LoopSteps that will be controlled by this loop.
     * @param ups Number of desired updates per second.
     */
    public MainLoop(LoopSteps loopSteps, int ups)
    {
        this(loopSteps, ups, DEFAULT_MAX_FRAME_SKIPS, DEFAULT_NO_DELAYS_PER_YIELD);
    }

    /**
     * Create a new MainLoop object and an update per second factor of 80.
     * 
     * @param loopSteps The LoopSteps that will be controlled by this loop.
     */
    public MainLoop(LoopSteps loopSteps)
    {
        this(loopSteps, DEFAULT_UPS);
    }

    /**
     * Sleep the given amount of time. Since the sleep() method of the thread
     * class is not precise, the overSleepTime will be calculated.
     * 
     * @param nanos Number of nanoseconds to sleep.
     * @throws InterruptedException If the thread was interrupted 
     */
    private void sleep(long nanos) throws InterruptedException
    {
        noDelays = 0;
        long beforeSleep = System.nanoTime();
        Thread.sleep(nanos / 1000000L, (int) (nanos % 1000000L));
        overSleepTime = System.nanoTime() - beforeSleep - nanos;
    }

    /**
     * If the number of frames without a delay is reached, force the thread to
     * yield, allowing other threads to process.
     */
    private void yieldIfNeed()
    {
        if (++noDelays == noDelaysPerYield)
        {
            Thread.yield();
            noDelays = 0;
        }
    }

    /**
     * Calculates the sleep time based in the calculation the previous loop. To
     * achieve this time, the frame display time will be subtracted by the time
     * elapsed in the last computation (afterTime - beforeTime). Then, if in the
     * previous loop there was an oversleep time, this will also be subtracted,
     * so the system can compensate this overtime.
     */
    private long calculateSleepTime()
    {
        return desiredUpdateTime - (afterTime - beforeTime) - overSleepTime;
    }

    /**
     * Runs the main loop. This method is not thread safe and should not be
     * called more than once.
     */
    public void run()
    {
        running = true;
        try
        {
            game.setup();
            while (running)
            {
                beforeTime = System.nanoTime();
                skipFramesInExcessTime();
    
                // Updates, renders and paint the screen
                game.processLogics();
                game.renderGraphics();
                game.paintScreen();
                afterTime = System.nanoTime();
    
                long sleepTime = calculateSleepTime();
    
                if (sleepTime >= 0)
                    sleep(sleepTime);
                else
                {
                    excessTime -= sleepTime; // Sleep time is negative
                    overSleepTime = 0L;
                    yieldIfNeed();
                }
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("Exception during game loop", e);
        }
        finally
        {
            running = false;
            game.tearDown();
            System.exit(0);
        }
    }

    /**
     * Skip the number of frames according to the given excess time. This allow
     * the game to run with the same speed even if the computer has a frame rate
     * minor than the necessary.The number of total skips are limited to
     * MAX_FRAME_SKIPS.
     * 
     * @param exceededTime The exceeded time. If the time is bigger enough to
     *        skip one or more frames, they will be skipped.
     * @return The remaining excess time, after the skips.
     */
    private void skipFramesInExcessTime()
    {
        int skips = 0;
        while ((excessTime > desiredUpdateTime) && (skips < maxFrameSkips))
        {
            excessTime -= desiredUpdateTime;
            game.processLogics();
            skips++;
        }
    }

    /**
     * Stops the main loop thread. Normally game applications finishes
     * afterwards.
     */
    public void stop()
    {
        running = false;
    }
}
