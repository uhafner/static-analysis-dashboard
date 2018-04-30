package edu.hm.hafner.java.ui;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;

import edu.hm.hafner.analysis.parser.pmd.PmdParser;
import edu.hm.hafner.java.uc.AnalysisTool;
import edu.hm.hafner.java.uc.IssuesService;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests the class {@link AnalysisDashboardController}.
 *
 * @author Ullrich Hafner
 */
class AnalysisDashboardControllerTest {
    @Test
    void shouldPopulateModelWithOriginAndReference() {
        AnalysisDashboardController controller = new AnalysisDashboardController(null);

        ConcurrentModel model = new ConcurrentModel();
        String details = controller.createDetails("origin", "reference", model);

        assertThat(details).isEqualTo("details");
        assertThat(model).contains(entry("origin", "origin"));
        assertThat(model).contains(entry("reference", "reference"));
    }

    @Test
    void shouldPopulateModelWithAvailableTools() {
        AnalysisDashboardController controller = new AnalysisDashboardController(new IssuesServiceStub());

        ConcurrentModel model = new ConcurrentModel();
        String details = controller.createUpload(model);

        assertThat(details).isEqualTo("upload");
        assertThat(model).containsKeys("tools");
        assertThat(model).containsValue(Collections.singletonList(IssuesServiceStub.PMD));
    }

    @Test
    void shouldPopulateModelWithAvailableToolsUsingAStub() {
        IssuesService service = mock(IssuesService.class);
        AnalysisDashboardController controller = new AnalysisDashboardController(service);

        List<AnalysisTool> tools = Collections.singletonList(mock(AnalysisTool.class));
        when(service.findAllTools()).thenReturn(tools);

        ConcurrentModel model = new ConcurrentModel();
        String details = controller.createUpload(model);

        assertThat(details).isEqualTo("upload");
        assertThat(model).containsKeys("tools");
        assertThat(model).containsValue(tools);
    }

    /**
     * A simple stub.
     */
    private static class IssuesServiceStub extends IssuesService {
        private static final AnalysisTool PMD = new AnalysisTool("pmd", "PMD", new PmdParser());

        IssuesServiceStub() {
            super(null);
        }

        @Override
        public List<AnalysisTool> findAllTools() {
            return Collections.singletonList(PMD);
        }
    }
}