package jp.co.yumemi.koma;

import org.apache.commons.numbers.core.Addition;

public class Test8 {
    public static void main(String[] args) throws Exception {
        System.out.println("Test8 start");

        System.out.println("Test8StaticHolder1 init(initialize=true) start");
        try {
            var c21 = Class.forName("jp.co.yumemi.koma.Test8StaticHolder1",
                true, ClassLoader.getSystemClassLoader());
            System.out.println("Test8StaticHolder1 init(initialize=true) end");
        } catch (NoClassDefFoundError error) {
            error.printStackTrace(System.err);
            Thread.sleep(500);
        }

        System.out.println("Test8StaticHolder2 init(initialize=true) start");
        try {
            // here
            var c21 = Class.forName("jp.co.yumemi.koma.Test8StaticHolder2",
                true, ClassLoader.getSystemClassLoader());
            System.out.println("Test8StaticHolder2 init(initialize=true) end");
        } catch (NoClassDefFoundError error) {
            error.printStackTrace(System.err);
            Thread.sleep(500);
        }

        System.out.println("Test8StaticHolder3 init(initialize=true) start");
        try {
            var c21 = Class.forName("jp.co.yumemi.koma.Test8StaticHolder3",
                true, ClassLoader.getSystemClassLoader());
            System.out.println("Test8StaticHolder3 init(initialize=true) end");
        } catch (NoClassDefFoundError error) {
            error.printStackTrace(System.err);
            Thread.sleep(500);
        }
    }
}

class Test8StaticHolder1 {
    static Addition<?> getAddition() {
        return (Addition<?>) ImplementNotExistInterface.returnAsObject();
    }

    static Addition<?> getAddition2() {
        return ImplementNotExistInterface.returnAdditionInstance();
    }
}

class Test8StaticHolder2 {
    static Addition<?> getAddition() {
        return ImplementNotExistInterface.returnImplInstance();
    }
}

class Test8StaticHolder3 {
    static Object getAddition() {
        return ImplementNotExistInterface.returnImplInstance();
    }
}
