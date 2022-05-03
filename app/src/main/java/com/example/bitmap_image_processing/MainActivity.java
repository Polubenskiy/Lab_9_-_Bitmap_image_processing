package com.example.bitmap_image_processing;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity
{

    ImageView imageView;
    ImageView imageView2;
    Bitmap bitmap1;
    Bitmap bitmap2;

    int width, height;
    boolean result = false;

    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AssetManager asset = getAssets();

        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText editText = new EditText(this);

        editText.setLayoutParams(new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        builder.setView(editText);

        builder.setMessage("Enter the name of the image");
        builder.setPositiveButton("ОК", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                name = editText.getText().toString();
                result = true;
                dialog.cancel();

                if (!result)
                {
                    name = "tigerlog";
                }
                name += ".png";

                InputStream stream = null;
                try
                {
                    stream = asset.open(name);
                }
                catch (IOException e)
                {
                    try
                    {
                        stream = asset.open("tigerlog.png");
                    } catch (IOException d) { }
                }

                bitmap1 = BitmapFactory.decodeStream(stream);

                try
                {
                    stream.close();
                }
                catch (IOException e) { }

                width = bitmap1.getWidth();
                height = bitmap1.getHeight();

                bitmap2 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                imageView = findViewById(R.id.imageView);
                imageView2 = findViewById(R.id.imageView2);

                imageView.setImageBitmap(bitmap1);
                imageView2.setImageBitmap(bitmap2);
            }
        });

        alert = builder.create();
        alert.show();
    }

    public void filter_copy(View v)
    {
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                int c = bitmap1.getPixel(i, j);
                bitmap2.setPixel(i, j, c);
            }
        }
    }

    public void filter_invert(View v)
    {
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                int c0 = bitmap1.getPixel(i, j);
                int r = 255 - Color.red(c0);
                int g = 255 - Color.green(c0);
                int b = 255 - Color.blue(c0);

                int c1 = Color.argb(255, r, g, b);
                bitmap2.setPixel(i, j, c1);
            }
        }
    }

    public void filter_shadesOfGrey(View v)
    {
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                int c0 = bitmap1.getPixel(i, j);
                int r = Color.red(c0);
                int g = Color.green(c0);
                int b = Color.blue(c0);

                double res = (r + g + b) * 0.33;

                int c1 = Color.argb(255, (int) res, (int) res, (int) res);
                bitmap2.setPixel(i, j, c1);
            }
        }
    }

    public void filter_blackAndWhite(View v)
    {
        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final SeekBar seekBar = new SeekBar(this);

        seekBar.setLayoutParams(new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        seekBar.setMax(255);
        builder.setView(seekBar);

        builder.setPositiveButton("ОК", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();
                BlackWhiteChange(seekBar.getProgress());
            }
        });

        alert = builder.create();
        alert.show();
    }

    public void BlackWhiteChange(int p)
    {
        filter_shadesOfGrey(new View(this));

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                int c0 = bitmap1.getPixel(i, j);
                int r = Color.red(c0);
                int g = Color.green(c0);
                int b = Color.blue(c0);

                double res = (r + g + b) * 0.33;

                int c1 = res <= p ? Color.BLACK : Color.WHITE;
                bitmap2.setPixel(i, j, c1);
            }
        }
    }

    public void filter_brightness(View v)
    {
        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final SeekBar seekBar = new SeekBar(this);

        seekBar.setLayoutParams
                (new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        seekBar.setMax(255);
        builder.setView(seekBar);
        //builder.setMessage("Set ratio of brightness (bigger - brighter)");
        builder.setPositiveButton("ОК", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();
                filter_brightnessChange(255 - seekBar.getProgress());
            }
        });

        alert = builder.create();
        alert.show();
    }

    public void filter_brightnessChange(int p)
    {
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                int c0 = bitmap1.getPixel(i, j);
                int r = Color.red(c0);
                int g = Color.green(c0);
                int b = Color.blue(c0);
                c0 = Color.argb(p, r, g, b);

                bitmap2.setPixel(i, j, c0);
            }
        }
    }

    public void filter_contrast(View v)
    {
        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final SeekBar seekBar = new SeekBar(this);
        seekBar.setLayoutParams(new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        seekBar.setMax(200);
        seekBar.setProgress(100);
        builder.setView(seekBar);

        builder.setPositiveButton("ОК", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();
                filter_contrastChange(seekBar.getProgress() - 100);
            }
        });

        alert = builder.create();
        alert.show();
    }

    public void filter_contrastChange(int lvl)
    {
        double c = Math.pow((100.0 + lvl) / 100.0, 2);

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {

                int c0 = bitmap1.getPixel(i, j);
                int r = Color.red(c0);
                int g = Color.green(c0);
                int b = Color.blue(c0);

                double R = ((((r / 255.0) - 0.5) * c) + 0.5) * 255.0;
                double G = ((((g / 255.0) - 0.5) * c) + 0.5) * 255.0;
                double B = ((((b / 255.0) - 0.5) * c) + 0.5) * 255.0;

                if (R > 255)
                {
                    R = 255;
                } else if (R < 0)
                {
                    R = 0;
                }
                if (G > 255)
                {
                    G = 255;
                } else if (G < 0)
                {
                    G = 0;
                }
                if (B > 255)
                {
                    B = 255;
                } else if (B < 0)
                {
                    B = 0;
                }
                bitmap2.setPixel(i, j, Color.argb(255, (int) R, (int) G, (int) B));
            }
        }
    }

    public void filter_verticalFlip(View v)
    {
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                int c0 = bitmap1.getPixel(i, j);
                bitmap2.setPixel(width - i - 1, height - j - 1, c0);
            }
        }
    }

    public void filter_horizontalFlip(View v)
    {
        for (int i = 1; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                int c0 = bitmap1.getPixel(i, j);
                bitmap2.setPixel(width - i - 1, j, c0);
            }
        }
    }
}