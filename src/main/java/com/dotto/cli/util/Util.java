package com.dotto.cli.util;

import com.dotto.cli.Core;
import java.io.File;
import java.net.URISyntaxException;

/**
 * Collection of assorted functions for convenience.
 * 
 * @author lite20 (Ephraim Bilson)
 * @author SoraKatadzuma
 */
public class Util {
    /**
     * Gets the root directory of jar file.
     * 
     * @return root directory
     * @throws URISyntaxException
     */
    public static File getLocalDirectory() throws URISyntaxException {
        String path = Core.class.getProtectionDomain().getCodeSource()
            .getLocation().toURI().getPath();

        File result;

        if (Flagger.DebugMode()) result = new File(
            new File(path).getParentFile().getParentFile().getPath() + "/bin/"
        );

        else result = new File(path).getParentFile().getParentFile();

        return result;
    }

    /**
     * Gets the path to an asset.
     * 
     * @param string The asset name.
     * @return String path to the asset.
     */
    public static String getAssetPath(String string) {
        return Core.rootDirectory.toPath().resolve(string).toString();
    }
}
