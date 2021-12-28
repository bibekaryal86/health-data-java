package health.data.java.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckupComponent implements Serializable {
    private String id;
    private CheckupCategory checkupCategory;
    private String componentName;
    private String componentStandard;
    private String componentComments;
}
