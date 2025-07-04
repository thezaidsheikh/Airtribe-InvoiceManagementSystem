// package service;

// import java.io.FileWriter;
// import java.io.IOException;
// import java.util.List;
// import java.util.Scanner;

// import com.itextpdf.text.Document;
// import com.itextpdf.text.DocumentException;
// import com.itextpdf.text.Font;
// import com.itextpdf.text.FontFactory;
// import com.itextpdf.text.Paragraph;
// import com.itextpdf.text.pdf.PdfPTable;
// import com.itextpdf.text.pdf.PdfWriter;
// import com.itextpdf.text.BaseColor;
// import com.itextpdf.text.Element;
// import com.itextpdf.text.pdf.PdfPCell;

// import common.utils;
// import model.Invoice;
// import model.InvoiceItem;

// import java.io.FileOutputStream;

// public class InvoiceExportService {

// public enum ExportFormat {
// PDF, CSV, JSON
// }

// private final Scanner scanner;

// public InvoiceExportService(Scanner scanner) {
// this.scanner = scanner;
// }

// /**
// * Export invoice to specified format
// */
// public void exportInvoice(Invoice invoice, ExportFormat format, String
// filePath) {
// if (invoice == null) {
// System.out.println("Invoice is null. Cannot export.");
// return;
// }

// try {
// switch (format) {
// case PDF:
// exportToPDF(invoice, filePath);
// break;
// case CSV:
// exportToCSV(invoice, filePath);
// break;
// case JSON:
// exportToJSON(invoice, filePath);
// break;
// default:
// System.out.println("Unsupported export format: " + format);
// }
// } catch (Exception e) {
// System.out.println("Error exporting invoice: " + e.getMessage());
// }
// }

// /**
// * Export invoice to PDF format
// */
// private void exportToPDF(Invoice invoice, String filePath) throws
// DocumentException, IOException {
// Document document = new Document();
// PdfWriter.getInstance(document, new FileOutputStream(filePath));
// document.open();

// // Title
// Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18,
// BaseColor.BLUE);
// Paragraph title = new Paragraph("INVOICE", titleFont);
// title.setAlignment(Element.ALIGN_CENTER);
// document.add(title);

// document.add(new Paragraph(" ")); // Empty line

// // Invoice Header Information
// Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
// Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

// document.add(new Paragraph("Invoice ID: " + invoice.getInvoiceId(),
// headerFont));
// document.add(new Paragraph("Invoice Date: " +
// utils.convertEpochToDateTime(invoice.getInvoiceDate()), normalFont));
// document.add(new Paragraph("Due Date: " +
// utils.convertEpochToDateTime(invoice.getDueDate()), normalFont));
// document.add(new Paragraph("Status: " + invoice.getStatus().toString(),
// normalFont));

// document.add(new Paragraph(" ")); // Empty line

// // Customer Information
// document.add(new Paragraph("BILL TO:", headerFont));
// document.add(new Paragraph("Customer ID: " + invoice.getCustomerId(),
// normalFont));
// document.add(new Paragraph("Customer Name: " + invoice.getCustomerName(),
// normalFont));
// if (invoice.getCustomerDetail() != null) {
// document.add(new Paragraph("Email: " +
// invoice.getCustomerDetail().getEmail(), normalFont));
// document.add(new Paragraph("Phone: " +
// invoice.getCustomerDetail().getPhone(), normalFont));
// document.add(new Paragraph("Address: " +
// invoice.getCustomerDetail().getAddress(), normalFont));
// }

// document.add(new Paragraph(" ")); // Empty line

// // Items Table
// PdfPTable table = new PdfPTable(5); // 5 columns
// table.setWidthPercentage(100);
// table.setSpacingBefore(10f);
// table.setSpacingAfter(10f);

// // Table Headers
// Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10,
// BaseColor.WHITE);
// PdfPCell[] headerCells = {
// new PdfPCell(new Paragraph("Product ID", tableHeaderFont)),
// new PdfPCell(new Paragraph("Product Name", tableHeaderFont)),
// new PdfPCell(new Paragraph("Quantity", tableHeaderFont)),
// new PdfPCell(new Paragraph("Unit Price", tableHeaderFont)),
// new PdfPCell(new Paragraph("Total", tableHeaderFont))
// };

