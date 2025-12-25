package utils;

public class Physic {
  private Physic() { }

  public static class Tag {
    public static final String ENCLOSURE_VERTICAL_WALL = "enclosure-vertical-wall";
    public static final String ENCLOSURE_HORIZONTAL_WALL = "enclosure-horizontal-wall";
    public static final String CRAFT_PROXIMITY = "craft-proximity";
    public static final String CRAFT_BODY = "craft-body";
    public static final String AIRSTRIP = "airstrip";
    public static final String JETSTRIP = "jetstrip";
    public static final String HELIPAD = "helipad";

    private Tag() { }
  }

  public static class CategoryMask {
    public static final int ENCLOSURE_WALL = 1;
    public static final int CRAFT_PROXIMITY = 2;
    public static final int CRAFT_BODY = 4;
    public static final int AIRSTRIP = 8;
    public static final int JETSTRIP = 16;
    public static final int HELIPAD = 32;

    private CategoryMask() { }
  }

  public static class CollisionMask {
    public static final int ENCLOSURE_WALL = CategoryMask.CRAFT_BODY;
    public static final int CRAFT_PROXIMITY = CategoryMask.CRAFT_BODY;
    public static final int CRAFT_BODY =
      CategoryMask.ENCLOSURE_WALL
      | CategoryMask.CRAFT_PROXIMITY
      | CategoryMask.CRAFT_BODY
      | CategoryMask.AIRSTRIP
      | CategoryMask.JETSTRIP
      | CategoryMask.HELIPAD;
    public static final int AIRSTRIP = CategoryMask.CRAFT_BODY;
    public static final int JETSTRIP = CategoryMask.CRAFT_BODY;
    public static final int HELIPAD = CategoryMask.CRAFT_BODY;

    private CollisionMask() { }
  }
}
