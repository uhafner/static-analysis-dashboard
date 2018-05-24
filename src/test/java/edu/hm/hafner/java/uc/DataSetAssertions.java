package edu.hm.hafner.java.uc;

import java.util.List;

import com.jayway.jsonpath.DocumentContext;

import net.minidev.json.JSONArray;
import static org.assertj.core.api.Assertions.*;

/**
 * Provides an assertion that checks if the model for the charts is correctly set up.
 *
 * @author Ullrich Hafner
 */
public class DataSetAssertions {
    /**
     * Asserts that the specified context contains the expected labels and data values.
     *
     * @param expectedLabels
     *         the expected labels
     * @param expectedSizes
     *         the expected data values
     * @param actualJson
     *         the JSON object to validate
     */
    public void assertThatChartModelIsCorrect(final List<String> expectedLabels, final List<Integer> expectedSizes,
            final DocumentContext actualJson) {
        JSONArray actualLabels = actualJson.read("$.labels[*]", JSONArray.class);
        assertThat(actualLabels).containsExactlyElementsOf(expectedLabels);

        assertThat(actualJson.read("$.datasets[*]", JSONArray.class)).hasSize(1);

        JSONArray actualSizes = actualJson.read("$.datasets[0].data", JSONArray.class);
        assertThat(actualSizes).containsExactlyElementsOf(expectedSizes);

        JSONArray backgroundColors = actualJson.read("$.datasets[0].backgroundColor[*]", JSONArray.class);
        assertThat(backgroundColors).hasSize(expectedSizes.size());

        JSONArray borderColors = actualJson.read("$.datasets[0].borderColor[*]", JSONArray.class);
        assertThat(borderColors).hasSize(expectedSizes.size());
    }
}
