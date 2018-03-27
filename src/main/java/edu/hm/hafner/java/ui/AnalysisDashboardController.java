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

package edu.hm.hafner.java.ui;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleParser;
import edu.hm.hafner.java.db.IssuesTableGateway;
import edu.hm.hafner.java.util.TechnicalException;

/**
 * Entry point for all direct web requests. Refer to {@link IssuesDetailController} in order to see the Ajax entry
 * points.
 *
 * @author Ullrich Hafner
 */
@Controller
public class AnalysisDashboardController {
    private IssuesTableGateway issuesTableGateway;

    @Autowired
    public void setIssuesTableGateway(final IssuesTableGateway issuesTableGateway) {
        this.issuesTableGateway = issuesTableGateway;
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

    // FIXME: should be removed
    @RequestMapping("/db")
    String db(Model model) {
        try {
            List<String> output = issuesTableGateway.readTicks();

            model.addAttribute("records", output);

            return "db";
        }
        catch (TechnicalException exception) {
            model.addAttribute("message", exception.getMessage());

            return "error";
        }
    }

}
