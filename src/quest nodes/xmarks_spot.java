package quest;

import body.main;
import body.var;
import org.dreambot.api.methods.MethodContext;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import javax.swing.*;

public class xmarks_spot extends node{
    Area lumbridge_pub = new Area(3227, 3242, 3229, 3241);
    Tile clue_1 = new Tile(3230, 3209, 0);
    Tile clue_2 = new Tile(3203, 3212, 0);
    Tile clue_3 = new Tile(3108, 3264, 0);
    Tile clue_4 = new Tile(3078, 3260, 0);
    Tile clue_tile;
    Area final_clue = new Area(3055, 3245, 3053, 3247);
    Area pen = new Area(3075, 3261, 3079, 3259);
    public xmarks_spot(main main) {
        super(main);
    }

    @Override
    public boolean validate() {
        return var.xmarkS;
    }

    @Override
    public int execute() {
        if (var.gui_on) {
            if (progress() == 583008256) {
                xI();
            } else if (progress() == 583008257) {
                xII();
            } else if (partI()) {
                xIII();
            } else if (progress() == 46137350) {
                xIV();
            } else if (partII()) {
                end();
            }
        }else {
            var.gui_title = "X Marks the Spot";
            SwingUtilities.invokeLater(var::progress_gui);
            var.gui_on = true;
        }
        return 1000;
    }

    private void end() {
        if (inDialogue()){
            optionDialogue(options);
        } else {
            var.percent_complete = "100%";
            WidgetChild screen = Widgets.getWidgetChild(153,16);
            if (screen !=null){
                screen.interact("Close");
            } else {
                var.quest_completed++;
                var.dispose = true;
                var.gui_on = false;
                var.xmarkS = false;
            }
        }
    }

    private void xI() {
        if (lumbridge_pub.contains(p())){
            var.percent_complete = "10%";
            if (inDialogue()){
                optionDialogue(options);
            } else {
                veo().interact("Talk-to");
                MethodProvider.sleepUntil(Dialogues::inDialogue, 5000);
            }
        } else if (Walking.getDestinationDistance() < 5){
            MethodProvider.log("die");
            var.action = "Heading to Lumbridge Pub";
            var.percent_complete = "0%";
            Walking.walk(lumbridge_pub.getCenter());
        }
    }

    private void xII() {
        if (inDialogue()){
            optionDialogue(options);
        } else {
            MethodProvider.sleepUntil(Dialogues::inDialogue, 5000);
        }
    }

    private void xIII() {
        var.action = "Heading to dig spot";
        if (progress() == 583008258){
            var.percent_complete = "30%";
            clue_tile = clue_1;
            digtile();
        } else if (progress() == 583008259){
            var.percent_complete = "50%";
            clue_tile = clue_2;
            digtile();
        } else if (progress() == 583008260){
            var.percent_complete = "70%";
            clue_tile = clue_3;
            digtile();
        } else if (progress() == 583008261) {
            var.percent_complete = "80%";
            clue_tile = clue_4;
            digtile_spec();
        }
    }

    private void digtile_spec() {
        GameObject gate = GameObjects.closest(1562);
        if (p().getTile().equals(new Tile(3078, 3258)) && gate !=null){
            gate.interact("Open");
        } else {
            if (p().getTile().equals(clue_tile)) {
                Inventory.interact("Spade", "Dig");
            }  else if (Walking.getDestinationDistance() < 4){
                Walking.walkExact(clue_tile);
            }
        }
    }

    private void xIV() {
        if (final_clue.contains(f.getLocalPlayer())) {
            var.action = "Finishing final clue";
            var.percent_complete = "90%";
            if (Dialogues.inDialogue()) {
                Dialogues.spaceToContinue();
            } else {
                veo().interact("Talk-to");
                MethodProvider.sleepUntil(Dialogues::inDialogue, 5000);
            }
        } else if (pen.contains(Players.localPlayer())) {
            GameObject gate = GameObjects.closest("Gate");
            if (gate.hasAction("Open")) {
                gate.interact("Open");
                MethodContext.sleep(2000);
            } else if (Walking.getDestinationDistance() < 4) {
                Walking.walk(final_clue.getRandomTile());
            }
        } else if (Walking.getDestinationDistance() < 4) {
            Walking.walk(final_clue.getRandomTile());
        }
    }

    private void digtile(){
        if (p().getTile().equals(clue_tile)){
            Inventory.interact("Spade", "Dig");
        } else if (Walking.getDestinationDistance() < 4){
            Walking.walkExact(clue_tile);
        }
    }

    private Player p() {
        return Players.localPlayer();
    }

    private int progress(){
        return PlayerSettings.getConfig(2111);
    }

    private final String[] options = {
            "I'm looking for a quest.",
            "Sounds good, what should I do?",
            "Yes.",
            "Okay, thanks Veos."
    };

    public boolean inDialogue(){
        return Dialogues.inDialogue();
    }

    private boolean optionDialogue(String[] s){
        if (Dialogues.inDialogue()){
            var.action = "Skipping through dialogue";
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

    private NPC veo() {
        return NPCs.closest("Veos");
    }

    private boolean partI(){
        return progress() == 583008258 || progress() == 583008259 || progress() == 583008260 || progress() == 583008261;
    }

    private boolean partII(){
        return progress() == 46137351 || progress() == 48758792;
    }
}
