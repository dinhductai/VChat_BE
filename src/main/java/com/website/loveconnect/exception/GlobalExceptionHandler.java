package com.website.loveconnect.exception;

import com.nimbusds.jose.JOSEException;
import com.website.loveconnect.dto.response.ErrorDetailResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.naming.AuthenticationException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Xử lý mọi ngoại lệ chung
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetailResponse> handleAllExceptions(Exception ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "An unexpected error occurred",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Xử lý lỗi quyền truy cập
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetailResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "Access denied",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.FORBIDDEN);
    }

    // Xử lý lỗi đọc HTTP message (sửa tên lớp)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDetailResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "Invalid request body",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    // Xử lý lỗi không tìm thấy người dùng
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDetailResponse> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                ex.getMessage(), // Giữ lỗi cụ thể từ exception
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }



    // Xử lý lỗi truy cập dữ liệu
    @ExceptionHandler(com.website.loveconnect.exception.DataAccessException.class)
    public ResponseEntity<ErrorDetailResponse> handleDataAccessException(DataAccessException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "Database error occurred: " + ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetailResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ErrorDetailResponse> handleEmailAlreadyInUseException(EmailAlreadyInUseException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorDetailResponse> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        String message;
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "Incorrect email or password",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.UNAUTHORIZED);
    }

    // Xử lý sai email/password
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDetailResponse> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "Incorrect email or password",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.UNAUTHORIZED);
    }

    // Xử lý user không tồn tại
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorDetailResponse> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "User not found",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.UNAUTHORIZED);
    }

    // Xử lý token hết hạn (nếu dùng JWT)
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorDetailResponse> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "Token has expired",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.UNAUTHORIZED);
    }

    // Xử lý các AuthenticationException chung
    @ExceptionHandler(GenericAuthenticationException.class)
    public ResponseEntity<ErrorDetailResponse> handleGenericAuthenticationException(GenericAuthenticationException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "Authentication failed: " + ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.UNAUTHORIZED);
    }


    // Xử lý lỗi JOSEException (liên quan đến JWT)
    @ExceptionHandler(JOSEException.class)
    public ResponseEntity<ErrorDetailResponse> handleJoseException(com.nimbusds.jose.JOSEException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "Error processing JWT: " + ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Xử lý lỗi ParseException (liên quan đến parsing JWT)
    @ExceptionHandler(ParseException.class)
    public ResponseEntity<ErrorDetailResponse> handleParseException(ParseException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "Invalid token format: " + ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    // Xử lý AuthenticationException riêng cho introspect
    @ExceptionHandler(IntrospectAuthenticationException.class)
    public ResponseEntity<ErrorDetailResponse> handleIntrospectAuthenticationException(IntrospectAuthenticationException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "Token validation failed: " + ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(PermissionAlreadyExistException.class)
    public ResponseEntity<ErrorDetailResponse> handlePermissionAlreadyExistException(PermissionAlreadyExistException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PermissionNotFoundException.class)
    public ResponseEntity<ErrorDetailResponse> handlePermissionNotFoundException(PermissionNotFoundException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "Permission does not exist", // Giữ lỗi cụ thể từ exception
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceEmptyException.class)
    public ResponseEntity<ErrorDetailResponse> handleResourceEmptyException(ResourceEmptyException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "Result is null", // Giữ lỗi cụ thể từ exception
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorDetailResponse> handleRoleNotFoundException(RoleNotFoundException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                ex.getMessage(), // Giữ lỗi cụ thể từ exception
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PasswordIncorrectException.class)
    public ResponseEntity<ErrorDetailResponse> handlePasswordIncorrectException(PasswordIncorrectException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                ex.getMessage(), // Giữ lỗi cụ thể từ exception
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InterestNotFoundException.class)
    public ResponseEntity<ErrorDetailResponse> handleInterestNotFoundException(InterestNotFoundException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                ex.getMessage(), // Giữ lỗi cụ thể từ exception
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenInvalid.class)
    public ResponseEntity<ErrorDetailResponse> handleTokenInvalid(TokenInvalid ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "Token invalid",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(LikeDuplicatedException.class)
    public ResponseEntity<ErrorDetailResponse> handleLikeDuplicatedException(LikeDuplicatedException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "Like already exists",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ReportTypeConflictedException.class)
    public ResponseEntity<ErrorDetailResponse> handleReportTypeDuplicatedException(ReportTypeConflictedException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "Report type already exists",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(ReportTypeNotFoundException.class)
    public ResponseEntity<ErrorDetailResponse> handleReportTypeNotFoundException(ReportTypeNotFoundException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "Report type not found",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReportConflictedException.class)
    public ResponseEntity<ErrorDetailResponse> handleReportConflictException(ReportConflictedException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "Report already exists",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ReportNotFoundException.class)
    public ResponseEntity<ErrorDetailResponse> handleReportNotFoundException(ReportNotFoundException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "Report not found",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new LinkedHashMap<>(); // Sử dụng LinkedHashMap
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmotionNotFoundException.class)
    public ResponseEntity<ErrorDetailResponse> handleEmotionNotFoundException(EmotionNotFoundException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "Emotion not found",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(MatchNotFoundException.class)
    public ResponseEntity<ErrorDetailResponse> handleMatchNotFoundException(MatchNotFoundException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "Match not found",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorDetailResponse> handlePostNotFoundException(PostNotFoundException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "Post not found",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<ErrorDetailResponse> handleNotificationNotFoundException(NotificationNotFoundException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "Notification not found",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ErrorDetailResponse> handleCommentNotFoundException(CommentNotFoundException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "Comment not found",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VideoNotFoundException.class)
    public ResponseEntity<ErrorDetailResponse> handleVideoNotFoundException(VideoNotFoundException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "Video not found",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReactionAlreadyExistException.class)
    public ResponseEntity<ErrorDetailResponse> handleReactionAlreadyExistException(ReactionAlreadyExistException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "Reaction already exist",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PromptEmptyException.class)
    public ResponseEntity<ErrorDetailResponse> handlePromptEmptyException(PromptEmptyException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "No prompt found",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MatchAlreadyExistingException.class)
    public ResponseEntity<ErrorDetailResponse> handleMatchAlreadyExistingException(MatchAlreadyExistingException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "Match already exist",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<ErrorDetailResponse> handleMessageNotFoundException(MessageNotFoundException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                "Message not found",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorDetailResponse> handleForbiddenException(ForbiddenException ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MessageAlreadyDeleted.class)
    public ResponseEntity<ErrorDetailResponse> handleMessageAlreadyDeleted(MessageAlreadyDeleted ex, WebRequest request) {
        ErrorDetailResponse errorDetail = new ErrorDetailResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.CONFLICT);
    }
}
