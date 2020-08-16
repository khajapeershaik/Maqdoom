package com.project.maqdoom.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetProfileResponse {


    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("name_ar")
    private String nameAr;

    @Expose
    @SerializedName("email")
    private String email;

    @Expose
    @SerializedName("active")
    private String active;

    @Expose
    @SerializedName("is_seller")
    private String is_seller;

    @Expose
    @SerializedName("updated_at")
    private String updated_at;

    @Expose
    @SerializedName("created_at")
    private String created_at;

    @Expose
    @SerializedName("phone")
    private String phone;

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

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getIs_seller() {
        return is_seller;
    }

    public void setIs_seller(String is_seller) {
        this.is_seller = is_seller;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDpimg() {
        return dpimg;
    }

    public void setDpimg(String dpimg) {
        this.dpimg = dpimg;
    }

    public String getSeller_subscrption_status() {
        return seller_subscrption_status;
    }

    public void setSeller_subscrption_status(String seller_subscrption_status) {
        this.seller_subscrption_status = seller_subscrption_status;
    }

    @Expose
    @SerializedName("dpimg")
    private String dpimg;

    @Expose
    @SerializedName("seller_subscrption_status")
    private String seller_subscrption_status;

    public GetProfileResponse(String id, String name, String nameAr, String email, String active, String is_seller, String updated_at, String created_at, String phone, String dpimg, String seller_subscrption_status) {
        this.id = id;
        this.name = name;
        this.nameAr = nameAr;
        this.email = email;
        this.active = active;
        this.is_seller = is_seller;
        this.updated_at = updated_at;
        this.created_at = created_at;
        this.phone = phone;
        this.dpimg = dpimg;
        this.seller_subscrption_status = seller_subscrption_status;
    }
}
