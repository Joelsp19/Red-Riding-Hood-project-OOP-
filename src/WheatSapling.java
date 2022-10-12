import processing.core.PImage;

import java.util.List;



public class WheatSapling extends ChoppableEntity {

    public static final int WHEAT_SAPLING_HEALTH_LIMIT = 5;
    public static final int WHEAT_SAPLING_ACTION_ANIMATION_PERIOD = 1300; // have to be in sync since grows and gains health at same time


    //assuming that a wheat sapling grows at the same rate as a normal tree sapling
    public WheatSapling(
            String id,
            Point position,
            List<PImage> images) {
        super(id,position,images,WHEAT_SAPLING_ACTION_ANIMATION_PERIOD,WHEAT_SAPLING_ACTION_ANIMATION_PERIOD,0,WHEAT_SAPLING_HEALTH_LIMIT);
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
            Entity wheatStump = Factory.createWheatStump(getId(),
                    getPosition(),
                    imageStore.getImageList(Functions.WHEAT_STUMP_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(wheatStump);

            return true;
        } else if (getHealth() >= getHealthLimit()) {
            AnimationEntity wheat = Factory.createWheat("wheat_" + getId(),
                    getPosition(),
                    getNumFromRange(Functions.WHEAT_ACTION_MAX, Functions.WHEAT_ACTION_MIN),
                    getNumFromRange(Functions.WHEAT_ANIMATION_MAX, Functions.WHEAT_ANIMATION_MIN),
                    getNumFromRange(Functions.WHEAT_HEALTH_MAX, Functions.WHEAT_HEALTH_MIN),
                    imageStore.getImageList(Functions.WHEAT_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(wheat);
            wheat.scheduleActions(world,scheduler, imageStore);

            return true;
        }

        return false;
    }


}
