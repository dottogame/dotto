package com.dotto.cli.util;

import java.io.File;
import java.net.URISyntaxException;

import com.dotto.cli.Core;

/**
 * Collection of assorted functions for convenience
 * 
 * @author lite20 (Ephraim Bilson)
 *
 */
public class Util {
    /**
     * Gets the root directory of jar file
     * 
     * @return root directory
     * @throws URISyntaxException
     */
    public static File getLocalDirectory() throws URISyntaxException {
        String path = Core.class.getProtectionDomain().getCodeSource()
            .getLocation().toURI().getPath();
        return new File(path).getParentFile();
    }

    public static String getAssetPath(String string) {
        return Core.rootDirectory.toPath().resolve(string).toString();
    }
}
