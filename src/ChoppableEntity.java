import processing.core.PImage;

import java.util.List;
import java.util.Random;

public abstract class ChoppableEntity extends ActivityEntity{

        private int health;
        private int healthLimit;

        public ChoppableEntity(
                String id,
                Point position,
                List<PImage> images, int actionPeriod, int animationPeriod, int health, int healthLimit) {
                super(id,position,images, actionPeriod,animationPeriod);
                this.health = health;
                this.healthLimit = healthLimit;
        }

        public int getHealth() {
                return health;
        }

        public void setHealth(int health){
                this.health = health;
        }

        public int getHealthLimit() {return healthLimit;}

        public int getNumFromRange(int max, int min) {
                Random rand = new Random();
                return min + rand.nextInt(max - min);
        }


}
