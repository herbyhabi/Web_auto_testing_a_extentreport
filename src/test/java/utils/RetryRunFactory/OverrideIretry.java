package utils.RetryRunFactory;

import org.apache.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.Reporter;

public class OverrideIretry implements IRetryAnalyzer {
    public static Logger logger=Logger.getLogger(OverrideIretry.class);
    public int retryCount=1;
    private static int maxRetryCount ;



    static {
        //外围文件配置最大运行次数，失败后重跑maxRetryCount+1次

        maxRetryCount = 2;//也就是失败后重跑3次
        logger.info("maxRunCount=" + (maxRetryCount));
    }

    /**
     * override the retry method.
     * @param iTestResult
     * @return true means will retry to run case, false means not try to run case
     * @authot He, Ying
     */

    @Override public boolean retry(ITestResult iTestResult){
        if(retryCount <= maxRetryCount) {
            String message = "running retry for '" + iTestResult.getName() + "' on class " +
                this.getClass().getName() + " Retrying " + retryCount + " times";
            logger.info(message);
            Reporter.setCurrentTestResult(iTestResult);
            Reporter.log("RunCount=" + (retryCount + 1));
            retryCount++;
            return true;
        }
        return false;
    }
}