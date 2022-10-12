import processing.core.PImage;

import java.util.List;

public class Sugar extends ChoppableEntity {
    public Sugar(
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
            Entity sugarStump = Factory.createSugarStump(getId(),
                    getPosition(),
                    imageStore.getImageList(Functions.SUGAR_STUMP_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(sugarStump);
            scheduleActions( world,scheduler, imageStore);

            return true;
        }

        return false;
    }

}
