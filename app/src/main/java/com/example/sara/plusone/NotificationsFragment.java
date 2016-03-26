package com.example.sara.plusone;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.sara.plusone.adapters.NotificationAdapter;
import com.example.sara.plusone.objects.Notification;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    ListView listView;
    NotificationAdapter adapter;

    public NotificationsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        listView = (ListView)view.findViewById(R.id.notification_list);
        ArrayList<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification("-1","-1","-1","-1","this is the description of a notification", false));
        notifications.add(new Notification("-1","-1","-1","-1","this is the description of a notification. but this ones a much longer description. good for you. aosgfiabd  ihbdus fiusybd fusydfbsd fbsudyfbsjdfbsjd uysvd vftsvd fusydbsbdfsbdufysdf sdfsdf", false));
        notifications.add(new Notification("-1","-1","-1","-1","this is the description of a notification", true));
        adapter = new NotificationAdapter(getActivity(), R.layout.notification_segment, notifications);
        listView.setAdapter(adapter);

        return view;
    }
}
