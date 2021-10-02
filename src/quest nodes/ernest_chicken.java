package quest;

import body.*;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodContext;
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

public class ernest_chicken extends node {
    Area veronica_area = new Area(3112, 3328, 3108, 3332);
    Area mansion = new Area(3127, 3352, 3091, 3374);
    Area mansion_door = new Area(3109, 3353, 3108, 3352);
    Area mansion_second = new Area(3098, 3373, 3118, 3354, 1);
    Area mansion_third = new Area(3104, 3370, 3112, 3362, 2);
    Area poison_area = new Area(3100, 3366, 3097, 3364);
    Area ladder_area = new Area(3096, 3354, 3091, 3363);
    Tile lever_B = new Tile(3118, 9752);
    Tile lever_A = new Tile(3108, 9745);
    Tile lever_C = new Tile(3112, 9760);
    Tile lever_D = new Tile(3108, 9767);
    Tile lever_F = new Tile(3096, 9765);
    Tile lever_E = new Tile(3097, 9767);
    Area basement = new Area(3118, 9745, 3090, 9767);
    Area oilcan_area = new Area(3099, 9753, 3090, 9757);
    Area basement_ladder = new Area(3118, 9755, 3116, 9753);
    Tile shovel = new Tile(3121, 3359);
    Area patch_area = new Area(3084, 3359, 3089, 3362);
    Area fountain = new Area(3086, 3333, 3089, 3336);
    Area rubber = new Area(3108, 3368, 3112, 3366);
    private float timer;
    private String lever_name;
    private Tile lever_tile;
    String[] options = {
            "Aha, sounds like a quest. I'll help.",
            "I'm looking for a guy called Ernest.",
            "Change him back this instant!",
    };

    public ernest_chicken(main main) {
        super(main);
    }

    @Override
    public boolean validate() {
        return var.ernestS;
    }
    
    @Override
    public int execute() {
        timer++;
        if (var.gui_on) {
            if (progress() == 0) {
                ernestI();
            } else if (progress() == 1) {
                ernestII();
            } else if (progress() == 2) {
                if (Inventory.contains("Oil can")) {
                    ernestIV();
                } else {
                    ernestIII();
                }
            } else if (progress() == 3) {
                end();
            }
        } else {
            var.gui_title = "Ernest The Chicken";
            SwingUtilities.invokeLater(var::progress_gui);
            var.gui_on = true;
        }
        return 1000;
    }

    private void end() {
        var.percent_complete = "100%";
        var.action = "Walking to Reset Point";
        WidgetChild screen = Widgets.getWidgetChild(153,16);
        if (screen !=null){
            screen.interact("Close");
        } else if (mansion_third.contains(p())){
            GameObject s = GameObjects.closest("Staircase");
            s.interact("Climb-down");
            MethodProvider.sleepUntil(() -> mansion_second.contains(p()), 5000);
        } else if (mansion_second.contains(p())) {
            GameObject s = GameObjects.closest(11499);
            s.interact("Climb-down");
            MethodProvider.sleepUntil(() -> mansion.contains(p()), 5000);
        } else if (mansion.contains(p())) {
            if (Walking.getDestinationDistance() < 5) {
                Walking.walk(veronica_area.getRandomTile());
            }
        } else if (inDialogue()) {
            optionDialogue(options);
        } else {
            MethodProvider.log("Finished Ernest the Chicken in: " + Math.round(timer/60));
            var.quest_completed++;
            var.dispose = true;
            var.gui_on = false;
            var.ernestS = false;
        }
    }

