package models;

import dev.gamekit.components.CircleCollider;
import dev.gamekit.components.RigidBody;
import dev.gamekit.components.Transform;
import dev.gamekit.core.*;
import dev.gamekit.core.Component;
import dev.gamekit.utils.Position;
import dev.gamekit.utils.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Craft extends Entity {
  public static boolean TRACING_ENABLED = true;
  private static final double SQUARED_WAYPOINT_MARK_THRESHOLD = 256;
  private static final double SQUARED_MIN_WAYPOINT_DISTANCE_THRESHOLD = 600;

  private final List<Vector> waypoints = new ArrayList<>();
  private final Vector desiredVelocity = new Vector();
  private final Vector velocity = new Vector(0, getMoveSpeed());
  private boolean tracingPath = false;
  private int waypointIndex = 0;

  public Craft(String name) {
    super(name);
  }

  @Override
  protected void update() {
    Transform tx = findComponent(Transform.class);
    RigidBody rb = findComponent(RigidBody.class);

    velocity.lerpAngle(desiredVelocity, getTurnSpeed());
    rb.setLinearVelocity(velocity.x * getMoveSpeed(), velocity.y * getMoveSpeed());
    rb.setRotation(velocity.getAngle());

    if (waypointIndex < waypoints.size()) {
      Vector pos = tx.getGlobalPosition();
      Vector currentWaypoint = waypoints.get(waypointIndex);

      if (Vector.squaredDistance(pos, currentWaypoint) <= SQUARED_MIN_WAYPOINT_DISTANCE_THRESHOLD) {
        logger.debug("Moving to next waypoint");
        waypointIndex++;
      } else {
        Vector vel = new Vector(currentWaypoint.x - pos.x, currentWaypoint.y - pos.y).getNormalized();
        desiredVelocity.set(vel.x, vel.y);
      }
    } else {
      waypoints.clear();
    }

    if (Input.isButtonDown(Input.BUTTON_LMB) && TRACING_ENABLED) {
      Position mousePos = Input.getMousePosition();
      Vector pos = Camera.screenToWorldPosition(mousePos.x, mousePos.y);

      if (rb.containsPoint(pos)) {
        waypoints.clear();
        waypoints.add(new Vector(pos));
        tracingPath = true;
        waypointIndex = 0;
      }
    }

    if (Input.isButtonPressed(Input.BUTTON_LMB) && TRACING_ENABLED && tracingPath) {
      Vector lastPos = waypoints.get(waypoints.size() - 1);

      if (lastPos != null) {
        Position mousePos = Input.getMousePosition();
        Vector pos = Camera.screenToWorldPosition(mousePos.x, mousePos.y);

        if (Vector.squaredDistance(pos, lastPos) >= SQUARED_WAYPOINT_MARK_THRESHOLD) {
          waypoints.add(new Vector(pos));
        }
      }
    }

    if (Input.isButtonReleased(Input.BUTTON_LMB)) {
      tracingPath = false;
    }
  }

  @Override
  protected final List<Component> getComponents() {
    RigidBody rb = new RigidBody();
    CircleCollider proximitySensor = new CircleCollider(128);

    proximitySensor.setOffset(0, -12);
    proximitySensor.setSensor(true);

    List<Component> components = new ArrayList<>();
    setupAdditionalComponents(components);
    components.add(proximitySensor);
    components.add(rb);

    return components;
  }

  @Override
  protected void render() {
    if (!waypoints.isEmpty()) {
      for (int i = 0; i < waypoints.size() - 1; i++) {
        Vector pos1 = waypoints.get(i);
        Vector pos2 = waypoints.get(i + 1);
        Color lineColor = waypointIndex <= i ? Color.CYAN : Color.DARK_GRAY;

        Renderer.drawLine((int) pos1.x, (int) pos1.y, (int) pos2.x, (int) pos2.y).withColor(lineColor);
      }
    }
  }

  protected void setupAdditionalComponents(List<Component> components) { /* No-op */ }

  protected abstract double getMoveSpeed();

  protected abstract double getTurnSpeed();
}
