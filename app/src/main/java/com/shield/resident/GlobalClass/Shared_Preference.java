package com.shield.resident.GlobalClass;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


/**
 * Created by Developer on 7/19/17.
 */

public class Shared_Preference {
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    GlobalClass globalclass;
    private boolean pref_logInStatus;
    private String pref_name;
    private String pref_fname;
    private String pref_lname;
    private String pref_id;
    private String list_id;
    private String pref_email;
    private String pref_phone_number;
    private String pref_order_id;
    private String pref_order_type;
    private String pref_business;
    private String pref_user_type;
    private String pref_profile_img;
    private String pref_shipping_address;
    private String pref_cart_no;
    private String pref_ship_address_id;
    private String pref_ship_full_address;
    private String pref_organisation;
    String user_type;
    String flat_no;
    String flat_name;
    String block;
    String complex_name;
    String complex_id;
    String is_login;
    String first_time_login;


    private String remote_user_id;

    private String fcm;
    private String login_from;


    private static final String PREFS_NAME = "preferences";
    private static final String PREFS_NAME2 = "preferences2";

    private static final String PREF_logInStatus = "logInStatus";
    private static final String PREF_firstlogin = "firstlogin";
    private static final String PREF_name = "name";
    private static final String PREF_fname = "fname";
    private static final String PREF_lname = "lname";
    private static final String PREF_email = "user_email";
    private static final String PREF_phone_number = "phone_number";
    private static final String PREF_business = "business";
    private static final String PREF_id = "id";
    private static final String PREF_profile_img = "profile_img";
    private static final String PREF_cart_no = "cart_no";
    private static final String PREF_ship_address_id = "ship_address_id";
    private static final String PREF_ship_full_address = "ship_full_address";
    private static final String PREF_login_from = "login_from";
    private static final String PREF_organisation = "organisation";
    private static final String PREF_order_id = "order_id";
    private static final String PREF_order_type = "ordertype";
    private static final String remote = "remote_id";
    private static final String prefuser_type="prefuser_type";
    private static final String prefflat_no="prefflat_no";
    private static final String prefflat_name="prefflat_name";
    private static final String prefblock="prefblock";
    private static final String prefcomplex_name="prefcomplex_name";
    private static final String prefcomplex_id="prefcomplex_id";
    private static final String prefis_login="prefis_login";
    private static final String preffirst_time_login="preffirst_time_login";
    private static final String tenant="tenant";
    private static final String payment_system="payment_system";
    private static final String address="address";
    private static final String payu_salt="payu_salt";
    private static final String payu_mid="payu_mid";
    private static final String payu_mkey="payu_mkey";
    private static final String parking_no="parking_no";
    private static final String parking_id="parking_id";
    private static final String owner="owner";


    public Shared_Preference(Context context) {
        this.context = context;

        this.globalclass = (GlobalClass) context.getApplicationContext();
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();

    }

    public void saveFirstLogin(boolean val){
        editor.putBoolean(PREF_firstlogin, val);
        editor.commit();
    }

    public boolean isFirstLogin(){
        return sharedPreferences.getBoolean(PREF_firstlogin, true);
    }

    public boolean isLogin(){
        return sharedPreferences.getBoolean(PREF_logInStatus, false);
    }

    public void setPref_logInStatus(boolean val){
        editor.putBoolean(PREF_logInStatus, val);
        editor.commit();
    }

