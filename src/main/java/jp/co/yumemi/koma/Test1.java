package jp.co.yumemi.koma;

/**
 * Call method in a class implementing no-existing interface.
 */
public class Test1 {
    public static void main(String[] args) {
        System.out.println("Start Test1");
        // Here!
        ImplementNotExistInterface.staticMethod();
        System.out.println("End Test1");
    }
}
