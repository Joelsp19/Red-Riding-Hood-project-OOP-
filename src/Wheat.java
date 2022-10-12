import processing.core.PImage;

import java.util.List;

public class Wheat extends ChoppableEntity{
    public Wheat(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod,
            int health) {
        super(id,position,images,actionPeriod,animationPeriod,health,0);
    }

    public void execute(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {

        if (!transform(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    getActionPeriod());
        }
    }

    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore) {
        if (getHealth() <= getHealthLimit()) {
            Entity wheatStump = Factory.createWheatStump(getId(),
                    getPosition(),
                    imageStore.getImageList(Functions.WHEAT_STUMP_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(wheatStump);
            scheduleActions( world,scheduler, imageStore);

            return true;
        }

        return false;
    }

}
