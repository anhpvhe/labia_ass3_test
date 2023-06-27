package lab.ia.ExpenseManagement.Services.Impl;

import lab.ia.ExpenseManagement.DAO.DBContext;
import lab.ia.ExpenseManagement.DAO.RecordDAO;
import lab.ia.ExpenseManagement.Exceptions.BadRequestException;
import lab.ia.ExpenseManagement.Exceptions.ResourceNotFoundException;
import lab.ia.ExpenseManagement.Models.Category;
import lab.ia.ExpenseManagement.Models.Enums.ERecordType;
import lab.ia.ExpenseManagement.Models.Record;
import lab.ia.ExpenseManagement.Models.User;
import lab.ia.ExpenseManagement.Payloads.Request.RecordRequest;
import lab.ia.ExpenseManagement.Payloads.Response.ApiResponse;
import lab.ia.ExpenseManagement.Payloads.Response.PagedResponse;
import lab.ia.ExpenseManagement.Repositories.CategoryRepository;
import lab.ia.ExpenseManagement.Repositories.RecordRepository;
import lab.ia.ExpenseManagement.Repositories.UserRepository;
import lab.ia.ExpenseManagement.Security.UserPrincipal;
import lab.ia.ExpenseManagement.Services.RecordServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RecordServerImpl implements RecordServer {
    @Autowired
    RecordRepository recordRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public PagedResponse<Record> getAllRecords(int page, int size, UserPrincipal currentUser) {
        User user = userRepository.getUserByUserPrincipal(currentUser);
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<Record> records = recordRepository.findRecordsByUser(user, pageable);
        return new PagedResponse<>(
                records.getContent(),
                records.getNumber(),
                records.getSize(),
                records.getTotalPages(),
                records.getTotalElements(),
                records.isLast());
    }

    @Override
    public Record getRecord(Long id, UserPrincipal currentUser) {
        User user = userRepository.getUserByUserPrincipal(currentUser);
        Record record = recordRepository.findRecordByUserAndId(user, id);
        if (record == null)
            throw new ResourceNotFoundException("Record", "id", String.valueOf(id));
        return record;
    }

    @Override
    public PagedResponse<Record> getRecordsInRange(int page, int size, Date startDate, Date endDate, UserPrincipal currentUser) {
        if (startDate == null && endDate == null)
            return this.getAllRecords(page, size, currentUser);
        User user = userRepository.getUserByUserPrincipal(currentUser);
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<Record> records;
        if (endDate == null)
            records = recordRepository.findRecordsByUserAndDateAfter(
                    user,
                    startDate,
                    pageable);
        else if (startDate == null)
            records = recordRepository.findRecordsByUserAndDateBefore(
                    user,
                    endDate,
                    pageable);
        else
            records = recordRepository.findRecordsByUserAndDateBetween(
                    user,
                    startDate,
                    endDate,
                    pageable);
        return new PagedResponse<>(
                records.getContent(),
                records.getNumber(),
                records.getSize(),
                records.getTotalPages(),
                records.getTotalElements(),
                records.isLast());
    }

    private List<Category> getCategoryFromCategoryIdList(List<Long> categoryIdList, String username) {
        List<Category> categories = new ArrayList<>();
        for (long categoryID : categoryIdList) {
            Category category = categoryRepository.findById(categoryID)
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", String.valueOf(categoryID)));
            if (!category.getUser().getUsername().equals(username))
                throw new BadRequestException(new ApiResponse(false, String.format("Category id %s does not belong to user %s!", categoryID, username)));
            categories.add(category);
        }
        return categories;
    }

    @Override
    public Record addRecord(RecordRequest recordRequest, UserPrincipal currentUser) {
        User user = userRepository.getUserByUserPrincipal(currentUser);
        if (!recordRequest.isValid())
            throw new BadRequestException(new ApiResponse(false, "Request is invalid!"));
        List<Category> categories = this.getCategoryFromCategoryIdList(recordRequest.getCategories(), user.getUsername());
        Record record = new Record(user, recordRequest.getName(), recordRequest.getNote(), recordRequest.getType(),
                recordRequest.getAmount(), recordRequest.getDate(), categories);
        return recordRepository.save(record);
    }

    @Override
    public ApiResponse updateRecord(Long id, RecordRequest newRecord, UserPrincipal currentUser) {
        Record recordToUpdate = getRecord(id, currentUser);
        if (recordToUpdate == null) {
            throw new ResourceNotFoundException("Record", "id", id);
        }

        try {
            DBContext dbContext = new DBContext();
            RecordDAO recordDAO = new RecordDAO(dbContext);

            if (!newRecord.getName().isBlank()) {
                String name = sanitizeInput(newRecord.getName());
                recordToUpdate.setName(name);
            }
            if (!newRecord.getNote().isBlank()) {
                String note = sanitizeInput(newRecord.getNote());
                recordToUpdate.setNote(note);
            }
            if (newRecord.getType() != null) {
                ERecordType type = newRecord.getType();
                recordToUpdate.setType(type);
            }
            if (newRecord.getAmount() != null) {
                double amount = newRecord.getAmount();
                recordToUpdate.setAmount(amount);
            }
            if (newRecord.getDate() != null) {
                Date date = newRecord.getDate();
                recordToUpdate.setDate(date);
            }
            if (newRecord.getCategories() != null) {
                List<Category> categories = getCategoryFromCategoryIdList(newRecord.getCategories(), currentUser.getUsername());
                recordToUpdate.setCategories(categories);
            }

            recordDAO.updateRecord(recordToUpdate);

            return new ApiResponse(true, String.format("Record %s updated successfully!", id));
        } catch (SQLException e) {
            return new ApiResponse(false, "Failed to update record: " + e.getMessage());
        }
    }

    private String sanitizeInput(String input) {
        // Implement input sanitization technique (e.g., parameterized queries, input validation)
        // to prevent SQL injection attacks.
        // Example: Use prepared statements or an ORM that handles parameter binding.
        return input;
    }


    @Override
    public ApiResponse deleteRecord(Long id, UserPrincipal currentUser) {
        Record record = this.getRecord(id, currentUser);
        recordRepository.delete(record);
        return new ApiResponse(true, String.format("Record %s deleted successfully!", id));
    }
}
