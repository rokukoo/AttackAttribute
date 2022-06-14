package io.rokuko.attackattribute.config;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    private FileConfiguration wrapperConfig;

    public Config refresh(FileConfiguration fileConfiguration){
        this.wrapperConfig = fileConfiguration;
        return this;
    }

    public String attackIntervalName(){
        return wrapperConfig.getString("attributes.attack-interval.name");
    }

    public String attackIntervalReductionName(){
        return wrapperConfig.getString("attributes.attack-interval-reduction.name");
    }

    public String attackDistanceName(){
        return wrapperConfig.getString("attributes.attack-distance.name");
    }

}
