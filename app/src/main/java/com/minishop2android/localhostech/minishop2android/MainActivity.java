package com.minishop2android.localhostech.minishop2android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ListView productsList;
    CustomList productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Создать новый ресурс", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        productsList = (ListView) findViewById(R.id.product_list);

        MiniShop.getProducts(this, new VolleyCallback(){
            @Override
            public void onSuccess(JSONArray dataProducts){
                List<String> titles = new ArrayList<String>();
                List<String> subtitles = new ArrayList<String>();
                List<String> images = new ArrayList<String>();
                try {
                    for (int i = 0; i < dataProducts.length(); i++) {
                        JSONObject productsArr = dataProducts.getJSONObject(i);
                        Log.d("MY_LOG", productsArr.getString("pagetitle"));
                        titles.add(productsArr.getString("pagetitle"));
                        subtitles.add("Цена: " + productsArr.getString("price") + "р.");
                        images.add(productsArr.getString("image"));
                    }
                    productAdapter = new CustomList(MainActivity.this, titles, subtitles, images);
                    productsList.setAdapter(productAdapter);
                    productsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Log.d("MY_TAG", "List item clicked");
                            Intent intent = new Intent(MainActivity.this, EditForm.class);
                            startActivity(intent);
                        }
                    });
                    productsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                            return false;
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class CustomList extends ArrayAdapter<String>{

        private final Activity context;
        private final List<String> title;
        private final List<String> subtitle;
        private final List<String> image_url;
        public CustomList(Activity context,
                          List<String> title, List<String> subtitle, List<String> image_url) {
            super(context, R.layout.productrow, title);
            this.context = context;
            this.title = title;
            this.subtitle = subtitle;
            this.image_url = image_url;
        }
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView= inflater.inflate(R.layout.productrow, null, true);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.label);
            ImageView productImage = (ImageView) rowView.findViewById(R.id.icon);
            TextView txtSubtitle = (TextView) rowView.findViewById(R.id.subtitle);
            txtTitle.setText(title.get(position));
            txtSubtitle.setText(subtitle.get(position));
            Picasso.with(context).load(image_url.get(position)).resize(200, 200).centerCrop().transform(new CircleTransform()).into(productImage);
            return rowView;
        }
    }
}