// for (PdfPCell cell : headerCells) {
// cell.setBackgroundColor(BaseColor.GRAY);
// cell.setHorizontalAlignment(Element.ALIGN_CENTER);
// table.addCell(cell);
// }

// // Table Data
// Font tableDataFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
// for (InvoiceItem item : invoice.getItems()) {
// table.addCell(new PdfPCell(new Paragraph(String.valueOf(item.getProductId()),
// tableDataFont)));
// table.addCell(new PdfPCell(new Paragraph(item.getProductDetail().getName(),
// tableDataFont)));
// table.addCell(new PdfPCell(new Paragraph(String.valueOf(item.getQuantity()),
// tableDataFont)));
// table.addCell(new PdfPCell(new Paragraph("$" +
// String.valueOf(item.getProductDetail().getBasePrice()), tableDataFont)));
// table.addCell(new PdfPCell(new Paragraph("$" +
// String.valueOf(item.getProductDetail().getBasePrice() * item.getQuantity()),
// tableDataFont)));
// }

// document.add(table);

// // Totals
// document.add(new Paragraph(" ")); // Empty line
// document.add(new Paragraph("Subtotal: $" + invoice.getSubtotal(),
// normalFont));
// document.add(new Paragraph("Tax Amount: $" + invoice.getTaxAmount(),
// normalFont));
// document.add(new Paragraph("Discount: $" + invoice.getDiscountAmount(),
// normalFont));
// document.add(new Paragraph("TOTAL AMOUNT: $" + invoice.getFinalAmount(),
// headerFont));

// // Payment Information
// if (invoice.getPaymentMethod() != null) {
// document.add(new Paragraph(" ")); // Empty line
// document.add(new Paragraph("Payment Method: " +
// invoice.getPaymentMethod().toString(), normalFont));
// }

// // Notes
// if (invoice.getNotes() != null && !invoice.getNotes().trim().isEmpty()) {
// document.add(new Paragraph(" ")); // Empty line
// document.add(new Paragraph("Notes: " + invoice.getNotes(), normalFont));
// }

// document.close();
// System.out.println("Invoice exported to PDF successfully: " + filePath);
// }

// /**
// * Export invoice to CSV format
// */
// private void exportToCSV(Invoice invoice, String filePath) throws IOException
// {
// try (FileWriter writer = new FileWriter(filePath)) {
// // Write header
// writer.append("Invoice ID,Customer ID,Customer Name,Invoice Date,Due
// Date,Status,Payment Method,Product ID,Product Name,Quantity,Unit
// Price,Total,Subtotal,Tax Amount,Discount Amount,Final Amount,Notes\n");

// // Write data for each item
// for (InvoiceItem item : invoice.getItems()) {
// writer.append(invoice.getInvoiceId()).append(",");
// writer.append(String.valueOf(invoice.getCustomerId())).append(",");
// writer.append("\"").append(invoice.getCustomerName()).append("\"").append(",");
// writer.append(utils.convertEpochToDateTime(invoice.getInvoiceDate())).append(",");
// writer.append(utils.convertEpochToDateTime(invoice.getDueDate())).append(",");
// writer.append(invoice.getStatus().toString()).append(",");
// writer.append(invoice.getPaymentMethod() != null ?
// invoice.getPaymentMethod().toString() : "").append(",");
// writer.append(String.valueOf(item.getProductId())).append(",");
// writer.append("\"").append(item.getProductDetail().getName()).append("\"").append(",");
// writer.append(String.valueOf(item.getQuantity())).append(",");
// writer.append(String.valueOf(item.getProductDetail().getBasePrice())).append(",");
// writer.append(String.valueOf(item.getProductDetail().getBasePrice() *
// item.getQuantity())).append(",");
// writer.append(String.valueOf(invoice.getSubtotal())).append(",");
// writer.append(String.valueOf(invoice.getTaxAmount())).append(",");
// writer.append(String.valueOf(invoice.getDiscountAmount())).append(",");
// writer.append(String.valueOf(invoice.getFinalAmount())).append(",");
// writer.append("\"").append(invoice.getNotes() != null ? invoice.getNotes() :
// "").append("\"");
// writer.append("\n");
// }

// System.out.println("Invoice exported to CSV successfully: " + filePath);
// }
// }

