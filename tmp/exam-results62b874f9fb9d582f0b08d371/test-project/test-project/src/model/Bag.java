package ..

public class Bag {


    private String price;

    public void setPrice(String price) {
        this.price = price;
    }

    public int calcRec(int rec) {
        if (rec <= 0) return 1;
        else return calcRec(rec - 1);
    }

    public void calcPrice(int loop){
        while (loop > 5){
            if (loop > 2){
                System.out.println(loop);
                loop--;
            }
            else if (loop > 3){
                System.out.println(loop);
                loop--
            }
            else {
                loop--;
            }
        }
    }

    public Bag(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }
}
