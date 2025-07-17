package LinkedInJob;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class LinkedInJobSearch {

    public static void main(String[] args) throws InterruptedException {

        // Set path to ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", "C:\\selenium webdriver\\Chrome webdriver\\chromedriver-win64\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();	

        try {
            driver.manage().window().maximize();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

            //  Open LinkedIn login page
            driver.get("https://www.linkedin.com/login");

            //  Enter email and password
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username"))).sendKeys("vibuddha2025@gmail.com");
            driver.findElement(By.id("password")).sendKeys("Vibuddha123#");
            driver.findElement(By.xpath("//button[@type='submit']")).click();

            
            Thread.sleep(10000); // 10 seconds wait for CAPTCHA or redirect

            // Check if login was successful
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl.contains("checkpoint") || currentUrl.contains("login-submit")) {
                System.out.println("❌ Login failed or verification required.");
                driver.quit();
                return;
            }

            System.out.println("✅ Logged in successfully!");

            // Navigate to Jobs page
            driver.get("https://www.linkedin.com/jobs");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("main")));

            // Dismiss popup if appears (Turn on job alerts, etc.)
            try {
                WebElement dismissBtn = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("button[aria-label='Dismiss']")));
                if (dismissBtn.isDisplayed()) {
                    dismissBtn.click();
                    Thread.sleep(2000);
                }
            } catch (Exception e) {
                System.out.println("No popup found to dismiss.");
            }

            // Enter job title in search field
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[contains(@placeholder, 'Search jobs')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", searchBox);
            searchBox.clear();
            searchBox.sendKeys("Software Engineer");

            // Enter location in search field
            WebElement locationBox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[contains(@placeholder, 'Search location')]")));
            locationBox.clear();
            locationBox.sendKeys("Colombo, Western Province, Sri Lanka");

            // Click the Search button
            WebElement searchBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(@aria-label, 'Search')]")));
            searchBtn.click();

            //  Wait for job results to load
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("ul.jobs-search-results__list")));

            // Get job title elements
            List<WebElement> jobs = driver.findElements(By.cssSelector(
                    "ul.jobs-search-results__list li div.base-search-card__info h3.base-search-card__title"));

            if (jobs.isEmpty()) {
                System.out.println("❗ No job results found.");
            } else {
                System.out.println("✅ Found " + jobs.size() + " job(s):");
                for (WebElement job : jobs) {
                    System.out.println("🔹 Job Title: " + job.getText().trim());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            // Wait before closing
            Thread.sleep(5000);
            driver.quit();
        }
    }
}
