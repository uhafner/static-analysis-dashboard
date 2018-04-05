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
import edu.hm.hafner.java.uc.IssuePropertyDistribution;
import edu.hm.hafner.java.uc.IssuesService;

/**
 * Provides detail information for a specific set of {@link Issues}.
 *
 * @author Ullrich Hafner
 */
@Controller
public class IssuesDetailController {
    @SuppressWarnings("InstanceVariableMayNotBeInitialized")
    private IssuesService issuesService;

    @Autowired
    public void setIssuesService(final IssuesService issuesService) {
        this.issuesService = issuesService;
    }

    /**
     * Ajax entry point: returns the number of issues per category (as JSON object). The returned JSON object
     * is in the expected format for the {@code data} property of a bar chart.
     *
     * Example:
     * <pre>
     *     { "labels" : ["Design","Documentation","Best Practices","Performance","Code Style","Error Prone"],
     *      "datasets" : [
     *          {"data" : [15,3,20,6,53,12]}
      *      ]
     *      }
     * </pre>
     *
     * @param id
     *         the ID of the issues instance to show the details for
     *
     * @return the number of issues per category, e.g. [10, 20, 70]
     */
    @RequestMapping(path = "/ajax/categories", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    @SuppressWarnings("unused") // called by details.js
    ResponseEntity<?> getCategories(@RequestParam("id") final String id) {
        IssuePropertyDistribution model = issuesService.createDistributionByCategory(id);

        Gson gson = new Gson();
        return ResponseEntity.ok(gson.toJson(model));
    }

    /**
     * Ajax entry point: returns the number of issues per category (as JSON object). The returned JSON object
     * is in the expected format for the {@code data} property of a bar chart.
     *
     * Example:
     * <pre>
     *     { "labels" : ["Design","Documentation","Best Practices","Performance","Code Style","Error Prone"],
     *      "datasets" : [
     *          {"data" : [15,3,20,6,53,12]}
      *      ]
     *      }
     * </pre>
     *
     * @param id
     *         the ID of the issues instance to show the details for
     *
     * @return the number of issues per category, e.g. [10, 20, 70]
     */
    @RequestMapping(path = "/ajax/types", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    @SuppressWarnings("unused") // called by details.js
    ResponseEntity<?> getTypes(@RequestParam("id") final String id) {
        IssuePropertyDistribution model = issuesService.createDistributionByType(id);

        Gson gson = new Gson();
        return ResponseEntity.ok(gson.toJson(model));
    }
}
