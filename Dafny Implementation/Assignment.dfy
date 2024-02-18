class {:autocontracts} CarPark
{
    var parkedCars: set <string> // cars currently parked
    var subscriptions: set <string> // cars with a subscription
    var totalCapacity: nat
    var reservedCapacity: nat

    predicate Valid()
    reads this
    {
        // fixed total capacity
        |parkedCars| <= totalCapacity &&
        parkedCars * subscriptions == {}
    }

    constructor()
    ensures parkedCars == {} && subscriptions == {}
    {
        parkedCars := {};
        subscriptions := {};
    }

    method enterCarPark(car : string) returns (success: bool)

        modifies this
        ensures car in old(parkedCars) ==> success == false
        ensures old(|parkedCars|) >= totalCapacity - reservedCapacity && car !in subscriptions ==> !success
        ensures !success ==> parkedCars == old(parkedCars)
        ensures car !in old(parkedCars) && (old(|parkedCars|) < totalCapacity || car in subscriptions) ==> success && (parkedCars == old(parkedCars) + {car})

        {
            if (car in parkedCars || (|parkedCars| >= totalCapacity - reservedCapacity && car !in subscriptions))
            {
                success := false; 
            }
            else
            {
                parkedCars := parkedCars + {car};
                success := true;
            }
                
        }

    method leaveCarPark(car: string) returns (success: bool)
    
        modifies this
        ensures car !in parkedCars ==> !success
        ensures car in parkedCars ==> success && parkedCars == old(parkedCars) - {car}

        {
            if (car in parkedCars) 
            {
                parkedCars := parkedCars - {car};
                success := true;
            } 
            else 
            {
                success := false;
            }
        }

    method checkAvailability() returns (availableSpaces: nat)
    {
        return totalCapacity - |parkedCars|;
    }

    // Assuming a method to validate if the state invariants hold
    method valid() returns (isValid: bool)
    {
        return parkedCars * subscriptions == {} && |parkedCars| <= totalCapacity;
    }
}
    

