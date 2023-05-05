package jp.co.yumemi.koma;

import org.apache.commons.numbers.core.Addition;

class ImplementNotExistInterface implements Addition<Integer> {
    static void staticMethod() {
    }

    static Addition<Integer> returnAdditionInstance() {
        return new ImplementNotExistInterface();
    }

    static ImplementNotExistInterface returnImplInstance() {
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
