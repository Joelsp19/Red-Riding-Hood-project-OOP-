import processing.core.PImage;

import java.util.List;

public abstract class ActivityEntity extends AnimationEntity {

        private int actionPeriod;

        public ActivityEntity(
                String id,
                Point position,
                List<PImage> images, int actionPeriod, int animationPeriod) {
                super(id, position,images, animationPeriod);
                this.actionPeriod = actionPeriod;
        }

        public abstract void execute(
                WorldModel world,
                ImageStore imageStore,
                EventScheduler scheduler);

        public int getActionPeriod() {
                return actionPeriod;
        }

        public void setActionPeriod(int actionPeriod){this.actionPeriod = actionPeriod;}

        public void scheduleActions(
                WorldModel world,
                EventScheduler scheduler,
                ImageStore imageStore) {

                scheduler.scheduleEvent(this,
                        Factory.createActivityAction(this, world, imageStore),
                        getActionPeriod());
                super.scheduleActions(world,scheduler,imageStore);

        }
}
