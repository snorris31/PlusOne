package com.example.sara.plusone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.sara.plusone.adapters.EventAdapter;
import com.example.sara.plusone.objects.Search;

public class EventsFragment extends Fragment {

    ListView listView;

    public static EventAdapter adapter;
    public static Search currentSearch = new Search("", "any", "any");

    public EventsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        listView = (ListView)view.findViewById(R.id.event_list);
        adapter = new EventAdapter(getActivity(), R.layout.event_segment, ((MainActivity) getActivity()).events, false);
        listView.setAdapter(adapter);

        return view;
    }
}
