/*
@ASSESSME.USERID: kk238
@ASSESSME.AUTHOR: Kiril
@ASSESSME.LANGUAGE: JAVA
@ASSESSME.DESCRIPTION: ASS91
@ASSESSME.ANALYZE: YES
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Displays data in a table format allowing the user to sort the table by the
 * data in any column by clicking on the column header.
 */
public class Filters extends Application {

    private List<String[]> data;
    private List<List<Label>> labels;

    @Override
    public void start(Stage stage) throws Exception {
        // The filename will be passed through as a command line parameter
        List<String> args = getParameters().getRaw();
        String filename = args.get(0);

        GridPane pane = new GridPane();
        data = new ArrayList<>();
        labels = new ArrayList<>();

        // Read data from file using streams
        try (BufferedReader fin = new BufferedReader(new FileReader(filename))) {
            // Use the header to create the first row as buttons
            String[] header = fin.readLine().strip().split(",");
            for (int col = 0; col < header.length; col++) {
                Button button = new Button(header[col]);
                int columnIndex = col; // Needed to use in lambda
                button.setOnAction(e -> sortDataByColumn(columnIndex));
                pane.add(button, col, 0);
            }

            // Use the rest of the data to fill in the labels
            data = fin.lines()
                      .map(line -> line.strip().split(","))
                      .collect(Collectors.toList());

            for (int row = 0; row < data.size(); row++) {
                labels.add(new ArrayList<>());
                for (int col = 0; col < data.get(row).length; col++) {
                    Label label = new Label(data.get(row)[col]);
                    labels.get(row).add(label);
                    pane.add(label, col, row + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // If the data is too big, add scroll bars
        ScrollPane scroller = new ScrollPane(pane);
        scroller.setMaxSize(1000, 600);
        Scene scene = new Scene(scroller);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sort the data by the specified column index and update the labels.
     * 
     * @param columnIndex the index of the column to sort by
     */
    private void sortDataByColumn(int columnIndex) {
        data.sort((a, b) -> a[columnIndex].compareTo(b[columnIndex]));
        update();
    }

    /**
     * Helper function used to update all the labels based on the
     * data. It should be called whenever the data changes.
     */
    private void update() {
        for (int row = 0; row < data.size(); row++) {
            for (int col = 0; col < data.get(row).length; col++) {
                labels.get(row).get(col).setText(data.get(row)[col]);
            }
        }
    }

    public static void main(String[] args) {
        // Example of hard coding the args, useful for debugging but
        // should be removed to test using command line arguments.
        args = new String[] { "data/grades_010.csv" };
        launch(args);
    }
}
