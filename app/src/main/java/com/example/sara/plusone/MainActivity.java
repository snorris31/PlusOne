package com.example.sara.plusone;

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
import android.util.Log;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;

public class MainActivity extends AppCompatActivity {

    public static String FIREBASE_URL = "https://plusjuan.firebaseio.com/";

    ViewPager mPager;
    ScreenSlider mPagerAdapter;
    TabLayout tabLayout;

    public CurrentUser currentUser;
    public ArrayList<Event> events;

    Firebase mFirebaseRef;
    CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Firebase.setAndroidContext(this);
         mFirebaseRef = new Firebase(FIREBASE_URL);

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
        currentUser = new CurrentUser("-1", "test", 21, null, sampleEvents, null);

        tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.home_grey));
        tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.events_grey));
        tabLayout.getTabAt(2).setIcon(getResources().getDrawable(R.drawable.messages_grey));
        //TODO change based on users notification status
        tabLayout.getTabAt(3).setIcon(getResources().getDrawable(R.drawable.notification_no_alert_grey));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
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
        if( id == R.id.login_page){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivityForResult(intent, 1);
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

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Home";
                case 1:
                    return "Events";
                case 2:
                    return "Messages";
                case 3:
                    return "Notifications";
            }
            return null;
        }
    }

    public void startEvent(){
        Intent intent = new Intent(this,CreateEvent.class);
        startActivity(intent);
    }
}




