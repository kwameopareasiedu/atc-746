package entities;

import dev.gamekit.components.*;
import dev.gamekit.core.*;
import dev.gamekit.core.Component;
import dev.gamekit.settings.ImageInterpolation;
import dev.gamekit.settings.Resolution;
import dev.gamekit.utils.Bounds;
import dev.gamekit.utils.Vector;
import entities.crafts.Craft;
import utils.Physic;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

public class Enclosure extends Entity {
  private static final double SPAWN_EDGE_OFFSET = 128;
  private static final double WALL_EDGE_OFFSET = 256;
  private static final double INDICATOR_EDGE_OFFSET = -96;

  private final Craft.Host craftHost;
  private final Region[] spawnRegions;
  private final Craft.Creator[] craftCreators;
  private final Random rnd = new Random();

  public Enclosure(Craft.Host craftHost, Craft.Creator... craftCreators) {
    super("Enclosure");
    this.craftHost = craftHost;
    this.craftCreators = craftCreators;

    Resolution resolution = Application.getInstance().getSettings().resolution;
    double halfScreenWidth = 0.5 * resolution.width() + SPAWN_EDGE_OFFSET;
    double halfScreenHeight = 0.5 * resolution.height() + SPAWN_EDGE_OFFSET;

    spawnRegions = new Region[]{
      new Region(0, 2, 0, halfScreenHeight, resolution.width(), 25),
      new Region(1, 3, halfScreenWidth, 0, 25, resolution.height()),
      new Region(2, 0, 0, -halfScreenHeight, resolution.width(), 25),
      new Region(3, 1, -halfScreenWidth, 0, 25, resolution.height()),
    };
  }

