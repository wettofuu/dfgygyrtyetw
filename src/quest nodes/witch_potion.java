package quest;

import body.main;
import body.var;

public class witch_potion extends node{
    public witch_potion(main main) {
        super(main);
    }

    @Override
    public boolean validate() {
        return var.witchS;
    }

    @Override
    public int execute() {
        return 0;
    }
}
