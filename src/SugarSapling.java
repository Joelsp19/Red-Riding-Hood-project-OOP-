import processing.core.PImage;

import java.util.List;

public class SugarSapling extends ChoppableEntity{


    public static final int SUGAR_SAPLING_HEALTH_LIMIT = 5;
    public static final int SUGAR_SAPLING_ACTION_ANIMATION_PERIOD = 1300; // have to be in sync since grows and gains health at same time


    //assuming that a wheat sapling grows at the same rate as a normal tree sapling
    public SugarSapling(
            String id,
            Point position,
            List<PImage> images) {
        super(id,position,images,SUGAR_SAPLING_ACTION_ANIMATION_PERIOD,SUGAR_SAPLING_ACTION_ANIMATION_PERIOD,0,SUGAR_SAPLING_HEALTH_LIMIT);
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
            Entity sugarStump = Factory.createSugarStump(getId(),
                    getPosition(),
                    imageStore.getImageList(Functions.SUGAR_STUMP_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(sugarStump);

            return true;
        } else if (getHealth() >= getHealthLimit()) {
            AnimationEntity sugar = Factory.createSugar("sugar_" + getId(),
                    getPosition(),
                    getNumFromRange(Functions.SUGAR_ACTION_MAX, Functions.SUGAR_ACTION_MIN),
                    getNumFromRange(Functions.SUGAR_ANIMATION_MAX, Functions.SUGAR_ANIMATION_MIN),
                    getNumFromRange(Functions.SUGAR_HEALTH_MAX, Functions.SUGAR_HEALTH_MIN),
                    imageStore.getImageList(Functions.SUGAR_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(sugar);
            sugar.scheduleActions(world,scheduler, imageStore);

            return true;
        }

        return false;
    }


}
