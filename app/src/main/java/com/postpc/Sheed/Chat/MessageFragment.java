package com.postpc.Sheed.Chat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.postpc.Sheed.R;
import com.postpc.Sheed.SheedApp;
import com.postpc.Sheed.database.SheedUsersDB;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MessageFragment extends Fragment {

    CircleImageView profile_image;
    TextView username;

    ImageButton btn_send;
    EditText text_send;

    SheedUsersDB db;

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        profile_image = view.findViewById(R.id.profile_image);
        username = view.findViewById(R.id.username);
        btn_send = view.findViewById(R.id.btn_send);
        text_send = view.findViewById(R.id.text_send);

        db = SheedApp.getDB();

        assert getArguments() != null;
        String userId = getArguments().getString("userId");

        db.downloadUserAndDo(userId, sheedUser -> {
//            profile_image.setImageResource(sheedUser.imageUrl);
            username.setText(sheedUser.firstName);
            Picasso.with(getActivity()).load(sheedUser.imageUrl).into(profile_image);
        });

        return view;
    }
}