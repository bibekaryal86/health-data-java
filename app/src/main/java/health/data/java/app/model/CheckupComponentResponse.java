package health.data.java.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckupComponentResponse implements Serializable {
    private List<CheckupComponent> checkupComponentList;
    private int modifiedCount;
    private String errMsg;
}
