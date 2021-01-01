package com.kachundena.kreference.model;

import java.util.Date;

public class reference {
    private int reference_id;
    private String description;
    private String url;
    private Date start_date;
    private String metadata;

    public reference() {
    }

    public reference(int reference_id, String description, String url, Date start_date, String metadata) {
        this.reference_id = reference_id;
        this.description = description;
        this.url = url;
        this.start_date = start_date;
        this.metadata = metadata;
    }

    public int getReference_id() {
        return reference_id;
    }

    public void setReference_id(int reference_id) {
        this.reference_id = reference_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
