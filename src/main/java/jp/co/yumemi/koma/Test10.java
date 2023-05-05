package jp.co.yumemi.koma;

public class Test10 {
    public static void main(String[] args) {
        System.out.println("Test10 start");
        if (true) {
            System.out.println("In if block");
        } else {
            ImplementNotExistInterface a = new ImplementNotExistInterface();
        }
        System.out.println("Test10 end");
    }
}
