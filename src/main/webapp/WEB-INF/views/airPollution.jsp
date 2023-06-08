<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
    <style>
        #tbl {
            border: 2px solid gray;
        }

        #a1 {
            width: 100%;
            text-align: center;
        }

        #a2 {
            width: 100%;
            text-align: center;
        }

        #airGrade {
            width: 100%;
            text-align: center;
            border-top: 1px solid #444444;
            border-collapse: collapse;
        }

        #airLegend {
            text-align: center;
            border-collapse: collapse;
            float: right;
            table-layout: fixed;
        }

        tr, td {
            border-top: 1px solid #444444;
            border-bottom: 1px solid #444444;
            border-left: 1px solid #444444;
            border-right: 1px solid #444444;
            padding: 10px;
        }
    </style>
    <script type="text/javascript"
            src="https://www.gstatic.com/charts/loader.js"></script>
    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
    <script type="text/javascript">
        google.charts.load('current', {
            'packages' : [ 'corechart', 'line' ]
        });
        google.charts.setOnLoadCallback(drawBackgroundColor);

        function drawBackgroundColor() {

            $
                .ajax({
                    url : "getAirPollutionInfo", //요청할 url, 주소:포트(http://localhost:8080)는 일반적으로 생략
                    type : "GET", //요청 방식 - GET:조회, POST:입력
                    cache : false, //캐쉬 - 임시로 데이터를 저장할지 여부, 거의 false
                    dataType : "json", //데이터의 형식, 거의 json
                    data : {
                        stationName : $("#stationName").val()
                    },

                    success : function(data) { //데이터 송,수신에 성공했을 경우의 동작
                        console.log(data);

                        if (data.response.body.totalCount == 0) {
                            $("#pm10").val("");
                            $("#pm25").val("");
                            alert("존재하지 않는 측정소입니다.");
                        } else {
                            //-------------------------------Value Chart ---------------------------------
                            var chartData = new google.visualization.DataTable();
                            chartData.addColumn('string', '경과시간');
                            chartData.addColumn('number', '초미세먼지');
                            chartData.addColumn('number', '미세먼지');
                            for (var i = 12; i >= 0; i--) {
                                chartData
                                    .addRows([
                                        [
                                            data.response.body.items[i].dataTime
                                                .substring(11),
                                            parseInt(data.response.body.items[i].pm25Value),
                                            parseInt(data.response.body.items[i].pm10Value) ],

                                    ]);
                            }
                            var options = {
                                hAxis : {
                                    title : '경과시간',
                                    textStyle : {
                                        fontSize : 15,
                                        fontName : 'Arial'
                                    }
                                },
                                vAxis : {
                                    title : '측정값',
                                    textStyle : {
                                        fontSize : 20,
                                        fontName : 'Arial'
                                    }
                                },
                                colors : [ '#141423', '#a52714' ],
                                backgroundColor : '#f1f8e9'

                            };

                            var chart = new google.visualization.LineChart(
                                document.getElementById('chart_div'));
                            chart.draw(chartData, options);

                            //-------------------------------Grade Chart ---------------------------------

                            var gradeChartData = new google.visualization.DataTable();
                            gradeChartData.addColumn('string', '경과시간');
                            gradeChartData.addColumn('number', '초미세먼지 등급');
                            gradeChartData.addColumn('number', '미세먼지 등급');
                            for (var i = 12; i >= 0; i--) {
                                gradeChartData
                                    .addRows([
                                        [
                                            data.response.body.items[i].dataTime
                                                .substring(11),
                                            parseInt(data.response.body.items[i].pm25Grade),
                                            parseInt(data.response.body.items[i].pm10Grade) ],

                                    ]);
                            }
                            var options = {
                                hAxis : {
                                    title : '경과시간',
                                    textStyle : {
                                        fontSize : 15,
                                        fontName : 'Arial'
                                    }
                                },
                                vAxis : {
                                    title : '등급',
                                    textStyle : {
                                        fontSize : 20,
                                        fontName : 'Arial'
                                    }
                                },
                                colors : [ '#141423', '#a52714' ],
                                backgroundColor : '#f1f8e9'

                            };

                            var gradeChart = new google.visualization.LineChart(
                                document.getElementById('gradeChart_div'));
                            gradeChart.draw(gradeChartData, options);

                        } //else 괄호
                    },
                    error : function(request, status, error) { // 오류가 발생했을 경우의 동작
                        alert("code:" + request.status + "\n" + "message:"
                            + request.responseText + "\n" + "error:"
                            + error);
                    }
                });
        }

        $(document).ready(function() { //시작시 실행함수
            // $("#locNm").val("경기"); 기본값 지정시

            change_Station()

        });

        //제이쿼리방식
        function change_Station() {
            var locNm = $("#locNm").val();
            console.log(locNm);

            var stationName = $("#stationName").val();
            console.log(stationName);

            $.ajax({
                url : "getStationList", //요청할 url, 주소:포트(http://localhost:8080)는 일반적으로 생략
                type : "GET", //요청 방식 - GET:조회, POST:입력
                cache : false, //캐쉬 - 임시로 데이터를 저장할지 여부, 거의 false
                data : {

                    locNm : locNm

                },
                success : function(data) { //데이터 송,수신에 성공했을 경우의 동작
                    console.log(data);
                    $("#stationName").val(data.STTN_NM);

                    var t = ""

                    for (var i = 0; i < data.length; i++) {
                        t += "<option value='"+data[i].STTN_NM+"'>"
                            + data[i].STTN_NM + "</option>"

                    }
                    $("#stationName").html(t);
                    // $("#stationName").val("신원동"); 기본값 지정시
                    fnSearch() //화면 실행할때 바로 실행

                },
                error : function(request, status, error) { // 오류가 발생했을 경우의 동작
                    alert("code:" + request.status + "\n" + "message:"
                        + request.responseText + "\n" + "error:" + error);
                }
            });

        }

        function fnSearch() {

            var locNm = $("#locNm").val();
            console.log(locNm);

            var stationName = $("#stationName").val();
            console.log(stationName);

            $
                .ajax({
                    url : "getAirPollutionInfo", //요청할 url, 주소:포트(http://localhost:8080)는 일반적으로 생략
                    type : "GET", //요청 방식 - GET:조회, POST:입력
                    cache : false, //캐쉬 - 임시로 데이터를 저장할지 여부, 거의 false
                    dataType : "json", //데이터의 형식, 거의 json
                    data : {
                        stationName : $("#stationName").val()
                    },

                    success : function(data) { //데이터 송,수신에 성공했을 경우의 동작
                        console.log(data);

                        if (data.response.body.totalCount == 0) {
                            $("#pm10").val("");
                            $("#pm25").val("");
                            alert("존재하지 않는 측정소입니다.");
                        } else {
                            $("#pm10").val(
                                data.response.body.items[0].pm10Value);
                            $("#pm25").val(
                                data.response.body.items[0].pm25Value);

                            var pm25Grade = data.response.body.items[0].pm25Grade;
                            var pm10Grade = data.response.body.items[0].pm10Grade;
                            var no2Grade = data.response.body.items[0].no2Grade;
                            var coGrade = data.response.body.items[0].coGrade;
                            var o3Grade = data.response.body.items[0].o3Grade;
                            var so2Grade = data.response.body.items[0].so2Grade;

                            console.log(pm25Grade);
                            if (pm25Grade == 1) {
                                pm25Grade = "style='background-color:#0000FF'"
                            } else if (pm25Grade == 2) {
                                pm25Grade = "style='background-color:#00FF00'"
                            } else if (pm25Grade == 3) {
                                pm25Grade = "style='background-color:#FFFF00'"
                            } else if (pm25Grade == 4) {
                                pm25Grade = "style='background-color:#FF0000'"
                            } else if (pm25Grade == null) {
                                pm25Grade = "style='background-color:#FFFFFF'"
                            }

                            console.log(pm10Grade);
                            if (pm10Grade == 1) {
                                pm10Grade = "style='background-color:#0000FF'"
                            } else if (pm10Grade == 2) {
                                pm10Grade = "style='background-color:#00FF00'"
                            } else if (pm10Grade == 3) {
                                pm10Grade = "style='background-color:#FFFF00'"
                            } else if (pm10Grade == 4) {
                                pm10Grade = "style='background-color:#FF0000'"
                            } else if (pm10Grade == null) {
                                pm10Grade = "style='background-color:#FFFFFF'"
                            }

                            console.log(no2Grade);
                            if (no2Grade == 1) {
                                no2Grade = "style='background-color:#0000FF'"
                            } else if (no2Grade == 2) {
                                no2Grade = "style='background-color:#00FF00'"
                            } else if (no2Grade == 3) {
                                no2Grade = "style='background-color:#FFFF00'"
                            } else if (no2Grade == 4) {
                                no2Grade = "style='background-color:#FF0000'"
                            } else if (no2Grade == null) {
                                no2Grade = "style='background-color:#FFFFFF'"
                            }

                            console.log(coGrade);
                            if (coGrade == 1) {
                                coGrade = "style='background-color:#0000FF'"
                            } else if (coGrade == 2) {
                                coGrade = "style='background-color:#00FF00'"
                            } else if (coGrade == 3) {
                                coGrade = "style='background-color:#FFFF00'"
                            } else if (coGrade == 4) {
                                coGrade = "style='background-color:#FF0000'"
                            } else if (coGrade == null) {
                                coGrade = "style='background-color:#FFFFFF'"
                            }

                            console.log(o3Grade);
                            if (o3Grade == 1) {
                                o3Grade = "style='background-color:#0000FF'"
                            } else if (o3Grade == 2) {
                                o3Grade = "style='background-color:#00FF00'"
                            } else if (o3Grade == 3) {
                                o3Grade = "style='background-color:#FFFF00'"
                            } else if (o3Grade == 4) {
                                o3Grade = "style='background-color:#FF0000'"
                            } else if (o3Grade == null) {
                                o3Grade = "style='background-color:#FFFFFF'"
                            }

                            console.log(so2Grade);
                            if (so2Grade == 1) {
                                so2Grade = "style='background-color:#0000FF'"
                            } else if (so2Grade == 2) {
                                so2Grade = "style='background-color:#00FF00'"
                            } else if (so2Grade == 3) {
                                so2Grade = "style='background-color:#FFFF00'"
                            } else if (so2Grade == 4) {
                                so2Grade = "style='background-color:#FF0000'"
                            } else if (so2Grade == null) {
                                so2Grade = "style='background-color:#FFFFFF'"
                            }

                            var t = ""

                            t += "<tr class='a'>"
                            t += "<td>항목</td>"
                            t += "<td>등급</td>"
                            t += "<td>측정값</td>"
                            t += "<td>항목</td>"
                            t += "<td>등급</td>"
                            t += "<td>측정값</td>"
                            t += "</tr>"

                            t += "<tr class='b'>"
                            t += "<td>초미세먼지(PM2.5)</td>"
                            t += "<td " + pm25Grade + "></td>"
                            t += "<td>" + data.response.body.items[0].pm25Value
                                + "㎍/㎥(1h)</td>"
                            t += "<td>미세먼지(PM10)</td>"
                            t += "<td "+ pm10Grade+ "></td>"
                            t += "<td>" + data.response.body.items[0].pm10Value
                                + "㎍/㎥(1h)</td>"
                            t += "</tr>"

                            t += "<tr class='b'>"
                            t += "<td>이산화질소</td>"
                            t += "<td " +no2Grade+"></td>"
                            t += "<td>" + data.response.body.items[0].no2Value
                                + "ppm</td>"
                            t += "<td>일산화탄소</td>"
                            t += "<td "+coGrade+"></td>"
                            t += "<td>" + data.response.body.items[0].coValue
                                + "ppm</td>"
                            t += "</tr>"

                            t += "<tr class='b'>"
                            t += "<td>오존</td>"
                            t += "<td " + o3Grade+"></td>"
                            t += "<td>" + data.response.body.items[0].o3Value
                                + "ppm</td>"
                            t += "<td>이황산가스</td>"
                            t += "<td "+so2Grade+"></td>"
                            t += "<td>" + data.response.body.items[0].so2Value
                                + "ppm</td>"
                            t += "</tr>"
                            $("#airGrade").html(t);

                            var l = ""
                            l += "<tr>"
                            l += "<td style='table-layout: fixed;flex:1'>측정불가</td>"
                            l += "<td style='table-layout: fixed;flex:1'>좋음(5~50)</td>"
                            l += "<td style='table-layout: fixed;flex:1'>보통(51~100)</td>"
                            l += "<td style='table-layout: fixed;flex:1'>나쁨(101~250)</td>"
                            l += "<td style='table-layout: fixed;flex:1'>매우나쁨(251~)</td>"
                            l += "</tr>"
                            l += "<td style='background-color:#FFFFFF;flex:1'></td>"
                            l += "<td style='background-color:#0000FF;flex:1'></td>"
                            l += "<td style='background-color:#00FF00;flex:1'></td>"
                            l += "<td style='background-color:#FFFF00;flex:1'></td>"
                            l += "<td style='background-color:#FF0000;flex:1'></td>"
                            l += "<tr>"
                            l += "</tr>"
                            $("#airLegend").html(l);

                            drawBackgroundColor(); // 차트그리기
                        } //else 괄호
                    },
                    error : function(request, status, error) { // 오류가 발생했을 경우의 동작
                        alert("code:" + request.status + "\n" + "message:"
                            + request.responseText + "\n" + "error:"
                            + error);
                    }
                });

        }
    </script>
</head>
<body>
<div id="a1">
    <select id="locNm" onchange="change_Station()">

        <c:forEach var="locationName" items="${locationList}">
            <option value="${locationName.LOC_NM}">${locationName.LOC_NM}</option>

        </c:forEach>

    </select> <select id="stationName" onchange="fnSearch()">
</select>
</div>

<div id="a2">
    <br> 미세먼지 : <input type="text" id="pm10"> 초미세먼지 : <input
        type="text" id="pm25">
</div>
<br>
<div style="float: right">
    <table id="airLegend" style="float: left">

    </table>

</div>

<br>
<div id="a3">
    <table id="airGrade">

    </table>
</div>


<br>
<table style="width:100%">
    <tr>
        <td id="chart_div" style="width: 50%; height: 300px;"></td>
        <td id="gradeChart_div" style="width: 50%; height: 300px;"></td>
    </tr>
</table>





</body>
</html>