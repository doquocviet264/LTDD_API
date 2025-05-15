package com.ltdd.QuanLyThuChiAPI.Service.User;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.ltdd.QuanLyThuChiAPI.Model.User.AccountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(AccountModel accountModel, String verifyCode)
    {
        String from ="khoiphucmatkhau@gmail.com";
        String to = accountModel.getEmail();

        MimeMessage message =mailSender.createMimeMessage();
        MimeMessageHelper helper =new MimeMessageHelper(message);

        try {
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("Quên mật khẩu");

            ResourceLoader loader = new DefaultResourceLoader();
            MustacheFactory mf = new DefaultMustacheFactory(loader.getResource("classpath:/templates/").getFile());
            Mustache mustache =mf.compile("forgot-password.html");
            StringWriter htmlContent = new StringWriter();
            String link = "http://localhost:3000/change-pass/verify-code="+verifyCode;
            mustache.execute(htmlContent, Map.of("link", link,"name",accountModel.getUsername()));

            helper.setText(htmlContent.toString(), true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();;
        }
    }
}
