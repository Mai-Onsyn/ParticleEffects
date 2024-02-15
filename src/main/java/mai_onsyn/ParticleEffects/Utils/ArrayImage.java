package mai_onsyn.ParticleEffects.Utils;


import java.awt.*;
import java.awt.image.BufferedImage;

public class ArrayImage {
    private final byte[][][] img;
    public ArrayImage(int xLength, int yLength) {
        this.img = new byte[xLength][yLength][4];
        for (int x = 0; x < xLength; x++) {
            for (int y = 0; y < yLength; y++) {
                this.img[x][y] = new byte[] {127, 127, 127, -128};
            }
        }
    }
    public ArrayImage(int xLength, int yLength, Color backGround) {
        this.img = new byte[xLength][yLength][4];
        for (int x = 0; x < xLength; x++) {
            for (int y = 0; y < yLength; y++) {
                this.img[x][y] = new byte[] {(byte) (backGround.getRed() - 128),(byte) (backGround.getGreen() - 128), (byte) (backGround.getBlue() - 128), (byte) (backGround.getAlpha() - 128)};
            }
        }
    }
    public ArrayImage(BufferedImage image) {
        this.img = new byte[image.getWidth()][image.getHeight()][4];
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int clr = image.getRGB(x, y);
                img[x][y][0] = (byte)(((clr >> 16) & 0xff) - 128);
                //System.out.println(((clr >> 16) & 0xff) - 128);
                img[x][y][1] = (byte)(((clr >> 8) & 0xff) - 128);
                img[x][y][2] = (byte)((clr & 0xff) - 128);
                img[x][y][3] = (byte)(((clr >> 24) & 0xff) - 128);
            }
        }
    }
    public void setPixelColor (int x, int y, Color pixel) {
        this.img[x][y][0] = (byte)(pixel.getRed() - 128);
        this.img[x][y][1] = (byte)(pixel.getGreen() - 128);
        this.img[x][y][2] = (byte)(pixel.getBlue() - 128);
        this.img[x][y][3] = (byte)(pixel.getAlpha() - 128);
    }
    public Color getColor (int x, int y) {
        return new Color(this.img[x][y][0] + 128, this.img[x][y][1] + 128, this.img[x][y][2] + 128, this.img[x][y][3] + 128);
    }
    public BufferedImage toBufferedImage () {
        BufferedImage image = new BufferedImage(this.img.length, this.img[0].length, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        for (int x = 0; x < this.img.length; x++) {
            for (int y = 0; y < this.img[0].length; y++) {
                Color color = new Color(this.img[x][y][0] + 128, this.img[x][y][1] + 128, this.img[x][y][2] + 128, this.img[x][y][3] + 128);
                g2d.setColor(color);
                g2d.drawRect(x, y, 1, 1);
            }
        }
        g2d.dispose();
        return image;
    }
    public Color getAverageColor () {
        int[] sum = {0, 0, 0};
        for (byte[][] bytes : img) {
            for (int y = 0; y < img[0].length; y++) {
                sum[0] += bytes[y][0] + 128;
                sum[1] += bytes[y][1] + 128;
                sum[2] += bytes[y][2] + 128;
            }
        }
        int total = img.length*img[0].length;
        return new Color(sum[0] / total, sum[1] / total, sum[2] / total);
    }
    public int getWidth () {
        return img.length;
    }
    public int getHeight () {
        return img[0].length;
    }
    public void setPart ( int startX, int startY, ArrayImage part) {
        try {
            for (int x = 0; x < part.getWidth(); x++) {
                for (int y = 0; y < part.getHeight(); y++) {
                    try {
                        Color c = part.getColor(x, y);
                        this.img[x + startX][y + startY][0] = (byte)(c.getRed() - 128);
                        this.img[x + startX][y + startY][1] = (byte)(c.getGreen() - 128);
                        this.img[x + startX][y + startY][2] = (byte)(c.getBlue() - 128);
                        this.img[x + startX][y + startY][3] = (byte)(c.getAlpha() - 128);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        continue;
                    }
                }
            }
        } catch (NullPointerException e) {
            return;
        }
    }
    public ArrayImage getPart (int sx, int sy, int ex, int ey) {
        ArrayImage out = new ArrayImage(ex - sx + 1, ey - sy + 1);
        for (int x = 0; x < ex - sx + 1 ; x++) {
            for (int y = 0; y < ey - sy + 1 ; y++) {
                try {
                    out.setPixelColor(x, y, new Color(this.img[sx + x][sy + y][0] + 128, this.img[sx + x][sy + y][1] + 128, this.img[sx + x][sy + y][2] + 128, this.img[sx + x][sy + y][3] + 128));
                } catch (ArrayIndexOutOfBoundsException e) {
                    continue;
                }
            }
        }
        return out;
    }
}