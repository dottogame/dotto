package com.dotto.cli.ui;

import com.dotto.cli.Core;
import com.dotto.cli.util.Util;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class representing and holding all the data of a skin pack.
 * 
 * @author SoraKatadzuma
 */
public final class Skin {
    /** The loaded skin. */
    private static Skin thisSkin;
    /** The cursor of this {@code Skin}. */
    private final Graphic cursor;
    /** The grid point image of this {@code Skin}. */
    private final Graphic gridPoint;
    /** The hit circle of this {@code Skin}. */
    private final Graphic hitCircle;
    /** The hit circle overlay of this {@code Skin}. */
    private final Graphic hitCircleOverlay;
    /** The approach circle of this {@code Skin}. */
    private final Graphic approachCircle;
    
    /** Loads in all the skin assets. */
    private Skin() {
        cursor = load(Util.getSkinPath("cursor"));
        gridPoint = load(Util.getSkinPath("grid_plus"));
        hitCircle = load(Util.getSkinPath("hit_circle"));
        hitCircleOverlay = load(Util.getSkinPath("hit_circle_overlay"));
        approachCircle = load(Util.getSkinPath("approach_circle"));
    }
    
    /**
     * Initializes the new skin.
     */
    public static void Initialize() {
        thisSkin = new Skin();
    }
    
    /**
     * @param path
     * @return
     */
    public static Graphic load(String path) {
        Graphic result = null;
        
        try (Graphic g = new Graphic(path)) {
            result = g;
        } catch (IOException ioe) {
            String[] filePath = path.split("/");
            String file = filePath[filePath.length - 1];
            
            try (
                Graphic gStandard = new Graphic(
                    Core.rootDirectory.getAbsolutePath() + "\\data\\skins\\standard\\" + file
                )
            ) {
                result = gStandard;
            } catch (IOException ioex) {
                Logger.getLogger(Skin.class.getName())
                    .log(Level.SEVERE, "Could not load skin or default skin images.", ioex);
            } catch (Exception ex) {
                Logger.getLogger(Skin.class.getName())
                    .log(Level.SEVERE, "Resource improperly released.", ex);
            }
            
            Logger.getLogger(Skin.class.getName())
                    .log(Level.WARNING, "Unable to load skin image: \"" + path + "\"", ioe);
        } catch (Exception e) {
            Logger.getLogger(Skin.class.getName())
                .log(Level.SEVERE, "Resource improperly released.", e);
        }
        
        return result;
    }
    
    /**
     * @return 
     */
    public static Graphic getCursor() {
        return thisSkin.cursor;
    }
    
    /**
     * @return 
     */
    public static Graphic getGridPoint() {
        return thisSkin.gridPoint;
    }
    
    /**
     * @return
     */
    public static Graphic getHitCircle() {
        return thisSkin.hitCircle;
    }
    
    /**
     * @return
     */
    public static Graphic getHitCircleOverlay() {
        return thisSkin.hitCircleOverlay;
    }
    
    /**
     * @return
     */
    public static Graphic getApproachCircle() {
        return thisSkin.approachCircle;
    }
}
