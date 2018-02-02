package edu.cmu.cs.cs214.hw2;

import javax.swing.SwingUtilities;

import edu.cmu.cs.cs214.hw2.ai.FoxAI;
import edu.cmu.cs.cs214.hw2.ai.LizardAI;
import edu.cmu.cs.cs214.hw2.ai.RabbitAI;
import edu.cmu.cs.cs214.hw2.ai.SpiderAI;
import edu.cmu.cs.cs214.hw2.ai.WolfAI;
import edu.cmu.cs.cs214.hw2.disasters.Quake;
import edu.cmu.cs.cs214.hw2.disasters.Thunder;
import edu.cmu.cs.cs214.hw2.disasters.Tornado;
import edu.cmu.cs.cs214.hw2.items.Gardener;
import edu.cmu.cs.cs214.hw2.items.Grass;
import edu.cmu.cs.cs214.hw2.items.animals.Fox;
import edu.cmu.cs.cs214.hw2.items.animals.Gnat;
import edu.cmu.cs.cs214.hw2.items.animals.Lizard;
import edu.cmu.cs.cs214.hw2.items.animals.Rabbit;
import edu.cmu.cs.cs214.hw2.items.animals.Spider;
import edu.cmu.cs.cs214.hw2.items.animals.Wolf;
import edu.cmu.cs.cs214.hw2.items.vehicles.Bike;
import edu.cmu.cs.cs214.hw2.items.vehicles.Jeep;
import edu.cmu.cs.cs214.hw2.items.vehicles.Truck;
import edu.cmu.cs.cs214.hw2.staff.WorldImpl;
import edu.cmu.cs.cs214.hw2.staff.WorldUI;

/**
 * The Main class initialize a world with some {@link Grass}, {@link Rabbit}s,
 * {@link Fox}es, {@link Gnat}s, {@link Gardener}, etc.
 *
 * You may modify or add Items/Actors to the World.
 *
 */
public class Main {

