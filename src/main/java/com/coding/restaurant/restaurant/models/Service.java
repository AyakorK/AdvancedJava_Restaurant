package com.coding.restaurant.restaurant.models;

import com.coding.restaurant.restaurant.controllers.DatabaseManager;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.List;

public class Service {
  public Date getBeginDate() {
    return beginDate;
  }

  public String getPeriod() {
    return period;
  }

  public List<Worker> getWorkers() {
    return workers;
  }

  public Timestamp getCreatedAt() {
    return createdAt;
  }

  public String getServiceUUID() {
    return serviceUUID;
  }

  public Boolean isPaid() {
    return isPaid;
  }

  public void setIsPaid(Boolean isPaid) {
    this.isPaid = isPaid;
  }

  String serviceUUID;
  Date beginDate;
  String period;
  List<Worker> workers;

  Boolean isPaid;

  Timestamp createdAt;

  /**
   *
   * @param serviceUUID
   * @param beginDate
   * @param createdAt
   * @param period
   * @param workers
   * @param isPaid
   */
  public Service(String serviceUUID, Date beginDate, Timestamp createdAt, String period, List<Worker> workers, Boolean isPaid) {
    this.serviceUUID = serviceUUID;
    this.beginDate = beginDate;
    this.createdAt = createdAt;
    this.period = period;
    this.workers = workers;
    this.isPaid = isPaid;
  }

  /**
   * 
   * @param service
   * @return
   * @throws SQLException
   */
  public String getTimer(Service service) throws SQLException {
    Timestamp now = new Timestamp(System.currentTimeMillis());

    long maxTime = this.createdAt.getTime() + (3 * 60 * 60 * 1000);
    long nowTime = now.getTime();

    if (nowTime > maxTime) {

      if (Boolean.TRUE.equals(!service.isPaid())) {
        service.setIsPaid(true);
        DatabaseManager db = new DatabaseManager();
        db.endService(service);
      }
      return "Service terminé !";
    }

    long diff = maxTime - nowTime;
    String seconds = String.valueOf(diff / 1000 % 60);
    if (seconds.length() == 1) seconds = "0" + seconds;
    String minutes = String.valueOf(diff / (60 * 1000) % 60);
    if (minutes.length() == 1) minutes = "0" + minutes;
    String hours = String.valueOf(diff / (60 * 60 * 1000) % 24);
    if (hours.length() == 1) hours = "0" + hours;

    return hours + ":" + minutes + ":" + seconds;
  }
}
