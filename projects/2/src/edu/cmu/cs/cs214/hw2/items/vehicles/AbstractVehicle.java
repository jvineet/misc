package edu.cmu.cs.cs214.hw2.items.vehicles;

import javax.swing.ImageIcon;

import edu.cmu.cs.cs214.hw2.Actor;
import edu.cmu.cs.cs214.hw2.Direction;
import edu.cmu.cs.cs214.hw2.Food;
import edu.cmu.cs.cs214.hw2.Location;
import edu.cmu.cs.cs214.hw2.Util;
import edu.cmu.cs.cs214.hw2.World;
import edu.cmu.cs.cs214.hw2.ai.AI;
import edu.cmu.cs.cs214.hw2.commands.Command;
import edu.cmu.cs.cs214.hw2.commands.CrashCommand;
import edu.cmu.cs.cs214.hw2.commands.InvalidCommandException;
import edu.cmu.cs.cs214.hw2.commands.MoveCommand;
import edu.cmu.cs.cs214.hw2.commands.MoveVehicleCommand;
import edu.cmu.cs.cs214.hw2.commands.WaitCommand;
import edu.cmu.cs.cs214.hw2.items.Item;
import edu.cmu.cs.cs214.hw2.items.LivingItem;
import edu.cmu.cs.cs214.hw2.items.MoveableItem;

public class AbstractVehicle implements MoveableItem, Actor {
	private static boolean turns = false;

	protected int strength;
	protected int cooldown;
	protected int energy;
	protected ImageIcon image;
	protected Location location;
	protected Direction direction;
	protected int maxCooldown;
	protected int minCooldown;

	public AbstractVehicle(int st, int e, int max, int min) {
		strength = st;
		cooldown = min;
		energy = e;
		maxCooldown = max;
		minCooldown = min;
	}

	@Override
	public ImageIcon getImage() {
		return image;
	}

	@Override
	public String getName() {
		// vehicle specific implementation
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
		// not plant
		return 0;
	}

	@Override
	public int getMeatCalories() {
		// not animal
		return 0;
	}

	@Override
	public void moveTo(Location targetLocation) {
		location = targetLocation;
	}

	@Override
	public int getMovingRange() {
		return 1;
	}

	//checks if the car or the item is destroyed
	private void crash(World world) {
		for (Item item : world.getItems()) {
			if (item.getLocation().equals(location) && item != this) {
				if (getStrength() > item.getStrength() )
					item.loseEnergy(Integer.MAX_VALUE);
				else
					loseEnergy(Integer.MAX_VALUE);
			}
		}
	}

	private void speedAndTurn() {
		if (cooldown == maxCooldown)
			turns = true;
		else if (cooldown == minCooldown) {
			turns = false;
			direction = Util.getRandomDirection();
		}

		if (turns)
			cooldown++;
		else
			cooldown--;
	}

	public Command getNextAction(World world) {
		// check for crash
		crash(world);
        // checks if turning is possible 
		speedAndTurn();

		// move to next adjacet location or do nothing 
		Location next = new Location(location, direction);

		if (Util.isValidLocation(world, next))
			return new MoveVehicleCommand(this, next);
		else
			return new WaitCommand();
	}

	@Override
	public int getCoolDownPeriod() {
		return cooldown;
	}

}
