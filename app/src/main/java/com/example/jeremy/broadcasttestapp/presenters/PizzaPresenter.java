package com.example.jeremy.broadcasttestapp.presenters;

import com.example.jeremy.broadcasttestapp.IContract;
import com.example.jeremy.broadcasttestapp.R;
import com.example.jeremy.broadcasttestapp.constants.AnimationType;
import com.example.jeremy.broadcasttestapp.constants.OrderStates;
import com.example.jeremy.broadcasttestapp.models.Pizza;

public class PizzaPresenter implements IContract.IPresenter {

    private IContract.IView view;
    private Pizza pizza;

    public PizzaPresenter(IContract.IView view) {
        this.view = view;
        pizza = new Pizza(view.getContext().getResources().getString(R.string.pizza_name) ,
                view.getContext().getResources().getString(R.string.pizza_description) ,
                view.getContext().getResources().getString(R.string.pizza_ingredients) ,
                21.69 , OrderStates.NOT_ORDERED);
    }

    public void onTouchOrderBtn() {
        pizza.setOrderStates(OrderStates.ORDERED);
        view.animate(AnimationType.ANIMATE_ORDERED);
    }

    public void onTouchCancelBtn() {
        pizza.setOrderStates(OrderStates.NOT_ORDERED);
        view.animate(AnimationType.ANIMATE_NOT_ORDERED);
    }

    @Override
    public void onClear() {
        //empty method
        pizza = null;
    }
}
