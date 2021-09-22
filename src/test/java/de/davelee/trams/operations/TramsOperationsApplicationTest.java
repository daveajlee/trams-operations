package de.davelee.trams.operations;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Simple integration test to ensure 100% test coverage of Spring Boot application.
 * Just to make the metric look good :)
 * @author Dave Lee
 */
@SpringBootTest(classes = TramsOperationsApplication.class)
@ActiveProfiles("test")
public class TramsOperationsApplicationTest {

    /**
     * Test case: check main method throws no errors
     * No expected result.
     */
    @Test
    public void main() {
        TramsOperationsApplication.main(new String[] {});
    }

}