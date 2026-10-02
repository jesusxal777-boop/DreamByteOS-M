package com.dreambyte.osm;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ShellTest {
    @Test public void helpListsSupportedCommands() {
        String output = MainActivity.Shell.execute("help", null);
        assertTrue(output.contains("pkg update"));
        assertTrue(output.contains("python"));
    }

    @Test public void unknownCommandsAreExplicit() {
        assertTrue(MainActivity.Shell.execute("sudo", null).contains("command not found"));
    }
}
