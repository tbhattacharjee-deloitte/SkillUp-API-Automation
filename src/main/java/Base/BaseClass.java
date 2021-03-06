package Base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.util.Properties;

public class BaseClass {
    public Logger logger;
    public static ExtentTest test;
    public static ExtentReports extent;
    public static Properties prop;

    static {
        extent = new ExtentReports();
        extent.attachReporter(new ExtentHtmlReporter("extent.html"));
    }

    public BaseClass() {
        logger = LogManager.getLogger(BaseClass.class.getName());
        //extent.attachReporter(new ExtentHtmlReporter("extent.html"));
    }

    static {
        try{
            prop = new Properties();
            FileInputStream ip=new FileInputStream(System.getProperty("user.dir") + "\\src\\main\\java\\config\\categoryConfig.properties");
            prop.load(ip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