    private void ernestIV() {
        if (basement.contains(p())) {
            var.action = "Heading to First Floor";
            if (basement_ladder.contains(p())) {
                GameObject l = GameObjects.closest("Ladder");
                l.interact("Climb-up");
                MethodProvider.sleepUntil(() -> !basement.contains(p()), 5000);
            } else if (oilcan_area.contains(p())) {
                GameObject door = GameObjects.getTopObjectOnTile(new Tile(3100, 9755));
                door.interact("Open");
                MethodContext.sleep(3000);
            } else if (Walking.getDestinationDistance() < 4) {
                Walking.walk(basement_ladder.getRandomTile());
            }
        } else if (!Inventory.contains("Spade")) {
            var.percent_complete = "50%";
            var.action = "Retrieving Spade";
            if (ladder_area.contains(p())) {
                GameObject lever = GameObjects.closest("Lever");
                if (lever.interact("Pull")) {
                    MethodProvider.sleepUntil(() -> !ladder_area.contains(p()), Calculations.random(5000, 7000));
                }
            } else {
                GroundItem spade = GroundItems.closest("Spade");
                if (Map.canReach(spade)) {
                    if (spade.interact("Take")) {
                        MethodProvider.sleepUntil(() -> Inventory.contains("Spade"), Calculations.random(4000, 5000));
                    }
                } else if (Walking.getDestinationDistance() < 5) {
                    Walking.walk(shovel);
                }
            }
        } else if (!Inventory.contains("Key")) {
            var.percent_complete = "60%";
            var.action = "Retrieving Key";
            GameObject compost = GameObjects.closest("Compost heap");
            if (patch_area.contains(p())) {
                if (Inventory.interact("Spade", "Use")) {
                    if (compost.interact("Use")) {
                        MethodProvider.sleep(Calculations.random(2000, 5000));
                    }
                }
            } else if (Walking.getDestinationDistance() < 5) {
                Walking.walk(patch_area.getCenter());
            }
        } else if (!Inventory.contains("Pressure gauge")) {
            var.percent_complete = "70%";
            var.action = "Retrieving Pressure Gauge";
            if (fountain.contains(p())) {
                GameObject fountain = GameObjects.closest("Fountain");
                if (Inventory.contains("Poisoned fish food")) {
                    if (Inventory.interact("Poisoned fish food", "Use")) {
                        if (fountain.interact("Use")) {
                            MethodProvider.sleep(Calculations.random(4000, 6000));
                        }
                    }
                } else {
                    if (inDialogue()) {
                        optionDialogue(options);
                    } else {
                        fountain.interact("Search");
                    }
                }
            } else if (Walking.getDestinationDistance() < 5) {
                Walking.walk(fountain.getRandomTile());
            }
        } else if (!Inventory.contains("Rubber tube")) {
            var.percent_complete = "80%";
            var.action = "Retrieving Rubber Tube";
            if (mansion.contains(p())) {
                if (mansion_door.contains(p())) {
                    GameObject d = GameObjects.closest("Large door");
                    d.interact("Open");
                    MethodProvider.sleep(3000);
                } else {
                    if (rubber.contains(p())) {
                        GroundItem r = GroundItems.closest("Rubber tube");
                        r.interact("Take");
                    } else {
                        Walking.walk(rubber.getCenter());
                    }
                }
            } else if (Walking.getDestinationDistance() < 5) {
                Walking.walk(mansion_door.getCenter());
            }
        } else if (mansion.contains(p())) {
            var.percent_complete = "90%";
            if (rubber.contains(p())) {
                var.action = "Revitalizing Ernest";
                GameObject door = GameObjects.closest(131);
                door.interact("Open");
                MethodProvider.sleepUntil(() -> !rubber.contains(p()), 5000);
            } else if (Map.canReach(first_staircase())) {
                first_staircase().interact("Climb-up");
                MethodProvider.sleepUntil(() -> mansion_second.contains(p()), 5000);
            }
        } else if (mansion_second.contains(p())) {
            if (second_staircase().interact("Climb-up")) {
                MethodProvider.sleepUntil(() -> mansion_third.contains(p()), Calculations.random(5000, 6000));
            }
        } else if (mansion_third.contains(p())) {
            if (inDialogue()) {
                optionDialogue(options);
            } else {
                NPC odden = NPCs.closest("Professor Oddenstein");
                if (Map.canReach(odden)) {
                    odden.interact("Talk-to");
                } else if (Walking.getDestinationDistance() < 2) {
                    Walking.walk(odden);
                }
            }
        } else if (Walking.getDestinationDistance() < 6) {
            Walking.walk(mansion_door.getRandomTile());
        }
    }

