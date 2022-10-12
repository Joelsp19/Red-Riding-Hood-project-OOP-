import java.util.*;

import processing.core.PImage;
import processing.core.PApplet;

/**
 * This class contains many functions written in a procedural style.
 * You will reduce the size of this class over the next several weeks
 * by refactoring this codebase to follow an OOP style.
 */
public final class Functions {
    public static final Random rand = new Random();

    public static final int PROPERTY_KEY = 0;

    public static final List<String> PATH_KEYS = new ArrayList<>(Arrays.asList("bridge", "dirt", "dirt_horiz", "dirt_vert_left", "dirt_vert_right",
            "dirt_bot_left_corner", "dirt_bot_right_up", "dirt_vert_left_bot"));

    public static final String SAPLING_KEY = "sapling";
    public static final int SAPLING_NUM_PROPERTIES = 4;
    public static final int SAPLING_ID = 1;
    public static final int SAPLING_COL = 2;
    public static final int SAPLING_ROW = 3;

    public static final String BGND_KEY = "background";
    public static final int BGND_NUM_PROPERTIES = 4;
    public static final int BGND_ID = 1;
    public static final int BGND_COL = 2;
    public static final int BGND_ROW = 3;

    public static final String OBSTACLE_KEY = "obstacle";
    public static final int OBSTACLE_NUM_PROPERTIES = 5;
    public static final int OBSTACLE_ID = 1;
    public static final int OBSTACLE_COL = 2;
    public static final int OBSTACLE_ROW = 3;
    public static final int OBSTACLE_ANIMATION_PERIOD = 4;

    public static final String DUDE_KEY = "dude";
    public static final int DUDE_NUM_PROPERTIES = 7;
    public static final int DUDE_ID = 1;
    public static final int DUDE_COL = 2;
    public static final int DUDE_ROW = 3;
    public static final int DUDE_LIMIT = 4;
    public static final int DUDE_ACTION_PERIOD = 5;
    public static final int DUDE_ANIMATION_PERIOD = 6;

    public static final String HOUSE_KEY = "house";
    public static final int HOUSE_NUM_PROPERTIES = 4;
    public static final int HOUSE_ID = 1;
    public static final int HOUSE_COL = 2;
    public static final int HOUSE_ROW = 3;

    public static final String FAIRY_KEY = "fairy";
    public static final int FAIRY_NUM_PROPERTIES = 6;
    public static final int FAIRY_ID = 1;
    public static final int FAIRY_COL = 2;
    public static final int FAIRY_ROW = 3;
    public static final int FAIRY_ANIMATION_PERIOD = 4;
    public static final int FAIRY_ACTION_PERIOD = 5;

    public static final String STUMP_KEY = "stump";

    public static final String TREE_KEY = "tree";
    public static final int TREE_NUM_PROPERTIES = 7;
    public static final int TREE_ID = 1;
    public static final int TREE_COL = 2;
    public static final int TREE_ROW = 3;
    public static final int TREE_ANIMATION_PERIOD = 4;
    public static final int TREE_ACTION_PERIOD = 5;
    public static final int TREE_HEALTH = 6;

    public static final int TREE_ANIMATION_MAX = 600;
    public static final int TREE_ANIMATION_MIN = 50;
    public static final int TREE_ACTION_MAX = 1400;
    public static final int TREE_ACTION_MIN = 1000;
    public static final int TREE_HEALTH_MAX = 3;
    public static final int TREE_HEALTH_MIN = 1;

    public static final String RRH_KEY = "rrh";
    public static final int RRH_NUM_PROPERTIES = 7;
    public static final int RRH_ID = 1;
    public static final int RRH_COL = 2;
    public static final int RRH_ROW = 3;
    public static final int RRH_COUNT = 4;
    public static final int RRH_ACTION_PERIOD = 5;
    public static final int RRH_ANIMATION_PERIOD = 6;


    public static final String WHEAT_STUMP_KEY = "wheatStump";

    public static final String WHEAT_KEY = "wheat";
    public static final int WHEAT_NUM_PROPERTIES = 7;
    public static final int WHEAT_ID = 1;
    public static final int WHEAT_COL = 2;
    public static final int WHEAT_ROW = 3;
    public static final int WHEAT_ANIMATION_PERIOD = 4;
    public static final int WHEAT_ACTION_PERIOD = 5;
    public static final int WHEAT_HEALTH = 6;

    public static final int WHEAT_ANIMATION_MAX = 600;
    public static final int WHEAT_ANIMATION_MIN = 50;
    public static final int WHEAT_ACTION_MAX = 1400;
    public static final int WHEAT_ACTION_MIN = 1000;
    public static final int WHEAT_HEALTH_MAX = 3;
    public static final int WHEAT_HEALTH_MIN = 1;

    public static final String WHEAT_SAPLING_KEY = "wheatSapling";
    public static final int WHEAT_SAPLING_NUM_PROPERTIES = 4;
    public static final int WHEAT_SAPLING_ID = 1;
    public static final int WHEAT_SAPLING_COL = 2;
    public static final int WHEAT_SAPLING_ROW = 3;


    public static final String SUGAR_STUMP_KEY = "sugarStump";

    public static final String SUGAR_KEY = "sugar";
    public static final int SUGAR_NUM_PROPERTIES = 7;
    public static final int SUGAR_ID = 1;
    public static final int SUGAR_COL = 2;
    public static final int SUGAR_ROW = 3;
    public static final int SUGAR_ANIMATION_PERIOD = 4;
    public static final int SUGAR_ACTION_PERIOD = 5;
    public static final int SUGAR_HEALTH = 6;

    public static final int SUGAR_ANIMATION_MAX = 600;
    public static final int SUGAR_ANIMATION_MIN = 50;
    public static final int SUGAR_ACTION_MAX = 1400;
    public static final int SUGAR_ACTION_MIN = 1000;
    public static final int SUGAR_HEALTH_MAX = 3;
    public static final int SUGAR_HEALTH_MIN = 1;

    public static final String SUGAR_SAPLING_KEY = "sugarSapling";
    public static final int SUGAR_SAPLING_NUM_PROPERTIES = 4;
    public static final int SUGAR_SAPLING_ID = 1;
    public static final int SUGAR_SAPLING_COL = 2;
    public static final int SUGAR_SAPLING_ROW = 3;

    public static final String CLOUD_KEY = "cloud";
    public static final String WOLF_KEY = "wolf";

    public static final String GUARDIAN_KEY = "guardian";
    public static final String GHOUSE_KEY = "gHouse";




}
