import processing.core.PImage;

import java.util.*;

public class Sapling extends ChoppableEntity{

    public static final String SAPLING_KEY = "sapling";
    public static final int SAPLING_HEALTH_LIMIT = 5;
    public static final int SAPLING_ACTION_ANIMATION_PERIOD = 1000; // have to be in sync since grows and gains health at same time
    public static final int SAPLING_NUM_PROPERTIES = 4;
    public static final int SAPLING_ID = 1;
    public static final int SAPLING_COL = 2;
    public static final int SAPLING_ROW = 3;
    public static final int SAPLING_HEALTH = 4;

    public Sapling(
            String id,
            Point position,
            List<PImage> images) {
        super(id,position,images,SAPLING_ACTION_ANIMATION_PERIOD,SAPLING_ACTION_ANIMATION_PERIOD,0,SAPLING_HEALTH_LIMIT);
    }

    public void execute(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {
        setHealth(getHealth()+1);

        if (!transform(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    getActionPeriod());
        }

    }


    private boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore) {
        if (getHealth() <= 0) {
            Entity stump = Factory.createStump(getId(),
                    getPosition(),
                    imageStore.getImageList(Functions.STUMP_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(stump);

            return true;
        } else if (getHealth() >= getHealthLimit()) {
            AnimationEntity tree = Factory.createTree("tree_" + getId(),
                    getPosition(),
                    getNumFromRange(Functions.TREE_ACTION_MAX, Functions.TREE_ACTION_MIN),
                    getNumFromRange(Functions.TREE_ANIMATION_MAX, Functions.TREE_ANIMATION_MIN),
                    getNumFromRange(Functions.TREE_HEALTH_MAX, Functions.TREE_HEALTH_MIN),
                    imageStore.getImageList(Functions.TREE_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(tree);
            tree.scheduleActions(world,scheduler, imageStore);

            return true;
        }

        return false;
    }



}

