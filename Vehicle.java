public class Vehicle
{
    private String destination;
    private long entryTime;
    private long parkTime;

    public Vehicle(String destination, long entryTime)
    {

        this.destination = destination;
        this.entryTime = entryTime;

    }

    public void setParkTime(long parkTime)
    {
        this.parkTime = parkTime;
    }

    public String getDestination()
    {
        return destination;
    }

    public long getEntryTime()
    {
        return entryTime;
    }

    public long getParkTime()
    {
        return parkTime;
    }
}