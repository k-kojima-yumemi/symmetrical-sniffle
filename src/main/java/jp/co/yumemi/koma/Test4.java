package jp.co.yumemi.koma;

import org.apache.commons.numbers.core.Addition;

public class Test4 {
    public static void main(String[] args) {
        System.out.println("Test4 access static start");
        try {
            if (Test4HolderStatic.integerAddition != null)
                System.out.println("Accessed Test4HolderStatic.integerAddition");
            System.out.println("Test4 access static end");
        } catch (NoClassDefFoundError error) {
            error.printStackTrace(System.err);
        }

        System.out.println("Test4 call static method");
        Test4HolderInstance.doNothing();
        System.out.println("Test4 access instance start");
        var i = new Test4HolderInstance();
        if (i.integerAddition != null)
            System.out.println("Accessed Test4HolderInstance.integerAddition");
        System.out.println("Test4 access instance end");
    }
}

class Test4HolderInstance {
    final Addition<Integer> integerAddition = new ImplementNotExistInterface();

    static void doNothing() {
    }

    {
        System.out.println("Test4HolderInstance initialize");
    }
}

class Test4HolderStatic {
    static final Addition<Integer> integerAddition = new ImplementNotExistInterface();

    static void doNothing() {
    }

    static {
        System.out.println("Test4HolderStatic static initialize");
    }
}