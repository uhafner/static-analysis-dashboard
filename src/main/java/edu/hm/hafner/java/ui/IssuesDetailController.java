package edu.hm.hafner.java.ui;

import java.util.Random;

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
    /**
     * Ajax entry point: returns the number of issues per priority (as JSON array).
     *
     * @param id
     *         the ID of the issues instance to show the details for
     *
     * @return the number of issues per priority, e.g. {@code [10, 20, 70]}
     */
    @RequestMapping(path = "/ajax/priorities", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    @SuppressWarnings("unused") // called by details.js
    ResponseEntity<?> getPriorities(@RequestParam(value = "id") final String id) {
        Random random = new Random();
        int[] priorities = {random.nextInt(100), random.nextInt(100), random.nextInt(100)};

        Gson gson = new Gson();

        return ResponseEntity.ok(gson.toJson(priorities));
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
    ResponseEntity<?> getCategories(@RequestParam(value = "id") final String id) {
        IssuePropertyDistribution model = new IssuesService().createDistributionByCategory();

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
    ResponseEntity<?> getTypes(@RequestParam(value = "id") final String id) {
        IssuePropertyDistribution model = new IssuesService().createDistributionByType();

        Gson gson = new Gson();
        return ResponseEntity.ok(gson.toJson(model));
    }

}
