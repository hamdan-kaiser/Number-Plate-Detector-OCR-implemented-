package hamdan.JuniorDesign.DigitalNumPlateDetector.photoedit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;

import com.theartofdev.edmodo.cropper.CropImageView;

import hamdan.JuniorDesign.DigitalNumPlateDetector.R;

public class PhotoEditingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    Menu menu;
    MenuItem toggleScale, toggleShape, toggleGuidelines, toggleMultitouch, toggleShowOverlay, toggleShowProgressbar, toggleAspectRatio, toggleAutoZoom, toggleMaxZoom;
    private MainFragment mCurrentFragment;
    private CropImageViewOptions mCropImageViewOptions = new CropImageViewOptions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_editing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        menu = navigationView.getMenu();

        toggleScale = menu.findItem(R.id.drawer_option_toggle_scale);
        toggleShape = menu.findItem(R.id.drawer_option_toggle_shape);
        toggleGuidelines = menu.findItem(R.id.drawer_option_toggle_guidelines);
        toggleMultitouch = menu.findItem(R.id.drawer_option_toggle_multitouch);
        toggleShowOverlay = menu.findItem(R.id.drawer_option_toggle_show_overlay);
        toggleShowProgressbar = menu.findItem(R.id.drawer_option_toggle_show_progress_bar);
        toggleAspectRatio = menu.findItem(R.id.drawer_option_toggle_aspect_ratio);
        toggleAutoZoom = menu.findItem(R.id.drawer_option_toggle_auto_zoom);
        toggleMaxZoom = menu.findItem(R.id.drawer_option_toggle_max_zoom);

        if (savedInstanceState == null) {
            setMainFragmentByPreset(CropDemoPreset.RECT);
        }
    }

    public void setCurrentFragment(MainFragment fragment) {
        mCurrentFragment = fragment;
    }

    public void setCurrentOptions(CropImageViewOptions options) {
        mCropImageViewOptions = options;
        updateDrawerTogglesByOptions(options);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setMainFragmentByPreset(CropDemoPreset demoPreset) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, MainFragment.newInstance(demoPreset))
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photo_editing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mCurrentFragment != null && mCurrentFragment.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

    }

    private void updateDrawerTogglesByOptions(CropImageViewOptions options) {

        toggleScale.setTitle(getResources().getString(R.string.drawer_option_toggle_scale, options.scaleType.name()));
        toggleShape.setTitle(getResources().getString(R.string.drawer_option_toggle_shape, options.cropShape.name()));
        toggleGuidelines.setTitle(getResources().getString(R.string.drawer_option_toggle_guidelines, options.guidelines.name()));
        toggleMultitouch.setTitle(getResources().getString(R.string.drawer_option_toggle_multitouch, Boolean.toString(options.multitouch)));
        toggleShowOverlay.setTitle(getResources().getString(R.string.drawer_option_toggle_show_overlay, Boolean.toString(options.showCropOverlay)));
        toggleShowProgressbar.setTitle(getResources().getString(R.string.drawer_option_toggle_show_progress_bar, Boolean.toString(options.showProgressBar)));

        String aspectRatio = "FREE";
        if (options.fixAspectRatio) {
            aspectRatio = options.aspectRatio.first + ":" + options.aspectRatio.second;
        }
        toggleAspectRatio.setTitle(getResources().getString(R.string.drawer_option_toggle_aspect_ratio, aspectRatio));
        toggleAutoZoom.setTitle(getResources().getString(R.string.drawer_option_toggle_auto_zoom, options.autoZoomEnabled ? "Enable" : "Disable"));
        toggleMaxZoom.setTitle(getResources().getString(R.string.drawer_option_toggle_max_zoom, Integer.toString(options.maxZoomLevel)));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.drawer_option_oval) {
            setMainFragmentByPreset(CropDemoPreset.CIRCULAR);
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.drawer_option_rect) {
            setMainFragmentByPreset(CropDemoPreset.RECT);
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.drawer_option_customized_overlay) {
            setMainFragmentByPreset(CropDemoPreset.CUSTOMIZED_OVERLAY);
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.drawer_option_min_max_override) {
            setMainFragmentByPreset(CropDemoPreset.MIN_MAX_OVERRIDE);
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.drawer_option_scale_center) {
            setMainFragmentByPreset(CropDemoPreset.SCALE_CENTER_INSIDE);
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.drawer_option_toggle_scale) {
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

        } else if (id == R.id.drawer_option_toggle_shape) {
            mCropImageViewOptions.cropShape =
                    mCropImageViewOptions.cropShape == CropImageView.CropShape.RECTANGLE
                            ? CropImageView.CropShape.OVAL
                            : CropImageView.CropShape.RECTANGLE;
            mCurrentFragment.setCropImageViewOptions(mCropImageViewOptions);
            updateDrawerTogglesByOptions(mCropImageViewOptions);

        } else if (id == R.id.drawer_option_toggle_guidelines) {
            mCropImageViewOptions.guidelines =
                    mCropImageViewOptions.guidelines == CropImageView.Guidelines.OFF
                            ? CropImageView.Guidelines.ON
                            : mCropImageViewOptions.guidelines == CropImageView.Guidelines.ON
                            ? CropImageView.Guidelines.ON_TOUCH
                            : CropImageView.Guidelines.OFF;
            mCurrentFragment.setCropImageViewOptions(mCropImageViewOptions);
            updateDrawerTogglesByOptions(mCropImageViewOptions);

        } else if (id == R.id.drawer_option_toggle_aspect_ratio) {
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

        } else if (id == R.id.drawer_option_toggle_auto_zoom) {
            mCropImageViewOptions.autoZoomEnabled = !mCropImageViewOptions.autoZoomEnabled;
            mCurrentFragment.setCropImageViewOptions(mCropImageViewOptions);
            updateDrawerTogglesByOptions(mCropImageViewOptions);

        } else if (id == R.id.drawer_option_toggle_max_zoom) {
            mCropImageViewOptions.maxZoomLevel =
                    mCropImageViewOptions.maxZoomLevel == 4
                            ? 8
                            : mCropImageViewOptions.maxZoomLevel == 8 ? 2 : 4;
            mCurrentFragment.setCropImageViewOptions(mCropImageViewOptions);
            updateDrawerTogglesByOptions(mCropImageViewOptions);

        } else if (id == R.id.drawer_option_set_initial_crop_rect) {
            mCurrentFragment.setInitialCropRect();
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.drawer_option_reset_crop_rect) {
            mCurrentFragment.resetCropRect();
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.drawer_option_toggle_multitouch) {
            mCropImageViewOptions.multitouch = !mCropImageViewOptions.multitouch;
            mCurrentFragment.setCropImageViewOptions(mCropImageViewOptions);
            updateDrawerTogglesByOptions(mCropImageViewOptions);

        } else if (id == R.id.drawer_option_toggle_show_overlay) {
            mCropImageViewOptions.showCropOverlay = !mCropImageViewOptions.showCropOverlay;
            mCurrentFragment.setCropImageViewOptions(mCropImageViewOptions);
            updateDrawerTogglesByOptions(mCropImageViewOptions);

        } else if (id == R.id.drawer_option_toggle_show_progress_bar) {
            mCropImageViewOptions.showProgressBar = !mCropImageViewOptions.showProgressBar;
            mCurrentFragment.setCropImageViewOptions(mCropImageViewOptions);
            updateDrawerTogglesByOptions(mCropImageViewOptions);

        }

        return true;
    }
}
