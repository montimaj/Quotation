package main;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author Sayantan Majumdar (monti.majumdar@gmail.com)
 */
public class Quotation {
    private ArrayList<Product> mProductList;
    private File mExcelFile;
    private ArrayList<Integer> mSheets;
    private String mMessage = "";

    private static final String OUTPUT_FILE = File.separator + "Quotation.xlsx";

    public Quotation(String excelFile, String sheets) {
        mProductList = new ArrayList<>();
        mSheets = new ArrayList<>();
        mExcelFile = new File(excelFile);
        Scanner sc = new Scanner(sheets);
        while(sc.hasNext()) {
            mSheets.add(sc.nextInt() - 1);
        }
        if(mSheets.isEmpty()) {
            mSheets.add(0);
            mMessage = "Used sheet 1 only.\n";
        }
    }

    public void generateQuotation() throws IOException {
        initAllProducts();
        generateOptimalProducts();
        mMessage += "Quotation generated.\nCheck " + mExcelFile.getParent() + OUTPUT_FILE;
    }

    private void initAllProducts() throws IOException {
        FileInputStream fis = new FileInputStream(mExcelFile);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        for(int sheets: mSheets) {
            Sheet sheet = workbook.getSheetAt(sheets);
            for (Row nextRow : sheet) {
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                Product product = getProduct(cellIterator);
                if (product != null && !product.containsNullField()) {
                    product.setVendor(sheet.getSheetName());
                    Product product1 = getProduct(product);
                    if (product1 != null) {
                        if(product1.getPrice() > product.getPrice()) {
                            mProductList.remove(product1);
                        } else if(product1.getPrice() == product.getPrice()) {
                            mProductList.add(product);
                        }
                    }
                    if (!mProductList.contains(product)) mProductList.add(product);
                }
            }
        }
        workbook.close();
        fis.close();
    }

    public String getMessage() { return mMessage; }

    private Product getProduct(Product product) {
        for(Product product1: mProductList) {
            if(product1.equals(product))    return product1;
        }
        return null;
    }

    private Product getProduct(Iterator<Cell> cellIterator) throws IOException {
        Product product = new Product();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            int columnIndex = cell.getColumnIndex();
            if (columnIndex == 0) {
                try {
                    int sl = (int) cell.getNumericCellValue();
                    if(sl == 0) return null;
                    product.setSerialNo(sl);
                } catch (IllegalStateException e) { return null; }
            } else if (columnIndex == 1) {
                product.setName(cell.getStringCellValue());
            } else if (columnIndex == 3) {
                float price;
                price = (float) cell.getNumericCellValue();
                if(price == 0.0f)   price = Float.MAX_VALUE;
                product.setPrice(price);
            }
        }
        return product;
    }

    private void generateOptimalProducts() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Quotation");
        String[] columns = {"SL", "Vendor", "Item & Specification", "Rate Exclusive of Vat"};
        Row row = sheet.createRow(0);
        for (int colNum = 0; colNum < columns.length; ++colNum) {
            Cell cell = row.createCell(colNum);
            cell.setCellValue(columns[colNum]);
        }
        mProductList.sort(Product.SERIAL_NO);
        for(int rowNum = 0; rowNum < mProductList.size(); ++rowNum) {
            row = sheet.createRow(rowNum + 1);
            Product product = mProductList.get(rowNum);
            for(int colNum = 0; colNum < Product.NUM_FIELDS; ++colNum) {
                Cell cell = row.createCell(colNum);
                if(colNum == 0) {
                    cell.setCellValue(product.getSerialNo());
                } else if(colNum == 1) {
                    cell.setCellValue(product.getVendor());
                } else if(colNum == 2) {
                    cell.setCellValue(product.getName());
                } else {
                    float price = product.getPrice();
                    if(price == Float.MAX_VALUE)  cell.setCellValue("-");
                    else {
                        cell.setCellValue(product.getPrice());
                        CellStyle style = workbook.createCellStyle();
                        DataFormat format = workbook.createDataFormat();
                        style.setDataFormat(format.getFormat("#,##0.00"));
                        cell.setCellStyle(style);
                    }
                }
            }
        }
        FileOutputStream outputStream = new FileOutputStream(mExcelFile.getParent() + OUTPUT_FILE);
        workbook.write(outputStream);
        workbook.close();
    }
}