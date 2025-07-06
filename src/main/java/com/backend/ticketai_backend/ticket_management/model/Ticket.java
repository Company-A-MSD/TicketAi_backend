package com.backend.ticketai_backend.ticket_management.model;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "Tickets")
public class Ticket {

    @Id
    private String _id;

    private String subject;
    private String description;
    private String status;
    private String priority;
    private String category;
    private ObjectId assigned;
    private ObjectId created;
    private Date created_at;
    private Date updated_at;
    private String channel;


    // Getters and Setters
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }   

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ObjectId getAssigned_to() {
        return assigned;
    }

    public void setAssigned_to(ObjectId assigned) {
        this.assigned = assigned;
    }

    public ObjectId getCreated_by() {
        return created;
    }

    public void setCreated_by(ObjectId created) {
        this.created = created;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }


}
