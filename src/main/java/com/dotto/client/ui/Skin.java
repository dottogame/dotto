package com.dotto.client.ui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dotto.client.Core;
import com.dotto.client.util.Util;

/**
 * Class representing and holding all the data of a skin pack.
 * 
 * @author SoraKatadzuma
 */
public final class Skin {
    /** The cursor of this {@code Skin}. */
    public static Graphic cursor;
    /** The grid point image of this {@code Skin}. */
    public static Graphic gridPoint;
    /** The hit circle of this {@code Skin}. */
    public static Graphic hitCircle;
    /** The hit circle overlay of this {@code Skin}. */
    public static Graphic hitCircleOverlay;
    /** The approach circle of this {@code Skin}. */
    public static Graphic approachCircle;
    /** The font to use for numbers */
    public static Font numbers;

    /**
     * Loads in all the skin assets.
     * 
     * @throws IOException
     * @throws FontFormatException
     */
    public static void init() throws FontFormatException, IOException {
        cursor = load(Util.getSkinPath("cursor"));
        gridPoint = load(Util.getSkinPath("grid_plus"));
        hitCircle = load(Util.getSkinPath("hit_circle"));
        hitCircleOverlay = load(Util.getSkinPath("hit_circle_overlay"));
        approachCircle = load(Util.getSkinPath("approach_circle"));
        Font x = Font.createFont(
            Font.TRUETYPE_FONT, new File(Util.getSkinPath("numbers.otf"))
        );

        numbers = x.deriveFont(40.0f);
    }

    /**
     * @param path The absolute path of the {@code Graphic} image we are trying to load.
     * @return The Graphic image that ended up loaded.
     */
    public static Graphic load(String path) {
        Graphic result = null;

        try (Graphic g = new Graphic(path)) {
            result = g;
        } catch (IOException ioe) {
            Logger.getLogger(Skin.class.getName()).log(
                Level.WARNING, "Unable to load skin image: \"" + path + "\"",
                ioe
            );

            String[] filePath = path.split("/");
            String file = filePath[filePath.length - 1];

            try (
                Graphic gStandard = new Graphic(
                    Core.rootDirectory.getAbsolutePath() + "/skins/standard/"
                        + file
                )
            ) {
                result = gStandard;
            } catch (IOException ioex) {
                Logger.getLogger(Skin.class.getName()).log(
                    Level.SEVERE, "Could not load skin or default skin images.",
                    ioex
                );
            } catch (Exception ex) {
                Logger.getLogger(Skin.class.getName()).log(
                    Level.SEVERE, "Resource improperly released.", ex
                );
            }
        } catch (Exception e) {
            Logger.getLogger(Skin.class.getName()).log(
                Level.SEVERE, "Resource improperly released.", e
            );
        }

        return result;
    }
}
