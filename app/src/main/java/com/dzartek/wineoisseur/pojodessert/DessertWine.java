
package com.dzartek.wineoisseur.pojodessert;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class DessertWine {

    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("wines")
    @Expose
    private List<Wine> wines = new ArrayList<Wine>();

    /**
     * 
     * @return
     *     The meta
     */
    public Meta getMeta() {
        return meta;
    }

    /**
     * 
     * @param meta
     *     The meta
     */
    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    /**
     * 
     * @return
     *     The wines
     */
    public List<Wine> getWines() {
        return wines;
    }

    /**
     * 
     * @param wines
     *     The wines
     */
    public void setWines(List<Wine> wines) {
        this.wines = wines;
    }

}
