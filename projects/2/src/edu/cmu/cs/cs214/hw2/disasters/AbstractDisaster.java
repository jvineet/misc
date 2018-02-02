package edu.cmu.cs.cs214.hw2.disasters;

import javax.swing.ImageIcon;

import edu.cmu.cs.cs214.hw2.Actor;
import edu.cmu.cs.cs214.hw2.Direction;
import edu.cmu.cs.cs214.hw2.Location;
import edu.cmu.cs.cs214.hw2.Util;
import edu.cmu.cs.cs214.hw2.World;
import edu.cmu.cs.cs214.hw2.commands.Command;
import edu.cmu.cs.cs214.hw2.commands.MoveVehicleCommand;
import edu.cmu.cs.cs214.hw2.commands.WaitCommand;
import edu.cmu.cs.cs214.hw2.items.Item;
import edu.cmu.cs.cs214.hw2.items.MoveableItem;
import edu.cmu.cs.cs214.hw2.items.animals.Rabbit;

//Abstract disaster goes around destroying all things in it spread of destruction
public class AbstractDisaster implements MoveableItem, Actor {
	protected int strength;
	protected int energy;
	protected int cooldown;
	protected int spread; // measures spread of desrtuction
	protected ImageIcon image;
	protected Location location;
	protected Direction direction;

	public AbstractDisaster(int is, int ie, int ic, int spr) {
		strength = is;
		energy = ie;
		cooldown = ic;
		spread = spr;
	}

	@Override
	public ImageIcon getImage() {
		return image;
	}

	@Override
	public String getName() {
		// unimplimented
		return null;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public int getStrength() {
		return strength;
	}

	@Override
	public void loseEnergy(int energyLoss) {
		energy = energy - energyLoss;

	}

	@Override
	public boolean isDead() {
		return energy <= 0;
	}

	@Override
	public int getPlantCalories() {
		return 0; // not plant
	}

	@Override
	public int getMeatCalories() {
		return 0; // not animal
	}

	@Override
	public void moveTo(Location targetLocation) {
		location = targetLocation;

	}

	@Override
	public int getMovingRange() {
		return 1; // can only move to adjacent locatios
	}

	@Override
	public int getCoolDownPeriod() {
		return cooldown;
	}

	private void killItem(World world, Location l) {
		for (Item item : world.getItems()) {
			if (item.getLocation().equals(l)
					&& !(item instanceof AbstractDisaster))
				item.loseEnergy(Integer.MAX_VALUE);
		}
	}

	@Override
	public Command getNextAction(World world) {

		// kill all overlapping and adjacent items up to 'spread' units away
		for (int x = location.getX() - spread; x <= location.getX() + spread; x++) {
			for (int y = location.getY() - spread; y <= location.getY() + spread; y++) {
				Location l = new Location(x, y);
				if (Util.isValidLocation(world, l)) {
					killItem(world, l);

				}
			}
		}

		// move to a random location
		Location next = new Location(location, direction);
		int random = Util.RAND.nextInt(6);

		// changes direction with 17% probability after each cooldown or when
		// reaches end of world
		if (!Util.isValidLocation(world, next) || random == 1) {
			direction = Util.getRandomDirection();
			next = new Location(location, direction);
		}

		if (Util.isValidLocation(world, next))
			return new MoveVehicleCommand(this, next);

		return new WaitCommand();

	}
}
