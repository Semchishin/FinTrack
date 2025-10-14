package semchishin.core.service.transaction;

import semchishin.core.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionService {

    Transaction addTransaction(Transaction transaction);

    void deleteTransaction(long transactionId);

    Optional<Transaction> findTransactionById(long transactionId);

    List<Transaction> findAllTransactions();

    void updateTransaction(Transaction transaction);

}
