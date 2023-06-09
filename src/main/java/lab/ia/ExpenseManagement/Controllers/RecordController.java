package lab.ia.ExpenseManagement.Controllers;

import lab.ia.ExpenseManagement.Payloads.Request.RecordRequest;
import lab.ia.ExpenseManagement.Security.CurrentUser;
import lab.ia.ExpenseManagement.Security.UserPrincipal;
import lab.ia.ExpenseManagement.Services.RecordServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/records")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class RecordController {
    @Autowired
    RecordServer recordServer;

//    @GetMapping
//    public ResponseEntity<?> getUserRecords(@RequestParam(name = "page", defaultValue = "0") int page,
//                                            @RequestParam(name = "size", defaultValue = "10") int size,
//                                            @CurrentUser UserPrincipal currentUser) {
//        return ResponseEntity.ok(recordServer.getAllRecords(page, size, currentUser));
//    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecord(@PathVariable(name = "id") Long id,
                                       @CurrentUser UserPrincipal currentUser) {
        return ResponseEntity.ok(recordServer.getRecord(id, currentUser));
    }

    @GetMapping
    public ResponseEntity<?> getRecordInRange(@RequestParam(name = "page", defaultValue = "0") int page,
                                              @RequestParam(name = "size", defaultValue = "10") int size,
                                              @RequestParam(name = "from", defaultValue = "") @DateTimeFormat(pattern = "dd-MM-yyyy") Date startDate,
                                              @RequestParam(name = "to", defaultValue = "") @DateTimeFormat(pattern = "dd-MM-yyyy") Date endDate,
                                              @CurrentUser UserPrincipal currentUser) {
        return ResponseEntity.ok(recordServer.getRecordsInRange(page, size, startDate, endDate, currentUser));
    }

    @PostMapping
    public ResponseEntity<?> addRecord(@RequestBody RecordRequest recordRequest,
                                       @CurrentUser UserPrincipal currentUser) {
        System.out.println(recordRequest);
        return ResponseEntity.ok(recordServer.addRecord(recordRequest, currentUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecord(@PathVariable(name = "id") Long id,
                                          @RequestBody RecordRequest recordRequest,
                                          @CurrentUser UserPrincipal currentUser) {
        return ResponseEntity.ok(recordServer.updateRecord(id, recordRequest, currentUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecord(@PathVariable(name = "id") Long id,
                                          @CurrentUser UserPrincipal currentUser) {
        return ResponseEntity.ok(recordServer.deleteRecord(id, currentUser));
    }
}
