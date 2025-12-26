package entities;

import dev.gamekit.components.Sprite;
import dev.gamekit.components.Transform;
import dev.gamekit.core.Component;
import dev.gamekit.core.Entity;
import dev.gamekit.core.IO;
import dev.gamekit.utils.Vector;

import java.awt.image.BufferedImage;
import java.util.List;

public class Explosion extends Entity {
  private static final BufferedImage SPRITE = IO.getResourceImage("boom.png");
  private final Vector initialLocation;

  public Explosion(Vector initialLocation) {
    super("Explosion");
    this.initialLocation = initialLocation;
  }

  @Override
  protected List<Component> getComponents() {
    Sprite sprite = new Sprite(SPRITE);

    sprite.setWidth(192);

    return List.of(sprite);
  }

  @Override
  protected void start() {
    findComponent(Transform.class).setGlobalPosition(initialLocation.x, initialLocation.y);
  }
}
