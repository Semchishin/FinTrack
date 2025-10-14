package semchishin.core.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository <T, N>{

    T save(T entity);

    Optional<T> findById(N id);

    List<T> findAll();

    void update(T entity);

    void deleteById(N id);
}
