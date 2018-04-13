package edu.hm.hafner.java.ui;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.java.uc.IssuesService;

/**
 * Uploads new issues reports.
 *
 * @author Ullrich Hafner
 */
@Controller
public class UploadController {
    @SuppressWarnings("InstanceVariableMayNotBeInitialized")
    private final IssuesService issuesService;

    @Autowired
    public UploadController(final IssuesService issuesService) {
        this.issuesService = issuesService;
    }

    /**
     * Uploads a static analysis report via curl or a web form.
     * <p>
     * Example:
     * <pre>
     *     curl -F "file=@checkstyle-result.xml" -F"tool=checkstyle" https://[id].herokuapp.com/issues
     * </pre>
     *
     * @param file the analysis report
     * @param tool the ID of the static analysis tool
     * @return A response with the details
     */
    @RequestMapping(path = "/issues", method = RequestMethod.POST)
    @ResponseBody
    String upload(@RequestParam(value = "file") final MultipartFile file,
            @RequestParam(value = "tool") final String tool) {
        try {
            return issuesService.parse(tool, file.getInputStream());
        }
        catch (IOException e) {
            return "Can't read uploaded file.";
        }
        catch (ParsingException e) {
            return String.format("Can't parse uploaded file.%n%s", e.getMessage());
        }
    }
}
