package com.example.sara.plusone.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.CalendarContract;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sara.plusone.EventsFragment;
import com.example.sara.plusone.HomeFragment;
import com.example.sara.plusone.MainActivity;
import com.example.sara.plusone.R;
import com.example.sara.plusone.enums.EventType;
import com.example.sara.plusone.listeners.EventViewListener;
import com.example.sara.plusone.objects.Event;
import com.example.sara.plusone.objects.Search;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by Zack on 3/26/2016.
 */
public class EventAdapter extends ArrayAdapter<Event> implements Filterable {

    private int resource;
    private Context context;
    private ArrayList<Event> events;
    private ArrayList<Event> originalEvents;
    private boolean isHomePage;
    private Firebase mFirebase;
    private Firebase mFirebaseUID;
    private ArrayList<String> mKeys;

    public EventAdapter(Context context, int resource, final ArrayList<Event> events, final boolean isHomePage) {
        super(context, resource, events);
        this.context = context;
        this.resource = resource;
        this.events = events;
        this.originalEvents = new ArrayList<>();
        this.isHomePage = isHomePage;
        this.mKeys = new ArrayList<>();

        mFirebaseUID = new Firebase(MainActivity.FIREBASE_URL);
        mFirebase = new Firebase(MainActivity.FIREBASE_URL).child("events");

        final EventAdapter thisInstance = this;

        mFirebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Event event = dataSnapshot.getValue(Event.class);
//                Map<String,String> eventMap = (Map<String,String>)dataSnapshot.getValue(Map.class);
//                Event event = new Event();
//                if(eventMap.containsKey("applicantIDs")){
//                    //Then load it
//                }
//                if (eventMap.containsKey("address")){
//                    event.address = eventMap.get("address");
//                }
//                if (eventMap.containsKey("completed")){
//                    event.completed = Boolean.parseBoolean(eventMap.get("completed"));
//                }
//                if (eventMap.containsKey("creatorID")){
//                    event.creatorID = eventMap.get("creatorID");
//                }
//                if (eventMap.containsKey("date")){
//                    event.setTimestamp(Long.parseLong(eventMap.get("date")));
//                    event.date = event.getDateObject();
//                }
//                if (eventMap.containsKey("description")){
//                    event.description = eventMap.get("description");
//                }
//                if (eventMap.containsKey("time")){
//                    event.time = eventMap.get("time");
//                }
//                if (eventMap.containsKey("title")){
//                    event.title = eventMap.get("title");
//                }
//                if (eventMap.containsKey("type")){
//                   event.setType(eventMap.get("type"));
//
//                }
                String key = dataSnapshot.getKey();

                // Insert into the correct location, based on previousChildName
                if (previousChildName == null) {
                    originalEvents.add(0, event);
                    mKeys.add(0, key);
                } else {
                    int previousIndex = mKeys.indexOf(previousChildName);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex >= originalEvents.size()) {
                        originalEvents.add(event);
                        mKeys.add(key);
                    } else {
                        originalEvents.add(nextIndex, event);
                        mKeys.add(nextIndex, key);
                    }
                }
                Search search = isHomePage ? HomeFragment.currentSearch : EventsFragment.currentSearch;
                thisInstance.getFilter().filter(search.textMatch + "~" + search.eventType);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // One of the mModels changed. Replace it in our list and name mapping
                String key = dataSnapshot.getKey();
                Event newModel = dataSnapshot.getValue(Event.class);
                int index = mKeys.indexOf(key);

                if (index < originalEvents.size()) {
                    originalEvents.set(index, newModel);
                }

                Search search = isHomePage ? HomeFragment.currentSearch : EventsFragment.currentSearch;
                thisInstance.getFilter().filter(search.textMatch + "~" + search.eventType);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                // A model was removed from the list. Remove it from our list and the name mapping
                String key = dataSnapshot.getKey();
                int index = mKeys.indexOf(key);

                mKeys.remove(index);
                originalEvents.remove(index);

