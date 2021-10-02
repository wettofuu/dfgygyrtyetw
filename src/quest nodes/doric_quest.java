package quest;

import body.main;
import body.var;

public class doric_quest extends node{
    public doric_quest(main main) {
        super(main);
    }

    @Override
    public boolean validate() {
        return var.doricS;
    }


    @Override
    public int execute() {

        return 0;
    }
}
