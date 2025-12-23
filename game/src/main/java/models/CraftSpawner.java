package models;

import dev.gamekit.core.Application;
import dev.gamekit.settings.Resolution;
import dev.gamekit.utils.Bounds;
import dev.gamekit.utils.Vector;
import entities.crafts.Craft;

import java.util.Random;

public class CraftSpawner {
  private static final double EDGE_OFFSET = 128;

  private final Craft.Host craftHost;
  private final SpawnRegion[] spawnRegions;
  private final Creator[] craftCreators;
  private final Random rnd = new Random();

  public CraftSpawner(Craft.Host craftHost, Creator... craftCreators) {
    this.craftHost = craftHost;
    this.craftCreators = craftCreators;

    Resolution resolution = Application.getInstance().getSettings().resolution;
    double left = resolution.width() / 2.0;
    double top = resolution.height() / 2.0;
    double width = resolution.width() / 4.0;
    double height = resolution.height() / 3.0;

    spawnRegions = new SpawnRegion[]{
      // Segments on top of screen
      new SpawnRegion(0, new int[]{ 5, 6, 7, 8, 9, 10 }, 0 * width - left, top + EDGE_OFFSET, width, 25),
      new SpawnRegion(1, new int[]{ 6, 7, 8, 9, 10, 11 }, 1 * width - left, top + EDGE_OFFSET, width, 25),
      new SpawnRegion(2, new int[]{ 7, 8, 9, 10, 11, 12 }, 2 * width - left, top + EDGE_OFFSET, width, 25),
      new SpawnRegion(3, new int[]{ 8, 9, 10, 11, 12, 13 }, 3 * width - left, top + EDGE_OFFSET, width, 25),
      // Segments to the right of screen
      new SpawnRegion(4, new int[]{ 9, 10, 11, 12, 13, 0 }, EDGE_OFFSET + left - 25, 0 * height - top, 25, height),
      new SpawnRegion(5, new int[]{ 10, 11, 12, 13, 0, 1 }, EDGE_OFFSET + left - 25, 1 * height - top, 25, height),
      new SpawnRegion(6, new int[]{ 11, 12, 13, 0, 1, 2 }, EDGE_OFFSET + left - 25, 2 * height - top, 25, height),
      // Segments below screen
      new SpawnRegion(7, new int[]{ 12, 13, 0, 1, 2, 3 }, 3 * width - left, 25 - (top + EDGE_OFFSET), width, 25),
      new SpawnRegion(8, new int[]{ 13, 0, 1, 2, 3, 4 }, 2 * width - left, 25 - (top + EDGE_OFFSET), width, 25),
      new SpawnRegion(9, new int[]{ 0, 1, 2, 3, 4, 5 }, 1 * width - left, 25 - (top + EDGE_OFFSET), width, 25),
      new SpawnRegion(10, new int[]{ 1, 2, 3, 4, 5, 6 }, 0 * width - left, 25 - (top + EDGE_OFFSET), width, 25),
      // Segments to the left of screen
      new SpawnRegion(11, new int[]{ 2, 3, 4, 5, 6, 7 }, -(EDGE_OFFSET + left), 2 * height - top, 25, height),
      new SpawnRegion(12, new int[]{ 3, 4, 5, 6, 7, 8 }, -(EDGE_OFFSET + left), 1 * height - top, 25, height),
      new SpawnRegion(13, new int[]{ 4, 5, 6, 7, 8, 9 }, -(EDGE_OFFSET + left), 0 * height - top, 25, height),
    };
  }

  public Craft spawnCraft() {
    CraftSpawner.Creator craftCreator = craftCreators[rnd.nextInt(craftCreators.length)];
    SpawnRegion spawnRegion = spawnRegions[rnd.nextInt(spawnRegions.length)];
    SpawnRegion targetRegion = spawnRegions[spawnRegion.destIds[rnd.nextInt(spawnRegion.destIds.length)]];

    Vector spawnPosition = new Vector(
      rnd.nextDouble(spawnRegion.x, spawnRegion.x + spawnRegion.width),
      rnd.nextDouble(spawnRegion.y, spawnRegion.y + spawnRegion.height)
    );

    Vector targetPosition = new Vector(
      rnd.nextDouble(targetRegion.x, targetRegion.x + targetRegion.width),
      rnd.nextDouble(targetRegion.y, targetRegion.y + targetRegion.height)
    );

    double heading = Vector.angle(spawnPosition, targetPosition);
    return craftCreator.create(spawnPosition, heading, craftHost);
  }

  public interface Creator {
    Craft create(Vector initialPosition, double initialHeading, Craft.Host host);
  }

  private static class SpawnRegion extends Bounds {
    final int id;
    final int[] destIds;

    private SpawnRegion(int id, int[] destIds, double x, double y, double width, double height) {
      super(x, y, width, height);
      this.id = id;
      this.destIds = destIds;
    }
  }
}
