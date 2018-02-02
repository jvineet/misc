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
import edu.cmu.cs.cs214.hw2.items.Item;
import edu.cmu.cs.cs214.hw2.items.animals.ArenaAnimal;

public class AbstractAI implements AI {

	protected boolean hasFood(World world, Location l) {
		return true; // unimplemented
	}

	protected Item getFood(World world, Location l) {
		return null; // unimplemented
	}

	/* gives the food count immediately around current location */
	protected int getLocationAdjacentFoodCount(Location loc, World world) {
		int numLocs = 0;
		for (int x = loc.getX() - 1; x <= loc.getX() + 1; x++) {
			for (int y = loc.getY() - 1; y <= loc.getY() + 1; y++) {
				Location l = new Location(x, y);
				if (Util.isValidLocation(world, l) && hasFood((World) world, l)) {
					numLocs++;
				}
			}
		}
		return numLocs;
	}

	@Override
	public Command getNextAction(ArenaWorld world, ArenaAnimal animal) {
		Location currentLoc = animal.getLocation();
		Location nextLoc = currentLoc;
		// to search for food in all adjacent locations
		Location[] neighbors = new Location[3 * 3]; // 3 x 3 bounding box
		// to search for the next best move
		int[] neighborsFoodCount = new int[3 * 3];
		int numFoodLocs = 0;
		int numValidLocs = 0;
		int mostFood = 0;
		int bestLocIndex = 0;

		// look through neighbors for either presence of food or best next
		// location for moving
		for (int x = currentLoc.getX() - 1; x <= currentLoc.getX() + 1; x++) {
			for (int y = currentLoc.getY() - 1; y <= currentLoc.getY() + 1; y++) {
				Location l = new Location(x, y);

				// check for next location surrounded with most food
				if (Util.isValidLocation(world, l)) {
					neighborsFoodCount[numValidLocs] = getLocationAdjacentFoodCount(
							l, (World) world);
					neighbors[numValidLocs] = l;
					// keep track of current best location
					if (neighborsFoodCount[numValidLocs] > mostFood) {
						mostFood = neighborsFoodCount[numValidLocs];
						bestLocIndex = numValidLocs;
					}
					numValidLocs++;

					// keep track of adjacent location with food
					if (hasFood((World) world, l)) {
						nextLoc = l;
						numFoodLocs++;
					}
				}

			}
		}

		// feed the animal
		if (numFoodLocs != 0) {
			Item food = getFood((World) world, nextLoc);
			return new EatCommand(animal, food);
		}

		// retrieve the next best location
		if (mostFood == 0)
			nextLoc = neighbors[Util.RAND.nextInt(numValidLocs)];
		else
			nextLoc = neighbors[bestLocIndex];

		if (Util.isValidLocation(world, nextLoc)
				&& Util.isLocationEmpty((World) world, nextLoc)) {
			// breed animal if energy is 0.8 times maximum
			if (animal.getEnergy() >= 0.8 * animal.getMaxEnergy())
				return new BreedCommand(animal, nextLoc);
			// if energy is less than 0.8 times maximum then move animal
			return new MoveCommand(animal, nextLoc);
		}
		// wait for next step
		return new WaitCommand();

	}

}
