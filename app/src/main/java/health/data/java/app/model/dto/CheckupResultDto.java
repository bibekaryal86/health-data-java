package health.data.java.app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hdt_checkup_result")
public class CheckupResultDto implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "component_id", nullable = false)
    private Integer componentId;

    @Column(name = "checkup_date", nullable = false)
    private String checkupDate;

    @Column(name = "test_result", nullable = false)
    private String testResult;

    @Column(name = "result_flag", nullable = false)
    private String resultFlag;

    @Column(name = "username", nullable = false)
    private String username;
}
