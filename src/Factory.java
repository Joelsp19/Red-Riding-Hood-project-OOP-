import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Factory {

    public static Action createAnimationAction(AnimationEntity entity, int repeatCount) {
        return new Animation(entity, repeatCount);
    }

    public static Action createActivityAction(
            ActivityEntity entity, WorldModel world, ImageStore imageStore) {
        return new Activity( entity, world, imageStore, 0);
    }

    public static House createHouse(
            String id, Point position, List<Resource> resources,List<PImage> images) {
        return new House( id, position,resources, images);
    }

    public static GrandmaHouse createGrandmaHouse(
            String id, Point position,List<PImage> images) {
        return new GrandmaHouse( id, position, images);
    }

    public static FullHouse createFullHouse(
            String id, Point position, List<Resource> resources, List<PImage> images) {
        return new FullHouse( id, position, resources, images);
    }

    public static Obstacle createObstacle(
            String id, Point position, int animationPeriod, List<PImage> images) {
        return new Obstacle( id, position, images, animationPeriod);
    }

    public static Tree createTree(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int health,
            List<PImage> images) {
        return new Tree( id, position, images, actionPeriod, animationPeriod, health);
    }

    public static Stump createStump(
            String id,
            Point position,
            List<PImage> images) {
        return new Stump( id, position, images);
    }

    // health starts at 0 and builds up until ready to convert to Tree
    public static Sapling createSapling(
            String id,
            Point position,
            List<PImage> images) {
        return new Sapling( id, position, images);
    }

    public static Fairy createFairy(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images) {
        return new Fairy( id, position, images,actionPeriod, animationPeriod);
    }

    public static Guardian createGuardian(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images) {
        return new Guardian( id, position, images,actionPeriod, animationPeriod);
    }


    // need resource count, though it always starts at 0
    public static DudeNotFull createDudeNotFull(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int resourceLimit,
            List<PImage> images) {
        return new DudeNotFull( id, position, images, new ArrayList<Resource>(Arrays.asList(new WheatResource(0), new SugarResource(0), new WoodResource(0))), resourceLimit, actionPeriod, animationPeriod);
    }

    // don't technically need resource count ... full
    public static DudeFull createDudeFull(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<Resource> resources,
            int resourceLimit,
            List<PImage> images) {
        return new DudeFull( id, position, images, resources, resourceLimit, actionPeriod, animationPeriod);
    }

    public static RedRidingHood createRRH(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int resourceCount,
            List<PImage> images) {
        return new RedRidingHood( id, position, images, resourceCount, actionPeriod, animationPeriod);
    }

    public static Wolf createWolf(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int resourceCount,
            List<PImage> images) {
        return new Wolf( id, position, images, resourceCount, actionPeriod, animationPeriod);
    }

    public static WheatStump createWheatStump(
            String id,
            Point position,
            List<PImage> images) {
        return new WheatStump( id, position, images);
    }

    // health starts at 0 and builds up until ready to convert to Tree
    public static WheatSapling createWheatSapling(
            String id,
            Point position,
            List<PImage> images) {
        return new WheatSapling( id, position, images);
    }

    public static Wheat createWheat(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int health,
            List<PImage> images) {
        return new Wheat( id, position, images, actionPeriod, animationPeriod, health);
    }

    public static SugarStump createSugarStump(
            String id,
            Point position,
            List<PImage> images) {
        return new SugarStump( id, position, images);
    }

    // health starts at 0 and builds up until ready to convert to Tree
    public static SugarSapling createSugarSapling(
            String id,
            Point position,
            List<PImage> images) {
        return new SugarSapling( id, position, images);
    }

    public static Sugar createSugar(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int health,
            List<PImage> images) {
        return new Sugar( id, position, images, actionPeriod, animationPeriod, health);
    }





}
