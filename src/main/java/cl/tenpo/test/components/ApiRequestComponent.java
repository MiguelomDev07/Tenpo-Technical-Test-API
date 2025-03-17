package cl.tenpo.test.components;

public interface ApiRequestComponent {

    /**
     * This method is used to perform a get request
     * @param classToMappingResponse
     * @param apiUrl
     * @return
     */
    <T> T get(Class<T> classToMappingResponse, String apiUrl);
}
