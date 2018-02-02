package edu.cmu.cs.cs214.hw2.items.vehicles;

import edu.cmu.cs.cs214.hw2.Location;
import edu.cmu.cs.cs214.hw2.Util;
import edu.cmu.cs.cs214.hw2.Direction;

public class Jeep extends AbstractVehicle {
	public Jeep(Location initialLocation, Direction initialDirection) {
		super(170, 100, 1, 5);
		image = Util.loadImage("jeep.png");
		location = initialLocation;
		direction = initialDirection;
	}
}
