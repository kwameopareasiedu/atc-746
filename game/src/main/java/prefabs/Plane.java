package prefabs;

import dev.gamekit.components.BoxCollider;
import dev.gamekit.components.Sprite;
import dev.gamekit.core.Component;
import dev.gamekit.core.IO;
import models.Craft;

import java.awt.image.BufferedImage;
import java.util.List;

public class Plane extends Craft {
  private static final BufferedImage SPRITE = IO.getResourceImage("plane.png");

  public Plane() {
    super("Plane");
  }

  @Override
  protected void setupAdditionalComponents(List<Component> components) {
    Sprite sprite = new Sprite(SPRITE);
    BoxCollider bodyCollider = new BoxCollider(18, 58);
    BoxCollider wingCollider = new BoxCollider(120, 14);

    sprite.setCenter(-3, -12);
    sprite.setWidth(128);
    bodyCollider.setOffset(0, -12);

    components.add(wingCollider);
    components.add(0, sprite);
  }

  @Override
  protected double getMoveSpeed() {
    return 1;
  }

  @Override
  protected double getTurnSpeed() {
    return 0.015;
  }
}
