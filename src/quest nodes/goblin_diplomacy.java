package quest;

import body.main;
import body.var;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.grandexchange.LivePrices;
import org.dreambot.api.methods.input.Camera;
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

public class goblin_diplomacy extends node{
    Area grand_exchange = new Area(3161, 3493, 3168, 3486);
    final Area crate_one = new Area(2959,3498,2954,3496,2);
    final Tile ladder_tile = new Tile(2955,3497);
    final Area crate_two = new Area(2952,3508,2952,3505);
    final Area crate_three = new Area(2959,3515,2960,3514);
    private String[] options = {
            "What about a different colour?",
            "Do you want me to pick an armour colour for you?",
            "No, he doesn't look fat",
    };
    private boolean dye;
    public goblin_diplomacy(main main) {
        super(main);
    }

    @Override
    public boolean validate() {
        return var.goblinS;
    }

    @Override
    public int execute() {
        if (var.gui_on) {
            if (progress() == 0) {
                if (dye){
                    var.percent_complete = "10%";
                    goblinI();
                } else if (Inventory.contains("Orange dye")){
                    dye = true;
                } else {
                    buy_items();
                }
            } else if (progress() == 256) {
                var.percent_complete = "30%";
                goblinII();
            } else if (progress() == 384) {
                var.percent_complete = "50%";
                goblinIII();
            } else if (progress() == 448) {
                var.percent_complete = "70%";
                goblinIV();
            } else if (partI()) {
                goblinV();
            } else if (progress() == 454) {
                end();
            }
        } else {
            var.gui_title = "Goblin Diplomacy";
            SwingUtilities.invokeLater(var::progress_gui);
            var.gui_on = true;
        }
        return 1000;
    }

    private void goblinV() {
        if (Camera.getPitch() == 157) {
            var.action = "In cutscene";
            if (inDialogue() && Dialogues.canContinue()) {
                optionDialogue(options);
            } else {
                MethodProvider.sleepUntil(this::inDialogue, 5000);
            }
        } else if (Dialogues.inDialogue()) {
            var.action = "Chatting";
            optionDialogue(options);
        } else if (Inventory.contains("Orange goblin mail")) {
            var.action = "Interacting";
            var.percent_complete = "80%";
            if (Inventory.interact("Orange goblin mail", "Use")) {
                general().interact("Use");
                MethodProvider.sleepUntil(()-> Camera.getPitch() == 157, 5000);
            }
        } else if (Inventory.contains("Blue goblin mail")) {
            var.action = "Interacting";
            var.percent_complete = "85%";
            if (Inventory.interact("Blue goblin mail", "Use")) {
                general().interact("Use");
                MethodProvider.sleepUntil(()-> Camera.getPitch() == 157, 5000);
            }
        } else if (Inventory.contains("Goblin mail")) {
            var.action = "Interacting";
            var.percent_complete = "90%";
            if (Inventory.interact("Goblin mail", "Use")) {
                general().interact("Use");
                MethodProvider.sleepUntil(()-> Camera.getPitch() == 157, 5000);
            }
        }
    }

    private void goblinIV() {
        if (Map.canReach(general())) {
            if (Inventory.contains("Blue dye")) {
                var.action = "Combining";
                if (Inventory.interact("Blue dye", "Use")) {
                    Inventory.interact("Goblin mail", "Use");
                }
            } else if (Inventory.contains("Orange dye")) {
                var.action = "Combining";
                if (Inventory.interact("Orange dye", "Use")) {
                    Inventory.interact("Goblin mail", "Use");
                }
            } else if (inDialogue()) {
                var.action = "Chatting";
                optionDialogue(options);
            } else {
                var.action = "Interacting";
                general().interact("Talk-to");
                MethodProvider.sleepUntil(this::inDialogue, 5000);
            }
        } else {
            var.action = "Heading to General";
            GameObject largedoor = GameObjects.closest("Large door");
            largedoor.interact("Open");
            MethodProvider.sleepUntil(()-> Map.canReach(general()), 5000);
        }
    }

    private void goblinIII() {
        if (crate_three.contains(f.getLocalPlayer())) {
            var.action = "Searching Crate";
            crate().interact("Search");
            MethodProvider.sleep(Calculations.random(1000, 3000));
        } else if (Walking.getDestinationDistance() < Calculations.random(3, 6)) {
            var.action = "Heading to crate";
            Walking.walk(crate_three.getCenter());
        }
    }

