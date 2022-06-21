package eu.qped.java.checkers.coverage.feedback;

import java.util.Objects;

abstract class Node<V> {
    final V value;

    Node(V value) {
        this.value = Objects.requireNonNull(value);
    }

    abstract String keyByClass();

    abstract String className();

    abstract String keyByMethod();

    abstract String methodName();

    void insert(ByClass byClass) {
        ByMethod byMethod = byClass.methodByName.get(keyByMethod());
        if (Objects.nonNull(byMethod)) {
            byMethod.insert(this);
        } else {
            byClass.methodByName.put(keyByMethod(), new ByMethod(this));
        }
    }

    abstract void insert(ByMethod byMethod);

}
