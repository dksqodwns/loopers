package com.loopers.interfaces.api.point;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.loopers.application.user.UserCommand.UserCreateCommand;
import com.loopers.domain.user.Gender;
import com.loopers.domain.user.User;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.ApiResponse.Metadata.Result;
import com.loopers.interfaces.api.point.PointDto.PointChargeRequest;
import com.loopers.interfaces.api.point.PointDto.PointResponse;
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
public class PointApiE2ETest {
    public static final String END_POINT = "/api/v1/users/points";

    private final TestRestTemplate testRestTemplate;
    private final UserJpaRepository userJpaRepository;
    private final DatabaseCleanUp databaseCleanUp;


    @Autowired
    public PointApiE2ETest(
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


    @DisplayName("POST /api/v1/users/points")
    @Nested
    class Charge {
        @DisplayName("존재하는 유저가 1,000원을 충전 할 경우, 충전 된 보유 총량을 응답으로 반환한다.")
        @Test
        void returnTotalPoint_whenUserCharged1000Point() {
            // given
            UserCreateCommand command = new UserCreateCommand(
                    "test", "테스터", "test@test.com", Gender.MALE, LocalDate.of(1998, 1, 8)
            );
            User requestUser = User.create(command);
            userJpaRepository.save(requestUser);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
            headers.add("X-USER-ID", "test");

            PointChargeRequest pointChargeRequest = new PointChargeRequest(1_000);

            ParameterizedTypeReference<ApiResponse<PointResponse>> responseType = new ParameterizedTypeReference<>() {
            };

            // when
            ResponseEntity<ApiResponse<PointResponse>> response = testRestTemplate.exchange(
                    END_POINT, HttpMethod.POST, new HttpEntity<>(pointChargeRequest, headers), responseType
            );

            // then
            assertAll(
                    () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                    () -> assertThat(response.getBody().meta().result()).isEqualTo(Result.SUCCESS),
                    () -> assertThat(response.getBody().data().point()).isEqualTo(1_000)
            );
        }

        @DisplayName("존재하지 않는 유저로 요청 할 경우, 404 Not Found를 반환한다.")
        @Test
        void return404_whenUserNotFound() {
            // given
            ParameterizedTypeReference<?> responseType = new ParameterizedTypeReference<>() {
            };
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
            headers.add("X-USER-ID", "test");

            PointChargeRequest pointChargeRequest = new PointChargeRequest(1_000);

            // when
            ResponseEntity<?> response = testRestTemplate.exchange(
                    END_POINT, HttpMethod.POST, new HttpEntity<>(pointChargeRequest, headers), responseType
            );

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @DisplayName("GET /api/v1/users/points")
    @Nested
    class GetPoints {
        @DisplayName("포인트 조회에 성공 할 경우, 보유 포인트를 응답으로 반환한다.")
        @Test
        void returnTotalPoint_whenUserCheckPoint() {
            // given
            UserCreateCommand command = new UserCreateCommand(
                    "test", "테스터", "test@test.com", Gender.MALE, LocalDate.of(1998, 1, 8)
            );
            User requestUser = User.create(command);
            userJpaRepository.save(requestUser);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
            headers.add("X-USER-ID", "test");

            PointChargeRequest pointChargeRequest = new PointChargeRequest(1_000);

            ParameterizedTypeReference<ApiResponse<PointDto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
            };

            // when
            ResponseEntity<ApiResponse<PointDto.PointResponse>> response = testRestTemplate.exchange(
                    END_POINT, HttpMethod.GET, new HttpEntity<>(pointChargeRequest, headers), responseType
            );

            // then
            assertAll(
                    () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                    () -> assertThat(response.getBody().meta().result()).isEqualTo(Result.SUCCESS),
                    () -> assertThat(response.getBody().data().point()).isEqualTo(0)
            );

        }

        @DisplayName("X-USER-ID` 헤더가 없을 경우, `400 Bad Request` 응답을 반환한다.")
        @Test
        void return400_whenNotInXUserId() {
            // given
            ParameterizedTypeReference<ApiResponse<PointDto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
            };

            // when
            ResponseEntity<ApiResponse<PointDto.PointResponse>> response = testRestTemplate.exchange(
                    END_POINT, HttpMethod.GET, new HttpEntity<>(null), responseType
            );

            // then
            assertAll(
                    () -> assertTrue(response.getStatusCode().is4xxClientError()),
                    () -> assertThat(response.getBody().meta().result()).isEqualTo(Result.FAIL)
            );
        }
    }

}
