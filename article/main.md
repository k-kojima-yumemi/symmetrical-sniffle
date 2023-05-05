# はじめに

世の中にはなぜか、コンパイル時には存在しているのに実行時には存在するかわからないようなクラスを取り扱わなければならないケースがあります。
存在していなければ正常にスキップし、`NoClassDefFoundError`や`ClassNotFoundException`を発生させないようなコードとしなければなりません。
そのためにはどのタイミングでこれらの例外が発生するか知っておく必要があります。

# 前提条件

* 対象のクラスやインターフェイスの存在は例外を発生させずクラス読み込みもしない方法でわかるものとします。
* 今回は主にインターフェイスを扱います。
  * クラスでもほぼ一緒の結果になると思います。
* この記事では`org.apache.commons:commons-math4-core:4.0-beta1`をコンパイル時にのみ読み込ませ、実行時には使用しないようにします。
  * このライブラリの`org.apache.commons.numbers.core.Addition`を実装したりいろいろやります。
* 例に出しているプログラムはパッケージ文など省略しているのでスタックトレースの行数とソースのコードは一致しません。

# ケース

# ソースと結果

## 共通のコード
```java ImplementNotExistInterface.java

import org.apache.commons.numbers.core.Addition;

class ImplementNotExistInterface implements Addition<Integer> {
    static void staticMethod() {
    }

    static Addition<Integer> returnAdditionInstance() {
        return new ImplementNotExistInterface();
    }

    static Object returnAsObject() {
        return new ImplementNotExistInterface();
    }

    @Override
    public Integer add(Integer a) {
        return null;
    }

    @Override
    public Integer zero() {
        return null;
    }

    @Override
    public Integer negate() {
        return null;
    }
}
```

## 1. 何もせずにインターフェイスを実装したクラスのメソッドを呼ぶ
ソース
```java Test1.java

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
```
結果
```
Start Test1
Exception in thread "main" java.lang.NoClassDefFoundError: org/apache/commons/numbers/core/Addition
	at java.base/java.lang.ClassLoader.defineClass1(Native Method)
	at java.base/java.lang.ClassLoader.defineClass(ClassLoader.java:1012)
	at java.base/java.security.SecureClassLoader.defineClass(SecureClassLoader.java:150)
	at java.base/jdk.internal.loader.BuiltinClassLoader.defineClass(BuiltinClassLoader.java:862)
	at java.base/jdk.internal.loader.BuiltinClassLoader.findClassOnClassPathOrNull(BuiltinClassLoader.java:760)
	at java.base/jdk.internal.loader.BuiltinClassLoader.loadClassOrNull(BuiltinClassLoader.java:681)
	at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:639)
	at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:188)
	at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:520)
	at jp.co.yumemi.koma.Test1.main(Test1.java:10)
Caused by: java.lang.ClassNotFoundException: org.apache.commons.numbers.core.Addition
	at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:641)
	at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:188)
	at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:520)
	... 10 more

Process finished with exit code 1
```

## 2. 対象のインターフェイス(`null`)をフィールドに持つクラスのメソッドを呼ぶ
ソース
```java Test2.java
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
```
結果
```
Start Test2 instance
End Test2 instance
Start Test2 static
End Test2 static

Process finished with exit code 0
```
この場合はエラーになりません。

## 3. 対象のインターフェイス(`null`)のフィールドにアクセスする
コード
```java Test3.java
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
```
結果
```
Test3 access static start
Accessed Test3HolderStatic.integerAddition
Test3 access static end
Test3 access instance start
Accessed Test3HolderInstance.integerAddition
Test3 access instance end

Process finished with exit code 0
```
この場合もエラーにはなりません。
`null`で初期化している場合にはアクセスしてもエラーにはならないので値を仮に埋めている際には注意してください。

