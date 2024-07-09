package com.example.myapplication.fragment.home;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
//import com.example.myapplication.activity.GameActivity;
import com.example.myapplication.activity.GameActivity;
import com.example.myapplication.activity.LoginActivity;
import com.example.myapplication.activity.ProfileActivity;
import com.example.myapplication.activity.recharge.ItemOffsetDecoration;
import com.example.myapplication.activity.recharge.RechargeActivity;
import com.example.myapplication.adapter.RechargeAdapter;
import com.example.myapplication.adapter.RoomSelectionAdapter;
import com.example.myapplication.model.RechargeOption;
import com.example.myapplication.model.RoomOption;
import com.example.myapplication.socket.SocketManager;
import com.example.myapplication.tokenManager.TokenManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LinearLayout findRoomButton;
    private LinearLayout playWithBotButton;
    private TextView coinDisplay;
    private JSONObject user = TokenManager.getUserObject();

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private ImageView rechargeIcon;
    private ImageView avatar;
    private TextView playerName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Context context = getContext();

        rechargeIcon = view.findViewById(R.id.rechargeIcon);
        avatar = view.findViewById(R.id.avatar);
        playerName = view.findViewById(R.id.playerName);
        findRoomButton = view.findViewById(R.id.findRoomButton);
        playWithBotButton = view.findViewById(R.id.playWithBotButton);
        coinDisplay = view.findViewById(R.id.coinDisplay);
        StringBuilder coinDisplayTxt = new StringBuilder("Coins: ");
        try {
            playerName.setText(user.getString("fullName"));

            Log.d("USER", user.toString());

            String imageUrl = user.getString("profilePic");
            Picasso.get().load(imageUrl).into(avatar);

            coinDisplayTxt.append(String.valueOf(user.get("wallet")));
        } catch (JSONException e) {
            //throw new RuntimeException(e);
        }
        coinDisplay.setText(coinDisplayTxt);

        findRoomButton.setOnClickListener(v -> {
            showDialog(context);
        });

        playWithBotButton.setOnClickListener(v -> {
            playWithBot(context);
        });
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToProfile();
            }
        });

        rechargeIcon.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RechargeActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        StringBuilder coinDisplayTxt = new StringBuilder("Coins: ");
        try {
            coinDisplayTxt.append(String.valueOf(user.get("wallet")));
        } catch (JSONException e) {
            //throw new RuntimeException(e);
        }
        coinDisplay.setText(coinDisplayTxt);
    }

    private void showPopupMenu(View view) {
//        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
//        MenuInflater inflater = popupMenu.getMenuInflater();
//        inflater.inflate(R.menu.avatar_menu, popupMenu.getMenu());
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//
//                int itemId =  item.getItemId();
//                if(itemId == R.id.menu_profile) {
//                    navigateToProfile();
//                } else if (itemId == R.id.menu_logout) {
//                    navigateToLogin();
//                }
//                return true;
//            }
//        });
//        popupMenu.show();

    }

    private void navigateToProfile() {
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        startActivity(intent);
    }

//    private void navigateToLogin() {
//        Intent intent = new Intent(getActivity(), LoginActivity.class);
//        startActivity(intent);
//        getActivity().finish();
//    }


    private void showDialog(Context context){
        Dialog dialog = new Dialog(context, R.style.DialogStyle);
        dialog.setContentView(R.layout.dialog_room_selection);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

        ImageView btnClose = dialog.findViewById(R.id.btn_close);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        RecyclerView recyclerView;
            RoomSelectionAdapter adapter;
        List<RoomOption> roomOptions;

        recyclerView = dialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(dialog.getContext(), 1));
        recyclerView.addItemDecoration(new ItemOffsetDecoration(16));


        roomOptions = new ArrayList<>();
        roomOptions.add(new RoomOption(R.drawable.coin_1, 100));
        roomOptions.add(new RoomOption(R.drawable.coin_2, 200));
        roomOptions.add(new RoomOption(R.drawable.coin_3, 500));
        roomOptions.add(new RoomOption(R.drawable.coin_4, 1000));
        roomOptions.add(new RoomOption(R.drawable.coin_5, 2000));
        roomOptions.add(new RoomOption(R.drawable.coin_6, 5000));

        adapter = new RoomSelectionAdapter(roomOptions, dialog);
        recyclerView.setAdapter(adapter);

        dialog.show();
    }

    private void playWithBot(Context context){
        SocketManager socketManager = SocketManager.getInstance(TokenManager.getId_user());
        try {
            JSONObject data = new JSONObject();
            JSONObject user = TokenManager.getUserObject();
            data.put("user", user);
            Toast.makeText(context, "Joining...", Toast.LENGTH_SHORT).show();
            socketManager.getmSocket().emit("joinroom-ai", data);
            socketManager.connect();


            Emitter.Listener onJoinRoomSuccess = args -> {
                JSONObject response = (JSONObject) args[0];
                Intent intent = new Intent(context, GameActivity.class);
                intent.putExtra("room", response.toString());
                context.startActivity(intent);
            };

            socketManager.getmSocket().on("joinroom-success", onJoinRoomSuccess);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}