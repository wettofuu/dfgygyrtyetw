package body;

import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.input.Camera;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.core.Instance;
import quest.*;
import org.dreambot.api.Client;
import org.dreambot.api.methods.input.mouse.MouseSettings;
import org.dreambot.api.script.AbstractScript;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.net.MalformedURLException;
import java.net.URL;

@ScriptManifest(category = Category.QUEST, name = "Wet Quests", author = "wettofu", version = 1.0)
public class main extends AbstractScript {
    public boolean started;
    private node[] nodes;
    public boolean quest;
    private final Point[] lastPositions = new Point[15];
    private long angle;
    private int numb;
    private boolean help;
    private AffineTransform t;
    private final BasicStroke stroke = new BasicStroke(2);
    private int mX, mY;

    @Override
    public void onStart() {
        SwingUtilities.invokeLater(this::gui);
        MouseSettings.setSpeed(50);
        Client.getInstance().setDrawMouse(false);
        nodes = new node[]{
                new validate_quest(this),
                new cook_assistant(this),
                new restless_ghost(this),
                new sheep_shearer(this),
                new ernest_chicken(this),
                new xmarks_spot(this),
                new rune_mysteries(this),
                new romeo_juliet(this),
                new doric_quest(this),
                new goblin_diplomacy(this),
                new witch_potion(this),
                new imp_catcher(this),
                new below_mount(this),
                new stop_script(this),
        };
        if (Camera.getZoom() != 181){
            Rectangle r = new Rectangle(Calculations.random(90,600),Calculations.random(70,400));
            Mouse.move(r);
            Mouse.scrollUntil(Camera.getZoom() == 181, 5000, ()-> Camera.getZoom() == 181);
        }
    }
    @Override
    public void onExit() {
        Client.getInstance().setDrawMouse(true);
    }

    @Override
    public int onLoop() {
        if (started) {
            for (node node : nodes) {
                if (node.validate()) {
                    return node.execute();
                }
            }
        }
        return 1500;
    }

