package semchishin.core.repository;

public final class SqlQueries {

    public static final String SELECT_BY_ID = "SELECT * FROM %s WHERE transaction_id = ?";

    public static final String SELECT_ALL = "SELECT * FROM %s";

    public static final String INSERT = "INSERT INTO %s (%s) VALUES (%s)";

    public static final String UPDATE_BY_ID = "UPDATE %s SET %s WHERE transaction_id = ?";

    public static final String DELETE_BY_ID = "DELETE FROM %s WHERE transaction_id = ?";

}

