package edu.cmu.cs.cs214.hw2.items.animals;

import javax.swing.ImageIcon;

import edu.cmu.cs.cs214.hw2.Direction;
import edu.cmu.cs.cs214.hw2.Food;
import edu.cmu.cs.cs214.hw2.Location;
import edu.cmu.cs.cs214.hw2.Util;
import edu.cmu.cs.cs214.hw2.World;
import edu.cmu.cs.cs214.hw2.commands.Command;
import edu.cmu.cs.cs214.hw2.commands.MoveCommand;
import edu.cmu.cs.cs214.hw2.commands.WaitCommand;
import edu.cmu.cs.cs214.hw2.items.LivingItem;

/**
 * This is a simple implementation of a Gnat. It never loses energy and moves in
 * random directions.
 */
public class Gnat implements LivingItem {
	private static final ImageIcon gnatImage = Util.loadImage("gnat.gif");

	private static final int MEAT_CALORIES = 17;
	private static final int STRENGTH = 10;

	private Location location;
	private boolean isDead;

	/**
	 * Create a new Gnat at <code>initialLocation</code>. The
	 * <code>initialLocation</code> must be valid and empty.
	 * 
	 * @param initialLocation
	 *            the location where the Gnat will be created
	 */
	public Gnat(Location initialLocation) {
		this.location = initialLocation;
		this.isDead = false;
	}

	@Override
	public ImageIcon getImage() {
		return gnatImage;
	}

	@Override
	public String getName() {
		return "Gnat";
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public int getPlantCalories() {
		return 0;
	}

	@Override
	public int getMeatCalories() {
		return MEAT_CALORIES;
	}

	@Override
	public void loseEnergy(int energyLoss) {
		isDead = true; // Dies if it loses energy.
	}

	@Override
	public boolean isDead() {
		return isDead;
	}

	@Override
	public void moveTo(Location targetLocation) {
		location = targetLocation;
	}

	@Override
	public int getCoolDownPeriod() {
		// Each Gnat acts every 1-10 steps randomly.
		return Util.RAND.nextInt(10) + 1;
	}

	@Override
	public Command getNextAction(World world) {
		// The Gnat selects a random direction and check if the next location at
		// the direction is valid and empty. If yes, then it moves to the
		// location, otherwise it waits.
		Direction dir = Util.getRandomDirection();
		Location targetLocation = new Location(this.getLocation(), dir);

		if (Util.isValidLocation(world, targetLocation)
				&& Util.isLocationEmpty(world, targetLocation)) {
			return new MoveCommand(this, targetLocation);
		}

		return new WaitCommand();
	}

	@Override
	public int getStrength() {
		return STRENGTH;
	}

	@Override
	public int getEnergy() {
		// doesn't ever die, except when run over by a Vehicle or eaten by
		// spider
		return 20;
	}

	@Override
	public LivingItem breed() {
		return null; // Never eats.
	}

	@Override
	public void eat(Food food) {
		// Never eats.
	}

	@Override
	public int getMovingRange() {
		return 1; // Can only move to adjacent locations.
	}
}
