package sig;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import sig.dialogs.InvoiceItemDialog;
import sig.listners.LeftPanelBtnListner;
import sig.listners.MenuBarListner;
import sig.listners.RightPanelBtnListner;
import sig.models.FileOperations;
import sig.models.InvoiceHeader;

/**
 * @author Abdelrhman
 *
 */
public class SalesInvoiceGeneratorApp extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private List<String> invoices = new ArrayList<>();
	private ArrayList<InvoiceHeader> invoiceItems = new ArrayList<>();

	private JPanel contentPane;
	private JTable invoicesTable;
	private JMenuItem loadFile;
	private JMenuItem saveFile;
	private JLabel invoiceNumValueLabel;
	private String invoicesTableHeader[] = { "No.", "Date", "Customer", "Total" };
	private static Object invoicesTableData[][];
	private DefaultTableModel invoicesTableModel = new DefaultTableModel(invoicesTableData, invoicesTableHeader) {
		@Override
		public boolean isCellEditable(int row, int column) {
			return column != 3;
		}
	};

	private JTextField customerNametextField;

	private String InvoicesDetailsTableHeader[] = { "No.", "Item Name", "Item Price", "Count", "Item Total" };
	private String InvoicesDetailsTableData[][];
	private DefaultTableModel InvoicesDetailsTableModel = new DefaultTableModel(InvoicesDetailsTableData,
			InvoicesDetailsTableHeader) {
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};

	private JTable InvoicesDetailsTable;

	private JLabel invoiceTotalValueLabel;

	private JFormattedTextField invoiceDateTextField;

	private JButton createNewInvoiceBtn;

	private JButton deleteInvoiceBtn;

	private InvoiceItemDialog invoiceItemDialog;

	private MenuBarListner customMenuBarListner = new MenuBarListner(this);

	private LeftPanelBtnListner leftPanelBtnListner = new LeftPanelBtnListner(this);

	private RightPanelBtnListner rightPanelBtnListner = new RightPanelBtnListner(this);

	private JButton deleteChangeButton;

	private JButton cancelButton;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SalesInvoiceGeneratorApp frame = new SalesInvoiceGeneratorApp();
					frame.setSize(852, 420);
					frame.setResizable(true);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public SalesInvoiceGeneratorApp() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		loadFile = new JMenuItem("Load file");
		loadFile.addActionListener(customMenuBarListner);
		fileMenu.add(loadFile);

		saveFile = new JMenuItem("Save file");
		saveFile.addActionListener(customMenuBarListner);
		fileMenu.add(saveFile);
		contentPane = new JPanel();

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());

		JSplitPane splitPane = new JSplitPane();

		contentPane.add(splitPane);

		drawLeftPanel(splitPane);

		drawRightPanel(splitPane);

	}
	
	private void drawLeftPanel(JSplitPane splitPane) {
		JPanel leftPanel = new JPanel();
		splitPane.setLeftComponent(leftPanel);
		leftPanel.setLayout(new GridLayout(2, 1, 0, 0));

		JPanel tablePanel = new JPanel();
		leftPanel.add(tablePanel);
		tablePanel.setLayout(new BorderLayout(0, 0));

		invoicesTable = new JTable(invoicesTableModel);
		invoicesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		invoicesTable.setBorder(new LineBorder(new Color(0, 0, 0)));
		JScrollPane scrollPane = new JScrollPane(invoicesTable);
		tablePanel.add(scrollPane);
		invoicesTable.getSelectionModel().addListSelectionListener(leftPanelBtnListner);
		invoicesTable.addMouseListener(leftPanelBtnListner);
		JPanel leftBtnPanel = new JPanel();
		leftPanel.add(leftBtnPanel);
		leftBtnPanel.setLayout(new BoxLayout(leftBtnPanel, BoxLayout.LINE_AXIS));
		leftBtnPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		leftBtnPanel.add(Box.createHorizontalGlue());
		createNewInvoiceBtn = new JButton("Create New Invoice");
		createNewInvoiceBtn.addActionListener(leftPanelBtnListner);
		leftBtnPanel.add(createNewInvoiceBtn);
		leftBtnPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		deleteInvoiceBtn = new JButton("Delete Invoice");
		deleteInvoiceBtn.addActionListener(leftPanelBtnListner);

		leftBtnPanel.add(deleteInvoiceBtn);
		leftBtnPanel.add(Box.createHorizontalGlue());
	}
	
	private void drawRightPanel(JSplitPane splitPane) {
		JPanel rightPanel = new JPanel();
		splitPane.setRightComponent(rightPanel);
		rightPanel.setLayout(new GridLayout(3, 1, 0, 0));

		JPanel invoiceDetailPanel = new JPanel();
		rightPanel.add(invoiceDetailPanel);
		invoiceDetailPanel.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel invoiceNumLabel = new JLabel("Invoice Number");
		invoiceDetailPanel.add(invoiceNumLabel);
		invoiceNumLabel.setHorizontalAlignment(SwingConstants.LEFT);
		invoiceNumLabel.setLabelFor(invoiceNumValueLabel);

		invoiceNumValueLabel = new JLabel("");
		invoiceDetailPanel.add(invoiceNumValueLabel);
		invoiceNumValueLabel.setHorizontalAlignment(SwingConstants.LEFT);

		JLabel invoiceDateLabel = new JLabel("Invoice Date");
		invoiceDetailPanel.add(invoiceDateLabel);
		invoiceDateLabel.setHorizontalAlignment(SwingConstants.LEFT);
		invoiceDateLabel.setLabelFor(invoiceDateTextField);

		invoiceDateTextField = new JFormattedTextField();
		invoiceDetailPanel.add(invoiceDateTextField);
		invoiceDateTextField.setHorizontalAlignment(SwingConstants.LEFT);

		JLabel customerNameLabel = new JLabel("Customer name");
		invoiceDetailPanel.add(customerNameLabel);
		customerNameLabel.setHorizontalAlignment(SwingConstants.LEFT);
		customerNameLabel.setLabelFor(customerNametextField);

		customerNametextField = new JTextField();
		invoiceDetailPanel.add(customerNametextField);
		customerNametextField.setHorizontalAlignment(SwingConstants.LEFT);
		customerNametextField.setColumns(10);

		JLabel invocieTotalLabel = new JLabel("Invoice Total");
		invoiceDetailPanel.add(invocieTotalLabel);
		invocieTotalLabel.setLabelFor(invoiceTotalValueLabel);

		invoiceTotalValueLabel = new JLabel("");
		invoiceDetailPanel.add(invoiceTotalValueLabel);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Invoice Items", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		rightPanel.add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		InvoicesDetailsTable = new JTable(InvoicesDetailsTableModel);
		panel.add(new JScrollPane(InvoicesDetailsTable), BorderLayout.CENTER);

		JPanel rightBtnPanel = new JPanel();
		rightPanel.add(rightBtnPanel);
		rightBtnPanel.setLayout(new BoxLayout(rightBtnPanel, BoxLayout.LINE_AXIS));
		rightBtnPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		rightBtnPanel.add(Box.createHorizontalGlue());
		JButton createInvoiceItemButton = new JButton("Create Invoice Item");
		createInvoiceItemButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		createInvoiceItemButton.setActionCommand("createNewInvoiceItem");
		createInvoiceItemButton.addActionListener(rightPanelBtnListner);

		rightBtnPanel.add(createInvoiceItemButton);
		rightBtnPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		deleteChangeButton = new JButton("Delete Item");
		deleteChangeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		rightBtnPanel.add(deleteChangeButton);
		rightBtnPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		deleteChangeButton.setActionCommand("deleteSelectedRows");
		deleteChangeButton.addActionListener(rightPanelBtnListner);
	}

	public void readInvoiceItemFileFromResources() {
		FileOperations fileOperations = new FileOperations();
		try {
			invoiceItems = fileOperations.readFile(new File("src/resources/InvoiceHeader.csv"),
					new File("src/resources/InvoiceLine.csv"));
		} catch (Exception e) {
			System.out.println("Couldn't read file");
		}
	}

	public void toCsv() {
		try {
			FileOperations fileOperations = new FileOperations();
			fileOperations.writeFile(invoiceItems);
		} catch (Exception e) {
			System.out.println("Couldn't write to file");
		}
	}

	public void toCsvFromList(List<String> data, File file) {
		try {

			data.forEach(System.out::println);

			FileWriter Csv = new FileWriter(file);
			for (String string : data) {
				Csv.write(string);
				Csv.write("\n");
			}

			Csv.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public void updateView(ArrayList<InvoiceHeader> invoiceHeaders) {
		invoiceItems = invoiceHeaders;
		invoicesTableModel.setRowCount(0);
		for (int index = 0; index < invoiceHeaders.size(); index++) {
			InvoiceHeader invoiceHeader = invoiceHeaders.get(index);
			Object[] model = new Object[] { invoiceHeader.invoiceNum, invoiceHeader.invoiceDate,
					invoiceHeader.customerName, invoiceHeader.getTotal() };
			invoicesTableModel.insertRow(index, model);
		}
	}

	public void updateInvoiceItemsModel(ArrayList<InvoiceHeader> invoices) {
		invoiceItems = invoices;
	}

	public List<String> getInvoices() {
		return invoices;
	}

	public JPanel getContentPane() {
		return contentPane;
	}

	public JTable getInvoicesTable() {
		return invoicesTable;
	}

	public JMenuItem getLoadFile() {
		return loadFile;
	}

	public JMenuItem getSaveFile() {
		return saveFile;
	}

	public JLabel getInvoiceNumValueLabel() {
		return invoiceNumValueLabel;
	}

	public String[] getInvoicesTableHeader() {
		return invoicesTableHeader;
	}

	public static Object[][] getInvoicesTableData() {
		return invoicesTableData;
	}

	public DefaultTableModel getInvoicesTableModel() {
		return invoicesTableModel;
	}

	public JTextField getCustomerNametextField() {
		return customerNametextField;
	}

	public String[] getInvoicesDetailsTableHeader() {
		return InvoicesDetailsTableHeader;
	}

	public String[][] getInvoicesDetailsTableData() {
		return InvoicesDetailsTableData;
	}

	public DefaultTableModel getInvoicesDetailsTableModel() {
		return InvoicesDetailsTableModel;
	}

	public JTable getInvoicesDetailsTable() {
		return InvoicesDetailsTable;
	}

	public JLabel getInvoiceTotalValueLabel() {
		return invoiceTotalValueLabel;
	}

	public JFormattedTextField getInvoiceDateTextField() {
		return invoiceDateTextField;
	}

	public MenuBarListner getCustomListner() {
		return customMenuBarListner;
	}

	public LeftPanelBtnListner getAddNewInvoiceListner() {
		return leftPanelBtnListner;
	}

	public JButton getCreateNewInvoiceBtn() {
		return createNewInvoiceBtn;
	}

	public JButton getDeleteInvoiceBtn() {
		return deleteInvoiceBtn;
	}

	public InvoiceItemDialog getInvoiceItemDialog() {
		return invoiceItemDialog;
	}

	public void setInvoiceItemDialog(InvoiceItemDialog invoiceItemDialog) {
		this.invoiceItemDialog = invoiceItemDialog;
	}

	public RightPanelBtnListner getRightPanelBtnListner() {
		return rightPanelBtnListner;
	}

	public JButton getDeleteChangeButton() {
		return deleteChangeButton;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public ArrayList<InvoiceHeader> getInvoiceItems() {
		return invoiceItems;
	}
}
