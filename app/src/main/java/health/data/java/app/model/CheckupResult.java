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
public class CheckupResult implements Serializable {
    private String id;
    private String username;
    private CheckupComponent checkupComponent;
    private String checkupDate;
    private String testResult;
    private String resultFlag;
}
