package edu.cmu.cs.cs214.hw2.items.animals;

import javax.swing.ImageIcon;

import edu.cmu.cs.cs214.hw2.Food;
import edu.cmu.cs.cs214.hw2.Location;
import edu.cmu.cs.cs214.hw2.Util;
import edu.cmu.cs.cs214.hw2.World;
import edu.cmu.cs.cs214.hw2.ai.AI;
import edu.cmu.cs.cs214.hw2.commands.Command;
import edu.cmu.cs.cs214.hw2.items.LivingItem;

//wolf eats fox and rabbits
public class Wolf extends AbstractAnimal {

	public Wolf(AI wolfAI, Location initialLocation) {
		super(119, 150, 130, 5, 30, 2);
		ai = wolfAI;
		location = initialLocation;
		image = Util.loadImage("Wolf.jpg");
		energy = initialEnergy;
	}

	@Override
	public LivingItem breed() {
		Wolf child = new Wolf(ai, location);
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
		return "Wolf";
	}

	@Override
	public int getMeatCalories() {
		return energy;
	}
}
