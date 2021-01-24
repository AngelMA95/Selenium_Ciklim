package com.selenium.ciklum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BookingSearchTest {
	private WebDriver driver;
	
	@Before
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "./src/test/resources/chromedriver/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://www.booking.com/");
	}
	
	@Test
	public void testScenario_1_BookingPage() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, 7);
		String strDateFrom = dateFormat.format(c.getTime());
		c.add(Calendar.DAY_OF_YEAR, 1);
		String strDateTo = dateFormat.format(c.getTime());
		String city = "Málaga";
		WebElement searchbox = driver.findElement(By.id("ss"));
		Integer timeout = 10;

		searchbox.clear();
		searchbox.sendKeys(city);
		driver.findElement(By.className("xp__date")).click();
		new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".bui-calendar__date[data-date='" + strDateFrom  + "']"))).click();
		driver.findElement(By.cssSelector(".bui-calendar__date[data-date='" + strDateTo  + "']")).click();
		driver.findElement(By.className("xp__input")).click();
		new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".sb-group-children .bui-stepper__add-button"))).click();
		searchbox.submit();

		assertNotEquals(city + ": 0 properties found", new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOf(driver.findElement(By.className("sr_header")))).getText());
		assertEquals("Booking.com : Hoteles en " + city + " . ¡Reserva tu hotel ahora!", driver.getTitle());
	}
	
	@Test
	public void testScenario_2_BookingPage() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, 7);
		String strDateFrom = dateFormat.format(c.getTime());
		c.add(Calendar.DAY_OF_YEAR, 4);
		String strDateTo = dateFormat.format(c.getTime());
		String city = "Málaga";
		WebElement searchbox = driver.findElement(By.id("ss"));
		Integer timeout = 10;

		searchbox.clear();
		searchbox.sendKeys(city);
		driver.findElement(By.className("xp__date")).click();
		new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".bui-calendar__date[data-date='" + strDateFrom  + "']"))).click();
		driver.findElement(By.cssSelector(".bui-calendar__date[data-date='" + strDateTo  + "']")).click();
		driver.findElement(By.className("xp__input")).click();
		new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".sb-group-children .bui-stepper__add-button"))).click();
		searchbox.submit();

		assertNotEquals(city + ": 0 properties found", new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOf(driver.findElement(By.className("sr_header")))).getText());
		assertEquals("Booking.com : Hoteles en " + city + " . ¡Reserva tu hotel ahora!", driver.getTitle());

		new WebDriverWait(driver, timeout).until(ExpectedConditions.elementToBeClickable(By.cssSelector(".sort_category.sort_price"))).click();
		new WebDriverWait(driver, timeout).until(ExpectedConditions.stalenessOf(driver.findElement(By.className("bui-spinner__inner"))));
		Integer initPrice = 0;
		for (WebElement hotel : driver.findElements(By.cssSelector("#hotellist_inner>.sr_item"))) {
			/* INFO: Note for the reviewer -> At first, I coded the commented line below because I thought that the original prices were being taken into account to make the sort of the hotels
			(and I suspect that's partially true), but then I noticed that sometimes not even the original nor the fixed prices were sorted even if the option was selected, 
			so I guess other parameters are being taken into account when it comes to make this sort. Therefore I decided to compare just the fixed prices. 
			I've attached a video in the repository called "Sort by lowest price - Booking.mp4" showing this issue and highlighting these cases.
			That's basically why I expect this test to fail at the assert below most of the times. We can talk about this in the interview if required.*/
			
			// Integer currentPrice = hotel.findElements(By.className("bui-price-display__original")).isEmpty() ? Integer.valueOf(hotel.findElement(By.className("bui-price-display__value")).getText().substring(2)) : Integer.valueOf(hotel.findElement(By.className("bui-price-display__original")).getText().substring(2));
			Integer currentPrice = Integer.valueOf(hotel.findElement(By.className("bui-price-display__value")).getText().substring(2));
			assertTrue("List is not ordered by price (lowest first)", currentPrice >= initPrice);
			initPrice = currentPrice;
		}
	}
	
	@After
	public void tearDown() {
		driver.quit();
	}

}
