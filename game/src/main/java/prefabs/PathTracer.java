package prefabs;

import dev.gamekit.core.Camera;
import dev.gamekit.core.Entity;
import dev.gamekit.core.Input;
import dev.gamekit.core.Renderer;
import dev.gamekit.utils.Position;
import dev.gamekit.utils.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PathTracer extends Entity {
  private static final PathTracer INSTANCE = new PathTracer();
  private static final double SQUARED_DISTANCE_THRESHOLD = 4096;

  private final List<Vector> points = new ArrayList<>();
  private boolean enabled = true;

  private PathTracer() {
    super("Path Tracer");
  }

  public static PathTracer getInstance() {
    return INSTANCE;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  protected void update() {
    if (!enabled) return;

    if (Input.isButtonDown(Input.BUTTON_LMB) && points.isEmpty()) {
      Position mousePos = Input.getMousePosition();
      Vector pos = Camera.screenToWorldPosition(mousePos.x, mousePos.y);
      points.clear();
      points.add(new Vector(pos));
    }

    if (Input.isButtonPressed(Input.BUTTON_LMB) && !points.isEmpty()) {
      Vector lastPos = points.get(points.size() - 1);

      if (lastPos != null) {
        Position mousePos = Input.getMousePosition();
        Vector pos = Camera.screenToWorldPosition(mousePos.x, mousePos.y);

        if (Vector.squaredDistance(pos, lastPos) >= SQUARED_DISTANCE_THRESHOLD) {
          points.add(new Vector(pos));
        }
      }
    }

    if (Input.isButtonReleased(Input.BUTTON_LMB)) {
      points.clear();
    }

    if (!points.isEmpty()) {
      logger.debug("Points: {}", points.size());
    }
  }

  @Override
  protected void render() {
    if (!points.isEmpty()) {
      for (int i = 0; i < points.size() - 1; i++) {
        Vector pos1 = points.get(i);
        Vector pos2 = points.get(i + 1);

        Renderer.drawLine((int) pos1.x, (int) pos1.y, (int) pos2.x, (int) pos2.y).withColor(Color.CYAN);
      }
    }
  }
}
