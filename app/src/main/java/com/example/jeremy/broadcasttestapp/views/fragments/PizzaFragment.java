package com.example.jeremy.broadcasttestapp.views.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jeremy.broadcasttestapp.IContract;
import com.example.jeremy.broadcasttestapp.R;
import com.example.jeremy.broadcasttestapp.constants.AnimationType;
import com.example.jeremy.broadcasttestapp.constants.IntentActions;
import com.example.jeremy.broadcasttestapp.constants.RequestCodes;
import com.example.jeremy.broadcasttestapp.constants.Schemas;
import com.example.jeremy.broadcasttestapp.presenters.PizzaPresenter;

public class PizzaFragment extends Fragment implements IContract.IView , View.OnClickListener {
    //activity
    private Activity activity;
    //presenters
    private PizzaPresenter pizzaPresenter;
    //views
    private TextView orderStateTextView;
    private Button callButton , orderButton , cancelButton;
    //broadcast receivers
    private BroadcastReceiver pizzaBroadcastReceiver;
    //handlers
    private Handler handler;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.pizza_fragment, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initVars();
        registerReceivers();
        initListeners();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceivers();
    }

    @Override
    public void onStop() {
        super.onStop();
        pizzaPresenter.onClear();
    }

    @Override
    public void initViews(View view) {
        //text_views
        final TextView pizzaNameTextView = view.findViewById(R.id.pizza_name_text_view);
        final TextView pizzaDescriptionLabelTextView = view.findViewById(R.id.description_text_view);
        final TextView pizzaDescriptionTextView = view.findViewById(R.id.description_text);
        final TextView pizzaIngredientsTextView = view.findViewById(R.id.ingredients_text);
        final TextView pizzaIngredientsLabelTextView = view.findViewById(R.id.ingredients_text_view);
        orderStateTextView = view.findViewById(R.id.order_status_text_view);
        //image view
        final ImageView pizzaImageView = view.findViewById(R.id.photo_image_view);
        //buttons
        callButton = view.findViewById(R.id.call_btn);
        cancelButton = view.findViewById(R.id.cancel_btn);
        orderButton = view.findViewById(R.id.order_btn);
    }

    @Override
    public void initVars() {
        handler = new Handler();
        pizzaPresenter = new PizzaPresenter(this);
    }

    @Override
    public void initListeners() {
        callButton.setOnClickListener(this);
        orderButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void animate(AnimationType animationType) {
        switch (animationType) {
            case ANIMATE_ORDERED:
                orderStateTextView.setText(getResources().getString(R.string.ordered));
                orderStateTextView.setTextColor(getResources().getColor(R.color.green));
                break;
            case ANIMATE_NOT_ORDERED:
                orderStateTextView.setText(getResources().getString(R.string.not_ordered));
                orderStateTextView.setTextColor(getResources().getColor(R.color.colorAccent));
                break;
            case ANIMATE_ORDERING:
                orderStateTextView.setText(getResources().getString(R.string.ordering));
                orderStateTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_btn:
                pizzaPresenter.onTouchCancelBtn();
                break;
            case R.id.call_btn:
                makeCall();
                break;
            case R.id.order_btn:
                sendBroadcast();
                startBroadcast();
                break;
        }
    }

    private void registerReceivers() {
        activity.registerReceiver(pizzaBroadcastReceiver , new IntentFilter(IntentActions.ACTION_ORDER_PIZZA));
    }

    private void unregisterReceivers() {
        activity.unregisterReceiver(pizzaBroadcastReceiver);
    }

    private void makeCall() {
        startActivityForResult(new Intent(Intent.ACTION_DIAL , Uri.fromParts(
                Schemas.TEL_SCHEMA ,
                getResources().getString(R.string.phone_number) ,
                null)) , RequestCodes.REQUEST_CODE_CALL);
    }

    private void sendBroadcast() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                activity.sendBroadcast(new Intent(IntentActions.ACTION_ORDER_PIZZA));
            }
        }).start();
    }

    private void startBroadcast() {
        animate(AnimationType.ANIMATE_ORDERING);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pizzaBroadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        pizzaPresenter.onTouchOrderBtn();
                    }
                };
            }
        } , 3000);
    }
}
