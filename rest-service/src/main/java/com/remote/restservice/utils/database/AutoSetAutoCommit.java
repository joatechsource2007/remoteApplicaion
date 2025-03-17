package com.remote.restservice.utils.database;

import java.sql.Connection;
import java.sql.SQLException;
public class AutoSetAutoCommit implements AutoCloseable {

    private Connection conn;
    private boolean originalAutoCommit;

    public AutoSetAutoCommit(Connection conn, boolean autoCommit) throws SQLException {
        this.conn = conn;
        originalAutoCommit = conn.getAutoCommit();
        conn.setAutoCommit(autoCommit);
    }

    @Override
    public void close() throws SQLException {
        conn.setAutoCommit(originalAutoCommit);
    }

}
