import java.util.*;
import java.util.stream.Stream;
import java.io.*;
import java.nio.file.*;

public class Main {

    private static final HashMap<String, Integer> entryPoints = new HashMap<>();
    private static final HashMap<String, Integer> junctions = new HashMap<>();

    public static void main(String[] args)
    {
        
        String filePath = (".\\Task 1 Scenarios\\Scenario1.txt");
        readConfigFile(filePath);
        System.out.println("Reading from Config file");
        outputConfig(filePath);

        int entryRate = entryPoints.getOrDefault("south", 550); // Default to 550 
        long greenDuration = junctions.getOrDefault("A", 60); // Default to 60 
        //int entryRate = 550;
        int roadCapacity = 60;
        int exitRoadCapacity = 15;
        int carParkCapacity = 1000;
        //long greenDuration = 60;
        long simDuration = 3600000; // 3,600,000
        long tickDuration = 1000; // 3 600 000 /1000 = 3600 

        Clock clock = new Clock(simDuration, tickDuration);
        Road entryRoad = new Road(roadCapacity);
        Road exitRoad = new Road(exitRoadCapacity);

        Junction junctionA = new Junction(entryRoad, exitRoad, greenDuration, "A", clock);
        CarPark industrial = new CarPark(exitRoad, carParkCapacity, "Industrial Park", clock);      
        EntryPoint entryPoint = new EntryPoint(entryRoad, entryRate, "Industrial Park", clock);

        // System.out.println(entryRate);
        // System.out.println(greenDuration);

        clock.setIndustrial(industrial);
        
        industrial.start();
        clock.start();
        junctionA.start();
        entryPoint.start();

        try
         {
            clock.join();
        } catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }

        entryPoint.interrupt();
        junctionA.interrupt();
        industrial.interrupt();

        // entryPoint.shutdown();
        // junctionA.shutdown();
        // industrial.shutdown();

        try
        {
            entryPoint.join();
            junctionA.join();
            industrial.join();
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }

        finalReport(entryRoad, exitRoad, industrial);


    }

    private static void finalReport(Road entryRoad, Road exitRoad, CarPark industrial)
    {
        int totalCreated = EntryPoint.getTotalCreated();
        int totalParked = industrial.getTotalParked();
        long totalParkedTime = industrial.getTotalParkedTime();
        int totalQueued = entryRoad.getCount() + exitRoad.getCount();

        long averageJourneyTime = totalParked > 0 ? totalParkedTime / totalParked : 0;
        long avgMinutes = averageJourneyTime / 60000; // Convert milliseconds to minutes
        long avgSeconds = (averageJourneyTime % 60000) / 1000; 
        System.out.printf("Industrial: %d Cars parked, average journey time %dm%ds\n", totalParked, avgMinutes, avgSeconds);
    

        System.out.println("Final Simulation Report:");
        System.out.println("Total Cars Created: " + totalCreated);
        System.out.println("Total Cars Parked: " + totalParked);
        System.out.println("Total Cars Queued: " + totalQueued);

        if(totalCreated == (totalParked + totalQueued))
        {
            System.out.println("All cars accounted for");
        }
        else
        {
            System.out.println("Mismatch");
        }
    }

    private static void outputConfig(String filePath)
    {
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) 
        {
        stream.forEach(System.out::println);
        } catch (IOException e) 
        {
        System.err.println("Error reading log file: " + e.getMessage());
        }
    }

    private static void readConfigFile(String filePath)
    {
        try(Scanner scanner = new Scanner(new File(filePath)))
        {
            boolean readEntry = false;
            boolean readJunction = false;

            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine().trim();
                if (line.equalsIgnoreCase("ENTRYPOINTS"))
                {
                    readEntry = true;
                    readJunction = false;
                    continue;
                }
                else if (line.equalsIgnoreCase("JUNCTIONS"))
                {
                    readEntry = false;
                    readJunction = true;
                    continue;
                }

                if (readEntry) 
                {
                    String[] parts = line.split(" ");
                    if (parts.length == 2) 
                    {
                        entryPoints.put(parts[0].toLowerCase(), Integer.parseInt(parts[1]));
                    }
                } 
                else if (readJunction) 
                {
                    String[] parts = line.split(" ");
                    if (parts.length == 2)
                    {
                        junctions.put(parts[0].toUpperCase(), Integer.parseInt(parts[1]));
                    }
                }
            
            }
        } catch (FileNotFoundException e) {
            System.err.println("Configuration file not found: " + e.getMessage());
            
        } catch (NumberFormatException e) {
            System.err.println("Error parsing configuration: " + e.getMessage());
        }   
    }

    
}
