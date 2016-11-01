package com.yuntong.arsearch.bean;

/**
 * 浏览记录数据
 */
public class HistoryBean  {

    private String sceneId;
    private String sceneName;
    private String sceneAdress;
    private String createTime;
    private String thumbUrl;

    public String getSceneAdress() {
        return sceneAdress;
    }

    public void setSceneAdress(String sceneAdress) {
        this.sceneAdress = sceneAdress;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }
}
