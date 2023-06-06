package com.codingrecipe.project01.controller;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GhostSoulSelenium {
    private static final Logger log = LoggerFactory.getLogger(SeleniumTest.class);

    public static void main(String[] args) {
        GhostSoulSelenium selTest = new GhostSoulSelenium();
        selTest.crawl();
    }

    //WebDriver
    private WebDriver driver;

    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static final String WEB_DRIVER_PATH = "C:\\Users\\backsung\\selenium_test\\src\\main\\resources\\selenium\\chromedriver_win32/chromedriver.exe";
    //크롤링 할 URL
    private String base_url;

    private String targetURL;

    private int rankChkInt = 0;
    private int 천랑무사;
    private int 패도무사;
    private int 유협;
    private int 흑영;
    private int 현자;
    private int 현인;
    private int 초월역사;
    private int 파천역사;
    private int 천진격사;
    private int 적인표사;
    private int 수라장군;
    private int 야차장군;
    private int 주천마령;
    private int 지천마령;
    private int 원천마령;
    private int 혈천마령;
    private int 살천마령;



    public GhostSoulSelenium() {
        super();

        //System Property SetUp
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        //Driver SetUp
        //driver = new ChromeDriver();
        base_url = "https://hon.mgame.com/ranking/?rtype=R";

        // Driver SetUp
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // 창이 표시되지 않도록 설정 (백그라운드에서 실행)
        //options.addArguments("start-maximized");
        driver = new ChromeDriver(options);


    }

           public void crawl() {

            //get page
            driver.get(base_url);

            try{




                while (true) {
                    // 테이블 요소 탐색
                    WebElement tableElement = driver.findElement(By.xpath("//table[@class='commonTable02']"));
                    WebElement tbodyElement = tableElement.findElement(By.xpath(".//tbody"));
                    List<WebElement> rows = tbodyElement.findElements(By.xpath(".//tr"));
                    log.info("tableElement : " + tableElement);
                    log.info("tbodyElement : " + tbodyElement);
                    log.info("rows : " + rows);
                    log.info("rows.size(): " + rows.size());

                    // 데이터 추출
                    for (int i = 1; i < rows.size(); i++) {
                        WebElement row = rows.get(i);
                        List<WebElement> cells = row.findElements(By.xpath(".//td"));

                        // 캐릭터명, 직업, 문파 데이터 추출
                        String rankInt = cells.get(0).getText();
                        String characterName = cells.get(2).getText();
                        String job = cells.get(3).getText();
                        String guild = cells.get(4).getText();
                        if (guild.equals("-")) {
                            guild = "없음";
                        }

                        switch (job) {
                            case "천랑무사":
                                천랑무사++;
                                break;
                            case "패도무사":
                                패도무사++;
                                break;
                            case "유협":
                                유협++;
                                break;
                            case "흑영":
                                흑영++;
                                break;
                            case "현자":
                                현자++;
                                break;
                            case "현인":
                                현인++;
                                break;
                            case "초월역사":
                                초월역사++;
                                break;
                            case "파천역사":
                                파천역사++;
                                break;
                            case "천진격사":
                                천진격사++;
                                break;
                            case "적인표사":
                                적인표사++;
                                break;
                            case "수라장군":
                                수라장군++;
                                break;
                            case "야차장군":
                                야차장군++;
                                break;
                            case "주천마령":
                                주천마령++;
                                break;
                            case "지천마령":
                                지천마령++;
                                break;
                            case "원천마령":
                                원천마령++;
                                break;
                            case "혈천마령":
                                혈천마령++;
                                break;
                            case"살천마령":
                                살천마령++;
                                break;
                            default:
                                // 처리할 작업이 없는 경우
                                break;
                        }

                        // 추출한 정보 출력
                        log.info("서열: " + rankInt);
                        log.info("캐릭터명: " + characterName);
                        log.info("직업: " + job);
                        log.info("문파: " + guild);
                        rankChkInt++;


                    }
                    log.info("rankChkInt : " + rankChkInt);

                    // 클릭할 페이지 번호 확인
                    WebElement nextPageLink = driver.findElement(By.xpath("//a[@class='thisPage']/following-sibling::a[1]"));
                    if (nextPageLink == null) {
                        break;  // 다음 페이지가 없으면 반복문 종료
                    }

                    // 다음 페이지 클릭
                    nextPageLink.click();

                    // 페이지 이동 후 로딩 대기 (적절한 대기 시간을 설정해야 합니다.)
                    Thread.sleep(1000);  // 1초 대기 (필요에 따라 조정 가능)
                }



            }catch(Exception e){
                log.error("작업 중 오류가 발생하였습니다.", e);
                e.printStackTrace();
            } finally {
                if(driver != null){
                    log.info("천랑무사 : "+ 천랑무사);
                    log.info("패도무사 : "+ 패도무사);

                    log.info("유협 : "+ 유협);
                    log.info("흑영 : "+ 흑영);

                    log.info("현자 : "+ 현자);
                    log.info("현인 : "+ 현인);

                    log.info("초월역사 : "+ 초월역사);
                    log.info("파천역사 : "+ 파천역사);

                    log.info("천진격사 : "+ 천진격사);
                    log.info("적인표사 : "+ 적인표사);

                    log.info("수라장군 : "+ 수라장군);
                    log.info("야차장군 : "+ 야차장군);

                    log.info("주천마령 : "+ 주천마령);
                    log.info("원천마령 : "+ 원천마령);
                    log.info("지천마령 : "+ 지천마령);
                    log.info("혈천마령 : "+ 혈천마령);
                    log.info("살천마령 : "+ 살천마령);

                    int totalMembers1 = 천랑무사+유협+현자+초월역사+천진격사+수라장군;
                    int totalMembers2 = 패도무사+흑영+현인+파천역사+적인표사+야차장군;
                    int totalMembers3 = 주천마령+지천마령+혈천마령+원천마령+살천마령;
                    log.info("정파인원 : " + totalMembers1 + "명");
                    log.info("사파인원 : "+ totalMembers2 + "명");
                    log.info("마교인원 : "+ totalMembers3 + "명");


                    log.info("셀레니움을 종료합니다.");
                    driver.quit();
                }
            }


        }

}


