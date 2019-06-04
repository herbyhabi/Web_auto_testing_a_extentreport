package tests;

import org.testng.annotations.Test;
import pages.BasicPage;
import pages.LoginPage;
import sun.awt.windows.ThemeReader;
import utils.CustomizeAssertion;
import utils.CustomizeFunctions;
import utils.Functions;
import utils.TestBase;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TC01 extends TestBase {
    BasicPage basicPage;
    CustomizeFunctions customizeFunctions;
    CustomizeAssertion customizeAssertion;
    Functions functions;
    LoginPage loginPage;


    @Test
    public void verify_TC01() throws Exception {

        basicPage = new BasicPage();
        customizeFunctions = new CustomizeFunctions();
        customizeAssertion = new CustomizeAssertion();
        functions = new Functions();
        loginPage = new LoginPage();

        String chooseCompanyPageTitle = "选择公司";

        driver.get("http://172.16.3.155/main/login");

        customizeFunctions.input(loginPage.listOfInput.get(0),"13777842891","Fill in the username");
        customizeFunctions.input(loginPage.listOfInput.get(1),"znqt123","Fill in the password");

        functions.getImage(loginPage.img);
        String content = functions.getImgContent();
        customizeFunctions.input(loginPage.listOfInput.get(2),content,"Fill in the verification code");
        customizeFunctions.click(loginPage.btnOfLogin,"Click on login button");

        for(int i =0; i<10; i++){
            Thread.sleep(2000);
            List<String> list = functions.getCurrentPageInfo();
            if(! list.get(1).equals(chooseCompanyPageTitle)){
            customizeFunctions.click(loginPage.img, "update the verification image");
            functions.getImage(loginPage.img);
            content = functions.getImgContent();

            customizeFunctions.click(loginPage.listOfInput.get(2),"");
            loginPage.listOfInput.get(2).clear();
            customizeFunctions.input(loginPage.listOfInput.get(2),content,"");
            customizeFunctions.click(loginPage.btnOfLogin,"");

            }
            else{
                break;
            }
        }

        Thread.sleep(3000);

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
