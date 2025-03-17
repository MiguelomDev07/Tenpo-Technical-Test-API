package cl.tenpo.test.components.impl;

import cl.tenpo.test.config.WebClientBaseConfig;
import cl.tenpo.test.constants.ApiEndpointConstants;
import cl.tenpo.test.constants.GeneralConstants;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;

@ExtendWith(MockitoExtension.class)
class ApiRequestComponentImplTest {

    @InjectMocks
    private ApiRequestComponentImpl apiRequestComponentImpl;

    @Mock
    private static WireMockServer wireMockServer;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(apiRequestComponentImpl, "webClientBaseConfig", new WebClientBaseConfig());

        wireMockServer = new WireMockServer(8081);
        wireMockServer.start();
        WireMock.configureFor("localhost", 8081);

        /**
         * This endpoint simulate the API service to get the percentage value.
         */
        stubFor(get(urlEqualTo("/mock-service/api/get-percentage"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(String.valueOf(GeneralConstants.EXTERNAL_SERVICE_PERCENTAGE))));
    }

    @Test
    void test_get_should_return_responseCorrectly_with_validInputs_and_externalService_isOkey() {
        /** It is called to main method */
        String result = apiRequestComponentImpl.get(
                String.class,
                "http://localhost:8081/mock-service/api".concat(ApiEndpointConstants.GET_PERCENTAGE_VALUE_EXTERNAL_SERVICE_URI)
        );

        /** It is validated the output from the main method in accordance with the inputs */
        assertNotNull(result);
        assertEquals(String.valueOf(GeneralConstants.EXTERNAL_SERVICE_PERCENTAGE), result);
    }
}