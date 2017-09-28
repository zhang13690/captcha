# JAVA生成验证码captcha图片项目

本项目可用于生成简单的验证码资源，使用十分简单和方便，可以参考使用。

## 使用方法

只要创建一个Captcha验证码对象，就能生成一个验证码，通过该对象就能拿到该验证码，并能通过write(OutputStream os)方法输出这个验证码图片。例如(在Servlet环境中)：

```java
package com.servlet.demo;

import com.zhang13690.captcha.Captcha;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(urlPatterns = "/captchaServlet")
public class CaptchaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 创建验证码对象
        Captcha captcha = new Captcha();
        // 拿到验证码
        String code = captcha.getCaptcha();
        // 可将验证码保存在session中
        req.getSession().setAttribute("code", code);
        // 调用提供的write方法，将验证码图片以流的形式输出。
        // 先设置响应头，再调用captcha的write()方法将图片输出
        resp.setContentType(getServletContext().getMimeType(".jpeg"));
        captcha.write(resp.getOutputStream());
    }
}
```

使用就是这么简单，就是创建对象，调用提供的getCaptcha()和write()方法即可。目前API只提供了这些操作。非常简单，别无其他内容。

使用Captcha的默认构造方法，创建的验证码的图片大小是宽160像素，高40像素，其中有5个字符，验证码中的干扰线是150条。这也是推荐的设置。

还可使用提供的有参构造自行设置上面的参数：
```java
Captcha captcha = new Captcha(200, 80, 6, 180);
/*
四个参数的含义：
参数1：验证码宽的像素
参数2：验证码高的像素
参数3：验证码的字符个数
参数4：验证码图片中的干扰线个数
 */
```
## 实现的原理

原理很简单，就是随机生成需要的字符数，把他们绘制在ImageBuffer对象中，然后在图片上随机画上指定条数的干扰线。有兴趣的可以参见代码。

## 构建项目

本项目的构建也很简单。在github上克隆项目到本地后，直接在项目本目录运行mvn等命令即可。

例如 mvn package打包等。