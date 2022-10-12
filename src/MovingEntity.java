import processing.core.PImage;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public abstract class MovingEntity extends ActivityEntity {

        public MovingEntity(
                String id,
                Point position,
                List<PImage> images, int actionPeriod, int animationPeriod) {
                super(id,position,images,actionPeriod,animationPeriod);
        }

        public abstract boolean moveTo(
                WorldModel world,
                Entity target,
                EventScheduler scheduler);

        public abstract Point nextPosition(
                WorldModel world, Point destPos);

        public Optional<Entity> findNearest(
                WorldModel world, Point pos, List<Class> kinds) {
                List<Entity> ofType = new LinkedList<>();
                for (Class kind : kinds) {
                        for (Entity entity : world.getEntities()) {
                                if (entity.getClass() == kind) {
                                        ofType.add(entity);
                                }
                        }
                }
                return nearestEntity(ofType, pos);
        }

        public Optional<Entity> nearestEntity(
                List<Entity> entities, Point pos) {
                if (entities.isEmpty()) {
                        return Optional.empty();
                } else {
                        Entity nearest = entities.get(0);
                        int nearestDistance = distanceSquared(nearest.getPosition(), pos);

                        for (Entity other : entities) {
                                int otherDistance = distanceSquared(other.getPosition(), pos);

                                if (otherDistance < nearestDistance) {
                                        nearest = other;
                                        nearestDistance = otherDistance;
                                }
                        }

                        return Optional.of(nearest);
                }
        }

        private int distanceSquared(Point p1, Point p2) {
                int deltaX = p1.x - p2.x;
                int deltaY = p1.y - p2.y;

                return deltaX * deltaX + deltaY * deltaY;
        }



}




