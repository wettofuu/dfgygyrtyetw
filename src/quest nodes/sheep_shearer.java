package quest;

import body.main;
import body.var;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Map;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import javax.swing.*;


public class sheep_shearer extends node{
    Area sheep_field = new Area(3212, 3257, 3193, 3276);
    Area inner_field = new Area(3195, 3273, 3209, 3260);
    Area fred_area = new Area(3184, 3279, 3192, 3270);
    Tile spinner = new Tile(3209,3213,1);
    Area stile_area = new Area(3201, 3281, 3194, 3277);
    Tile jump_tile = new Tile(3197, 3280);
    Area second_floor = new Area(3204, 3230, 3216, 3207,1);
    Area spinner_area = new Area(3212,3217,3208,3212,1);
    public String[] options = {
            "I'm looking for a quest.",
            "Yes.",
            "Yes, okay. I can do that."
    };
    public sheep_shearer(main main) {
        super(main);
    }

    @Override
    public boolean validate() {
        return var.sheepS;
    }


    @Override
    public int execute() {
        if (var.gui_on){
            if (partIII()){
                sheepI();
            } else if (progress() == 1){
                sheepII();
            } else if (progress() == 21){
                end();
            }
        } else {
            var.gui_title = "Sheep Shearer";
            SwingUtilities.invokeLater(var::progress_gui);
            var.gui_on = true;
        }
        return 1000;
    }

    private void sheepI() {
        if (fred_area.contains(p())) {
            var.action = "Chatting with Fred";
            if (Map.canReach(fred())) {
                if (inDialogue()) {
                    optionDialogue(options);
                } else {
                    fred().interact("Talk-to");
                    MethodProvider.sleepUntil(Dialogues::inDialogue, 5000);
                }
            } else if (Walking.getDestinationDistance() < 5) {
                Walking.walk(fred());
            }
        } else if (Walking.getDestinationDistance() < 5) {
            var.action = "Heading to Fred";
            Walking.walk(fred_area.getCenter());
        }
    }

    private void sheepII() {
        if (Inventory.contains("Shears")) {
            if (Inventory.count("Wool") < 20 && Inventory.count("Ball of wool") == 0) {
                var.percent_complete = "50%";
                if (Inventory.isFull()) {
                    var.action = "Dropping Loot";
                    Inventory.dropAllExcept("Wool", "Shears");
                } else if (sheep_field.contains(p())) {
                    var.action = "Shearing Sheep";
                    NPC sheep = NPCs.closest(2694, 2699, 2693, 2695, 2786, 2787);
                    if (sheep.hasAction("Shear") && Map.canReach(sheep)) {
                        if (sheep.interact("Shear")) {
                            MethodProvider.sleep(2000, 3000);
                        }
                    } else if (Walking.getDestinationDistance() < 5) {
                        Walking.walk(inner_field.getRandomTile());
                    }
                } else if (stile_area.contains(f.getLocalPlayer())) {
                    var.action = "Heading into Sheep Pen";
                    stile().interact("Climb-over");
                    MethodProvider.sleepUntil(() -> inner_field.contains(p()), 5000);
                } else if (Walking.getDestinationDistance() < 5) {
                    var.action = "Walking to Stile";
                    Walking.walk(jump_tile);
                }
            } else if (Inventory.count("Wool") >= 20) {
                var.percent_complete = "70%";
                GameObject wheel = GameObjects.closest("Spinning wheel");
                if (spinner_area.contains(p())) {
                    if (p().getTile().equals(spinner)) {
                        WidgetChild widget = Widgets.getWidgetChild(270, 14);
                        if (widget != null) {
                            widget.interact("Spin");
                            MethodProvider.sleepUntil(() -> Inventory.count("Ball of wool") >= 20, 30000);
                        } else {
                            var.action = "Spinning Wool";
                            wheel.interact("Spin");
                            MethodProvider.sleep(2000);
                        }
                    } else {
                        Walking.walkExact(spinner);
                    }
                } else if (second_floor.contains(p())){
                    GameObject door = GameObjects.getTopObjectOnTile(new Tile(3207,3214,1));
                    if (door !=null && door.hasAction("Open")){
                        door.interact("Open");
                        MethodProvider.sleepUntil(()-> spinner_area.contains(p()), 5000);
                    } else {
                        Walking.walk(spinner);
                    }
                } else if (Walking.getDestinationDistance() < 3) {
                    var.action = "Heading to Spinner";
                    Walking.walkExact(spinner);
                }
            } else {
                if (Inventory.count("Ball of wool") >= 20) {
                    var.percent_complete = "90%";
                    sheepI();
                }
            }
        } else {
            var.percent_complete = "30%";
            var.action = "Grabbing Shears";
            GroundItem shear = GroundItems.closest("Shears");
            if (shear !=null && shear.distance(p()) < 3) {
                shear.interact("Take");
                MethodProvider.sleepUntil(() -> Inventory.contains("Shears"), 5000);
            } else {
                Walking.walk(shear);
            }
        }
    }

    private void end() {
        var.percent_complete = "100%";
        WidgetChild screen = Widgets.getWidgetChild(153,16);
        if (screen !=null){
            screen.interact("Close");
        } else {
            var.quest_completed++;
            var.dispose = true;
            var.gui_on = false;
            var.sheepS = false;
        }
    }

    private int progress(){
        return PlayerSettings.getConfig(179);
    }

    private NPC fred(){
        return NPCs.closest("Fred the Farmer");
    }

    public boolean inDialogue(){
        return Dialogues.inDialogue();
    }

    private boolean optionDialogue(String[] s){
        if (Dialogues.inDialogue()){
            if (Dialogues.continueDialogue()){
                return Dialogues.spaceToContinue();
            }
            if (!Dialogues.continueDialogue()){
                for (String option : options){
                    Dialogues.chooseOption(option);
                }
            }
        } else {
            MethodProvider.sleepUntil((Dialogues::inDialogue), 2000);
        }
        return optionDialogue(options);
    }
    private GameObject stile(){
        return GameObjects.closest("Stile");
    }

    private boolean partIII(){
        return progress() == 0 || progress() == 20;
    }
    
    private Player p(){
        return Players.localPlayer();
    }
}
