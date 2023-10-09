package dblab.sharing_platform.config.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

import static dblab.sharing_platform.config.mail.MailConfigInfo.*;


@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender NaverMailService(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(HOST);
        javaMailSender.setUsername(USERNAME);
        javaMailSender.setPassword(PASSWORD);

        javaMailSender.setPort(PORT);
        javaMailSender.setJavaMailProperties(getMailProperties());
        return javaMailSender;
    }

    private Properties getMailProperties(){
        Properties properties = new Properties();
        properties.setProperty(MAIL_TRANSPORT_PROTOCOL, SMTP);
        properties.setProperty(MAIL_SMTP_AUTH, IS_TRUE);
        properties.setProperty(MAIL_SMTP_STARTTLS_ENABLE, IS_TRUE);
        properties.setProperty(MAIL_DEBUG, IS_TRUE);
        properties.setProperty(MAIL_SMTP_SSL_TRUST, HOST);
        properties.setProperty(MAIL_SMTP_SSL_ENABLE, IS_TRUE);

        return properties;
    }
}
