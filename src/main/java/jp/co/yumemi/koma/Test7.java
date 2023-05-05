package jp.co.yumemi.koma;

import org.apache.commons.numbers.core.Addition;

public class Test7 {
    public static void main(String[] args) throws Exception {
        System.out.println("Test7 start");

        System.out.println("Test7StaticHolder init(initialize=false) start");
        var c11 = Class.forName("jp.co.yumemi.koma.Test7StaticHolder",
            false, ClassLoader.getSystemClassLoader());
        System.out.println("Test7StaticHolder init(initialize=false) end");

        System.out.println("Test7StaticHolder class call start");
        try {
            var c22 = jp.co.yumemi.koma.Test7StaticHolder.class;
        } catch (NoClassDefFoundError error) {
            error.printStackTrace(System.err);
            Thread.sleep(500);
        }
        System.out.println("Test7StaticHolder class call end");

        System.out.println("Test7StaticHolder init(initialize=true) start");
        try {
            // here
            var c21 = Class.forName("jp.co.yumemi.koma.Test7StaticHolder",
                true, ClassLoader.getSystemClassLoader());
        } catch (NoClassDefFoundError error) {
            error.printStackTrace(System.err);
            Thread.sleep(500);
        }
        System.out.println("Test7StaticHolder init(initialize=true) end");

    }
}

class Test7StaticHolder {
    static Addition<?> getAddition() {
        return new ImplementNotExistInterface();
    }
}
