package quest;

import body.main;
import body.var;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.emotes.Emotes;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.grandexchange.LivePrices;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Map;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.walking.path.impl.LocalPath;
import org.dreambot.api.methods.walking.pathfinding.impl.obstacle.impl.PassableObstacle;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import javax.swing.*;
import java.util.Arrays;

public class below_mount extends node {

    Area grand_exchange = new Area(3161, 3493, 3168, 3486);
    Area willow_area = new Area(2996, 3436, 3010, 3429);
    Area bar = new Area(2955, 3375, 2960, 3367);
    Area bar_perimeter = new Area(2953, 3378, 2961, 3366);
    Area charlie_area = new Area(3207, 3393, 3211, 3390);
    Area cook_area = new Area(3229, 3402, 3232, 3399);
    Area checkal_area = new Area(3084, 3417, 3088, 3412);
    Area atlas_area = new Area(3082, 3436, 3075, 3445);
    Area marley_area = new Area(3091, 3472, 3085, 3468);
    Area ice_mountain = new Area(2993, 3498, 2998, 3488);
    Item food = Inventory.get("Lobster", "Salmon", "Trout");
    Tile[] path = {
            new Tile(3093, 3470, 0),
            new Tile(3093, 3479, 0),
            new Tile(3088, 3486, 0),
            new Tile(3085, 3494, 0),
            new Tile(3081, 3502, 0),
            new Tile(3073, 3508, 0),
            new Tile(3073, 3516, 0),
            new Tile(3064, 3520, 0),
            new Tile(3059, 3514, 0),
            new Tile(3052, 3510, 0),
            new Tile(3051, 3502, 0),
            new Tile(3051, 3494, 0),
            new Tile(3051, 3485, 0),
            new Tile(3051, 3476, 0),
            new Tile(3046, 3471, 0),
            new Tile(3037, 3471, 0),
            new Tile(3031, 3465, 0),
            new Tile(3022, 3459, 0),
            new Tile(3016, 3456, 0),
            new Tile(3007, 3455, 0),
            new Tile(2999, 3457, 0),
            new Tile(2993, 3458, 0),
            new Tile(2988, 3463, 0),
            new Tile(2985, 3468, 0),
            new Tile(2987, 3474, 0),
            new Tile(2991, 3478, 0),
            new Tile(2994, 3484, 0),
            new Tile(2995, 3490, 0),
            new Tile(2996, 3494, 0)
    };
    private Area areaWalk;
    private NPC npcTalk;

    public String[] options = {
            "Yes.",
            "I'll try the Mind Bomb.",
            "Rock.",
            "I'm looking for a man named Marley. Have you seen him?",
            "I was wondering if you'd be able to make me a Steak sandwich?",
    };

    public below_mount(main main) {
        super(main);
    }

    @Override
    public boolean validate() {
        return var.belowS;
    }

    @Override
    public int execute() {
        if (var.gui_on) {
            if (partI()) {
                if (Inventory.contains("Knife")) {
                    var.percent_complete = "7%";
                    questI();
                } else {
                    buyItem();
                    var.percent_complete = "0%";
                }
            } else if (partII()) {
                if (!Inventory.contains("Wizard's mind bomb")) {
                    var.percent_complete = "14%";
                    questII();
                } else {
                    var.percent_complete = "21%";
                    questIII();
                }
            } else if (progress() == 7864330) {
                var.percent_complete = "28%";
                questIV();
            } else if (progress() == 20971535) {
                var.percent_complete = "35%";
                questV();
            } else if (progress() == 20971791) {
                var.percent_complete = "42%";
                questVI();
            } else if (progress() == 20972815) {
                var.percent_complete = "49%";
                questVII();
            } else if (partVIII()) {
                var.percent_complete = "56%";
                questVIII();
            } else if (progress() == 21095695) {
                var.percent_complete = "63%";
                questIX();
            } else if (partX()) {
                var.percent_complete = "70%";
                questX();
            } else if (progress() == 21304340) {
                var.percent_complete = "77%";
                questXI();
            } else if (space_dialogue()) {
                if (inDialogue()){
                    optionDialogue(options);
                } else {
                    MethodProvider.sleepUntil(this::inDialogue, 5000);
                }
            } else if (progress() == 26630435) {
                var.percent_complete = "91%";
                questXII();
            } else if (progress() == 26630440) {
                var.percent_complete = "100%";
                questIV();
            } else if (progress() == 60184952) {
                completed();
            }
        } else {
            var.gui_title = "Below Ice Mountain";
            SwingUtilities.invokeLater(var::progress_gui);
            var.gui_on = true;
        }
        return 1000;
    }

