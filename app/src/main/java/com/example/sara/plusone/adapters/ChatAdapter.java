package com.example.sara.plusone.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sara.plusone.MainActivity;
import com.example.sara.plusone.R;
import com.example.sara.plusone.objects.Conversation;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Shannor on 3/26/2016.
 */
public class ChatAdapter extends BaseAdapter{
    private String currUserUID;
    private String receipentUID;
    private String currUserName;
    private String receipentName;
    private LayoutInflater mLayoutInflater;
    private Firebase mFireBaseRef;
    private Query mRef;
    private int mLayout;
    private LayoutInflater mInflater;
    private List<Conversation> mModels;
    private List<String> mKeys;
    private ChildEventListener mListener;

    public ChatAdapter(Query ref, final Activity context, int layout, final String currentUserUID, final String receiUID,
                       String userName, String receipName){
        this.currUserUID = currentUserUID;
        this.receipentUID = receiUID;
        this.currUserName = userName;
        this.receipentName = receipName;
        this.mLayout = layout;
        mRef = ref;
        mLayoutInflater = LayoutInflater.from(context);
        mFireBaseRef = new Firebase(MainActivity.FIREBASE_URL).child("users");
        mInflater = context.getLayoutInflater();
        mModels = new ArrayList<>();
        mKeys = new ArrayList<>();
        // Look for all child events. We will then map them to our own internal ArrayList, which backs ListView
        mListener = this.mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                Conversation conversation = dataSnapshot.getValue(Conversation.class);
                String key = dataSnapshot.getKey();

                if (conversation.getPersonalUid().equals(currUserUID) && conversation.getRecipentUid().equals(receipentUID)
                        ||conversation.getPersonalUid().equals(receipentUID) && conversation.getRecipentUid().equals(currUserUID)) {
                    // Insert into the correct location, based on previousChildName
                    if (previousChildName == null) {
                        mModels.add(0,conversation);
                        mKeys.add(0, key);
                    } else {
                        int previousIndex = mKeys.indexOf(previousChildName);
                        int nextIndex = previousIndex + 1;
                        if (nextIndex == mModels.size()) {
                            mModels.add(conversation);
                            mKeys.add(key);
                        } else {
                            mModels.add(nextIndex, conversation);
                            mKeys.add(nextIndex, key);
                        }
                    }
                    notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // One of the mModels changed. Replace it in our list and name mapping
                String key = dataSnapshot.getKey();
                Conversation conversation = dataSnapshot.getValue(Conversation.class);
                if (conversation.getPersonalUid().equals(currUserUID) && conversation.getRecipentUid().equals(receipentUID)
                        ||conversation.getPersonalUid().equals(receipentUID) && conversation.getRecipentUid().equals(currUserUID)) {
                    int index = mKeys.indexOf(key);

                    mModels.set(index, conversation);

                    notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                // A model was removed from the list. Remove it from our list and the name mapping
                String key = dataSnapshot.getKey();
                int index = mKeys.indexOf(key);

                mKeys.remove(index);
                mModels.remove(index);

                notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                // A model changed position in the list. Update our list accordingly
                String key = dataSnapshot.getKey();
                Conversation conversation = dataSnapshot.getValue(Conversation.class);
                int index = mKeys.indexOf(key);
                if (conversation.getPersonalUid().equals(currUserUID) && conversation.getRecipentUid().equals(receipentUID)
                        ||conversation.getPersonalUid().equals(receipentUID) && conversation.getRecipentUid().equals(currUserUID)) {
                    mModels.remove(index);
                    mKeys.remove(index);
                    if (previousChildName == null) {
                        mModels.add(0, conversation);
                        mKeys.add(0, key);
                    } else {
                        int previousIndex = mKeys.indexOf(previousChildName);
                        int nextIndex = previousIndex + 1;
                        if (nextIndex == mModels.size()) {
                            mModels.add(conversation);
                            mKeys.add(key);
                        } else {
                            mModels.add(nextIndex, conversation);
                            mKeys.add(nextIndex, key);
                        }
                    }
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("FirebaseListAdapter", "Listen was cancelled, no more updates will occur");
            }

        });
    }

    public void cleanup() {
        // We're being destroyed, let go of our mListener and forget about all of the mModels
        mRef.removeEventListener(mListener);
        mModels.clear();
        mKeys.clear();
    }

    @Override
    public int getCount() {
        return mModels.size();
    }

    @Override
    public Object getItem(int i) {
        return mModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = mInflater.inflate(mLayout, viewGroup, false);
        }

        Conversation model = mModels.get(i);

        TextView senderName = (TextView) view.findViewById(R.id.age_amount);
        senderName.setText(currUserName);
        ((TextView)view.findViewById(R.id.personName)).setText(model.getMessage());

        return view;
    }
}
