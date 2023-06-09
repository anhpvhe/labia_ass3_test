package lab.ia.ExpenseManagement.Repositories;

import lab.ia.ExpenseManagement.Models.Category;
import lab.ia.ExpenseManagement.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findCategoriesByUser(User user, Pageable pageable);
}
