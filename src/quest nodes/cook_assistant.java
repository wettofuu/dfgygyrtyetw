package quest;

import body.main;
import body.var;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.walking.pathfinding.impl.obstacle.impl.PassableObstacle;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import javax.swing.*;

public class cook_assistant extends node{
    Area cook_area = new Area(3212, 3217, 3205, 3212);
    Area basement_area = new Area(3208, 9625, 3219, 9615);
    Area cow_field = new Area(3256, 3270, 3253, 3276);
    Area egg_area = new Area(3227, 3300, 3233, 3299);
    Area wheat_field = new Area(3161, 3293, 3163, 3291);
    Area top_floor = new Area(3162, 3311, 3171, 3302, 2);
    Area mid_floor = new Area(3163, 3310, 3170, 3303,1);
    Area bottom_floor = new Area(3164, 3308, 3169, 3305);
    private boolean egg;
    private boolean wheat;
    private boolean flour;
    private boolean flipped;
    private boolean finish;
    private float timer;
    public String[] options = {
            "What's wrong?",
            "Yes.",
            "Actually, I know where to find this stuff."
    };

    public cook_assistant(main main) {
        super(main);
    }

    @Override
    public boolean validate() {
        return var.cookS;
    }

    @Override
    public int execute() {
        timer++;
        if (var.gui_on) {
            if (progress() == 0) {
                cookI();
            } else if (progress() == 1) {
                if (finish) {
                    cookVII();
                } else if (flipped) {
                    cookVI();
                } else if (flour) {
                    cookV();
                } else if (wheat) {
                    cookIV();
                } else if (egg) {
                    cookIII();
                } else {
                    cookII();
                }
            } else if (progress() == 2) {
                end();
            }
        } else {
            var.gui_title = "Cook's Assistant";
            SwingUtilities.invokeLater(var::progress_gui);
            var.gui_on = true;
        }
        return 1000;
    }

    private void cookVII() {
        var.percent_complete = "90%";
        if (cook_area.contains(p())){
            var.action = "Finishing Quest";
            if (inDialogue()){
                optionDialogue(options);
            } else {
                NPC cook = NPCs.closest("Cook");
                cook.interact("Talk-to");
                MethodProvider.sleepUntil(Dialogues::inDialogue, 5000);
            }
        } else if (Walking.getDestinationDistance() < 5){
            var.action = "Walking to Cook";
            Walking.walk(cook_area.getRandomTile());
        }
    }

    private void cookVI() {
        var.action = "Grabbing Flour";
        var.percent_complete = "85%";
        if (top_floor.contains(p())){
            ladder().interact("Climb-down");
            MethodProvider.sleepUntil(()-> mid_floor.contains(p()),5000);
        } else if (mid_floor.contains(p())){
            ladder().interact("Climb-down");
            MethodProvider.sleepUntil(()-> bottom_floor.contains(p()),5000);
        } else if (bottom_floor.contains(p())){
            GameObject bin = GameObjects.closest("Flour bin");
            bin.interact("Empty");
            MethodProvider.sleepUntil(()-> Inventory.contains("Pot of flour"), 5000);
            finish = true;
        }
    }

    private void cookV() {
        var.action = "Operating Flour";
        var.percent_complete = "80%";
        if (bottom_floor.contains(p())){
            ladder().interact("Climb-up");
            MethodProvider.sleepUntil(()-> mid_floor.contains(p()),5000);
        } else if (mid_floor.contains(p())){
            ladder().interact("Climb-up");
            MethodProvider.sleepUntil(()-> top_floor.contains(p()),5000);
        } else if (top_floor.contains(p())){
                //fix
                GameObject hopper = GameObjects.closest("Hopper");
                hopper.interact("Fill");
                MethodProvider.sleep(Calculations.random(4500,7000));
                GameObject control = GameObjects.closest("Hopper controls");
                control.interact("Operate");
                MethodProvider.sleep(Calculations.random(3500,6000));
                //fix
                flipped = true;
        } else {
            if (Walking.getDestinationDistance() < 5){
                Walking.getAStarPathFinder().addObstacle(new PassableObstacle("Large door", "Open", null,null,null));
                Walking.walk(bottom_floor.getRandomTile());
            }
        }
    }

