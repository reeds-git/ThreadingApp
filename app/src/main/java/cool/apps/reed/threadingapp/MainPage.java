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

public class MainPage extends AppCompatActivity {

    public final String FILE_NAME = "numbers.txt";
    private ArrayAdapter<Integer> arrayAdapter;
    private ProgressBar pBar = null;
    private ListView loadView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        // get the listView from the xml screen
        loadView = (ListView)findViewById(R.id.listView);

        pBar = (ProgressBar)findViewById(R.id.progressBar);
        pBar.setVisibility(View.VISIBLE);
        pBar.setMax(10);

    }

    public void clickCreate(View view){
        new CreateThread().execute();
    }

    public void clickLoad(View view) {
        new LoadThread().execute();
    }

    /***
     * WriteAndReadTask:
     *   An AsyncTask to create a progress bar and to show what is being done
     */
    class CreateThread extends AsyncTask<View, Integer, Void> {

        protected Void doInBackground(View...view) {

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

            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            pBar.setProgress(progress[0]);
        }

    }

    class LoadThread extends AsyncTask<View, Integer, ArrayAdapter<Integer>> {

        protected ArrayAdapter<Integer> doInBackground(View...view) {

            // create a FileWriter to write from the file
            try (Scanner scanner = new Scanner(new File(getFilesDir(), FILE_NAME))) {

                ArrayList<Integer> intList;
                // create a ArrayList to hold the contents of the file
                intList = new ArrayList<>();

                // for the progress bar
                int i = 0;

                // read in the file with a scanner
                while (scanner.hasNextInt()){

                    // add each one to the list
                    intList.add(scanner.nextInt());

                    // increment the progress
                    i++;
                    publishProgress(i);

                    // go to sleep for 250 milliseconds or .25 seconds
                    Thread.sleep(250);
                }

                // attach the arrayList to the arrayAdapter, choose the layout from a list that
                //   android has
                arrayAdapter = new ArrayAdapter<>(MainPage.this, android.R.layout.simple_spinner_item, intList);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                System.out.println("you will never get here unless you broke it bad");
            }

            return arrayAdapter;
        }

        protected void onProgressUpdate(Integer... progress) {
            pBar.setProgress(progress[0]);
        }

        protected void onPostExecute(ArrayAdapter<Integer> nothing) {
            // set the arrayAdapter to the ListView
            loadView.setAdapter(nothing);
        }

    }

    public void clickClear(View view) {
        if (arrayAdapter != null) {
            arrayAdapter.clear();
        }
    }

}