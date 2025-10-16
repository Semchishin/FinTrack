package semchishin.core.service.transaction.impl;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import semchishin.core.model.Transaction;
import semchishin.core.repository.impl.DefaultTransactionRepository;
import semchishin.core.util.Constants;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class DefaultTransactionServiceTest {

    @Mock
    private DefaultTransactionRepository repository;

    @InjectMocks
    private DefaultTransactionService service;

    private final Transaction transaction = new Transaction(
            Constants.LONG_1,
            Constants.BIG_DECIMAL_100,
            Constants.FOOD,
            LocalDateTime.now()
    );

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldAddTransaction() {
        when(repository.save(transaction)).thenReturn(transaction);

        Transaction actual = service.addTransaction(transaction);

        assertThat(actual).isEqualTo(transaction);
        verify(repository).save(transaction);
    }

    @Test
    void shouldDeleteTransaction() {
        service.deleteTransaction(Constants.LONG_1);

        verify(repository).deleteById(Constants.LONG_1);
    }

    @Test
    void shouldFindTransactionById() {
        when(repository.findById(Constants.LONG_1)).thenReturn(Optional.of(transaction));

        Transaction actual = service.findTransactionById(Constants.LONG_1).orElse(null);

        assertThat(actual).isEqualTo(transaction);
        verify(repository).findById(Constants.LONG_1);
    }

    @Test
    void shouldFindAllTransactions() {
        List<Transaction> expected = List.of(transaction);
        when(repository.findAll()).thenReturn(List.of(transaction));

        List<Transaction> transactionList = service.findAllTransactions();

        assertThat(transactionList).isEqualTo(expected);
        verify(repository).findAll();
    }

    @Test
    void shouldUpdateTransaction() {
        service.updateTransaction(transaction);

        verify(repository).update(transaction);
    }
}