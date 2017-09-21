package com.pogiba.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class CircleButtonView extends View implements View.OnTouchListener {
  public static String TAG = "CircleButtonView";

  private Paint circlePaint;
  private Paint linePaint;

  private float centerX;
  private float centerY;
  private float radius;

  public CircleButtonView(Context context) {
    this(context, null, 0);
  }

  public CircleButtonView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CircleButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initPaints();
    setOnTouchListener(this);
  }

  private void initPaints() {
    circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    circlePaint.setStyle(Paint.Style.STROKE);
    circlePaint.setStrokeWidth(getStrokeWidth());
    circlePaint.setColor(Color.GRAY);

    linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    linePaint.setStyle(Paint.Style.FILL);
    linePaint.setStrokeWidth(getStrokeWidth() / 3);
    linePaint.setColor(Color.GRAY);
  }

  private float getStrokeWidth() {
    return 5 * getResources().getDisplayMetrics().density;
  }

  @Override
  public boolean onTouch(View v, MotionEvent event) {
    float pointX = event.getX();
    float pointY = event.getY();
    // Checks for the event that occurs
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        String btn = identifyButton(pointX, pointY);
        Toast.makeText(getContext(), btn, Toast.LENGTH_SHORT).show();
        //Toast.makeText(getContext(), "angle: " + angle +  "radius: " + radius + ", distance: " + distanceFromCenter, Toast.LENGTH_SHORT).show();
        //Toast.makeText(getContext(), "pointX: " + pointX + ", pointY: " + pointY, Toast.LENGTH_SHORT).show();
        return true;
      case MotionEvent.ACTION_MOVE:
        //Toast.makeText(getContext(), "pointX: " + pointX + ", pointY: " + pointY, Toast.LENGTH_SHORT).show();
        break;
      default:
        return false;
    }
    // Force a view to draw again
    postInvalidate();
    return true;
  }

  @NonNull
  private String identifyButton(float pointX, float pointY) {
    double distanceFromCenter = Math.sqrt(Math.pow(centerX - pointX, 2) + Math.pow(centerY - pointY, 2));
    double angle = Math.toDegrees(Math.atan((centerY - pointY) / (centerX - pointX)));
    String btn = "outside of the button";
    if (distanceFromCenter < radius) {
      if (angle > -45 && angle < 45) {
        if (pointX > centerX) {
          btn = "right button";
        } else {
          btn = "left button";
        }
      } else {
        if (pointY > centerY) {
          btn = "bottom button";
        } else {
          btn = "top button";
        }
      }
    }
    return btn;
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int w = MeasureSpec.getSize(widthMeasureSpec);
    int h = MeasureSpec.getSize(heightMeasureSpec);

    Log.d(TAG, "onMeasure width: " + MeasureSpec.toString(widthMeasureSpec) + " height: " + MeasureSpec.toString(heightMeasureSpec));

    int size = Math.min(w, h);
    setMeasuredDimension(size, size);
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    centerX = w / 2f;
    centerY = h / 2f;
    radius = Math.min(w, h) / 3f;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    // draw circle
    canvas.drawCircle(centerX, centerY, radius, circlePaint);
    // draw diagonal lines
    canvas.drawLine((float) Math.cos(getAngleRadian(45)) * radius + centerX, (float) Math.sin(getAngleRadian(45)) * radius + centerY,
        (float) Math.cos(getAngleRadian(225)) * radius + centerX, (float) Math.sin(getAngleRadian(225)) * radius + centerY,
        linePaint);

    canvas.drawLine((float) Math.cos(getAngleRadian(135)) * radius + centerX, (float) Math.sin(getAngleRadian(135)) * radius + centerY,
        (float) Math.cos(getAngleRadian(315)) * radius + centerX, (float) Math.sin(getAngleRadian(315)) * radius + centerY,
        linePaint);

  }

  private double getAngleRadian(int alfa) {
    return alfa * Math.PI / 180;
  }
}