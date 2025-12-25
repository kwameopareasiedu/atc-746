package utils;

public class Physic {
  private Physic() { }

  public static class Tag {
    public static final String ENCLOSURE_VERTICAL_WALL = "enclosure-vertical-wall";
    public static final String ENCLOSURE_HORIZONTAL_WALL = "enclosure-horizontal-wall";
    public static final String CRAFT_PROXIMITY = "craft-proximity";
    public static final String CRAFT_BODY = "craft-body";
    public static final String RUNWAY_STRIP = "runway-strip";

    private Tag() { }
  }

  public static class CategoryMask {
    public static final int ENCLOSURE_WALL = 1;
    public static final int CRAFT_PROXIMITY = 2;
    public static final int CRAFT_BODY = 4;
    public static final int RUNWAY_STRIP = 8;

    private CategoryMask() { }
  }

  public static class CollisionMask {
    public static final int ENCLOSURE_WALL = CategoryMask.CRAFT_BODY;
    public static final int CRAFT_PROXIMITY = CategoryMask.CRAFT_BODY;
    public static final int CRAFT_BODY = CategoryMask.ENCLOSURE_WALL
      | CategoryMask.CRAFT_PROXIMITY
      | CategoryMask.CRAFT_BODY
      | CategoryMask.RUNWAY_STRIP;
    public static final int RUNWAY_STRIP = CategoryMask.CRAFT_BODY;

    private CollisionMask() { }
  }
}
