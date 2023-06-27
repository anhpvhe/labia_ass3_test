package lab.ia.ExpenseManagement.Payloads.Request;

import lab.ia.ExpenseManagement.Models.Enums.ERecordType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class RecordRequest {
    private String name;

    private String note;

    private ERecordType type;

    private Double amount;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date date;

    private List<Long> categories;

    public boolean isValid() {
        if (name.isBlank() || type == null || amount == null || date == null || categories == null)
            return false;
        return true;
    }
}
