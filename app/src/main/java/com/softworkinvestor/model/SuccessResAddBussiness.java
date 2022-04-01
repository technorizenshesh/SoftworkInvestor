package com.softworkinvestor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuccessResAddBussiness
{
    @SerializedName("result")
    @Expose
    public Result result;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public String status;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class Result {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("user_id")
        @Expose
        public String userId;
        @SerializedName("category_id")
        @Expose
        public String categoryId;
        @SerializedName("bussiness_name")
        @Expose
        public String bussinessName;
        @SerializedName("bussiness_invest_amt")
        @Expose
        public String bussinessInvestAmt;
        @SerializedName("bussiness_monthly_amt")
        @Expose
        public String bussinessMonthlyAmt;
        @SerializedName("bussiness_detail")
        @Expose
        public String bussinessDetail;
        @SerializedName("image")
        @Expose
        public String image;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getBussinessName() {
            return bussinessName;
        }

        public void setBussinessName(String bussinessName) {
            this.bussinessName = bussinessName;
        }

        public String getBussinessInvestAmt() {
            return bussinessInvestAmt;
        }

        public void setBussinessInvestAmt(String bussinessInvestAmt) {
            this.bussinessInvestAmt = bussinessInvestAmt;
        }

        public String getBussinessMonthlyAmt() {
            return bussinessMonthlyAmt;
        }

        public void setBussinessMonthlyAmt(String bussinessMonthlyAmt) {
            this.bussinessMonthlyAmt = bussinessMonthlyAmt;
        }

        public String getBussinessDetail() {
            return bussinessDetail;
        }

        public void setBussinessDetail(String bussinessDetail) {
            this.bussinessDetail = bussinessDetail;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

    }
    
    
}
