import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Flipkart {

	static WebDriverWait w;
	static WebDriver driver;
	static String projPath;

	public static void main(String[] args) throws IOException {

		projPath = System.getProperty("user.dir");
		System.setProperty("webdriver.chrome.driver", projPath + "\\driver\\chromedriver.exe");
		driver = new ChromeDriver();

		driver.get("https://www.flipkart.com");
		driver.manage().window().maximize();

		Actions a = new Actions(driver);
		a.sendKeys(Keys.ESCAPE); // Close the ad if any
		WebElement search = driver.findElement(By.xpath("//input[@name='q']"));
		a.moveToElement(search).click().sendKeys("iphone").sendKeys(Keys.ENTER).build().perform(); // Search iPhone

		w = new WebDriverWait(driver, 5);
		w.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@class='_2b0bUo']/div[3]/select")));
		WebElement price = driver.findElement(By.xpath("//div[@class='_2b0bUo']/div[3]/select"));
		Select priceDropDown = new Select(price);
		priceDropDown.selectByValue("30000"); // Select Price range
		a.moveToElement(driver.findElement(By.xpath("//div[text()='Price -- Low to High']"))).click().build().perform(); //Apply filter on price low to high

		csvWrite();
	}

	private static void csvWrite() throws IOException {
		w = new WebDriverWait(driver, 5);
		w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='_10UF8M _3LsR0e']")));

		List<WebElement> deviceModel = driver.findElements(By.xpath("//div[@class='_4rR01T']")); // Get device model
		String[] model = new String[deviceModel.size()];
		int i = 0;
		for (WebElement m : deviceModel) {
			model[i] = m.getText();
			i++;
		}

		List<WebElement> storageCapacity = driver.findElements(By.xpath("//ul[@class='_1xgFaf']//li[1]")); // Get Storage capacity
		String[] capacity = new String[storageCapacity.size()];
		int j = 0;
		for (WebElement c : storageCapacity) {
			capacity[j] = c.getText();
			j++;
		}

		List<WebElement> customerRating = driver.findElements(By.xpath("//div[@class='_3LWZlK']")); // Get customer // rating
		String[] rating = new String[customerRating.size()];
		int k = 0;
		for (WebElement r : customerRating) {
			rating[k] = r.getText();
			k++;
		}

		String saveDataCsv[] = new String[deviceModel.size()];
		FileWriter writer = new FileWriter(projPath+"\\iPhoneData.csv");
		for (int s = 0; s < deviceModel.size(); s++) {
			saveDataCsv[s] = String.join(",", model[s], capacity[s], rating[s]);

			writer.write(Arrays.asList(saveDataCsv[s]).stream().collect(Collectors.joining())); //write into csv
			writer.write("\n");
			s++;
		}
		writer.flush();
		writer.close();
		driver.close();
		driver.quit();
	}
}
