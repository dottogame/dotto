package com.dotto.cli.util.manager;

import java.util.HashMap;

import com.dotto.cli.ui.Graphic;

/**
 * The {@code GraphicLoader} class is responsible for loading all assets with graphics and store
 * them in a map by their name, making them available at runtime.
 * 
 * @author SoraKatadzuma
 */
public class Graphics {
    /** The {@code Graphic}s that are loaded at runtime mapped to their name. */
    private final HashMap<String, Graphic> LoadedGraphics;

    /**
     * Constructs a new {@code GraphicLoader}.
     */
    public Graphics() {
        LoadedGraphics = new HashMap<>();
    }

    /**
     * Adds a single {@code Graphic} to the map of {@code LoadedGraphics}.
     * 
     * @param Image The image to add.
     */
    public void AddGraphic(Graphic Image) {
        LoadedGraphics.put(Image.toString(), Image);
    }

    /**
     * Returns the {@code Graphic} mapped by the {@code FileName} parameter.
     * 
     * @param FileName The name of the image file being used to collect the {@code Graphic}.
     * @return The {@code Graphic} mapped by {@code FileName} if it exists.
     */
    public Graphic getGraphic(String FileName) {
        return LoadedGraphics.get(FileName);
    }
}
