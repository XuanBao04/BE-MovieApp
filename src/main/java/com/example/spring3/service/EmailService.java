package com.example.spring3.service;

import com.example.spring3.dto.response.BookingResultResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.text.NumberFormat; // Thêm import cho NumberFormat

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailService {

    // === START FIX: Lớp wrapper để giải quyết lỗi EL1008E (time pattern) ===
    public static class TimePatternHolder {
        // Field 'pattern' phải là public hoặc có public getter.
        public final String pattern = "HH:mm";
    }

    // Khai báo một instance tĩnh của holder để truyền vào Context
    private static final TimePatternHolder TIME_PATTERN_HOLDER = new TimePatternHolder();
    // === END FIX ===

    // === START FIX: Khai báo Currency Formatter để giải quyết lỗi EL1011E ===
    // Tạo NumberFormat cho tiền tệ Việt Nam (VND) để sử dụng trong template
    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    // === END FIX ===


    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    SpringTemplateEngine templateEngine;

    // Lấy thông tin người gửi từ application.yaml
    @Value("${app.mail.sender.email}")
    private String senderEmail;
    @Value("${app.mail.sender.name}")
    private String senderName;

    /**
     * Gửi email xác nhận đặt vé sau khi thanh toán thành công.
     * @param bookingResultResponse Dữ liệu booking đầy đủ đã được ánh xạ.
     */
    @Async
    public void sendBookingConfirmationEmail(BookingResultResponse bookingResultResponse) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            // Sử dụng MULTIPART_MODE_MIXED_RELATED để hỗ trợ hình ảnh và file đính kèm
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

            // Thiết lập thông tin cơ bản
            helper.setFrom(new InternetAddress(senderEmail, senderName));
            helper.setTo(bookingResultResponse.getEmail()); // Lấy email từ kết quả booking
            helper.setSubject("[Xác nhận Đặt vé] Vé xem phim của bạn đã sẵn sàng!");

            // Thiết lập Thymeleaf Context
            Context context = new Context(new Locale("vi", "VN"));
            // Gắn toàn bộ DTO vào context để sử dụng trong template HTML
            context.setVariable("booking", bookingResultResponse);

            // === FIX: Thay thế DateTimeFormatter bằng TimePatternHolder ===
            // Gán đối tượng có thuộc tính 'pattern' vào context
            context.setVariable("timeFormatter", TIME_PATTERN_HOLDER);
            // ==============================================================

            // === FIX: Thêm Currency Formatter vào Context ===
            // Cần cho template sử dụng 'currencyFormatter.format(booking.amount)'
            context.setVariable("currencyFormatter", CURRENCY_FORMATTER);
            // ==================================================

            // Render template HTML (Tên file: confirmation-email.html)
            String htmlContent = templateEngine.process("confirmation-email", context);

            // Thiết lập nội dung là HTML
            helper.setText(htmlContent, true);

            // Gửi email
            mailSender.send(message);
            log.info("Đã gửi email xác nhận thành công cho: {}", bookingResultResponse.getEmail());

        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Lỗi khi gửi email xác nhận cho {}: {}", bookingResultResponse.getEmail(), e.getMessage());
            // TODO: Ghi log vào DB để có thể thử gửi lại sau
        }
    }
}