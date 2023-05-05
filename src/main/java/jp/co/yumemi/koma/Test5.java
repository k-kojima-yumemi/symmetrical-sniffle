package jp.co.yumemi.koma;

import org.apache.commons.numbers.core.Addition;

public class Test5 {
    public static void main(String[] args) throws ReflectiveOperationException, InterruptedException {
        System.out.println("Test5 access static class start");
        try {
            System.out.println("Just access with .class");
            var c1 = Test5HolderStatic.class;
            System.out.println("Access via Class.forName, initialize=false");
            var c11 = Class.forName("jp.co.yumemi.koma.Test5HolderStatic",
                false, ClassLoader.getSystemClassLoader());
            System.out.println("Access via Class.forName, initialize=true");
            var c12 = Class.forName("jp.co.yumemi.koma.Test5HolderStatic",
                true, ClassLoader.getSystemClassLoader());
            System.out.println("Test5 access static class end");
        } catch (NoClassDefFoundError error) {
            error.printStackTrace(System.err);
            // Wait for stack trace printed
            Thread.sleep(500);
        }

        System.out.println("Test5 access instance class start");
        System.out.println("Just access with .class");
        var c2 = Test5HolderInstance.class;
        System.out.println("Access via Class.forName, initialize=false");
        var c21 = Class.forName("jp.co.yumemi.koma.Test5HolderInstance",
            false, ClassLoader.getSystemClassLoader());
        System.out.println("Access via Class.forName, initialize=true");
        try {
            var c22 = Class.forName("jp.co.yumemi.koma.Test5HolderInstance",
                true, ClassLoader.getSystemClassLoader());
            System.out.println("Test5 access instance class end");
        } catch (NoClassDefFoundError error) {
            error.printStackTrace(System.err);
            Thread.sleep(500);
        }

        System.out.println("Test5 access instance class2 start");
        try {
            var c22 = Class.forName("jp.co.yumemi.koma.Test5HolderInstance2",
                true, ClassLoader.getSystemClassLoader());
            System.out.println("Test5 access instance class2 end");
        } catch (NoClassDefFoundError error) {
            error.printStackTrace(System.err);
            Thread.sleep(500);
        }
    }
}

class Test5HolderInstance {
    final Addition<Integer> integerAddition = new ImplementNotExistInterface();

    static void doNothing() {
    }

    {
        System.out.println("Test5HolderInstance initialize");
    }
}

class Test5HolderInstance2 {
    final Addition<Integer> integerAddition = ImplementNotExistInterface.returnAdditionInstance();

    static void doNothing() {
    }

    static {
        System.out.println("Test5HolderInstance2 static initialize");
    }
}

class Test5HolderStatic {
    static final Addition<Integer> integerAddition = new ImplementNotExistInterface();

    static void doNothing() {
    }

    static {
        System.out.println("Test5HolderStatic static initialize");
    }
}
