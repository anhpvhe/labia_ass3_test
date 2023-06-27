package lab.ia.ExpenseManagement.Payloads.Request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CategoryListRequest {
    List<Long> categories;
}
