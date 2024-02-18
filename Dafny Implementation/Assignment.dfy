class {:autocontracts} CarPark
{
    var parkedCars: set <nat> // cars currently parked
    var totalCapacity: nat

    ghost predicate Valid()
    reads this
    {
        // ensure that the number of parked cars don't exceed the capacity of the car park
        |parkedCars| <= totalCapacity 
        
    }

    constructor()
    ensures parkedCars == {} 
    {
        // Create a new instance of the class, setting the parkedCars set as empty
        parkedCars := {};
    }

    method enterCarPark(car: nat) returns (success: bool)
    modifies this

    // if success is true, the car was not already parked and the number of parked cars doesn't exceed capacity
    ensures success ==> car !in old(parkedCars) && |parkedCars| <= totalCapacity
    // if success isn't true, don't change the state of parked cars
    ensures !success ==> parkedCars == old(parkedCars)
    // The valid predicate must hold true regardless of the outcome
    ensures Valid()
    {

        // Check if a car is already parked, return false
        if (car in parkedCars) 
        {
            return false;
        } 
        // Check if the size of the set is smaller than the capacity, if so, add a car and return true
        else if (|parkedCars| < totalCapacity) {
            parkedCars := parkedCars + {car};
            return true;
        } 
        // Otherwise return false
        else 
        {
            return false;
        }
    }

    method leaveCarPark(car: nat) returns (success: bool)
    modifies this
    
    // If success is true, the car in the parkedCars set is removed
    ensures success ==> car in old(parkedCars) && parkedCars == old(parkedCars) - {car} 
    // If success is false, the car wasn't in the set
    ensures !success ==> car !in old(parkedCars) 
    // State the result
    ensures parkedCars == (if car in old(parkedCars) then old(parkedCars) - {car} else old(parkedCars)) 
    // Ensures predicate holds true regardless
    ensures Valid()
    {
        // If there's a car in the set, remove it from the set and return true
        if (car in parkedCars) 
        {
            parkedCars := parkedCars - {car};
            return true;
        } 
        // Otherwise return false
        else 
        {
            return false;
        }
    }

    // Method to return the number of available spaces in the car park
    method checkAvailability() returns (availableSpaces: nat)
    {
        return totalCapacity - |parkedCars|;
    }

    // Method to evaluate to true when the invariants hold (if there's less parked cars than the capacity)
    method valid() returns (isValid: bool)
    {
        return |parkedCars| <= totalCapacity;
    }

    method printParkingPlan()
    {
        print "Total Capacity: ", totalCapacity, "\n";
        print "Parked Cars: ", parkedCars, "\nAvailable Space: ", totalCapacity - |parkedCars|, "\n";
    }

}

method Main()
    {
        var carpark: CarPark := new CarPark();
        // Assume the car park has a capacity of 10
        carpark.totalCapacity := 10; 

        // Attempt to park a car
        print "Attempting to park a car\n";
        var success := carpark.enterCarPark(1);
        if (success) 
        {
            print "Parked successfully.\n";
        } 
        else 
        {
            print "Failed to park\n";
        }
        carpark.printParkingPlan();

        // Attempt to park a second car
        print "Attempting to park another car\n";
        success := carpark.enterCarPark(2);
        if (success) 
        {
            print "Another car parked successfully.\n";
        } 
        else 
        {
            print "Failed to park another car\n";
        }
        carpark.printParkingPlan();

        // Attempt to leave the car park
        print "Attempting to leave the car park\n";
        success := carpark.leaveCarPark(1);
        if (success) 
        {
            print "Car 1 left the car park\n";
        } 
        else 
        {
            print "Car 1 couldn't leave the car park\n";
        }
        carpark.printParkingPlan();
    }
