package com.example.sara.plusone.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sara.plusone.MainActivity;
import com.example.sara.plusone.R;
import com.example.sara.plusone.enums.EventType;
import com.example.sara.plusone.listeners.EventViewListener;
import com.example.sara.plusone.objects.Event;
import com.example.sara.plusone.objects.Notification;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Zack on 3/26/2016.
 */
public class NotificationAdapter extends ArrayAdapter<Notification> {

    private int resource;
    private Context context;
    private ArrayList<Notification> notifications;
    private Firebase mFirebase;

    public NotificationAdapter(Context context, int resource, ArrayList<Notification> notifications) {
        super(context, resource, notifications);
        this.context = context;
        this.resource = resource;
        this.notifications = notifications;
        mFirebase = new Firebase(MainActivity.FIREBASE_URL).child("users");
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
            holder = new Holder();

            holder.layout = (RelativeLayout)convertView.findViewById(R.id.layout);

            holder.userImage = (ImageView)convertView.findViewById(R.id.user_image);

            holder.body = (TextView)convertView.findViewById(R.id.body);

            //TODO fetch event by ID
            //convertView.setOnClickListener(new EventViewListener(inflater, event, context));

            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }
        Notification notification = notifications.get(position);
        holder.layout.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
        //TODO fetch user info from anySenderID
        //holder.userImage = ...
        holder.body.setText(notification.body);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.layout.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryLight));
            }
        });



        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.popupview);
        dialog.setTitle(notification.affectedUserID);
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        return convertView;
    }

    static class Holder {
        RelativeLayout layout;
        ImageView userImage;
        TextView body;
    }

}
