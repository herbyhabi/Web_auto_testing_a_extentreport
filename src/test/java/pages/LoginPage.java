package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class LoginPage extends BasicPage{

    //Image: verification image
    @FindBy(xpath="//div[@class = 'login']//img")
    public WebElement img;

    //Input: username, password, and verification code
    @FindBy(xpath="//div[@class = 'login']//input")
    public List<WebElement> listOfInput;

    //Text: App name
    @FindBy(xpath="//div[@class = 'name fl']")
    public WebElement textOfAppName;

    //Button: Login button
    @FindBy(xpath="//div[@class = 'login']//button")
    public WebElement btnOfLogin;



}
