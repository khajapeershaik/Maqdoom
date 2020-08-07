package com.project.maqdoom.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileResponse {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("name_ar")
        @Expose
        private String nameAr;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("dpimg")
        @Expose
        private String dpimg;
        @SerializedName("active")
        @Expose
        private String active;
        @SerializedName("is_seller")
        @Expose
        private String isSeller;
        @SerializedName("notifications")
        @Expose
        private String notifications;
        @SerializedName("seller_subscrption_status")
        @Expose
        private String sellerSubscrptionStatus;
        @SerializedName("approved")
        @Expose
        private String approved;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("created_at")
        @Expose
        private String createdAt;

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

        public String getActive() {
            return active;
        }

        public void setActive(String active) {
            this.active = active;
        }

        public String getIsSeller() {
            return isSeller;
        }

        public void setIsSeller(String isSeller) {
            this.isSeller = isSeller;
        }

        public String getNotifications() {
            return notifications;
        }

        public void setNotifications(String notifications) {
            this.notifications = notifications;
        }

        public String getSellerSubscrptionStatus() {
            return sellerSubscrptionStatus;
        }

        public void setSellerSubscrptionStatus(String sellerSubscrptionStatus) {
            this.sellerSubscrptionStatus = sellerSubscrptionStatus;
        }

        public String getApproved() {
            return approved;
        }

        public void setApproved(String approved) {
            this.approved = approved;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

    }