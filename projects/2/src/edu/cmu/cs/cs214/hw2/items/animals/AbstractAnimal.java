package edu.cmu.cs.cs214.hw2.items.animals;

import javax.swing.ImageIcon;

import edu.cmu.cs.cs214.hw2.Food;
import edu.cmu.cs.cs214.hw2.Location;
import edu.cmu.cs.cs214.hw2.Util;
import edu.cmu.cs.cs214.hw2.World;
import edu.cmu.cs.cs214.hw2.ai.AI;
import edu.cmu.cs.cs214.hw2.commands.Command;
import edu.cmu.cs.cs214.hw2.items.LivingItem;
import edu.cmu.cs.cs214.hw2.items.animals.ArenaAnimal;

public class AbstractAnimal implements ArenaAnimal {
	protected int initialEnergy;
	protected int maxEnergy;
	protected int strength;
	protected int viewRange;
	protected int minBreedingEnergy;
	protected int cooldown;
	protected ImageIcon image;
	protected AI ai;

	protected Location location;
	protected int energy;

	public AbstractAnimal(int ie, int me, int s, int vr, int mbe, int c) {
		initialEnergy = ie;
		maxEnergy = me;
		strength = s;
		viewRange = vr;
		minBreedingEnergy = mbe;
		cooldown = c;
	}

	@Override
	public int getEnergy() {
		return energy;
	}

	@Override
	public LivingItem breed() {
		return null;
	}

	@Override
	public void eat(Food food) {
		// Note that energy does not exceed energy limit.
	}

	@Override
	public void moveTo(Location targetLocation) {
		location = targetLocation;

	}

	@Override
	public int getMovingRange() {
		return 1; // Can only move to adjacent locations.
	}

	@Override
	public ImageIcon getImage() {
		return image;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
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
		this.energy = this.energy - energyLoss;
	}

	@Override
	public boolean isDead() {
		return energy <= 0;
	}

	@Override
	public int getPlantCalories() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMeatCalories() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCoolDownPeriod() {
		return cooldown;
	}

	@Override
	public Command getNextAction(World world) {
		Command nextAction = ai.getNextAction(world, this);
		this.energy--; // Loses 1 energy regardless of action.
		return nextAction;
	}

	@Override
	public int getMaxEnergy() {
		return maxEnergy;
	}

	@Override
	public int getViewRange() {
		return viewRange;
	}

	@Override
	public int getMinimumBreedingEnergy() {
		return minBreedingEnergy;
	}
}
