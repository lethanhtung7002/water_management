package model;

public class WaterPriceTier {
    private int idWaterPriceTier;
    private int idWaterPrice;
    private int tier;
    private int minConsumption;
    private int maxConsumption;
    private double price;

    public WaterPriceTier() {
    }

    public WaterPriceTier(int idWaterPriceTier, int idWaterPrice, int tier, int minConsumption, int maxConsumption, double price) {
        this.idWaterPriceTier = idWaterPriceTier;
        this.idWaterPrice = idWaterPrice;
        this.tier = tier;
        this.minConsumption = minConsumption;
        this.maxConsumption = maxConsumption;
        this.price = price;
    }

    public int getIdWaterPriceTier() {
        return idWaterPriceTier;
    }

    public void setIdWaterPriceTier(int idWaterPriceTier) {
        this.idWaterPriceTier = idWaterPriceTier;
    }

    public int getIdWaterPrice() {
        return idWaterPrice;
    }

    public void setIdWaterPrice(int idWaterPrice) {
        this.idWaterPrice = idWaterPrice;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public int getMinConsumption() {
        return minConsumption;
    }

    public void setMinConsumption(int minConsumption) {
        this.minConsumption = minConsumption;
    }

    public int getMaxConsumption() {
        return maxConsumption;
    }

    public void setMaxConsumption(int maxConsumption) {
        this.maxConsumption = maxConsumption;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
