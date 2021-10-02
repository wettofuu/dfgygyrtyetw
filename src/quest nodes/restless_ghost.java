package quest;

import body.main;
import body.var;
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
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import javax.swing.*;


public class restless_ghost extends node{
    Area aereck_area = new Area(3240, 3208, 3247, 3204);
    Area urhney_area = new Area(3151, 3177, 3144, 3173);
    Area grave = new Area(3247, 3195, 3252, 3190);
    Area ghost_basement = new Area(3122, 9555, 3103, 9577);
    Area ghost_altar = new Area(3119, 9566, 3119, 9567);
    Area ghost_ladder = new Area(3103, 9577, 3110, 9575);
    Area wiz_area = new Area(3113, 3171, 3101, 3158);
    private int timer;
    private  int clicked = 0;
    public String[] options = {
            "I'm looking for a quest!",
            "Ok, let me help then.",
            "Yes.",
            "Father Aereck sent me to talk to you.",
            "He's got a ghost haunting his graveyard.",
            "Yep, now tell me what the problem is."
    };

    public restless_ghost(main main) {
        super(main);
    }

    @Override
    public boolean validate() {
        return var.ghostS;
    }

    @Override
    public int execute() {
        timer++;
        if (var.gui_on){
            if (progress() == 0){
                ghostI();
            } else if (progress() == 1){
                ghostII();
            } else if (progress() == 2){
                ghostIII();
            } else if (progress() == 3){
                ghostIV();
            } else if (progress() == 4){
                ghostV();
            } else if (progress() == 5){
                end();
            }
        } else {
            var.gui_title = "Restless Ghost";
            SwingUtilities.invokeLater(var::progress_gui);
            var.gui_on = true;
        }
        return 1000;
    }

    private void ghostV() {
        if (ghost_basement.contains(p())){
            var.action = "Getting out of Basement";
            if (ghost_ladder.contains(p())){
                GameObject ghostLadder = GameObjects.closest("Ladder");
                ghostLadder.interact("Climb-up");
                MethodProvider.sleepUntil(()-> wiz_area.contains(p()), 5000);
            } else if (Walking.getDestinationDistance() < 2){
                Walking.walk(ghost_ladder.getCenter());
            }
        } else {
            if (grave.contains(p())){
                var.action = "Setting Ghost Free";
                GameObject graveCoffin = GameObjects.closest("Coffin");
                if (graveCoffin.hasAction("Open")){
                    graveCoffin.interact("Open");
                    MethodProvider.sleepUntil(()-> !graveCoffin.hasAction("Open"), 5000);
                } else {
                    if (Inventory.interact("Ghost's skull", "Use")) {
                        graveCoffin.interact("Use");
                        MethodProvider.sleep(3000);
                    }
                }
            } else if (Walking.getDestinationDistance() < 5){
                var.percent_complete = "90%";
                Walking.walk(grave.getRandomTile());
            }
        }
    }

    private void ghostIV() {
        if (wiz_area.contains(p())){
            var.percent_complete = "60%";
            var.action = "Going to basement";
            GameObject ladder = GameObjects.getTopObjectOnTile(new Tile(3104,3162));
            if (Map.canReach(ladder)){
                ladder.interact("Climb-down");
                MethodProvider.sleepUntil(()-> ghost_basement.contains(p()),5000);
            } else {
                Walking.walk(ladder);
            }
        } else {
            if (ghost_basement.contains(p())){
                if (ghost_altar.contains(p())){
                    var.percent_complete = "80%";
                    var.action = "Grabbing Skull";
                    GameObject altar = GameObjects.closest("Altar");
                    altar.interact("Search");
                    MethodProvider.sleepUntil(()-> Inventory.contains("Ghost's skull"),5000);
                } else if (Walking.getDestinationDistance() < 4){
                    var.action = "Walking to Altar";
                    Walking.walk(ghost_altar.getRandomTile());
                }
            } else if (Walking.getDestinationDistance() < 5){
                var.action = "Walking to Wizard Tower";
                Walking.walk(wiz_area.getCenter());
            }
        }
    }

    private void ghostIII() {
        if (grave.contains(p())){
            var.percent_complete = "40%";
            var.action = "Chatting with Ghost";
            GameObject deadCoffin = GameObjects.closest("Coffin");
            if (deadCoffin.hasAction("Open")){
                deadCoffin.interact("Open");
            } else {
                if (Inventory.contains("Ghostspeak amulet")) {
                    Inventory.interact("Ghostspeak amulet", "Wear");
                    MethodProvider.sleepUntil(()-> !Inventory.contains("Ghostspeak amulet"), 5000);
                } else if (inDialogue()){
                        optionDialogue(options);
                    } else {
                        NPC restGhost = NPCs.closest("Restless ghost");
                        if (restGhost !=null){
                            restGhost.interact("Talk-to");
                            MethodProvider.sleepUntil(Dialogues::inDialogue,5000);
                        } else {
                            deadCoffin.interact("Search");
                            MethodProvider.sleepUntil(()-> Map.canReach(restGhost), 5000);
                        }
                }
            }
        } else if (Walking.getDestinationDistance() < 5){
            var.percent_complete = "30%";
            var.action = "Walking to Graveyard";
            Walking.walk(grave.getRandomTile());
        }
    }

    private void ghostII() {
        if (urhney_area.contains(p())){
            var.action = "Talking to Urhney";
            if (inDialogue()){
                optionDialogue(options);
            } else {
                NPC urhney = NPCs.closest("Father Urhney");
                urhney.interact("Talk-to");
                MethodProvider.sleepUntil(Dialogues::inDialogue,5000);
            }
        } else if (Walking.getDestinationDistance() < 6){
            var.percent_complete = "10%";
            var.action = "Walking to Urhney";
            Walking.walk(urhney_area.getRandomTile());
        }
    }

    private void ghostI() {
        if (aereck_area.contains(p())){
            var.action = "Talking to Aereck";
            var.percent_complete = "10%";
            if (inDialogue()){
                optionDialogue(options);
            } else {
                NPC aereck = NPCs.closest("Father Aereck");
                aereck.interact("Talk-to");
                MethodProvider.sleepUntil(Dialogues::inDialogue,5000);
            }
        } else if (Walking.getDestinationDistance() < 5){
            var.action = "Walking to Church";
            Walking.walk(aereck_area.getRandomTile());
        }
    }

    private void end() {
        var.percent_complete = "100%";
        WidgetChild screen = Widgets.getWidgetChild(153,16);
        if (clicked == 0){
            if (screen !=null){
                screen.interact("Close");
                clicked = 1;
            } else {
                MethodProvider.sleep(5000);
            }
        } else {
            MethodProvider.log("Finished Restless Ghost in: " + timer);
            var.quest_completed++;
            var.dispose = true;
            var.gui_on = false;
            var.ghostS = false;
        }
    }

    private int progress(){
        return PlayerSettings.getConfig(107);
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


}
