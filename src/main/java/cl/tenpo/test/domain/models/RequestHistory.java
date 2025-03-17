package cl.tenpo.test.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.AUTO;

@Entity
@Table(name = "requesthistory")
public class RequestHistory {

    @Id
    @GeneratedValue(strategy = AUTO)
    @JsonIgnore
    private Long id;

    @Column(nullable = false, length = 50)
    private String endpoint;

    @Column(nullable = false, length = 50)
    private String parameters;

    @Column(nullable = false, length = 100)
    private String response;

    @CreatedDate
    @Column(name = "creationDate", columnDefinition = "TIMESTAMP", updatable = false, nullable = false)
    protected LocalDate creationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
}
