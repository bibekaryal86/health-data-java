package health.data.java.app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hdt_checkup_component")
public class CheckupComponentDto implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "category_id", nullable = false)
    private Integer categoryId;

    @Column(name = "component_name", nullable = false)
    private String componentName;

    @Column(name = "standard_low")
    private BigDecimal standardLow;

    @Column(name = "standard_high")
    private BigDecimal standardHigh;

    @Column(name = "measure_unit")
    private String measureUnit;

    @Column(name = "component_comments")
    private String componentComments;
}
