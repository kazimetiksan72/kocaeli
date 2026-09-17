package tr.gov.kocaeli.sehir.common.api;
import jakarta.servlet.http.HttpServletRequest; import org.slf4j.Logger; import org.slf4j.LoggerFactory; import org.slf4j.MDC; import org.springframework.http.*; import org.springframework.web.bind.MethodArgumentNotValidException; import org.springframework.web.bind.annotation.*; import org.springframework.web.server.ResponseStatusException; import java.util.*;
@RestControllerAdvice
public class GlobalExceptionHandler {
 private static final Logger log=LoggerFactory.getLogger(GlobalExceptionHandler.class);
 @ExceptionHandler(ApiException.class) ResponseEntity<ApiError> api(ApiException e){return ResponseEntity.status(e.status()).body(new ApiError(e.code(),e.getMessage(),List.of(),MDC.get("traceId")));}
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<ApiError> validation(MethodArgumentNotValidException e){var fields=e.getBindingResult().getFieldErrors().stream().map(f->new ApiError.FieldError(f.getField(),Objects.requireNonNullElse(f.getDefaultMessage(),"Geçersiz değer"))).toList();return ResponseEntity.badRequest().body(new ApiError("VALIDATION_ERROR","Alan doğrulaması başarısız",fields,MDC.get("traceId")));}
 @ExceptionHandler(ResponseStatusException.class) ResponseEntity<ApiError> status(ResponseStatusException e){return ResponseEntity.status(e.getStatusCode()).body(new ApiError("HTTP_"+e.getStatusCode().value(),e.getReason(),List.of(),MDC.get("traceId")));}
 @ExceptionHandler(Exception.class) ResponseEntity<ApiError> unknown(Exception e,HttpServletRequest req){log.error("Unhandled error for {} {} traceId={}",req.getMethod(),req.getRequestURI(),MDC.get("traceId"),e);return ResponseEntity.internalServerError().body(new ApiError("INTERNAL_ERROR","Beklenmeyen bir hata oluştu",List.of(),MDC.get("traceId")));}
}
