package com.loopers.interfaces.api.point;

import com.loopers.domain.user.BirthDate;
import com.loopers.domain.user.Email;
import com.loopers.domain.user.Gender;
import com.loopers.domain.user.LoginId;
import com.loopers.domain.user.User;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PointApiV1E2ETest {
    public final String END_POINT = "/api/v1/points";

    private TestRestTemplate testRestTemplate;
    private UserJpaRepository userJpaRepository;
    private DatabaseCleanUp databaseCleanUp;


    @Autowired
    public PointApiV1E2ETest(TestRestTemplate testRestTemplate, DatabaseCleanUp databaseCleanUp, UserJpaRepository userJpaRepository) {
        this.testRestTemplate = testRestTemplate;
        this.databaseCleanUp = databaseCleanUp;
        this.userJpaRepository = userJpaRepository;
    }

    @AfterEach
    public void cleanUp() {
        this.databaseCleanUp.truncateAllTables();
    }

    @DisplayName("포인트를 충전 할 때,")
    @Nested
    class Charge {
        @DisplayName("성공 할 경우, 정상적으로 충전된다.")
        @Test
        void returnSuccess_whenChargeSuccessful() {
            User user = userJpaRepository.save(new User(new LoginId("test"), new Email("test@test.com"), "안병준", new BirthDate("1998-01-08"), Gender.from("MALE")));
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", user.getId().toString());
            PointDto.V1.ChargeRequest request = new PointDto.V1.ChargeRequest(100L);

            HttpEntity<PointDto.V1.ChargeRequest> requestEntity = new HttpEntity<>(request, headers);

            ParameterizedTypeReference<ApiResponse<PointDto.V1.PointResponse>> responseType = new ParameterizedTypeReference<>() {
            };

            ResponseEntity<ApiResponse<PointDto.V1.PointResponse>> response = testRestTemplate.exchange(END_POINT + "/charge", HttpMethod.POST, requestEntity, responseType);

        }
    }

    @DisplayName("포인트를 조회 할 때,")
    @Nested
    class Get {
        @DisplayName("성공 할 경우, 포인트 정보가 반환된다.")
        @Test
        void returnPointInfo_whenGetPointSuccessful() {
            User user = userJpaRepository.save(new User(new LoginId("test"), new Email("test@test.com"), "안병준", new BirthDate("1998-01-08"), Gender.from("MALE")));
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", user.getId().toString());
            HttpEntity<Object> requestEntity = new HttpEntity<>(null, headers);

            ParameterizedTypeReference<ApiResponse<PointDto.V1.PointResponse>> responseType = new ParameterizedTypeReference<>() {
            };

            ResponseEntity<ApiResponse<PointDto.V1.PointResponse>> response = testRestTemplate.exchange(END_POINT + "/charge", HttpMethod.POST, requestEntity, responseType);
        }
    }
}
