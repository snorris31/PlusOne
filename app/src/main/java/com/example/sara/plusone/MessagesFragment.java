package com.example.sara.plusone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sara.plusone.adapters.FireBaseListAdapter;
import com.example.sara.plusone.objects.Person;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.List;
import java.util.Map;


/**
 */
public class MessagesFragment extends Fragment {

    Firebase mFirebaseRef;
    PersonAdapter adapter;
    ListView listView;
    String userName;

    public MessagesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MessagesFragment newInstance(String param1, String param2) {
        MessagesFragment fragment = new MessagesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFirebaseRef = new Firebase(MainActivity.FIREBASE_URL).child("users");
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        listView = (ListView)view.findViewById(R.id.list_of_conversations);
        adapter = new PersonAdapter(mFirebaseRef.limitToLast(40),
                getActivity(),R.layout.chat_item,mFirebaseRef.getAuth().getUid());
        listView.setAdapter(adapter);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(adapter.getCount() - 1);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Person picked = (Person)parent.getItemAtPosition(position);
                if (!picked.getUid().equals(mFirebaseRef.getAuth().getUid())) {
                    Intent intent = new Intent(getContext(), ChatApplication.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("uid", picked.getUid());
                    bundle.putString("name", picked.getName());
                    bundle.putString("currentName", userName);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });


        return view;
    }

    public class PersonAdapter extends FireBaseListAdapter<Person> {

        private String currentUser;
        private LayoutInflater mLayoutInflater;
        private Firebase mFireBaseRef;

        public PersonAdapter(Query ref, Activity context, int layout, String currentUser){
            super(ref,Person.class,layout,context);
            this.currentUser = currentUser;
            mLayoutInflater = LayoutInflater.from(context);
            mFireBaseRef = new Firebase(MainActivity.FIREBASE_URL).child("users");
        }


        @Override
        protected void populateView(final View v, final Person model) {
            //Your own name
            if(model.getUid().equals(currentUser)){
                userName = model.getName();
            }

            TextView senderName = (TextView) v.findViewById(R.id.personName);
            senderName.setText(model.name);
            String age = "Age";
            ((TextView)v.findViewById(R.id.age_amount)).setText(String.format("%s: %d", age, model.age));

        }

    }
}

