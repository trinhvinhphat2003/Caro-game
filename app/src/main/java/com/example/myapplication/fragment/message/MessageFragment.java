package com.example.myapplication.fragment.message;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.UserAdapter;
import com.example.myapplication.api.UserRepository;
import com.example.myapplication.model.UserItem;
import com.example.myapplication.model.response.UserResponse;
import com.example.myapplication.services.UserService;
import com.example.myapplication.tokenManager.TokenManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    UserService userService;

    public MessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
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

    private RecyclerView userRecyclerView;
    private UserAdapter userAdapter;
    private List<UserItem> userList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        userRecyclerView = view.findViewById(R.id.userRecyclerView);
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList, getContext());
        userRecyclerView.setAdapter(userAdapter);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userService = UserRepository.getUserService();


        loadUser();



        return view;
    }
    private void loadUser(){
        try {
            Call<UserResponse> call = userService.getUsers("Bearer " + TokenManager.getToken());
            call.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    if (response.body() != null){
                        UserResponse userResponse = response.body();
                        if (userResponse.isOnSuccess()) {
                            userList.clear();
                            Toast.makeText(getContext(), userResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            for (UserItem userItem : userResponse.getData()) {
                                UserItem user = new UserItem(userItem.getFullName(), userItem.getProfilePic(), userItem.getId());
                                userList.add(user);
                            }
                            userAdapter.notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(getContext(), userResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "Fail to load user", Toast.LENGTH_SHORT).show();
                }
            });
        }catch ( Exception e){

        }


    }

}