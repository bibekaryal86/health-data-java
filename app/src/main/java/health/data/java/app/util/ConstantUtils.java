package health.data.java.app.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConstantUtils {

    // provided at runtime
    public static final String SERVER_PORT = "PORT";
    public static final String DB2_USERNAME = "DB2USR";
    public static final String DB2_PASSWORD = "DB2PWD";
    public static final String DB2_HOSTNAME = "DB2HST";
    public static final String DB2_PORT_NUM = "DB2DEP";
    public static final String BASIC_AUTH_USR = "BASIC_AUTH_USR";
    public static final String BASIC_AUTH_PWD = "BASIC_AUTH_PWD";

    // other constants
    public static final String STH_WENT_WRONG = "Error! Something Went Wrong!! Please Try Again!!!";
    public static final String ERR_MSG_FIND = "Error Retrieving Objects from Database! Please Try Again!!!";
    public static final String ERR_MSG_SAVE = "Error Saving Object to Database! Please Try Again!!!";
    public static final String ERR_MSG_DELETE = "Error Deleting Object from Database! Please Try Again!!!";
    public static final String ERR_MSG_MISSING = "Error! Required Input Missing or Invalid!! Please Try Again!!!";
}
