package lab.ia.ExpenseManagement.Services.Impl;

import lab.ia.ExpenseManagement.Exceptions.BadRequestException;
import lab.ia.ExpenseManagement.Exceptions.ResourceNotFoundException;
import lab.ia.ExpenseManagement.Models.Category;
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
        Record record = this.getRecord(id, currentUser);
        List<Category> categories = this.getCategoryFromCategoryIdList(newRecord.getCategories(), currentUser.getUsername());
        if (!newRecord.getName().isBlank())
            record.setName(newRecord.getName());
        if (!newRecord.getNote().isBlank())
            record.setNote(newRecord.getNote());
        if (newRecord.getType() != null)
            record.setType(newRecord.getType());
        if (newRecord.getAmount() != null)
            record.setAmount(newRecord.getAmount());
        if (newRecord.getDate() != null)
            record.setDate(newRecord.getDate());
        if (newRecord.getCategories() != null)
            record.setCategories(categories);
        recordRepository.save(record);
        return new ApiResponse(true, String.format("Record %s updated successfully!", id));
    }

    @Override
    public ApiResponse deleteRecord(Long id, UserPrincipal currentUser) {
        Record record = this.getRecord(id, currentUser);
        recordRepository.delete(record);
        return new ApiResponse(true, String.format("Record %s deleted successfully!", id));
    }
}
