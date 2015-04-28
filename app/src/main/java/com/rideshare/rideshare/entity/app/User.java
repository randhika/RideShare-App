package com.rideshare.rideshare.entity.app;

import com.rideshare.rideshare.entity.app.sub.Car;
import com.rideshare.rideshare.entity.app.sub.Notification;
import com.rideshare.rideshare.entity.app.sub.UserRequest;
import com.rideshare.rideshare.entity.app.sub.UserRide;
import java.util.ArrayList;
import java.util.Date;

public class User {

    private String email;
    private String fullName;
    private Date created;
    private String phone;
    private Car car;
    private String profileImageUrl;
    private ArrayList<Notification> notifications;
    private ArrayList<UserRide> rides;
    private ArrayList<UserRequest> requests;
}
