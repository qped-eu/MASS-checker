package eu.qped.java.checkers.coverage.framework.ast;

import eu.qped.java.checkers.coverage.enums.StatementType;

import java.util.Objects;

public class AstMethod extends AstResult {
    private final String content;

    public AstMethod(StatementType type, String className, String methodName, int start, int end, String content) {
        super(type, className, methodName, start, end);
        if (! type.equals(StatementType.METHOD) && ! type.equals(StatementType.CONSTRUCTOR))
            throw new IllegalStateException("ERROR::AstMethod.new() parameter type can only be type method or constructor");

        this.content = Objects.requireNonNull(content,"ERROR::AstMethod.new() parameter content can't be null");
    }

    public boolean isConstructor() {
        return type.equals(StatementType.CONSTRUCTOR);
    }

    public String content() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AstMethod astMethod = (AstMethod) o;
        return content.equals(astMethod.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), content);
    }

    @Override
    public String toString() {
        return "AstMethod{" +
                "content='" + content + '\'' +
                ", type=" + type +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }

}
