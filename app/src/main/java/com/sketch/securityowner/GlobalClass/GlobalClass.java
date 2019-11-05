package com.sketch.securityowner.GlobalClass;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.security.ProviderInstaller;
import com.sketch.securityowner.Font.FontsOverride;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Developer on 3/9/19.
 */

public class GlobalClass extends Application {

    String TAG = "app";

    public Boolean login_status = false;

    private static GlobalClass mInstance;
    public static final String CHANNEL_ID = "CustomNotification";

    String id;
    String name;
    String email;
    String phone_number;
    String fcm_reg_token;
    String deviceid;
    String profil_pic;
    String fname;
    String lname;
    String addressid;
    String user_type;
    String flat_no;
    String flat_name;
    String block;
    String complex_name;
    String complex_id;
    String is_login;
    String first_time_login;

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getFlat_no() {
        return flat_no;
    }

    public void setFlat_no(String flat_no) {
        this.flat_no = flat_no;
    }

    public String getFlat_name() {
        return flat_name;
    }

    public void setFlat_name(String flat_name) {
        this.flat_name = flat_name;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getComplex_name() {
        return complex_name;
    }

    public void setComplex_name(String complex_name) {
        this.complex_name = complex_name;
    }

    public String getComplex_id() {
        return complex_id;
    }

    public void setComplex_id(String complex_id) {
        this.complex_id = complex_id;
    }

    public String getIs_login() {
        return is_login;
    }

    public void setIs_login(String is_login) {
        this.is_login = is_login;
    }

    public String getFirst_time_login() {
        return first_time_login;
    }

    public void setFirst_time_login(String first_time_login) {
        this.first_time_login = first_time_login;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }



    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    String order_no;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    String order_id;

    public String getAddressid() {
        return addressid;
    }

    public void setAddressid(String addressid) {
        this.addressid = addressid;
    }



    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    String cat_id;

    public String device_type = "Android";
    public String login_from= "";
   public RequestQueue mRequestQueue;
    String currency_symbol;
    String grand_total;
    String taxPer;
    String taxAmount;
    String sub_total;
    String discount_amnt;
    String offer_coupon_applied = "";
    String discount_type = "";
    String discount_id = "";
    String discount_amount = "";
    String slot_to_deliver = "";

    String shipping_id, shipping_fname, shipping_lname, shipping_address, shipping_city, shipping_state,
            shipping_country, shipping_zip, shipping_mobile,shipping_full_address ;

    public static synchronized GlobalClass getInstance() {
        return mInstance;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        createNotificationChannel();
        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/Poppins-ExtraLight.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/Poppins-ExtraLight.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/Poppins-ExtraLight.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/Poppins-ExtraLight.ttf");
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public String getCurrency_symbol() {
        return currency_symbol;
    }

    public void setCurrency_symbol(String currency_symbol) {
        this.currency_symbol = currency_symbol;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public Boolean getLogin_status() {
        return login_status;
    }

    public void setLogin_status(Boolean login_status) {
        this.login_status = login_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getFcm_reg_token() {
        return fcm_reg_token;
    }

    public void setFcm_reg_token(String fcm_reg_token) {
        this.fcm_reg_token = fcm_reg_token;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getProfil_pic() {
        return profil_pic;
    }

    public void setProfil_pic(String profil_pic) {
        this.profil_pic = profil_pic;
    }

    public String getLogin_from() {
        return login_from;
    }

    public void setLogin_from(String login_from) {
        this.login_from = login_from;
    }

    ///////////////////////////////////////////////////////////
    //cart section


    public String cart_no= "0";

    public String getCart_no() {
        return cart_no;
    }

    public void setCart_no(String cart_no) {
        this.cart_no = cart_no;
    }

    public ArrayList<HashMap<String,String>> CART_item_list = new ArrayList<>();

    public ArrayList<HashMap<String, String>> getCART_item_list() {
        return CART_item_list;
    }

    public void setCART_item_list(ArrayList<HashMap<String, String>> CART_item_list) {
        this.CART_item_list = CART_item_list;
    }

    /////////////////////////////////////////////
    //shipping_details


    public String getShipping_id() {
        return shipping_id;
    }

    public void setShipping_id(String shipping_id) {
        this.shipping_id = shipping_id;
    }

    public String getShipping_fname() {
        return shipping_fname;
    }

    public void setShipping_fname(String shipping_fname) {
        this.shipping_fname = shipping_fname;
    }

    public String getShipping_lname() {
        return shipping_lname;
    }

    public void setShipping_lname(String shipping_lname) {
        this.shipping_lname = shipping_lname;
    }

    public String getShipping_address() {
        return shipping_address;
    }

    public void setShipping_address(String shipping_address) {
        this.shipping_address = shipping_address;
    }

    public String getShipping_city() {
        return shipping_city;
    }

    public void setShipping_city(String shipping_city) {
        this.shipping_city = shipping_city;
    }

    public String getShipping_state() {
        return shipping_state;
    }

    public void setShipping_state(String shipping_state) {
        this.shipping_state = shipping_state;
    }

    public String getShipping_country() {
        return shipping_country;
    }

    public void setShipping_country(String shipping_country) {
        this.shipping_country = shipping_country;
    }

    public String getShipping_zip() {
        return shipping_zip;
    }

    public void setShipping_zip(String shipping_zip) {
        this.shipping_zip = shipping_zip;
    }

    public String getShipping_mobile() {
        return shipping_mobile;
    }

    public void setShipping_mobile(String shipping_mobile) {
        this.shipping_mobile = shipping_mobile;
    }

    public String getShipping_full_address() {
        return shipping_full_address;
    }

    public void setShipping_full_address(String shipping_full_address) {
        this.shipping_full_address = shipping_full_address;
    }

    public String getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(String grand_total) {
        this.grand_total = grand_total;
    }


    public String getTaxPer() {
        return taxPer;
    }

    public void setTaxPer(String taxPer) {
        this.taxPer = taxPer;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getSub_total() {
        return sub_total;
    }

    public void setSub_total(String sub_total) {
        this.sub_total = sub_total;
    }

    public String getDiscount_amnt() {
        return discount_amnt;
    }

    public void setDiscount_amnt(String discount_amnt) {
        this.discount_amnt = discount_amnt;
    }

    public String getOffer_coupon_applied() {
        return offer_coupon_applied;
    }

    public void setOffer_coupon_applied(String offer_coupon_applied) {
        this.offer_coupon_applied = offer_coupon_applied;
    }


    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public String getDiscount_id() {
        return discount_id;
    }

    public void setDiscount_id(String discount_id) {
        this.discount_id = discount_id;
    }

    public String getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }

    /////////////////////////////////////////
   public ArrayList<String> GselectedString = new ArrayList<>();
    public ArrayList<String> Capcity_selectedString = new ArrayList<>();
    public ArrayList<String> Days_selectedString = new ArrayList<>();

    public ArrayList<String> getGselectedString() {
        return GselectedString;
    }

    public void setGselectedString(ArrayList<String> gselectedString) {
        GselectedString = gselectedString;
    }

    public ArrayList<String> getCapcity_selectedString() {
        return Capcity_selectedString;
    }

    public void setCapcity_selectedString(ArrayList<String> capcity_selectedString) {
        Capcity_selectedString = capcity_selectedString;
    }

    public ArrayList<String> getDays_selectedString() {
        return Days_selectedString;
    }

    public void setDays_selectedString(ArrayList<String> days_selectedString) {
        Days_selectedString = days_selectedString;
    }

    public String getSlot_to_deliver() {
        return slot_to_deliver;
    }

    public void setSlot_to_deliver(String slot_to_deliver) {
        this.slot_to_deliver = slot_to_deliver;
    }

    /////////////////////

    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "CustomNotification",
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

}
