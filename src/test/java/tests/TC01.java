package tests;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.BasicPage;
import utilities.Log;
import utils.CustomizeAssertion;
import utils.CustomizeFunctions;
import utils.TestBase;

public class TC01 extends TestBase {
    BasicPage basicPage;
    CustomizeFunctions customizeFunctions;
    CustomizeAssertion customizeAssertion;

    @Test
    public void verify_TC01() throws Exception {

        basicPage = new BasicPage();
        customizeFunctions = new CustomizeFunctions();
        customizeAssertion = new CustomizeAssertion();

        driver.get("https://www.baidu.com/");

        Log.info("Step 1: Enter keywords into search field");
        customizeFunctions.input(basicPage.inputOfSearch,"Automation test","Enter a keywords");
        customizeAssertion.assertTrue(true,"verify it is true");

        Log.info("Step 2: Click on Submit button");
        customizeFunctions.click(basicPage.btnOfSubmit,"Click on submit button");

        eventualAssert();


    }

//    @Test
//    public void testMockMethod(){
//        List<String> list = mock(List.class);
//        Mockito.when(list.get(0)).thenReturn("hellow world");
//        String result = list.get(0);
//        System.out.println(result);
//
//        verify(list).get(0);
//        Assert.assertEquals("hellow world", result);
//    }
//
//    @Test
//    public void verify_behaviour(){
//        //模拟创建一个List对象
//        List mock = mock(List.class);
//        //使用mock的对象
//        mock.add(1);
//        mock.clear();
//        //验证add(1)和clear()行为是否发生
//        verify(mock).add(1);
//        verify(mock).clear();
//    }


}
