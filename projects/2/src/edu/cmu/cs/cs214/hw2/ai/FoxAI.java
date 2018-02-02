package edu.cmu.cs.cs214.hw2.ai;

import edu.cmu.cs.cs214.hw2.ArenaWorld;
import edu.cmu.cs.cs214.hw2.Food;
import edu.cmu.cs.cs214.hw2.Location;
import edu.cmu.cs.cs214.hw2.World;
import edu.cmu.cs.cs214.hw2.commands.BreedCommand;
import edu.cmu.cs.cs214.hw2.commands.Command;
import edu.cmu.cs.cs214.hw2.commands.EatCommand;
import edu.cmu.cs.cs214.hw2.commands.MoveCommand;
import edu.cmu.cs.cs214.hw2.commands.WaitCommand;
import edu.cmu.cs.cs214.hw2.items.Item;
import edu.cmu.cs.cs214.hw2.items.animals.*;
import edu.cmu.cs.cs214.hw2.Util;

/**
 * Implements AI for Fox.
 */
public class FoxAI extends AbstractAI {

	@Override
	protected boolean hasFood(World world, Location l) {
		for (Item item : world.getItems()) {
			if (item.getName().equals("Rabbit") && item.getLocation().equals(l)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected Item getFood(World world, Location l) {
		for (Item item : world.getItems()) {
			if (item.getName().equals("Rabbit") && item.getLocation().equals(l)) {
				return item;
			}
		}
		return null;
	}

}
