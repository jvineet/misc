package edu.cmu.cs.cs214.hw2.ai;

import edu.cmu.cs.cs214.hw2.ArenaWorld;
import edu.cmu.cs.cs214.hw2.Location;
import edu.cmu.cs.cs214.hw2.Util;
import edu.cmu.cs.cs214.hw2.World;
import edu.cmu.cs.cs214.hw2.commands.BreedCommand;
import edu.cmu.cs.cs214.hw2.commands.Command;
import edu.cmu.cs.cs214.hw2.commands.EatCommand;
import edu.cmu.cs.cs214.hw2.commands.MoveCommand;
import edu.cmu.cs.cs214.hw2.commands.WaitCommand;
import edu.cmu.cs.cs214.hw2.items.Grass;
import edu.cmu.cs.cs214.hw2.items.Item;
import edu.cmu.cs.cs214.hw2.items.animals.ArenaAnimal;
import edu.cmu.cs.cs214.hw2.items.animals.Rabbit;

/**
 * Your Rabbit AI.
 */
public class RabbitAI extends AbstractAI {
    
	@Override
	protected boolean hasFood(World world, Location l) {
		for (Item item : world.getItems()) {
			if (item instanceof Grass && item.getLocation().equals(l)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected Item getFood(World world, Location l) {
		for (Item item : world.getItems()) {
			if (item instanceof Grass && item.getLocation().equals(l)) {
				return item;
			}
		}
		return null;
	}

}
