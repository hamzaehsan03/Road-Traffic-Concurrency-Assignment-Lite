public class Main {

    public static void main(String[] args)
    {
        // I forgot the word but I'm temporarily putting in the variables that will be read from a config file
        // Update: Hardcoding

        int entryRate = 550;
        int roadCapacity = 60;
        int exitRoadCapacity = 15;
        int carParkCapacity = 1000;
        long greenDuration = 60;
        long simDuration = 3600000; // 3,600,000
        long tickDuration = 1000; // 3 600 000 /1000 = 3600 多分


        Clock clock = new Clock(simDuration, tickDuration);
        Road entryRoad = new Road(roadCapacity);
        Road exitRoad = new Road(exitRoadCapacity);

        Junction junctionA = new Junction(entryRoad, exitRoad, greenDuration, "A", clock);
        CarPark industrial = new CarPark(exitRoad, carParkCapacity, "Industrial Park", clock);      
        EntryPoint entryPoint = new EntryPoint(entryRoad, entryRate, "Industrial Park", clock);

        clock.start();
        industrial.start();
        junctionA.start();
        entryPoint.start();

        try
        {
            clock.join();
            entryPoint.interrupt();
            junctionA.interrupt();
            industrial.interrupt();

            entryPoint.join();
            junctionA.join();
            industrial.join();
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }


    }

    
}
