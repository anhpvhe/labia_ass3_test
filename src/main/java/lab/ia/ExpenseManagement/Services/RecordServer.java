package lab.ia.ExpenseManagement.Services;

import lab.ia.ExpenseManagement.Models.Record;
import lab.ia.ExpenseManagement.Payloads.Request.RecordRequest;
import lab.ia.ExpenseManagement.Payloads.Response.ApiResponse;
import lab.ia.ExpenseManagement.Payloads.Response.PagedResponse;
import lab.ia.ExpenseManagement.Security.UserPrincipal;

import java.util.Date;

public interface RecordServer {
    PagedResponse<Record> getAllRecords(int page, int size, UserPrincipal currentUser);

    Record getRecord(Long id, UserPrincipal currentUser);

    PagedResponse<Record> getRecordsInRange(int page, int size, Date startDate, Date endDate, UserPrincipal currentUser);

    Record addRecord(RecordRequest recordRequest, UserPrincipal currentUser);

    ApiResponse updateRecord(Long id, RecordRequest newRecord, UserPrincipal currentUser);

    ApiResponse deleteRecord(Long id, UserPrincipal currentUser);
}
