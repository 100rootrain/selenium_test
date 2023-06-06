package com.codingrecipe.project01.controller;


import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class InstagramSelenium {
    private static final Logger log = LoggerFactory.getLogger(InstagramSelenium.class);

    public static void main(String[] args)  throws NoSuchElementException {

        InstagramSelenium selTest = new InstagramSelenium();
        selTest.InstaCrawl();
    }

    //WebDriver
    private WebDriver driver;

    //Properties
    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static final String WEB_DRIVER_PATH = "C:\\Users\\backsung\\selenium_test\\src\\main\\resources\\selenium\\chromedriver_win32/chromedriver.exe";

    //크롤링 할 URL
    private String base_url;

    private String targetURL;

    public InstagramSelenium() {
        super();

        //System Property SetUp
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        //Driver SetUp
        //driver = new ChromeDriver();
        base_url = "https://www.instagram.com/";

        // 로그인 이후의 URL
        targetURL = "https://www.instagram.com/explore/?next=%2F";

        // Driver SetUp
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless"); // 창이 표시되지 않도록 설정 (백그라운드에서 실행)
        //options.addArguments("start-maximized");
        driver = new ChromeDriver(options);


    }

    public void InstaCrawl()  {

        try {
            //get page (= 브라우저에서 url을 주소창에 넣은 후 request 한 것과 같다)
            driver.get(base_url);
            Thread.sleep(3000);


            //driver.findElement(By.cssSelector("input[name='username']"))의 value값 아이디입력
            WebElement idInput = driver.findElement(By.xpath("(//input[@class='_aa4b _add6 _ac4d'])[1]"));
            idInput.sendKeys("baekkeunwoo");
            log.info("아이디입력");
            Thread.sleep(1000); // 1초 대기

            //driver.findElement(By.cssSelector("input[name='password']"))의 value값 비번입력

            WebElement passwordInput = driver.findElement(By.xpath("(//input[@name='password'])"));
            passwordInput.sendKeys("!@qnpfrrmsdn7A");
            log.info("비밀번호입력");
            Thread.sleep(1000); // 1초 대기

            // 로그인 버튼 클릭
            WebElement loginButton = driver.findElement(By.cssSelector("._acan._acap._acas._aj1-"));
            loginButton.click();
            log.info("loginButton을 클릭합니다: " + loginButton.getText());
            Thread.sleep(9000); // 9초 대기(모바일 계정확인 요청)


            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // 최대 5초 동안 대기


            Actions actions = new Actions(driver);


            driver.get(targetURL);




            // 첫번째 게시물 요소들을 선택
            WebElement firstBoard = driver.findElement(By.className("_al5u"));
            log.info("firstBoard : "+firstBoard);


            //첫번째 게시물에 진입하면서 targetURL 초기화가됨
            firstBoard.click();

            Thread.sleep(3000);
            wait.until(ExpectedConditions.urlContains("https://www.instagram.com/p/"));
            log.info("=======");



            // SVG 태그를 찾기 위해 XPath 사용
            String svgGoodList = "//*[local-name()='svg' and @aria-label='좋아요']";
            String svgGoodCancel = "//svg[@aria-label='좋아요 취소']";


            //다음 버튼
            String nextButton = "//*[local-name()='svg' and @aria-label='다음']";

            goodCheck:
            while (true) {
                Thread.sleep(5000);

                try {
                    List<WebElement> svgElements = driver.findElements(By.xpath(svgGoodList));

                    log.info("svgElements" + svgElements);
//                    for (WebElement svgElement : svgElements) {
//                        log.info("svgElement :" + svgElement);
//                        Thread.sleep(2000);
//                        svgElement.click();
//
//                    }
                    int size = svgElements.size();
                    int k = 2;
                    log.info("size : " + size);
                    for (int i = 1; i < size; i++) {
                        if(i%k ==1){
                            log.info("@@@@@@@@@@@@@@@@@@@");
                            // SVG 태그를 찾기 위해 XPath 사용
                            String svgGood = "(//*[local-name()='svg' and @aria-label='좋아요'])[" + i + "]";
//                            String parentButtonXPath = svgGood + "/ancestor::button/ancestor::button";
//                            WebElement parentButton = driver.findElement(By.xpath(parentButtonXPath));
//                            parentButton.click();


                            log.info("svgGood : " + svgGood);
                            log.info("222222222222222222");
                            Thread.sleep(1000);
                            driver.findElement(By.xpath(svgGood)).click();
                            svgGood = "";
                            Thread.sleep(1000);
                        }else{
                            continue;
                        }



                    }


                } catch (Exception e) {
                    log.info("더 이상 좋아요를 할 수 없습니다.");
                    try {
                        log.info("다음 버튼 : " + nextButton);
                        driver.findElement(By.xpath(nextButton)).click();
                        Thread.sleep(2000); // 1초 대기
                    } catch (NoSuchElementException ex) {
                        break goodCheck; // 다음 버튼이 없는 경우 반복 종료
                    }
                }

            }











        } catch (Exception e) {
            log.error("작업 중 오류가 발생했습니다", e);
            e.printStackTrace();

        } finally {
            if (driver != null) {
                driver.quit();
            }
        }

    }


}