## 4. 対象のインターフェイス(値あり)のフィールドにアクセスする
ソース
```java
import org.apache.commons.numbers.core.Addition;

public class Test4 {
    public static void main(String[] args) {
        System.out.println("Test4 access static start");
        try {
            // Here!
            if (Test4HolderStatic.integerAddition != null)
                System.out.println("Accessed Test4HolderStatic.integerAddition");
            System.out.println("Test4 access static end");
        } catch (NoClassDefFoundError error) {
            // Requires to go to next test.
            error.printStackTrace(System.err);
        }

        System.out.println("Test4 access instance start");
        // Here!
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
```
結果
```
Test4 access static start
Test4 access instance start
java.lang.NoClassDefFoundError: org/apache/commons/numbers/core/Addition
	at jp.co.yumemi.koma.Test4.main(Test4.java:9)
Caused by: java.lang.ClassNotFoundException: org.apache.commons.numbers.core.Addition
	at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:641)
	at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:188)
	at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:520)
	... 1 more
Exception in thread "main" java.lang.NoClassDefFoundError: org/apache/commons/numbers/core/Addition
	at jp.co.yumemi.koma.Test4.main(Test4.java:17)
Caused by: java.lang.ClassNotFoundException: org.apache.commons.numbers.core.Addition
	at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:641)
	at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:188)
	at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:520)
	... 1 more

Process finished with exit code 1
```
Mainのクラスの呼び出しの手前のprintは呼ばれていますが、インスタンス初期化子や静的初期化子の中のprint文は呼ばれていません。
フィールドの宣言と初期化が初期化子の前に来ているからです。
またクラスにアクセスしたからエラーが発生したとも考えられるので、Test5で検証します。

## 5. 対象のインターフェイス(値あり)のフィールドを持つクラスにアクセスする
ソース
```java Test5.java
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
            // Here!
            var c12 = Class.forName("jp.co.yumemi.koma.Test5HolderStatic",
                true, ClassLoader.getSystemClassLoader());
            System.out.println("Test5 access static class end");
        } catch (NoClassDefFoundError error) {
            error.printStackTrace(System.err);
        }
        // Wait for stack trace printed
        Thread.sleep(500);

        System.out.println("Test5 access instance class start");
        System.out.println("Just access with .class");
        var c2 = Test5HolderInstance.class;
        System.out.println("Access via Class.forName, initialize=false");
        var c21 = Class.forName("jp.co.yumemi.koma.Test5HolderInstance",
            false, ClassLoader.getSystemClassLoader());
        System.out.println("Access via Class.forName, initialize=true");
        // Here!
        var c22 = Class.forName("jp.co.yumemi.koma.Test5HolderInstance",
            true, ClassLoader.getSystemClassLoader());
        System.out.println("Test5 access instance class end");
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

class Test5HolderStatic {
    static final Addition<Integer> integerAddition = new ImplementNotExistInterface();

    static void doNothing() {
    }

    static {
        System.out.println("Test5HolderStatic static initialize");
    }
}
```
結果
```
Test5 access static class start
Just access with .class
Access via Class.forName, initialize=false
Access via Class.forName, initialize=true
java.lang.NoClassDefFoundError: org/apache/commons/numbers/core/Addition
	at java.base/java.lang.Class.forName0(Native Method)
	at java.base/java.lang.Class.forName(Class.java:467)
	at jp.co.yumemi.koma.Test5.main(Test5.java:15)
Caused by: java.lang.ClassNotFoundException: org.apache.commons.numbers.core.Addition
	at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:641)
	at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:188)
	at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:520)
	... 3 more
Test5 access instance class start
Just access with .class
Access via Class.forName, initialize=false
Access via Class.forName, initialize=true
Exception in thread "main" java.lang.NoClassDefFoundError: org/apache/commons/numbers/core/Addition
	at java.base/java.lang.Class.forName0(Native Method)
	at java.base/java.lang.Class.forName(Class.java:467)
	at jp.co.yumemi.koma.Test5.main(Test5.java:31)
Caused by: java.lang.ClassNotFoundException: org.apache.commons.numbers.core.Addition
	at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:641)
	at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:188)
	at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:520)
	... 3 more

Process finished with exit code 1
```
`.class`でアクセスしたり、初期化しないでアクセスする分にはエラーにはなりません。
しかし初期化ありでアクセスするとstatic、インスタンスどちらで値を保持していてもエラーになります。
インスタンスフィールドで、初期化処理が走っていないにもかかわらずエラーになる点には注意が必要です。
初期化ありでアクセスとなるのは`Class#forName(String)`の使用やstaticメソッドの呼び出しがあります。

