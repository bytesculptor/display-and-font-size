package com.bytesculptor.displayandfontsize;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bytesculptor.displayandfontsize.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_SETTINGS = 2;
    private boolean writePermissionGranted;

    private final float FONT_SIZE_SMALL = 0.85f;
    private final float FONT_SIZE_DEFAULT = 1.0f;
    private final float FONT_SIZE_LARGE = 1.15f;
    private final float FONT_SIZE_HUGE = 1.3f;

    private final float[] scale = {FONT_SIZE_SMALL, FONT_SIZE_DEFAULT, FONT_SIZE_LARGE, FONT_SIZE_HUGE};
    private int idx = 0;

    private ActivityMainBinding mActivityMainBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mActivityMainBinding.getRoot();
        setContentView(view);
        setSupportActionBar(mActivityMainBinding.toolbar);
    }


    @Override
    public void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
            writePermissionGranted = false;
            requestWritePermission();
        } else {
            writePermissionGranted = true;
        }
        getValuesAndShowOnActivity();
        updateButtonBackground();
    }


    private void requestWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, MY_PERMISSIONS_REQUEST_WRITE_SETTINGS);
            } else {
                writePermissionGranted = true;
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_SETTINGS) {
            writePermissionGranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }


    public void onBtClicked(View view) {
        if (!writePermissionGranted) {
            Toast toast = Toast.makeText(this, "Needs permission to write system settings!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }

        switch (view.getId()) {
            case R.id.bt_085:
                idx = 0;
                break;

            case R.id.bt_100:
                idx = 1;
                break;

            case R.id.bt_115:
                idx = 2;
                break;

            case R.id.bt_130:
                idx = 3;
                break;
        }

        boolean res = Settings.System.putFloat(this.getContentResolver(), Settings.System.FONT_SCALE, scale[idx]);
        if (res) {
            Toast.makeText(this, "set font to " + scale[idx] + ", idx = " + idx, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "failed set font", Toast.LENGTH_SHORT).show();
        }
        updateButtonBackground();
    }


    public void updateButtonBackground() {
        switch (idx) {
            case 0:
                mActivityMainBinding.bt085.setBackgroundResource((R.drawable.button_active));
                mActivityMainBinding.bt100.setBackgroundResource((R.drawable.button_inactive));
                mActivityMainBinding.bt115.setBackgroundResource((R.drawable.button_inactive));
                mActivityMainBinding.bt130.setBackgroundResource((R.drawable.button_inactive));
                break;

            case 1:
                mActivityMainBinding.bt085.setBackgroundResource((R.drawable.button_inactive));
                mActivityMainBinding.bt100.setBackgroundResource((R.drawable.button_active));
                mActivityMainBinding.bt115.setBackgroundResource((R.drawable.button_inactive));
                mActivityMainBinding.bt130.setBackgroundResource((R.drawable.button_inactive));
                break;

            case 2:
                mActivityMainBinding.bt085.setBackgroundResource((R.drawable.button_inactive));
                mActivityMainBinding.bt100.setBackgroundResource((R.drawable.button_inactive));
                mActivityMainBinding.bt115.setBackgroundResource((R.drawable.button_active));
                mActivityMainBinding.bt130.setBackgroundResource((R.drawable.button_inactive));
                break;

            case 3:
                mActivityMainBinding.bt085.setBackgroundResource((R.drawable.button_inactive));
                mActivityMainBinding.bt100.setBackgroundResource((R.drawable.button_inactive));
                mActivityMainBinding.bt115.setBackgroundResource((R.drawable.button_inactive));
                mActivityMainBinding.bt130.setBackgroundResource((R.drawable.button_active));
                break;
        }
    }


    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void getValuesAndShowOnActivity() {
        DisplayMetrics disp = this.getResources().getDisplayMetrics();

        mActivityMainBinding.tvWidth.setText(String.format("%d", disp.widthPixels));
        mActivityMainBinding.tvHeight.setText(String.format("%d", disp.heightPixels));
        mActivityMainBinding.tvXdpi.setText(String.format("%f", disp.xdpi));
        mActivityMainBinding.tvYdpi.setText(String.format("%f", disp.ydpi));
        mActivityMainBinding.tvDensity.setText(String.format("%d", disp.densityDpi));

        float currentScale = getResources().getConfiguration().fontScale;
        mActivityMainBinding.tvFontScale.setText(currentScale + "");
        float size = getResources().getConfiguration().densityDpi;

        if (compareFloatEqual(currentScale, scale[0], 0.02f)) {
            idx = 0;
        } else if (compareFloatEqual(currentScale, scale[1], 0.02f)) {
            idx = 1;
        } else if (compareFloatEqual(currentScale, scale[2], 0.02f)) {
            idx = 2;
        } else if (compareFloatEqual(currentScale, scale[3], 0.02f)) {
            idx = 3;
        } else {
            idx = 0;
        }
    }


    private boolean compareFloatEqual(float v1, float v2, float epsilon) {
        return Math.abs(v1 - v2) < (epsilon);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}