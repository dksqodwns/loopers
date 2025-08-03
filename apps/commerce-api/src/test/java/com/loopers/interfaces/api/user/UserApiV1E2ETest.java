package com.loopers.interfaces.api.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.loopers.domain.user.User;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.ApiResponse.Metadata.Result;
import com.loopers.interfaces.api.user.UserDto.V1.RegisterRequest;
import com.loopers.interfaces.api.user.UserDto.V1.UserResponse;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
public class UserApiV1E2ETest {

    public static final String END_POINT = "/api/v1/users";

    private final TestRestTemplate testRestTemplate;
    private final DatabaseCleanUp databaseCleanUp;

    @Autowired
    public UserApiV1E2ETest(TestRestTemplate testRestTemplate, DatabaseCleanUp databaseCleanUp) {
        this.testRestTemplate = testRestTemplate;
        this.databaseCleanUp = databaseCleanUp;
    }

    @AfterEach
    public void cleanUp() {
        this.databaseCleanUp.truncateAllTables();
    }

    @DisplayName("유저가 회원가입을 할 때,")
    @Nested
    class Register {

        @DisplayName("성공 할 경우, 생성된 유저 정보를 응답으로 반환한다.")
        @Test
        void returnUserResponse_whenSuccessRegister() {
            // given
            RegisterRequest request = new RegisterRequest("test", "test@test.com", "안병준", "1998-01-08", "MALE");
            HttpEntity<RegisterRequest> requestEntity = new HttpEntity<>(request);
            ParameterizedTypeReference<ApiResponse<UserResponse>> responseType = new ParameterizedTypeReference<>() {
            };

            // when
            ResponseEntity<ApiResponse<UserResponse>> response = testRestTemplate.exchange(
                    END_POINT, HttpMethod.POST, requestEntity, responseType
            );

            // then
            Assertions.assertAll(
                    () -> assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(),
                    () -> assertThat(response.getBody().meta().result()).isEqualTo(Result.SUCCESS),
                    () -> assertThat(response.getBody().data().id()).isEqualTo(1L)
            );
        }

        void returnUserResponse_whenOwnInfo() {
            // given
            final User user = new User("test", "test@test.com", "안병준", "1998-01-08", "MALE");
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", user.getId().toString());
            HttpEntity<Object> requestEntity = new HttpEntity<>(null, headers);

            // when
            ParameterizedTypeReference<ApiResponse<UserResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<UserResponse>> response = testRestTemplate.exchange(
                    END_POINT, HttpMethod.GET, requestEntity, responseType
            );

            // then
            Assertions.assertAll(
                    () -> assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(),
                    () -> assertThat(response.getBody().meta().result()).isEqualTo(Result.SUCCESS),
                    () -> assertThat(response.getBody().data().id()).isEqualTo(1L)
            );

        }
    }
}
