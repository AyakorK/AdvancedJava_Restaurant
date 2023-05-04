package com.coding.restaurant.restaurant.models;

import java.sql.Timestamp;
import java.sql.Date;
import java.util.List;

public class Service {
    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getServiceUUID() {
        return serviceUUID;
    }

    String serviceUUID;
    Date beginDate;
    String period;
    List<Worker> workers;

    Timestamp createdAt;
    public Service(String serviceUUID, Date beginDate, Timestamp createdAt, String period, List<Worker> workers) {
        this.serviceUUID = serviceUUID;
        this.beginDate = beginDate;
        this.createdAt = createdAt;
        this.period = period;
        this.workers = workers;
    }

    public String getTimer() {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        long maxTime = this.createdAt.getTime() + (3 * 60 * 60 * 1000);
        long nowTime = now.getTime();

        if (nowTime > maxTime) {
            return "Service terminé !";
            // TODO: End the Service + Give 3 hours to the workers
        }

        long diff = maxTime - nowTime;
        return String.format("%d:%d:%d", diff / (60 * 60 * 1000) % 24, diff / (60 * 1000) % 60, diff / 1000 % 60);
    }
}
