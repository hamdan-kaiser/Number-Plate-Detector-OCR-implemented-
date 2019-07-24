package hamdan.JuniorDesign.DigitalNumPlateDetector.editing;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import hamdan.JuniorDesign.DigitalNumPlateDetector.R;

public class PhotoEditActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mDrawerToggle;

    private MainFragment mCurrentFragment;
    private CropImageViewOptions mCropImageViewOptions = new CropImageViewOptions();

    public void setCurrentFragment(MainFragment fragment) {
        mCurrentFragment = fragment;
    }

    public void setCurrentOptions(CropImageViewOptions options) {
        mCropImageViewOptions = options;
        updateDrawerTogglesByOptions(options);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_edit);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.main_drawer_open, R.string.main_drawer_close);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        //mDrawerToggle.syncState();

        if (savedInstanceState == null) {
            setMainFragmentByPreset(CropDemoPreset.RECT);
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
        mCurrentFragment.updateCurrentCropViewOptions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (mCurrentFragment != null && mCurrentFragment.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StringFormatMatches")
    private void updateDrawerTogglesByOptions(CropImageViewOptions options) {
        ((TextView) findViewById(R.id.drawer_option_toggle_scale))
                .setText(getResources().getString(R.string.drawer_option_toggle_scale,
                        options.scaleType.name()));
        ((TextView) findViewById(R.id.drawer_option_toggle_shape))
                .setText(
                        getResources()
                                .getString(R.string.drawer_option_toggle_shape, options.cropShape.name()));
        ((TextView) findViewById(R.id.drawer_option_toggle_guidelines))
                .setText(
                        getResources()
                                .getString(R.string.drawer_option_toggle_guidelines, options.guidelines.name()));
        ((TextView) findViewById(R.id.drawer_option_toggle_multitouch))
                .setText(
                        getResources()
                                .getString(
                                        R.string.drawer_option_toggle_multitouch,
                                        Boolean.toString(options.multitouch)));
        ((TextView) findViewById(R.id.drawer_option_toggle_show_overlay))
                .setText(
                        getResources()
                                .getString(
                                        R.string.drawer_option_toggle_show_overlay,
                                        Boolean.toString(options.showCropOverlay)));
        ((TextView) findViewById(R.id.drawer_option_toggle_show_progress_bar))
                .setText(
                        getResources()
                                .getString(
                                        R.string.drawer_option_toggle_show_progress_bar,
                                        Boolean.toString(options.showProgressBar)));

        String aspectRatio = "FREE";
        if (options.fixAspectRatio) {
            aspectRatio = options.aspectRatio.first + ":" + options.aspectRatio.second;
        }
        ((TextView) findViewById(R.id.drawer_option_toggle_aspect_ratio))
                .setText(getResources().getString(R.string.drawer_option_toggle_aspect_ratio, aspectRatio));

        ((TextView) findViewById(R.id.drawer_option_toggle_auto_zoom))
                .setText(
                        getResources()
                                .getString(
                                        R.string.drawer_option_toggle_auto_zoom,
                                        options.autoZoomEnabled ? "Enabled" : "Disabled"));
        ((TextView) findViewById(R.id.drawer_option_toggle_max_zoom))
                .setText(
                        getResources().getString(R.string.drawer_option_toggle_max_zoom, options.maxZoomLevel));
    }

    private void setMainFragmentByPreset(CropDemoPreset demoPreset) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, MainFragment.newInstance(demoPreset))
                .commit();
    }

    public void onDrawerOptionClicked(View view) {
        switch (view.getId()) {
            case R.id.drawer_option_load:
                CropImage.startPickImageActivity(this);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.drawer_option_oval:
                setMainFragmentByPreset(CropDemoPreset.CIRCULAR);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.drawer_option_rect:
                setMainFragmentByPreset(CropDemoPreset.RECT);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.drawer_option_customized_overlay:
                setMainFragmentByPreset(CropDemoPreset.CUSTOMIZED_OVERLAY);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.drawer_option_min_max_override:
                setMainFragmentByPreset(CropDemoPreset.MIN_MAX_OVERRIDE);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.drawer_option_scale_center:
                setMainFragmentByPreset(CropDemoPreset.SCALE_CENTER_INSIDE);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.drawer_option_toggle_scale:
                mCropImageViewOptions.scaleType =
                        mCropImageViewOptions.scaleType == CropImageView.ScaleType.FIT_CENTER
                                ? CropImageView.ScaleType.CENTER_INSIDE
                                : mCropImageViewOptions.scaleType == CropImageView.ScaleType.CENTER_INSIDE
                                ? CropImageView.ScaleType.CENTER
                                : mCropImageViewOptions.scaleType == CropImageView.ScaleType.CENTER
                                ? CropImageView.ScaleType.CENTER_CROP
                                : CropImageView.ScaleType.FIT_CENTER;
                mCurrentFragment.setCropImageViewOptions(mCropImageViewOptions);
                updateDrawerTogglesByOptions(mCropImageViewOptions);
                break;
            case R.id.drawer_option_toggle_shape:
                mCropImageViewOptions.cropShape =
                        mCropImageViewOptions.cropShape == CropImageView.CropShape.RECTANGLE
                                ? CropImageView.CropShape.OVAL
                                : CropImageView.CropShape.RECTANGLE;
                mCurrentFragment.setCropImageViewOptions(mCropImageViewOptions);
                updateDrawerTogglesByOptions(mCropImageViewOptions);
                break;
            case R.id.drawer_option_toggle_guidelines:
                mCropImageViewOptions.guidelines =
                        mCropImageViewOptions.guidelines == CropImageView.Guidelines.OFF
                                ? CropImageView.Guidelines.ON
                                : mCropImageViewOptions.guidelines == CropImageView.Guidelines.ON
                                ? CropImageView.Guidelines.ON_TOUCH
                                : CropImageView.Guidelines.OFF;
                mCurrentFragment.setCropImageViewOptions(mCropImageViewOptions);
                updateDrawerTogglesByOptions(mCropImageViewOptions);
                break;
            case R.id.drawer_option_toggle_aspect_ratio:
                if (!mCropImageViewOptions.fixAspectRatio) {
                    mCropImageViewOptions.fixAspectRatio = true;
                    mCropImageViewOptions.aspectRatio = new Pair<>(1, 1);
                } else {
                    if (mCropImageViewOptions.aspectRatio.first == 1
                            && mCropImageViewOptions.aspectRatio.second == 1) {
                        mCropImageViewOptions.aspectRatio = new Pair<>(4, 3);
                    } else if (mCropImageViewOptions.aspectRatio.first == 4
                            && mCropImageViewOptions.aspectRatio.second == 3) {
                        mCropImageViewOptions.aspectRatio = new Pair<>(16, 9);
                    } else if (mCropImageViewOptions.aspectRatio.first == 16
                            && mCropImageViewOptions.aspectRatio.second == 9) {
                        mCropImageViewOptions.aspectRatio = new Pair<>(9, 16);
                    } else {
                        mCropImageViewOptions.fixAspectRatio = false;
                    }
                }
                mCurrentFragment.setCropImageViewOptions(mCropImageViewOptions);
                updateDrawerTogglesByOptions(mCropImageViewOptions);
                break;
            case R.id.drawer_option_toggle_auto_zoom:
                mCropImageViewOptions.autoZoomEnabled = !mCropImageViewOptions.autoZoomEnabled;
                mCurrentFragment.setCropImageViewOptions(mCropImageViewOptions);
                updateDrawerTogglesByOptions(mCropImageViewOptions);
                break;
            case R.id.drawer_option_toggle_max_zoom:
                mCropImageViewOptions.maxZoomLevel =
                        mCropImageViewOptions.maxZoomLevel == 4
                                ? 8
                                : mCropImageViewOptions.maxZoomLevel == 8 ? 2 : 4;
                mCurrentFragment.setCropImageViewOptions(mCropImageViewOptions);
                updateDrawerTogglesByOptions(mCropImageViewOptions);
                break;
            case R.id.drawer_option_set_initial_crop_rect:
                mCurrentFragment.setInitialCropRect();
                mDrawerLayout.closeDrawers();
                break;
            case R.id.drawer_option_reset_crop_rect:
                mCurrentFragment.resetCropRect();
                mDrawerLayout.closeDrawers();
                break;
            case R.id.drawer_option_toggle_multitouch:
                mCropImageViewOptions.multitouch = !mCropImageViewOptions.multitouch;
                mCurrentFragment.setCropImageViewOptions(mCropImageViewOptions);
                updateDrawerTogglesByOptions(mCropImageViewOptions);
                break;
            case R.id.drawer_option_toggle_show_overlay:
                mCropImageViewOptions.showCropOverlay = !mCropImageViewOptions.showCropOverlay;
                mCurrentFragment.setCropImageViewOptions(mCropImageViewOptions);
                updateDrawerTogglesByOptions(mCropImageViewOptions);
                break;
            case R.id.drawer_option_toggle_show_progress_bar:
                mCropImageViewOptions.showProgressBar = !mCropImageViewOptions.showProgressBar;
                mCurrentFragment.setCropImageViewOptions(mCropImageViewOptions);
                updateDrawerTogglesByOptions(mCropImageViewOptions);
                break;
            default:
                Toast.makeText(this, "Unknown drawer option clicked", Toast.LENGTH_LONG).show();
        }
    }

}
