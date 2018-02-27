package cn.jcloud.jaf.common.query;

import java.util.HashSet;
import java.util.Set;

/**
 * @author hasayaki
 */
public enum Operator {
    EQ("="),
    NE("!="),
    GT(">"),
    GE(">="),
    LT("<"),
    LE("<="),
    IN("IN"),
    LIKE("LIKE");

    private static final Set<String> SET = new HashSet<>();

    private String value;

    Operator(String value) {
        this.value = value;
    }

    static {
        for (Operator operator : Operator.values()) {
            SET.add(operator.toString());
        }
    }

    public static boolean isValid(String str) {
        return SET.contains(str);
    }

    public String getValue() {
        return this.value;
    }
}