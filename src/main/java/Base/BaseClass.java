package Base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseClass {
    public Logger logger;
    public static ExtentTest test;
    public static ExtentReports extent;

    static {
        extent = new ExtentReports();
    }

    public BaseClass() {
        logger = LogManager.getLogger(BaseClass.class.getName());
        //extent.attachReporter(new ExtentHtmlReporter("extent.html"));
    }

}
