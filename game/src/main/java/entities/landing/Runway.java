package entities.landing;

import dev.gamekit.components.AnimatedSprite;
import dev.gamekit.components.BoxCollider;
import dev.gamekit.components.RigidBody;
import dev.gamekit.components.Sprite;
import dev.gamekit.core.Component;
import dev.gamekit.core.Entity;
import dev.gamekit.core.IO;
import dev.gamekit.settings.ImageInterpolation;
import dev.gamekit.utils.Vector;
import scenes.Level;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class Runway extends Entity {
  private final Vector initialPosition;
  private final double initialRotation;
  private final DirectionIndicator directionIndicator;

  public Runway(String name, Vector initialPosition, double initialRotation) {
    super(name);
    this.initialPosition = initialPosition;
    this.initialRotation = initialRotation;
    directionIndicator = new DirectionIndicator();
  }

  @Override
  protected List<Component> getComponents() {
    List<Component> components = new ArrayList<>();
    Parameters parameters = getParameters();

    RigidBody rb = new RigidBody();
    components.add(rb);

    Sprite sprite = new Sprite(parameters.image, ImageInterpolation.BICUBIC);
    sprite.setWidth(72);
    components.add(sprite);

    BoxCollider collider = new BoxCollider(56, 284);
    collider.setMetaData(parameters.tag);
    collider.setCollisionFilter(parameters.categoryMask, parameters.collisionMask);
    components.add(collider);

    return components;
  }

  @Override
  protected void start() {
    RigidBody rb = findComponent(RigidBody.class);
    rb.setPosition(initialPosition.x, initialPosition.y);
    rb.setRotation(initialRotation);

    Level.getLandingIndicatorSignal().subscribe(
      getClass().getName() + "runway-landing-sub", val -> {
        logger.debug("Clicked: {}, Class: {}", val, getClass());
        if (val != null && val.equals(getClass())) {
          addChild(directionIndicator);
        } else {
          removeChild(directionIndicator);
        }
      }, false
    );
  }

  @Override
  protected void dispose() {
    Level.getLandingIndicatorSignal().unsubscribe(getClass().getName() + "runway-landing-sub");
  }

  protected abstract Parameters getParameters();

  protected record Parameters(BufferedImage image, String tag, int categoryMask, int collisionMask) { }


  private static class DirectionIndicator extends Entity {
    private static final BufferedImage SPRITE = IO.getResourceImage("runway_arrows_sheet.png");

    public DirectionIndicator() {
      super("Direction Indicator");
    }

    @Override
    protected List<Component> getComponents() {
      AnimatedSprite animatedSprite = new AnimatedSprite(
        SPRITE, 80, 300, new int[]{ 0, 0, 80, 0, 160, 0, 240, 0, 320, 0 }, 500, true
      );

      animatedSprite.setSize(32, 120);
      animatedSprite.setOffset(0, -240);
      animatedSprite.setInterpolation(ImageInterpolation.BICUBIC);

      return List.of(animatedSprite);
    }
  }
}
