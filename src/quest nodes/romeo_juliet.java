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
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Map;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.walking.path.impl.LocalPath;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import javax.swing.*;
import java.util.Arrays;

public class romeo_juliet extends node{
    Area cadava_area = new Area(3264, 3375, 3278, 3365);
    Area varrock_area = new Area(3205, 3437, 3222, 3420);
    Area juliet_house = new Area(3156, 3432, 3164, 3437);
    Tile stair_tile = new Tile(3159, 3435);
    Area second_floor = new Area(3151, 3439, 3163, 3425, 1);
    Tile second_stair = new Tile(3155, 3435, 1);
    Area church_area = new Area(3252, 3487, 3259, 3480);
    Area pot_area = new Area(3192, 3402, 3197, 3406);
    Tile juliet_tile = new Tile(3158, 3425, 1);
    private float timer;
    Tile[] path = {
            new Tile(3272,3369),
            new Tile(3273, 3370, 0),
            new Tile(3279, 3372, 0),
            new Tile(3285, 3373, 0),
            new Tile(3290, 3376, 0),
            new Tile(3291, 3382, 0),
            new Tile(3290, 3389, 0),
            new Tile(3289, 3395, 0),
            new Tile(3289, 3401, 0),
            new Tile(3289, 3408, 0),
            new Tile(3285, 3412, 0),
            new Tile(3281, 3416, 0),
            new Tile(3279, 3420, 0),
            new Tile(3276, 3425, 0),
            new Tile(3270, 3427, 0),
            new Tile(3262, 3428, 0),
            new Tile(3253, 3429, 0),
            new Tile(3246, 3429, 0),
            new Tile(3239, 3429, 0),
            new Tile(3229, 3429, 0),
            new Tile(3220, 3429, 0),
            new Tile(3213, 3429, 0),
            new Tile(3206, 3429, 0),
            new Tile(3198, 3429, 0),
            new Tile(3190, 3429, 0),
            new Tile(3179, 3429, 0),
            new Tile(3171, 3430, 0),
            new Tile(3167, 3433, 0),
            new Tile(3160, 3434, 0),
    };

    private final String[] options = {
            "Yes, I have seen her actually!",
            "Yes.",
            "Talk about something else.",
            "Talk about Romeo & Juliet.",
    };
    public romeo_juliet(main main) {
        super(main);
    }

    @Override
    public boolean validate() {
        return var.romeoS;
    }

    @Override
    public int execute() {
        if (var.gui_on){
            timer++;
            if (progress() == 0){
                rnjI();
            } else if (progress() == 10){
                rnjII();
            } else if (partII()){
                rnjIII();
            } else if (progress() == 30){
                rnjIV();
            } else if (progress() == 40){
                rnjV();
            } else if (progress() == 50){
                if (!Inventory.contains("Cadava potion")){
                    rnjV();
                } else {
                    rnjVI();
                }
            } else if (progress() == 100){
                end();
            }
        } else {
            var.gui_title = "Romeo & Juliet";
            SwingUtilities.invokeLater(var::progress_gui);
            var.gui_on = true;
        }
        return 1000;
    }

    private void end() {
        var.percent_complete = "100%";
        WidgetChild screen = Widgets.getWidgetChild(153,16);
        if (screen !=null){
            screen.interact("Close");
        } else {
            MethodProvider.log("Finished Romeo & Juliet in: " + timer);
            var.quest_completed++;
            var.dispose = true;
            var.gui_on = false;
            var.romeoS = false;
        }
    }

    private void rnjI() {
        if (Inventory.contains("Cadava berries")) {
            var.action = "Heading to Romeo";
            var.percent_complete = "10%";
            if (romeo() !=null){
                    if (inDialogue()) {
                        optionDialogue(options);
                    } else {
                        romeo().interact("Talk-to");
                        MethodProvider.sleepUntil(Dialogues::inDialogue, Calculations.random(5000, 6000));
                    }
            } else if (Walking.getDestinationDistance() < Calculations.random(4,10)) {
                LocalPath<Tile> path1 = new LocalPath<>();
                path1.addAll(Arrays.asList(path));
                if (path1.next() != null && Map.canReach(path1.next()) && Players.localPlayer().getTile() != stair_tile) {
                    Walking.walk(path1.next());
                } else {
                    Walking.walkExact(stair_tile);
                }
            }
        } else if (cadava_area.contains(p())) {
            var.action = "Picking Cadava";
            GameObject bush = GameObjects.closest(l -> l !=null && l.getName().equals("Cadava bush") && l.getID() != 23627);
            bush.interact("Pick-from");
            MethodProvider.sleepUntil(()-> Inventory.contains("Cadava berries"), 5000);
        } else if (Walking.getDestinationDistance() < 5) {
            var.action = "Walking to Cadava";
            Walking.walk(cadava_area.getCenter());
        }
    }
    
