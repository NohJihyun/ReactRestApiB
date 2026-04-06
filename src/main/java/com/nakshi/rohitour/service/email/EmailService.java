package com.nakshi.rohitour.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${mail.from}")
    private String from;

    /**
     * 이메일 인증코드 발송
     * Amazon SES SMTP → JavaMailSender 사용
     */
    public void sendVerificationCode(String toEmail, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setFrom(from);
            helper.setTo(toEmail);
            helper.setSubject("[로히투어] 이메일 인증코드 안내");

            String html = """
                    <div style="font-family:Arial,sans-serif;max-width:480px;margin:0 auto;padding:32px;border:1px solid #e0e0e0;border-radius:8px;">
                        <h2 style="color:#7CB342;margin-bottom:8px;">이메일 인증코드</h2>
                        <p style="color:#555;margin-bottom:24px;">아래 인증코드를 입력하여 이메일 인증을 완료해주세요.<br>인증코드는 <strong>5분간</strong> 유효합니다.</p>
                        <div style="background:#f5f5f5;border-radius:6px;padding:20px;text-align:center;">
                            <span style="font-size:32px;font-weight:bold;letter-spacing:8px;color:#333;">%s</span>
                        </div>
                        <p style="color:#999;font-size:12px;margin-top:24px;">본 메일은 발신 전용입니다. 문의는 고객센터를 이용해주세요.</p>
                    </div>
                    """.formatted(code);

            helper.setText(html, true);
            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("이메일 발송에 실패했습니다: " + e.getMessage(), e);
        }
    }

    /**
     * 비밀번호 재설정 링크 발송
     */
    public void sendPasswordResetLink(String toEmail, String resetLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setFrom(from);
            helper.setTo(toEmail);
            helper.setSubject("[로히투어] 비밀번호 재설정 안내");

            String html = """
                    <div style="font-family:Arial,sans-serif;max-width:480px;margin:0 auto;padding:32px;border:1px solid #e0e0e0;border-radius:8px;">
                        <h2 style="color:#7CB342;margin-bottom:8px;">비밀번호 재설정</h2>
                        <p style="color:#555;margin-bottom:24px;">아래 버튼을 클릭하여 비밀번호를 재설정해주세요.<br>링크는 <strong>30분간</strong> 유효합니다.</p>
                        <div style="text-align:center;margin-bottom:24px;">
                            <a href="%s" style="display:inline-block;background:#7CB342;color:#fff;text-decoration:none;padding:14px 32px;border-radius:6px;font-size:16px;font-weight:bold;">비밀번호 재설정하기</a>
                        </div>
                        <p style="color:#999;font-size:12px;">버튼이 클릭되지 않으면 아래 링크를 복사하여 브라우저에 붙여넣으세요.<br><a href="%s" style="color:#7CB342;">%s</a></p>
                        <p style="color:#999;font-size:12px;margin-top:16px;">본인이 요청하지 않은 경우 이 메일을 무시하세요.</p>
                    </div>
                    """.formatted(resetLink, resetLink, resetLink);

            helper.setText(html, true);
            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("이메일 발송에 실패했습니다: " + e.getMessage(), e);
        }
    }
}
