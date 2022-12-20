package sig.listners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import sig.SalesInvoiceGeneratorApp;
import sig.exceptions.FileFormatException;
import sig.models.FileOperations;
import sig.models.InvoiceHeader;

/**
 * @author Abdelrhman
 *
 */
public class MenuBarListner implements ActionListener{

	private SalesInvoiceGeneratorApp salesInvoiceGenerator;

	public MenuBarListner(SalesInvoiceGeneratorApp salesInvoiceGenerator) {
		this.salesInvoiceGenerator = salesInvoiceGenerator;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == salesInvoiceGenerator.getSaveFile()) {
			salesInvoiceGenerator.toCsv();
		}

		else if (e.getSource() == salesInvoiceGenerator.getLoadFile()) {
			try {
				ArrayList<File> selectedFiles = new ArrayList<File>();
				openFileBrowser(selectedFiles);
				FileOperations fileOperations = new FileOperations();
				if (selectedFiles != null && selectedFiles.size() > 0) {
					File invoiceHeaderFile = selectedFiles.get(0);
					File invoiceLineFile = selectedFiles.get(1);
					ArrayList<InvoiceHeader> invoiceHeaders = fileOperations.readFile(invoiceHeaderFile,
							invoiceLineFile);
					salesInvoiceGenerator.updateView(invoiceHeaders);
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Wrong Format", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	private void openFileBrowser(ArrayList<File> files) throws Exception {
		JFileChooser openFileChooser = new JFileChooser();
		openFileChooser.setFileFilter(new FileNameExtensionFilter("CSV files (*csv)", "csv"));
		openFileChooser.setDialogTitle("Select Invoice Header File");
		int invoiceHeaderSelection = openFileChooser.showOpenDialog(salesInvoiceGenerator);

		if (openFileChooser.getSelectedFile() == null && invoiceHeaderSelection == JFileChooser.APPROVE_OPTION) {
			JOptionPane.showMessageDialog(null, "please select invoice header file first");

		} else if (invoiceHeaderSelection == JFileChooser.APPROVE_OPTION) {
			validateCSVFileFormat(openFileChooser.getSelectedFile());
			files.add(openFileChooser.getSelectedFile());

			openFileChooser.setDialogTitle("Select Invoice Line File");
			int invoiceLineSelection = openFileChooser.showOpenDialog(salesInvoiceGenerator);
			if (invoiceLineSelection == JFileChooser.APPROVE_OPTION && openFileChooser.getSelectedFile() == null) {
				JOptionPane.showMessageDialog(null, "please select invoice line file first");
			} else if (invoiceLineSelection == JFileChooser.APPROVE_OPTION) {
				validateCSVFileFormat(openFileChooser.getSelectedFile());
				files.add(openFileChooser.getSelectedFile());
			}
		}
	}
	
	private String getFileExtension(File file) {
		String fileName = file.getName();
		return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
	}
	
	private void validateCSVFileFormat(File file) throws FileFormatException{
		if(!"csv".equalsIgnoreCase(getFileExtension(file))) {
			throw new FileFormatException("Wrong file format");
		}
	}
}
