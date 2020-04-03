package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.exceptions.ResourceNotFoundException;
import ba.unsa.etf.si.local_server.requests.MainLoginRequest;
import ba.unsa.etf.si.local_server.responses.MainLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class HttpClientService {
    private final RestTemplate restTemplate;

    @Value("${main_server.base_URI}")
    private String baseURI;

    @Value("${main_server.login_username}")
    private String username;

    @Value("${main_server.login_password}")
    private String password;

    public String makeGetRequest(String relativeURI) {
        ResponseEntity<String> response = makeRequest(relativeURI, HttpMethod.GET, "");
        return response.getBody();
    }

    public String makePostRequest(String relativeURI, String json) {
        ResponseEntity<String> response = makeRequest(relativeURI, HttpMethod.POST, json);
        return response.getBody();
    }

    public ResponseEntity<String> makeRequest(String relativeURI, HttpMethod httpMethod, String json) {
        String uri = baseURI + relativeURI;
        MainLoginRequest mainLoginRequest = new MainLoginRequest(username, password);
        String token = obtainOfficeManagerToken(mainLoginRequest);

        HttpHeaders headers = new HttpHeaders() {{
            String bearerToken = String.format("Bearer %s", token);
            set(AUTHORIZATION, bearerToken);
            set(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        }};

        HttpEntity<String> request = new HttpEntity<>(json, headers);

        return restTemplate.exchange(uri, httpMethod, request, String.class);
    }

    private String obtainOfficeManagerToken(MainLoginRequest request) {
        try {
            String uri = baseURI + "/auth/login";
            return restTemplate.postForObject(uri, request, MainLoginResponse.class).getToken();
        } catch (HttpClientErrorException err) {
            err.printStackTrace();
            throw new ResourceNotFoundException("Could not get login response from the main server");
        }
    }

}
