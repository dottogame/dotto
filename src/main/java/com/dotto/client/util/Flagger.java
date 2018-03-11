package com.dotto.client.util;

/**
 * Responsible for setting flags that the command line argues.
 * 
 * @author SoraKatadzuma
 */
public final class Flagger {
    /** Tells whether the application should run in debug mode. */
    private static boolean DEBUG = true;

    /**
     * Takes in the command line arguments and sets flags that correspond to their names.
     * 
     * @param cla The command line arguments.
     */
    public static void setFlags(String... cla) {
        for (String arg : cla)
            switch (arg) {
            case "debug":
                DEBUG = true;
                break;
            }
    }

    /**
     * Returns whether or not the application is in debug mode.
     * 
     * @return Whether or not the application is in debug mode.
     */
    public static boolean DebugMode() {
        return DEBUG;
    }
}
