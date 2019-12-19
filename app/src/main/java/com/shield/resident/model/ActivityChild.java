package com.shield.resident.model;

import java.io.Serializable;

public class ActivityChild implements Serializable {

    private String type;
    private String activity_id;
    private String name;
    private String mobile;
    private String qr_code;
    private String qr_code_image;
    private String activity_type; // visitor,
    private String visitor_type; // guest, cab, delivery, visiting_help, staff
    private String actual_in_time; // if blank - show on IN list
    private String actual_out_time; // if blank - show on OUT list
    private String visiting_time;
    private String visiting_date;
    private String visiting_help_cat; // visiting_help cat
    private String vehicle_no;
    private String profile_image;
    private String vendor_name;
    private String vendor_image;
    private String block;
    private String flat_no;
    private String flat_id;
    private String requested_by;
    private String type_in_out;
    private String approve_status;
    private String leave_at_gate_code;
    private String security_mobile;
    private String note;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    private String user_id;

    public String getApprove_by() {
        return approve_by;
    }

    public void setApprove_by(String approve_by) {
        this.approve_by = approve_by;
    }

    private String approve_by;

    public String getGetpass_image() {
        return getpass_image;
    }

    public void setGetpass_image(String getpass_image) {
        this.getpass_image = getpass_image;
    }

    private String getpass_image;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public String getGetpass() {
        return getpass;
    }

    public void setGetpass(String getpass) {
        this.getpass = getpass;
    }

    private String getpass;

    public String getSecurity_id() {
        return security_id;
    }

    public void setSecurity_id(String security_id) {
        this.security_id = security_id;
    }

    private String security_id;

    public String getVisitor_id() {
        return visitor_id;
    }

    public void setVisitor_id(String visitor_id) {
        this.visitor_id = visitor_id;
    }

    private String visitor_id;



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    public String getQr_code_image() {
        return qr_code_image;
    }

    public void setQr_code_image(String qr_code_image) {
        this.qr_code_image = qr_code_image;
    }

    public String getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(String activity_type) {
        this.activity_type = activity_type;
    }

    public String getVisitor_type() {
        return visitor_type;
    }

    public void setVisitor_type(String visitor_type) {
        this.visitor_type = visitor_type;
    }

    public String getActual_in_time() {
        return actual_in_time;
    }

    public void setActual_in_time(String actual_in_time) {
        this.actual_in_time = actual_in_time;
    }

    public String getActual_out_time() {
        return actual_out_time;
    }

    public void setActual_out_time(String actual_out_time) {
        this.actual_out_time = actual_out_time;
    }

    public String getVisiting_time() {
        return visiting_time;
    }

    public void setVisiting_time(String visiting_time) {
        this.visiting_time = visiting_time;
    }

    public String getVisiting_date() {
        return visiting_date;
    }

    public void setVisiting_date(String visiting_date) {
        this.visiting_date = visiting_date;
    }

    public String getVisiting_help_cat() {
        return visiting_help_cat;
    }

    public void setVisiting_help_cat(String visiting_help_cat) {
        this.visiting_help_cat = visiting_help_cat;
    }

    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }

    public String getVendor_image() {
        return vendor_image;
    }

    public void setVendor_image(String vendor_image) {
        this.vendor_image = vendor_image;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getFlat_no() {
        return flat_no;
    }

    public void setFlat_no(String flat_no) {
        this.flat_no = flat_no;
    }

    public String getRequested_by() {
        return requested_by;
    }

    public void setRequested_by(String requested_by) {
        this.requested_by = requested_by;
    }

    public String getType_in_out() {
        return type_in_out;
    }

    public void setType_in_out(String type_in_out) {
        this.type_in_out = type_in_out;
    }

    public String getApprove_status() {
        return approve_status;
    }

    public void setApprove_status(String approve_status) {
        this.approve_status = approve_status;
    }

    public String getFlat_id() {
        return flat_id;
    }

    public void setFlat_id(String flat_id) {
        this.flat_id = flat_id;
    }

    public String getLeave_at_gate_code() {
        return leave_at_gate_code;
    }

    public void setLeave_at_gate_code(String leave_at_gate_code) {
        this.leave_at_gate_code = leave_at_gate_code;
    }

    public String getSecurity_mobile() {
        return security_mobile;
    }

    public void setSecurity_mobile(String security_mobile) {
        this.security_mobile = security_mobile;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}


