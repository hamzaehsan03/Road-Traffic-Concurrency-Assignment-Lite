public class EntryPoint extends Thread {
    
    // Class to generate vehicles

    private final Road road;
    private final int carsPH;
    private final String destination;
    private final Clock clock; // Use the simulated clock for timing

    public EntryPoint(Road road, int carsPH, String destination, Clock clock)
    {
        this.road = road;
        this.carsPH = carsPH;
        this.destination = destination;
        this.clock = clock;
    }

    @Override
    public void run()
    {
        long interval = 3600 / carsPH * 1000; 
        while (!Thread.currentThread().isInterrupted())
        {
            long elapsed = clock.getCurrentTime();
            if (elapsed >= 3600*1000)
            {
                break; // after an hour
            }

            try{
                Vehicle vehicle = new Vehicle(destination, elapsed);
                if (road.addVehicle(vehicle))
                {
                    clock.waitTick(); // synchronise
                }
                else{
                    clock.waitTick(); // synchronise
                }
            } catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }
}