## 6. 対象のインターフェイスをメソッドの返り値(`null`)に持つクラスを初期化する
ソース
```java
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
        // here
        var c1Methods2 = c1.getDeclaredMethods();
        System.out.println("Test6StaticHolder getDeclaredMethods end");
    }
}

class Test6StaticHolder {
    static Addition<?> getAddition() {
        return null;
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
```
結果
```
Test6 start
Test6StaticHolder init start
Test6StaticHolder init end
Test6MethodHolder init start
Test6MethodHolder init end
Test6 end
Test6StaticHolder get methods start
Test6StaticHolder get methods end
Test6StaticHolder getDeclaredMethods start
Exception in thread "main" java.lang.NoClassDefFoundError: org/apache/commons/numbers/core/Addition
	at java.base/java.lang.Class.getDeclaredMethods0(Native Method)
	at java.base/java.lang.Class.privateGetDeclaredMethods(Class.java:3402)
	at java.base/java.lang.Class.getDeclaredMethods(Class.java:2504)
	at jp.co.yumemi.koma.Test6.main(Test6.java:27)
Caused by: java.lang.ClassNotFoundException: org.apache.commons.numbers.core.Addition
	at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:641)
	at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:188)
	at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:520)
	... 4 more

Process finished with exit code 1
```
対象のインターフェイスを返す(ただし`null`)メソッドを持っていてもそのメソッドの詳細を取得しようとしないかぎりエラーにはならないようです。
publicでなければ`Class#getMethods()`を呼び出してもエラーにはなりません。
`Class#getDeclaredMethods()`で初めてエラーになります。

ここでは`null`を返すようにしていますが、例外を投げるなど対象のインターフェイスに関連しないことをしていればエラーにはなりません。

## 7. 対象のインターフェイスをメソッドの返り値(コンストラクタの呼び出し)に持つクラスを初期化する
ソース
```java
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
```
結果
```
Test7 start
Test7StaticHolder init(initialize=false) start
Test7StaticHolder init(initialize=false) end
Test7StaticHolder class call start
Test7StaticHolder class call end
Test7StaticHolder init(initialize=true) start
java.lang.NoClassDefFoundError: org/apache/commons/numbers/core/Addition
	at java.base/java.lang.Class.forName0(Native Method)
	at java.base/java.lang.Class.forName(Class.java:467)
	at jp.co.yumemi.koma.Test7.main(Test7.java:25)
Caused by: java.lang.ClassNotFoundException: org.apache.commons.numbers.core.Addition
	at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:641)
	at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:188)
	at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:520)
	... 3 more
Test7StaticHolder init(initialize=true) end

Process finished with exit code 0
```
5のフィールドの時と同様、クラスの初期化処理を走らせると実際のメソッドを呼んでいないにもかかわらずエラーとなります。
存在しないインターフェイスを実装したクラスのコンストラクターの呼び出しがあるだけでエラーになるということです。

## 8. 対象のインターフェイスをメソッドの返り値(staticでの呼び出し)に持つクラスを初期化する
ソース
```java
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
```
結果
```
Test8 start
Test8StaticHolder1 init(initialize=true) start
Test8StaticHolder1 init(initialize=true) end
Test8StaticHolder2 init(initialize=true) start
java.lang.NoClassDefFoundError: org/apache/commons/numbers/core/Addition
	at java.base/java.lang.Class.forName0(Native Method)
	at java.base/java.lang.Class.forName(Class.java:467)
	at jp.co.yumemi.koma.Test8.main(Test8.java:22)
Caused by: java.lang.ClassNotFoundException: org.apache.commons.numbers.core.Addition
	at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:641)
	at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:188)
	at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:520)
	... 3 more
Test8StaticHolder3 init(initialize=true) start
Test8StaticHolder3 init(initialize=true) end

Process finished with exit code 0
```