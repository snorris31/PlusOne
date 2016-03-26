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
import android.util.Log;
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
import android.widget.Toast;

import com.example.sara.plusone.enums.EventType;
import com.example.sara.plusone.objects.CurrentUser;
import com.example.sara.plusone.objects.Event;
import com.example.sara.plusone.objects.Search;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    public static String FIREBASE_URL = "https://plusjuan.firebaseio.com/";
    public static CurrentUser gUser = null;

    ViewPager mPager;
    ScreenSlider mPagerAdapter;
    TabLayout tabLayout;
    public CurrentUser currentUser;
    public ArrayList<Event> events;
    Firebase mFirebaseRef;

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
        events = new ArrayList<>();

        tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.home));
        tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.events));
        tabLayout.getTabAt(2).setIcon(getResources().getDrawable(R.drawable.messages));
        //TODO change based on users notification status
        tabLayout.getTabAt(3).setIcon(getResources().getDrawable(R.drawable.notification_no_alert));
        AuthData authData = mFirebaseRef.getAuth();
        if (authData != null) {
            //TODO fetch all events from database

//            //TODO fetch currentUser data here. this one is a demo
//            ArrayList<Event> sampleEvents = new ArrayList<>();
//            sampleEvents.add(new Event(mFirebaseRef.getAuth().getUid(), null, EventType.GREEK, new Date(0), "address", "Title", "description", false));
//            sampleEvents.add(new Event(mFirebaseRef.getAuth().getUid(), null, EventType.MOVIE, new Date(0), "address", "Another title", "description", false));
//            sampleEvents.add(new Event(mFirebaseRef.getAuth().getUid(), null, EventType.MOVIE, new Date(0), "address", "Another title", "description", false));
//            sampleEvents.add(new Event(mFirebaseRef.getAuth().getUid(), null, EventType.MOVIE, new Date(0), "address", "Another title", "description", false));
//            sampleEvents.add(new Event(mFirebaseRef.getAuth().getUid(), null, EventType.MOVIE, new Date(0), "address", "Another title", "description", false));
//            sampleEvents.add(new Event(mFirebaseRef.getAuth().getUid(), null, EventType.OTHER, new Date(0), "address", "Yet another, long as fuck, possibly too long, title", "this is also an extremely long description, which may cause overflow problems in other cells. hopefully it doesnt. lorem ipsum fml", false));
//            currentUser = new CurrentUser(mFirebaseRef.getAuth().getUid(), "test", 21, null);

            Firebase eventRef = new Firebase(FIREBASE_URL).child("events");
            eventRef.setValue(sampleEvents);

//             user authenticated
            Toast.makeText(MainActivity.this, "Signed In", Toast.LENGTH_SHORT).show();
        } else {
//            Intent intent = new Intent(this,LoginActivity.class);
//            startActivityForResult(intent, 1);
            mFirebaseRef.createUser("TestEmail@gmail.com", "password", new Firebase.ValueResultHandler<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> stringObjectMap) {
                    System.out.print(stringObjectMap.get("uid"));
                }

                @Override
                public void onError(FirebaseError firebaseError) {

                }
            });

            mFirebaseRef.authWithPassword("TestEmail@gmail.com", "password", new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {

                }
            });
            // no user authenticated
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    private void awaitLatch(CountDownLatch latch) {
        try {
            latch.await(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

                    matchingField = (EditText) detailView.findViewById(R.id.matching_field);
                    matchingField.setText(currentSearch.textMatch);

                    eventTypeField = (Spinner) detailView.findViewById(R.id.event_type_field);
                    ArrayList<String> adjustedArray = EventType.asArrayList();
                    adjustedArray.add(0, "Any");
                    eventTypeField.setAdapter(new ArrayAdapter<>(context, R.layout.spinner_item, R.id.view, adjustedArray));
                    eventTypeField.setSelection(adjustedArray.indexOf(currentSearch.eventType));

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Search options").setView(detailView).setPositiveButton("Search", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            currentSearch.textMatch = matchingField.getText().toString();
                            currentSearch.eventType = (String) eventTypeField.getSelectedItem();
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
        if( id == R.id.login_page){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivityForResult(intent, 1);
        }
        if( id == R.id.signOut){
            mFirebaseRef.unauth();
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




