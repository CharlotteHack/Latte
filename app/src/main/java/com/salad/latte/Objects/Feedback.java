package com.salad.latte.Objects;

import androidx.annotation.Keep;

import java.io.Serializable;

public class Feedback implements Serializable {
    String email;
    String feedback;
    Feedback() {}
    @Keep
    public Feedback(String e, String f){
        email = e;
        feedback = f;
    }
}
