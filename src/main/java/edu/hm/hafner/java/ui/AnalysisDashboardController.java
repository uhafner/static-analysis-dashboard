package edu.hm.hafner.java.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Entry point for all direct web requests. Refer to {@link IssuesDetailController} in order to see the Ajax entry
 * points.
 *
 * @author Ullrich Hafner
 */
@Controller
public class AnalysisDashboardController {
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
     * Shows the details for one static analysis run.
     *
     * @return the URL for the details page
     */
    @RequestMapping("/details")
    String createDetails() {
        return "details";
    }
}
