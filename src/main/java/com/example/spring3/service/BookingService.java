package com.example.spring3.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.spring3.configuration.VNPayConfig;
import com.example.spring3.dto.request.BookingRequest;
import com.example.spring3.dto.request.payment.PaymentCreateRequest;
import com.example.spring3.dto.response.BookingResultResponse;
import com.example.spring3.dto.response.TicketDetailResponse;
import com.example.spring3.dto.response.payment.PaymentResponse;
import com.example.spring3.dto.response.payment.VNPayResponse;
import com.example.spring3.entity.Payment;
import com.example.spring3.entity.Room;
import com.example.spring3.entity.Seat;
import com.example.spring3.entity.Showtime;
import com.example.spring3.entity.Ticket;
import com.example.spring3.entity.User;
import com.example.spring3.exception.AppException;
import com.example.spring3.repository.PaymentRepository;
import com.example.spring3.repository.SeatRepository;
import com.example.spring3.repository.ShowtimeRepository;
import com.example.spring3.repository.TicketRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingService {

    TicketRepository ticketRepository;
    SeatRepository seatRepository;
    PaymentRepository paymentRepository;
    ShowtimeRepository showtimeRepository;
    UserService userService; // Giả định service để lấy User
    PaymentService paymentService; // Service xử lý logic VNPay
    EmailService emailService;

    public BookingResultResponse getBookingResult(String txnRef, String paymentId) {

        List<Ticket> tickets = ticketRepository.findByVnpTxnRef(txnRef);

        if (tickets.isEmpty() || tickets.stream().anyMatch(t -> t.getStatus() != 1)) {
            // Xử lý lỗi
            throw new RuntimeException("Booking not found or not completed.");
        }

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment record not found."));

        // GỌI PHƯƠNG THỨC ÁNH XẠ THỦ CÔNG
        return mapToBookingResultResponse(tickets, payment);
    }

    // --- 1. Ánh xạ Ticket Detail (Từng vé) ---
    private List<TicketDetailResponse> mapToTicketDetailResponseList(List<Ticket> tickets) {
        return tickets.stream().map(ticket -> {
            // Lấy thông tin cần thiết
            String seatName = ticket.getSeat().getSeatRow() + ticket.getSeat().getSeatNumber();

            // Ánh xạ thủ công
            return TicketDetailResponse.builder()
                    .ticketId(ticket.getTicketId())
                    .type(ticket.getType())
                    .seatName(seatName) // Kết hợp chuỗi
                    // Giả định Ticket Entity có trường getTicketPrice()
                    .build();
        }).collect(Collectors.toList());
    }

    // --- 2. Ánh xạ Booking Result (Tổng hợp) ---
    private BookingResultResponse mapToBookingResultResponse(List<Ticket> tickets, Payment payment) {
        // Lấy thông tin chung từ vé đầu tiên
        Ticket firstTicket = tickets.get(0);
        Showtime showtime = firstTicket.getShowtime();
        Room room = showtime.getRoom();

        // Ánh xạ thủ công
        return BookingResultResponse.builder()
                // Infomation customer
                .name(firstTicket.getCustomerName())
                .email(firstTicket.getCustomerEmail())
                .phone(firstTicket.getCustomerPhone())

                // Payment
                .paymentId(payment.getPaymentId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .paymentTime(payment.getPaymentTime())
                .paymentStatus(payment.getStatus())

                // Order
                .orderReferenceId(firstTicket.getVnpTxnRef())

                // Movie & Showtime
                .movieTitle(showtime.getMovie().getTitle())
                .startTime(showtime.getStartTime())

                // Theater & Room
                .theaterName(room.getTheater().getName())
                .theaterAddress(room.getTheater().getAddress())
                .roomName(room.getName())

                // Tickets (Gọi phương thức ánh xạ list)
                .tickets(mapToTicketDetailResponseList(tickets))
                .build();
    }

    // Thời gian giữ ghế: 15 phút
    private static final long LOCK_DURATION_MINUTES = 15;

    public VNPayResponse createBookingAndVnPayUrl(BookingRequest bookingRequest, HttpServletRequest request)
            throws UnsupportedEncodingException {
        // 1. Khởi tạo & Lấy dữ liệu
        User user = userService.findUserById(bookingRequest.getUserId());
        Showtime showtime = showtimeRepository.findById(bookingRequest.getShowtimeId())
                .orElseThrow(() -> new RuntimeException("Showtime not found"));

        List<String> seatIds = bookingRequest.getSeatIds();

        // 2. Kiểm tra tính hợp lệ & Khóa ghế (Seat Locking)
        List<Seat> seatsToLock = seatRepository.findAllById(seatIds);

        // Đảm bảo số lượng ghế khớp và tất cả ghế đều 'AVAILABLE'
        if (seatsToLock.size() != seatIds.size() ||
                seatsToLock.stream().anyMatch(seat -> !seat.getStatus().equals("AVAILABLE"))) {
            throw new RuntimeException("One or more seats are already locked or sold.");
        }

        // Cập nhật trạng thái ghế thành LOCKED
        seatsToLock.forEach(seat -> seat.setStatus("LOCKED"));
        seatRepository.saveAll(seatsToLock);

        // 3. Tạo Mã Đơn hàng & Thời gian hết hạn
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        LocalDateTime bookingTime = LocalDateTime.now();
        LocalDateTime expireTime = bookingTime.plusMinutes(LOCK_DURATION_MINUTES);
        long totalAmount = (long) bookingRequest.getAmount();

        // 4. Tạo Tickets PENDING
        List<Ticket> pendingTickets = seatsToLock.stream().map(seat -> Ticket.builder()
                .showtime(showtime)
                .seat(seat)
                .user(user)
                .type("adult") // Mặc định là adult
                .bookingTime(bookingTime)
                .expireTime(expireTime)
                .status(0) // 0 = PENDING/LOCKED
                .vnpTxnRef(vnp_TxnRef)
                .orderStatus("PENDING")
                .customerName(bookingRequest.getName())
                .customerEmail(bookingRequest.getEmail())
                .customerPhone(bookingRequest.getPhone())
                .build()).collect(Collectors.toList());

        ticketRepository.saveAll(pendingTickets);

        // 5. Chuẩn bị URL Thanh toán VNPay
        // Chú ý: Cần chuyển đổi totalAmount sang tiền tệ nhỏ nhất (VND -> cent hoặc
        // đồng)
        long vnPayAmount = totalAmount; // VNPay nhận đơn vị là Đồng

        PaymentCreateRequest paymentCreateRequest = PaymentCreateRequest.builder()
                .amount(vnPayAmount)
                .paymentMethod("vnpay")
                .paymentTime(bookingTime)
                .build();

        String vnpayUrl = paymentService.createVNPayPayment(paymentCreateRequest, request, vnp_TxnRef);

        return VNPayResponse.builder()
                .status("Ok")
                .message("Tạo url thành công.")
                .url(vnpayUrl)
                .build();
    }

    public String handleVnPayCallback(Map<String, String> params) {
        // --- BƯỚC 1: XÁC MINH CHỮ KÝ (SECURE HASH) ---
        // String secureHash = params.get("vnp_SecureHash");

        // 1.1. Tái tạo hashData từ params
        // String hashData = vnPayUtil.hashAllFields(params);

        // 1.2. So sánh
        // if (!secureHash.equals(vnPayUtil.sha256(hashData,
        // vnPayUtil.getHashSecret()))) {
        // return "Callback Failed: Invalid SecureHash";
        // }

        // --- BƯỚC 2: TRUY VẤN VÉ & KIỂM TRA TÍNH HỢP LỆ ---
        String vnpTxnRef = params.get("vnp_TxnRef");
        String responseCode = params.get("vnp_ResponseCode");

        // Truy vấn tất cả vé thuộc đơn hàng này
        List<Ticket> tickets = ticketRepository.findByVnpTxnRef(vnpTxnRef);

        if (tickets.isEmpty()) {
            return "Callback Failed: Order Not Found";
        }

        // Đảm bảo đơn hàng chưa được xử lý thành công trước đó (kiểm tra kép)
        if (tickets.stream().anyMatch(t -> t.getOrderStatus().equals("COMPLETED"))) {
            return "Callback Success: Order Already Completed";
        }

        // --- BƯỚC 3: XỬ LÝ DỰA TRÊN MÃ PHẢN HỒI ---

        // Lấy thông tin thanh toán (cần thiết cho Payment Entity)
        long amount = Long.parseLong(params.get("vnp_Amount")) / 100; // VNPay trả về đơn vị nhỏ nhất (cent/đồng)
        String transactionId = params.get("vnp_TransactionNo");

        if ("00".equals(responseCode)) {
            // Trường hợp 3.1: THANH TOÁN THÀNH CÔNG
            return handleSuccessfulPayment(tickets, vnpTxnRef, transactionId, amount);

        } else {
            // Trường hợp 3.2: THANH TOÁN THẤT BẠI/LỖI
            return handleFailedPayment(tickets, transactionId);
        }
    }

    /** Xử lý khi thanh toán thành công (vnp_ResponseCode = 00) **/
    private String handleSuccessfulPayment(List<Ticket> tickets, String vnpTxnRef, String transactionId, long amount) {

        // 1. Tạo bản ghi Payment
        Payment payment = Payment.builder()
                // .paymentId(UUID.randomUUID().toString())
                .amount(amount)
                .paymentMethod("VNPay")
                .paymentTime(LocalDateTime.now())
                .status(1) // 1 = COMPLETED
                .build();
        Payment savedPayment = paymentRepository.save(payment);

        // 2. Cập nhật Tickets & Seats
        List<Seat> seatsToUpdate = new ArrayList<>();

        for (Ticket ticket : tickets) {
            // Cập nhật Ticket
            ticket.setStatus(1); // 1 = SOLD
            ticket.setOrderStatus("COMPLETED");
            ticket.setPayment(savedPayment);
            ticket.setExpireTime(null); // Xóa thời gian hết hạn

            // Cập nhật Seat
            Seat seat = ticket.getSeat();
            seat.setStatus("SOLD");
            seatsToUpdate.add(seat);
        }

        ticketRepository.saveAll(tickets);
        seatRepository.saveAll(seatsToUpdate);

        // --- 3. GỬI EMAIL XÁC NHẬN VÉ ---
        BookingResultResponse bookingResultResponse = mapToBookingResultResponse(tickets, savedPayment);
        emailService.sendBookingConfirmationEmail(bookingResultResponse);

        // 4. Trả về URL Client
        String returnUrlParam = String.format(
                "http://localhost:5173/booking/result?status=success&txnRef=%s&paymentId=%s",
                vnpTxnRef,
                payment.getPaymentId());

        return returnUrlParam;
    }

    /** Xử lý khi thanh toán thất bại (vnp_ResponseCode != 00) **/
    private String handleFailedPayment(List<Ticket> tickets, String transactionId) {

        List<Seat> seatsToRelease = new ArrayList<>();

        for (Ticket ticket : tickets) {
            // Cập nhật Ticket
            ticket.setStatus(-1); // -1 = CANCELLED/FAILED
            ticket.setOrderStatus("FAILED");

            // Giải phóng Seat (chuyển từ LOCKED sang AVAILABLE)
            Seat seat = ticket.getSeat();
            seat.setStatus("AVAILABLE");
            seatsToRelease.add(seat);
        }

        ticketRepository.saveAll(tickets);
        seatRepository.saveAll(seatsToRelease);

        String returnUrlParam = String.format(
                "http://localhost:5173/booking/result?status=failed&transactionId=%s",
                transactionId);

        return returnUrlParam;
    }
}