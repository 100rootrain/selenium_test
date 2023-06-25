package com.codingrecipe.project01.controller;


import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class FacebookSelenium {
    private static final Logger log = LoggerFactory.getLogger(FacebookSelenium.class);

    public static void main(String[] args) throws NoSuchElementException {

        FacebookSelenium selTest = new FacebookSelenium();
        selTest.FacebookCrawl();
    }

    //WebDriver
    private WebDriver driver;

    //Properties
    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static final String WEB_DRIVER_PATH = "C:\\Users\\backsung\\selenium_test\\src\\main\\resources\\selenium\\chromedriver_win32/chromedriver.exe";

    //크롤링 할 URL
    private String base_url;


    public FacebookSelenium() {
        super();

        //System Property SetUp
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        //Driver SetUp
        //driver = new ChromeDriver();
        base_url = "https://www.facebook.com/";


        // Driver SetUp
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless"); // 창이 표시되지 않도록 설정 (백그라운드에서 실행)
        //options.addArguments("start-maximized");
        driver = new ChromeDriver(options);


    }

    public void FacebookCrawl() {

        try {
            //get page (= 브라우저에서 url을 주소창에 넣은 후 request 한 것과 같다)
            driver.get(base_url);
            Thread.sleep(3000);


            //아이디입력
            WebElement idInput = driver.findElement(By.xpath("//input[@data-testid='royal_email']"));
            log.info("아이디입력");
            idInput.sendKeys("qor0923@naver.com");
            Thread.sleep(2000); // 2초 대기

            //비밀번호입력
            WebElement passwordInput = driver.findElement(By.xpath("//input[@data-testid='royal_pass']"));
            log.info("비밀번호입력");
            passwordInput.sendKeys("!@qnpfrrmsdn7A");
            Thread.sleep(2000); // 2초 대기
            passwordInput.sendKeys(Keys.ENTER);


            //로그인버튼클릭
//            WebElement loginButton = driver.findElement(By.xpath("(//input[@data-testid='royal_login_button'])"));
//            log.info("로그인버튼클릭");
//            loginButton.click();
//
//            Thread.sleep(6000); // 6초 대기



            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // 최대 5초 동안 대기
            Actions actions = new Actions(driver);

            JavascriptExecutor executor = (JavascriptExecutor) driver;

            // 좋아요 여부
            String goodButton = "(//div[@aria-label='좋아요'])";
            List<WebElement> goodElements = driver.findElements(By.xpath(goodButton));



            while(true){
                log.info("while 시작");
                Thread.sleep(5000);

                for (WebElement goodElement : goodElements) {
                    Thread.sleep(1500);
                    actions.moveToElement(goodElement).perform();
                    Thread.sleep(1500);
                    goodElement.click();
                    log.info("좋아요 클릭");
                }

                log.info("좋아요를 위한 아래로 페이지스크롤하겠습니다.");
                long durationInMillis = 5000; // 5초
                long endTime = System.currentTimeMillis() + durationInMillis;

                while (System.currentTimeMillis() < endTime) {
                    actions.sendKeys(Keys.PAGE_DOWN).perform();
                }
                log.info("PAGE_DOWN종료 WHILE문 처음으로 돌아갑니다");
                Thread.sleep(5500);


            }






        } catch (Exception e) {
            log.error("작업 중 오류가 발생했습니다", e);
            e.printStackTrace();
        }finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
