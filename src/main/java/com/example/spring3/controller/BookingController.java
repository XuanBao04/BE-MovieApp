package com.example.spring3.controller;

import com.example.spring3.dto.response.BookingResultResponse;
import com.example.spring3.dto.response.TicketDetailResponse;
import com.example.spring3.service.BookingService;

import javax.management.relation.RelationNotFoundException;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {
    BookingService bookingService;
    private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy", new Locale("vi", "VN"));

    @GetMapping("")
    public List<BookingResultResponse> getAll() {
        List<BookingResultResponse> resultResponses = bookingService.getAllBooking();
        return  resultResponses;
    }

    @GetMapping("/result")
    public ResponseEntity<BookingResultResponse> getBookingResult(
            @RequestParam("txnRef") String txnRef,
            @RequestParam("paymentId") String paymentId) throws RelationNotFoundException {

        try {
            // Gọi Service để thực hiện logic truy vấn và ánh xạ
            BookingResultResponse response = bookingService.getBookingResult(txnRef, paymentId);

            // Trả về kết quả 200 OK
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            log.error("Error retrieving booking result: {}", e.getMessage());
            // Xử lý các lỗi khác (như đơn hàng chưa hoàn tất) bằng 400 Bad Request hoặc 500
            // Internal Server Error
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{paymentId}" )
    public BookingResultResponse getBookingByPaymentId(@PathVariable String paymentId) {
        BookingResultResponse resultResponses = bookingService.getBookingByPaymentId(paymentId);

        return resultResponses;
    }

    @GetMapping("/me")
    public List<BookingResultResponse> getMyBooking() {
        List<BookingResultResponse> resultResponses = bookingService.getMyBooking();

        return resultResponses;
    }

    @GetMapping("/print/{paymentId}")
    public ResponseEntity<byte[]> generateBillPdf(@PathVariable String paymentId) {

        // --- 1. LẤY DỮ LIỆU BOOKING TỪ SERVICE/DB

        BookingResultResponse bookingData = bookingService.getBookingByPaymentId(paymentId);

        try {
            // Tên file JRXML
            InputStream billTemplate = getClass().getResourceAsStream("/reports/CinemaBill.jrxml");
            if (billTemplate == null) {
                throw new RuntimeException("Lỗi: Không tìm thấy file CinemaBill.jrxml.");
            }

            // 2. Biên dịch template
            JasperReport jasperReport = JasperCompileManager.compileReport(billTemplate);

            // 3. Chuẩn bị Parameters (Thông tin chung của hóa đơn)
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("ORDER_REF", bookingData.getOrderReferenceId());
            parameters.put("CUSTOMER_NAME", bookingData.getName());
            parameters.put("CUSTOMER_EMAIL", bookingData.getEmail());
            parameters.put("CUSTOMER_PHONE", bookingData.getPhone());
            parameters.put("MOVIE_TITLE", bookingData.getMovieTitle());
            parameters.put("SHOWTIME", bookingData.getStartTime().format(DATE_TIME_FORMATTER));
            parameters.put("THEATER_ROOM", bookingData.getTheaterName() + " - " + bookingData.getRoomName());
            parameters.put("TOTAL_AMOUNT", String.format("%,d VND", bookingData.getAmount()));
            parameters.put("PAYMENT_TIME", bookingData.getPaymentTime().format(DATE_TIME_FORMATTER));
            parameters.put("QR_CODE_DATA", bookingData.getOrderReferenceId());

            // --- CẢI THIỆN --- Thêm URL ảnh QR Code (nếu có)
            parameters.put("QR_CODE_IMAGE_URL", "https://api.qrserver.com/v1/create-qr-code/?size=100x100&data=" + bookingData.getOrderReferenceId());


            // 4. CHUẨN BỊ DATA SOURCE CHO DANH SÁCH CHI TIẾT (Ticket list)
            String seatNamesString = bookingData.getTickets().stream()
                    .map(ticket -> ticket.getSeatName()) // Giả sử Ticket object có method getSeatName()
                    .collect(Collectors.joining(", "));

            // Đặt Data Source vào một Parameter để List Component trong JRXML sử dụng
            parameters.put("LIST_SEAT_NAMES", seatNamesString);

            // 5. Điền dữ liệu vào báo cáo
            // LƯU Ý: Vẫn dùng JREmptyDataSource cho báo cáo chính, vì Data Source cho List
            // đã được truyền qua Parameter "TICKET_LIST_DATA_SOURCE".
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parameters,
                    new JREmptyDataSource()
            );

            // 6. Xuất báo cáo thành PDF
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
            exporter.exportReport();

            // 7. Trả về ResponseEntity
            byte[] pdfBytes = baos.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String filename = paymentId + "_bill.pdf";
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (JRException e) {
            log.error("Lỗi khi tạo báo cáo Jasper: {}", e.getMessage(), e);
            return new ResponseEntity<>(("Lỗi khi tạo báo cáo Jasper: " + e.getMessage()).getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException e) {
            log.error("Lỗi runtime khi tạo báo cáo: {}", e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage().getBytes(), HttpStatus.NOT_FOUND);
        }
    }

}
