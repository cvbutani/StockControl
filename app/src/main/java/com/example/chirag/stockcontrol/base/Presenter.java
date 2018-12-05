package com.example.chirag.stockcontrol.base;

public interface Presenter<T extends MvpView> {
    void attachView(T view);
}
