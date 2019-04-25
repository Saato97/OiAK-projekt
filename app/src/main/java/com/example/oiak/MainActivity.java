package com.example.oiak;

import android.app.ActivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.lang.Math;


public class MainActivity extends AppCompatActivity {

    int popSize, citySize, maxGenNum;
    EditText citySizeInput;
    EditText populationSizeInput;
    EditText maxGenerationNumberInput;
    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        citySizeInput = (EditText) findViewById(R.id.citySizeEdit);
        populationSizeInput = (EditText) findViewById(R.id.populationSizeEdit);
        maxGenerationNumberInput = (EditText) findViewById(R.id.maxGenNumber);
        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Start Algorytmu Genetycznego", Toast.LENGTH_SHORT).show();
                popSize = Integer.valueOf(populationSizeInput.getText().toString());
                citySize = Integer.valueOf(citySizeInput.getText().toString());
                maxGenNum = Integer.valueOf(maxGenerationNumberInput.getText().toString());
                GeneticAlgorithmActivity mGenActivity = new GeneticAlgorithmActivity();
                mGenActivity.execute(citySize, popSize, maxGenNum);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // Get a MemoryInfo object for the device's current memory status.
    private ActivityManager.MemoryInfo getAvailableMemory() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }

    public void displayAnswer(String message){
        TextView mTextView = (TextView) findViewById(R.id.bestDistance);
        mTextView.setText(message);
    }

    public class GeneticAlgorithmActivity extends AsyncTask<Integer, Void, String> {


        private float[][] getDist(Point[] points) {
            float[][] dist = new float[points.length][points.length];
            for (int i = 0; i < points.length; i++) {
                for (int j = 0; j < points.length; j++) {
                    dist[i][j] = distance(points[i], points[j]);
                }
            }
            return dist;
        }

        private float distance(Point p1, Point p2) {
            return (float) Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
        }


        @Override
        protected String doInBackground(Integer... integers) {
            int nrOfPoints = integers[0];
            int nrOfPopulation = integers[1];
            int nrOfGenerations = integers[2];
            Point[] points = new Point[nrOfPoints];
            for (int i = 0; i < points.length; i++) {
                points[i] = new Point();
                points[i].x = new Random().nextInt(200);
                points[i].y = new Random().nextInt(200);
            }

            int[] best;

            GeneticAlgorithm ga = GeneticAlgorithm.getInstance();
            ga.setPopulationSize(nrOfPopulation);
            ga.setMaxGeneration(nrOfGenerations);
            ga.setAutoNextGeneration(true);
            best = ga.tsp(getDist(points));
            //System.out.print("best path:");
            //for (int aBest : best) {
            //    System.out.print(aBest + " ");
            //}
            //System.out.println();
            float bestDist = ga.getBestDist();
            return String.valueOf(bestDist);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            displayAnswer(s);
            Toast.makeText(MainActivity.this, "Koniec Algorytmu Genetycznego", Toast.LENGTH_SHORT).show();
        }
    }

}

