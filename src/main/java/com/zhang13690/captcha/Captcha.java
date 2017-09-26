package com.zhang13690.captcha;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.Buffer;
import java.util.Random;

/**
 * 验证码图片的实体类
 */
public class Captcha {
    private int width;    // 验证码图片的宽度
    private int height;   // 验证码图片的高度
    private String captcha; // 验证码，即根据该验证码生成验证码图片
    private int captchaLength; // 验证码长度，即字符个数
    private int interLine; // 干扰线数
    private BufferedImage imgBuff; // 用于暂存验证码图片的对象

    // 定义用于生成验证码的字符。去掉 数字0、字母O、数字1、小写字母l 这些容易混淆的字符。
    private char[] captchaSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    /**
     * 根据参数创建验证码对象。其中会自动生成验证码和图片
     * @param width 验证码图片的宽度
     * @param height 验证码图片的宽度
     * @param captchaLength 验证码的长度
     * @param interLine 干扰线个数
     */
    public Captcha(int width, int height, int captchaLength, int interLine) {
        this.width = width;
        this.height = height;
        this.captchaLength = captchaLength;
        this.interLine = interLine;
        // 其中验证码是自动生成的。调用下面的方法自动根据上述设置，生成验证码和相应的图片
        this.generateCodeAndImage();
    }

    /**
     * 默认构造验证码对象。会创建 默认宽为160，高为40，验证码长度为4，干扰线个数为150
     * 的验证码对象。
     */
    public Captcha() {
        // 在默认构造中给该类的字段默认的参数。
        // 其中验证码是自动生成的，因此无需赋值。
        // 最后直接调用generateCodeAndImage()方法生成验证码和图片
        // 因此，我们直接借用上述的有参构造即可。
        this(160, 40, 5, 150);
    }

    /**
     * 生成验证码和相应的验证码图片，会保存在该类的captcha和imgBuff字段中
     */
    private void generateCodeAndImage() {
        // 1. 根据制定宽高创建imgBuff
        this.imgBuff = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
        // 2. 在imgBuff上创建Graphics2D对象。
        Graphics2D g = imgBuff.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.width, this.height);
        // 上述代码就是将图像填充为白色。

        // 创建随机数对象
        Random random = new Random();
        // 定义RGB三色
        int red, green, blue;
        // 绘制干扰线
        for (int i = 0; i < interLine; i++) {
            int xs = random.nextInt(width); // 开始的x坐标
            int ys = random.nextInt(height); // 开始的y坐标
            int xe = xs + random.nextInt(width / 8); // 结束的x坐标
            int ye = ys + random.nextInt(height / 8); // 结束的y坐标
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            g.setColor(new Color(red, green, blue));
            g.drawLine(xs, ys, xe, ye);
        }

        int x = this.width / this.captchaLength; // 每个字符的宽度。
        int fontSize = (int)(this.height * 0.85); // 字体大小为图像高度的85%
        // 设置字体为Arial
        g.setFont(new Font("Arial", Font.PLAIN, fontSize));
        // randomCaptcha记录随机产生的验证码
        StringBuffer randomCaptcha = new StringBuffer();
        // 随机产生codeCount个字符的验证码。
        for (int i = 0; i < this.captchaLength; i++) {
            String strRand = String.valueOf(captchaSequence[random.nextInt(captchaSequence.length)]);
            // 产生随机的颜色值，让输出的每个字符的颜色值都将不同。
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            g.setColor(new Color(red, green, blue));
            g.drawString(strRand, i * x, (int)(this.height * 0.8) );
            // 将产生的四个随机数组合在一起。
            randomCaptcha.append(strRand);
        }
        // 设置验证码
        this.captcha = randomCaptcha.toString();
    }


    /**
     * 提供write方法，该方法将验证码图片输出到提供的流参数中。
     * @param os 用于输出的流
     * @throws IOException IO流异常
     */
    public void write(OutputStream os) throws IOException {
        ImageIO.write(imgBuff, "jpeg", os);
    }

    /**
     * 获得验证码字符串
     * @return 验证码字符串
     */
    public String getCaptcha() {
        return captcha;
    }
}
