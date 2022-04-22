package io.cartel.noord.brabant.shared;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * {@link AbstractRedisIT} base test class to be extended when redis integration tests are needed.
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
public abstract class AbstractRedisIT {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * Cleanup all keys after a test is run
     */
    @AfterEach
    public void cleanup() {
        var allKeys = redisTemplate.keys("*");
        if (allKeys != null) {
            redisTemplate.delete(allKeys);
        }
    }
}
