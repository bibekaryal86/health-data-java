package health.data.java.app.repository;

import health.data.java.app.model.dto.CheckupComponentDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckupComponentRepository extends JpaRepository<CheckupComponentDto, Integer> { }
