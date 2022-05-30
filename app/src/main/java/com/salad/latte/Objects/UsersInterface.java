package com.salad.latte.Objects;
/*
Use this interface to describe the methods that your observers (users) must follow

*/
public interface UsersInterface {
    void registerObserver();
    void removeObserver();
    void notifyObservers();
}
