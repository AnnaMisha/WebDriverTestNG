package day2;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;
import java.util.Set;

public class day3 {
    WebDriver driver;
    WebDriverWait wait;
    String adminPageURL;

    void navigateToURL(String url){
        driver.get(url);
        wait.until(ExpectedConditions.titleIs("The Internet"));
    }

    void login(){
        By nameField = By.cssSelector("input[name=username]");
        By passwordField = By.cssSelector("input[name=password]");
        By loginButton = By.cssSelector("button[name=login]");
        By logo = By.cssSelector("div.logotype");
        driver.findElement(nameField).sendKeys("admin");
        driver.findElement(passwordField).sendKeys("admin");
        driver.findElement(loginButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(logo));
    }

    void sleepFor(int i){
        try{
            Thread.sleep(i*1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    @BeforeTest
    private void setupBrowser() {
        String geckoPath = System.getProperty("user.dir") + "\\src\\test\\resources\\geckodriver.exe";
        System.setProperty("webdriver.gecko.driver", geckoPath);
        driver = new FirefoxDriver();
        wait = new WebDriverWait(driver,100);
        adminPageURL = "https://the-internet.herokuapp.com/windows";
    }
    @Test
    public void testWindows() {
        navigateToURL(adminPageURL);
        String initialWindowHandle = driver.getWindowHandle();
        Set<String> oldWindowHandles = driver.getWindowHandles();
        checkNewWindowLink(initialWindowHandle, oldWindowHandles);
    }

    private void checkNewWindowLink(String initialWindowHandle, Set<String> oldWindowHandles) {
        clickOnLink();
        String newWindowHandle = wait.until(getNewWindowHandle(oldWindowHandles));
        driver.switchTo().window(newWindowHandle);
        wait.until(ExpectedConditions.titleIs("New Window"));
        String text = driver.findElement(By.cssSelector("div.example h3")).getAttribute("textContent");
        Assert.assertEquals(text, "New Window");
        sleepFor(1);
        driver.close();
        driver.switchTo().window(initialWindowHandle);
        sleepFor(1); 
    }

    private ExpectedCondition<String> getNewWindowHandle(final Set<String> oldWindows) {
        return  new ExpectedCondition<String>() {
            public String apply(WebDriver webDriver) {
                Set<String> handles = driver.getWindowHandles();
                handles.removeAll(oldWindows);
                return handles.size() > 0 ? handles.iterator().next() : null;
            }
        };
    }

    private void clickOnLink() {
        By link = By.cssSelector("div[id=content] a");
        driver.findElement(link).click();
    }

    @AfterTest
    private void closeBrowser() {
        driver.quit();
    }
}
