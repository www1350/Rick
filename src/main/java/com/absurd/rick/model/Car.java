package com.absurd.rick.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;


/**
 * @author Absurd.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Car implements Serializable{
    private String id;
    @ApiModelProperty(value = "车名",example = "阿斯顿马丁")
    private String name;
    protected String carShortName;
    protected String registerDate;

    protected String pictures;

    protected String shopCode;
    /**
     * 价格
     */
    @ApiModelProperty(value = "价格(万)",example = "66.6")
    protected Double price;

    /**
     * 指导价
     */
    protected Double priceGuide;

    /**
     * 品牌
     */
    protected String brand;

    private int version;
}