    private void ernestIII() {
        if (mansion_third.contains(p())) {
            var.action = "Heading to Second Floor";
            if (Map.canReach(third_staircase())) {
                third_staircase().interact("Climb-down");
                MethodProvider.sleepUntil(() -> mansion_second.contains(p()), 5000);
            } else {
                Walking.walk(third_staircase());
            }
        } else if (mansion_second.contains(p())) {
            if (Inventory.contains("Fish food")) {
                var.percent_complete = "20%";
                var.action = "Heading to First Floor";
                GameObject s = GameObjects.closest(11499);
                if (Map.canReach(s)) {
                    if (s.interact("Climb-down")) {
                        MethodProvider.sleepUntil(() -> mansion.contains(p()), Calculations.random(5000, 7000));
                    }
                } else if (Walking.getDestinationDistance() < 2) {
                    Walking.walk(s);
                }
            } else {
                var.action = "Grabbing Fish Food";
                GroundItem fish = GroundItems.closest("Fish food");
                if (Map.canReach(fish)) {
                    if (fish.interact("Take")) {
                        MethodProvider.sleepUntil(() -> Inventory.contains("Fish food"), Calculations.random(5000, 7000));
                    }
                } else if (Walking.getDestinationDistance() < 2) {
                    Walking.walk(fish);
                }
            }
        } else if (mansion.contains(p())) {
            if (Inventory.contains("Poison")) {
                var.percent_complete = "30%";
                if (Inventory.interact("Poison", "Use")) {
                    Inventory.interact("Fish food", "Use");
                    MethodProvider.sleep(1000);
                }
            } else if (Inventory.contains("Poisoned fish food")) {
                var.percent_complete = "40%";
                var.action = "Heading to Basement";
                if (ladder_area.contains(p())) {
                    GameObject l = GameObjects.closest("Ladder");
                    if (l.interact("Climb-down")) {
                        MethodProvider.sleepUntil(() -> !ladder_area.contains(p()), Calculations.random(5000, 6000));
                    }
                } else {
                    GameObject bookCase = GameObjects.closest(155);
                    if (Map.canReach(bookCase)) {
                        if (bookCase.interact("Search")) {
                            MethodProvider.sleepUntil(() -> ladder_area.contains(p()), Calculations.random(5000, 6000));
                        }
                    } else if (Walking.getDestinationDistance() < 4) {
                        Walking.walk(bookCase);
                    }
                }
            } else if (poison_area.contains(p())) {
                var.action = "Getting poison";
                GroundItem p = GroundItems.closest("Poison");
                p.interact("Take");
            } else if (Walking.getDestinationDistance() < 4) {
                var.action = "Getting poison";
                Walking.walk(poison_area.getRandomTile());
            }
        } else if (basement.contains(p())) {
            lever();
        }
    }

