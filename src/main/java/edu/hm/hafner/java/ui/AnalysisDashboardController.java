package edu.hm.hafner.java.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.hm.hafner.java.uc.IssuesService;

/**
 * Entry point for all direct web requests. Refer to {@link IssuesDetailController} in order to see the Ajax entry
 * points.
 *
 * @author Ullrich Hafner
 */
@Controller
public class AnalysisDashboardController {
    @SuppressWarnings("InstanceVariableMayNotBeInitialized")
    private final IssuesService issuesService;

    @Autowired
    public AnalysisDashboardController(final IssuesService issuesService) {
        this.issuesService = issuesService;
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
     * Shows the details for one static analysis run.
     *
     * @return the URL for the details page
     */
    @RequestMapping("/details")
    String createDetails(@RequestParam("origin") final String origin,
            @RequestParam("reference") final String reference, final Model model) {
        model.addAttribute("origin", origin);
        model.addAttribute("reference", reference);

        return "details";
    }

    /**
     * Shows a table with the uploaded reports.
     *
     * @return the URL for the reports statistics page
     */
    @RequestMapping("/issues")
    String createIssues() {
        return "issues";
    }

    /**
     * Shows a form to upload a new report.
     *
     * @return the URL for the upload page
     */
    @RequestMapping("/upload")
    String createUpload(final Model model) {
        model.addAttribute("tools", issuesService.findAllTools());
        return "upload";
    }
}