                Search search = isHomePage ? HomeFragment.currentSearch : EventsFragment.currentSearch;
                thisInstance.getFilter().filter(search.textMatch + "~" + search.eventType);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                // A model changed position in the list. Update our list accordingly
                String key = dataSnapshot.getKey();
                Event newModel = dataSnapshot.getValue(Event.class);
                int index = mKeys.indexOf(key);
                originalEvents.remove(index);
                mKeys.remove(index);
                if (previousChildName == null) {
                    originalEvents.add(0, newModel);
                    mKeys.add(0, key);
                } else {
                    int previousIndex = mKeys.indexOf(previousChildName);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex == originalEvents.size()) {
                        originalEvents.add(newModel);
                        mKeys.add(key);
                    } else {
                        originalEvents.add(nextIndex, newModel);
                        mKeys.add(nextIndex, key);
                    }
                }
                Search search = isHomePage ? HomeFragment.currentSearch : EventsFragment.currentSearch;
                thisInstance.getFilter().filter(search.textMatch + "~" + search.eventType);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("FirebaseListAdapter", "Listen was cancelled, no more updates will occur");
            }
        });
    }

        @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        final LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        final Event event = events.get(position);

        if(convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
            holder = new Holder();

            holder.layout = (RelativeLayout)convertView.findViewById(R.id.layout);
            holder.title = (TextView)convertView.findViewById(R.id.title);
            holder.time = (TextView)convertView.findViewById(R.id.time);
            holder.description = (TextView)convertView.findViewById(R.id.description);

            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }

        final EventAdapter thisInstance = this;

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(holder.title.getLayoutParams());

        holder.garbageIcon = (ImageView)convertView.findViewById(R.id.garbage_icon);
        holder.requestButton = (Button)convertView.findViewById(R.id.request_button);
        if (isHomePage) {
            params.addRule(RelativeLayout.LEFT_OF, R.id.garbage_icon);

            holder.garbageIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to delete this event?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //TODO delete event from database
                            originalEvents.remove(event);
                            Search search = isHomePage ? HomeFragment.currentSearch : EventsFragment.currentSearch;
                            thisInstance.getFilter().filter(search.textMatch + "~" + search.eventType);
                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("No", null).show();
                }
            });

            holder.requestButton.setVisibility(View.GONE);
            holder.requestButton.setClickable(false);
        } else {
            params.addRule(RelativeLayout.LEFT_OF, R.id.request_button);
            final String userID = mFirebaseUID.getAuth().getUid();

            if (userID.equals(event.creatorID)) {
                holder.requestButton.setVisibility(View.GONE);
                holder.requestButton.setClickable(false);
            } else if (event.applicantIDs.contains(userID)){
                holder.requestButton.setText("Submitted");
                holder.requestButton.setTextColor(context.getResources().getColor(R.color.colorSecondaryText));
                holder.requestButton.setClickable(false);
            } else {
                holder.requestButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO submit application for this event, notify creator
                        originalEvents.get(originalEvents.indexOf(event)).applicantIDs.add(userID);
                        holder.requestButton.setText("Submitted");
                        holder.requestButton.setTextColor(context.getResources().getColor(R.color.colorSecondaryText));
                        holder.requestButton.setClickable(false);
                        Toast.makeText(context, "Submitted", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            holder.garbageIcon.setVisibility(View.GONE);
            holder.garbageIcon.setClickable(false);
        }

        holder.title.setLayoutParams(params);

        holder.layout.setBackgroundColor(context.getResources().getColor(EventType.fromString(event.type).getColorID()));
        holder.title.setText(event.title + ": " + event.type.toString());
        holder.time.setText(SimpleDateFormat.getDateTimeInstance().format(event.date));
        holder.description.setText(event.description);

        convertView.setOnClickListener(new EventViewListener(inflater, event, context));

        return convertView;
    }

    static class Holder {
        RelativeLayout layout;
        TextView title;
        ImageView garbageIcon;
        Button requestButton;
        TextView time;
        TextView description;
    }

    @Override
    public int getCount() {
        return events == null ? 0 : events.size();
    }

    @Override
    public Event getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            //constraint is formatted as such: "<matching_string>~<event_type>"
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<Event> filteredList = new ArrayList<>();

                String[] pieces = constraint.toString().split("~");
                String matchingString = pieces[0];
                EventType eventType = pieces[1].equals("Any") ? null : EventType.fromString(pieces[1]);

                String currentUserID = mFirebaseUID.getAuth().getUid();

                for (int i = 0; i < originalEvents.size(); i++) {
                    Event event = originalEvents.get(i);
                    boolean matchesString = matchingString.isEmpty() || event.title.toLowerCase().contains(matchingString.toLowerCase()) || event.description.toLowerCase().contains(matchingString.toLowerCase());
                    boolean matchesEventType = eventType == null || (eventType == EventType.fromString(event.type));
                    boolean isValid = (isHomePage && (currentUserID.equals(event.creatorID) || (event.applicantIDs != null && event.applicantIDs.contains(currentUserID)))) || (!isHomePage && event.date.after(Calendar.getInstance().getTime()));
                    if (matchesString && matchesEventType && isValid) {
                        filteredList.add(event);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;

                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                events = (ArrayList<Event>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
