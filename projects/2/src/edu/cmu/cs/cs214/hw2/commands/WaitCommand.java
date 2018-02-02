package edu.cmu.cs.cs214.hw2.commands;

import edu.cmu.cs.cs214.hw2.World;
import edu.cmu.cs.cs214.hw2.items.LivingItem;

/**
 * A WaitCommand is a {@link Command} which represents doing nothing
 */
public final class WaitCommand implements Command {

    @Override
    public void execute(World w) {
        // Do nothing.
    }

}
