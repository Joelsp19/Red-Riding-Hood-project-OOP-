import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FullHouse extends House{

    public FullHouse(
            String id,
            Point position,
            List<Resource> resources,
            List<PImage> images) {
        super(id,position,resources,images);
    }

    public void transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore) {
        if (getResources().get(3).getQuantity() <= 0) {
            House h = Factory.createHouse(getId(),
                    getPosition(),
                    getResources(),
                    getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(h);

        }

    }

}
