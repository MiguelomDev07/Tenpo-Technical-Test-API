package cl.tenpo.test.components;

import cl.tenpo.test.constants.GeneralConstants;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.github.tomakehurst.wiremock.client.WireMock;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public class MockExternalService {

    private static final Log LOGGER = LogFactory.getLog(MockExternalService.class);

    private static WireMockServer wireMockServer;

    public static void activateMockServer() {
        /**
         * It is raised the Mocked External Service with host:localhost and port:8081
         */
		wireMockServer = new WireMockServer(8081);
		wireMockServer.start();
		WireMock.configureFor("localhost", 8081);

        /**
         * This endpoint simulate the API healthcheck of the Mocked External Service
        */
        stubFor(get(urlEqualTo("/mock-service/api/healthcheck"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{\"message\":\"Mock server is on line!\"}")));

        /**
         * This endpoint simulate the API service to get the percentage value.
         */
        stubFor(get(urlEqualTo("/mock-service/api/get-percentage"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(String.valueOf(GeneralConstants.EXTERNAL_SERVICE_PERCENTAGE))));

		LOGGER.info("Mock server started on port 8081");
	}

    public static void startMockServer() {
        if (wireMockServer != null) {
            wireMockServer.start();
            LOGGER.info("Mock server STARTED");
        }
    }

    public static void stopMockServer() {
        if (wireMockServer != null) {
            wireMockServer.stop();
            LOGGER.info("Mock server STOPPED");
        }
    }

}
