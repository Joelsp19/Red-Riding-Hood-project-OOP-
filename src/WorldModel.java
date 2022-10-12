import processing.core.PImage;

import javax.sound.midi.SysexMessage;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Represents the 2D World in which this simulation is running.
 * Keeps track of the size of the world, the background image for each
 * location in the world, and the entities that populate the world.
 */
public final class WorldModel {
    private final int numRows;
    private final int numCols;
    private final Background background[][];
    private final Entity occupancy[][];
    private final Set<Entity> entities;

    public WorldModel(int numRows, int numCols, Background defaultBackground) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.background = new Background[numRows][numCols];
        this.occupancy = new Entity[numRows][numCols];
        this.entities = new HashSet<>();

        for (int row = 0; row < numRows; row++) {
            Arrays.fill(this.background[row], defaultBackground);
        }
    }


//    public void growEvent(ImageStore imageStore){
//        //checks if the effect area has enveloped the whole world
//        /*List<Point> pts = new ArrayList<Point>();
//        for(int x=-1 * VirtualWorld.effectArea; x<=VirtualWorld.effectArea;x++){
//            for (int y=-1 * VirtualWorld.effectArea;y<=VirtualWorld.effectArea;y++) {
//                Point pt = new Point(VirtualWorld.ptPressed.x + x, VirtualWorld.ptPressed.y + y);
//                pts.add(pt);
//            }
//        }
//        if(pts.contains(new Point(0,0)) &&
//                pts.contains(new Point(0,numCols)) &&
//                pts.contains(new Point(numRows,0))&&
//                pts.contains(new Point(numRows,numCols))){
//            System.out.println("The wolf has won");
//        }
//        */
//        System.out.println(VirtualWorld.effectArea);
//        if((VirtualWorld.effectArea < 5)) {
//            VirtualWorld.effectArea ++;
//            boolean addBckgrd;
//            for (int x = -1 * VirtualWorld.effectArea; x <= VirtualWorld.effectArea; x++) {
//                for (int y = -1 * VirtualWorld.effectArea; y <= VirtualWorld.effectArea; y++) {
//                    addBckgrd = true;
//                    if (x == 0 && y == 0) {
//                        addBckgrd = false;
//                    }
//                    if (addBckgrd) {
//                        Point pt = new Point(VirtualWorld.ptPressed.x + x, VirtualWorld.ptPressed.y + y);
//                        setBackground(pt, new Background("cloud_" + pt.x + "_" + pt.y, imageStore.getImageList(Functions.CLOUD_KEY)));
//                    }
//                }
//            }
//        }
//    }
//    public void reduceEvent(ImageStore imageStore){
//        if(VirtualWorld.effectArea > 1) {
//            boolean addBckgrd;
//            for (int x = -1 * VirtualWorld.effectArea; x <= VirtualWorld.effectArea; x++) {
//                for (int y = -1 * VirtualWorld.effectArea; y <= VirtualWorld.effectArea; y++) {
//                    addBckgrd = true;
//                    if (x == 0 && y == 0) {
//                        addBckgrd = false;
//                    }
//                    if (addBckgrd) {
//                        Point pt = new Point(VirtualWorld.ptPressed.x + x, VirtualWorld.ptPressed.y + y);
//                        setBackground(pt, new Background("grass" + pt.x + "_" + pt.y, imageStore.getImageList("grass")));
//                    }
//                }
//            }
//
//            VirtualWorld.effectArea--;
//            for (int x = -1 * VirtualWorld.effectArea; x <= VirtualWorld.effectArea; x++) {
//                for (int y = -1 * VirtualWorld.effectArea; y <= VirtualWorld.effectArea; y++) {
//                    addBckgrd = true;
//                    if (x == 0 && y == 0) {
//                        addBckgrd = false;
//                    }
//                    if (addBckgrd) {
//                        Point pt = new Point(VirtualWorld.ptPressed.x + x, VirtualWorld.ptPressed.y + y);
//                        setBackground(pt, new Background("cloud" + pt.x + "_" + pt.y, imageStore.getImageList(Functions.CLOUD_KEY)));
//                    }
//                }
//            }
//
//
//        }
//    }


    public boolean withinBounds(Point pos) {
        return pos.y >= 0 && pos.y < numRows && pos.x >= 0
                && pos.x < numCols;
    }

    /*
      Assumes that there is no entity currently occupying the
      intended destination cell.
   */
    public void addEntity(Entity entity) {
        if (withinBounds(entity.getPosition())) {
            setOccupancyCell(entity.getPosition(), entity);
            entities.add(entity);
        }
    }

    private boolean parseBackground(
            String[] properties, ImageStore imageStore) {
        if (properties.length == Functions.BGND_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.BGND_COL]),
                    Integer.parseInt(properties[Functions.BGND_ROW]));
            String id = properties[Functions.BGND_ID];
            setBackground(pt, new Background(id, imageStore.getImageList(id)));
            setBackground(pt, new Background(id, imageStore.getImageList(id)));
        }

        return properties.length == Functions.BGND_NUM_PROPERTIES;
    }

    private boolean parseSapling(
            String[] properties, ImageStore imageStore) {
        if (properties.length == Functions.SAPLING_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.SAPLING_COL]),
                    Integer.parseInt(properties[Functions.SAPLING_ROW]));
            String id = properties[Functions.SAPLING_ID];
            Entity entity = Factory.createSapling(id,pt, imageStore.getImageList(Functions.SAPLING_KEY));
            tryAddEntity(entity);
        }

        return properties.length == Functions.SAPLING_NUM_PROPERTIES;
    }

    private boolean parseWheatSapling(
            String[] properties, ImageStore imageStore) {
        if (properties.length == Functions.WHEAT_SAPLING_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.WHEAT_SAPLING_COL]),
                    Integer.parseInt(properties[Functions.WHEAT_SAPLING_ROW]));
            String id = properties[Functions.WHEAT_SAPLING_ID];
            Entity entity = Factory.createWheatSapling(id,pt, imageStore.getImageList(Functions.WHEAT_SAPLING_KEY));
            tryAddEntity(entity);
        }

        return properties.length == Functions.WHEAT_SAPLING_NUM_PROPERTIES;
    }

    private boolean parseSugarSapling(
            String[] properties, ImageStore imageStore) {
        if (properties.length == Functions.SUGAR_SAPLING_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.SUGAR_SAPLING_COL]),
                    Integer.parseInt(properties[Functions.SUGAR_SAPLING_ROW]));
            String id = properties[Functions.SUGAR_SAPLING_ID];
            Entity entity = Factory.createSugarSapling(id,pt, imageStore.getImageList(Functions.SUGAR_SAPLING_KEY));
            tryAddEntity(entity);
        }

        return properties.length == Functions.SUGAR_SAPLING_NUM_PROPERTIES;
    }


    private boolean parseDude(
            String[] properties, ImageStore imageStore) {
        if (properties.length == Functions.DUDE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.DUDE_COL]),
                    Integer.parseInt(properties[Functions.DUDE_ROW]));
            Entity entity = Factory.createDudeNotFull(properties[Functions.DUDE_ID],
                    pt,
                    Integer.parseInt(properties[Functions.DUDE_ACTION_PERIOD]),
                    Integer.parseInt(properties[Functions.DUDE_ANIMATION_PERIOD]),
                    Integer.parseInt(properties[Functions.DUDE_LIMIT]),
                    imageStore.getImageList(Functions.DUDE_KEY));
            tryAddEntity(entity);
        }

        return properties.length == Functions.DUDE_NUM_PROPERTIES;
    }

    private boolean parseRRH(
            String[] properties, ImageStore imageStore) {
        if (properties.length == Functions.RRH_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.RRH_COL]),
                    Integer.parseInt(properties[Functions.RRH_ROW]));
            Entity entity = Factory.createRRH(properties[Functions.RRH_ID],
                    pt,
                    Integer.parseInt(properties[Functions.RRH_ACTION_PERIOD]),
                    Integer.parseInt(properties[Functions.RRH_ANIMATION_PERIOD]),
                    Integer.parseInt(properties[Functions.RRH_COUNT]),
                    imageStore.getImageList(Functions.RRH_KEY));

            tryAddEntity(entity);
        }

        return properties.length == Functions.DUDE_NUM_PROPERTIES;
    }

    private boolean parseFairy(
            String[] properties, ImageStore imageStore) {
        if (properties.length == Functions.FAIRY_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.FAIRY_COL]),
                    Integer.parseInt(properties[Functions.FAIRY_ROW]));
            Entity entity = Factory.createFairy(properties[Functions.FAIRY_ID],
                    pt,
                    Integer.parseInt(properties[Functions.FAIRY_ACTION_PERIOD]),
                    Integer.parseInt(properties[Functions.FAIRY_ANIMATION_PERIOD]),
                    imageStore.getImageList(Functions.FAIRY_KEY));
            tryAddEntity(entity);
        }

        return properties.length == Functions.FAIRY_NUM_PROPERTIES;
    }

    private boolean parseTree(
            String[] properties, ImageStore imageStore) {
        if (properties.length == Functions.TREE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.TREE_COL]),
                    Integer.parseInt(properties[Functions.TREE_ROW]));
            Entity entity = Factory.createTree(properties[Functions.TREE_ID],
                    pt,
                    Integer.parseInt(properties[Functions.TREE_ACTION_PERIOD]),
                    Integer.parseInt(properties[Functions.TREE_ANIMATION_PERIOD]),
                    Integer.parseInt(properties[Functions.TREE_HEALTH]),
                    imageStore.getImageList(Functions.TREE_KEY));
            tryAddEntity(entity);
        }

        return properties.length == Functions.TREE_NUM_PROPERTIES;
    }


    private boolean parseWheat(
            String[] properties, ImageStore imageStore) {
        if (properties.length == Functions.WHEAT_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.WHEAT_COL]),
                    Integer.parseInt(properties[Functions.WHEAT_ROW]));
            Entity entity = Factory.createWheat(properties[Functions.WHEAT_ID],
                    pt,
                    Integer.parseInt(properties[Functions.WHEAT_ACTION_PERIOD]),
                    Integer.parseInt(properties[Functions.WHEAT_ANIMATION_PERIOD]),
                    Integer.parseInt(properties[Functions.WHEAT_HEALTH]),
                    imageStore.getImageList(Functions.WHEAT_KEY));
            tryAddEntity(entity);
        }

        return properties.length == Functions.WHEAT_NUM_PROPERTIES;
    }

    private boolean parseSugar(
            String[] properties, ImageStore imageStore) {
        if (properties.length == Functions.SUGAR_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.SUGAR_COL]),
                    Integer.parseInt(properties[Functions.SUGAR_ROW]));
            Entity entity = Factory.createSugar(properties[Functions.SUGAR_ID],
                    pt,
                    Integer.parseInt(properties[Functions.SUGAR_ACTION_PERIOD]),
                    Integer.parseInt(properties[Functions.SUGAR_ANIMATION_PERIOD]),
                    Integer.parseInt(properties[Functions.SUGAR_HEALTH]),
                    imageStore.getImageList(Functions.SUGAR_KEY));
            tryAddEntity(entity);
        }

        return properties.length == Functions.SUGAR_NUM_PROPERTIES;
    }


    private boolean parseObstacle(
            String[] properties, ImageStore imageStore) {
        if (properties.length == Functions.OBSTACLE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.OBSTACLE_COL]),
                    Integer.parseInt(properties[Functions.OBSTACLE_ROW]));
            Entity entity = Factory.createObstacle(properties[Functions.OBSTACLE_ID], pt,
                    Integer.parseInt(properties[Functions.OBSTACLE_ANIMATION_PERIOD]),
                    imageStore.getImageList(Functions.OBSTACLE_KEY));
            tryAddEntity(entity);
        }

        return properties.length == Functions.OBSTACLE_NUM_PROPERTIES;
    }

    private boolean parseHouse(String[] properties, ImageStore imageStore) {
        if (properties.length == Functions.HOUSE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.HOUSE_COL]),
                    Integer.parseInt(properties[Functions.HOUSE_ROW]));
            Entity entity = Factory.createHouse(properties[Functions.HOUSE_ID], pt, new ArrayList<Resource>(Arrays.asList(new WheatResource(0), new SugarResource(0), new WoodResource(0), new CookieResource(0))),
                    imageStore.getImageList(Functions.HOUSE_KEY));
            tryAddEntity(entity);
        }

        return properties.length == Functions.HOUSE_NUM_PROPERTIES;
    }

    private void tryAddEntity(Entity entity) {
        if (isOccupied(entity.getPosition())) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        addEntity(entity);
    }

    public boolean isOccupied(Point pos) {
        return withinBounds(pos) && getOccupancyCell(pos) != null;
    }

    public void moveEntity(Entity entity, Point pos) {
        Point oldPos = entity.getPosition();
        if (withinBounds(pos) && !pos.equals(oldPos)) {
            setOccupancyCell(oldPos, null);
            removeEntityAt(pos);
            setOccupancyCell(pos, entity);
            entity.setPosition(pos);
        }
    }

    public void removeEntity(Entity entity) {
        removeEntityAt(entity.getPosition());
    }

    private void removeEntityAt(Point pos) {
        if (withinBounds(pos) && getOccupancyCell(pos) != null) {
            Entity entity = getOccupancyCell(pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.setPosition(new Point(-1, -1));
            entities.remove(entity);
            setOccupancyCell(pos, null);
        }
    }

    public Entity getOccupancyCell(Point pos) {
        return occupancy[pos.y][pos.x];
    }

    public void setOccupancyCell(
            Point pos, Entity entity) {
        occupancy[pos.y][pos.x] = entity;
    }

    public Optional<PImage> getBackgroundImage(
            Point pos) {
        if (withinBounds(pos)) {
            return Optional.of(getBackgroundCell(pos).getCurrentImage());
        } else {
            return Optional.empty();
        }
    }

    public void setBackground(
            Point pos, Background background) {
        if (withinBounds(pos)) {
            setBackgroundCell(pos, background);
        }
    }

    public Optional<Entity> getOccupant(Point pos) {
        if (isOccupied(pos)) {
            return Optional.of(getOccupancyCell(pos));
        } else {
            return Optional.empty();
        }
    }

    public void load(
            Scanner in, ImageStore imageStore) {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                if (!processLine(in.nextLine(), imageStore)) {
                    System.err.println(String.format("invalid entry on line %d",
                            lineNumber));
                }
            } catch (NumberFormatException e) {
                System.err.println(
                        String.format("invalid entry on line %d", lineNumber));
            } catch (IllegalArgumentException e) {
                System.err.println(
                        String.format("issue on line %d: %s", lineNumber,
                                e.getMessage()));
            }
            lineNumber++;
        }
    }

    private boolean processLine(
            String line, ImageStore imageStore) {
        String[] properties = line.split("\\s");
        if (properties.length > 0) {
            switch (properties[Functions.PROPERTY_KEY]) {
                case Functions.BGND_KEY:
                    return parseBackground(properties, imageStore);
                case Functions.DUDE_KEY:
                    return parseDude(properties, imageStore);
                case Functions.OBSTACLE_KEY:
                    return parseObstacle(properties, imageStore);
                case Functions.FAIRY_KEY:
                    return parseFairy(properties, imageStore);
                case Functions.HOUSE_KEY:
                    return parseHouse(properties, imageStore);
                case Functions.TREE_KEY:
                    return parseTree(properties, imageStore);
                case Functions.SAPLING_KEY:
                    return parseSapling(properties, imageStore);
                case Functions.RRH_KEY:
                    return parseRRH(properties,imageStore);
                case Functions.WHEAT_KEY:
                    return parseWheat(properties,imageStore);
                case Functions.WHEAT_SAPLING_KEY:
                    return parseWheatSapling(properties,imageStore);
                case Functions.SUGAR_KEY:
                    return parseSugar(properties, imageStore);
                case Functions.SUGAR_SAPLING_KEY:
                    return parseSugarSapling(properties, imageStore);
            }
        }

        return false;
    }


    public Background getBackgroundCell(Point pos) {
        return background[pos.y][pos.x];
    }

    public void setBackgroundCell(
            Point pos, Background background) {
        this.background[pos.y][pos.x] = background;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public Set<Entity> getEntities() {
        return entities;
    }


}