    private void goblinII() {
        if (crate_one.contains(f.getLocalPlayer())) {
            ladder().interact("Climb-down");
            MethodProvider.sleepUntil(() -> !crate_one.contains(p()), 5000);
        } else if (crate_two.contains(f.getLocalPlayer())) {
            var.action = "Searching Crate";
            crate().interact("Search");
            MethodProvider.sleep(Calculations.random(1000, 3000));
        } else if (Walking.getDestinationDistance() < Calculations.random(3, 6)) {
            var.action = "Heading to crate";
            Walking.walk(crate_two.getCenter());
        }
    }

    private void goblinI() {
        if (crate_one.contains(p())) {
            var.action = "Searching Crate";
            crate().interact("Search");
            MethodProvider.sleep(Calculations.random(1000,3000));
        } else if (p().getTile().equals(ladder_tile)) {
            ladder().interact("Climb-up");
            MethodProvider.sleepUntil(()-> crate_one.contains(p()), 5000);
        } else if (Walking.getDestinationDistance() < Calculations.random(3,6)) {
            var.action = "Heading to Ladder";
            Walking.walkExact(ladder_tile);
        }
    }

    private void buy_items() {
            if (grand_exchange.contains(p())) {
                var.action = "Buying GE items";
                if (Inventory.count("Coins") < bluePrice()+orangePrice() && !GrandExchange.isOpen()) {
                    var.action = "Getting GP";
                    if (Bank.isOpen()) {
                        int currentCoins = Inventory.count("Coins");
                        Bank.withdraw("Coins", bluePrice()+orangePrice());
                    } else {
                        NPC banker = NPCs.closest(l -> l != null && l.getName().equals("Banker"));
                        banker.interact("Bank");
                        MethodProvider.sleepUntil(Bank::isOpen, 5000);
                    }
                } else if (Bank.isOpen()) {
                    Bank.close();
                } else if (GrandExchange.isBuyOpen()) {
                    if (!Inventory.contains("Blue dye")) {
                        GrandExchange.buyItem("Blue dye", 1, bluePrice() + 50);
                        MethodProvider.sleepUntil(GrandExchange::isReadyToCollect, 5000);
                    } else if (!Inventory.contains("Orange dye")) {
                        GrandExchange.buyItem("Orange dye", 1, orangePrice() + 50);
                        MethodProvider.sleepUntil(GrandExchange::isReadyToCollect, 5000);
                    }
                } else if (GrandExchange.isOpen()) {
                    if (GrandExchange.isReadyToCollect()) {
                        GrandExchange.collect();
                    } else {
                        int open = GrandExchange.getFirstOpenSlot();
                        GrandExchange.openBuyScreen(open);
                    }
                } else {
                    GameObject booth = GameObjects.closest(l -> l != null && l.getName().contains("booth") && l.hasAction("Exchange"));
                    booth.interact("Exchange");
                    MethodProvider.sleepUntil(GrandExchange::isOpen, 3000);
                }
            } else if (Walking.getDestinationDistance() < 5) {
                var.action = "Heading to the GE";
                Walking.walk(grand_exchange.getRandomTile());
        }
    }

    private void end() {
        var.percent_complete = "100%";
        WidgetChild screen = Widgets.getWidgetChild(153, 16);
        if (screen != null) {
            screen.interact("Close");
        } else {
            var.quest_completed++;
            var.dispose = true;
            var.gui_on = false;
            var.goblinS = false;
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
            MethodProvider.sleepUntil((Dialogues::inDialogue), 2000);
        }
        return optionDialogue(options);
    }

    private int bluePrice(){
        return LivePrices.getHigh("Blue dye");
    }
    private int orangePrice(){
        return LivePrices.getHigh("Orange dye");
    }
    private int progress(){
        return PlayerSettings.getConfig(62);
    }

    private Player p(){
        return Players.localPlayer();
    }

    private GameObject crate(){
        return GameObjects.closest("Crate");
    }

    private GameObject ladder(){
        return GameObjects.closest("Ladder");
    }

    private NPC general(){
        return NPCs.closest("General Wartface");
    }

    private boolean partI(){
        return progress() == 451 || progress() == 452 || progress() == 453;
    }
}
