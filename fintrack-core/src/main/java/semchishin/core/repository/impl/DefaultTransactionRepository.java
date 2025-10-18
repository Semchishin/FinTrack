package semchishin.core.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import semchishin.core.model.Transaction;
import semchishin.core.repository.CrudRepository;
import semchishin.core.repository.SqlQueries;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link CrudRepository} for handling {@link Transaction} entities.
 * <p>
 * Uses {@link JdbcTemplate} to execute SQL queries against the transactions table.
 * All SQL queries are defined in {@link SqlQueries}.
 * </p>
 *
 * <p>Supported operations:</p>
 * <ul>
 *     <li>Create a transaction (save)</li>
 *     <li>Find a transaction by ID (findById)</li>
 *     <li>Retrieve all transactions (findAll)</li>
 *     <li>Update a transaction (update)</li>
 *     <li>Delete a transaction by ID (deleteById)</li>
 * </ul>
 *
 * @author Sergey Semchishin
 * @since 1.0
 */

@Repository
@RequiredArgsConstructor
public class DefaultTransactionRepository implements CrudRepository<Transaction, Long> {

    /**
     * JdbcTemplate for executing SQL queries.
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * RowMapper to map each row of the ResultSet to a {@link Transaction} object.
     */
    private static final RowMapper<Transaction> ROW_MAPPER = (rs, rowNum) -> new Transaction(
            rs.getLong("transaction_id"),
            rs.getBigDecimal("amount"),
            rs.getString("category"),
            rs.getTimestamp("created_at").toLocalDateTime()
    );

    /**
     * Name of the database table.
     */
    private static final String TABLE_NAME = "transaction";

    /**
     * Saves a new transaction into the database.
     *
     * @param entity the {@link Transaction} object to save
     * @return the saved {@link Transaction} object
     */
    @Override
    public Transaction save(Transaction entity) {
        String sql = String.format(SqlQueries.INSERT, TABLE_NAME, "amount, category, created_at", "?, ?, ?");
        jdbcTemplate.update(sql, entity.getAmount(), entity.getCategory(), entity.getDateTime());
        return entity;
    }

    /**
     * Finds a transaction by its ID.
     *
     * @param id the transaction ID
     * @return an Optional containing the transaction if found, or empty if not found
     */
    @Override
    public Optional<Transaction> findById(Long id) {
        String sql = String.format(SqlQueries.SELECT_BY_ID, TABLE_NAME);
        List<Transaction> list = jdbcTemplate.query(sql, ROW_MAPPER, id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.getFirst());
    }

    /**
     * Returns a list of all transactions from the database.
     *
     * @return list of {@link Transaction} objects
     */
    @Override
    public List<Transaction> findAll() {
        String sql = String.format(SqlQueries.SELECT_ALL, TABLE_NAME);
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    /**
     * Updates an existing transaction in the database.
     *
     * @param entity the {@link Transaction} object with updated values
     */
    @Override
    public void update(Transaction entity) {
        String sql = String.format(SqlQueries.UPDATE_BY_ID, TABLE_NAME,"amount = ?, category = ?");
        jdbcTemplate.update(sql, entity.getAmount(), entity.getCategory(), entity.getTransactionId());
    }

    /**
     * Deletes a transaction from the database by its ID.
     *
     * @param id the transaction ID
     */
    @Override
    public void deleteById(Long id) {
        String sql = String.format(SqlQueries.DELETE_BY_ID, TABLE_NAME);
        jdbcTemplate.update(sql, id);
    }
}
