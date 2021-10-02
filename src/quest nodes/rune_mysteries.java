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

public class rune_mysteries extends node{
    public rune_mysteries(main main) {
        super(main);
    }
    Area duke_room = new Area(3209,3219,3212,3225,1);
    Area basement = new Area(3092, 9581, 3125, 9551);
    Area wiz_area = new Area(3113, 3171, 3101, 3158);
    Area varrock_area = new Area(3255, 3398, 3250, 3406);
    Area ladder_area = new Area(3103, 9577, 3110, 9575);
    private final String[] options = {
            "Yes, certainly.",
            "Have you any quests for me?",
            "Sure, no problem.",
            "I'm looking for the head wizard.",
            "Ok, here you are.",
            "I have been sent here with a package for you.",
            "Yes.",
    };
    @Override
    public boolean validate() {
        return var.runeS;
    }

    @Override
    public int execute() {
        if (var.gui_on) {
            if (progress() == 0){
                runeI();
            } else if (partI()){
                runeII();
            } else if (partII()){
                runeIII();
            } else if (progress() == 6){
                end();
            }
        } else {
            var.gui_title = "Rune Mysteries";
            SwingUtilities.invokeLater(var::progress_gui);
            var.gui_on = true;
        }
        return 1000;
    }

    private void runeIII() {
        if (varrock_area.contains(p())) {
            if (inDialogue()){
                optionDialogue(options);
            } else {
                var.action = "Talking to Aubury";
                GameObject door = GameObjects.closest("Door");
                if (Map.canReach(aubury())) {
                    aubury().interact("Talk-to");
                    MethodProvider.sleepUntil(Dialogues::inDialogue, 5000);
                } else {
                    door.interact("Open");
                }
            }
        } else if (basement.contains(p())) {
            var.action = "Escaping basement";
            if (ladder_area.contains(p())) {
                GameObject ghostLadder = GameObjects.closest("Ladder");
                ghostLadder.interact("Climb-up");
                MethodProvider.sleepUntil(() -> wiz_area.contains(p()), 5000);
            } else if (Walking.getDestinationDistance() < 2) {
                Walking.walk(ladder_area.getCenter());
            }
        } else if (Walking.getDestinationDistance() < 5){
            var.action = "Heading to Varrock";
            Walking.walk(varrock_area.getCenter());
        }
    }

    private void runeII() {
        if (basement.contains(p())) {
            if (inDialogue()) {
                optionDialogue(options);
            } else {
                var.action = "Talking to Sedridor";
                NPC sedridor = NPCs.closest("Sedridor");
                if (Map.canReach(sedridor)) {
                    sedridor.interact("Talk-to");
                    MethodProvider.sleepUntil(Dialogues::inDialogue, 5000);
                } else {
                    Walking.walk(sedridor);
                }
            }
        } else if (wiz_area.contains(p())) {
            var.action = "Heading to Basement";
            GameObject ladder = GameObjects.getTopObjectOnTile(new Tile(3104, 3162));
            if (Map.canReach(ladder)) {
                ladder.interact("Climb-down");
                MethodProvider.sleepUntil(() -> basement.contains(p()), 5000);
            } else {
                Walking.walk(ladder);
            }
        } else if (Walking.getDestinationDistance() < 4) {
            var.action = "Heading to Wizard Tower";
            Walking.walk(wiz_area.getCenter());
        }
    }

    private void runeI() {
        NPC duke = NPCs.closest("Duke Horacio");
        if (Map.canReach(duke)){
            var.action = "Talking to Horacio";
            var.percent_complete = "10%";
            if (inDialogue()){
                optionDialogue(options);
            } else {
                duke.interact("Talk-to");
                MethodProvider.sleepUntil(Dialogues::inDialogue, 5000);
            }
        } else if (Walking.getDestinationDistance() < 2){
                Walking.walk(duke_room.getCenter());
        }
    }

    private void end() {
        if (basement.contains(p())) {
            var.action = "Escaping basement";
            if (ladder_area.contains(p())) {
                GameObject ghostLadder = GameObjects.closest("Ladder");
                ghostLadder.interact("Climb-up");
                MethodProvider.sleepUntil(() -> wiz_area.contains(p()), 5000);
            } else if (Walking.getDestinationDistance() < 2) {
                Walking.walk(ladder_area.getCenter());
            }
        } else {
            var.percent_complete = "100%";
            WidgetChild screen = Widgets.getWidgetChild(153, 16);
            if (screen != null) {
                screen.interact("Close");
            } else {
                var.quest_completed++;
                var.dispose = true;
                var.gui_on = false;
                var.runeS = false;
            }
        }
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
        return PlayerSettings.getConfig(63);
    }

    private NPC aubury(){
        return NPCs.closest("Aubury");
    }

    private boolean partII(){
        return progress() == 3 || progress() == 4;
    }

    private boolean partI(){
        return progress() == 1 || progress() == 2 || progress() == 5;
    }

}
