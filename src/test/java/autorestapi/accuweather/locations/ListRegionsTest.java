package autorestapi.accuweather.locations;

import autorestapi.accuweather.AbstractTest;
import autorestapi.accuweather.locations.list.Regions;
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

public class ListRegionsTest extends AbstractTest {

    private static final Logger logger
            = LoggerFactory.getLogger(ListRegionsTest.class);


    @Test
    void getListRegions_shouldReturn200() throws IOException {
        logger.info("Тест код ответ 200 запущен");
        ObjectMapper mapper = new ObjectMapper();
        Regions country = new Regions();
        country.setLocalizedName("Oceania");

        logger.debug("Формируем мок GET /locations/v1/regions");
        stubFor(get(urlPathEqualTo("/locations/v1/regions"))
                .willReturn(aResponse().withStatus(200)
                        .withBody(mapper.writeValueAsString(country))));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        logger.debug("http-клиент создан");

        HttpGet request = new HttpGet(getBaseUrl() + "/locations/v1/regions");

        HttpResponse response = httpClient.execute(request);

        verify(getRequestedFor(urlPathEqualTo("/locations/v1/regions")));
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("Oceania", mapper.readValue(response
                .getEntity().getContent(), Regions.class).getLocalizedName());
    }


    @Test
    void getListRegions_shouldReturn401() throws IOException, URISyntaxException {
        logger.info("Тест код ответ 401 запущен");
        //given
        logger.debug("Формируем мок GET /locations/v1/regions");
        stubFor(get(urlPathEqualTo("/locations/v1/regions"))
                .withQueryParam("apiKey", containing("vU3yjDZtkiO8GCrGgfI6AApccY4AiMfn"))
                .willReturn(aResponse()
                        .withStatus(401).withBody("Unauthorized")));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(getBaseUrl()+"/locations/v1/regions");
        URI uri = new URIBuilder(request.getURI())
                .addParameter("apiKey", "P_vU3yjDZtkiO8GCrGgfI6AApccY4AiMfn")
                .build();
        request.setURI(uri);
        logger.debug("http клиент создан");
        //when
        HttpResponse response = httpClient.execute(request);
        //then
        verify(getRequestedFor(urlPathEqualTo("/locations/v1/regions")));
        assertEquals(401, response.getStatusLine().getStatusCode());
        assertEquals("Unauthorized", convertResponseToString(response));
    }
}