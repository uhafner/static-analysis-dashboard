package edu.hm.hafner.java.ui;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.java.uc.IssuesService;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests the class {@link UploadController}.
 *
 * @author Ullrich Hafner
 */
class UploadControllerTest {
    private static final String REFERENCE = "reference";
    private static final String ORIGIN = "origin";
    private static final String TOOL = "tool";

    @Test
    void shouldSetOriginAndReferenceInModel() {
        Issues<Issue> issues = new Issues<>();
        issues.setReference(REFERENCE);
        issues.setOrigin(ORIGIN);

        IssuesService service = mock(IssuesService.class);
        when(service.parse(TOOL, null)).thenReturn(issues);

        UploadController controller = new UploadController(service);

        Model model = new ConcurrentModel();
        String result = controller.upload(mock(MultipartFile.class), TOOL, model);

        assertThat(model.asMap()).containsOnly(entry(ORIGIN, ORIGIN), entry(REFERENCE, REFERENCE));
        assertThat(result).isEqualTo("details");
    }

    @Test
    void shouldThrowExceptionIfParsingThrowsException() throws IOException {
        IssuesService service = mock(IssuesService.class);
        UploadController controller = new UploadController(service);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenThrow(new FileNotFoundException(""));
        assertThatThrownBy(() -> controller.upload(file, TOOL, new ConcurrentModel()))
                .isInstanceOf(ParsingException.class)
                .hasMessageContaining(UploadController.FILENAME_DUMMY);
    }
}