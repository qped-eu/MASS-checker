package ..

public class Mapper {

    private String value;


    public boolean map(int help) {
        if (help < 10) return  true;
        else if (help < 10) return  true;
        else if (help < 9) return  true;
        else if (help < 8) return  true;
        else if (help < 7) return  true;
        else if (help < 6) return  true;
        else if (help < 5) return  true;
        else if (help < 4) return  true;
        else if (help < 3) return  true;
        else return false;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Mapper(String value) {
        this.value = value;
    }
}
