package body;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

public class wFrame extends JFrame {
    public wFrame(){
        Image frame_icon = null;
        try {
            frame_icon = Toolkit.getDefaultToolkit().getImage(new URL("https://i.imgur.com/VTqOz6F.png"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        setIconImage(frame_icon);
    }
}
