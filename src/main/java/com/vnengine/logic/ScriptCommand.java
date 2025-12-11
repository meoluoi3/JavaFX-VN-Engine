package com.vnengine.logic;

public class ScriptCommand {
    public enum Type {DIALOGUE, ACTION}
    private final Type type;
    private final String param1; // Speaker/ Cmd name
    private final String param2; // text / Argument

    public ScriptCommand(Type type, String param1, String param2) {
        this.type = type;
        this.param1 = param1;
        this.param2 = param2;
    }

    public Type getType() {
        return type;
    }

    public String getParam1() {
        return param1;
    }

    public String getParam2() {
        return param2;
    }
}
