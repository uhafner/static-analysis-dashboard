package edu.hm.hafner.java.db;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import edu.hm.hafner.java.util.TechnicalException;

/**
 * Acts as gateway to the ticks table.
 *
 * @author Ullrich Hafner
 */
// FIXME: should be replaced by entity layer
@Controller
public class IssuesTableGateway {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    private DataSource dataSource;

    @Autowired
    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /*
    @Bean
    public DataSource dataSource() throws SQLException {
        if (dbUrl == null || dbUrl.isEmpty()) {
            return new HikariDataSource();
        }
        else {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(dbUrl);
            return new HikariDataSource(config);
        }
    }
    */

    public List<String> readTicks() {
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
            stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
            ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

            List<String> output = new ArrayList<String>();
            while (rs.next()) {
                output.add("Read from DB: " + rs.getTimestamp("tick"));
            }

            return output;
        }
        catch (SQLException exception) {
            throw new TechnicalException("Can't read from table", exception);
        }
    }
}
