package ListenersPackage;

import Base.BaseClass;
import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ListenerClass implements ITestListener {
    @Override
    public void onTestStart(ITestResult result) {
        BaseClass.test = BaseClass.extent.createTest(result.getMethod().getMethodName());
        BaseClass.test.log(Status.INFO, result.getMethod().getMethodName() + " test start");
        ITestListener.super.onTestStart(result);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        BaseClass.test.log(Status.PASS, result.getMethod().getMethodName() + " test passed");
        ITestListener.super.onTestSuccess(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        BaseClass.test.log(Status.FAIL, result.getMethod().getMethodName() + " test failed");
        ITestListener.super.onTestFailure(result);
    }

    @Override
    public void onFinish(ITestContext context) {
        BaseClass.extent.flush();
        ITestListener.super.onFinish(context);
    }
}
