package body;

import org.dreambot.core.Instance;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class var {
    public static boolean check_quest;
    public static boolean dispose;
    public static String action;
    public static String gui_title;
    public static boolean gui_on;
    public static boolean gui_allowed;
    public static List<String> quest_list = new ArrayList<>();
    public static boolean
    romeoS, runeS, cookS, corsairS, ernestS, impS,
    doricS, ghostS, sheepS, goblinS, xmarkS, witchS,
    belowS
    ;
    public static int quest_completed;
    public static String percent_complete;

    public static void progress_gui(){
        if (gui_allowed){
            wFrame frame = new wFrame();
            wTextArea progress = new wTextArea();
            wLabel label_action = new wLabel();
            wLabel label_percent = new wLabel();
            wTextArea percent = new wTextArea();
            frame.setTitle(gui_title);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(Instance.getCanvas());
            frame.setPreferredSize(new Dimension(300,300));
            frame.getContentPane().setLayout(null);
            frame.add(progress);
            frame.add(label_action);
            frame.add(label_percent);
            frame.add(percent);
            label_percent.setText("% Till Finish:");
            label_action.setText("Current Action:");
            label_percent.setBackground(new Color(47,47,47));
            label_action.setBackground(new Color(47,47,47));
            label_percent.setBounds(5,35,90,20);
            percent.setBounds(100, 35,175,20);
            label_action.setBounds(5,5,85,20);
            progress.setBounds(100,5,175,20);
            progress.setEditable(false);
            javax.swing.Timer cycle = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.setTitle(gui_title);
                    progress.setText(var.action);
                    percent.setText(var.percent_complete + " / 100%");
                    if (dispose){
                        frame.dispose();
                        var.gui_title = "";
                        var.action = "";
                        var.percent_complete = "0%";
                        dispose = false;
                    }
                }
            });
            cycle.start();
            frame.pack();
            frame.setVisible(true);
        }
    }

    public static Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) {
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight,  java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }
}
