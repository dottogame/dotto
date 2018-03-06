package com.dotto.cli.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Generator {

    public Element buildButton(String text, BufferedImage parts[], int w) {
        BufferedImage buff = new BufferedImage(
            w, parts[1].getHeight(), BufferedImage.TYPE_INT_ARGB
        );

        Graphics g = buff.getGraphics();
        g.fillRect(0, 0, w, parts[1].getHeight());
        g.drawImage(parts[0], 0, 0, null);
        g.drawImage(parts[1], parts[0].getWidth(), 0, null);
        g.drawImage(
            parts[1], 0, 0, w - parts[2].getWidth(), parts[2].getHeight(), null
        );
        return new Element(buff);
    }

}
