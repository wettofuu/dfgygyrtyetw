package quest;

import body.main;
import body.var;
import org.dreambot.api.methods.MethodProvider;

public class stop_script extends node {
    public stop_script(main main) {
        super(main);
    }

    @Override
    public boolean validate() {
        return !var.romeoS && !var.runeS && !var.cookS && !var.corsairS && !var.ernestS && !var.impS && !var.doricS && !var.ghostS && !var.sheepS && !var.goblinS && !var.xmarkS && !var.witchS && !var.belowS;
    }

    @Override
    public int execute() {
        f.stop();
        return 1000;
    }
}
