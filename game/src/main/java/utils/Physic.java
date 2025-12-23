package utils;

public class Physic {
  private Physic() { }

  public static class Tag {
    public static final String DESTROYER = "destroyer";
    public static final String CRAFT_PROXIMITY = "craft-proximity";
    public static final String CRAFT_BODY = "craft-body";

    private Tag() { }
  }

  public static class CategoryMask {
    public static final int DESTROYER = 0x1;
    public static final int CRAFT_PROXIMITY = 0x2;
    public static final int CRAFT_BODY = 0x4;

    private CategoryMask() { }
  }

  public static class CollisionMask {
    public static final int DESTROYER = CategoryMask.CRAFT_BODY;
    public static final int CRAFT_PROXIMITY = CategoryMask.CRAFT_BODY;
    public static final int CRAFT_BODY =
      CategoryMask.CRAFT_BODY | CategoryMask.DESTROYER | CategoryMask.CRAFT_PROXIMITY;

    private CollisionMask() { }
  }
}
