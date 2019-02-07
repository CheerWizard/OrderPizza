package com.example.jeremy.broadcasttestapp;

import android.content.Context;
import android.view.View;

import com.example.jeremy.broadcasttestapp.constants.AnimationType;

public interface IContract {

    interface IView {
        void initViews(View view);
        void initVars();
        void initListeners();
        void animate(AnimationType animationType);
        Context getContext();
    }

    interface IPresenter {
        void onClear();
    }
}
