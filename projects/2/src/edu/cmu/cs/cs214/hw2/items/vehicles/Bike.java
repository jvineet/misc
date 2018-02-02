package edu.cmu.cs.cs214.hw2.items.vehicles;

import edu.cmu.cs.cs214.hw2.Location;
import edu.cmu.cs.cs214.hw2.Util;
import edu.cmu.cs.cs214.hw2.Direction;

public class Bike extends AbstractVehicle {
	public Bike(Location initialLocation, Direction initialDirection) {
		super(140, 50, 1, 4);
		image = Util.loadImage("bike.png");
		location = initialLocation;
		direction = initialDirection;
	}
}

