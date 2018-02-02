package edu.cmu.cs.cs214.hw2.commands;

import edu.cmu.cs.cs214.hw2.Location;
import edu.cmu.cs.cs214.hw2.Util;
import edu.cmu.cs.cs214.hw2.World;
import edu.cmu.cs.cs214.hw2.items.Item;
import edu.cmu.cs.cs214.hw2.items.LivingItem;


/**
 * A CrashCommand is a {@link Command} which represents a {@link MovableItem}
 * crashing into another item.
 */
public final class CrashCommand implements Command {

	private final LivingItem item;
	private final Item target;

	/**
	 * Construct a {@link MoveCommand}, where <code>item</code> is the moving
	 * item and <code>target</code> is the item it is crashing with
	 * 
	 * @param item
	 *            the Item that is moving
	 * @param target
	 *            the Item it might be crashing with
	 */
	public CrashCommand(LivingItem item, Item target) {
		this.item = item;
		this.target = target;
	}

	@Override
	public void execute(World world) throws InvalidCommandException {
		if (item.getStrength() > target.getStrength()) {
			target.loseEnergy(Integer.MAX_VALUE);
		} else {
			item.loseEnergy(Integer.MAX_VALUE);
		}

	}

}
