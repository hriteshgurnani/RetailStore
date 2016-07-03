package com.tw.retailstore.utils;

/**
 * Base Presenter for all UI Presenters.
 * Created by hritesh on 29/06/16.
 */

public abstract class BasePresenter<V> {

    protected V viewContract;

    public void attachView(V viewContract) {
        this.viewContract = viewContract;
        onViewAttached();
    }

    public void detachView() {
        onViewDetached();
        viewContract = null;
    }

    private void onViewAttached() {
    }

    protected void onViewDetached() {
    }
}