    private void cookIV() {
        if (Inventory.contains("Grain")){
            var.percent_complete = "70%";
            flour = true;
        } else {
            var.action = "Walking to Rice Field";
            if (wheat_field.contains(p())){
                GameObject wheat = GameObjects.closest("Wheat");
                wheat.interact("Pick");
                MethodProvider.sleepUntil(()-> Inventory.contains("Grain"),5000);
            } else if (Walking.getDestinationDistance() < 5){
                Walking.walk(wheat_field.getRandomTile());
            }
        }
    }

    private void cookIII() {
        if (Inventory.contains("Egg")){
            var.percent_complete = "60%";
            wheat = true;
        } else {
            var.action = "Grabbing an Egg";
            var.percent_complete = "30%";
            if (egg_area.contains(p())) {
                GroundItem egg = GroundItems.closest("Egg");
                egg.interact("Take");
                MethodProvider.sleepUntil(()-> Inventory.contains("Egg"), 2000);
            } else if (Walking.getDestinationDistance() < 2){
                Walking.walk(egg_area.getRandomTile());
            }
        }
    }

    private void cookII() {
        if (Inventory.contains("Pot")){
            if (Inventory.contains("Bucket")){
                var.action = "Milking Cows";
                var.percent_complete = "10%";
                if (cow_field.contains(p())){
                        GameObject cow = GameObjects.closest("Dairy cow");
                        cow.interact("Milk");
                        MethodProvider.sleepUntil(()-> Inventory.contains("Bucket of milk"), 5000);
                        egg = true;
                } else {
                    if (basement_area.contains(p())){
                        GameObject ladder = GameObjects.closest("Ladder");
                        ladder.interact("Climb-up");
                        MethodProvider.sleepUntil(()-> cook_area.contains(p()), Calculations.random(4000,7000));
                    } else if (Walking.getDestinationDistance() < 6){
                        Walking.walk(cow_field.getRandomTile());
                    }
                }
            } else {
                var.action = "Grabbing a Bucket";
                var.percent_complete = "7%";
                if (basement_area.contains(p())){
                    GroundItem bucket = GroundItems.closest(l-> l != null && l.getName().equals("Bucket"));
                    bucket.interact("Take");
                    MethodProvider.sleepUntil(()-> Inventory.contains("Bucket"), Calculations.random(4000,7000));
                } else {
                    if (cook_area.contains(p())){
                        GameObject trapdoor = GameObjects.closest("Trapdoor");
                        trapdoor.interact("Climb-down");
                        MethodProvider.sleepUntil(()-> basement_area.contains(p()), 5000);
                    } else if (Walking.getDestinationDistance() < 6){
                        Walking.walk(cook_area.getCenter());
                    }
                }
            }
        } else {
            var.action = "Grabbing a Pot";
            var.percent_complete = "4%";
            if (cook_area.contains(p())){
                GroundItem pot = GroundItems.closest(l-> l !=null && l.getName().equals("Pot"));
                pot.interact("Take");
                MethodProvider.sleepUntil(()-> Inventory.contains("Pot"), 5000);
            } else if (Walking.getDestinationDistance() < 6){
                Walking.walk(cook_area.getCenter());
            }
        }
    }

    private void cookI() {
            if (cook_area.contains(p())){
                var.action = "Talking to Cook";
                if (inDialogue()){
                    optionDialogue(options);
                } else {
                    NPC cook = NPCs.closest(l -> l !=null && l.getName().equals("Cook"));
                    cook.interact("Talk-to");
                    MethodProvider.sleepUntil(Dialogues::inDialogue, 5000);
                }
            } else if (Walking.getDestinationDistance() < 6){
                var.action = "Walking to Cook";
                Walking.walk(cook_area.getCenter());
            }
    }

    private void end() {
        var.percent_complete = "100%";
        WidgetChild screen = Widgets.getWidgetChild(153,16);
        if (screen !=null){
            screen.interact("Close");
        } else {
            MethodProvider.log("Finished Cook's Assistant in: " + timer);
            var.quest_completed++;
            var.dispose = true;
            var.gui_on = false;
            var.cookS = false;
        }
    }
    
    private Player p(){
        return Players.localPlayer();
    }

    private GameObject ladder(){
        return GameObjects.closest(l -> l !=null && l.getName().equals("Ladder"));
    }

    private boolean inDialogue(){
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

    private int progress(){
        return PlayerSettings.getConfig(29);
    }
}
