package lab.ia.ExpenseManagement.Controllers;

import lab.ia.ExpenseManagement.Payloads.Request.CategoryRequest;
import lab.ia.ExpenseManagement.Security.CurrentUser;
import lab.ia.ExpenseManagement.Security.UserPrincipal;
import lab.ia.ExpenseManagement.Services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getCategories(@RequestParam(name = "page", defaultValue = "0") int page,
                                          @RequestParam(name = "size", defaultValue = "10") int size,
                                          @CurrentUser UserPrincipal currentUser) {
        return ResponseEntity.ok(categoryService.getCategories(page, size, currentUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategory(@PathVariable("id") Long id,
                                         @CurrentUser UserPrincipal currentUser) {
        return ResponseEntity.ok(categoryService.getCategory(id, currentUser));
    }

    @PostMapping
    public ResponseEntity<?> addCategory(@RequestBody CategoryRequest categoryRequest,
                                         @CurrentUser UserPrincipal currentUser) {
        return ResponseEntity.ok(categoryService.addCategory(categoryRequest, currentUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Long id,
                                            @RequestBody CategoryRequest newCategory,
                                            @CurrentUser UserPrincipal currentUser) {
        return ResponseEntity.ok(categoryService.updateCategory(id, newCategory, currentUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id,
                                            @CurrentUser UserPrincipal currentUser) {
        return ResponseEntity.ok(categoryService.deleteCategory(id, currentUser));
    }
}
