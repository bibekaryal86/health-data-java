package health.data.java.app.repository;

import health.data.java.app.model.dto.CheckupResultDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckupResultRepository extends JpaRepository<CheckupResultDto, Integer> {
    List<CheckupResultDto> findCheckupResultDtoByUsername(String username);
}
