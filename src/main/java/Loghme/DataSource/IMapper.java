package Loghme.DataSource;

import java.sql.SQLException;

public interface IMapper<T, I> {
    T find(I id) throws SQLException;
    void insert(T t) throws SQLException;
    void delete(I id) throws SQLException;
    void update(T t, I id) throws SQLException;
}