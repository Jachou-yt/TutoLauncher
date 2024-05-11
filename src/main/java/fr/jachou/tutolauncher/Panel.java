package fr.jachou.tutolauncher;

import com.azuriom.azauth.exception.AuthException;
import fr.jachou.tutolauncher.utils.Animation;
import fr.jachou.tutolauncher.utils.MicrosoftThread;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.theshark34.openlauncherlib.util.ramselector.RamSelector;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static fr.jachou.tutolauncher.Frame.*;

public class Panel extends JPanel implements SwingerEventListener {
    private Image background = getImage("Launcher.png");
    private STexturedButton settings = new STexturedButton(getBufferedImage("reglage.png"), getBufferedImage("reglage.png"));
    private STexturedButton play = new STexturedButton(getBufferedImage("bouton.png"), getBufferedImage("bouton.png"));
    private STexturedButton croix = new STexturedButton(getBufferedImage("croix.png"), getBufferedImage("croix.png"));
    private STexturedButton microsoft = new STexturedButton(getBufferedImage("microsoft.png"), getBufferedImage("microsoft.png"));
    private RamSelector ramSelector = new RamSelector(Frame.getRamFile());

    public Panel() throws IOException {
        this.setLayout(null);

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
    }

    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);

        g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    @Override
    public void onEvent(SwingerEvent swingerEvent) {
        if (swingerEvent.getSource() == microsoft) {
            String username = JOptionPane.showInputDialog("Entrez votre adresse mail Azuriom :");
            String password = JOptionPane.showInputDialog("Entrez votre mot de passe Azuriom :");

            try {
                Launcher.auth(username, password);
            } catch (AuthException e) {
                Launcher.getReporter().catchError(e, "Impossible de s'authentifier.");
            }

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
