package com.loopers.interfaces.api;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.loopers.application.user.UserCommand.UserCreateCommand;
import com.loopers.domain.user.Gender;
import com.loopers.domain.user.UserModel;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.interfaces.api.ApiResponse.Metadata.Result;
import com.loopers.interfaces.api.user.UserDto;
import com.loopers.interfaces.api.user.UserDto.UserCreateRequest;
import com.loopers.interfaces.api.user.UserDto.UserResponse;
import com.loopers.utils.DatabaseCleanUp;
import java.time.LocalDate;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserApiE2ETest {

    public static final String END_POINT = "/api/v1/users";

    private final TestRestTemplate testRestTemplate;
    private final UserJpaRepository userJpaRepository;
    private final DatabaseCleanUp databaseCleanUp;

    @Autowired
    public UserApiE2ETest(
            TestRestTemplate testRestTemplate,
            UserJpaRepository userJpaRepository,
            DatabaseCleanUp databaseCleanUp
    ) {
        this.testRestTemplate = testRestTemplate;
        this.userJpaRepository = userJpaRepository;
        this.databaseCleanUp = databaseCleanUp;
    }

    @AfterEach
    void cleanUp() {
        this.databaseCleanUp.truncateAllTables();
    }

    @DisplayName("POST /api/v1/users")
    @Nested
    class Register {
        @DisplayName("회원가입 성공 시, 유저 정보를 반환한다.")
        @Test
        void returnUserInfo_whenUserCreated() {
            // given
            UserDto.UserCreateRequest request = new UserCreateRequest(
                    "test", "테스터", "test@test.com", "MALE", "1998-01-08"
            );
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");

            HttpEntity<UserDto.UserCreateRequest> requestEntity = new HttpEntity<>(request, headers);

            ParameterizedTypeReference<ApiResponse<UserDto.UserResponse>> responseType = new ParameterizedTypeReference<>() {
            };

            // when
            ResponseEntity<ApiResponse<UserDto.UserResponse>> response = testRestTemplate.exchange(
                    END_POINT, HttpMethod.POST, requestEntity, responseType
            );

            UserResponse responseBodyData = response.getBody().data();

            // then
            assertAll(
                    () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                    () -> assertThat(response.getBody().meta().result()).isEqualTo(Result.SUCCESS),
                    () -> assertThat(responseBodyData.id()).isEqualTo(1L),
                    () -> assertThat(responseBodyData.userId()).isEqualTo("test"),
                    () -> assertThat(responseBodyData.email()).isEqualTo("test@test.com")
            );
        }

        @DisplayName("성별이 없으면 400 에러를 반환한다.")
        @Test
        void return400_whenGenderIsNull() {
            // given
            UserDto.UserCreateRequest request = new UserCreateRequest(
                    "test", "테스터", "test@test.com", null, "1998-01-08"
            );
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");

            HttpEntity<UserDto.UserCreateRequest> requestEntity = new HttpEntity<>(request, headers);

            ParameterizedTypeReference<?> responseType = new ParameterizedTypeReference<>() {
            };

            // when
            ResponseEntity<?> response = testRestTemplate.exchange(
                    END_POINT, HttpMethod.POST, requestEntity, responseType
            );

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

    }

    @DisplayName("GET /api/v1/users/me")
    @Nested
    class Get {
        @DisplayName("내 정보 조회에 성공 할 경우, 해당하는 유저 정보를 응답으로 반환한다.")
        @Test
        void returnUserInfo_whenUserFound() {
            // given
            UserCreateCommand command = new UserCreateCommand(
                    "test", "테스터", "test@test.com", Gender.MALE, LocalDate.of(1998, 1, 8)
            );
            UserModel savedUser = userJpaRepository.save(UserModel.create(command));
            String requestUrl = END_POINT + "/me";

            ParameterizedTypeReference<ApiResponse<UserDto.UserResponse>>
                    responseType = new ParameterizedTypeReference<>() {
            };

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-USER-ID", "test");

            HttpEntity<UserDto.UserCreateRequest> requestEntity = new HttpEntity<>(null, headers);

            // when
            ResponseEntity<ApiResponse<UserDto.UserResponse>> response = testRestTemplate.exchange(
                    requestUrl, HttpMethod.GET, requestEntity, responseType
            );
            UserResponse responseBodyData = response.getBody().data();

            // then
            assertAll(
                    () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                    () -> assertThat(responseBodyData.userId()).isEqualTo(savedUser.getUserId()),
                    () -> assertThat(responseBodyData.username()).isEqualTo(savedUser.getUsername())
            );
        }

        @DisplayName("존재하지 않는 ID로 조회 할 경우, 404 응답을 반환한다.")
        @Test
        void return404_whenUserNotFound() {
            // given
            String requestUrl = END_POINT + "/me";

            ParameterizedTypeReference<?> responseType = new ParameterizedTypeReference<>() {
            };

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-USER-ID", "non-exists-user-id");

            HttpEntity<UserDto.UserCreateRequest> requestEntity = new HttpEntity<>(null, headers);

            // when
            ResponseEntity<?> response = testRestTemplate.exchange(
                    requestUrl, HttpMethod.GET, requestEntity, responseType
            );

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

}
