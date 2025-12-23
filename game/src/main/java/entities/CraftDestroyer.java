package entities;

import dev.gamekit.components.BoxCollider;
import dev.gamekit.components.RigidBody;
import dev.gamekit.core.Application;
import dev.gamekit.core.Component;
import dev.gamekit.core.Entity;
import dev.gamekit.settings.Resolution;
import utils.Physic;

import java.util.List;

public class CraftDestroyer extends Entity {
  private static final double EDGE_OFFSET = 256;

  public CraftDestroyer() {
    super("Craft Destroyer");
  }

  @Override
  protected List<Component> getComponents() {
    Resolution resolution = Application.getInstance().getSettings().resolution;
    double halfWidth = 0.5 * resolution.width();
    double halfHeight = 0.5 * resolution.height();
    double doubleWidth = 2.0 * resolution.width();
    double doubleHeight = 2.0 * resolution.height();

    RigidBody rb = new RigidBody();
    BoxCollider topWall = new BoxCollider(doubleWidth, 25);
    BoxCollider rightWall = new BoxCollider(25, doubleHeight);
    BoxCollider bottomWall = new BoxCollider(doubleWidth, 25);
    BoxCollider leftWall = new BoxCollider(25, doubleHeight);

    topWall.setOffset(0, halfHeight + EDGE_OFFSET);
    rightWall.setOffset(halfWidth + EDGE_OFFSET, 0);
    bottomWall.setOffset(0, -(halfHeight + EDGE_OFFSET));
    leftWall.setOffset(-(halfWidth + EDGE_OFFSET), 0);

    BoxCollider[] walls = new BoxCollider[]{ topWall, rightWall, bottomWall, leftWall };

    for (BoxCollider wall : walls) {
      wall.setMetaData(Physic.Tag.DESTROYER);
      wall.setCollisionFilter(Physic.CategoryMask.DESTROYER, Physic.CollisionMask.DESTROYER);
    }

    return List.of(rb, topWall, rightWall, bottomWall, leftWall);
  }
}
