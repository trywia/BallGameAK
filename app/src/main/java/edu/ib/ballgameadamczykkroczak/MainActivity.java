package edu.ib.ballgameadamczykkroczak;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private RectangleView gameView;
    private static float NSS = (float) Math.pow(10, -9); // konwersja na sekundy
    private int[] a = new int[18];   // długość boku przeszkody
    private int[] b = new int[18];   // długość boku przeszkody
    private int[] x = new int[18]; // położenie przeszkody
    private int[] y = new int[18];
    private float vx = 0;
    private float vy = 0;
    private int px = 50;
    private int py = 50;

    private static final String TAG = "EDUIB"; // tag

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // chęć korzystania z czujników sprzętowych
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // korzystanie z akcelerometru - przyspieszenie telefonu, wraz ze składową przyspieszenia ziemskiego

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        boolean flaga = false;
        do { //pętla ustalająca współrzędne pierwszego elementu labiryntu
            int r = (int) (Math.random() * 2);

            if (r == 1) { //losowa orientacja prostokątnej przeszkody
                a[0] = (int) (Math.random() * (500 - 250) + 250); //długości boków przeszkody
                b[0] = 50;
            } else {
                a[0] = 50;
                b[0] = (int) (Math.random() * (500 - 250) + 250);
            }

            x[0] = (int) (Math.random() * (width - 50) + 50); //grubość
            y[0] = (int) (Math.random() * (height - 50) + 50);
            if ((x[0] + a[0] <= width - 50) && (y[0] + b[0] <= height - 50)) { // sprawdzenie, czy przeszkoda znajduje się w odpowiednim obszarze
                flaga = true;
            }
        } while (flaga == false);

        for (int i = 1; i < x.length; i++) { //pętla tworząca przeszkody/labirynt pacmana
            int r = (int) (Math.random() * 2);

            if (r == 1) { //losowa orientacja prostokątnych przeszkód
                a[i] = (int) (Math.random() * (500 - 250) + 250); //długości boków przeszkód
                b[i] = 50;
            } else {
                a[i] = 50;
                b[i] = (int) (Math.random() * (500 - 250) + 250);
            }

            x[i] = (int) (Math.random() * (width - 50) + 50); //współrzędne przeszkody
            y[i] = (int) (Math.random() * (width - 50) + 50);

            for (int j = 0; j < i; j++) { //pętla zagnieżdżona niepozwalająca na nachodzenie na siebie przeszkód i na utrzymanie ich w oknie graficznym
                if ((x[i] + a[i] >= x[j] - 50) && (x[i] <= x[j] + 50 + a[j]) && (y[i] + b[i] >= y[j] - 50) && (y[i] <= y[j] + 50 + b[j]) || (x[i] + a[i] >= width - 50) || (y[i] + b[i] >= height - 50)) {
                    i--;
                    break;
                }
            }
        }


        gameView = new RectangleView(this, a, b, x, y);
        // setContentView(gameView);


        RectangleView[] rectTable = new RectangleView[a.length];

        for (int i = 0; i < a.length; i++) {
            //rectTable[i] = new RectangleView(this, a, b, x, y);
            rectTable[i] = gameView;
            setContentView(rectTable[i]);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        // odczytanie bieżących wskazań


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        float timeStamp = event.timestamp; // zwracanie czasu pomiaru, ns * 10^-9
        float actualTime = timeStamp * NSS;

        //vx = 0;
        //vy = 1;


        float[] values = event.values;
        float ax = values[0];
        float ay = values[1];
        Log.d(TAG, String.valueOf(ax + " " + ay));

        for (int i = 0; i < x.length; i++) { //kolizje pacmana z przeszkodami
            if (gameView.getBallX() + 15 >= x[i] && gameView.getBallX() - 15 <= x[i] + a[i] && gameView.getBallY() + 15 >= y[i] && gameView.getBallY() - 15 <= y[i] + b[i]) {
                vx = 0; //-vx * (float) 1.5; // -vx * 4;
                vy = 0; //-vy * (float) 1.5; // -vy * 4;
                gameView.setBallX(px);
                gameView.setBallY(py);
            }
        }
        if (gameView.getBallX() < 15 || gameView.getBallX() > width - 15) { //kolizje pacmana z brzegami ekranu
            vx = 0; //-vx * (float) 1.5;
            gameView.setBallX(px);
        }
        if (gameView.getBallY() < 15 || gameView.getBallY() > height - 15) { //kolizje pacmana z brzegami ekranu
            vy = 0; //-vy * (float) 1.5;
            gameView.setBallY(py);
        }

        gameView.setBallX(gameView.getBallX() + (int) vx); //ruch pacmana
        gameView.setBallY(gameView.getBallX() + (int) vy);

        vx += ax / 200;
        vy += ay / 200;

        px = gameView.getBallX();
        py = gameView.getBallY();

        RectangleView[] rectTable = new RectangleView[a.length];

        for (int i = 0; i < a.length; i++) {
            //rectTable[i] = new RectangleView(this, a, b, x, y);
            rectTable[i] = gameView;
            setContentView(rectTable[i]);
        }
        //setContentView(gameView);

    }

    // metody decydujące o tym, że nie sprawdzamy czujników, kiedy aplikacja nie jest użytkowana
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME); // zczytujemy wskazania akcelerometru
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this); // stopujemy wskazania
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}