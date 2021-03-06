package xyz.ersut.core.controller;

import xyz.ersut.core.constans.CommonConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 生成验证图片Controller
 */
@Controller
@ApiIgnore
public class RandomCodeController {
	

    public RandomCodeController() {
    }

	@RequestMapping({"/ran/ajax/random" })
	public void genericRandomCode(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setHeader("Cache-Control", "private,no-cache,no-store");
            response.setContentType("image/png");

            byte width = 85;
            byte height = 28;
            BufferedImage image = new BufferedImage(width, height, 2);
            Graphics2D g = image.createGraphics();
            g.setComposite(AlphaComposite.getInstance(3, 1.0F));
            Random random = new Random();
            g.setColor(new Color(231, 231, 231));
            g.fillRect(0, 0, width, height);
            g.setFont(new Font("Microsoft YaHei", 2, 24));
            String sRand = "";

            for(int responseOutputStream = 0; responseOutputStream < 4; ++responseOutputStream) {
                String rand = String.valueOf(random.nextInt(10));
                sRand = sRand + rand;
                g.setColor(new Color(121, 143, 96));
                g.drawString(rand, 13 * responseOutputStream + 16, 23);
            }

            ServletOutputStream var12 = response.getOutputStream();
            HttpSession session = request.getSession();
            session.setAttribute(CommonConstants.RAND_CODE, sRand);
            g.dispose();
            ImageIO.write(image, "png", var12);
            var12.close();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

	
}