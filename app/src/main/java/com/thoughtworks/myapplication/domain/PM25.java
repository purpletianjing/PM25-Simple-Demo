package com.thoughtworks.myapplication.domain;

import com.google.gson.annotations.SerializedName;

public class PM25 {
    @SerializedName("position_name")
    private String positionName;

    @SerializedName("quality")
    private String quality;

    @SerializedName("pm2_5")
    private String pm2_5;

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getPm2_5() {
        return pm2_5;
    }
}
