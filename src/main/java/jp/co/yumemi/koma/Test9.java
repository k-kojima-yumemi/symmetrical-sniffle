package jp.co.yumemi.koma;

import org.apache.commons.numbers.core.Addition;

public class Test9 {
    public static void main(String[] args) throws Exception {
        System.out.println("Start Test9");
        System.out.println("Test9Holder init(initialize=true) start");
        try {
            var c21 = Class.forName("jp.co.yumemi.koma.Test9Holder",
                true, ClassLoader.getSystemClassLoader());
            System.out.println("Test9Holder init(initialize=true) end");
        } catch (NoClassDefFoundError error) {
            error.printStackTrace(System.err);
            Thread.sleep(500);
        }
        System.out.println("End Test9");
    }

}

class Test9Holder {
    private static void hasAddition() {
        ImplementNotExistInterface anInterface = new ImplementNotExistInterface();
    }

    public static void use(Addition<?> addition) {
        System.out.println(addition.zero());
    }

    private static void h2() {
        Addition<Void> voidAddition = new Addition<>() {
            @Override
            public Void add(Void a) {
                return null;
            }

            @Override
            public Void zero() {
                return null;
            }

            @Override
            public Void negate() {
                return null;
            }
        };
    }
}