    private void completed() {
        WidgetChild close = Widgets.getChildWidget(153, 16);
        if (close != null) {
            close.interact("Close");
        } else {
            MethodProvider.log("Goodluck Soldier!");
            var.quest_completed++;
            var.dispose = true;
            var.gui_on = false;
            var.belowS = false;
        }
    }

    private void questXII() {
        if (inDialogue()) {
            var.action = "Chatting";
            optionDialogue(options);
        } else {
            var.action = "Fighting Ancient Guardian";
            NPC guardian = NPCs.closest("Ancient Guardian");
            if (Inventory.contains(food) && Players.localPlayer().getHealthPercent() <= 50) {
                food.interact("Eat");
            } else {
                if (!Players.localPlayer().isInCombat()) {
                    if (guardian != null) {
                        guardian.interact("Attack");
                    } else {
                        MethodProvider.log("Waiting...");
                    }
                }
            }
        }
    }

    private void questXI() {
        if (ice_mountain.contains(Players.localPlayer())) {
            areaWalk = ice_mountain;
            npcTalk = NPCs.closest("Willow");
            questing();
        } else if (Walking.getDestinationDistance() < Calculations.random(4, 10)) {
            var.action = "Heading to Ice Mountain";
            LocalPath<Tile> path1 = new LocalPath<>();
            path1.addAll(Arrays.asList(path));
            if (path1.next() != null && Map.canReach(path1.next()) && !ice_mountain.contains(Players.localPlayer())) {
                Walking.walk(path1.next());
            } else {
                Walking.walk(ice_mountain.getCenter());
            }
        }
    }

    private void questX() {
        if (Inventory.contains("Steak sandwich") || marley_area.contains(Players.localPlayer())) {
            areaWalk = marley_area;
            npcTalk = NPCs.closest("Marley");
            questing();
        } else {
            if (Inventory.contains("Cooked meat")){
                var.action = "Combine Food";
                Inventory.interact("Knife", "Use");
                Inventory.interact("Cooked meat", "Use");
                MethodProvider.sleepUntil(() -> Inventory.contains("Steak sandwich"), 3000);
            }
        }
    }

    private void questIX() {
        //everything here sucks, refix later
        if (inDialogue()) {
            var.action = "Chatting";
            optionDialogue(options);
        } else if (Inventory.contains("Cooked meat")) {
            if (checkal_area.contains(Players.localPlayer())) {
                var.action = "Emoting";
                if (Emotes.isTabOpen()) {
                    //cursed fucking shit, fix this later
                    WidgetChild flex = Widgets.getWidgetChild(216, 1, 40);
                    WidgetChild box = Widgets.getWidgetChild(216, 1);
                    box.interact("ha");
                    Mouse.scrollUntil(false, 1000, () -> false);
                    flex.interact("Flex");
                    MethodProvider.sleepUntil(Dialogues::inDialogue, 5000);
                    //cursed fucking shit, fix this later
                } else {
                    Emotes.openTab();
                }
            } else {
                var.action = "Heading to Checkal";
                GameObject longhall = GameObjects.closest(l -> l != null && l.getName().contains("Longhall") && l.hasAction("Open"));
                if (atlas_area.contains(Players.localPlayer()) && longhall != null) {
                    longhall.interact("Open");
                    MethodProvider.sleepUntil(() -> !longhall.hasAction("Open"), 3000);
                } else if (Walking.getDestinationDistance() < 5) {
                    Walking.walk(checkal_area.getRandomTile());
                }
            }
        } else {
            var.action = "Grabbing Meat";
            GroundItem beef = GroundItems.closest(l -> l != null && l.getName().contains("meat"));
            beef.interact("Take");
            MethodProvider.sleepUntil(() -> Inventory.contains("Cooked meat"), 5000);
        }
    }