    private void rnjII() {
        var.percent_complete = "20%";
        if (second_floor.contains(p())){
            var.action = "Talking to Juliet";
            if (inDialogue()){
                optionDialogue(options);
            } else if (p().distance(juliet()) < 2){
                juliet().interact("Talk-to");
                MethodProvider.sleepUntil(Dialogues::inDialogue,5000);
                } else {
                GameObject door1 = GameObjects.getTopObjectOnTile(new Tile(3157, 3430, 1));
                GameObject door2 = GameObjects.getTopObjectOnTile(new Tile(3158, 3426, 1));
                if (door1 != null) {
                    door1.interact("Open");
                    MethodProvider.sleepUntil(door1::exists, 2000);
                } else {
                    if (door2 != null) {
                        door2.interact("Open");
                        MethodProvider.sleepUntil(door2::exists, 5000);
                    } else {
                        Walking.walkExact(juliet_tile);
                    }
                }
            }
        } else if (juliet_house.contains(p())){
            var.action = "Heading upstairs";
                stairs().interact("Climb-up");
                MethodProvider.sleepUntil(()-> second_floor.contains(p()), 5000);
            } else if (Walking.getDestinationDistance() < 5){
            var.action = "Heading to Juliet";
                Walking.walkExact(stair_tile);
            }
    }
    
    private void rnjIII() {
        if (cutscene() == 19136){
            var.action = "Watching Cutscene";
            var.percent_complete = "99%";
            if (inDialogue()){
                Dialogues.spaceToContinue();
                optionDialogue(options);
            } else {
                MethodProvider.sleepUntil(Dialogues::inDialogue, 5000);
            }
        } else {
            var.percent_complete = "35%";
            var.action = "Heading to Romeo";
            if (second_floor.contains(p())){
                if (Map.canReach(stairs())){
                    stairs().interact("Climb-down");
                    MethodProvider.sleepUntil(() -> !second_floor.contains(p()), 5000);
                } else {
                    Walking.walkExact(second_stair);
                }
            } else {
                if (romeo() !=null && romeo().distance(p()) < 10){
                    if (inDialogue()){
                        optionDialogue(options);
                    } else {
                        romeo().interact("Talk-to");
                        MethodProvider.sleepUntil(Dialogues::inDialogue, 5000);
                    }
                } else if (Walking.getDestinationDistance() < 5) {
                    Walking.walk(varrock_area.getRandomTile());
                }
            }
        }
        Dialogues.spaceToContinue();
    }
    
    private void rnjIV() {
        var.percent_complete = "50%";
        if (church_area.contains(p())){
            var.action = "Talking to Father";
            if (inDialogue() && Dialogues.canContinue()){
                 optionDialogue(options);
            } else {
                NPC father = NPCs.closest("Father Lawrence");
                father.interact("Talk-to");
                MethodProvider.sleepUntil(()-> Dialogues.inDialogue() && Dialogues.canContinue(),5000);
            }
        } else if (Walking.getDestinationDistance() < 5) {
            var.action = "Heading to Church";
            Walking.walk(church_area.getCenter());
        }
    }
     
    private void rnjV() {
        var.percent_complete = "60%";
            if (pot_area.contains(p())){
                var.action = "Getting Contraband";
                if (inDialogue()){
                    optionDialogue(options);
                } else {
                    NPC apothecary = NPCs.closest("Apothecary");
                    apothecary.interact("Talk-to");
                    MethodProvider.sleepUntil(Dialogues::inDialogue,5000);
                }
            } else if (Walking.getDestinationDistance() < 5) {
                var.action = "Heading to Apothecary";
                Walking.walk(pot_area.getCenter());
            }
    }
    
    private void rnjVI() {
        var.percent_complete = "80%";
        if (inDialogue() && p().distance(juliet()) < 2){
            optionDialogue(options);
            var.action = "Poisoning Juliet";
        } else {
            if (pot_area.contains(p())){
                if (!Inventory.contains("Cadava potion")){
                    if (inDialogue()){
                        optionDialogue(options);
                    }
                } else {
                    Walking.walkExact(stair_tile);
                }
            } else {
                if (juliet_house.contains(p())){
                    var.action = "Heading upstairs";
                    stairs().interact("Climb-up");
                    MethodProvider.sleepUntil(()-> second_floor.contains(p()), 5000);
                } else {
                    if (second_floor.contains(p())){
                        if (inDialogue()){
                            optionDialogue(options);
                        } else {
                            if (p().distance(juliet()) < 2){
                                juliet().interact("Talk-to");
                            } else {
                                GameObject door1 = GameObjects.getTopObjectOnTile(new Tile(3157,3430,1));
                                GameObject door2 = GameObjects.getTopObjectOnTile(new Tile(3158,3426,1));
                                if (door1 !=null){
                                    door1.interact("Open");
                                    MethodProvider.sleepUntil(()-> !door1.hasAction("Open"), 2000);
                                } else {
                                    if (door2 !=null){
                                        door2.interact("Open");
                                        MethodProvider.sleepUntil(()-> !door2.hasAction("Open"), 2000);
                                    } else {
                                        Walking.walkExact(juliet_tile);
                                    }
                                }
                            }
                        }
                    } else if (Walking.getDestinationDistance() < 5){
                        var.action = "Heading to Juliet's House";
                        Walking.walkExact(stair_tile);
                    }
                }
            }
        }
    }

    private GameObject stairs(){
        return GameObjects.closest("Staircase");
    }

    private NPC romeo(){
        return NPCs.closest("Romeo");
    }

    private NPC juliet(){
        return NPCs.closest("Juliet");
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
            MethodProvider.sleepUntil((Dialogues::inDialogue), 5000);
        }
        return optionDialogue(options);
    }

    private Player p(){
        return Players.localPlayer();
    }

    private int progress(){
        return PlayerSettings.getConfig(144);
    }

    private boolean partII(){
        return progress() == 20 || progress() == 60;
    }

    private int cutscene(){
        return PlayerSettings.getConfig(1021);
    }

 }
