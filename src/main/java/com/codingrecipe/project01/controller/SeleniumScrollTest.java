package com.codingrecipe.project01.controller;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class SeleniumScrollTest {
    private static final Logger log = LoggerFactory.getLogger(SeleniumScrollTest.class);

    public static void main(String[] args)  throws NoSuchElementException {

        SeleniumScrollTest selTest = new SeleniumScrollTest();
        selTest.crawl();
    }

    //WebDriver
    private WebDriver driver;

    //Properties
    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static final String WEB_DRIVER_PATH = "C:\\Users\\kma04\\selenium_test\\src\\main\\resources\\selenium\\chromedriver_win32/chromedriver.exe";

    //크롤링 할 URL
    private String base_url;

    //좋아요 개수체크
    private int goodCnt = 0;
    //더보기버튼 개수체크
    private int showMoreCnt=0;

    public SeleniumScrollTest() {
        super();

        //System Property SetUp
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        //Driver SetUp
        //driver = new ChromeDriver();
        base_url = "https://accounts.kakao.com/login/?continue=https://story.kakao.com/#login";

        // Driver SetUp
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless"); // 창이 표시되지 않도록 설정 (백그라운드에서 실행)
        options.addArguments("start-maximized");
        driver = new ChromeDriver(options);


    }

    public void crawl()  {

        try {
            //get page (= 브라우저에서 url을 주소창에 넣은 후 request 한 것과 같다)
            driver.get(base_url);

            Thread.sleep(1000); // 1초 대기

            //loginKey--1의 value값 아이디입력
            WebElement idInput = driver.findElement(By.id("loginKey--1"));
            idInput.sendKeys("");
            Thread.sleep(1000); // 1초 대기

            //password--2의 value값 비번입력
            WebElement passwordInput = driver.findElement(By.id("password--2"));
            passwordInput.sendKeys("");
            Thread.sleep(1000);

            // 로그인 버튼 클릭
            WebElement loginButton = driver.findElement(By.cssSelector(".btn_g.highlight.submit"));
            loginButton.click();
            log.info("loginButton을 클릭합니다: " + loginButton.getText());
            Thread.sleep(2500);

            Actions actions = new Actions(driver);

            // 로그인 후 검색창
            WebElement tfKeword = driver.findElement(By.xpath("//input[@id='tfKeyword']"));
            tfKeword.sendKeys("카카오스토리");
            Thread.sleep(2500);
            WebElement kakaostory = driver.findElement(By.xpath("//a[@href='/ch/kakaostory']"));
            actions.moveToElement(kakaostory).perform();
            kakaostory.click();

            log.info("::카카오스토리를 입력합니다::");
            Thread.sleep(2500);


            // 좋아요버튼
            String likeCommentButtons = "//a[@class='btn_like _btnLikeComment' and text()='좋아요']";


            // 더보기버튼
            List<WebElement> showMoreButtons = driver.findElements(By.xpath("//a[@class='_btnShowMoreComment']"));
            log.info("더보기버튼 갯수 :" +showMoreButtons.size());


            scrollTest:while(true){
                log.info("while시작");

                //like버튼이 없을경우 더보기버튼 클릭  btn_like _btnLikeComment
                showElementClick:try{
                    //like버튼이 없을경우
                    if(driver.findElements(By.xpath(likeCommentButtons)).isEmpty()) {
                        //더보기버튼 클릭
                        for (WebElement showElement : showMoreButtons) {

                            actions.moveToElement(showElement).perform();
                            Thread.sleep(2500);
                            showElement.sendKeys(Keys.ENTER);
                            Thread.sleep(2500);
                            log.info((showMoreButtons.indexOf(showElement) + 1) + "번째 더보기버튼 클릭");
                            showMoreCnt++;
                        }
                    }

                    break showElementClick;

                }catch(Exception e){
                    log.error("showElementClick 에러", e);
                    e.printStackTrace();

                }

                likeBtnClick:try{
                    if(!driver.findElements(By.xpath(likeCommentButtons)).isEmpty()) {
                        List<WebElement> likeCommentBtns = driver.findElements(By.xpath(likeCommentButtons));
                        log.info("likeCommentBtns 총갯수 : " + likeCommentBtns.size());
                        for(WebElement likeCommentBtn : likeCommentBtns){
                            actions.moveToElement(likeCommentBtn).perform();
                            Thread.sleep(2500);
                            likeCommentBtn.sendKeys(Keys.ENTER,Keys.TAB);
                            Thread.sleep(2500);
                            log.info((likeCommentBtns.indexOf(likeCommentBtn) + 1) + "번째 좋아요버튼 클릭");
                            goodCnt++;
                        }

                    }
                    break likeBtnClick;

                }catch(Exception e){
                    log.error("likeBtnClick 에러", e);
                    e.printStackTrace();
                }

/*

                //더보기 && like 버튼 없을경우
                isEmptyChk:try{
                    if(driver.findElements(By.xpath(likeCommentButtons)).isEmpty() &&  driver.findElements(By.xpath(likeCommentButtons)).isEmpty()){
                        log.info("더이상 진행할수없습니다. While문을 빠져나갑니다.");
                    }else{
                        continue scrollTest;
                    }
                    break scrollTest;
                }catch(Exception e){
                    log.error("isEmptyChk 에러", e);
                    e.printStackTrace();
                }

*/

                log.info("while종료");
            }



            } catch (Exception e) {
            log.error("작업 중 오류가 발생했습니다", e);
            e.printStackTrace();

        } finally {
            if (driver != null) {

                log.info("좋아요한 횟수 : " + goodCnt);
                log.info("더보기한 횟수 : " + showMoreCnt);
                log.info("종료시간 : " + LocalDateTime.now());
                driver.quit();
            }
        }

    }



}
