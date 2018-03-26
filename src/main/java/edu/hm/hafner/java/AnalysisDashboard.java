/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.hm.hafner.java;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleParser;

/**
 * Entry point for all web requests. Also responsible to start the Spring Boot Application.
 *
 * @author Ullrich Hafner
 */
@Controller
@SpringBootApplication
public class AnalysisDashboard {
    @Value("${spring.datasource.url}")
    private String dbUrl;

    private DataSource dataSource;

    @Autowired
    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Starts the application.
     *
     * @param args
     *         optional commandline arguments
     */
    public static void main(final String... args) {
        SpringApplication.run(AnalysisDashboard.class, args);
    }

    /**
     * Returns the main page, served as "index.html".
     *
     * @return the main page
     */
    @RequestMapping("/")
    String index() {
        return "index";
    }

    /**
     * Shows the trend for several static analysis runs.
     *
     * @param model
     *         UI model
     *
     * @return the URL for the trend page
     */
    @RequestMapping("/trend")
    String createTrend(final Model model) {
        return "trend";
    }

    /**
     * Shows the details for one static analysis run.
     *
     * @return the URL for the details page
     */
    @RequestMapping("/details")
    String createDetails() {
        return "details";
    }

    /**
     * Ajax entry point: returns the number of issues per priority (as a JSON array).
     *
     * @param id
     *         the ID of the issues instance to show the details for
     *
     * @return the number of issues as priority, e.g. [10, 20, 70]
     */
    @RequestMapping(path = "/priorities", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    ResponseEntity<?> getPriorities(@RequestParam(value = "id") final String id) {
        Random random = new Random();
        int[] priorities = {random.nextInt(100), random.nextInt(100), random.nextInt(100)};

        Gson gson = new Gson();

        return ResponseEntity.ok(gson.toJson(priorities));
    }

    /**
     * Ajax entry point: returns the number of issues per priority (as a JSON array).
     *
     * @param id
     *         the ID of the issues instance to show the details for
     *
     * @return the number of issues as priority, e.g. [10, 20, 70]
     */
    @RequestMapping(path = "/categories", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    ResponseEntity<?> getCategories(@RequestParam(value = "id") final String id) {
        Random random = new Random();
        int[] priorities = {random.nextInt(100), random.nextInt(100), random.nextInt(100)};

        Gson gson = new Gson();

        return ResponseEntity.ok(gson.toJson(priorities));
    }

    /**
     * Experimental entry point to upload a static analysis report via curl.
     * <p>
     * Example:
     * <pre>
     *     curl -F "file=@checkstyle-result.xml" -F"tool=checkstyle" https://[id].herokuapp.com/upload
     * </pre>
     *
     * @param file
     *         the analysis report
     * @param tool
     *         the ID of the static analysis tool
     */
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    @ResponseBody
    String upload(@RequestParam(value = "file") final MultipartFile file,
            @RequestParam(value = "tool") final String tool) {
        if (StringUtils.containsIgnoreCase(tool, "checkstyle")) {
            return parseCheckstyleResults(file);
        }
        return String.format("Tool %s not yet supported.", tool);
    }

    private String parseCheckstyleResults(final MultipartFile file) {
        try {
            CheckStyleParser parser = new CheckStyleParser();
            Issues<Issue> issues = parser.parse(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));

            return String.format("Found %d issues", issues.size());
        }
        catch (IOException exception) {
            return exception.getLocalizedMessage();
        }
    }

    @RequestMapping("/db")
    String db(Model model) {
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
            stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
            ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

            ArrayList<String> output = new ArrayList<String>();
            while (rs.next()) {
                output.add("Read from DB: " + rs.getTimestamp("tick"));
            }

            model.addAttribute("records", output);
            return "db";
        }
        catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }
    }

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
}
