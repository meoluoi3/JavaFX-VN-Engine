package com.vnengine.logic;

public class ScriptCommand {
    public enum Type {DIALOGUE, ACTION}
    public final Type type;
    public final String param1; // Speaker/ Cmd name
    public final String param2; // text / Argument

    public ScriptCommand(Type type, String param1, String param2) {
        this.type = type;
        this.param1 = param1;
        this.param2 = param2;
    }
}
