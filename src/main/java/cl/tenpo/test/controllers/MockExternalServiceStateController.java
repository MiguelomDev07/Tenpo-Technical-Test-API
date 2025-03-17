package cl.tenpo.test.controllers;

import cl.tenpo.test.components.MockExternalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/mock-service")
@Tag(name = "The API Endpoints to manage the Mocked External Server!")
public class MockExternalServiceStateController {

    private boolean isServiceUp = true;

    @GetMapping("/status")
    @Operation(summary = "This endpoint allows to retrieve the status of the mocked external service.")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok(isServiceUp ? "Service is UP" : "Service is DOWN");
    }

    @PostMapping("/start")
    @Operation(summary = "This endpoint allows to start the mocked external service.")
    public ResponseEntity<String> startService() {
        this.isServiceUp = true;
        MockExternalService.startMockServer();
        return ResponseEntity.ok("Mock service STARTED.");
    }

    @PostMapping("/stop")
    @Operation(summary = "This endpoint allows to stop the mocked external service.")
    public ResponseEntity<String> stopService() {
        this.isServiceUp = false;
        MockExternalService.stopMockServer();
        return ResponseEntity.ok("Mock service STOPPED.");
    }

}
