package tv.hooq.sampleapp.networks.requests.signup;

import com.google.gson.annotations.SerializedName;

public class WithActivate {

    @SerializedName("enabled")
    private boolean enabled;

    @SerializedName("activation")
    private Activation activation;

    public WithActivate() {
        //Empty Constructor
    }

    public WithActivate(boolean enabled, String sku) {
        this.enabled = enabled;
        this.activation = new Activation(sku);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Activation getActivation() {
        return activation;
    }

    public void setActivation(Activation activation) {
        this.activation = activation;
    }

    public class Activation {
        @SerializedName("sku")
        private String sku;

        public Activation() {
            //Empty Constructor
        }

        public Activation(String sku) {
            this.sku = sku;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }
    }
}
