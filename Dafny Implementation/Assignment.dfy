class {:autocontracts} CarPark
{
    var parkedCars: set <string> // cars currently parked
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

    method enterCarPark(car: string) returns (success: bool)
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

    method leaveCarPark(car: string) returns (success: bool)
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

}