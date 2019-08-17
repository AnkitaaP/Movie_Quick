package com.ankita.moviequick.activity.activity.activity.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ankita.moviequick.R;

public class SearchActivity extends AppCompatActivity {

    private EditText etSearchBox;
    private Button btnSearchMovie;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_grossing:
                    Intent trendingIntent = new Intent(getApplicationContext(), DisplayWallActivity.class);
                    startActivity(trendingIntent);
                    return true;
                case R.id.action_search:
                    return true;
                case R.id.action_favourites:
                    Intent favoritesIntent = new Intent(getApplicationContext(), FavouritesActivity.class);
                    startActivity(favoritesIntent);
                    return true;
            }
            return false;
        }
    };

    private void submitSearch() {
        Intent intent = new Intent(getApplicationContext(), SearchResultsActivity.class);
        String TMDBQuery = etSearchBox.getText().toString();

        intent.putExtra("SearchQuery", TMDBQuery);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        BottomNavigationView navigation =  (BottomNavigationView) findViewById(R.id.bottom_navigation_search);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigation.setSelectedItemId(R.id.action_search);

        etSearchBox = findViewById(R.id.et_search_box);
        btnSearchMovie = findViewById(R.id.btn_search_movie);

        btnSearchMovie.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                submitSearch();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }
}

