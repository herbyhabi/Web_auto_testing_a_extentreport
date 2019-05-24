package utils;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.ResourceCDN;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ReportFactory extends TestBase {

    public static ExtentHtmlReporter htmlReporter;
    public static ExtentReports extentReports = new ExtentReports();
    public static Map<Long, String> threadToExtentTestMap = new HashMap<Long, String>();
    public static Map<String, ExtentTest> nameToTestMap = new HashMap<String, ExtentTest>();

    private synchronized static ExtentHtmlReporter getExtentReport() {
        if (htmlReporter == null) {
            htmlReporter = new ExtentHtmlReporter("./test-output/ExtentReport_"+buildNumber + ".html");
            htmlReporter.config().setTheme(Theme.STANDARD);
            htmlReporter.config().setResourceCDN(ResourceCDN.EXTENTREPORTS); //解决报告没有css样式的问题
            htmlReporter.config().setDocumentTitle("ExtentReports - Automation testing");
            htmlReporter.config().setReportName("ExtentReports - Automation testing Demo");
            htmlReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);
            extentReports.setSystemInfo("OS","Windows10, 64");
            extentReports.setSystemInfo("Type","Automation Testing");
            extentReports.setSystemInfo("User","He, Ying");

            extentReports.attachReporter(htmlReporter);

        }
        return htmlReporter;
    }

    public synchronized static ExtentTest getTest(String testName) {
        return getTest(testName, "");
    }

    public synchronized static ExtentTest getTest(String testName, String categoryName) {

        // if this test has already been created return
        if (!nameToTestMap.containsKey(testName)) {
            Long threadID = Thread.currentThread().getId();
            getExtentReport().start();
            ExtentTest test = extentReports.createTest(testName,categoryName);

            test.assignCategory(categoryName);
            nameToTestMap.put(testName, test);
            threadToExtentTestMap.put(threadID, testName);
        }
        return nameToTestMap.get(testName);
    }

    public synchronized static ExtentTest getTest() {
        Long threadID = Thread.currentThread().getId();

        if (threadToExtentTestMap.containsKey(threadID)) {
            String testName = threadToExtentTestMap.get(threadID);
            return nameToTestMap.get(testName);
        }
        //system log, this shouldn't happen but in this crazy times if it did happen log it.
        return null;
    }

    public synchronized static void closeTest(String testName) {

        if (!testName.isEmpty()) {
            ExtentTest test = getTest(testName);
        }
    }

    public synchronized static void closeTest(ExtentTest test) {
        if (test != null) {
            getExtentReport().flush();
            extentReports.flush();

        }
    }

    public synchronized static void closeTest() {
        ExtentTest test = getTest();
        closeTest(test);
    }

    public synchronized static void closeReport() throws IOException {
        if (htmlReporter != null) {
            htmlReporter.flush();
            extentReports.flush();
//            File currentReport = new File("/test-output/ExtentReport" + ".html");
//            FileUtils.copyFile(currentReport, new File("/test-output/ExtentReport.html"));
        }
    }




}
