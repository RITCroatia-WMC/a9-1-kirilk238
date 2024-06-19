/*
@ASSESSME.USERID: kk238
@ASSESSME.AUTHOR: Kiril
@ASSESSME.LANGUAGE: JAVA
@ASSESSME.DESCRIPTION: ASS91
@ASSESSME.ANALYZE: YES
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

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
    private List<List<Label>> lables;

    @Override
    public void start(Stage stage) throws Exception {
        // The filename will be passed through as a command line parameter
        List<String> args = getParameters().getRaw();
        FileReader file = new FileReader(args.get(0));
        BufferedReader fin = new BufferedReader(file);
        int row = 0;
        int col = 0;

        // If the data is too big, add scroll bars
        ScrollPane scroller = new ScrollPane();
        scroller.setMaxSize(1000, 600);

        GridPane pane = new GridPane();
        data = new ArrayList<>();
        lables = new ArrayList<>();

        // Use the header to create the first row as buttons.
        String[] header = fin.readLine().strip().split(",");
        for (String value : header) {
            Button button = new Button(value);

            pane.add(button, col, row);
            col++;
        }
        row++;

        // Use the rest of the data to fill in the labels.
        String line = fin.readLine();
        while (line != null) {
            String[] record = line.strip().split(",");
            // Store all the data in a list so it can be easily sorted
            // later on (List.sort)
            data.add(record);
            col = 0;
            lables.add(new ArrayList<>());
            for (String value : record) {
                Label label = new Label(value);
                // Keep track of all the labels so they can be adjusted without
                // haveing to find them in the Grid which can be a pain.
                lables.get(row - 1).add(label);
                pane.add(label, col, row);
                col++;
            }
            row++;
            line = fin.readLine();
        }
        fin.close();

        scroller.setContent(pane);
        Scene scene = new Scene(scroller);
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Helper funciton used to update all the labels based on the
     * data. It should be called whenever the data changes.
     */
    private void update() {
        int row = 0;
        for (List<Label> label_row : lables) {
            int col = 0;
            for (Label label : label_row) {
                label.setText(data.get(row)[col]);
                col++;
            }
            row++;
        }
    }

    public static void main(String[] args) {
        // Example of hard coding the args, useful for debugging but
        // should be removed to test using command line arguments.
        args = new String[] { "data/grades_010.csv" };
        launch(args);
    }

}
