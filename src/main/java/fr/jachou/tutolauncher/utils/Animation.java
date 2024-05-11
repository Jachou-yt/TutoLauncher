package fr.jachou.tutolauncher.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Animation {
    private static boolean isFadingOut = false;

    public static void fadeInFrame(JFrame frame, int duration) {
        float opacityIncrement = 1.0f / (float) duration;
        Timer timer = new Timer(1000 / duration, null);
        timer.addActionListener(new ActionListener() {
            float opacity = 0.0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += opacityIncrement;
                if (opacity >= 1.0f) {
                    opacity = 1.0f;
                    timer.stop();
                }
                setOpacity(frame, opacity);
            }
        });
        setOpacity(frame, 0.0f); // Initial opacity
        frame.setVisible(true); // Show the frame
        timer.start();
    }

    public static void fadeOutFrame(JFrame frame, int duration) {
        fadeOutFrame(frame, duration, null);
    }

    public static void fadeOutFrame(JFrame frame, int duration, Runnable onFinish) {
        if (isFadingOut) {
            return; // Already fading out
        }
        isFadingOut = true;

        float opacityIncrement = 1.0f / (float) duration;
        Timer timer = new Timer(1000 / duration, null);
        timer.addActionListener(new ActionListener() {
            float opacity = 1.0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                opacity -= opacityIncrement;
                if (opacity <= 0.0f) {
                    opacity = 0.0f;
                    timer.stop();
                    isFadingOut = false;
                    if (onFinish != null) {
                        onFinish.run();
                    }
                    frame.setVisible(false); // Hide the frame when animation completes
                }
                setOpacity(frame, opacity);
            }
        });
        setOpacity(frame, 1.0f); // Set initial opacity to fully visible
        timer.start();
    }

    private static void setOpacity(JFrame frame, float opacity) {
        if (opacity > 1.0f) {
            opacity = 1.0f;
        } else if (opacity < 0.0f) {
            opacity = 0.0f;
        }
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        if (gd.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSLUCENT)) {
            frame.setOpacity(opacity);
        }
    }
}
