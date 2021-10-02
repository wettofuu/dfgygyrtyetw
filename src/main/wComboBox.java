package body;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class wComboBox extends JComboBox {
    public wComboBox(){
        setBorder(new LineBorder(new Color(125,125,125).brighter()));
        setOpaque(true);
        setFocusable(true);
        setBackground(Color.BLACK);
        setForeground(Color.white);
    }
}
