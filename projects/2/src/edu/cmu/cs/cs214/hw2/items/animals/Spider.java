package edu.cmu.cs.cs214.hw2.items.animals;

import javax.swing.ImageIcon;

import edu.cmu.cs.cs214.hw2.Food;
import edu.cmu.cs.cs214.hw2.Location;
import edu.cmu.cs.cs214.hw2.Util;
import edu.cmu.cs.cs214.hw2.World;
import edu.cmu.cs.cs214.hw2.ai.AI;
import edu.cmu.cs.cs214.hw2.commands.Command;
import edu.cmu.cs.cs214.hw2.items.LivingItem;

//spider eats gnats
public class Spider extends AbstractAnimal {

	public Spider(AI spiderAI, Location initialLocation) {
		super(20, 27, 15, 3, 1, 2);
		ai = spiderAI;
		location = initialLocation;
		image = Util.loadImage("spider.gif");
		energy = initialEnergy;
	}

	@Override
	public LivingItem breed() {
		Spider child = new Spider(ai, location);
		child.energy = energy / 2;
		this.energy = energy / 2;
		return child;
	}

	@Override
	public void eat(Food food) {
		// Note that energy does not exceed energy limit.
		energy = Math.min(maxEnergy, energy + food.getMeatCalories());
	}

	@Override
	public String getName() {
		return "Spider";
	}

	@Override
	public int getMeatCalories() {
		return energy;
	}
}
