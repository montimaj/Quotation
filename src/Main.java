import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

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
public class Main {
    private ArrayList<Product> mProductList;

    private static final Main MAIN = new Main();
    private static final float NIL_VALUE = 9999999.99f;

    Main() {
        mProductList = new ArrayList<>();
    }

    public static void main(String[] args) {
        try {
            MAIN.initAllProducts(args);
            MAIN.generateOptimalProducts();
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void initAllProducts(String[] sheets) throws IOException {
        FileInputStream excelFile = new FileInputStream(sheets[0]);
        XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
        for(int i = 1; i < sheets.length; ++i) {
            Sheet sheet = workbook.getSheetAt(Integer.parseInt(sheets[i]));
            for (Row nextRow : sheet) {
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                Product product = getProduct(cellIterator);
                if (product != null) {
                    product.setVendor(sheet.getSheetName());
                    Product product1 = getProduct(product);
                    if (product1 != null && product1.getPrice() > product.getPrice()) {
                        mProductList.remove(product1);
                    }
                    if (!mProductList.contains(product)) mProductList.add(product);
                }
            }
        }
        workbook.close();
        excelFile.close();
    }

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
                if(price == 0.0f)   price = NIL_VALUE;
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
                    if(price == NIL_VALUE)  cell.setCellValue("-");
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
        FileOutputStream outputStream = new FileOutputStream("Quotation.xlsx");
        workbook.write(outputStream);
        workbook.close();
    }
}