    private void lever() {
        var.action = "Executing Lever Puzzle";
        if (lever_progress() == 0) {
            var.percent_complete = "41%";
            lever_name = "Lever B";
            lever_tile = lever_B;
            execute_lever();
        } else if (lever_progress() == 4) {
            var.percent_complete = "42%";
            lever_name = "Lever A";
            lever_tile = lever_A;
            execute_lever();
        } else if (lever_progress() == 6) {
            var.percent_complete = "43%";
            lever_name = "Lever D";
            lever_tile = lever_D;
            execute_lever();
        } else if (lever_progress() == 22) {
            var.percent_complete = "44%";
            lever_name = "Lever B";
            lever_tile = lever_B;
            execute_lever();
        } else if (lever_progress() == 18) {
            var.percent_complete = "45%";
            lever_name = "Lever A";
            lever_tile = lever_A;
            execute_lever();
        } else if (lever_progress() == 16) {
            var.percent_complete = "46%";
            lever_name = "Lever F";
            lever_tile = lever_F;
            execute_lever();
        } else if (lever_progress() == 80) {
            var.percent_complete = "47%";
            lever_name = "Lever E";
            lever_tile = lever_E;
            execute_lever();
        } else if (lever_progress() == 112) {
            var.percent_complete = "48%";
            lever_name = "Lever C";
            lever_tile = lever_C;
            execute_lever();
        } else if (lever_progress() == 120) {
            var.percent_complete = "49%";
            lever_name = "Lever E";
            lever_tile = lever_E;
            execute_lever();
        } else if (lever_progress() == 88 && !Inventory.contains("Oil can")) {
            GroundItem oilCan = GroundItems.closest("Oil can");
            if (oilcan_area.contains(p())) {
                if (oilCan.interact("Take")) {
                    MethodProvider.sleepUntil(() -> Inventory.contains("Oil can"), Calculations.random(4000, 6000));
                }
            } else if (Walking.getDestinationDistance() < 5) {
                Walking.walk(oilcan_area.getCenter());
            }
        }
    }

    private void execute_lever(){
        if (p().getTile().equals(lever_tile)){
            if (lever_object().interact("Pull")){
                MethodProvider.sleep(3000);
            }
        } else if (Walking.getDestinationDistance() < 5){
            Walking.walk(lever_tile);
        }
    }

    private void ernestII() {
        var.percent_complete = "10%";
        if (mansion.contains(p())) {
            var.action = "Going to Third Floor";
            if (mansion_door.contains(p())) {
                GameObject largeDoor = GameObjects.closest(l -> l != null && l.getTile().equals(new Tile(3108, 3353)) && l.getName().contains("Large"));
                largeDoor.interact("Open");
                MethodProvider.sleep(3000);
            } else if (Map.canReach(first_staircase())) {
                first_staircase().interact("Climb-up");
                MethodProvider.sleepUntil(() -> mansion_second.contains(p()), 5000);
            } else if (Walking.getDestinationDistance() < 2) {
                Walking.walk(first_staircase());
            }
        } else {
            if (mansion_second.contains(p())){
                var.action = "Going to Third Floor";
                if (second_staircase().interact("Climb-up")){
                    MethodProvider.sleepUntil(()-> mansion_third.contains(p()), 5000);
                }
            } else {
                if (mansion_third.contains(p())){
                    var.action = "Talking To Professor";
                    NPC odden = NPCs.closest("Professor Oddenstein");
                    if (inDialogue()){
                        optionDialogue(options);
                    } else if (Map.canReach(odden)){
                            odden.interact("Talk-to");
                            MethodProvider.sleepUntil(Dialogues::inDialogue, 5000);
                        } else if (Walking.getDestinationDistance() < 2){
                                Walking.walk(odden);
                        }
                } else if (Walking.getDestinationDistance() < 6) {
                    Walking.walk(mansion_door.getRandomTile());
                }
            }
        }
    }

    private void ernestI() {
        var.action = "Initiating Quest";
        var.percent_complete = "0%";
        if (inDialogue()){
            optionDialogue(options);
        } else {
            if (veronica_area.contains(p())){
                NPC veronica = NPCs.closest("Veronica");
                veronica.interact("Talk-to");
                MethodProvider.sleepUntil(Dialogues::inDialogue, 5000);
            } else if (Walking.getDestinationDistance() < Calculations.random(4,7)){
                Walking.walk(veronica_area.getRandomTile());
            }
        }
    }

    private int progress(){
        return PlayerSettings.getConfig(32);
    }
    private int lever_progress(){
        return PlayerSettings.getConfig(33);
    }

    private Player p(){
        return Players.localPlayer();
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

    private GameObject first_staircase(){
        return GameObjects.closest(11498);
    }

    private GameObject second_staircase(){
        return GameObjects.closest(11511);
    }

    private GameObject third_staircase(){
        return GameObjects.closest(9584);
    }

    private GameObject lever_object(){
        return GameObjects.closest(lever_name);
    }


}
