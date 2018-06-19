package com.dotto.client.util;

import java.io.File;
import java.net.URISyntaxException;

import com.dotto.client.Core;

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
    public static String getLocal() throws URISyntaxException {
        String path = Core.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

        File result;

        if (Flagger.DEBUG)
            result = new File(new File(path).getParentFile().getParentFile().getPath() + "/bin/");

        else
            result = new File(path).getParentFile().getParentFile();

        return result.getAbsolutePath();
    }

    public static String getSkinPath(String item) throws URISyntaxException {
        return getLocal() + "/skins/" + Config.SKIN + "/" + item;
    }
}
