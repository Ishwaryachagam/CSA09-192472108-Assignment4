
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class LibrarySearch extends JFrame {

    private JTextField searchField;
    private JComboBox<String> categoryBox;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JLabel pageLabel;

    private ArrayList<String[]> books = new ArrayList<>();
    private ArrayList<String[]> filteredBooks = new ArrayList<>();

    private int currentPage = 0;
    private final int PAGE_SIZE = 5;

    public LibrarySearch() {
        setTitle("Campus Library Search System");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        addSampleBooks();

        JPanel topPanel = new JPanel(new FlowLayout());

        searchField = new JTextField(20);

        String[] categories = {
            "All", "Programming", "Database",
            "Artificial Intelligence", "Networking"
        };

        categoryBox = new JComboBox<>(categories);

        JButton searchButton = new JButton("Search");
        JButton reserveButton = new JButton("Reserve Book");
        JButton previousButton = new JButton("Previous");
        JButton nextButton = new JButton("Next");

        topPanel.add(new JLabel("Search Book:"));
        topPanel.add(searchField);
        topPanel.add(categoryBox);
        topPanel.add(searchButton);

        String[] columns = {
            "Book ID", "Book Title", "Author",
            "Category", "Availability"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookTable = new JTable(tableModel);
        bookTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(bookTable);

        JPanel bottomPanel = new JPanel();

        pageLabel = new JLabel("Page 1");

        bottomPanel.add(previousButton);
        bottomPanel.add(pageLabel);
        bottomPanel.add(nextButton);
        bottomPanel.add(reserveButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        filteredBooks.addAll(books);
        displayPage();

        searchButton.addActionListener(e -> searchBooks());

        searchField.addActionListener(e -> searchBooks());

        nextButton.addActionListener(e -> {
            if ((currentPage + 1) * PAGE_SIZE < filteredBooks.size()) {
                currentPage++;
                displayPage();
            }
        });

        previousButton.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                displayPage();
            }
        });

        reserveButton.addActionListener(e -> reserveBook());
    }

    private void addSampleBooks() {
        books.add(new String[]{"B101", "Java Programming",
                "James Gosling", "Programming", "Available"});

        books.add(new String[]{"B102", "Python Basics",
                "Guido van Rossum", "Programming", "Available"});

        books.add(new String[]{"B103", "Database Management",
                "Raghu Ramakrishnan", "Database", "Available"});

        books.add(new String[]{"B104", "Artificial Intelligence",
                "Stuart Russell", "Artificial Intelligence", "Available"});

        books.add(new String[]{"B105", "Computer Networks",
                "Andrew Tanenbaum", "Networking", "Available"});

        books.add(new String[]{"B106", "Machine Learning",
                "Tom Mitchell", "Artificial Intelligence", "Available"});

        books.add(new String[]{"B107", "Data Structures",
                "Mark Allen Weiss", "Programming", "Available"});

        books.add(new String[]{"B108", "Operating Systems",
                "Abraham Silberschatz", "Networking", "Available"});
    }

    private void searchBooks() {
        String keyword = searchField.getText()
                .trim().toLowerCase();

        String category = (String) categoryBox.getSelectedItem();

        filteredBooks.clear();

        for (String[] book : books) {

            boolean matchesKeyword =
                    book[1].toLowerCase().contains(keyword)
                    || book[2].toLowerCase().contains(keyword)
                    || book[0].toLowerCase().contains(keyword);

            boolean matchesCategory =
                    category.equals("All")
                    || book[3].equals(category);

            if (matchesKeyword && matchesCategory) {
                filteredBooks.add(book);
            }
        }

        currentPage = 0;
        displayPage();

        if (filteredBooks.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No books found!");
        }
    }

    private void displayPage() {
        tableModel.setRowCount(0);

        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE,
                filteredBooks.size());

        for (int i = start; i < end; i++) {
            tableModel.addRow(filteredBooks.get(i));
        }

        int totalPages = Math.max(1,
                (filteredBooks.size() + PAGE_SIZE - 1)
                        / PAGE_SIZE);

        pageLabel.setText("Page "
                + (currentPage + 1) + " of " + totalPages);
    }

    private void reserveBook() {
        int selectedRow = bookTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a book first!");
            return;
        }

        int modelRow = bookTable.convertRowIndexToModel(selectedRow);

        String bookId = (String) tableModel.getValueAt(modelRow, 0);

        for (String[] book : books) {
            if (book[0].equals(bookId)) {

                if (book[4].equals("Available")) {
                    book[4] = "Reserved";

                    JOptionPane.showMessageDialog(this,
                            "Book reserved successfully!");

                    displayPage();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "This book is already reserved!");
                }

                return;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LibrarySearch().setVisible(true);
        });
    }
}
