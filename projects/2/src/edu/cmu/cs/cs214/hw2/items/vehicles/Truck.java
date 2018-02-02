package edu.cmu.cs.cs214.hw2.items.vehicles;

import edu.cmu.cs.cs214.hw2.Location;
import edu.cmu.cs.cs214.hw2.Util;
import edu.cmu.cs.cs214.hw2.Direction;

public class Truck extends AbstractVehicle {
	public Truck(Location initialLocation, Direction initialDirection) {
		super(200, 150, 1, 6);
		image = Util.loadImage("truck.jpg");
		location = initialLocation;
		direction = initialDirection;
	}
}
