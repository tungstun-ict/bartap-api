package com.tungstun.barapi.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tungstun.barapi.domain.Bartender;
import com.tungstun.barapi.domain.bill.Bill;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "Response details about the session")
public class SessionResponse {
    @ApiModelProperty(notes = "The session's id")
    private Long id;

    @ApiModelProperty(notes = "The session's creation date")
    private LocalDateTime creationDate;

    @ApiModelProperty(notes = "The date the session got close")
    private LocalDateTime closedDate;

    @ApiModelProperty(notes = "Boolean if the session is locked")
    private boolean isLocked;

    @ApiModelProperty(notes = "The session's bartenders")
    private List<Bartender> bartenders;

    @ApiModelProperty(notes = "The session's bills")
    private List<Bill> bills;

    public SessionResponse() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreationDate() { return creationDate; }

    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

    public LocalDateTime getClosedDate() { return closedDate; }

    public void setClosedDate(LocalDateTime closedDate) { this.closedDate = closedDate; }

    public boolean isLocked() { return isLocked; }

    public void setLocked(boolean locked) { isLocked = locked; }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public List<Bartender> getBartenders() {
        return bartenders;
    }

    public void setBartenders(List<Bartender> bartenders) {
        this.bartenders = bartenders;
    }
}
