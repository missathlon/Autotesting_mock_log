package autorestapi.accuweather.alarm;

import autorestapi.accuweather.AbstractAccuweatherTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class FifteenDaysOfWeatherAlarmsTest extends AbstractAccuweatherTest {


    @Test
    void getFifteenDaysOfWeatherAlarms() {

       String responseBody = given()
                .when()
                .get(getBaseUrl()+"/alarms/v1/15day/316938" +
                        "?apikey=" +getApiKey())
                .then()
                .statusCode(401)
                .extract()
                .body().asString();

        Assertions.assertTrue(responseBody.contains("Api Authorization failed"),
                "Contains:Api Authorization failed");
    }
}
