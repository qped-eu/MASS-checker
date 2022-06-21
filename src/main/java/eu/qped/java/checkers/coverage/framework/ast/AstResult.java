package eu.qped.java.checkers.coverage.framework.ast;

import eu.qped.java.checkers.coverage.enums.StatementType;

import java.util.Objects;

public class AstResult {
    protected StatementType type;
    protected final String className;
    protected final String methodName;
    protected final int start;
    protected int end;

    public AstResult(StatementType type,
                     String className,
                     String methodName,
                     int start,
                     int end) {

        this.type = Objects.requireNonNull(type, "ERROR::AstResult.new() parameter type can't be null");
        if (! (Objects.nonNull(className) && ! className.isBlank()))
            throw new IllegalStateException("ERROR::AstResult.new() parameter methodName can't be null or blank");
        this.className = className;
        if (! (Objects.nonNull(methodName) && ! methodName.isBlank()))
            throw new IllegalStateException("ERROR::AstResult.new() parameter methodName can't be null or blank");
        this.methodName = methodName;
        if (end < start)
            throw new IllegalStateException("ERROR::AstResult.new() parameter end  can't be bigger then start");
        this.start = start;
        this.end = end;
    }

    public StatementType type() {
        return type;
    }

    public String className() {
        return className;
    }

    public String methodName() {
        return methodName;
    }

    public int start() {
        return start;
    }

    public int end() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AstResult astResult = (AstResult) o;
        return start == astResult.start && end == astResult.end && type == astResult.type && className.equals(astResult.className) && methodName.equals(astResult.methodName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, className, methodName, start, end);
    }

    @Override
    public String toString() {
        return "AstResult{" +
                "type=" + type +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }

}
