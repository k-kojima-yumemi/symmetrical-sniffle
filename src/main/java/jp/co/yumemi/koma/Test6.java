package jp.co.yumemi.koma;

import org.apache.commons.numbers.core.Addition;

public class Test6 {
    public static void main(String[] args) throws Exception {
        System.out.println("Test6 start");

        System.out.println("Test6StaticHolder init start");
        var c1 = Test6StaticHolder.class;
        var c11 = Class.forName(c1.getCanonicalName(), true, c1.getClassLoader());
        Test6StaticHolder.doNothing();
        System.out.println("Test6StaticHolder init end");
        Thread.sleep(500);

        System.out.println("Test6MethodHolder init start");
        var c2 = Test6MethodHolder.class;
        var c21 = Class.forName(c2.getCanonicalName(), true, c2.getClassLoader());
        Test6MethodHolder.doNothing();
        System.out.println("Test6MethodHolder init end");
        System.out.println("Test6 end");

        System.out.println("Test6StaticHolder get methods start");
        var c1Methods = c1.getMethods();
        System.out.println("Test6StaticHolder get methods end");
        System.out.println("Test6StaticHolder getDeclaredMethods start");
        var c1Methods2 = c1.getDeclaredMethods();
        System.out.println("Test6StaticHolder getDeclaredMethods end");
    }
}

class Test6StaticHolder {
    static Addition<?> getAddition() {
        throw new AssertionError();
    }

    public static void doNothing() {
    }
}

class Test6MethodHolder {
    Addition<?> getAddition() {
        return null;
    }

    public static void doNothing() {
    }
}
