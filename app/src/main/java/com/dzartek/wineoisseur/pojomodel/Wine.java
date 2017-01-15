
package com.dzartek.wineoisseur.pojomodel;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Wine {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("winery")
    @Expose
    private String winery;
    @SerializedName("winery_id")
    @Expose
    private String wineryId;
    @SerializedName("varietal")
    @Expose
    private String varietal;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("vintage")
    @Expose
    private String vintage;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("snoothrank")
    @Expose
    private long snoothrank;
    @SerializedName("available")
    @Expose
    private long available;
    @SerializedName("num_merchants")
    @Expose
    private long numMerchants;
    @SerializedName("num_reviews")
    @Expose
    private long numReviews;

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The code
     */
    public String getCode() {
        return code;
    }

    /**
     * 
     * @param code
     *     The code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 
     * @return
     *     The region
     */
    public String getRegion() {
        return region;
    }

    /**
     * 
     * @param region
     *     The region
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * 
     * @return
     *     The winery
     */
    public String getWinery() {
        return winery;
    }

    /**
     * 
     * @param winery
     *     The winery
     */
    public void setWinery(String winery) {
        this.winery = winery;
    }

    /**
     * 
     * @return
     *     The wineryId
     */
    public String getWineryId() {
        return wineryId;
    }

    /**
     * 
     * @param wineryId
     *     The winery_id
     */
    public void setWineryId(String wineryId) {
        this.wineryId = wineryId;
    }

    /**
     * 
     * @return
     *     The varietal
     */
    public String getVarietal() {
        return varietal;
    }

    /**
     * 
     * @param varietal
     *     The varietal
     */
    public void setVarietal(String varietal) {
        this.varietal = varietal;
    }

    /**
     * 
     * @return
     *     The price
     */
    public String getPrice() {
        return price;
    }

    /**
     * 
     * @param price
     *     The price
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * 
     * @return
     *     The vintage
     */
    public String getVintage() {
        return vintage;
    }

    /**
     * 
     * @param vintage
     *     The vintage
     */
    public void setVintage(String vintage) {
        this.vintage = vintage;
    }

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The link
     */
    public String getLink() {
        return link;
    }

    /**
     * 
     * @param link
     *     The link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * 
     * @return
     *     The tags
     */
    public String getTags() {
        return tags;
    }

    /**
     * 
     * @param tags
     *     The tags
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * 
     * @return
     *     The image
     */
    public String getImage() {
        return image;
    }

    /**
     * 
     * @param image
     *     The image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * 
     * @return
     *     The snoothrank
     */
    public long getSnoothrank() {
        return snoothrank;
    }

    /**
     * 
     * @param snoothrank
     *     The snoothrank
     */
    public void setSnoothrank(long snoothrank) {
        this.snoothrank = snoothrank;
    }

    /**
     * 
     * @return
     *     The available
     */
    public long getAvailable() {
        return available;
    }

    /**
     * 
     * @param available
     *     The available
     */
    public void setAvailable(long available) {
        this.available = available;
    }

    /**
     * 
     * @return
     *     The numMerchants
     */
    public long getNumMerchants() {
        return numMerchants;
    }

    /**
     * 
     * @param numMerchants
     *     The num_merchants
     */
    public void setNumMerchants(long numMerchants) {
        this.numMerchants = numMerchants;
    }

    /**
     * 
     * @return
     *     The numReviews
     */
    public long getNumReviews() {
        return numReviews;
    }

    /**
     * 
     * @param numReviews
     *     The num_reviews
     */
    public void setNumReviews(long numReviews) {
        this.numReviews = numReviews;
    }

}
