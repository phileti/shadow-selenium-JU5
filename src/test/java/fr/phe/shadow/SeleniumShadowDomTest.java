package fr.phe.shadow;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


@TestMethodOrder(OrderAnnotation.class)
public class SeleniumShadowDomTest {

    WebDriver driver;

    @BeforeEach
    public void open() {
        WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("disable-extensions",
				"disable-popup-blocking",
				"window-size=1024,768");
        driver = new ChromeDriver(options);
        driver.get("http://watir.com/examples/shadow_dom.html");
    }

    @AfterEach
    public void quit() {
        if (driver != null) {
            driver.quit();
        }
    }
    
	@Test
	@Order(1)
	public void checkDomContent() {
		WebElement link = driver.findElement(By.cssSelector("a"));
		
		Assertions.assertEquals("scroll.html", link.getText());
	}

    @Test
    @Order(2)
    public void checkShadowContent() {
        WebElement shadowHost = driver.findElement(By.cssSelector("#shadow_host"));
        SearchContext shadowRoot = shadowHost.getShadowRoot();
        WebElement shadowContent = shadowRoot.findElement(By.cssSelector("#shadow_content"));
        
        Assertions.assertEquals("some text", shadowContent.getText());
    }

    @Test
    @Order(3)
    public void checkShadowInput() {
        WebElement shadowHost = driver.findElement(By.cssSelector("#shadow_host"));
        SearchContext shadowRoot = shadowHost.getShadowRoot();
        WebElement shadowInput = shadowRoot.findElement(By.cssSelector("input[type='text']"));
        
        if (!shadowInput.getText().isEmpty()) {
    		shadowInput.clear();
        }
        String msg = "je remplis le text shadow";
        shadowInput.sendKeys(msg);
        
        Assertions.assertEquals(msg, shadowInput.getAttribute("value"));
    }
}
