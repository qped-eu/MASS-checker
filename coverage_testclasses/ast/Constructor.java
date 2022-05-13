package test;

public class Constructor {
    private final int arg;

    public Constructor(int arg) {
        this.arg = arg;
    }

    public Constructor() {
        this(0);
    }

    public int add(int num) {
        return arg + num;
    }

}
