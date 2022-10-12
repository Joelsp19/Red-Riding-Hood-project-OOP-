import processing.core.PImage;

import java.util.List;

public abstract class AnimationEntity extends Entity{

        private int imageIndex = 0;
        private int animationPeriod;

        public AnimationEntity(
                String id,
                Point position,
                List<PImage> images, int animationPeriod) {
                super(id, position, images);
                this.animationPeriod = animationPeriod;
        }

        public int getAnimationPeriod() {
                return animationPeriod;
        }
        public void setAnimationPeriod(int animationPeriod){this.animationPeriod = animationPeriod;}


        public void nextImage() {
                imageIndex = (imageIndex + 1) % getImages().size();
        }

        public void scheduleActions(
                WorldModel world,
                EventScheduler scheduler,
                ImageStore imageStore) {

                scheduler.scheduleEvent(this,
                        Factory.createAnimationAction(this, 0),
                        getAnimationPeriod());
        }


        @Override
        public PImage getCurrentImage() {
                return getImages().get(imageIndex);
        }
}
