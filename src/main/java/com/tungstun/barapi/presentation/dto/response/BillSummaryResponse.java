package com.tungstun.barapi.presentation.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@ApiModel(description = "Response detail summary about the bill")
public class BillSummaryResponse {
    @ApiModelProperty(notes = "The bill's id")
    private String id;

    @ApiModelProperty(notes = "The bill's payment state")
    private boolean isPayed;

    @ApiModelProperty(notes = "The bill's total price")
    private Double totalPrice;

    @ApiModelProperty(notes = "The bill's session's date")
    private LocalDateTime date;

    public BillSummaryResponse() { }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public boolean isPayed() { return isPayed; }

    public void setPayed(boolean payed) { isPayed = payed; }

    public Double getTotalPrice() { return totalPrice; }

    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }

    public LocalDateTime getDate() { return date; }

    public void setDate(LocalDateTime date) { this.date = date; }
}
