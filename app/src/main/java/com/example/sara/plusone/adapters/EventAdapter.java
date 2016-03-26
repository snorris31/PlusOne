package com.example.sara.plusone.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Layout;
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

import com.example.sara.plusone.MainActivity;
import com.example.sara.plusone.R;
import com.example.sara.plusone.enums.EventType;
import com.example.sara.plusone.objects.Event;
import com.example.sara.plusone.objects.Search;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Zack on 3/26/2016.
 */
public class EventAdapter extends ArrayAdapter<Event> implements Filterable {

    private int resource;
    private Context context;
    private ArrayList<Event> events;
    private ArrayList<Event> originalEvents;
    private boolean isHomePage;

    public EventAdapter(Context context, int resource, ArrayList<Event> events, boolean isHomePage) {
        super(context, resource, events);
        this.context = context;
        this.resource = resource;
        this.events = events;
        this.originalEvents = new ArrayList(events);
        this.isHomePage = isHomePage;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder;
        final LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        if(row == null) {
            row = inflater.inflate(resource, parent, false);

            holder = new Holder();
            final EventAdapter thisInstance = this;

            holder.title = (TextView)row.findViewById(R.id.title);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(holder.title.getLayoutParams());

            holder.garbageIcon = (ImageView)row.findViewById(R.id.garbage_icon);
            holder.requestButton = (Button)row.findViewById(R.id.request_button);
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
                                originalEvents.remove(events.get(position));
                                events.remove(position);
                                thisInstance.notifyDataSetChanged();
                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("No", null).show();
                    }
                });

                holder.requestButton.setVisibility(View.GONE);
                holder.requestButton.setClickable(false);
            } else {
                params.addRule(RelativeLayout.LEFT_OF, R.id.request_button);

                if (((MainActivity)context).currentUser.id.equals(events.get(position).creatorID) || events.get(position).applicantIDs.contains(((MainActivity)context).currentUser.id)) {
                    holder.requestButton.setBackgroundColor(0x727272);
                    holder.requestButton.setText("Submitted");
                    holder.requestButton.setClickable(false);
                } else {
                    holder.requestButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO submit application for this event
                            originalEvents.get(originalEvents.indexOf(events.get(position))).applicantIDs.add(((MainActivity) context).currentUser.id);
                            events.get(position).applicantIDs.add(((MainActivity) context).currentUser.id);
                            thisInstance.notifyDataSetChanged();
                            Toast.makeText(context, "Submitted", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                holder.garbageIcon.setVisibility(View.GONE);
                holder.garbageIcon.setClickable(false);
            }

            holder.title.setLayoutParams(params);

            holder.time = (TextView)row.findViewById(R.id.time);

            holder.description = (TextView)row.findViewById(R.id.description);

            row.setTag(holder);
        } else {
            holder = (Holder)row.getTag();
        }

        final Event event = events.get(position);
        holder.title.setText(event.title + ": " + event.type.toString());
        holder.time.setText(SimpleDateFormat.getDateTimeInstance().format(event.date));
        holder.description.setText(event.description);

        row.setOnClickListener(new View.OnClickListener() {

            TextView titleField;
            TextView posterField;
            TextView typeField;
            TextView dateField;
            TextView addressField;
            TextView descriptionField;

            @Override
            public void onClick(View v) {
                View detailView = inflater.inflate(R.layout.fragment_event_detail, null);

                titleField = (TextView)detailView.findViewById(R.id.title_field);
                titleField.setText(event.title);

                //TODO get poster info from database
                posterField = (TextView)detailView.findViewById(R.id.poster_field);
                posterField.setText(event.creatorID);

                typeField = (TextView)detailView.findViewById(R.id.type_field);
                typeField.setText(event.type.toString());

                dateField = (TextView)detailView.findViewById(R.id.date_field);
                dateField.setText(new SimpleDateFormat().format(event.date));

                addressField = (TextView)detailView.findViewById(R.id.address_field);
                addressField.setText(event.address);

                descriptionField = (TextView)detailView.findViewById(R.id.description_field);
                descriptionField.setText(event.description);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Event details").setView(detailView).show();
            }
        });

        return row;
    }

    static class Holder {
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

                String currentUserID = ((MainActivity)context).currentUser.id;

                for (int i = 0; i < originalEvents.size(); i++) {
                    Event event = originalEvents.get(i);
                    boolean matchesString = matchingString.isEmpty() || event.title.toLowerCase().contains(matchingString.toLowerCase()) || event.description.toLowerCase().contains(matchingString.toLowerCase());
                    boolean matchesEventType = eventType == null || (eventType == event.type);
                    boolean isValid = (isHomePage && (currentUserID.equals(event.creatorID) || event.applicantIDs.contains(currentUserID))) || (!isHomePage && event.date.after(Calendar.getInstance().getTime()));
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
