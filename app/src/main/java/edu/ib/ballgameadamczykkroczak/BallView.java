package edu.ib.ballgameadamczykkroczak;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class BallView extends View {

    private Paint ballPaint;
    private static int CIRLE_RADIUS = 40;
    private int x;
    private int y;
    private int viewWidth; // szerokość ekranu
    private int viewHeight; // wysokość ekranu
    private long lastTime;

    public BallView(Context context) {
        super(context); // wywołanie kostruktora klasy bazowej
        x = 100;
        y = 100;
        ballPaint = new Paint();
        ballPaint.setColor(Color.CYAN);
    }

    // metody do wyświetlania
    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        viewWidth = w;
        viewHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(x, y, CIRLE_RADIUS, ballPaint);
        invalidate(); // odświeżenie ekranu
        // przy eskperymentowaniu z przemieszczaniem się kulki
        ballPaint.setTextSize(32);
        canvas.drawText("x = " + x + " y = " + y, viewWidth / 2, viewHeight / 2, ballPaint);
    }
}