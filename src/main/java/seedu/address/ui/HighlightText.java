package seedu.address.ui;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/** Builds text with case-insensitive prefix matches highlighted. */
public final class HighlightText {
    private HighlightText() { }

    /**
     * Creates a text flow where case-insensitive prefix matches are highlighted.
     *
     * @param value the text to display
     * @param keywords the prefixes to highlight
     * @return a text flow containing the styled text segments
     */
    public static TextFlow create(String value, List<String> keywords) {
        TextFlow flow = new TextFlow();
        addTextSegments(flow, value, markMatches(value, keywords));
        return flow;
    }

    private static boolean[] markMatches(String value, List<String> keywords) {
        boolean[] marked = new boolean[value.length()];
        for (String word : value.split("\\s+")) {
            int start = value.indexOf(word);
            for (String keyword : keywords) {
                markMatch(marked, word, start, keyword);
            }
        }
        return marked;
    }

    private static void markMatch(boolean[] marked, String word, int start, String keyword) {
        if (word.regionMatches(true, 0, keyword, 0, keyword.length())) {
            int end = Math.min(start + keyword.length(), marked.length);
            for (int i = start; i < end; i++) {
                marked[i] = true;
            }
        }
    }

    private static void addTextSegments(TextFlow flow, String value, boolean[] marked) {
        int start = 0;
        while (start < value.length()) {
            boolean highlighted = marked[start];
            int end = findSegmentEnd(marked, start, highlighted);
            flow.getChildren().add(createTextNode(value.substring(start, end), highlighted));
            start = end;
        }
    }

    private static int findSegmentEnd(boolean[] marked, int start, boolean highlighted) {
        int end = start + 1;
        while (end < marked.length && marked[end] == highlighted) {
            end++;
        }
        return end;
    }

    private static Node createTextNode(String value, boolean highlighted) {
        if (highlighted) {
            Label match = new Label(value);
            match.setTextFill(Color.WHITE);
            match.getStyleClass().add("search-match");
            return match;
        } else {
            Text normal = new Text(value);
            normal.setFill(Color.WHITE);
            return normal;
        }
    }
}
