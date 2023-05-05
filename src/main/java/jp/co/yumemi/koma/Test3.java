package jp.co.yumemi.koma;

import org.apache.commons.numbers.core.Addition;

public class Test3 {
    public static void main(String[] args) {
        System.out.println("Test3 access static start");
        if (Test3HolderStatic.integerAddition == null)
            System.out.println("Accessed Test3HolderStatic.integerAddition");
        System.out.println("Test3 access static end");

        System.out.println("Test3 access instance start");
        var i = new Test3HolderInstance();
        if (i.integerAddition == null)
            System.out.println("Accessed Test3HolderInstance.integerAddition");
        System.out.println("Test3 access instance end");
    }
}

class Test3HolderInstance {
    final Addition<Integer> integerAddition = null;

    static void doNothing() {
    }
}

class Test3HolderStatic {
    static final Addition<Integer> integerAddition = null;

    static void doNothing() {
    }
}