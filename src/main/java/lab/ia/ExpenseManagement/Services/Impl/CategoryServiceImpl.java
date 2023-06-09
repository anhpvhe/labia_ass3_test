package lab.ia.ExpenseManagement.Services.Impl;

import lab.ia.ExpenseManagement.Exceptions.AccessDeniedException;
import lab.ia.ExpenseManagement.Exceptions.ResourceNotFoundException;
import lab.ia.ExpenseManagement.Models.Category;
import lab.ia.ExpenseManagement.Models.User;
import lab.ia.ExpenseManagement.Payloads.Request.CategoryRequest;
import lab.ia.ExpenseManagement.Payloads.Response.ApiResponse;
import lab.ia.ExpenseManagement.Payloads.Response.PagedResponse;
import lab.ia.ExpenseManagement.Repositories.CategoryRepository;
import lab.ia.ExpenseManagement.Repositories.UserRepository;
import lab.ia.ExpenseManagement.Security.UserPrincipal;
import lab.ia.ExpenseManagement.Services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public PagedResponse<Category> getCategories(int page, int size, UserPrincipal currentUser) {
        User user = userRepository.getUserByUserPrincipal(currentUser);
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categories = categoryRepository.findCategoriesByUser(user, pageable);
        return new PagedResponse<>(categories.getContent(), categories.getNumber(), categories.getSize(), categories.getTotalPages(), categories.getTotalElements(), categories.isLast());
    }

    @Override
    public Category addCategory(CategoryRequest category, UserPrincipal currentUser) {
        User user = userRepository.getUserByUserPrincipal(currentUser);
        Category newCategory = new Category(category.getName(), category.getDescription(), user);
        return categoryRepository.save(newCategory);
    }

    @Override
    public Category getCategory(Long id, UserPrincipal currentUser) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", String.valueOf(id)));
        if (!category.getUser().getUsername().equals(currentUser.getUsername()) && !currentUser.isAdmin())
            throw new AccessDeniedException(new ApiResponse(false, "You do not have access to this resource!"));
        return category;
    }

    @Override
    public Category updateCategory(Long id, CategoryRequest newCategory, UserPrincipal currentUser) {
        Category category = this.getCategory(id, currentUser);
        category.setName(newCategory.getName());
        category.setDescription(newCategory.getDescription());
        return categoryRepository.save(category);
    }

    @Override
    public ApiResponse deleteCategory(Long id, UserPrincipal currentUser) {
        Category category = this.getCategory(id, currentUser);
        categoryRepository.delete(category);
        return new ApiResponse(true, String.format("Deleted successfully category %s", id));
    }
}
