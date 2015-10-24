package cool.apps.reed.threadingapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/********
 * Main Class and Main Page of the app
 *
 * This app demonstrates threading by reading and writing to
 * an internal file and displaying a progress bar.
 * Reading and writing to the file is done when buttons are pressed.
 * The file content is displayed on the ListView.
 * A clear button removes the writing from the screen
 *
 * @author Reead Atwood
 * @colaborator Landon Jamieson and Israel Carrera
 */
public class MainPage extends AppCompatActivity {

    // filename doesn't need to change ever
    public final String FILE_NAME = "numbers.txt";

    // needed to have the right scope
    private ArrayAdapter<Integer> arrayAdapter;
    private ProgressBar pBar = null;
    private ListView loadView;

    /*
    * onCreate
    * linked the ListView and called it loadView
    * linked ProgressBar and called it pBar and set up its size
    *    and visibility
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        // get the listView from the xml screen
        loadView = (ListView)findViewById(R.id.listView);

        // set up the size and visibility of the progress bar
        pBar = (ProgressBar)findViewById(R.id.progressBar);
        pBar.setVisibility(View.VISIBLE);
        pBar.setMax(10);
    }

    /*
    * Starts the CreateThread and links to the Create button
    * @param view
    */
    public void clickCreate(View view){
        new CreateThread().execute();
    }

    /*
    * Starts the LoadThread and links to the Load button
    * @param view
    */
    public void clickLoad(View view) {
        new LoadThread().execute();
    }

    /*
    * Clears the ArrayAdapter and links to the Clear button
    * @param view
    */
    public void clickClear(View view) {
        // check to ensure that there is content to clear
        if (arrayAdapter != null) {
            arrayAdapter.clear();
        }
    }

    /***
     * CreateThread extends AsyncTask
     * Creates a thread to write 10 numbers to a file, displays a progress bar
     *   and sleeps for 250 milliseconds to simulate a harder task
     *
     * Implements doInBackground() and onProgressUpdate()
     *
     * @param "Void" background doesn't need anything
     * @param "Integer" to display the progress
     * @param "Void" does nothing
     */
    class CreateThread extends AsyncTask<Void, Integer, Void> {

        /*
         * Creates a thread to write the numbers 1 - 10 to a file
         * publish the progress
         * sleeps for 250 milliseconds to simulate a harder task
         *
         * @throws IOException and InterruptedException
         *
         * @param "Void" background doesn't need anything
         */
        protected Void doInBackground(Void...blank) {

            // create a FileWriter to write from the file
            try (FileWriter fileWriter = new FileWriter(new File(getFilesDir(), FILE_NAME))) {

                // add the numbers 1 - 10 to a file
                for (int i = 1; i <= 10; i++) {
                    fileWriter.write(i + System.getProperty("line.separator"));

                    // increment the progress
                    publishProgress(i);

                    // go to sleep for 250 milliseconds or .25 seconds
                    Thread.sleep(250);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                System.out.println("you will never get here unless you broke it bad");
            }

            // needed for the Void object
            return null;
        }

        /*
         * Displays the progress of the file being written
         *
         * @param "Integer"
         */
        protected void onProgressUpdate(Integer... progress) {
            pBar.setProgress(progress[0]);
        }

    }

    /***
     * LoadThread extends AsyncTask
     * Loads a thread to read from a file, displays a progress bar,
     *   sleeps for 250 milliseconds to simulate a harder task, and
     *   display the contents to the ListView
     *
     * Implements doInBackground(), onProgressUpdate(), and onPostExecute()
     *
     * @param "Void" background doesn't need anything
     * @param "Integer" to display the progress
     * @param "ArrayAdapter<Integer>" displays the contents of the file with an adapter
     */
    class LoadThread extends AsyncTask<Void, Integer, ArrayAdapter<Integer>> {

        /*
         * Creates a thread to read the contents of the file
         * publish the progress
         * sleeps for 250 milliseconds to simulate a harder task
         * Returns arrayAdapter to display the contents
         *
         * @throws IOException and InterruptedException
         *
         * @param "Void" background doesn't need anything
         */
        protected ArrayAdapter<Integer> doInBackground(Void...blank) {

            // create a FileWriter to write from the file
            try (Scanner scanner = new Scanner(new File(getFilesDir(), FILE_NAME))) {

                // create a ArrayList to hold the contents of the file
                ArrayList<Integer>intList = new ArrayList<>();

                // for the progress bar
                int i = 0;

                // read in the file with a scanner
                while (scanner.hasNextInt()){

                    // add each one to the list
                    intList.add(scanner.nextInt());

                    // increment the progress and publish it
                    i++;
                    publishProgress(i);

                    // go to sleep for 250 milliseconds or .25 seconds
                    Thread.sleep(250);
                }

                // attach the arrayList to the arrayAdapter, choose the layout from a list that android has
                arrayAdapter = new ArrayAdapter<>(MainPage.this, android.R.layout.simple_spinner_item, intList);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                System.out.println("you will never get here unless you broke it bad");
            }

            // for displaying the contents
            return arrayAdapter;
        }

        /*
         * Displays the progress of the file being read
         *
         * @param "Integer"
         */
        protected void onProgressUpdate(Integer... progress) {
            pBar.setProgress(progress[0]);
        }

        /*
         * Sets the ArrayAdapter to the ListView for displaying the contents of the file
         *
         * @param "ArrayAdapter<Integer>" displays the contents of the file with an adapter
         */
        protected void onPostExecute(ArrayAdapter<Integer> nothing) {
            // set the arrayAdapter to the ListView
            loadView.setAdapter(nothing);
        }
    }
}