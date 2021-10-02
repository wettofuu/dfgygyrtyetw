package quest;

import body.main;
import body.var;

public class imp_catcher extends node{
    public imp_catcher(main main) {
        super(main);
    }

    @Override
    public boolean validate() {
        return var.impS;
    }

    @Override
    public int execute() {
        return 0;
    }
}