    static final int X_DIM = 40;
    static final int Y_DIM = 40;
    static final int SPACES_PER_GRASS = 7;
    static final int INITIAL_GRASS = X_DIM * Y_DIM / SPACES_PER_GRASS;
    static final int INITIAL_GNATS = INITIAL_GRASS / 4;
    static final int INITIAL_RABBITS = INITIAL_GRASS / 4;
    static final int INITIAL_FOXES = INITIAL_GRASS / 32;
    static final int INITIAL_LIZARDS =  INITIAL_GRASS / 32;
    static final int INITIAL_SPIDERS =  INITIAL_GRASS / 4;
    static final int INITIAL_WOLVES = INITIAL_GRASS / 32;
    static final int INITIAL_TRUCKS =  10; 
    static final int INITIAL_JEEPS =  140;
    static final int INITIAL_BIKES =  40;
    static final int INITIAL_TORNADOS =  1;
    static final int INITIAL_QUAKES =  1;
    static final int INITIAL_THUNDER =  1;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().createAndShowWorld();
            }
        });
    }

    public void createAndShowWorld() {
        World world = new WorldImpl(X_DIM, Y_DIM);
        initialize(world);
        new WorldUI(world).show();
    }

    public void initialize(World world) {
        addGrass(world);
        world.addActor(new Gardener());

        //addGnats(world);
        addFoxes(world);
        addRabbits(world);
        //addWolves(world);
        //addLizards(world);
        //addSpiders(world);
       // addTrucks(world);
        //addJeeps(world);
        //addBikes(world);
        //addTornados(world);
        //addQuakes(world);
        //addThunder(world);
       
    }

    private void addGrass(World world) {
        for (int i = 0; i < INITIAL_GRASS; i++) {
            Location loc = Util.getRandomEmptyLocation(world);
            world.addItem(new Grass(loc));
        }
    }

    private void addGnats(World world) {
        for (int i = 0; i < INITIAL_GNATS; i++) {
            Location loc = Util.getRandomEmptyLocation(world);
            Gnat gnat = new Gnat(loc);
            world.addItem(gnat);
            world.addActor(gnat);
        }
    }

    private void addFoxes(World world) {
        FoxAI foxAI = new FoxAI();
        for (int i = 0; i < INITIAL_FOXES; i++) {
            Location loc = Util.getRandomEmptyLocation(world);
            Fox fox = new Fox(foxAI, loc);
            world.addItem(fox);
            world.addActor(fox);
        }
    }

    private void addRabbits(World world) {
        RabbitAI rabbitAI = new RabbitAI();
        for (int i = 0; i < INITIAL_RABBITS; i++) {
            Location loc = Util.getRandomEmptyLocation(world);
            Rabbit rabbit = new Rabbit(rabbitAI, loc);
            world.addItem(rabbit);
            world.addActor(rabbit);
        }
    }

    private void addLizards(World world) {
        LizardAI lizardAI = new LizardAI();
        for (int i = 0; i < INITIAL_LIZARDS; i++) {
            Location loc = Util.getRandomEmptyLocation(world);
            Lizard lizard = new Lizard(lizardAI, loc);
            world.addItem(lizard);
            world.addActor(lizard);
        }
    }
    
    private void addSpiders(World world) {
        SpiderAI spiderAI = new SpiderAI();
        for (int i = 0; i < INITIAL_SPIDERS; i++) {
            Location loc = Util.getRandomEmptyLocation(world);
            Spider spider = new Spider(spiderAI, loc);
            world.addItem(spider);
            world.addActor(spider);
        }
    }
    
    private void addWolves(World world) {
        WolfAI wolfAI = new WolfAI();
        for (int i = 0; i < INITIAL_WOLVES; i++) {
            Location loc = Util.getRandomEmptyLocation(world);
            Wolf wolf = new Wolf(wolfAI, loc);
            world.addItem(wolf);
            world.addActor(wolf);
        }
    }
    
    private void addTrucks(World world) {
        for (int i = 0; i < INITIAL_TRUCKS; i++) {
            Location loc = Util.getRandomEmptyLocation(world);
            Truck truck = new Truck(loc, Util.getRandomDirection());
            world.addItem(truck);
            world.addActor(truck);
        }
    }

    private void addJeeps(World world) {
        for (int i = 0; i < INITIAL_JEEPS; i++) {
            Location loc = Util.getRandomEmptyLocation(world);
            Jeep jeep = new Jeep(loc, Util.getRandomDirection());
            world.addItem(jeep);
            world.addActor(jeep);
        }
    }
    
    private void addBikes(World world) {
        for (int i = 0; i < INITIAL_BIKES; i++) {
            Location loc = Util.getRandomEmptyLocation(world);
            Bike bike = new Bike(loc, Util.getRandomDirection());
            world.addItem(bike);
            world.addActor(bike);
        }
    }
    
    private void addTornados(World world) {
        for (int i = 0; i < INITIAL_TORNADOS; i++) {
            Location loc = Util.getRandomEmptyLocation(world);
            Tornado tornado = new Tornado(loc, Util.getRandomDirection());
            world.addItem(tornado);
            world.addActor(tornado);
        }
    }
    
    private void addQuakes(World world) {
        for (int i = 0; i < INITIAL_QUAKES; i++) {
            Location loc = Util.getRandomEmptyLocation(world);
            Quake quake = new Quake(loc, Util.getRandomDirection());
            world.addItem(quake);
            world.addActor(quake);
        }
    }
    
    private void addThunder(World world) {
        for (int i = 0; i < INITIAL_THUNDER; i++) {
            Location loc = Util.getRandomEmptyLocation(world);
            Thunder thunder = new Thunder(loc, Util.getRandomDirection());
            world.addItem(thunder);
            world.addActor(thunder);
        }
    }

}
