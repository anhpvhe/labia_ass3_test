package lab.ia.ExpenseManagement.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlers {
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> resolvedException(AccessDeniedException accessDeniedException) {
        return new ResponseEntity<>(accessDeniedException.getApiResponse(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> resolvedException(BadRequestException badRequestException) {
        return new ResponseEntity<>(badRequestException.getApiResponse(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resolvedException(ResourceNotFoundException resourceNotFoundException) {
        return new ResponseEntity<>(resourceNotFoundException.getApiResponse(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> resolvedException(UnauthorizedException unauthorizedException) {
        return new ResponseEntity<>(unauthorizedException.getApiResponse(), HttpStatus.UNAUTHORIZED);
    }
}
