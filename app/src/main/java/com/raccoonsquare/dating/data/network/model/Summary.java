package com.thinkin_service.provider.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by suthakar@appoets.com on 29-06-2018.
 */
public class Summary {

    @SerializedName("rides")
    @Expose
    private int rides;
    @SerializedName("completed_rides")
    @Expose
    private int completedRides;
    @SerializedName("revenue")
    @Expose
    private Float  revenue;
    @SerializedName("montly_pass")
    @Expose
    private Float montlyPass;
    @SerializedName("montly_gains")
    @Expose
    private Float montlyGains;
    @SerializedName("cancel_rides")
    @Expose
    private int cancelRides;

    public int getRides() {
        return rides;
    }

    public void setRides(int rides) {
        this.rides = rides;
    }

    public int getCompletedRides() {
        return completedRides;
    }

    public void setCompletedRides(int completedRides) {
        this.rides = completedRides;
    }

    public Float  getRevenue() {
        return revenue;
    }

    public void setRevenue(Float  revenue) {
        this.revenue = revenue;
    }

    public Float getMontlyPass() {
        return montlyPass;
    }

    public void setMontlyPass(Float montlyPass) {
        this.montlyPass = montlyPass;
    }

    public Float getMontlyGains() {
        return montlyGains;
    }

    public void setMontlyGains(Float montlyGains) {
        this.montlyGains = montlyGains;
    }

    public int getCancelRides() {
        return cancelRides;
    }

    public void setCancelRides(int cancelRides) {
        this.cancelRides = cancelRides;
    }
}
