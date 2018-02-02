package edu.cmu.cs.cs214.hw2.items.animals;

import javax.swing.ImageIcon;

import edu.cmu.cs.cs214.hw2.Food;
import edu.cmu.cs.cs214.hw2.Location;
import edu.cmu.cs.cs214.hw2.Util;
import edu.cmu.cs.cs214.hw2.World;
import edu.cmu.cs.cs214.hw2.ai.AI;
import edu.cmu.cs.cs214.hw2.commands.Command;
import edu.cmu.cs.cs214.hw2.items.LivingItem;

//lizard eats spiders
public class Lizard extends AbstractAnimal {
	
	public Lizard(AI lizardAI, Location initialLocation) {
		super(30, 38, 30, 3, 2, 2);
		ai = lizardAI;
		location = initialLocation;
		energy = initialEnergy;
		image = Util.loadImage("lizard.jpg");
	}

	@Override
	public LivingItem breed() {
		Lizard child = new Lizard(ai, location);
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
		return "Lizard";
	}

	@Override
	public int getMeatCalories() {
		return energy;
	}
	
}
