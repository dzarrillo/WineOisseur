
package com.dzartek.wineoisseur.pojomodel;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Meta {

    @SerializedName("results")
    @Expose
    private long results;
    @SerializedName("returned")
    @Expose
    private long returned;
    @SerializedName("errmsg")
    @Expose
    private String errmsg;
    @SerializedName("status")
    @Expose
    private long status;

    /**
     * 
     * @return
     *     The results
     */
    public long getResults() {
        return results;
    }

    /**
     * 
     * @param results
     *     The results
     */
    public void setResults(long results) {
        this.results = results;
    }

    /**
     * 
     * @return
     *     The returned
     */
    public long getReturned() {
        return returned;
    }

    /**
     * 
     * @param returned
     *     The returned
     */
    public void setReturned(long returned) {
        this.returned = returned;
    }

    /**
     * 
     * @return
     *     The errmsg
     */
    public String getErrmsg() {
        return errmsg;
    }

    /**
     * 
     * @param errmsg
     *     The errmsg
     */
    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    /**
     * 
     * @return
     *     The status
     */
    public long getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(long status) {
        this.status = status;
    }

}
