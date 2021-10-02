package body;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class wTextArea extends JTextArea {
    public wTextArea(){
        setLineWrap(true);
        setBorder(new LineBorder(new Color(125,125,125).brighter()));
        setOpaque(true);
        setFocusable(true);
        setBackground(new Color(47,47,47));
        setForeground(Color.WHITE);
    }
}
