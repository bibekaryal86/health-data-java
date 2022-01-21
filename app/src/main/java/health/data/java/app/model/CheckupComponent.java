package health.data.java.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckupComponent implements Serializable {
    private String id;
    private CheckupCategory checkupCategory;
    private String componentName;
    private BigDecimal standardLow;
    private BigDecimal standardHigh;
    private String measureUnit;
    private String componentComments;
    private String standardRange;
}
