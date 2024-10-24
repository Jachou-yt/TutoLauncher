package fr.jachou.tutolauncher;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import com.azuriom.azauth.exception.AuthException;
import fr.jachou.tutolauncher.utils.Animation;
import fr.jachou.tutolauncher.utils.MicrosoftThread;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.theshark34.openlauncherlib.util.ramselector.RamSelector;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;
import javafx.embed.swing.JFXPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.nio.file.Paths;

import static fr.jachou.tutolauncher.Frame.*;

public class Panel extends JPanel implements SwingerEventListener {
    private final Image background = getImage("Launcher.png");
    private final STexturedButton settings = new STexturedButton(getBufferedImage("reglage.png"), getBufferedImage("reglage.png"));
    private final STexturedButton play = new STexturedButton(getBufferedImage("bouton.png"), getBufferedImage("bouton.png"));
    private final STexturedButton croix = new STexturedButton(getBufferedImage("croix.png"), getBufferedImage("croix.png"));
    private final STexturedButton microsoft = new STexturedButton(getBufferedImage("microsoft.png"), getBufferedImage("microsoft.png"));
    private final RamSelector ramSelector = new RamSelector(Frame.getRamFile());

    private JFXPanel jfxPanel;
    private MediaPlayer mediaPlayer;

    public Panel() throws IOException {
        this.setLayout(null);

        jfxPanel = new JFXPanel();
        this.add(jfxPanel);

        Platform.runLater(() -> {
            initFX(jfxPanel);  // Initialiser la vidéo dans le JFXPanel
        });

        play.setBounds(109, 109);
        play.setLocation(200, 490);
        play.addEventListener(this);
        this.add(play);

        microsoft.setBounds(100, 100);
        microsoft.setLocation(200, 300);
        microsoft.addEventListener(this);
        this.add(microsoft);

        settings.setBounds(64, 64);
        settings.setLocation(10, 10);
        settings.addEventListener(this);
        this.add(settings);

        croix.setBounds(64, 64);
        croix.setLocation(450, 10);
        croix.addEventListener(this);
        this.add(croix);

        this.setComponentZOrder(jfxPanel, this.getComponentCount() - 1);

        jfxPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (mediaPlayer != null) {  // Vérifie si mediaPlayer est non null
                    mediaPlayer.setOnReady(() -> {
                        System.out.println("La vidéo est prête à être lue.");
                        adjustMediaViewSize(jfxPanel);
                    });
                }
            }
        });

    }

    private void adjustMediaViewSize(JFXPanel fxPanel) {
        MediaView mediaView = (MediaView) ((javafx.scene.Group) fxPanel.getScene().getRoot()).getChildren().get(0);
        mediaView.setFitWidth(fxPanel.getWidth());
        mediaView.setFitHeight(fxPanel.getHeight());
    }

    private void initFX(JFXPanel fxPanel) {
        // mettre le chemin absolu de la vidéo.
        String videoPath = Paths.get("src/main/resources/test.mp4").toUri().toString();

        // Charge la vidéo.
        Media media = new Media(videoPath);
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setOnError(() -> {
            System.out.println("Erreur de lecture de la vidéo : " + mediaPlayer.getError().getMessage());
        });

        mediaPlayer.setOnReady(() -> {
            System.out.println("Vidéo prête à être lue.");
            mediaPlayer.play();  // Démarre la vidéo quand elle est prête
        });

        mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        // Ajustement de la taille de la MediaView.
        mediaView.setFitWidth(fxPanel.getWidth());
        mediaView.setFitHeight(fxPanel.getHeight());
        mediaView.setPreserveRatio(false);

        // Création de la scène JavaFX avec la vidéo en arrière-plan.
        Scene scene = new Scene(new javafx.scene.Group(mediaView));
        scene.setFill(javafx.scene.paint.Color.BLACK);  // Ajoute un fond noir
        fxPanel.setScene(scene);


        // Lecture en boucle de la vidéo.
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // fais une lecture en boucle de la vidéo.
        mediaPlayer.setVolume(0);
        mediaPlayer.play(); // lance la vidéo.
    }





    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        jfxPanel.setBounds(0, 0, this.getWidth(), this.getHeight());
        // Assure que les boutons seront toujours en premier plan par rapport au background.
        this.setComponentZOrder(jfxPanel, this.getComponentCount() - 1);;
        jfxPanel.repaint();
    }

    @Override
    public void onEvent(SwingerEvent swingerEvent) {
        if (swingerEvent.getSource() == microsoft) {
            new Thread(new MicrosoftThread()).start();

            JOptionPane.showMessageDialog(null, "Authentification réussie avec le compte "+ Launcher.getAuthInfos().getUsername() +" !", "Succès", JOptionPane.INFORMATION_MESSAGE);

        } else if (swingerEvent.getSource() == play) {
            ramSelector.save();

            try {
                Launcher.update();
            } catch (Exception e) {
                Launcher.getReporter().catchError(e, "Impossible de mettre à jour le launcher.");
            }

            try {
                Launcher.launch();
            } catch (Exception e) {
                Launcher.getReporter().catchError(e, "Impossible de lancer le jeu.");
            }

            Animation.fadeOutFrame(Frame.getInstance(), 30, () -> System.exit(0));
        } else if (swingerEvent.getSource() == settings) {
            ramSelector.display();
        } else if (swingerEvent.getSource() == croix) {
            Animation.fadeOutFrame(Frame.getInstance(), 30, () -> System.exit(0));
        }
    }

    public RamSelector getRamSelector() {
        return ramSelector;
    }
}
