package dblab.sharing_flatform.service.mail;

import dblab.sharing_flatform.domain.emailAuth.EmailAuth;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.repository.emailAuth.EmailAuthRepository;
import dblab.sharing_flatform.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResetPasswordMailService implements MailService{

    private final JavaMailSender emailSender;
    private final EmailAuthRepository emailAuthRepository;
    private final MemberRepository memberRepository;

    private String ePw;


    @Override
    @Transactional
    public void sendSimpleMessage(String email) {
        ePw = createKey();
        MimeMessage message = null;

        memberRepository.findByUsername(email).orElseThrow(MemberNotFoundException::new);

        try {
            message = creatMessage(email);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        try {
            emailSender.send(message);
            emailAuthRepository.save(new EmailAuth(ePw, email, "RESET-PASSWORD"));
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public MimeMessage creatMessage(String email) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("[뭐든빌리개]" + "Sharing-Platform 비밀번호 재설정 페이지에 대한 인증코드 입니다.");

        String msgg = "";
        // msgg += "<img src=../resources/static/image/emailheader.jpg />";
        msgg += "<h1>안녕하세요</h1>";
        msgg += "<h1>대여 서비스 플랫폼 뭐든빌리개 입니다</h1>";
        msgg += "<br>";
        msgg += "<p>아래 인증코드를 비밀번호 재설정 페이지에 입력해주세요</p>";
        msgg += "<br>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black'>";
        msgg += "<h3 style='color:blue'>비밀번호 재설정을 하기 위한 사전인증코드 입니다</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "<strong>" + ePw + "</strong></div><br/>" ;
        msgg += "</div>";
        // msgg += "<img src=../resources/static/image/emailfooter.jpg />"; // footer image

        message.setText(msgg, "utf-8", "html");

        message.setFrom(new InternetAddress("zerozae0926@naver.com", "SharingPlatform_Admin"));

        return message;
    }

    @Override
    public String createKey() {
        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = 10;
        Random random = new Random();
        String key = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return key;
    }


}
