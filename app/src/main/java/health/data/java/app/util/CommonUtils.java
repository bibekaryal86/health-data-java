package health.data.java.app.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
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

    public static boolean isValidNumber(String id) {
        boolean isValid = false;

        if (StringUtils.hasText(id))
            try {
                new BigDecimal(id);
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
        return isValidNumber(id) ? Integer.parseInt(id) : null;
    }

    public static String findStandardRange(BigDecimal standardLow, BigDecimal standardHigh, String measureUnit) {
        if (standardLow != null && standardHigh != null && StringUtils.hasText(measureUnit)) {
            return String.format("%s - %s %s", standardLow, standardHigh, measureUnit);
        }
        return "";
    }
}
