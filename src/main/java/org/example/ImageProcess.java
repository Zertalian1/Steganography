package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;

public class ImageProcess {

    /**
     * Метод зашифровывающий сообщение в последнем бите синего цвета
     * @param img входное изображение
     * @param text текст, который необходимо зашифровать
     * @return изображение с закодированым сообщением
     */
    static BufferedImage hideText(BufferedImage img , byte [] text) {
        int i = 0;
        int j = 0;
        int maxTextLen = img.getHeight()*img.getWidth()/8;
        if ( maxTextLen < text.length) {
            System.out.printf("Вводимый текст превышает максимальный размер текста, который можно закодировать, будут закодированы первые %d символов \n", maxTextLen);
        }
        for (int g = 0; g < text.length && g<maxTextLen; g++) {
            byte b = text[g];
            // идём по b и записываем его повитово
            for (int k=7;k>=0;k--) {
                if (j == img.getWidth()) {
                    i++;
                    j=0;
                }
                Color c = new Color(img.getRGB(j,i));
                // засовывать биты будем в синий цвет
                byte blue = (byte)c.getBlue();
                // берём очередной бит
                int bitVal = (b >>> k) & 1;
                // пихаем полученный бит на последнюю позицию
                blue = (byte)((blue & 0xFE)| bitVal);
                // перезаписываем
                Color newColor = new Color(c.getRed(), c.getGreen(),(blue & 0xFF));
                img.setRGB(j,i,newColor.getRGB());
                j++;
            }
        }
        return img;
    }

    /**
     * Метод находящий зашифрованное сообщение
     * @param img изображение
     * @param size размер сообщения, при передачи null преабразует весь файл
     * @return найденный текст
     */
    static String extractHiddenBytes(BufferedImage img , Integer size) {
        int i = 0;
        int j = 0;
        int maxSize = img.getHeight()*img.getWidth()/8;
        if (size == null || size > maxSize) {
            size = maxSize;
        }
        byte [] hiddenBytes = new byte[size];
        for(int l=0;l<size;l++){
            for(int k=0 ; k<8 ; k++){
                if (j == img.getWidth()) {
                    i++;
                    j=0;
                }
                Color c = new Color(img.getRGB(j,i));
                byte blue = (byte)c.getBlue();
                hiddenBytes[l] = (byte) ((hiddenBytes[l]<<1)|(blue&1));
                j++;
            }
        }
        return new String(hiddenBytes, StandardCharsets.UTF_8);

    }
}
