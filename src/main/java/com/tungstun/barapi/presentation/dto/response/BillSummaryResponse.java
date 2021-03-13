package com.tungstun.barapi.presentation.dto.response;

import com.tungstun.barapi.domain.Session;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Response detail summary about the bill")
public class BillSummaryResponse {
    @ApiModelProperty(notes = "The bill's id")
    private String id;

    @ApiModelProperty(notes = "The bill's payment state")
    private boolean isPayed;

    @ApiModelProperty(notes = "The bill's total price")
    private Double totalPrice;

//    @ApiModelProperty(notes = "The bill's session's date")
//    private LocalDateTime date;

    @ApiModelProperty(notes = "The bill's session")
    private Session session;

    public BillSummaryResponse() { }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public boolean isPayed() { return isPayed; }

    public void setPayed(boolean payed) { isPayed = payed; }

    public Double getTotalPrice() { return totalPrice; }

    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }

    public Session getSession() { return session; }

    public void setSession(Session session) { this.session = session; }
    //    public LocalDateTime getDate() { return date; }
//
//    public void setDate(LocalDateTime date) { this.date = date; }
}
