package main;

import java.util.Comparator;

/**
 * @author Sayantan Majumdar (monti.majumdar@gmail.com)
 */
public class Product implements Comparable<Product> {
    private String mName;
    private String mVendor;
    private float mPrice;
    private int mSerialNo;

    public static final Comparator<Product> SERIAL_NO = Comparator.comparingInt(o -> o.mSerialNo);
    public static final int NUM_FIELDS = 4;

    public Product() {}

    public Product(int serialNo, String vendor, String name, float price) {
        mSerialNo = serialNo;
        mVendor = vendor.substring(vendor.indexOf("-") + 1);
        if(mVendor.isEmpty())   mVendor = vendor;
        mName = name;
        mPrice = price;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof Product)) return false;
        Product other = (Product) obj;
        return mName.equalsIgnoreCase(other.mName);
    }

    @Override
    public int compareTo(Product other) { return SERIAL_NO.compare(this, other); }

    @Override
    public String toString() { return mSerialNo + ", " + mVendor + ", " +  mName + ", " + mPrice; }

    public void setVendor(String vendor) {
        mVendor = vendor.substring(vendor.indexOf("-") + 1);
        if(mVendor.isEmpty())   mVendor = vendor;
    }
    public void setName(String name) { mName = name; }
    public void setPrice(float price) { mPrice = Float.parseFloat(String.format("%.2f", price)); }
    public void setSerialNo(int serialNo) { mSerialNo = serialNo; }

    public String getVendor() { return mVendor; }
    public String getName() { return  mName; }
    public float getPrice() { return  mPrice; }
    public int getSerialNo() { return mSerialNo; }
}