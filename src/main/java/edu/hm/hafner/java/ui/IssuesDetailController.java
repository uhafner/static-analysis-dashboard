package edu.hm.hafner.java.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.java.uc.IssuesService;

/**
 * Provides detail information for a specific set of {@link Issues}.
 *
 * @author Ullrich Hafner
 */
@Controller
public class IssuesDetailController {
    @SuppressWarnings("InstanceVariableMayNotBeInitialized")
    private final IssuesService issuesService;

    @Autowired
    public IssuesDetailController(final IssuesService issuesService) {
        this.issuesService = issuesService;
    }

    /**
     * Ajax entry point: returns a table with statistics of the uploaded reports (as JSON object). The returned JSON
     * object is in the expected format for the {@code data} property of a DataTable.
     *
     * @return issues statistics of all uploaded reports
     */
    @RequestMapping(path = "/ajax/issues", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    @SuppressWarnings("unused")
    // called by issues.js
    ResponseEntity<String> getIssues() {
        return createResponseFrom(issuesService.createIssuesStatistics());
    }

    /**
     * Ajax entry point: returns the number of issues per category (as JSON object). The returned JSON object is in the
     * expected format for the {@code data} property of a bar chart.
     * <p>
     * Example:
     * <pre>
     *     { "labels" : ["Design","Documentation","Best Practices","Performance","Code Style","Error Prone"],
     *      "datasets" : [
     *          {"data" : [15,3,20,6,53,12]}
     *      ]
     *      }
     * </pre>
     *
     * @param origin
     *         the origin of the issues instance to show the details for
     * @param reference
     *         the reference of the issues instance to show the details for
     *
     * @return the number of issues per category
     */
    @RequestMapping(path = "/ajax/categories", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    @SuppressWarnings("unused")
    // called by details.js
    ResponseEntity<String> getCategories(@RequestParam("origin") final String origin,
            @RequestParam("reference") final String reference) {
        return createResponseFrom(issuesService.createDistributionByCategory(origin, reference));
    }

    /**
     * Ajax entry point: returns the number of issues per type (as JSON object). The returned JSON object is in the
     * expected format for the {@code data} property of a bar chart.
     *
     * @param origin
     *         the origin of the issues instance to show the details for
     * @param reference
     *         the reference of the issues instance to show the details for
     *
     * @return the number of issues per type
     */
    @RequestMapping(path = "/ajax/types", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    @SuppressWarnings("unused")
    // called by details.js
    ResponseEntity<String> getTypes(@RequestParam("origin") final String origin,
            @RequestParam("reference") final String reference) {
        return createResponseFrom(issuesService.createDistributionByType(origin, reference));
    }

    /**
     * Ajax entry point: returns the number of issues per priority (as JSON array). The returned JSON object is in the
     *      * expected format for the {@code data} property of a doughnut chart.
     *
     * @param origin
     *         the origin of the issues instance to show the details for
     * @param reference
     *         the reference of the issues instance to show the details for
     *
     * @return the number of issues per priority
     */
    @RequestMapping(path = "/ajax/priorities", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    @SuppressWarnings("unused")
    // called by details.js
    ResponseEntity<String> getPriorities(@RequestParam("origin") final String origin,
            @RequestParam("reference") final String reference) {
        return createResponseFrom(issuesService.createDistributionByPriority(origin, reference));
    }

    private ResponseEntity<String> createResponseFrom(final Object model) {
        return ResponseEntity.ok(new Gson().toJson(model));
    }
}
