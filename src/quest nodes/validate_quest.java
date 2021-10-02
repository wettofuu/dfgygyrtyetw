package quest;

import body.main;
import body.var;
import org.dreambot.api.methods.MethodProvider;



public class validate_quest extends node{

    public validate_quest(main main) {
        super(main);
    }
    @Override
    public boolean validate() {
        return var.check_quest;
    }

    @Override
    public int execute() {
            if (var.quest_list.contains("romeo")) var.romeoS = true;
            if (var.quest_list.contains("rune")) var.runeS = true;
            if (var.quest_list.contains("cook")) var.cookS = true;
            if (var.quest_list.contains("corsair")) var.corsairS = true;
            if (var.quest_list.contains("ernest")) var.ernestS = true;
            if (var.quest_list.contains("imp")) var.impS = true;
            if (var.quest_list.contains("doric")) var.doricS = true;
            if (var.quest_list.contains("ghost")) var.ghostS = true;
            if (var.quest_list.contains("sheep")) var.sheepS = true;
            if (var.quest_list.contains("goblin")) var.goblinS = true;
            if (var.quest_list.contains("xmarks")) var.xmarkS = true;
            if (var.quest_list.contains("witch")) var.witchS = true;
            if (var.quest_list.contains("icemount")) var.belowS = true;
            MethodProvider.log(var.quest_list);
            var.check_quest = false;
        return 1000;
    }
}
