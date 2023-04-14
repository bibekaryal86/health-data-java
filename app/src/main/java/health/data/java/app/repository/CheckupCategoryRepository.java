package health.data.java.app.repository;

import health.data.java.app.model.dto.CheckupCategoryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckupCategoryRepository extends JpaRepository<CheckupCategoryDto, Integer> {}
