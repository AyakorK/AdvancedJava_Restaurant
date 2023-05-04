package com.coding.restaurant.restaurant.controllers;

import java.io.FileOutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import com.coding.restaurant.restaurant.models.Bill;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class BillsController {

  public Button allBillsButton;
  @FXML
  private ListView<Bill> billsListView;

  @FXML
  private Button exportAllButton;

  @FXML
  Button exportButton;

  @FXML
  private Button monthBillsButton;

  public ObservableList<Bill> filteredBills() throws SQLException {
    DatabaseManager db = new DatabaseManager();
    List<Bill> bills = db.getBillsOfThisMonth().stream().sorted(Comparator.comparing(Bill::getBillDate)).toList();
    // Stream bills to sort them by date
    ObservableList<Bill> filteredBill = FXCollections.observableArrayList();
    filteredBill.addAll(bills);

    billsListView.setItems(filteredBill);
    exportButton.setText("Exporter (Mois)");
    return filteredBill;
  }

  public ObservableList<Bill> displayAllBills() throws SQLException {
    DatabaseManager db = new DatabaseManager();
    List<Bill> bills = db.getBills().stream().sorted(Comparator.comparing(Bill::getBillDate)).toList();
    ObservableList<Bill> filteredBill = FXCollections.observableArrayList();
    filteredBill.addAll(bills);

    billsListView.setItems(filteredBill);
    exportButton.setText("Exporter (Tout)");
    return filteredBill;
  }

  public void initialize() {
    try {
      billsListView.setItems(filteredBills());
    } catch (SQLException e) {
      e.printStackTrace();
    }

    displayCells();
  }

  public void displayCells() {
    billsListView.setCellFactory(new Callback<>() {
      @Override
      public ListCell<Bill> call(ListView<Bill> listView) {
        return new ListCell<>() {
          @Override
          protected void updateItem(Bill bill, boolean empty) {
            super.updateItem(bill, empty);
            if (empty || bill == null) {
              setText(null);
            } else {
              setText(bill.getBillDate() + " - " + bill.getAmount() + "€" + " - " + (bill.getType() ? "Bénéfice" : "Dépense"));
            }
          }
        };
      }
    });
  }

  public void exportBills() {
    try {
      // Initialize document
      Document document = new Document(PageSize.A4, 50, 50, 50, 50);
      String saveName = exportButton.getText().equals("Exporter (Mois)") ? "Factures_" + new SimpleDateFormat("MMMM_yyyy").format(new Date()) : "Toutes_les_factures";
      // save into the folder Factures (create it if it doesn't exist)
      PdfWriter.getInstance(document, new FileOutputStream("Factures/" + saveName + ".pdf"));
      document.open();

      // Add title and date
      Font titleFont = new Font(FontFamily.TIMES_ROMAN, 24, Font.BOLD);
//      Paragraph title = new Paragraph("Bills for " + new SimpleDateFormat("MMMM yyyy").format(new Date()), titleFont);
      String message = exportButton.getText().equals("Exporter (Mois)") ? "Factures de " + new SimpleDateFormat("MMMM yyyy").format(new Date()) : "Toutes les factures";
      Paragraph title = new Paragraph(message, titleFont);
      // Title will be "Bills for Month Year" if the user clicked on "Mois" button
      title.setAlignment(Paragraph.ALIGN_CENTER);
      document.add(title);

      // Add bills
      Font billFont = new Font(FontFamily.TIMES_ROMAN, 12, Font.NORMAL);


      billsListView.getItems().stream()
              .filter(Objects::nonNull)
              .forEach(bill -> {
                Paragraph billParagraph =
                        new Paragraph(bill.getBillDate() + " - " + bill.getAmount() + "€" + " - " + (bill.getType() ? "Bénéfice" : "Dépense"), billFont);
                try {
                  document.add(billParagraph);
                } catch (DocumentException e) {
                  throw new RuntimeException(e);
                }
              });

      System.out.println("Bills exported successfully!");

      // Close document
      document.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
