package sig.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author Abdelrhman
 *
 */
public class FileOperations {
    public ArrayList<InvoiceHeader> readFile(File invoiceHeaderFile, File invoiceLineFile) throws Exception {
        BufferedReader fileReader = null;
        String line = "";

        int numberIndex = 0;
        int dateIndex = 1;
        int customerNameIndex = 2;

        int itemNameIndex = 1;
        int itemPriceIndex = 2;
        int itemCountIndex = 3;

        try {
            fileReader = new BufferedReader(new FileReader(invoiceLineFile));
            ArrayList<InvoiceLine> invoiceItems = new ArrayList<InvoiceLine>();

            while ((line = fileReader.readLine()) != null) {
                // Get all tokens available in line
                String[] tokens = line.split(",");
                InvoiceLine invoiceLine = new InvoiceLine(Integer.parseInt(tokens[numberIndex]),
                        tokens[itemNameIndex],
                        Double.parseDouble(tokens[itemPriceIndex]),
                        Integer.parseInt((tokens[itemCountIndex]))
                );
                invoiceItems.add(invoiceLine);
            }


            fileReader = new BufferedReader(new FileReader(invoiceHeaderFile));
            ArrayList<InvoiceHeader> invoiceHeaders = new ArrayList<InvoiceHeader>();

            // Read the file line by line starting from the second line
            while ((line = fileReader.readLine()) != null) {
                // Get all tokens available in line
                String[] tokens = line.split(",");
                InvoiceHeader invoiceHeader = new InvoiceHeader();
                invoiceHeader.invoiceNum = Integer.parseInt(tokens[numberIndex]);
                invoiceHeader.invoiceDate = tokens[dateIndex];
                invoiceHeader.customerName = tokens[customerNameIndex];
                ArrayList<InvoiceLine> filteredInvoiceLines = (ArrayList<InvoiceLine>) invoiceItems.stream().filter(item -> item.invoiceNum == invoiceHeader.invoiceNum)
                        .collect(Collectors.toList());
                invoiceHeader.invoiceLines = filteredInvoiceLines;

                invoiceHeaders.add(invoiceHeader);
            }

            fileReader.close();

            return invoiceHeaders;

        } catch (Exception e) {
        	if ( e instanceof FileNotFoundException) {
        		throw new FileNotFoundException("File not found");
        	}
            System.out.println("Error in CsvFileReader !!!");
            return null;
        }
    }

    public void writeFile(ArrayList<InvoiceHeader> invoiceList) throws Exception {
        try {
            FileWriter fileWriter = new FileWriter(new File("src/resources/InvoiceHeader.csv"));
            FileWriter invoiceLineFileWriter = new FileWriter(new File("src/resources/InvoiceLine.csv"));

            for (int i = 0; i < invoiceList.size(); i++) {
                InvoiceHeader invoiceHeader = invoiceList.get(i);
                fileWriter.write( invoiceHeader.invoiceNum + ",");
                fileWriter.write( invoiceHeader.invoiceDate + ",");
                fileWriter.write( invoiceHeader.customerName);
                fileWriter.write("\n");

				if (invoiceHeader.invoiceLines != null) {
					for (int j = 0; j < invoiceHeader.invoiceLines.size(); j++) {
						InvoiceLine invoiceLine = invoiceHeader.invoiceLines.get(j);
						invoiceLineFileWriter.write(invoiceLine.invoiceNum + ",");
						invoiceLineFileWriter.write(invoiceLine.itemName + ",");
						invoiceLineFileWriter.write(invoiceLine.itemPrice + ",");
						invoiceLineFileWriter.write(String.valueOf(invoiceLine.itemsCount));
						invoiceLineFileWriter.write("\n");
					}
				}
                
            }

            fileWriter.close();
            invoiceLineFileWriter.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
