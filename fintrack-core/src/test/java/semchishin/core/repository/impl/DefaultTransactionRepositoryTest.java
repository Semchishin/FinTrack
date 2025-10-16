package semchishin.core.repository.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import semchishin.core.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static semchishin.core.util.Constants.AMOUNT;
import static semchishin.core.util.Constants.ANOTHER;
import static semchishin.core.util.Constants.BIG_DECIMAL_100;
import static semchishin.core.util.Constants.CATEGORY;
import static semchishin.core.util.Constants.COUNT;
import static semchishin.core.util.Constants.CREATED_AT;
import static semchishin.core.util.Constants.CREATE_TABLE_IF_NOT_EXIST;
import static semchishin.core.util.Constants.CURRENT_TIMESTAMP;
import static semchishin.core.util.Constants.DEFAULT;
import static semchishin.core.util.Constants.DELETE;
import static semchishin.core.util.Constants.FOOD;
import static semchishin.core.util.Constants.FROM;
import static semchishin.core.util.Constants.INSERT_INTO;
import static semchishin.core.util.Constants.LONG_1;
import static semchishin.core.util.Constants.NOT_NULL;
import static semchishin.core.util.Constants.NUMERIC;
import static semchishin.core.util.Constants.PRIMARY_KEY;
import static semchishin.core.util.Constants.RESTART_IDENTITY_CASCADE;
import static semchishin.core.util.Constants.SELECT;
import static semchishin.core.util.Constants.SERIAL;
import static semchishin.core.util.Constants.SET;
import static semchishin.core.util.Constants.TABLE;
import static semchishin.core.util.Constants.TIMESTAMP;
import static semchishin.core.util.Constants.TRANSACTION;
import static semchishin.core.util.Constants.TRANSACTION_ID;
import static semchishin.core.util.Constants.TRUNCATE;
import static semchishin.core.util.Constants.UPDATE;
import static semchishin.core.util.Constants.VALUES;
import static semchishin.core.util.Constants.VARCHAR_255;
import static semchishin.core.util.Constants.WHERE;
import static semchishin.core.util.TestcontainersConstants.DB_NAME;
import static semchishin.core.util.TestcontainersConstants.DB_PASSWORD;
import static semchishin.core.util.TestcontainersConstants.DB_USERNAME;
import static semchishin.core.util.TestcontainersConstants.POSTGRES;

@Testcontainers
@Execution(ExecutionMode.SAME_THREAD)
class DefaultTransactionRepositoryTest {

    private static final LocalDateTime DATE_TIME = LocalDateTime.now().
            withNano((LocalDateTime.now().getNano() / 1000) * 1000);

    private static final Transaction TRANSACTION1 = new Transaction(LONG_1, BIG_DECIMAL_100, FOOD, DATE_TIME);

    @Container
    @SuppressWarnings("resource")
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(POSTGRES)
                    .withDatabaseName(DB_NAME)
                    .withUsername(DB_USERNAME)
                    .withPassword(DB_PASSWORD);

    private JdbcTemplate jdbc;

    @BeforeEach()
    void setup() {
        jdbc = new JdbcTemplate(
                new DriverManagerDataSource(
                        postgres.getJdbcUrl(),
                        postgres.getUsername(),
                        postgres.getPassword()
                )
        );

        jdbc.execute(
                CREATE_TABLE_IF_NOT_EXIST + " " + TRANSACTION + " (" +
                        TRANSACTION_ID + " " + SERIAL + " " + PRIMARY_KEY + ", " +
                        AMOUNT + " " + NUMERIC + " " + NOT_NULL + ", " +
                        CATEGORY + " " + VARCHAR_255 + ", " +
                        CREATED_AT + " " + TIMESTAMP + " " + NOT_NULL + " " + DEFAULT + " " + CURRENT_TIMESTAMP + ")"
        );

        jdbc.update(
                INSERT_INTO + " " + TRANSACTION + " (" + AMOUNT + ", " + CATEGORY + ", " + CREATED_AT + " ) "
                        + VALUES + "(?, ?, ?)",
                BIG_DECIMAL_100, FOOD, DATE_TIME
        );
    }

    @AfterEach
    void tearDown() {
        jdbc.execute(TRUNCATE + " " + TABLE + " " + TRANSACTION + " " + RESTART_IDENTITY_CASCADE);
    }

    @Test
    void shouldSave() {
        Integer count = jdbc.queryForObject(SELECT + " " + COUNT + "(*) " + TRANSACTION, Integer.class);

        assertEquals(1, count);
    }

    @Test
    void shouldFindById() {
        Transaction actual = jdbc.queryForObject(SELECT + " * " + FROM + " " + TRANSACTION + " " +
                        WHERE + " " + TRANSACTION_ID + " = " + LONG_1,
                (rs, rowNum) -> new Transaction(
                        rs.getLong(1),
                        rs.getBigDecimal(2),
                        rs.getString(3),
                        rs.getTimestamp(4).toLocalDateTime()
                )
        );

        assertEquals(TRANSACTION1, actual);
    }

    @Test
    void shouldFindAll() {
        List<Transaction> expected = List.of(TRANSACTION1);
        Transaction actual = jdbc.queryForObject(SELECT + " * " + FROM + " " + TRANSACTION + " " +
                        WHERE + " " + CATEGORY + " = '" + FOOD +"'",
                (rs, rowNum) -> new Transaction(
                        rs.getLong(1),
                        rs.getBigDecimal(2),
                        rs.getString(3),
                        rs.getTimestamp(4).toLocalDateTime()
                )
        );

        Assertions.assertNotNull(actual);
        assertEquals(expected, List.of(actual));
    }

    @Test
    void shouldUpdate() {
        Transaction actual = jdbc.queryForObject(SELECT + " * " + FROM + " " + TRANSACTION + " " +
                        WHERE + " " + CATEGORY + " = '" + FOOD +"'",
                (rs, rowNum) -> new Transaction(
                        rs.getLong(1),
                        rs.getBigDecimal(2),
                        rs.getString(3),
                        rs.getTimestamp(4).toLocalDateTime()
                )
        );

        assertEquals(TRANSACTION1, actual);

        jdbc.update(UPDATE + " " + TRANSACTION + " " + SET + " " + CATEGORY + " = '" + ANOTHER + "' " +
                WHERE + " " + TRANSACTION_ID + " = " + LONG_1);
        actual = jdbc.queryForObject(SELECT + " * " + FROM + " " + TRANSACTION + " " +
                WHERE + " " + TRANSACTION_ID + " = " + LONG_1,
                (rs, rowNum) -> new Transaction(
                        rs.getLong(1),
                        rs.getBigDecimal(2),
                        rs.getString(3),
                        rs.getTimestamp(4).toLocalDateTime()
                )
        );

        assertNotEquals(TRANSACTION1, actual);
    }

    @Test
    void deleteById() {
        jdbc.update(DELETE + " " + FROM + " " + TRANSACTION + " " + WHERE + " " + TRANSACTION_ID + " = ?", LONG_1);

        Integer count = jdbc.queryForObject(SELECT + " " + COUNT + "(*) " + FROM + " " + TRANSACTION, Integer.class);

        assertEquals(0, count);
    }
}