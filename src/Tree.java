import processing.core.PImage;

import java.util.*;

public class Tree extends ChoppableEntity{

    public Tree(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod,
            int health) {
        super(id,position,images,actionPeriod,animationPeriod,health, 0);

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



    //from TransformEntity interface
    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore) {
        if (getHealth() <= getHealthLimit()) {
            Entity stump = Factory.createStump(getId(),
                    getPosition(),
                    imageStore.getImageList(Functions.STUMP_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(stump);
            scheduleActions( world,scheduler, imageStore);

            return true;
        }

        return false;
    }

}

