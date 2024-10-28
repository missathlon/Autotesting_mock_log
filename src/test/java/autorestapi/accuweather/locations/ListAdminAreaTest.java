package autorestapi.accuweather.locations;

import autorestapi.accuweather.AbstractTest;
import autorestapi.accuweather.locations.list.AdminArea;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListAdminAreaTest extends AbstractTest {

    private static final Logger logger
            = LoggerFactory.getLogger(ListAdminAreaTest.class);


    @Test
    void getListAdminArea_shouldReturn200() throws IOException {
        logger.info("Тест код ответ 200 запущен");
        ObjectMapper mapper = new ObjectMapper();
        AdminArea city = new AdminArea();
        city.setLocalizedName("Brest");

        logger.debug("Формируем мок GET /locations/v1/adminareas/by");
        stubFor(get(urlPathEqualTo("/locations/v1/adminareas/by"))
                .willReturn(aResponse().withStatus(200)
                        .withBody(mapper.writeValueAsString(city))));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        logger.debug("http-клиент создан");

        HttpGet request = new HttpGet(getBaseUrl() + "/locations/v1/adminareas/by");

        HttpResponse response = httpClient.execute(request);

        verify(getRequestedFor(urlPathEqualTo("/locations/v1/adminareas/by")));
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("Brest", mapper.readValue(response
                .getEntity().getContent(), AdminArea.class).getLocalizedName());
    }


    @Test
    void getListAdminArea_shouldReturn401() throws IOException, URISyntaxException {
        logger.info("Тест код ответ 401 запущен");
        //given
        logger.debug("Формируем мок GET /locations/v1/adminareas/by");
        stubFor(get(urlPathEqualTo("/locations/v1/adminareas/by"))
                .withQueryParam("apiKey", containing("vU3yjDZtkiO8GCrGgfI6AApccY4AiMfn"))
                .willReturn(aResponse()
                        .withStatus(401).withBody("Unauthorized")));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(getBaseUrl()+"/locations/v1/adminareas/by");
        URI uri = new URIBuilder(request.getURI())
                .addParameter("apiKey", "P_vU3yjDZtkiO8GCrGgfI6AApccY4AiMfn")
                .build();
        request.setURI(uri);
        logger.debug("http клиент создан");
        //when
        HttpResponse response = httpClient.execute(request);
        //then
        verify(getRequestedFor(urlPathEqualTo("/locations/v1/adminareas/by")));
        assertEquals(401, response.getStatusLine().getStatusCode());
        assertEquals("Unauthorized", convertResponseToString(response));
    }
}


