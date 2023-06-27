package lab.ia.ExpenseManagement.Exceptions;


import lab.ia.ExpenseManagement.Payloads.Response.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
@Data
@AllArgsConstructor
public class AccessDeniedException extends RuntimeException {
	private ApiResponse apiResponse;
}
