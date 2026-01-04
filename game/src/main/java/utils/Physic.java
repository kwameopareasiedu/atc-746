package utils;

public class Physic {
  private Physic() { }

  public static class Tag {
    public static final String ENCLOSURE_VERTICAL_WALL = "enclosure-vertical-wall";
    public static final String ENCLOSURE_HORIZONTAL_WALL = "enclosure-horizontal-wall";
    public static final String ENCLOSURE_INDICATOR = "enclosure-indicator";
    public static final String CRAFT_PROXIMITY = "craft-proximity";
    public static final String CRAFT_BODY = "craft-body";
    public static final String AIRSTRIP = "airstrip";
    public static final String JETSTRIP = "jetstrip";
    public static final String SEAPORT = "seaport";
    public static final String HELIPAD = "helipad";
    public static final String BLIMPAD = "blimpad";

    private Tag() { }
  }

  public static class CategoryMask {
    public static final int ENCLOSURE_WALL = 1;
    public static final int ENCLOSURE_INDICATOR = 2;
    public static final int CRAFT_PROXIMITY = 4;
    public static final int CRAFT_BODY = 8;
    public static final int AIRSTRIP = 16;
    public static final int JETSTRIP = 32;
    public static final int SEAPORT = 64;
    public static final int HELIPAD = 128;
    public static final int BLIMPAD = 256;

    private CategoryMask() { }
  }

  public static class CollisionMask {
    public static final int ENCLOSURE_WALL = CategoryMask.CRAFT_BODY;
    public static final int ENCLOSURE_INDICATOR = 0;
    public static final int CRAFT_PROXIMITY = CategoryMask.CRAFT_BODY;
    public static final int CRAFT_BODY =
      CategoryMask.ENCLOSURE_WALL
        | CategoryMask.CRAFT_PROXIMITY
        | CategoryMask.CRAFT_BODY
        | CategoryMask.AIRSTRIP
        | CategoryMask.JETSTRIP
        | CategoryMask.SEAPORT
        | CategoryMask.HELIPAD
        | CategoryMask.BLIMPAD;
    public static final int AIRSTRIP = CategoryMask.CRAFT_BODY;
    public static final int JETSTRIP = CategoryMask.CRAFT_BODY;
    public static final int SEAPORT = CategoryMask.CRAFT_BODY;
    public static final int HELIPAD = CategoryMask.CRAFT_BODY;
    public static final int BLIMPAD = CategoryMask.CRAFT_BODY;

    private CollisionMask() { }
  }
}
