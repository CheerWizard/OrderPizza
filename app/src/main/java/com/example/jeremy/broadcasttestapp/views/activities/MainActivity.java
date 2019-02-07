package com.example.jeremy.broadcasttestapp.views.activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.jeremy.broadcasttestapp.R;
import com.example.jeremy.broadcasttestapp.constants.RequestCodes;
import com.example.jeremy.broadcasttestapp.views.fragments.PizzaFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        //fragment
        final Fragment pizzaFragment = new PizzaFragment();
        ((PizzaFragment) pizzaFragment).setActivity(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.container , pizzaFragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCodes.REQUEST_CODE_CALL:
                    Toast.makeText(this, R.string.called_success, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        else if (resultCode == RESULT_CANCELED) Toast.makeText(this, R.string.call_cancelled, Toast.LENGTH_SHORT).show();
    }
}
