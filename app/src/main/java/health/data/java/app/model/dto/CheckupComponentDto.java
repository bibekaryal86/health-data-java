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
@Table(name = "hdt_checkup_component")
public class CheckupComponentDto implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "category_id", nullable = false)
    private Integer categoryId;

    @Column(name = "component_name", nullable = false)
    private String componentName;

    @Column(name = "component_standard", nullable = false)
    private String componentStandard;

    @Column(name = "component_comments")
    private String componentComments;
}
