package com.wuggs.imgclassifier;

import android.content.Intent;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Typeface;

import android.media.ImageReader.OnImageAvailableListener;
import android.os.SystemClock;
import android.util.Size;
import android.util.TypedValue;
import android.view.Display;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.wuggs.imgclassifier.env.Logger;
import com.wuggs.imgclassifier.env.BorderedText;
import com.wuggs.imgclassifier.OverlayView.DrawCallback;

public class TakePhotoActivity extends CameraActivity implements OnImageAvailableListener {

    private static final Logger LOGGER = new Logger();

    private static final Size DESIRED_PREVIEW_SIZE = new Size(640, 480);

    private Integer sensorOrientation;
    private MSCognitiveServicesClassifier classifier;

    private BorderedText borderedText;

    @Override
    protected int getLayoutId() {
        return R.layout.camera_connection_fragment;
    }

    @Override
    protected Size getDesiredPreviewFrameSize() {
        return DESIRED_PREVIEW_SIZE;
    }

    private static final float TEXT_SIZE_DIP = 10;

    @Override
    public void onPreviewSizeChosen(final Size size, final int rotation) {
        final float textSizePx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
        borderedText = new BorderedText(textSizePx);
        borderedText.setTypeface(Typeface.MONOSPACE);

        classifier = new MSCognitiveServicesClassifier(TakePhotoActivity.this);

        previewWidth = size.getWidth();
        previewHeight = size.getHeight();

        final Display display = getWindowManager().getDefaultDisplay();
        final int screenOrientation = display.getRotation();

        LOGGER.i("Sensor orientation: %d, Screen orientation: %d", rotation, screenOrientation);

        sensorOrientation = rotation + screenOrientation;

        LOGGER.i("Initializing at size %dx%d", previewWidth, previewHeight);
        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Config.ARGB_8888);

        yuvBytes = new byte[3][];

        addCallback(
                new DrawCallback() {
                    @Override
                    public void drawCallback(final Canvas canvas) {
                        renderDebug(canvas);
                    }
                });
    }

    protected void processImageRGBbytes(int[] rgbBytes) {
        rgbFrameBitmap.setPixels(rgbBytes, 0, previewWidth, 0, 0, previewWidth, previewHeight);

        runInBackground(
                new Runnable() {
                    @Override
                    public void run() {
                        final long startTime = SystemClock.uptimeMillis();
                        Classifier.Recognition r = classifier.classifyImage(rgbFrameBitmap, sensorOrientation);
                        lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;

                        final List<Classifier.Recognition> results = new ArrayList<>();

                        if (r.getConfidence() > 0.7) {
                            results.add(r);
                        }

                        LOGGER.i("Detect: %s", results);
                        LOGGER.i("IT WORKS: %s", results);
                        if(results.toString().contains(" Apple ")) {
                            //nutritionix pozivi
                            TextView textView = (TextView) findViewById(R.id.nutrition);
                            textView.setText(R.string.Apple);
                        }
                        if(results.toString().contains(" Bread ")) {
                            TextView textView = (TextView) findViewById(R.id.nutrition);
                            textView.setText(R.string.Bread);
                        }
                        if(results.toString().contains(" Cake ")) {
                            TextView textView = (TextView) findViewById(R.id.nutrition);
                            textView.setText(R.string.Cake);
                        }
                        if(results.toString().contains(" Calamari ")) {
                            TextView textView = (TextView) findViewById(R.id.nutrition);
                            textView.setText(R.string.Calamari);
                        }
                        if(results.toString().contains(" Coffee ")) {
                            TextView textView = (TextView) findViewById(R.id.nutrition);
                            textView.setText(R.string.Coffee);
                        }
                        if(results.toString().contains(" Cupcake ")) {
                            TextView textView = (TextView) findViewById(R.id.nutrition);
                            textView.setText(R.string.Cupcake);
                        }
                        if(results.toString().contains(" Fish ")) {
                            TextView textView = (TextView) findViewById(R.id.nutrition);
                            textView.setText(R.string.Fish);
                        }
                        if(results.toString().contains(" Hamburger ")) {
                            TextView textView = (TextView) findViewById(R.id.nutrition);
                            textView.setText(R.string.Hamburger);
                        }
                        if(results.toString().contains(" Pancake ")) {
                            TextView textView = (TextView) findViewById(R.id.nutrition);
                            textView.setText(R.string.Pancake);
                        }
                        if(results.toString().contains(" Pasta ")) {
                            TextView textView = (TextView) findViewById(R.id.nutrition);
                            textView.setText(R.string.Pasta);
                        }
                        if(results.toString().contains(" Pizza ")) {
                            TextView textView = (TextView) findViewById(R.id.nutrition);
                            textView.setText(R.string.Pizza);
                        }
                        if(results.toString().contains(" Popcorn ")) {
                            TextView textView = (TextView) findViewById(R.id.nutrition);
                            textView.setText(R.string.Popcorn);
                        }
                        if(results.toString().contains(" Salad ")) {
                            TextView textView = (TextView) findViewById(R.id.nutrition);
                            textView.setText(R.string.Salad);
                        }
                        if(results.toString().contains(" Steak ")) {
                            TextView textView = (TextView) findViewById(R.id.nutrition);
                            textView.setText(R.string.Steak);
                        }
                        if(results.toString().contains(" Banana ")) {
                            TextView textView = (TextView) findViewById(R.id.nutrition);
                            textView.setText(R.string.Banana);
                        }
                        if(results.toString().contains(" Avocado ")) {
                            TextView textView = (TextView) findViewById(R.id.nutrition);
                            textView.setText(R.string.Avocado);
                        }

                        if (resultsView == null) {
                            resultsView = findViewById(R.id.results);
                        }
                        resultsView.setResults(results);
                        requestRender();
                        computing = false;
                        if (postInferenceCallback != null) {
                            postInferenceCallback.run();
                        }
                    }
                });

    }

    @Override
    public void onSetDebug(boolean debug) {
    }

    private void renderDebug(final Canvas canvas) {
        if (!isDebug()) {
            return;
        }

        final Vector<String> lines = new Vector<String>();
        lines.add("Inference time: " + lastProcessingTimeMs + "ms");
        borderedText.drawLines(canvas, 10, canvas.getHeight() - 10, lines);
    }
}
