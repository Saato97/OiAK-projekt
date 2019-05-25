package com.example.oiak;

import android.app.ActivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oiak.GeneticAlgorithm2.City;
import com.example.oiak.GeneticAlgorithm2.GeneticAlg;
import com.example.oiak.GeneticAlgorithm2.Population;
import com.example.oiak.GeneticAlgorithm2.Route;

import java.util.Random;
import java.lang.Math;


public class MainActivity extends AppCompatActivity {

    int popSize, citySize, maxGenNum;
    EditText citySizeInput;
    EditText populationSizeInput;
    EditText maxGenerationNumberInput;
    Button startButton;
    Button arrayListStartButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        int memoryClass = am.getMemoryClass();
        int heapSize = am.getLargeMemoryClass();
        long maxMemory = Runtime.getRuntime().maxMemory();
        Log.v("onCreate", "memoryClass:" + memoryClass);
        Log.v("onCreate", "largeMemoryClass:" + heapSize);
        Log.v("onCreate", "maxMemory:" + maxMemory);
        displayHeapSize(heapSize);

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

        arrayListStartButton = (Button) findViewById(R.id.startButton2);
        arrayListStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Start Algorytmu Genetycznego v2", Toast.LENGTH_SHORT).show();
                popSize = Integer.valueOf(populationSizeInput.getText().toString());
                citySize = Integer.valueOf(citySizeInput.getText().toString());
                maxGenNum = Integer.valueOf(maxGenerationNumberInput.getText().toString());
                GeneticAlgorithmActivity2 mGenActivity2 = new GeneticAlgorithmActivity2();
                mGenActivity2.execute(citySize, popSize, maxGenNum);
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

    public void displayAnswer(String message) {
        TextView mTextView = (TextView) findViewById(R.id.bestDistance);
        mTextView.setText(message);
    }

    public void displayHeapSize(int heapSize) {
        TextView mTextView = (TextView) findViewById(R.id.heapSizeValue);
        mTextView.setText(String.valueOf(heapSize));
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

    public class GeneticAlgorithmActivity2 extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... integers) {
            int nrOfPoints = integers[0];
            int nrOfPopulation = integers[1];
            int nrOfGenerations = integers[2];

            // Create cities

            City cities[] = new City[nrOfPoints];

            // Loop to create random cities
            for (int cityIndex = 0; cityIndex < nrOfPoints; cityIndex++) {
                // Generate x,y position
                int xPos = new Random().nextInt(200);
                int yPos = new Random().nextInt(200);

                // Add city
                cities[cityIndex] = new City(xPos, yPos);
            }

            // Initial GA
            GeneticAlg ga = new GeneticAlg(nrOfPopulation, 0.001, 0.9, 2, 5);

            // Initialize population
            Population population = ga.initPopulation(cities.length);

            // Evaluate population
            ga.evalPopulation(population, cities);

            Route startRoute = new Route(population.getFittest(0), cities);
            System.out.println("Start Distance: " + startRoute.getDistance());

            // Keep track of current generation
            int generation = 1;
            // Start evolution loop
            while (!ga.isTerminationConditionMet(generation, nrOfGenerations)) {
                // Print fittest individual from population
                Route route = new Route(population.getFittest(0), cities);
                System.out.println("G"+generation+" Best distance: " + route.getDistance());

                // Apply crossover
                population = ga.crossoverPopulation(population);

                // Apply mutation
                population = ga.mutatePopulation(population);

                // Evaluate population
                ga.evalPopulation(population, cities);

                // Increment the current generation
                generation++;
            }

            Route route = new Route(population.getFittest(0), cities);
            double bestDist = route.getDistance();
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

