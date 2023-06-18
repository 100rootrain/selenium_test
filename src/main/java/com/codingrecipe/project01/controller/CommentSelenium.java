package com.codingrecipe.project01.controller;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class CommentSelenium {
    private static final Logger logger = LoggerFactory.getLogger(CommentSelenium.class);

    public static void main(String[] args) throws NoSuchElementException{
        CommentSelenium commentSelenium = new CommentSelenium();
        commentSelenium.crawl();
    }

    private WebDriver driver;

    //Properties
    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static final String WEB_DRIVER_PATH = "C:\\Users\\qoreh\\git\\KeunWooRepository\\m\\src\\main\\webapp\\resources\\selenium\\chromedriver_win32/chromedriver.exe";

    //크롤링 할 URL
    private String base_url;
    private String currentPage;

    //좋아요 개수체크
    private int goodCnt = 0;
    //더보기 버튼 개수체크
    private int showBtnCnt = 0;

    public CommentSelenium() {
        super();

        //System Property SetUp
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        //Driver SetUp
        //driver = new ChromeDriver();
        base_url = "https://accounts.kakao.com/login/?continue=https%3A%2F%2Fstory.kakao.com%2Fch%2F"+"thinker"+"#login";
        //saal
        //funnybro
        //type4graphic
        //thinker

        // Driver SetUp
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // 창이 표시되지 않도록 설정 (백그라운드에서 실행)
        //options.addArguments("start-maximized");
        driver = new ChromeDriver(options);


    }

    public void crawl()  {
        logger.info("시작시간 : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
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
            Thread.sleep(1000); // 1초 대기

            // 로그인 버튼 클릭
            WebElement loginButton = driver.findElement(By.cssSelector(".btn_g.highlight.submit"));
            loginButton.click();
            logger.info("loginButton을 클릭합니다: " + loginButton.getText());
            Thread.sleep(1000); // 1초 대기


            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // 최대 30초 동안 대기


            Actions actions = new Actions(driver);

            driver.get(driver.getCurrentUrl()+"/feed");


            // 원하는 작업 수행
            // 예시: 데이터 수집 등

            long durationInMillis = 100000; // 100초
//            long durationInMillis = 3600000; // 3600초
            long endTime = System.currentTimeMillis() + durationInMillis;

            while (System.currentTimeMillis() < endTime) {
                actions.sendKeys(Keys.END).perform();
            }
            actions.sendKeys(Keys.HOME).perform();
            actions.sendKeys(Keys.PAGE_DOWN).perform();

            String pageList = "//div[@data-part-name='pageList']";
            String saList = "//div[@class='section _activity']";

            WebElement homePage = driver.findElement(By.xpath(pageList));
            logger.info("homePage"+homePage);
            List<WebElement> sectionActivities = driver.findElements(By.xpath((saList)));


            // section _activity 요소들을 선택
            logger.info("sectionActivities.size : "+sectionActivities.size());

            // data-model 값을 저장할 리스트 생성
            List<String> dataModelValues = new ArrayList<>();

            // imgItems에서 각 요소의 data-model 값을 추출하여 리스트에 저장
            for (WebElement element : sectionActivities) {
                String dataModel = element.getAttribute("data-model");
                dataModelValues.add(dataModel.replace(".","/"));
            }

            logger.info("dataModelValues.size: " + dataModelValues.size());

            //더보기버튼
            String showButton = "//a[@class='_btnShowMoreComment']";

            work:for(int i=0; i<sectionActivities.size(); i++){
                WebElement element = sectionActivities.get(i);

                driver.get("https://story.kakao.com/"+dataModelValues.get(i));
                logger.info("게시글URL : https://story.kakao.com/" +dataModelValues.get(i));


                commentLike:while(true){
                    Thread.sleep(1500);
                    logger.info("while시작");

                    // 좋아요버튼
                    String likeCommentButtons = "//a[@class='btn_like _btnLikeComment' and text()='좋아요']";

                    // 더보기버튼
                    List<WebElement> showMoreButtons = driver.findElements(By.xpath("//a[@class='_btnShowMoreComment']"));
                    logger.info("더보기버튼 갯수 :" +showMoreButtons.size());

                    Thread.sleep(1000);

                    //[-90009]팝업체크(이오류가 걸릴시 finally로 간다.)
                    finalErrorCheck:try{
                        //팝업체크

                        List<WebElement> error90009 = driver.findElements(By.xpath("//div[@id='kakaoWrap']"));
                        String error90009Style = error90009.get(0).getCssValue("overflow");

                        if (error90009Style.equals("visible")) {
                            //log.info("정상페이지입니다");
                            break finalErrorCheck;

                        } else if (error90009Style.equals("hidden")){
//                                }else if(wait.until(ExpectedConditions.textToBePresentInElementValue(error90009.get(0), "hidden"))){
                            logger.info("일시적인 오류입니다[-90009]");
                            logger.info("Selenium을 종료합니다.");
                            break work;
                        }

                    }catch(Exception e){
                        e.printStackTrace();
                        logger.info("[-90009]팝업체크 오류");
                        break work;

                    }



                    //더보기버튼갯수가 0이라면 break , 스크롤 업후 좋아요누르기
                    showElementClick:try{
                      if(driver.findElement(By.xpath(showButton)).isDisplayed()){
                          WebElement showBtn = driver.findElement(By.xpath(showButton));

                          actions.moveToElement(showBtn).perform();
                          Thread.sleep(1500);
                          showBtn.sendKeys(Keys.ENTER);
                          showBtnCnt++;
                          logger.info("현재까지 더보기 클릭횟수 : "+showBtnCnt+"개");

                          break showElementClick;
                      }



                    }catch(Exception e){
                        logger.error("showElementClick 에러", e);
                        e.printStackTrace();
                        break commentLike;

                    }

                    likeBtnClick:try{
                        if(!driver.findElements(By.xpath(likeCommentButtons)).isEmpty()) {
                            List<WebElement> likeCommentBtns = driver.findElements(By.xpath(likeCommentButtons));
                            logger.info("likeCommentBtns 총갯수 : " + likeCommentBtns.size());
                            for(WebElement likeCommentBtn : likeCommentBtns){
                                actions.moveToElement(likeCommentBtn).perform();
                                Thread.sleep(2000);
                                likeCommentBtn.sendKeys(Keys.ENTER,Keys.TAB);
                                Thread.sleep(2000);
                                logger.info((likeCommentBtns.indexOf(likeCommentBtn) + 1) + "번째 좋아요버튼 클릭");
                                logger.info("현재까지 좋아요 누른 갯수 : "+goodCnt+"개");
                                goodCnt++;
                            }

                        }else{
                            if(driver.findElement(By.xpath(showButton)).isDisplayed()){
                                continue commentLike;
                            }

                            continue work;
                        }

                    }catch(Exception e){
                        logger.error("likeBtnClick 에러", e);
                        e.printStackTrace();
                    }

                    logger.info("while종료");
                }




            }
        } catch (Exception e) {
            logger.error("작업 중 오류가 발생했습니다", e);
            e.printStackTrace();

        } finally {
            if (driver != null) {
                logger.info("좋아요한 횟수 : " + goodCnt);
                logger.info("종료시간 : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                logger.info("종료시간+45분 : " + LocalDateTime.now().plusMinutes(45).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                driver.quit();
            }
        }

    }


}