  public Craft spawnCraft() {
    Craft.Creator craftCreator = craftCreators[rnd.nextInt(craftCreators.length)];
    Region spawnRegion = spawnRegions[rnd.nextInt(spawnRegions.length)];
    Region targetRegion = spawnRegions[spawnRegion.adjacentIdx];

    Vector spawnPosition = new Vector(
      rnd.nextDouble(spawnRegion.x - 0.5 * spawnRegion.width, spawnRegion.x + 0.5 * spawnRegion.width),
      rnd.nextDouble(spawnRegion.y - 0.5 * spawnRegion.height, spawnRegion.y + 0.5 * spawnRegion.height)
    );

    Vector targetPosition = new Vector(
      rnd.nextDouble(targetRegion.x - 0.5 * targetRegion.width, targetRegion.x + 0.5 * targetRegion.width),
      rnd.nextDouble(targetRegion.y - 0.5 * targetRegion.height, targetRegion.y + 0.5 * targetRegion.height)
    );

    double heading = Vector.angle(spawnPosition, targetPosition);

    List<Physics.RaycastHit> hits =
      Physics.raycast(spawnPosition, heading, 500, Physic.CategoryMask.ENCLOSURE_INDICATOR);

    if (!hits.isEmpty()) {
      Physics.RaycastHit hit = hits.get(0);
      Indicator indicator = new Indicator();

      indicator.findComponent(Transform.class).setGlobalPosition(hit.point().x, hit.point().y);
      addChild(indicator);
    }

    return craftCreator.create(spawnPosition, heading, craftHost);
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
    BoxCollider indicatorRegion = new BoxCollider(resolution.width() + INDICATOR_EDGE_OFFSET,
      resolution.height() + INDICATOR_EDGE_OFFSET);

    topWall.setSensor(true);
    topWall.setOffset(0, halfHeight + WALL_EDGE_OFFSET);
    topWall.setMetaData(Physic.Tag.ENCLOSURE_VERTICAL_WALL);
    topWall.setCollisionFilter(Physic.CategoryMask.ENCLOSURE_WALL, Physic.CollisionMask.ENCLOSURE_WALL);
    topWall.setCollisionListener(new Physics.CollisionListener() {
      @Override
      public void onCollisionEnter(Collider otherCollider) {
        if (otherCollider.getMetaData() == Physic.Tag.CRAFT_BODY)
          ((Craft) otherCollider.getEntity()).flipVelocity(Craft.Flip.VERTICAL);
      }
    });

    rightWall.setSensor(true);
    rightWall.setOffset(halfWidth + WALL_EDGE_OFFSET, 0);
    rightWall.setMetaData(Physic.Tag.ENCLOSURE_HORIZONTAL_WALL);
    rightWall.setCollisionFilter(Physic.CategoryMask.ENCLOSURE_WALL, Physic.CollisionMask.ENCLOSURE_WALL);
    rightWall.setCollisionListener(new Physics.CollisionListener() {
      @Override
      public void onCollisionEnter(Collider otherCollider) {
        if (otherCollider.getMetaData() == Physic.Tag.CRAFT_BODY)
          ((Craft) otherCollider.getEntity()).flipVelocity(Craft.Flip.HORIZONTAL);
      }
    });

    bottomWall.setSensor(true);
    bottomWall.setOffset(0, -(halfHeight + WALL_EDGE_OFFSET));
    bottomWall.setMetaData(Physic.Tag.ENCLOSURE_VERTICAL_WALL);
    bottomWall.setCollisionFilter(Physic.CategoryMask.ENCLOSURE_WALL, Physic.CollisionMask.ENCLOSURE_WALL);
    bottomWall.setCollisionListener(new Physics.CollisionListener() {
      @Override
      public void onCollisionEnter(Collider otherCollider) {
        if (otherCollider.getMetaData() == Physic.Tag.CRAFT_BODY)
          ((Craft) otherCollider.getEntity()).flipVelocity(Craft.Flip.VERTICAL);
      }
    });

    leftWall.setSensor(true);
    leftWall.setOffset(-(halfWidth + WALL_EDGE_OFFSET), 0);
    leftWall.setMetaData(Physic.Tag.ENCLOSURE_HORIZONTAL_WALL);
    leftWall.setCollisionFilter(Physic.CategoryMask.ENCLOSURE_WALL, Physic.CollisionMask.ENCLOSURE_WALL);
    leftWall.setCollisionListener(new Physics.CollisionListener() {
      @Override
      public void onCollisionEnter(Collider otherCollider) {
        if (otherCollider.getMetaData() == Physic.Tag.CRAFT_BODY)
          ((Craft) otherCollider.getEntity()).flipVelocity(Craft.Flip.HORIZONTAL);
      }
    });

    indicatorRegion.setSensor(true);
    indicatorRegion.setCollisionFilter(
      Physic.CategoryMask.ENCLOSURE_INDICATOR,
      Physic.CollisionMask.ENCLOSURE_INDICATOR
    );

    return List.of(rb, topWall, rightWall, bottomWall, leftWall, indicatorRegion);
  }

  @Override
  protected void render() {
    for (Region region : spawnRegions) {
      Renderer.drawRect((int) region.x, (int) region.y, (int) region.width, (int) region.height)
        .withColor(Color.WHITE);
    }
  }

  private static class Region extends Bounds {
    final int idx;
    final int adjacentIdx;

    private Region(int idx, int adjacentIdx, double x, double y, double width, double height) {
      super(x, y, width, height);
      this.idx = idx;
      this.adjacentIdx = adjacentIdx;
    }
  }

  private static class Indicator extends Entity {
    private static final BufferedImage SPRITE = IO.getResourceImage("incoming.png");

    public Indicator() {
      super("Indicator");
    }

    @Override
    protected List<Component> getComponents() {
      Sprite sprite = new Sprite(SPRITE, ImageInterpolation.BICUBIC);
      sprite.setWidth(48);

      return List.of(sprite);
    }

    @Override
    protected void start() {
      Application.getInstance().scheduleTask(this::destroy, 2000);
    }
  }
}
