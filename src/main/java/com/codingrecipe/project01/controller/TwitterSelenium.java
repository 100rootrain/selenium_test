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

public class TwitterSelenium {
    private static final Logger log = LoggerFactory.getLogger(TwitterSelenium.class);

    public static void main(String[] args) throws NoSuchElementException {

        TwitterSelenium selTest = new TwitterSelenium();
        selTest.TwitterCrawl();
    }

    //WebDriver
    private WebDriver driver;

    //Properties
    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static final String WEB_DRIVER_PATH = "chromedriver.exe 경로";

    //크롤링 할 URL
    private String base_url;


    public TwitterSelenium() {
        super();

        //System Property SetUp
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        //Driver SetUp
        //driver = new ChromeDriver();
        base_url = "https://twitter.com/i/flow/login";


        // Driver SetUp
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless"); // 창이 표시되지 않도록 설정 (백그라운드에서 실행)
        //options.addArguments("start-maximized");
        driver = new ChromeDriver(options);


    }

    public void TwitterCrawl() {

        try {
            //get page (= 브라우저에서 url을 주소창에 넣은 후 request 한 것과 같다)
            driver.get(base_url);
            Thread.sleep(3000);


            //아이디입력
            WebElement idInput = driver.findElement(By.cssSelector(".r-30o5oe.r-1niwhzg.r-17gur6a.r-1yadl64.r-deolkf.r-homxoj.r-poiln3.r-7cikom.r-1ny4l3l.r-t60dpp.r-1dz5y72.r-fdjqy7.r-13qz1uu"));
            idInput.sendKeys("id");
            log.info("아이디입력");
            Thread.sleep(2000); // 1초 대기

            //로그인버튼클릭
            WebElement NextButton = driver.findElement(By.xpath("(//span[contains(text(),'다음')])"));
            NextButton.click();
            Thread.sleep(2000); // 1초 대기

            //비밀번호입력
            WebElement passwordInput = driver.findElement(By.xpath("(//input[@name='password'])"));
            passwordInput.sendKeys("pw");
            passwordInput.sendKeys(Keys.ENTER);
            log.info("비밀번호입력");
            Thread.sleep(6000); // 1초 대기



            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // 최대 5초 동안 대기
            Actions actions = new Actions(driver);

            // 트렌드
            String trendButton = "(//div[@data-testid='trend'])";

            List<WebElement> trendElements = driver.findElements(By.xpath(trendButton));
            int trendElementCount = trendElements.size();

            JavascriptExecutor executor = (JavascriptExecutor) driver;



            trendChk:try{
                if (trendElementCount > 0) {
                    // 랜덤한 인덱스 생성
                    Random random = new Random();
                    int randomIndex = random.nextInt(trendElementCount);

                    // 랜덤한 요소 선택
                    WebElement randomElement = trendElements.get(randomIndex);
                    Thread.sleep(3000);
                    log.info("랜덤 트랜드 선택");
                    actions.moveToElement(randomElement).perform();
                    Thread.sleep(3000);

                    //트랜드랜덤버튼 클릭
                    randomElement.click();

                    // 좋아요 여부
                    String pathGoodList = "(//div[@data-testid='like'])";

                    // 팝업 여부
                    String maskPopup = "(//div[@data-testid='mask'])";

                    Thread.sleep(8000);

                    //좋아요 버튼
                    List<WebElement> pathGoodElements = driver.findElements(By.xpath(pathGoodList));


                        goodCheck:
                        while (true) {


                            try {
                                log.info("=======>>>>>>>>♡♥좋아요 버튼 누르는곳 시작입니다.");
                                Thread.sleep(4000);


                                // pathGoodElements 배열의 길이 확인
                                if (pathGoodElements.size() > 0) {
                                    // pathGoodElements 배열이 존재하는 경우에만 실행될 코드 작성
                                    log.info("좋아요남은게시글갯수 : " + pathGoodElements.size() + " 개");


                                    for (WebElement pathGoodElement : pathGoodElements) {
                                        WebElement parentElement = driver.findElement(By.xpath("(//div[@data-testid='like'])[1]/.."));

                                        try{


                                            //팝업체크
                                            popupchk:try{
                                                if(driver.findElement(By.xpath(maskPopup)).isDisplayed()){
                                                    log.info("팝업창이 떴습니다. 스킵버튼을 눌러주세요.");
                                                    WebElement skipButton = driver.findElement(By.xpath("(//span[contains(text(),'Skip for now')])"));
                                                    skipButton.click();
                                                    continue;
                                                }

                                            }catch(Exception e){
                                                log.info("정상페이지입니다. 다음 for문으로 넘어가겠습니다.");
                                                break popupchk;
                                            }

                                            //팝업체크 후 좋아요 동작
                                            actions.moveToElement(parentElement).perform();
                                            Thread.sleep(1500);
                                            parentElement.click();
                                            log.info("좋아요 클릭");

                                        }catch(ElementClickInterceptedException e){

                                            executor.executeScript("arguments[0].scrollIntoView(true);", pathGoodElement);
                                            Thread.sleep(3000);
                                            pathGoodElement.click();
                                            log.info("스크롤 후 좋아요 클릭");

                                        }

                                        Thread.sleep(1500);

                                    }

                                    log.info("좋아요를 위한 아래로 페이지스크롤하겠습니다.");
                                    long durationInMillis = 1000; // 5초
                                    long endTime = System.currentTimeMillis() + durationInMillis;

                                    while (System.currentTimeMillis() < endTime) {
                                        actions.sendKeys(Keys.PAGE_DOWN).perform();
                                    }
                                    log.info("PAGE_DOWN종료 WHILE문 처음으로 돌아갑니다");
                                    Thread.sleep(1500);


                                }else{
                                    //
                                    log.info("좋아요할게 없어요. 작업종료");

                                }

                            } catch (Exception e) {
                                log.error("작업 중 오류가 발생했습니다", e);
                                e.printStackTrace();

                                log.info("<===================================================>");
                                long durationInMillis = 10000; // 10초
                                long endTime = System.currentTimeMillis() + durationInMillis;

                                while (System.currentTimeMillis() < endTime) {
                                    actions.sendKeys(Keys.PAGE_DOWN).perform();
                                }
                                log.info("PAGE_DOWN종료 WHILE문 처음으로 돌아갑니다");

                                Thread.sleep(1500);
                                continue;

                            }

                            if(pathGoodElements.size() < 0){
                                break trendChk;
                            }

                        }
                }
            }catch(Exception e){
                log.info("트렌드가 안보여요");
                break trendChk;
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
