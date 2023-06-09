package lab.ia.ExpenseManagement.Services;

import lab.ia.ExpenseManagement.Models.Category;
import lab.ia.ExpenseManagement.Payloads.Request.CategoryRequest;
import lab.ia.ExpenseManagement.Payloads.Response.ApiResponse;
import lab.ia.ExpenseManagement.Payloads.Response.PagedResponse;
import lab.ia.ExpenseManagement.Security.UserPrincipal;

public interface CategoryService {
    PagedResponse<Category> getCategories(int page, int size, UserPrincipal userPrincipal);

    Category addCategory(CategoryRequest category, UserPrincipal currentUser);

    Category getCategory(Long id, UserPrincipal currentUser);

    Category updateCategory(Long id, CategoryRequest newCategory, UserPrincipal currentUser);

    ApiResponse deleteCategory(Long id, UserPrincipal currentUser);
}
