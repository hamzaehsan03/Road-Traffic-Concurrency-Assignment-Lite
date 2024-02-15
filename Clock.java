public class Clock extends Thread {

    private final long duration; // Simulation duration, 1 hour in this case
    private final long tickDuration; // Real time duration of the simulation, 1s real to 10s simulated
    private long currentTime = 0; // Start at 0

    public Clock(long duration, long tickDuration)
    {
        this.duration = duration;
        this.tickDuration = tickDuration;
        
    }

    @Override
    public void run()
    {
        try
        {   
            // While the current time is less than the simulated time, put the thread to sleep for a tick
            // Increment the current time and notify threads that are waiting for updates
            while(currentTime < duration)
            {
                Thread.sleep(tickDuration);
                currentTime += tickDuration * 10;

                synchronized(this)
                {
                    //currentTime++;
                    this.notifyAll();
                }
            }
        } catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }
    
    // Getter
    public synchronized long getCurrentTime()
    {
        return currentTime;
    }

    // Method that others can call whilst waiting
    public synchronized void waitTick() throws InterruptedException
    {
        this.wait();
    }

    public void waitForAdditionalTime(long additionalTime) throws InterruptedException 
    {
        long waitUntil = this.getCurrentTime() + additionalTime / 1000;
        while (this.getCurrentTime() < waitUntil) 
        {
            synchronized (this) {
                this.wait(tickDuration);
            }
        }
    }
    // public synchronized void waitForNextTick(long additionalTime) throws InterruptedException 
    // {
    //     wait(additionalTime); // Wait for additional time in milliseconds.
    // }

}
