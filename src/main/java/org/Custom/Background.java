package org.Custom;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class Background extends PanelRound {

    RenderingHints hints;
    Timer timer = new Timer(1, (ActionEvent ae) -> {
        repaint();
    });

    private Color waveColor = Color.BLACK;
    private int waveX = 100;
    private int waveY = 20;
    private int waveSmooth = 15;
    private int wavePosition = 60;
    private int waveRoundLength = 25;
    private int waveLength = 720;

    public Color getWaveColor() {
        return waveColor;
    }

    public void setWaveColor(Color waveColor) {
        this.waveColor = waveColor;
    }

    public int getWaveX() {
        return waveX;
    }

    public void setWaveX(int waveX) {
        this.waveX = waveX;
    }

    public int getWaveY() {
        return waveY;
    }

    public void setWaveY(int waveY) {
        this.waveY = waveY;
    }

    public int getWaveSmooth() {
        return waveSmooth;
    }

    public void setWaveSmooth(int waveSmooth) {
        this.waveSmooth = waveSmooth;
    }

    public int getWavePosition() {
        return wavePosition;
    }

    public void setWavePosition(int wavePosition) {
        this.wavePosition = wavePosition;
    }

    public int getRoundLength() {
        return waveRoundLength;
    }

    public void setRoundLength(int roundLength) {
        this.waveRoundLength = roundLength;
    }

    public int getWaveLength() {
        return waveLength;
    }

    public void setWaveLength(int waveLength) {
        this.waveLength = waveLength;
    }

    public Background() {
        hints = new RenderingHints(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        hints.put(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        hints.put(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        hints.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        intervalStart = 0;
        timer.start();
    }

    int intervalStart;

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHints(hints);
        super.paintComponent(g2);

        int intervalEnd = intervalStart + waveLength;

        Area area = new Area(createRoundTopRight(waveRoundLength));
        area.intersect(new Area(createRoundBottomRight(waveRoundLength)));

        g2.setColor(waveColor);
        paintGraph(g2, area, intervalStart, intervalEnd);

        intervalStart--;
    }

    private void paintGraph(Graphics2D g2, Area a, int intervalStart, int intervalEnd) {
        int width = getWidth();
        double X = 0;
        double LX, LY;
        double Y = (int) (width * wavePosition / 100) + (Math.sin((intervalStart - 1) / (Math.PI * waveSmooth)) * waveY);

        Area area = new Area();

        for (int i = intervalStart; i < intervalStart + waveRoundLength; i++) {
            LX = X;
            LY = Y;

            X += waveX / 80;
            Y = (int) (width * wavePosition / 100) + (Math.sin((i / (Math.PI * waveSmooth))) * waveY);

            int[] x = {(int) LX, (int) X, (int) X, (int) LX};
            int[] y = {(int) LY, (int) Y, width, width};

            area.add(new Area(new Polygon(y, x, 4)));
        }
        area.intersect(a);
        g2.fill(area);
        area = new Area();

        for (int i = intervalStart + waveRoundLength; i < intervalEnd - waveRoundLength; i++) {
            LX = X;
            LY = Y;

            X += waveX / 80;
            Y = (int) (width * wavePosition / 100) + (Math.sin((i / (Math.PI * waveSmooth))) * waveY);

            int[] x = {(int) LX, (int) X, (int) X, (int) LX};
            int[] y = {(int) LY, (int) Y, width, width};

            g2.fillPolygon(y, x, 4);
        }

        for (int i = intervalEnd - waveRoundLength; i < intervalEnd; i++) {
            LX = X;
            LY = Y;

            X += waveX / 80;
            Y = (int) (width * wavePosition / 100) + (Math.sin((i / (Math.PI * waveSmooth))) * waveY);

            int[] x = {(int) LX, (int) X, (int) X, (int) LX};
            int[] y = {(int) LY, (int) Y, width, width};

            area.add(new Area(new Polygon(y, x, 4)));
        }
        area.intersect(a);
        g2.fill(area);
    }

    private Shape createRoundTopRight(int round) {
        int width = getWidth();
        int height = getHeight();
        int roundX = Math.min(width, round);
        int roundY = Math.min(height, round);
        Area area = new Area(new RoundRectangle2D.Double(0, 0, width, height, roundX, roundY));
        area.add(new Area(new Rectangle2D.Double(0, 0, width - roundX / 2, height)));
        area.add(new Area(new Rectangle2D.Double(0, roundY / 2, width, height - roundY / 2)));
        return area;
    }

    private Shape createRoundBottomRight(int round) {
        int width = getWidth();
        int height = getHeight();
        int roundX = Math.min(width, round);
        int roundY = Math.min(height, round);
        Area area = new Area(new RoundRectangle2D.Double(0, 0, width, height, roundX, roundY));
        area.add(new Area(new Rectangle2D.Double(0, 0, width - roundX / 2, height)));
        area.add(new Area(new Rectangle2D.Double(0, 0, width, height - roundY / 2)));
        return area;
    }

}