    private void gui() {
        wFrame main_frame = new wFrame();
        main_frame.setTitle("Wet Quests - Version 1.0.8");
        main_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        main_frame.setLocationRelativeTo(Instance.getCanvas());
        main_frame.setPreferredSize(new Dimension(700,500));
        main_frame.getContentPane().setLayout(null);

        JPanel sidebar = new JPanel();
        sidebar.setBounds(0,0,30,500);
        sidebar.setBackground(new Color(36,36,36));
        sidebar.setLayout(null);
        main_frame.add(sidebar);

        JPanel description = new JPanel();
        description.setBounds(30,0,670,200);
        description.setBackground(new Color(35,35,35));
        description.setLayout(null);
        main_frame.add(description);

        JPanel selection = new JPanel();
        selection.setBounds(30,200,670, 300);
        selection.setBackground(Color.black);
        selection.setLayout(null);
        main_frame.add(selection);

        wLabel title = new wLabel();
        title.setText("Wet Quests V1.0.8");
        title.setBounds(60,50,310,55);
        title.setFont(new Font("Corbert", Font.PLAIN, 36));
        description.add(title);

        JButton menu_button = null;
        try {
            menu_button = new JButton(new ImageIcon(((new ImageIcon(new URL("https://i.imgur.com/Wt2fYWE.png")).getImage().getScaledInstance(23,23, Image.SCALE_SMOOTH)))));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        menu_button.setFocusPainted(false);
        menu_button.setBorder(BorderFactory.createEmptyBorder());
        menu_button.setContentAreaFilled(false);
        menu_button.setBounds(0,5,28,30);
        sidebar.add(menu_button);

        JButton error_button = null;
        try {
            error_button = new JButton(new ImageIcon(((new ImageIcon(new URL("https://i.imgur.com/tcYGdlM.png")).getImage().getScaledInstance(42,42, Image.SCALE_SMOOTH)))));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        error_button.setFocusPainted(false);
        error_button.setBorder(BorderFactory.createEmptyBorder());
        error_button.setContentAreaFilled(false);
        error_button.setBounds(5,50,35,30);
        sidebar.add(error_button);

        JLabel book = null;
        try {
            book = new JLabel(new ImageIcon(((new ImageIcon(new URL("https://i.imgur.com/quOlmq0.png")).getImage().getScaledInstance(142,142, Image.SCALE_SMOOTH)))));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        book.setBounds(450, 20, 150,150);
        description.add(book);

        wButton db_link = new wButton();
        db_link.setText("Instructions");
        db_link.setBounds(49,105,110,20);
        db_link.setFont(new Font("Corbert", Font.BOLD, 14));
        db_link.setBorderPainted(false);
        description.add(db_link);

        db_link.addActionListener(l->{
            try {
                Desktop.getDesktop().browse(new URL("https://dreambot.org/forums/index.php?/topic/22583-wet-quests/").toURI());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        wButton start = new wButton();
        start.setBorderPainted(false);
        start.setText(">");
        start.setBounds(0,430,30,40);
        start.setFont(new Font("Corbert", Font.BOLD, 30));
        sidebar.add(start);

        DefaultListModel<String> listModel = new DefaultListModel<String>();
        listModel.addElement("Below Ice Mountain");
        listModel.addElement("Cook's Assistant");
        listModel.addElement("Ernest the Chicken");
        listModel.addElement("Goblin Diplomacy");
        listModel.addElement("Restless Ghost");
        listModel.addElement("Romeo & Juliet");
        listModel.addElement("Rune Mysteries");
        listModel.addElement("Sheep Shearer");
        listModel.addElement("X Marks the Spot");

        DefaultListModel<String> wip_list = new DefaultListModel<String>();
        wip_list.addElement("Black Knight Fortress");
        wip_list.addElement("Prince Ali Rescue");
        wip_list.addElement("Dragon Slayer I");
        wip_list.addElement("Corsair Curse");
        wip_list.addElement("Doric's Quest");
        wip_list.addElement("Imp Catcher");
        wip_list.addElement("Witch's Potion");

        JList<String> quest_list = new JList<>(listModel);
        quest_list.setLayoutOrientation(JList.VERTICAL);
        quest_list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane finished_quests = new JScrollPane(quest_list);
        finished_quests.setPreferredSize(new Dimension(115,80));
        finished_quests.setBounds(50, 40, 138,150);
        Border border = BorderFactory.createLineBorder(new Color(102,102,102));
        finished_quests.setBorder(border);
        finished_quests.setBackground(Color.BLACK);
        selection.add(finished_quests);

        JList<String> wip_listI = new JList<>(wip_list);
        wip_listI.setLayoutOrientation(JList.VERTICAL);
        wip_listI.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane wip_pane = new JScrollPane(wip_listI);
        wip_pane.setPreferredSize(new Dimension(115,80));
        wip_pane.setBounds(260,40,138,150);
        wip_pane.setBorder(border);
        wip_pane.setBackground(Color.BLACK);
        selection.add(wip_pane);

        DefaultListModel<String> final_list = new DefaultListModel<String>();
        JList<String> questing_list = new JList<>(final_list);
        questing_list.setLayoutOrientation(JList.VERTICAL);
        JScrollPane questPane = new JScrollPane(questing_list);
        questPane.setPreferredSize(new Dimension(115,80));
        questPane.setBounds(475,40,138,150);
        questPane.setBorder(border);
        questPane.setBackground(Color.BLACK);
        selection.add(questPane);

        wLabel available_quest = new wLabel();
        available_quest.setText("Available Quests");
        available_quest.setBounds(50,20,120,20);
        selection.add(available_quest);
        wButton add_quest = new wButton();
        add_quest.setText("Add Quest");
        add_quest.setBounds(50,195,138,20);
        add_quest.setBorder(border);
        selection.add(add_quest);
        add_quest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = quest_list.getSelectedIndex();
                if (selectedIndex != -1){
                    String final_quest = quest_list.getSelectedValue();
                    listModel.remove(selectedIndex);
                    final_list.addElement(final_quest);
                }
            }
        });


        wButton remove_quest = new wButton();
        remove_quest.setText("Remove Quest");
        remove_quest.setBounds(475,195,138,20);
        remove_quest.setBorder(border);
        selection.add(remove_quest);
        remove_quest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = questing_list.getSelectedIndex();
                if (selectedIndex != -1){
                    String final_quest = questing_list.getSelectedValue();
                    final_list.remove(selectedIndex);
                    listModel.addElement(final_quest);
                }
            }
        });

        wButton show_progress = new wButton();
        selection.add(show_progress);
        show_progress.setBounds(475, 220,138,20);
        show_progress.setBorder(border);
        show_progress.setText("Enable GUI Progress");
        show_progress.setFocusPainted(false);
        show_progress.setForeground(Color.cyan);
        show_progress.addActionListener(l -> {
            if (show_progress.getText().equals("Enable GUI Progress")){
                show_progress.setForeground(Color.red);
                show_progress.setText("Disable GUI Progress");
            } else if (show_progress.getText().equals("Disable GUI Progress")){
                show_progress.setText("Enable GUI Progress");
                show_progress.setForeground(Color.cyan);

            }
        });

        wButton add_wip_quest = new wButton();
        add_wip_quest.setText("Add Quest");
        add_wip_quest.setBounds(260,195,138,20);
        add_wip_quest.setBorder(border);
        selection.add(add_wip_quest);
        add_wip_quest.addActionListener(l->{
            int wipIndex = wip_listI.getSelectedIndex();
            if (wipIndex != -1){
                String wip_quest = wip_listI.getSelectedValue();
                wip_list.remove(wipIndex);
                final_list.addElement(wip_quest);
            }
        });

        wLabel wip_quest = new wLabel();
        wip_quest.setText("WIP Quests");
        wip_quest.setBounds(260,20,120,20);
        selection.add(wip_quest);


        wLabel queue_quest = new wLabel();
        queue_quest.setText("Quests in Queue");
        queue_quest.setBounds(475,20,120,20);
        selection.add(queue_quest);


        menu_button.addActionListener(l -> {
            if (help){
                selection.removeAll();
                selection.add(wip_quest);
                selection.add(queue_quest);
                selection.add(available_quest);
                selection.add(wip_pane);
                selection.add(questPane);
                selection.add(finished_quests);
                selection.add(add_wip_quest);
                selection.add(add_quest);
                selection.add(remove_quest);
                selection.add(show_progress);
                selection.revalidate();
                selection.repaint();
                help = false;
            }
        });

        error_button.addActionListener(l -> {
            if (!help){
                selection.removeAll();
                JLabel warning_label = new JLabel();
                JLabel discord_label = new JLabel();
                wButton report_link = new wButton();
                report_link.setText("here");
                report_link.setBounds(506,35,60,20);
                report_link.setFont(new Font("Corbert", Font.BOLD, 16));
                report_link.setBorderPainted(false);
                report_link.setFocusPainted(false);
                description.add(report_link);

                report_link.addActionListener(p->{
                    try {
                        Desktop.getDesktop().browse(new URL("https://dreambot.org/forums/index.php?/topic/22583-wet-quests/").toURI());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                warning_label.setText("If you encounter any problems with the script, please report it");
                warning_label.setFont(new Font("Corbert", Font.BOLD, 16));
                warning_label.setBounds(50,20,500,50);
                discord_label.setText("If you have any further questions, Add me on discord - tofuu#2714");
                discord_label.setFont(new Font("Corbert", Font.BOLD, 16));
                discord_label.setBounds(50,50,500,50);
                selection.add(discord_label);
                selection.add(warning_label);
                selection.add(report_link);
                selection.revalidate();
                selection.repaint();
                help = true;
            }
        });

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < questing_list.getModel().getSize(); i++){
                    if (questing_list.getModel().getElementAt(i).equals("Romeo & Juliet")){
                        var.quest_list.add("romeo");
                    }
                    if (questing_list.getModel().getElementAt(i).equals("Rune Mysteries")){
                        var.quest_list.add("rune");
                    }
                    if (questing_list.getModel().getElementAt(i).equals("Cook's Assistant")){
                        var.quest_list.add("cook");
                    }
                    if (questing_list.getModel().getElementAt(i).equals("Ernest the Chicken")){
                        var.quest_list.add("ernest");
                    }
                    if (questing_list.getModel().getElementAt(i).equals("Imp Catcher")){
                        var.quest_list.add("imp");
                    }
                    if (questing_list.getModel().getElementAt(i).equals("Doric's Quest")){
                        var.quest_list.add("doric");
                    }
                    if (questing_list.getModel().getElementAt(i).equals("Restless Ghost")){
                        var.quest_list.add("ghost");
                    }
                    if (questing_list.getModel().getElementAt(i).equals("Sheep Shearer")){
                        var.quest_list.add("sheep");
                    }
                    if (questing_list.getModel().getElementAt(i).equals("Goblin Diplomacy")){
                        var.quest_list.add("goblin");
                    }
                    if (questing_list.getModel().getElementAt(i).equals("X Marks the Spot")){
                        var.quest_list.add("xmarks");
                    }
                    if (questing_list.getModel().getElementAt(i).equals("Witch's Potion")){
                        var.quest_list.add("witch");
                    }
                    if (questing_list.getModel().getElementAt(i).equals("Below Ice Mountain")){
                        var.quest_list.add("icemount");
                    }
                    if (show_progress.getText().equals("Disable GUI Progress")){
                        var.gui_allowed = true;
                    }
                }
                var.check_quest = true;
                started = true;
                main_frame.dispose();
            }
        });

