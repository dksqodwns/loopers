package com.loopers.interfaces.api;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.loopers.application.user.UserCommand.UserCreateCommand;
import com.loopers.domain.user.Gender;
import com.loopers.domain.user.UserModel;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.interfaces.api.ApiResponse.Metadata.Result;
import com.loopers.interfaces.api.point.PointDto;
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
            UserDto.UserCreateRequest request = new UserCreateRequest(
                    "test", "테스터", "test@test.com", "MALE", "1998-01-08"
            );
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");

            HttpEntity<UserDto.UserCreateRequest> requestEntity = new HttpEntity<>(request, headers);

            ParameterizedTypeReference<ApiResponse<UserDto.UserResponse>> responseType = new ParameterizedTypeReference<>() {
            };

            ResponseEntity<ApiResponse<UserDto.UserResponse>> response = testRestTemplate.exchange(
                    END_POINT, HttpMethod.POST, requestEntity, responseType
            );

            UserResponse responseBodyData = response.getBody().data();

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
            UserDto.UserCreateRequest request = new UserCreateRequest(
                    "test", "테스터", "test@test.com", null, "1998-01-08"
            );
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");

            HttpEntity<UserDto.UserCreateRequest> requestEntity = new HttpEntity<>(request, headers);

            ParameterizedTypeReference<?> responseType = new ParameterizedTypeReference<>() {
            };

            ResponseEntity<?> response = testRestTemplate.exchange(
                    END_POINT, HttpMethod.POST, requestEntity, responseType
            );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

    }

    @DisplayName("GET /api/v1/users/me")
    @Nested
    class Get {
        @DisplayName("내 정보 조회에 성공 할 경우, 해당하는 유저 정보를 응답으로 반환한다.")
        @Test
        void returnUserInfo_whenUserFound() {
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

            ResponseEntity<ApiResponse<UserDto.UserResponse>> response = testRestTemplate.exchange(
                    requestUrl, HttpMethod.GET, requestEntity, responseType
            );
            UserResponse responseBodyData = response.getBody().data();

            assertAll(
                    () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                    () -> assertThat(responseBodyData.userId()).isEqualTo(savedUser.getUserId()),
                    () -> assertThat(responseBodyData.username()).isEqualTo(savedUser.getUsername())
            );
        }

        @DisplayName("존재하지 않는 ID로 조회 할 경우, 404 응답을 반환한다.")
        @Test
        void return404_whenUserNotFound() {
            String requestUrl = END_POINT + "/me";

            ParameterizedTypeReference<?> responseType = new ParameterizedTypeReference<>() {
            };

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-USER-ID", "non-exists-user-id");

            HttpEntity<UserDto.UserCreateRequest> requestEntity = new HttpEntity<>(null, headers);

            ResponseEntity<?> response = testRestTemplate.exchange(
                    requestUrl, HttpMethod.GET, requestEntity, responseType
            );
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @DisplayName("POST /api/v1/users/points")
    @Nested
    class Charge {
        @DisplayName("존재하는 유저가 1,000원을 충전 할 경우, 충전 된 보유 총량을 응답으로 반환한다.")
        @Test
        void returnTotalPoint_whenUserCharged1000Point() {
            String requestUrl = END_POINT + "/points";

            UserCreateCommand command = new UserCreateCommand(
                    "test", "테스터", "test@test.com", Gender.MALE, LocalDate.of(1998, 1, 8)
            );
            UserModel requestUser = UserModel.create(command);
            userJpaRepository.save(requestUser);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
            headers.add("X-USER-ID", "test");

            ParameterizedTypeReference<ApiResponse<PointDto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<PointDto.PointResponse>> response = testRestTemplate.exchange(
                    requestUrl, HttpMethod.POST, new HttpEntity<>(1_000, headers), responseType
            );

            assertAll(
                    () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                    () -> assertThat(response.getBody().meta().result()).isEqualTo(Result.SUCCESS),
                    () -> assertThat(response.getBody().data().point()).isEqualTo(1_000)
            );
        }

        @DisplayName("존재하지 않는 유저로 요청 할 경우, 404 Not Found를 반환한다.")
        @Test
        void return404_whenUserNotFound() {
            String requestUrl = END_POINT + "/points";
            ParameterizedTypeReference<?> responseType = new ParameterizedTypeReference<>() {
            };
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
            headers.add("X-USER-ID", "test");
            ResponseEntity<?> response = testRestTemplate.exchange(
                    requestUrl, HttpMethod.POST, new HttpEntity<>(1_000, headers), responseType
            );
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @DisplayName("GET /api/v1/users/points")
    @Nested
    class GetPoints {
        @DisplayName("포인트 조회에 성공 할 경우, 보유 포인트를 응답으로 반환한다.")
        @Test
        void returnTotalPoint_whenUserCheckPoint() {
            String requestUrl = END_POINT + "/points";

            UserCreateCommand command = new UserCreateCommand(
                    "test", "테스터", "test@test.com", Gender.MALE, LocalDate.of(1998, 1, 8)
            );
            UserModel requestUser = UserModel.create(command);
            userJpaRepository.save(requestUser);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
            headers.add("X-USER-ID", "test");

            ParameterizedTypeReference<ApiResponse<PointDto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
            };

            ResponseEntity<ApiResponse<PointDto.PointResponse>> response = testRestTemplate.exchange(
                    requestUrl, HttpMethod.GET, new HttpEntity<>(null, headers), responseType
            );

            assertAll(
                    () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                    () -> assertThat(response.getBody().meta().result()).isEqualTo(Result.SUCCESS),
                    () -> assertThat(response.getBody().data().point()).isEqualTo(0)
            );

        }

        @DisplayName("X-USER-ID` 헤더가 없을 경우, `400 Bad Request` 응답을 반환한다.")
        @Test
        void return400_whenNotInXUserId() {
            String requestUrl = END_POINT + "/points";

            ParameterizedTypeReference<ApiResponse<PointDto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<PointDto.PointResponse>> response = testRestTemplate.exchange(
                    requestUrl, HttpMethod.GET, new HttpEntity<>(null), responseType
            );

            assertAll(
                    () -> assertTrue(response.getStatusCode().is4xxClientError()),
                    () -> assertThat(response.getBody().meta().result()).isEqualTo(Result.FAIL)
            );
        }
    }

}
