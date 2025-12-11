package com.vnengine.logic;

import javafx.css.Match;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScriptParser {
    // regex: "Speaker: Text"
    private static final Pattern DIALOGUE_PATTERN = Pattern.compile("^(\\w+)\\s*:\\s*(.+)$");
    // regex: "[command argument]"
    private static final Pattern COMMAND_PATTERN = Pattern.compile("^\\[(\\w+)\\s+(.+)\\]$");

    public List <ScriptCommand> parse(String script) {
        List <ScriptCommand> commands = new ArrayList<>();
        String[] lines = script.split("\n");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;

            Matcher cmdMatcher = COMMAND_PATTERN.matcher(line);
            if (cmdMatcher.find()) {
                commands.add(new ScriptCommand(
                        ScriptCommand.Type.ACTION,
                        cmdMatcher.group(1),
                        cmdMatcher.group(2).replace("\"", "")
                ));
                continue;
            }

            Matcher diaMatcher = DIALOGUE_PATTERN.matcher(line);
            if (diaMatcher.find()) {
                commands.add(new ScriptCommand(
                        ScriptCommand.Type.DIALOGUE,
                        diaMatcher.group(1),
                        diaMatcher.group(2)
                ));
                continue;
            }

            if (!commands.isEmpty()) {
                int lastIndex = commands.size() - 1;
                ScriptCommand lastCommand = commands.get(lastIndex);

                if (ScriptCommand.Type.DIALOGUE == lastCommand.getType()) {
                    String mergedText = lastCommand.getParam2() + " " + line;

                    ScriptCommand mergedCmd = new ScriptCommand(
                            ScriptCommand.Type.DIALOGUE,
                            lastCommand.getParam1(),
                            mergedText
                    );

                    commands.set(lastIndex,mergedCmd);
                    continue;
                }
            }
        }
        return commands;
    }
}
