package vt.passit.Animations;

import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.VBox;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class TestCardAnimator {

    private static final Duration ANIMATION_DURATION = Duration.millis(200);
    private static final double SCALE_FACTOR = 1.05;
    private static final double TRANSLATE_Y_OFFSET = -5;

    private static final DropShadow INITIAL_SHADOW = new DropShadow(6, Color.BLACK);
    private static final DropShadow HOVER_SHADOW = new DropShadow(15, Color.BLACK);

    static {
        INITIAL_SHADOW.setRadius(6);
        INITIAL_SHADOW.setColor(Color.BLACK);

        HOVER_SHADOW.setRadius(15);
        HOVER_SHADOW.setColor(Color.BLACK);
    }

    public static void applyHoverAnimation(VBox card) {
        card.setEffect(INITIAL_SHADOW);

        ScaleTransition scaleIn = new ScaleTransition(ANIMATION_DURATION, card);
        scaleIn.setToX(SCALE_FACTOR);
        scaleIn.setToY(SCALE_FACTOR);

        TranslateTransition translateIn = new TranslateTransition(ANIMATION_DURATION, card);
        translateIn.setToY(TRANSLATE_Y_OFFSET);

        ParallelTransition animateIn = new ParallelTransition(scaleIn, translateIn);

        ScaleTransition scaleOut = new ScaleTransition(ANIMATION_DURATION, card);
        scaleOut.setToX(1.0);
        scaleOut.setToY(1.0);

        TranslateTransition translateOut = new TranslateTransition(ANIMATION_DURATION, card);
        translateOut.setToY(0);

        ParallelTransition animateOut = new ParallelTransition(scaleOut, translateOut);

        card.setOnMouseEntered(event -> {
            animateOut.stop();
            animateIn.play();
            card.setEffect(HOVER_SHADOW);
        });

        card.setOnMouseExited(event -> {
            animateIn.stop();
            animateOut.play();
            card.setEffect(INITIAL_SHADOW);
        });
    }
}