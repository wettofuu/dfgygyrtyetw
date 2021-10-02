package quest;

import body.main;

public abstract class node {
    protected final body.main f;
    public node(main main){
        this.f = main;
    }
    public abstract boolean validate();


    public abstract int execute();
}