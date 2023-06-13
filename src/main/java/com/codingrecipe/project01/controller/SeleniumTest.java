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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SeleniumTest {
    private static final Logger log = LoggerFactory.getLogger(SeleniumTest.class);


    public static void main(String[] args)  throws NoSuchElementException {

        SeleniumTest selTest = new SeleniumTest();
        selTest.crawl();
    }

    //WebDriver
    private WebDriver driver;

    //Properties
    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static final String WEB_DRIVER_PATH = "chromedriver.exe 경로";

    //크롤링 할 URL
    private String base_url;

    private String targetURL;

    //좋아요 개수체크
    private int goodCnt = 0;

    public SeleniumTest() {
        super();

        //System Property SetUp
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        //Driver SetUp
        //driver = new ChromeDriver();
        base_url = "https://accounts.kakao.com/login/?continue=https://story.kakao.com/#login";

        // 로그인 이후의 URL
        targetURL = "https://story.kakao.com/hashtag/소소한운세";

        // Driver SetUp
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // 창이 표시되지 않도록 설정 (백그라운드에서 실행)
        //options.addArguments("start-maximized");
        driver = new ChromeDriver(options);


    }

    public void crawl()  {

        try {
            //get page (= 브라우저에서 url을 주소창에 넣은 후 request 한 것과 같다)
            driver.get(base_url);

            //새로운 탭열기
            String currentHandle = driver.getWindowHandle();
            ((JavascriptExecutor) driver).executeScript("window.open();");

            Thread.sleep(1000); // 1초 대기

            Set<String> handles = driver.getWindowHandles();
            for (String handle : handles) {
                if (!handle.equals(currentHandle)) {
                    driver.switchTo().window(handle);
                    Thread.sleep(1000); // 1초 대기
                    break;
                }
            }

            //현재탭으로 돌아가기
            driver.switchTo().window(currentHandle);



            //loginKey--1의 value값 아이디입력
            WebElement idInput = driver.findElement(By.id("loginKey--1"));
            idInput.sendKeys("아이디");
            Thread.sleep(1000); // 1초 대기

            //password--2의 value값 비번입력
            WebElement passwordInput = driver.findElement(By.id("password--2"));
            passwordInput.sendKeys("비밀번호");
            Thread.sleep(1000); // 1초 대기

            // 로그인 버튼 클릭
            WebElement loginButton = driver.findElement(By.cssSelector(".btn_g.highlight.submit"));
            loginButton.click();
            log.info("loginButton을 클릭합니다: " + loginButton.getText());
            Thread.sleep(1000); // 1초 대기


            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // 최대 30초 동안 대기


            Actions actions = new Actions(driver);

            // 로그인 후 URL 체크
            if (driver.getCurrentUrl().equals(targetURL)) {
                Thread.sleep(3000); // 1초 대기
                // 이미 목표 URL에 접속한 경우
                // 원하는 작업 수행
                // 예시: 데이터 수집 등
            } else {

                // 목표 URL로 이동
                Thread.sleep(1000); // 1초 대기
                driver.get(targetURL);
                Thread.sleep(5000);


            }



            WebElement coverElement = null;
            boolean coverVisible = false;
            // 원하는 작업 수행
            // 예시: 데이터 수집 등

            long durationInMillis = 50000; // 50초
//            long durationInMillis = 3600000; // 3600초
            long endTime = System.currentTimeMillis() + durationInMillis;

            while (System.currentTimeMillis() < endTime) {
                actions.sendKeys(Keys.PAGE_DOWN).perform();
            }
            // img_item 요소들을 선택
            List<WebElement> imgItems = driver.findElements(By.cssSelector(".img_item"));

            log.info("imgItems : "+imgItems );
            log.info("imgItems.size : "+imgItems.size());

            // data-model 값을 저장할 리스트 생성
            List<String> dataModelValues = new ArrayList<>();

            // imgItems에서 각 요소의 data-model 값을 추출하여 리스트에 저장
            for (WebElement element : imgItems) {
                String dataModel = element.getAttribute("data-model");
                dataModelValues.add(dataModel.replace(".","/"));
            }

            log.info("dataModelValues.size: " + dataModelValues.size());




            work:for(int i=0; i<imgItems.size(); i++){
                WebElement element = imgItems.get(i);


                // 새 탭으로 전환
                ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
                driver.switchTo().window(tabs.get(1));

                driver.get("https://story.kakao.com/"+dataModelValues.get(i));
                log.info("게시글URL : https://story.kakao.com/" +dataModelValues.get(i));

                Thread.sleep(1000); // 1초 대기

                //오류페이지 여부확인
                errorPageCheck: try{
                    //오류페이지일경우 try문을 벗어나 for문 처음으로 돌아간다.
                    if (driver.findElement(By.xpath("//*[@class='ico_error ico_quotation']")).isDisplayed()) {
                        log.info("페이지가 존재하지 않습니다.");
                        continue;
                    }
                }catch(Exception e){
                    //오류페이지일경우 catch문을 벗어나 다음 try문으로 간다
                    break errorPageCheck;
                }



                goodCheck:try {
                    //좋아요 여부확인
                    WebElement bnLike = driver.findElement(By.className("bn_like"));
                    String style = bnLike.getAttribute("style");
                    if(style != null && style.contains("inline")){
                        log.info("이미 좋아요한 게시물입니다.");
                        continue;
                    }else{
                        //좋아요 동작
                        getGood:try{
/*
                            List<WebElement> feelButtons = driver.findElements(By.cssSelector("button[data-kant-id='92']"));

                            // 느낌에 해당하는 버튼들 중에서 랜덤한 인덱스 선택
                            //<button data-kant-id="131~5"><button>
                            int randomIndex = new Random().nextInt(feelButtons.size());
                            log.info("feelButtons.size() : " + feelButtons.size());
                            log.info("feelButtons.get(randomIndex) : " + feelButtons.get(randomIndex));



                            actions.moveToElement( driver.findElement(By.className("_btnLike"))).perform();
                            Thread.sleep(300);
                            actions.moveToElement(driver.findElement(By.cssSelector("button[data-kant-id='13"+randomIndex+"']"))).perform();
                            Thread.sleep(800);
                            actions.moveToElement(driver.findElement(By.cssSelector("button[data-kant-id='13"+randomIndex+"']"))).click();
                            Thread.sleep(500);
                            String emotionButton = driver.findElement(By.cssSelector("button[data-kant-id='13"+randomIndex+"']")).findElement(By.cssSelector("span")).getAttribute("innerHTML");
                            log.info("emotionButton : "+emotionButton);
                            Thread.sleep(1500);
*/


                            //좋아요 버튼만
                            driver.findElement(By.className("_btnLike")).click();
                            Thread.sleep(1500); // 팝업체크를 위한 딜레이, 필수


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
                                    log.info("일시적인 오류입니다[-90009]");
                                    log.info("Selenium을 종료합니다.");
                                    break work;
                                }

                            }catch(Exception e){
                                e.printStackTrace();
                                log.info("[-90009]팝업체크 오류");
                                break work;

                            }

                            ++goodCnt;
                            log.info("btnLike 버튼을 클릭했습니다.");
                            continue;
                        }catch(Exception e){
                            log.info("bnLike 중복체크 후 게시물이 삭제되었을 가능성이 있습니다.");
                            e.printStackTrace();
                            break getGood;

                        }
                    }

                } catch (NoSuchElementException e) {
                    log.info("btnLike를 클릭할 수 없습니다. 게시물이 삭제되었을 가능성이 있습니다.");
                    e.printStackTrace();
                    break goodCheck;

                }


                Thread.sleep(1000); // 1초 대기

            }
        } catch (Exception e) {
            log.error("작업 중 오류가 발생했습니다", e);
            e.printStackTrace();

        } finally {
            if (driver != null) {
                log.info("좋아요한 횟수 : " + goodCnt);
                log.info("종료시간+45분 : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                log.info("종료시간+45분 : " + LocalDateTime.now().plusMinutes(45).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                driver.quit();
            }
        }

    }


}