// /**
// * Export invoice to JSON format
// */
// private void exportToJSON(Invoice invoice, String filePath) throws
// IOException {
// try (FileWriter writer = new FileWriter(filePath)) {
// writer.write("{\n");
// writer.write(" \"invoiceId\": \"" + invoice.getInvoiceId() + "\",\n");
// writer.write(" \"customerId\": " + invoice.getCustomerId() + ",\n");
// writer.write(" \"customerName\": \"" + invoice.getCustomerName() + "\",\n");
// writer.write(" \"invoiceDate\": \"" +
// utils.convertEpochToDateTime(invoice.getInvoiceDate()) + "\",\n");
// writer.write(" \"dueDate\": \"" +
// utils.convertEpochToDateTime(invoice.getDueDate()) + "\",\n");
// writer.write(" \"status\": \"" + invoice.getStatus().toString() + "\",\n");
// writer.write(" \"paymentMethod\": \"" + (invoice.getPaymentMethod() != null ?
// invoice.getPaymentMethod().toString() : "") + "\",\n");

// // Customer details
// if (invoice.getCustomerDetail() != null) {
// writer.write(" \"customerDetails\": {\n");
// writer.write(" \"email\": \"" + invoice.getCustomerDetail().getEmail() +
// "\",\n");
// writer.write(" \"phone\": \"" + invoice.getCustomerDetail().getPhone() +
// "\",\n");
// writer.write(" \"address\": \"" + invoice.getCustomerDetail().getAddress() +
// "\"\n");
// writer.write(" },\n");
// }

// // Items
// writer.write(" \"items\": [\n");
// for (int i = 0; i < invoice.getItems().size(); i++) {
// InvoiceItem item = invoice.getItems().get(i);
// writer.write(" {\n");
// writer.write(" \"productId\": " + item.getProductId() + ",\n");
// writer.write(" \"productName\": \"" + item.getProductDetail().getName() +
// "\",\n");
// writer.write(" \"quantity\": " + item.getQuantity() + ",\n");
// writer.write(" \"unitPrice\": " + item.getProductDetail().getBasePrice() +
// ",\n");
// writer.write(" \"total\": " + (item.getProductDetail().getBasePrice() *
// item.getQuantity()) + "\n");
// writer.write(" }");
// if (i < invoice.getItems().size() - 1) {
// writer.write(",");
// }
// writer.write("\n");
// }
// writer.write(" ],\n");

// // Totals
// writer.write(" \"subtotal\": " + invoice.getSubtotal() + ",\n");
// writer.write(" \"taxAmount\": " + invoice.getTaxAmount() + ",\n");
// writer.write(" \"discountAmount\": " + invoice.getDiscountAmount() + ",\n");
// writer.write(" \"finalAmount\": " + invoice.getFinalAmount() + ",\n");
// writer.write(" \"notes\": \"" + (invoice.getNotes() != null ?
// invoice.getNotes() : "") + "\"\n");
// writer.write("}\n");

// System.out.println("Invoice exported to JSON successfully: " + filePath);
// }
// }

// /**
// * Interactive method to choose export format and export invoice
// */
// public void exportInvoiceInteractive(Invoice invoice) {
// if (invoice == null) {
// System.out.println("No invoice to export.");
// return;
// }

// System.out.println("========== EXPORT INVOICE ==========");
// System.out.println("Select export format:");
// System.out.println("1. PDF");
// System.out.println("2. CSV");
// System.out.println("3. JSON");
// System.out.print("Enter your choice (1-3): ");

// String choice = scanner.nextLine().trim();
// ExportFormat format = null;
// String extension = "";

// switch (choice) {
// case "1":
// format = ExportFormat.PDF;
// extension = ".pdf";
// break;
// case "2":
// format = ExportFormat.CSV;
// extension = ".csv";
// break;
// case "3":
// format = ExportFormat.JSON;
// extension = ".json";
// break;
// default:
// System.out.println("Invalid choice. Export cancelled.");
// return;
// }

// System.out.print("Enter file name (without extension): ");
// String fileName = scanner.nextLine().trim();

// if (fileName.isEmpty()) {
// fileName = "invoice_" + invoice.getInvoiceId();
// }

// String filePath = "./exports/" + fileName + extension;

// // Create exports directory if it doesn't exist
// java.io.File exportDir = new java.io.File("./exports");
// if (!exportDir.exists()) {
// exportDir.mkdirs();
// }

// exportInvoice(invoice, format, filePath);
// System.out.println("====================================");
// }
// }
