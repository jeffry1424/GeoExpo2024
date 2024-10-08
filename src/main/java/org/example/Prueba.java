package org.example;

import be.tarsos.dsp.*;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchProcessor;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;

public class Prueba extends JPanel {
    private float[] audioBuffer;
    private RenderingHints hints;
    private float currentFrequency = 0.0f;
    private float currentAmplitude = 0.0f;
    private boolean inversa = false;

    public boolean isInversa() {
        return inversa;
    }

    public void setInversa(boolean inversa) {
        this.inversa = inversa;
    }

    public Prueba() {
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

        this.audioBuffer = new float[0];
    }

    public void setAudioBuffer(float[] audioBuffer) {
        this.audioBuffer = audioBuffer;
        currentAmplitude = calculateAmplitude(audioBuffer);
        repaint();
    }

    private float calculateAmplitude(float[] buffer) {
        float sum = 0;
        for (float value : buffer) {
            sum += Math.abs(value);
        }
        return sum / buffer.length;
    }

    public void setCurrentFrequency(float frequency) {
        this.currentFrequency = frequency;
        repaint();
    }

    private float[] invertAudioBuffer(float[] buffer) {
        float[] invertedBuffer = new float[buffer.length];
        for (int i = 0; i < buffer.length; i++) {
            invertedBuffer[i] = -buffer[i];  
        }
        return invertedBuffer;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHints(hints);

        GradientPaint gradient = new GradientPaint(0, 0, Color.BLACK, getWidth(), getHeight(), Color.DARK_GRAY);
        g2.setPaint(gradient);
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setStroke(new BasicStroke(2f));
        g2.setColor(new Color(135, 206, 235));

        int width = getWidth();
        int height = getHeight();
        int middle = height / 2;

        g2.drawLine(0, middle, width, middle);  // Línea media

        if (!inversa) {

        if (audioBuffer != null && audioBuffer.length > 1) {
            for (int i = 0; i < audioBuffer.length - 1; i++) {
                int x1 = (i * width) / audioBuffer.length;
                int x2 = ((i + 1) * width) / audioBuffer.length;
                int y1 = middle - (int) (audioBuffer[i] * middle);
                int y2 = middle - (int) (audioBuffer[i + 1] * middle);

                g2.setColor(new Color(0, 191, 255, 120)); // Onda original
                g2.drawLine(x1, y1 - 2, x2, y2 - 2);
                g2.setColor(new Color(70, 130, 180));
                g2.drawLine(x1, y1, x2, y2);
            }
        }
        }

        if (inversa) {
            float[] invertedBuffer = invertAudioBuffer(audioBuffer);
        if (invertedBuffer != null && invertedBuffer.length > 1) {
            for (int i = 0; i < invertedBuffer.length - 1; i++) {
                int x1 = (i * width) / invertedBuffer.length;
                int x2 = ((i + 1) * width) / invertedBuffer.length;
                int y1 = middle - (int) (invertedBuffer[i] * middle);
                int y2 = middle - (int) (invertedBuffer[i + 1] * middle);

                g2.setColor(new Color(255, 69, 0, 120));  // Onda invertida (color rojo)
                g2.drawLine(x1, y1 - 2, x2, y2 - 2);
                g2.setColor(new Color(255, 99, 71));  // Onda invertida
                g2.drawLine(x1, y1, x2, y2);
            }
        }
        }

        g2.setColor(Color.WHITE);
        g2.drawString("Frecuencia: " + currentFrequency + " Hz", 10, 20);
        g2.drawString("Amplitud: " + currentAmplitude, 10, 40);

        g2.dispose();
    }
}