        main_frame.pack();
        main_frame.setResizable(false);
        main_frame.setVisible(true);
    }


    @Override
    public void onPaint(Graphics2D g) {
        Point currentPosition = Mouse.getPosition();
        mX = Mouse.getPosition().x;
        mY = Mouse.getPosition().y;
        t = g.getTransform();
        Point lastpoint = null;
        Color mColor = new Color(60,179,133);
        Arc2D.Double dub = new Arc2D.Double(mX-12, mY-12, 24, 24, 330, 60, Arc2D.OPEN);
        Arc2D.Double dubI = new Arc2D.Double(mX-12, mY-12, 24, 24, 151, 60, Arc2D.OPEN);
        for(int i=0;i<lastPositions.length - 1;i++){
            lastPositions[i]=lastPositions[i+1];
        }
        lastPositions[lastPositions.length - 1] = new Point(currentPosition.x, currentPosition.y);

        for(int i=lastPositions.length - 1;i>=0;i--) {
            Point p = lastPositions[i];
            if(p != null) {
                if(lastpoint == null)
                    lastpoint = p;
                g.setColor(new Color(60,179,133));
                g.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
                g.drawLine(lastpoint.x, lastpoint.y, p.x, p.y);
            }
            lastpoint = p;
            if(i % 3 == 0)
                mColor = mColor.darker();
        }
        if (mX != -1){
            g.setStroke(stroke);
            g.setColor(Color.white);
            g.drawLine(mX-3, mY-3, mX+2, mY+2);
            g.drawLine(mX-3, mY+2,mX+2, mY-3);
            g.rotate(Math.toRadians(angle+=6), mX, mY);

            g.draw(dub);
            g.draw(dubI);

            g.setTransform(t);
        }

    }
}
