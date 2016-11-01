package com.yuntong.arsearch.bean;

/**
 * 首页全景集合数据
 */
public class SceneListData {

    /**
     * 	sceneId: 500,
     	name: "镭之战真人cs",
     	address: "杭州市余杭区莫干山路1509号 西田城一楼A区001",
     	thumb: "http://images.duc.cn/group1/M00/2C/9D/c-dlNFc1RWaAfD-RAAINqDwoL8M321.png",
     	heat: 50,
     	panoId: 18072,
     	distance: 66,
     	lng: 120.253106,
     	lat: 30.208418,
     	fisheyeThumb: "http://image2.panocooker.com/group1/M00/D1/63/rBELClfiJTGAPso9AAFAALw1yu4267.png"
     */
    private int sceneId;
    private String name;
    private String address;
    private int thumb;
    private int heat;
    private int panoId;
    private double distance;
    private double lng;
    private double lat;
    private String fisheyeThumb;

    public int getSceneId() {
        return sceneId;
    }

    public void setSceneId(int sceneId) {
        this.sceneId = sceneId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getThumb() {
        return thumb;
    }

    public void setThumb(int thumb) {
        this.thumb = thumb;
    }

    public int getHeat() {
        return heat;
    }

    public void setHeat(int heat) {
        this.heat = heat;
    }

    public int getPanoId() {
        return panoId;
    }

    public void setPanoId(int panoId) {
        this.panoId = panoId;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getFisheyeThumb() {
        return fisheyeThumb;
    }

    public void setFisheyeThumb(String fisheyeThumb) {
        this.fisheyeThumb = fisheyeThumb;
    }
}
