package lk.empire.ams.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * <p>Title         : EmailSender
 * <p>Project       : Ams
 * <p>Description   :
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Component
public class EmailSender {

    private JavaMailSender javaMailSender;



    public EmailSender(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    /**
     * Send an e
     * @param to
     * @param subject
     * @param body
     */
    public void sendEmail(String to, String subject, String body) {
        Assert.notNull(to, "Expects valid sender email");
        Assert.notNull(subject, "Expects valid subject for email");
        Assert.notNull(body, "Expects valid body for email");

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("empiretestmail@gmail.com");
        msg.setTo(to);
        //msg.setTo("to_1@gmail.com", "to_2@gmail.com", "to_3@yahoo.com");

        msg.setSubject(subject);
        msg.setText(body);

        javaMailSender.send(msg);

    }
}
