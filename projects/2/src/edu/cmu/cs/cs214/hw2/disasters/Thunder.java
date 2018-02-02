package edu.cmu.cs.cs214.hw2.disasters;

import edu.cmu.cs.cs214.hw2.Direction;
import edu.cmu.cs.cs214.hw2.Location;
import edu.cmu.cs.cs214.hw2.Util;

public class Thunder extends AbstractDisaster {

	public Thunder(Location initialLocation, Direction initialDirection) {
		super(500, 500, 7, 1);
		location = initialLocation;
		direction = initialDirection;
		image = Util.loadImage("thunder.png");
	}
	
}
