package com.example.sara.plusone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.sara.plusone.enums.EventType;
import com.example.sara.plusone.objects.CurrentUser;
import com.example.sara.plusone.objects.Event;
import com.example.sara.plusone.objects.Search;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ViewPager mPager;
    ScreenSlider mPagerAdapter;
    TabLayout tabLayout;

    public CurrentUser currentUser;
    public ArrayList<Event> events;

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
                startEvent();
            }
        });
        mPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        mPagerAdapter = new ScreenSlider(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(mPager);

        //TODO fetch all events from database
        events = new ArrayList<>();

        //TODO fetch currentUser data here. this one is a demo
        ArrayList<Event> sampleEvents = new ArrayList<>();
        sampleEvents.add(new Event("-1", null, EventType.GREEK, new Date(0), "address", "Title", "description", false));
        sampleEvents.add(new Event("-1", null, EventType.MOVIE, new Date(0), "address", "Another title", "description", false));
        sampleEvents.add(new Event("-1", null, EventType.MOVIE, new Date(0), "address", "Another title", "description", false));
        sampleEvents.add(new Event("-1", null, EventType.MOVIE, new Date(0), "address", "Another title", "description", false));
        sampleEvents.add(new Event("-1", null, EventType.MOVIE, new Date(0), "address", "Another title", "description", false));
        sampleEvents.add(new Event("-1", null, EventType.OTHER, new Date(0), "address", "Yet another, long as fuck, possibly too long, title", "this is also an extremely long description, which may cause overflow problems in other cells. hopefully it doesnt. lorem ipsum fml", false));
        currentUser = new CurrentUser("-1", "test", 21, null);

        tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.home));
        tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.events));
        tabLayout.getTabAt(2).setIcon(getResources().getDrawable(R.drawable.messages));
        //TODO change based on users notification status
        tabLayout.getTabAt(3).setIcon(getResources().getDrawable(R.drawable.notification_no_alert));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final Activity context = this;

        menu.findItem(R.id.search).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            EditText matchingField;
            Spinner eventTypeField;
            Spinner locationSpinner;//TODO

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final Fragment currentFragment = mPagerAdapter.getItem(mPager.getCurrentItem());
                if (mPager.getCurrentItem() == 0 || mPager.getCurrentItem() == 1) {
                    final Search currentSearch = currentFragment instanceof HomeFragment ? HomeFragment.currentSearch : EventsFragment.currentSearch;

                    final LayoutInflater inflater = context.getLayoutInflater();
                    View detailView = inflater.inflate(R.layout.fragment_search_options, null);

                    matchingField = (EditText)detailView.findViewById(R.id.matching_field);
                    matchingField.setText(currentSearch.textMatch);

                    eventTypeField = (Spinner)detailView.findViewById(R.id.event_type_field);
                    ArrayList<String> adjustedArray = EventType.asArrayList();
                    adjustedArray.add(0, "Any");
                    eventTypeField.setAdapter(new ArrayAdapter<>(context, R.layout.spinner_item, R.id.view, adjustedArray));
                    eventTypeField.setSelection(adjustedArray.indexOf(currentSearch.eventType));

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Search options").setView(detailView).setPositiveButton("Search", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            currentSearch.textMatch = matchingField.getText().toString();
                            currentSearch.eventType = (String)eventTypeField.getSelectedItem();
                            //TODO currentSearch.latLong = something

                            String constraint = currentSearch.textMatch + "~" + currentSearch.eventType;

                            if (currentFragment instanceof HomeFragment) {
                                HomeFragment.adapter.getFilter().filter(constraint);
                            } else {
                                EventsFragment.adapter.getFilter().filter(constraint);
                            }
                        }
                    }).setNegativeButton("Cancel", null).show();
                }
                return true;
            }
        });

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

    private class ScreenSlider extends FragmentStatePagerAdapter {
        public ScreenSlider(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new HomeFragment();
                case 1:
                    return new EventsFragment();
                case 2:
                    return new MessagesFragment();
                case 3:
                    return new NotificationsFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    public void startEvent(){
        Intent intent = new Intent(this,CreateEvent.class);
        startActivity(intent);
    }
}




