package day2;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;

public class day2 {
    private static WebDriver driver;
    private static WebDriverWait wait;
    @BeforeTest
    public void setup(){
        System.setProperty("webdriver.chrome.driver", "c:\\Tools\\chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 100);
    }

    @Test
    public void testGoogleSearch() {

        driver.get("https://google.com");
        By element = By.cssSelector("input[name=q]");
        waitFor(element);
        sendKey(element, "Portnov");
        sleepFor(5);
    }

    private void sleepFor(int i) {
        try {
            Thread.sleep(i*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterTest
    public void setdown()    {
        driver.quit();
    }

    private void waitFor(By element){
        WebElement search = wait
                .until(ExpectedConditions.presenceOfElementLocated(element));
    }

    private void  sendKey(By element, String text){
        WebElement webElement = driver.findElement(element);
        webElement.sendKeys(text);
        webElement.submit();
    }
}
