package semchishin.core.service.transaction.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import semchishin.core.model.Transaction;
import semchishin.core.repository.impl.DefaultTransactionRepository;
import semchishin.core.service.transaction.TransactionService;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link TransactionService} that provides
 * CRUD operations for {@link Transaction} entities.
 * <p>
 * This service delegates all data access operations to
 * {@link DefaultTransactionRepository}.
 * </p>
 *
 * <p>Supported operations:</p>
 * <ul>
 *     <li>Add a new transaction</li>
 *     <li>Delete a transaction by ID</li>
 *     <li>Find a transaction by ID</li>
 *     <li>Retrieve all transactions</li>
 *     <li>Update an existing transaction</li>
 * </ul>
 *
 * <p>This service is annotated with {@link Service} for Spring
 * dependency injection.</p>
 *
 * @author Sergey Semchishin
 * @since 1.0
 */

@Service
@RequiredArgsConstructor
public class DefaultTransactionService implements TransactionService {

    /**
     * Repository for performing database operations on transactions.
     */
    private final DefaultTransactionRepository transactionRepository;

    /**
     * Adds a new transaction to the database.
     *
     * @param transaction the {@link Transaction} to add
     * @return the saved {@link Transaction} object
     */
    @Override
    public Transaction addTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    /**
     * Deletes a transaction by its ID.
     *
     * @param transactionId the ID of the transaction to delete
     */
    @Override
    public void deleteTransaction(long transactionId) {
        transactionRepository.deleteById(transactionId);
    }

    /**
     * Finds a transaction by its ID.
     *
     * @param transactionId the ID of the transaction
     * @return an Optional containing the {@link Transaction} if found, or empty if not found
     */
    @Override
    public Optional<Transaction> findTransactionById(long transactionId) {
        return transactionRepository.findById(transactionId);
    }

    /**
     * Returns all transactions.
     *
     * @return a list of all {@link Transaction} objects
     */
    @Override
    public List<Transaction> findAllTransactions() {
        return transactionRepository.findAll();
    }

    /**
     * Updates an existing transaction in the database.
     *
     * @param transaction the {@link Transaction} object with updated values
     */
    @Override
    public void updateTransaction(Transaction transaction) {
        transactionRepository.update(transaction);
    }

}
