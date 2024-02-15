public class Road {
    
    private final Vehicle[] vehicles;
    private int head = 0;
    private int tail = 0;
    private int count = 0;

    public Road(int capacity)
    {
        vehicles = new Vehicle[capacity];
    }

    public synchronized boolean addVehicle(Vehicle vehicle)
    {
        boolean added = false;
        if (count < vehicles.length)
        {
            vehicles[tail] = vehicle;
            tail = (tail + 1) % vehicles.length;
            count++;
            added = true;

            if (added)
            {
                System.out.println("Vehicle added to road. Current road count: " + count);
            }
        }

        return added;
    }

    public synchronized Vehicle consumeVehicle()
    {
        if (count > 0)
        {
            Vehicle vehicle = vehicles[head];
            if (vehicle != null)
            {
                System.out.println("Vehicle removed from road. Current road count: " + count);
            }
            head = (head + 1) % vehicles.length;
            count--;
            return vehicle;
        }

        return null;
    }

    public synchronized int getCount()
    {
        return count;
    }

    public synchronized boolean hasSpace()
    {
        return count < vehicles.length;
    }
}
