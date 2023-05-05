package jp.co.yumemi.koma;

import org.apache.commons.numbers.core.Addition;

public class Test2 {
    public static void main(String[] args) {
        System.out.println("Start Test2 instance");
        Test2HolderInstance.doNothing();
        System.out.println("End Test2 instance");

        System.out.println("Start Test2 static");
        Test2HolderStatic.doNothing();
        System.out.println("End Test2 static");
    }
}

class Test2HolderInstance {
    private final Addition<Integer> integerAddition = null;

    static void doNothing() {
    }
}

class Test2HolderStatic {
    private static final Addition<Integer> integerAddition = null;

    static void doNothing() {
    }
}