    public void savePrefrence() {

        if (globalclass.getLogin_status()) {

            editor.putString(remote,remote_user_id);
            pref_logInStatus = globalclass.getLogin_status();
            editor.putBoolean(PREF_logInStatus, pref_logInStatus);

            pref_name = globalclass.getName();
            editor.putString(PREF_name, pref_name);

            pref_fname = globalclass.getFname();
            editor.putString(PREF_fname, pref_fname);

            pref_lname = globalclass.getLname();
            editor.putString(PREF_lname, pref_lname);

            block = globalclass.getBlock();
            editor.putString(prefblock, block);


            user_type = globalclass.getUser_type();
            editor.putString(prefuser_type, user_type);


            flat_no = globalclass.getFlat_id();
            editor.putString(prefflat_no, flat_no);


            flat_name = globalclass.getFlat_name();
            editor.putString(prefflat_name, flat_name);


            complex_name = globalclass.getComplex_name();
            editor.putString(prefcomplex_name, complex_name);

            complex_id = globalclass.getComplex_id();
            editor.putString(prefcomplex_id, complex_id);


            is_login = globalclass.getIs_login();
            editor.putString(prefis_login, is_login);


            first_time_login = globalclass.getFirst_time_login();
            editor.putString(preffirst_time_login, first_time_login);

            pref_id= globalclass.getId();
            editor.putString(PREF_id,pref_id);

            editor.putString(PREF_id,pref_id);

            pref_email= globalclass.getEmail();
            editor.putString(PREF_email,pref_email);

            pref_phone_number= globalclass.getPhone_number();
            editor.putString(PREF_phone_number,pref_phone_number);

            editor.putString(PREF_order_id,pref_order_id);

            editor.putString(PREF_order_type,pref_order_type);

            editor.putString(PREF_business,pref_business);


            pref_profile_img = globalclass.getProfil_pic();
            editor.putString(PREF_profile_img, pref_profile_img);

            editor.putString(PREF_organisation, pref_organisation);

            pref_cart_no = globalclass.getCart_no();
            editor.putString(PREF_cart_no, pref_cart_no);


            login_from = globalclass.getLogin_from();
            editor.putString(PREF_login_from, login_from);

            editor.putString(tenant, globalclass.getIs_tenant());

            editor.putString(payment_system, globalclass.getPayment_system());

            editor.putString(address, globalclass.getComplex_address());

            editor.putString(payu_mkey, globalclass.getPayu_mkey());
            editor.putString(payu_mid, globalclass.getPayu_mid());
            editor.putString(payu_salt, globalclass.getPayu_salt());
            editor.putString(parking_no, globalclass.getParking_no());
            editor.putString(parking_id, globalclass.getParking_id());
            editor.putString(owner, globalclass.getOwner());


            editor.commit();

        }else{
            // dont save anything, if user is logged out
            pref_logInStatus = globalclass.getLogin_status();
            editor.putBoolean(PREF_logInStatus, pref_logInStatus);
            editor.commit();
        }

    }

    public void loadPrefrence() {
        pref_logInStatus = sharedPreferences.getBoolean(PREF_logInStatus, false);
        globalclass.setLogin_status(pref_logInStatus);

        Log.d("TV", globalclass.getLogin_status() + "");
        if (globalclass.getLogin_status()) {
            remote_user_id=sharedPreferences.getString(remote,"");
          //  globalclass.setRemote_user_id(remote_user_id);
            pref_name = sharedPreferences.getString(PREF_name, "");
            globalclass.setName(pref_name);

            pref_fname = sharedPreferences.getString(PREF_fname, "");
            globalclass.setFname(pref_fname);


            block = sharedPreferences.getString(prefblock, "");
            globalclass.setBlock(block);


            complex_id = sharedPreferences.getString(prefcomplex_id, "");
            globalclass.setComplex_id(complex_id);


            complex_name = sharedPreferences.getString(prefcomplex_name, "");
            globalclass.setComplex_name(complex_name);

            pref_lname = sharedPreferences.getString(PREF_lname, "");
            globalclass.setLname(pref_lname);

            pref_id= sharedPreferences.getString(PREF_id,"");
            globalclass.setId(pref_id);


            flat_no= sharedPreferences.getString(prefflat_no,"");
            globalclass.setFlat_id(flat_no);
            flat_name= sharedPreferences.getString(prefflat_name,"");
            globalclass.setFlat_name(flat_name);

            complex_id= sharedPreferences.getString(prefcomplex_id,"");
            globalclass.setComplex_id(complex_id);

            pref_phone_number=sharedPreferences.getString(PREF_phone_number,"");
            globalclass.setPhone_number(pref_phone_number);

            pref_email=sharedPreferences.getString(PREF_email,"");
            globalclass.setEmail(pref_email);

            pref_organisation=sharedPreferences.getString(PREF_organisation,"");

            pref_cart_no=sharedPreferences.getString(PREF_cart_no,"");
            globalclass.setCart_no(pref_cart_no);

            pref_order_id=sharedPreferences.getString(PREF_order_id,"");

            pref_order_type=sharedPreferences.getString(PREF_order_type,"");


            pref_profile_img=sharedPreferences.getString(PREF_profile_img,"");
            globalclass.setProfil_pic(pref_profile_img);


            login_from=sharedPreferences.getString(PREF_login_from,"");
            globalclass.setLogin_from(login_from);

            globalclass.setIs_tenant(sharedPreferences.getString(tenant,""));

            globalclass.setPayment_system(sharedPreferences.getString(payment_system,""));

            globalclass.setUser_type(sharedPreferences.getString(prefuser_type,""));

            globalclass.setComplex_address(sharedPreferences.getString(address,""));


            globalclass.setPayu_mkey(sharedPreferences.getString(payu_mkey,""));
            globalclass.setPayu_mid(sharedPreferences.getString(payu_mid,""));
            globalclass.setPayu_salt(sharedPreferences.getString(payu_salt,""));
            globalclass.setParking_no(sharedPreferences.getString(parking_no,""));
            globalclass.setParking_id(sharedPreferences.getString(parking_id,""));
            globalclass.setOwner(sharedPreferences.getString(owner,""));

        }
    }

    public void clearPreference(){
        editor.clear();
        editor.commit();
    }













}
