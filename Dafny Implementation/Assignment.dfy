class {:autocontracts} CarPark
{
    var parkedCars: set <string> // cars currently parked
    //var subscriptions: set <string> // cars with a subscription
    var totalCapacity: nat

    ghost predicate Valid()
    reads this
    {
        // fixed total capacity
        |parkedCars| <= totalCapacity 
        
    }

    constructor()
    ensures parkedCars == {} //&& subscriptions == {}
    {
        parkedCars := {};
        //subscriptions := {};
    }

    method enterCarPark(car: string) returns (success: bool)
    modifies this
    ensures success ==> car !in old(parkedCars) && |parkedCars| <= totalCapacity
    ensures !success ==> parkedCars == old(parkedCars)
    ensures Valid()
    {
        if (car in parkedCars) 
        {
            return false;
        } 
        else if (|parkedCars| < totalCapacity) {
            parkedCars := parkedCars + {car};
            return true;
        } 
        else 
        {
            return false;
        }
    }

    method leaveCarPark(car: string) returns (success: bool)
    modifies this
    ensures success ==> car in old(parkedCars) && parkedCars == old(parkedCars) - {car} // If success, car was in parkedCars and is now removed
    ensures !success ==> car !in old(parkedCars) // If not success, car was not in parkedCars to begin with
    ensures parkedCars == (if car in old(parkedCars) then old(parkedCars) - {car} else old(parkedCars)) // Explicitly stating result
    ensures Valid()
    {
        if (car in parkedCars) {
            parkedCars := parkedCars - {car};
            return true;
        } else {
            return false;
        }
    }

    method checkAvailability() returns (availableSpaces: nat)
    {
        return totalCapacity - |parkedCars|;
    }

    // Assuming a method to validate if the state invariants hold
    method valid() returns (isValid: bool)
    {
        return |parkedCars| <= totalCapacity;
        //return parkedCars * subscriptions == {} && |parkedCars| <= totalCapacity;
    }
}