package com.brkc.traffic.adapter;

/**
 * Created by Administrator on 16-4-22.
 */
public class VehicleResult {

    private int xh;
    private String plateNo;
    private String plateColor;
    private String crossName;
    private String crossPolice;
    private String passTime;
    private String image;
    private String thumb;

    public VehicleResult(int xh,String plateNo,String plateColor,
                        String crossName,String passTime,
                         String thumb, String image){
        this.xh = xh;
        this.plateNo = plateNo;
        this.plateColor = plateColor;
        this.crossName = crossName;
        this.passTime = passTime;
        this.thumb = thumb;
        this.image = image;
    }
    public String getCrossName() {
        return crossName;
    }

    public void setCrossName(String crossName) {
        this.crossName = crossName;
    }

    public String getCrossPolice() {
        return crossPolice;
    }

    public void setCrossPolice(String crossPolice) {
        this.crossPolice = crossPolice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPassTime() {
        return passTime;
    }

    public void setPassTime(String passTime) {
        this.passTime = passTime;
    }

    public String getPlateColor() {
        return plateColor;
    }

    public void setPlateColor(String plateColor) {
        this.plateColor = plateColor;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getXh() {
        return xh;
    }

    public void setXh(int xh) {
        this.xh = xh;
    }
}