    private void questing() {
        if (progress() == 21054735 && PlayerSettings.getConfig(2950) == 4) {
            if (inDialogue()) {
                optionDialogue(options);
            } else {
                MethodProvider.sleepUntil(this::inDialogue, 5000);
            }
        } else if (areaWalk.contains(Players.localPlayer())) {
            var.action = "Chatting with NPC";
            if (inDialogue()) {
                optionDialogue(options);
            } else if (npcTalk != null) {
                npcTalk.interact("Talk-to");
                MethodProvider.sleepUntil(Dialogues::inDialogue, 5000);
            } else {
                MethodProvider.sleepUntil(Players.localPlayer()::isStandingStill, 5000);
            }
        } else if (Walking.getDestinationDistance() < Calculations.random(4, 9)) {
            var.action = "Heading to Somewhere";
            Walking.getAStarPathFinder().addObstacle(new PassableObstacle("Longhall door", "Open", null, null, null));
            Walking.walk(areaWalk.getCenter());
        }
    }

    private void questVIII() {
        areaWalk = atlas_area;
        npcTalk = NPCs.closest("Atlas");
        questing();
    }

    private void questVII() {
        areaWalk = checkal_area;
        npcTalk = NPCs.closest("Checkal");
        questing();
    }

    private void questVI() {
        areaWalk = cook_area;
        npcTalk = NPCs.closest("Cook");
        questing();
    }

    private void questV() {
        if (inDialogue()){
            optionDialogue(options);
        } else {
            areaWalk = charlie_area;
            npcTalk = NPCs.closest("Charlie the Tramp");
            questing();
        }
    }

    private void questIV() {
        var.action = "Chatting";
        if (inDialogue()){
            optionDialogue(options);
        } else {
            MethodProvider.sleepUntil(Dialogues::inDialogue,5000);
        }
    }

    private void buyItem() {
        if (grand_exchange.contains(Players.localPlayer())) {
            var.action = "Buying GE items";
            if (Inventory.count("Coins") < 1000 && !GrandExchange.isOpen()) {
                var.action = "Getting GP";
                if (Bank.isOpen()) {
                    int currentCoins = Inventory.count("Coins");
                    Bank.withdraw("Coins", 1000 - currentCoins);
                } else {
                    NPC banker = NPCs.closest(l -> l != null && l.getName().equals("Banker"));
                    banker.interact("Bank");
                    MethodProvider.sleepUntil(Bank::isOpen, 5000);
                }
            } else if (Bank.isOpen()) {
                Bank.close();
            } else if (GrandExchange.isBuyOpen()) {
                if (!Inventory.contains("Bread")) {
                    GrandExchange.buyItem("Bread", 1, breadPrice() + 50);
                    MethodProvider.sleepUntil(GrandExchange::isReadyToCollect, 5000);
                } else if (!Inventory.contains("Knife")) {
                    GrandExchange.buyItem("Knife", 1, knifePrice() + 50);
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

    private void questI() {
        areaWalk = willow_area;
        npcTalk = NPCs.closest("Willow");
        questing();
    }

    private void questII() {
        if (bar_perimeter.contains(Players.localPlayer())){
            if (inDialogue()){
                var.action = "Chatting to Kaylee";
                optionDialogue(options);
            } else {
                NPC kaylee = NPCs.closest("Kaylee");
                kaylee.interact("Talk-to");
                MethodProvider.sleepUntil(Dialogues::inDialogue,5000);
            }
        } else if (Walking.getDestinationDistance() < Calculations.random(4,6)) {
            var.action = "Heading to Kaylee";
            Walking.walk(bar.getCenter());
        }
    }

    private void questIII() {
        areaWalk = bar_perimeter;
        npcTalk = NPCs.closest("Burntof");
        questing();
    }

    private int breadPrice(){
        return LivePrices.getHigh("Bread");
    }

    private int knifePrice(){
        return LivePrices.getHigh("Knife");
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

    private boolean partI(){
        return progress() == 0 || progress() == 7 || progress() == 5;
    }
    private boolean partII(){
        return progress() == 10 || progress() == 2621450 || progress() == 5242890;
    }
    private boolean partVIII(){
        return progress() == 21013775 || progress() == 21054735;
    }
    private boolean partX(){
        return progress() == 21300495 || progress() == 21303695 || progress() == 210304335;
    }
    private boolean space_dialogue(){
        return progress() == 21304345 || progress() == 23967390;
    }
    private int progress(){
        return PlayerSettings.getConfig(2951);
    }
}
