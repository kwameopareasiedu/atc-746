package ui;

import dev.gamekit.animation.Animation;
import dev.gamekit.animation.AnimationCurve;
import dev.gamekit.animation.AnimationSlice;
import dev.gamekit.core.Application;
import dev.gamekit.core.IO;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.enums.MainAxisAlignment;
import dev.gamekit.ui.events.MouseEvent;
import dev.gamekit.ui.widgets.*;
import scenes.TestScene;

import java.awt.image.BufferedImage;

import static dev.gamekit.utils.Math.degToRad;

public class LevelFailedPopup extends Compose {
  private static final BufferedImage NEWSPAPER_IMG = IO.getResourceImage("newspaper.png");

  private final Animation animation;
  private final AnimationSlice newspaperSlice;
  private final AnimationSlice buttonSlice;
  private boolean started = false;

  public LevelFailedPopup() {
    animation = new Animation(2500);
    animation.setValueListener(value -> updateUI());

    newspaperSlice = new AnimationSlice(animation, AnimationCurve.EASE_OUT_SINE, 0, 1500);
    buttonSlice = new AnimationSlice(animation, AnimationCurve.EASE_OUT_BOUNCE, 750, 1000);
  }

  public static LevelFailedPopup create() {
    return new LevelFailedPopup();
  }

  @Override
  protected void performPostLayout() {
    super.performPostLayout();

    if (!started) {
      Application.getInstance().scheduleTask(animation::start, 1000);
      started = true;
    }
  }

  @Override
  protected Widget build() {
    return Stack.create(
      Center.create(
        Rotated.create(
          newspaperSlice.getValue() * degToRad(1080),
          Scaled.create(
            Math.max(0.01, newspaperSlice.getValue()),
            Opacity.create(
              newspaperSlice.getValue(),
              Image.create(NEWSPAPER_IMG)
            )
          )
        )
      ),

      Center.create(
        Padding.create(
          600 + (int) (buttonSlice.getValue() * 45), 0, 0, 0,
          Opacity.create(
            buttonSlice.getValue(),
            Row.create(
              (props) -> {
                props.mainAxisAlignment = MainAxisAlignment.CENTER;
                props.crossAxisAlignment = CrossAxisAlignment.CENTER;
                props.gapSize = 16;
              },
              Button.create(
                props -> props.mouseListener = e -> {
                  if (e.type == MouseEvent.Type.CLICK) {
                    Application.getInstance().scheduleTask(
                      () -> Application.getInstance().loadScene(new TestScene())
                    );
                  }
                },
                Padding.create(
                  12, 12, 24, 12,
                  Text.create("Restart")
                )
              ),
              Button.create(
                props -> props.mouseListener = e -> {
                  if (e.type == MouseEvent.Type.CLICK) {
                    Application.getInstance().scheduleTask(
                      () -> Application.getInstance().loadScene(new TestScene())
                    );
                  }
                },
                Padding.create(
                  12, 12, 24, 12,
                  Text.create("Main Menu")
                )
              )
            )
          )
        )
      )
    );
  }

  @Override
  protected void performUnmount() {
    super.performUnmount();

    animation.end();
  }
}
