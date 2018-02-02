package edu.cmu.cs.cs214.hw2.disasters;

import edu.cmu.cs.cs214.hw2.Direction;
import edu.cmu.cs.cs214.hw2.Location;
import edu.cmu.cs.cs214.hw2.Util;

public class Quake extends AbstractDisaster {

	public Quake(Location initialLocation, Direction initialDirection) {
		super(500, 500, 5, 5);
		location = initialLocation;
		direction = initialDirection;
		image = Util.loadImage("quake.jpg");
	}
	
}
