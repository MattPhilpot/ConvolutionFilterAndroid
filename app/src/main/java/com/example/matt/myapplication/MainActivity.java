package com.example.matt.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import java.nio.IntBuffer;

public class MainActivity extends AppCompatActivity {

    ImageView testImage;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.test_brody_image);
        testImage = (ImageView)findViewById(R.id.testView);
        testImage.setImageBitmap(bitmap);

        Button but = (Button)findViewById(R.id.testbutton);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bit2 = laplace(bitmap, true, LaplacianOfGaussian);
                //Bitmap bit2 = enhanceBitmap(bitmap, 3);
                testImage.setImageBitmap(bit2);
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


    public static double[][] Laplacian3x3 = {
                                                { -1, -1, -1  },
                                                { -1,  8, -1  },
                                                { -1, -1, -1  },
                                            };

    public static double[][] LaplacianOfGaussian = {
                                                        {  0,   0, -1,  0,  0 },
                                                        {  0,  -1, -2, -1,  0 },
                                                        { -1,  -2, 16, -2, -1 },
                                                        {  0,  -1, -2, -1,  0 },
                                                        {  0,   0, -1,  0,  0 },
                                                    };

    public static double[][] Laplacian5x5 =  { { -1, -1, -1, -1, -1, },
            { -1, -1, -1, -1, -1, },
            { -1, -1, 24, -1, -1, },
            { -1, -1, -1, -1, -1, },
            { -1, -1, -1, -1, -1  } };

    public static double[][] Laplacian7x7 =  {
                                                { -1, -1, -1, -1, -1, -1, -1 },
                                                { -1, -1, -1, -1, -1, -1, -1 },
                                                { -1, -1, -1, -1, -1, -1, -1 },
                                                { -1, -1, -1, 48, -1, -1, -1 },
                                                { -1, -1, -1, -1, -1, -1, -1 },
                                                { -1, -1, -1, -1, -1, -1, -1 },
                                                { -1, -1, -1, -1, -1, -1, -1  } };


    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    public Bitmap enhanceBitmap(Bitmap bmp, float contrast)
    {
        bmp = toGrayscale(bmp);
        ColorMatrix cm = new ColorMatrix();

        float scale = contrast + 1.f;
        float translate = (-.5f * scale + .5f) * 255.f;
        cm.set(new float[] {
                scale, 0, 0, 0, translate,
                0, scale, 0, 0, translate,
                0, 0, scale, 0, translate,
                0, 0, 0, 1, 0 });


        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        Canvas canvas = new Canvas(ret);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);

        return ret;
    }


    public Bitmap laplace(Bitmap bmp, boolean grayscale, double[][] filterMatrix)
    {
        int bitmapHeight = bmp.getHeight();
        int bitmapWidth = bmp.getWidth();

        int[] pixelBuffer = new int[bitmapHeight * bitmapWidth];
        bmp.getPixels(pixelBuffer, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);
        int[] resultBuffer = pixelBuffer.clone();

        if (grayscale)
        {
            float rgb = 0;

            for (int k = 0; k < pixelBuffer.length; k++)
            {
                rgb = Color.red(pixelBuffer[k]) * 0.299f;
                rgb += Color.green(pixelBuffer[k]) * 0.587f;
                rgb += Color.green(pixelBuffer[k]) * 0.114f;
                pixelBuffer[k] = Color.argb(255, (int)rgb, (int)rgb, (int)rgb);
            }
        }


        double blue = 0.0;
        double green = 0.0;
        double red = 0.0;

        int filterWidth = 5;
        //int filterHeight = filterDimensions;

        int filterOffset = (filterWidth - 1) / 2;
        int calcOffset = 0;
        int byteOffset = 0;

        for (int offsetY = filterOffset; offsetY < bitmapHeight - filterOffset; offsetY++)
        {
            for (int offsetX = filterOffset; offsetX < bitmapWidth - filterOffset; offsetX++)
            {
                blue = 0;
                green = 0;
                red = 0;

                byteOffset = offsetY * bitmapWidth + offsetX;

                for (int filterY = -filterOffset; filterY <= filterOffset; filterY++)
                {
                    for (int filterX = -filterOffset; filterX <= filterOffset; filterX++)
                    {
                        calcOffset = byteOffset + filterX + (filterY * bitmapWidth);
                        red += (double)(Color.red(pixelBuffer[calcOffset])) * filterMatrix[filterY + filterOffset][filterX + filterOffset];
                        green += (double)(Color.green(pixelBuffer[calcOffset])) * filterMatrix[filterY + filterOffset][filterX + filterOffset];
                        blue += (double)(Color.blue(pixelBuffer[calcOffset])) * filterMatrix[filterY + filterOffset][filterX + filterOffset];
                    }
                }

                if (blue > 255) blue = 255;
                else if (blue < 0) blue = 0;

                if (green > 255) green = 255;
                else if (green < 0) green = 0;

                if (red > 255) red = 255;
                else if (red < 0) red = 0;

                resultBuffer[byteOffset++] = Color.argb(255, (int)red, (int)green, (int)blue);
            }
        }

        bitmap.copyPixelsFromBuffer(IntBuffer.wrap(resultBuffer));
        return bmp;
    }


    private static Bitmap applyConvolutionFilter(Bitmap bmp, int[][] filterMatrix, int filterDimensions, double factor) {
        int bitmapHeight = bmp.getHeight();
        int bitmapWidth = bmp.getWidth();
        int[] pixelBuffer = new int[bitmapHeight * bitmapWidth];
        bmp.getPixels(pixelBuffer, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);
        int[] resultBuffer = new int[pixelBuffer.length];

        int blue;
        int green;
        int red;

        int filterOffset = (filterDimensions - 1) / 2;
        int byteOffset;
        int currentPixel;
        int currentFilter;

        /*
        if (grayscale) {
            float rgb = 0;

            for (int k = 0; k < pixelBuffer.length; k++) {
                currentPixel = pixelBuffer[k];
                rgb = ((currentPixel & 0x00FF0000) >> 16) * 0.299f; //red
                rgb += ((currentPixel & 0x0000FF00) >> 8) * 0.587f; //green
                rgb += (currentPixel & 0x000000FF) * 0.114f; //blue
                pixelBuffer[k] = ((int)rgb) + ((int)rgb << 8) + ((int)rgb << 16) + (255 << 24);
            }
        }
        */

        for (int offsetY = filterOffset; offsetY < bitmapHeight - filterOffset; offsetY++) {
            for (int offsetX = filterOffset; offsetX < bitmapWidth - filterOffset; offsetX++) {
                red = 0;
                green = 0;
                blue = 0;

                byteOffset = offsetY * bitmapWidth + offsetX;

                for (int filterY = -filterOffset; filterY <= filterOffset; filterY++) {
                    for (int filterX = -filterOffset; filterX <= filterOffset; filterX++) {
                        currentPixel = pixelBuffer[byteOffset + filterX + (filterY * bitmapWidth)];
                        currentFilter = filterMatrix[filterY + filterOffset][filterX + filterOffset];

                        red += ((currentPixel & 0x00FF0000) >> 16) * currentFilter;
                        green += ((currentPixel & 0x0000FF00) >> 8) * currentFilter;
                        blue += (currentPixel & 0x000000FF) * currentFilter;
                    }
                }

                red *= factor;
                blue *= factor;
                green *= factor;

                if (blue > 255) blue = 255;
                else if (blue < 0) blue = 0;

                if (green > 255) green = 255;
                else if (green < 0) green = 0;

                if (red > 255) red = 255;
                else if (red < 0) red = 0;

                //noinspection UnusedAssignment
                resultBuffer[byteOffset++] = (blue) + (green << 8) + (red << 16) + (255 << 24);
            }
        }

        bmp.copyPixelsFromBuffer(IntBuffer.wrap(resultBuffer));
        return bmp;
    }

    private static Bitmap applyConvolutionFilter(Bitmap bmp, int[][] xFilterMatrix, int[][] yFilterMatrix, int filterDimensions) {
        int bitmapHeight = bmp.getHeight();
        int bitmapWidth = bmp.getWidth();
        int[] pixelBuffer = new int[bitmapHeight * bitmapWidth];
        bmp.getPixels(pixelBuffer, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);
        int[] resultBuffer = new int[pixelBuffer.length];

        int blueX;
        int greenX;
        int redX;

        int blueY;
        int greenY;
        int redY;

        int blueTotal;
        int greenTotal;
        int redTotal;

        int filterOffset = (filterDimensions - 1) / 2;
        int byteOffset;
        int currentPixel;
        int xCurrentFilter;
        int yCurrentFilter;

        /*
        if (grayscale){
            float rgb = 0;

            for (int k = 0; k < pixelBuffer.length; k++){
                currentPixel = pixelBuffer[k];
                rgb = ((currentPixel & 0x00FF0000) >> 16) * 0.299f; //red
                rgb += ((currentPixel & 0x0000FF00) >> 8) * 0.587f; //green
                rgb += (currentPixel & 0x000000FF) * 0.114f; //blue
                pixelBuffer[k] = ((int)rgb) + ((int)rgb << 8) + ((int)rgb << 16) + (255 << 24);
            }
        }
        */

        for (int offsetY = filterOffset; offsetY < bitmapHeight - filterOffset; offsetY++) {
            for (int offsetX = filterOffset; offsetX < bitmapWidth - filterOffset; offsetX++) {
                redX = greenX = blueX = 0;
                redY = greenY = blueY = 0;
                //noinspection UnusedAssignment
                redTotal = greenTotal = blueTotal = 0;

                byteOffset = offsetY * bitmapWidth + offsetX;

                for (int filterY = -filterOffset; filterY <= filterOffset; filterY++) {
                    for (int filterX = -filterOffset; filterX <= filterOffset; filterX++) {
                        currentPixel = pixelBuffer[byteOffset + filterX + (filterY * bitmapWidth)];
                        xCurrentFilter = xFilterMatrix[filterY + filterOffset][filterX + filterOffset];
                        yCurrentFilter = yFilterMatrix[filterY + filterOffset][filterX + filterOffset];

                        redX += ((currentPixel & 0x00FF0000) >> 16) * xCurrentFilter;
                        greenX += ((currentPixel & 0x0000FF00) >> 8) * xCurrentFilter;
                        blueX += (currentPixel & 0x000000FF) * xCurrentFilter;

                        redY += ((currentPixel & 0x00FF0000) >> 16) * yCurrentFilter;
                        greenY += ((currentPixel & 0x0000FF00) >> 8) * yCurrentFilter;
                        blueY += (currentPixel & 0x000000FF) * yCurrentFilter;
                    }
                }

                blueTotal = (int)Math.sqrt((blueX * blueX) + (blueY * blueY));
                greenTotal = (int)Math.sqrt((greenX * greenX) + (greenY * greenY));
                redTotal = (int)Math.sqrt((redX * redX) + (redY * redY));

                if (blueTotal > 255) blueTotal = 255;
                else if (blueTotal < 0) blueTotal = 0;

                if (greenTotal > 255) greenTotal = 255;
                else if (greenTotal < 0) greenTotal = 0;

                if (redTotal > 255) redTotal = 255;
                else if (redTotal < 0) redTotal = 0;

                //noinspection UnusedAssignment
                resultBuffer[byteOffset++] = (blueTotal) + (greenTotal << 8) + (redTotal << 16) + (255 << 24);
            }
        }

        bmp.copyPixelsFromBuffer(IntBuffer.wrap(resultBuffer));
        return bmp;
    }
}
