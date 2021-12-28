package health.data.java.app.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtils {

    public static String getSystemEnvProperty(String keyName, String defaultValue) {
        String envProperty = System.getProperty(keyName) != null ?
                System.getProperty(keyName) :
                System.getenv(keyName);
        return envProperty == null ?
                defaultValue :
                envProperty;
    }

    public static boolean isValidRequestId(String id) {
        boolean isValid = false;

        if (StringUtils.hasText(id))
            try {
                Integer.parseInt(id);
                isValid = true;
            } catch (NumberFormatException ignored) { /* ignored */ }

        return isValid;
    }

    public static boolean isValidDate(String date) {
        boolean isValid = false;

        if (StringUtils.hasText(date))
            try {
                isValid = LocalDate.parse(date).isBefore(LocalDate.now().plusDays(1L));
            } catch (DateTimeParseException ignored) { /* ignored */ }

        return isValid;
    }

    public static Integer getIntegerId(String id) {
        return isValidRequestId(id) ? Integer.parseInt(id) : null;
    }
}
