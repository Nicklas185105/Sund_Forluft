package com.example.sundforluft;

import android.app.Activity;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.sundforluft.DAL.DataAccessLayer;
import com.example.sundforluft.fragments.favorite.FavoriteFragment;
import com.example.sundforluft.fragments.help.HelpFragment;
import com.example.sundforluft.fragments.schools.SchoolsFragment;
import com.example.sundforluft.fragments.ranklist.RanklistFragment;
import com.example.sundforluft.fragments.scanner.ScannerFragment;
import com.example.sundforluft.services.Globals;
import com.example.sundforluft.services.SchoolAverageLoader;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static Toolbar toolbar;
    public static ActionBarDrawerToggle toggle;
    public static DrawerLayout drawer;
    public NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Globals.favoriteSchoolPreferences = getSharedPreferences("favorite_schools", MODE_PRIVATE);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FavoriteFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_favorit);
            getSupportActionBar().setTitle(R.string.menuFavorit);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.icon) {
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.fragment_container, new ScannerFragment()).addToBackStack(null).commit();
            navigationView.setCheckedItem(R.id.nav_scanner);
            getSupportActionBar().setTitle(R.string.menuScanner);
            hideKeyboard(this);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        hideKeyboard(this);
        switch (menuItem.getItemId()){
            case R.id.nav_favorit:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.fragment_container, new FavoriteFragment()).addToBackStack(null).commit();
                getSupportActionBar().setTitle(R.string.menuFavorit);

                break;
            case R.id.nav_map:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.fragment_container, new SchoolsFragment()).addToBackStack(null).commit();
                getSupportActionBar().setTitle(R.string.menuSchool);
                break;
            case R.id.nav_ranklist:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.fragment_container, new RanklistFragment()).addToBackStack(null).commit();
                getSupportActionBar().setTitle(R.string.menuRanklist);
                break;
            case R.id.nav_help:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.fragment_container, new HelpFragment()).addToBackStack(null).commit();
                getSupportActionBar().setTitle(R.string.menuHelp);
                break;
            case R.id.nav_scanner:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.fragment_container, new ScannerFragment()).addToBackStack(null).commit();
                getSupportActionBar().setTitle(R.string.menuScanner);
                break;
            case R.id.nav_back:
                Intent intent = new Intent(MainActivity.this, StartActivity.class);
                intent.putExtra("animation", false);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            System.out.println(getSupportFragmentManager().getBackStackEntryCount());
            getSupportFragmentManager().popBackStack();

        } else {
            Intent intent = new Intent(MainActivity.this, StartActivity.class);
            intent.putExtra("animation", false);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
