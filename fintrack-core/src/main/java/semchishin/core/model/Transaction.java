package semchishin.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a financial transaction record in the system.
 * <p>
 * Each transaction contains information about the amount,
 * associated category, and the timestamp when it occurred.
 * </p>
 *
 * <p>Typical use cases:</p>
 * <ul>
 *     <li>Displaying user’s transaction history</li>
 *     <li>Calculating balances and statistics</li>
 *     <li>Categorizing expenses or incomes</li>
 * </ul>
 *
 * @author Sergey Semchishin
 * @since 1.0
 */

@AllArgsConstructor
@Data
public class Transaction {

    /**
     * Unique identifier of the transaction.
     */
    private Long transactionId;

    /**
     * Transaction amount.
     * Positive values typically represent income,
     * while negative values represent expenses.
     */
    private BigDecimal amount;

    /**
     * Category to which this transaction belongs (e.g., "Food", "Transport").
     */
    private String category;

    /**
     * Date and time when the transaction occurred.
     */
    private LocalDateTime dateTime;

}
