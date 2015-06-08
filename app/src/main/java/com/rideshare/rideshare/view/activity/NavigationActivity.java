package com.rideshare.rideshare.view.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.rideshare.rideshare.R;
import com.rideshare.rideshare.view.fragment.MyRidesFragment;
import com.rideshare.rideshare.view.fragment.NotificationFragment;
import com.rideshare.rideshare.view.fragment.PassengersDetailsFragment;
import com.rideshare.rideshare.view.fragment.RequestFragment;
import com.rideshare.rideshare.view.fragment.SuggestionFragment;
import com.rideshare.rideshare.view.fragment.TripPlannerFragment;

public class NavigationActivity extends FragmentActivity {

    private String USER_ID;

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private String[] options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Bundle bundle = getIntent().getExtras();
        USER_ID = bundle.getString("USER");

        options = getResources().getStringArray(R.array.options);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_nav);
        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.navigation_item, options));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_drawer, R.string.close_drawer);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        selectItem(0, bundle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_navigation, menu);
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

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position, null);
        }
    }

    public void selectItem(int position, Bundle bundle) {
        if(bundle == null){
            bundle = new Bundle();
        }
        bundle.putString("USER", USER_ID);

        Fragment fragment = getFragmentAtPosition(position, bundle);
        displayFragment(fragment);

        drawerList.setItemChecked(position, true);
        if(position < 10)
            setTitle(options[position]);
        else{
            switch (position){
                case(10): setTitle("Update request"); break;
                case(11): setTitle("Suggestions"); break;
                case(12): setTitle("Passengers Details"); break;
            }
        }
        drawerLayout.closeDrawer(drawerList);
    }

    private void displayFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.body, fragment)
                .commit();
    }

    private Fragment getFragmentAtPosition(int position, Bundle args){
        switch (position){
            case 0:
                return NotificationFragment.newInstance(args);
            case 1:
                return TripPlannerFragment.newInstance(args);
            case 2:
                return MyRidesFragment.newInstance(args);
            case 10:
                return RequestFragment.newInstance(args);
            case 11:
                return SuggestionFragment.newInstance(args);
            case 12:
                return PassengersDetailsFragment.newInstance(args);
        }
        // Default value should never come to this point
        return NotificationFragment.newInstance(args);
    }

    @Override
    public void setTitle(CharSequence title) {
        ((TextView) findViewById(R.id.header)).setText(title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
}
