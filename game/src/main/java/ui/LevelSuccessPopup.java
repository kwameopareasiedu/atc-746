package ui;

import dev.gamekit.animation.Animation;
import dev.gamekit.animation.AnimationCurve;
import dev.gamekit.animation.AnimationSlice;
import dev.gamekit.core.Application;
import dev.gamekit.core.IO;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.widgets.*;

import java.awt.image.BufferedImage;

import static dev.gamekit.utils.Math.degToRad;

public class LevelSuccessPopup extends Compose {
  private static final BufferedImage SUCCESS_BANNER_IMG = IO.getResourceImage("success_banner.png");
  private static final BufferedImage PANEL_CONTAINER_IMG = IO.getResourceImage("panel_container.png");

  private final Animation animation;
  private final AnimationSlice rotationSlice;
  private final AnimationSlice scaleSlice;
  private boolean started = false;

  public LevelSuccessPopup() {
    animation = new Animation(2000);
    animation.setValueListener(value -> updateUI());

    rotationSlice = new AnimationSlice(animation, AnimationCurve.LINEAR, 0, 500);
    scaleSlice = new AnimationSlice(animation, AnimationCurve.LINEAR, 1500, 2000);
  }

  public static LevelSuccessPopup create() {
    return new LevelSuccessPopup();
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
        Opacity.create(
          scaleSlice.getValue(),
          Scaled.create(
            Math.max(0.01, scaleSlice.getValue()),
            Stack.create(
              Sized.create(
                props -> {
                  props.fixedWidth = 512.0;
                  props.fixedHeight = 256.0;
                },
                Panel.create(
                  props -> props.background = PANEL_CONTAINER_IMG,
                  Text.create(props -> {
                    props.text = "Level Completed";
                    props.alignment = Alignment.CENTER;
                    props.fontStyle = Text.BOLD;
                    props.fontSize = 64;
                  })
                )
              ),
              Padding.create(
                256, 0, 0, 0,
                Sized.create(
                  props -> {
                    props.fixedWidth = 128.0;
                    props.fixedHeight = 64.0;
                  },
                  Button.create(
                    props -> { },
                    Text.create("Proceed")
                  )
                )
              )
            )
          )
        )
      ),

      Padding.create(
        0, 0, (int) (256.0 * scaleSlice.getValue()), 0,
        Center.create(
          Rotated.create(
            degToRad(-45 + 45 * rotationSlice.getValue()),
            Opacity.create(
              rotationSlice.getValue(),
              Sized.create(
                props -> {
                  props.fixedWidth = 256.0;
                  props.fixedHeight = 64.0;
                },
                Panel.create(
                  props -> props.background = SUCCESS_BANNER_IMG,
                  Text.create(props -> {
                    props.text = "Success";
                    props.fontSize = 32;
                  })
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
