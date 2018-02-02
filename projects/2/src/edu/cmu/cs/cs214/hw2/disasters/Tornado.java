package edu.cmu.cs.cs214.hw2.disasters;

import edu.cmu.cs.cs214.hw2.Direction;
import edu.cmu.cs.cs214.hw2.Location;
import edu.cmu.cs.cs214.hw2.Util;

public class Tornado extends AbstractDisaster {

	public Tornado(Location initialLocation, Direction initialDirection) {
		super(500, 500, 4, 3);
		location = initialLocation;
		direction = initialDirection;
		image = Util.loadImage("tornado.jpg");
	}
	
}
