package sig.models;

import java.util.ArrayList;

/**
 * @author Abdelrhman
 *
 */
public class InvoiceHeader {
    public int invoiceNum;
    public String customerName;
    public String invoiceDate;
    public ArrayList<InvoiceLine> invoiceLines;

    public int getTotal() {
        int result = 0;
        for (InvoiceLine line : invoiceLines) {
            result += (line.itemPrice * line.itemsCount);
        }
        return result;
    }
}
