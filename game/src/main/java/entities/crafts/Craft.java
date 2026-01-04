package entities.crafts;

import dev.gamekit.animation.Animation;
import dev.gamekit.components.*;
import dev.gamekit.core.*;
import dev.gamekit.core.Component;
import dev.gamekit.utils.Math;
import dev.gamekit.utils.Position;
import dev.gamekit.utils.ValueCallback;
import dev.gamekit.utils.Vector;
import utils.Physic;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class Craft extends Entity {
  public static boolean ENABLED = true;
  private static final double MIN_SQR_DISTANCE_FOR_WAYPOINT_MARK_THRESHOLD = 256;
  private static final double MIN_SQR_DISTANCE_FOR_WAYPOINT_SWITCH_THRESHOLD = 600;

  protected final Host host;
  protected boolean beganLandingSequence = false;

  private final List<Vector> waypoints = new ArrayList<>();
  private final Vector desiredVelocity = new Vector();
  private final Vector initialPosition = new Vector();
  private final Vector velocity = new Vector();
  private final Parameters parameters;
  private final ProximityIndicator warningIndicator = new ProximityIndicator();
  private Animation waypointOpacityAnimation;
  private boolean tracingPath = false;
  private boolean canFlip = true;
  private int waypointIndex = 0;
  private int proximityCount = 0;

  public Craft(String name, Vector initialPosition, double initialHeading, Host host) {
    super(name);
    this.host = host;
    this.initialPosition.set(initialPosition);
    this.parameters = getParameters();

    desiredVelocity.set(Vector.from(parameters.moveSpeed, initialHeading));
    velocity.set(desiredVelocity);
  }

  public void flipVelocity(Flip flip) {
    if (canFlip) {
      switch (flip) {
        case VERTICAL -> {
          desiredVelocity.setY(-desiredVelocity.y);
          velocity.setY(-velocity.y);
        }
        case HORIZONTAL -> {
          desiredVelocity.setX(-desiredVelocity.x);
          velocity.setX(-velocity.x);
        }
      }

      velocity.set(desiredVelocity);
      canFlip = false;

      Application.getInstance().scheduleTask(() -> canFlip = true, 350);
    }
  }

  public Vector getLastWaypoint() {
    if (waypoints.isEmpty()) return null;

    return waypoints.get(waypoints.size() - 1);
  }

  public boolean hasBeganLandingSequence() {
    return beganLandingSequence;
  }

  public void beginLandingSequence() {
    beganLandingSequence = true;
  }

  @Override
  protected List<Component> getComponents() {
    List<Component> components = new ArrayList<>();

    RigidBody rb = new RigidBody();
    rb.setGravityScale(0);
    components.add(rb);

    CircleCollider proximitySensor = new CircleCollider(parameters.proximityRadius);
    proximitySensor.setOffset(0, -12);
    proximitySensor.setSensor(true);
    proximitySensor.setMetaData(Physic.Tag.CRAFT_PROXIMITY);
    proximitySensor.setCollisionFilter(Physic.CategoryMask.CRAFT_PROXIMITY, Physic.CollisionMask.CRAFT_PROXIMITY);
    proximitySensor.setCollisionListener(new Physics.CollisionListener() {
      @Override
      public void onCollisionEnter(Collider otherCollider) {
        host.onCraftNearMiss();

        if (proximityCount == 0)
          addChild(warningIndicator);

        proximityCount++;
      }

      @Override
      public void onCollisionExit(Collider otherCollider) {
        proximityCount--;

        if (proximityCount <= 0)
          removeChild(warningIndicator);
      }
    });
    components.add(proximitySensor);

    return components;
  }

  @Override
  protected void start() {
    RigidBody rb = findComponent(RigidBody.class);
    rb.setPosition(initialPosition.x, initialPosition.y);
  }

  @Override
  protected void update() {
    Transform tx = findComponent(Transform.class);
    RigidBody rb = findComponent(RigidBody.class);

    if (!ENABLED) {
      rb.setLinearVelocity(0, 0);
      return;
    }

    velocity.lerpAngle(desiredVelocity, parameters.turnSpeed);
    rb.setLinearVelocity(velocity.x * parameters.moveSpeed, velocity.y * parameters.moveSpeed);
    rb.setRotation(velocity.getAngle());

    if (waypointIndex < waypoints.size()) {
      Vector pos = tx.getGlobalPosition();
      Vector currentWaypoint = waypoints.get(waypointIndex);

      if (Vector.squaredDistance(pos, currentWaypoint) <= MIN_SQR_DISTANCE_FOR_WAYPOINT_SWITCH_THRESHOLD) {
        waypointIndex++;

        if (waypointIndex == waypoints.size()) {
          waypointOpacityAnimation = new Animation(1000);

          waypointOpacityAnimation.setStateListener(state -> {
            if (state == Animation.State.ENDED && waypointOpacityAnimation.getValue() >= 1) {
              waypoints.clear();
              waypointOpacityAnimation = null;
            }
          });

          waypointOpacityAnimation.start();
        }
      } else {
        Vector vel = new Vector(currentWaypoint.x - pos.x, currentWaypoint.y - pos.y).getNormalized();
        desiredVelocity.set(vel.x, vel.y);
      }
    }

    if (Input.isButtonDown(Input.BUTTON_LMB)) {
      Position mousePos = Input.getMousePosition();
      Vector pos = Camera.screenToWorldPosition(mousePos.x, mousePos.y);

      if (rb.containsPoint(pos, Physic.CategoryMask.CRAFT_BODY)) {
        waypoints.clear();
        waypoints.add(new Vector(pos));
        tracingPath = true;
        waypointIndex = 0;
      }
    }

    if (Input.isButtonPressed(Input.BUTTON_LMB) && tracingPath && !waypoints.isEmpty()) {
      Vector lastPos = waypoints.get(waypoints.size() - 1);

      if (waypointOpacityAnimation != null && !waypointOpacityAnimation.isEnded())
        waypointOpacityAnimation.end();

      waypointOpacityAnimation = null;

      if (lastPos != null) {
        Position mousePos = Input.getMousePosition();
        Vector pos = Camera.screenToWorldPosition(mousePos.x, mousePos.y);

        if (Vector.squaredDistance(pos, lastPos) >= MIN_SQR_DISTANCE_FOR_WAYPOINT_MARK_THRESHOLD) {
          waypoints.add(new Vector(pos));
        }
      }
    }

    if (Input.isButtonReleased(Input.BUTTON_LMB)) {
      tracingPath = false;
    }
  }

  @Override
  protected void render() {
    if (!waypoints.isEmpty()) {
      for (int i = 0; i < waypoints.size() - 1; i++) {
        Vector pos1 = waypoints.get(i);
        Vector pos2 = waypoints.get(i + 1);
        Color lineColor = waypointIndex <= i ? Color.CYAN : Color.DARK_GRAY;

        Renderer.drawLine((int) pos1.x, (int) pos1.y, (int) pos2.x, (int) pos2.y)
          .withOpacity(waypointOpacityAnimation != null ? 1.0 - waypointOpacityAnimation.getValue() : 1)
          .withColor(lineColor);
      }
    }
  }

  protected void configureCollider(Collider collider) {
    configureCollider(collider, null);
  }

  protected void configureCollider(Collider collider, ValueCallback<Collider> additionalActions) {
    collider.setSensor(true);
    collider.setMetaData(Physic.Tag.CRAFT_BODY);
    collider.setCollisionFilter(Physic.CategoryMask.CRAFT_BODY, Physic.CollisionMask.CRAFT_BODY);
    collider.setCollisionListener(new Physics.CollisionListener() {
      @Override
      public void onCollisionEnter(Collider otherCollider) {
        if (otherCollider.getMetaData().equals(Physic.Tag.CRAFT_BODY)) {
          Craft otherCraft = (Craft) otherCollider.getEntity();

          if (!hasBeganLandingSequence() && !otherCraft.hasBeganLandingSequence()) {
            Transform selfTx = findComponent(Transform.class);
            Transform otherTx = otherCollider.getEntity().findComponent(Transform.class);
            Vector crashLocation = new Vector(
              0.5 * (selfTx.getGlobalPosition().x + otherTx.getGlobalPosition().x),
              0.5 * (selfTx.getGlobalPosition().y + otherTx.getGlobalPosition().y)
            );
            host.onCraftCrash(crashLocation);
          }
        }

        if (additionalActions != null)
          additionalActions.update(otherCollider);
      }
    });
  }

  protected abstract Parameters getParameters();

  public enum Flip {VERTICAL, HORIZONTAL}

  public interface Host {
    void onCraftNearMiss();

    void onCraftCrash(Vector position);

    void onCraftLanded();
  }

  protected record Parameters(double moveSpeed, double turnSpeed, double proximityRadius) { }

  private static class ProximityIndicator extends Entity {
    private static final BufferedImage SPRITE = IO.getResourceImage("warning.png");
    private static final double SPIN_SPEED = 0.015;

    private double rotation;

    public ProximityIndicator() {
      super("Indicator");
    }

    @Override
    protected List<Component> getComponents() {
      Sprite sprite = new Sprite(SPRITE);

      sprite.setWidth(160);

      return List.of(sprite);
    }

    @Override
    protected void update() {
      Transform tx = findComponent(Transform.class);
      rotation = (rotation + SPIN_SPEED) % Math.TWO_PI;
      tx.setGlobalRotation(rotation);
    }
  }
}
