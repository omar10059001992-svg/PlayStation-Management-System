package gaming;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalTime;

public class SquadGameSystem extends JFrame {
    private Connection conn;
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);

    // الألوان النيون والداكنة الخاصة بالواجهة
    private Color bgBlue = new Color(11, 15, 26);
    private Color panelBlue = new Color(19, 24, 38);
    private Color tableBg = new Color(15, 19, 31);
    private Color textWhite = new Color(243, 244, 246);
    private Color gridColor = new Color(31, 41, 55);
    private Color neonTitleColor = new Color(139, 92, 246);

    // عناصر شاشة تسجيل الدخول
    private JTextField txtLoginUser = new JTextField(15);
    private JPasswordField txtLoginPass = new JPasswordField(15);

    // التبويبات الرئيسية
    private JTabbedPane tabs = new JTabbedPane();
    private JPanel itemsPanel = new JPanel(new BorderLayout());
    private JPanel resPanel = new JPanel(new BorderLayout());
    private JPanel maintenancePanel = new JPanel(new BorderLayout());
    private JPanel usersPanel = new JPanel(new BorderLayout());

    // واجهة الأجهزة
    private DefaultTableModel itemsModel;
    private JTable tblItems;
    private JTextField txtItemName = new JTextField(12);
    private JTextField txtItemType = new JTextField(12);
    private JTextField txtItemRate = new JTextField(12);
    private JButton btnAddItem = new JButton("➕ إضافة جهاز");
    private JButton btnUpdateItem = new JButton("✏️ تعديل جهاز");
    private JButton btnDeleteItem = new JButton("❌ حذف جهاز");

    // واجهة الحجوزات
    private DefaultTableModel resModel;
    private JTable tblRes;
    private JTextField txtResDeviceID = new JTextField(12);
    private JTextField txtResDuration = new JTextField(12);
    private JButton btnStartRes = new JButton("⏱️ بدء حجز جديد");
    private JButton btnEndRes = new JButton("💵 إنهاء الحجز والتشيك");

    // واجهة الصيانة والتقارير
    private DefaultTableModel maintenanceModel;
    private JTable tblMaintenance;
    private JTextField txtMaintDeviceID = new JTextField(12);
    private JTextField txtMaintCost = new JTextField(12);
    private JTextField txtMaintDesc = new JTextField(12);
    private JTextField txtMaintDate = new JTextField(12);
    private JButton btnAddMaintenance = new JButton("➕ إضافة تقرير");
    private JButton btnDeleteMaintenance = new JButton("❌ حذف تقرير");
    // 🌟 العناصر الجديدة الخاصة بالطباعة والمجموع
    private JButton btnPrintMaintenance = new JButton("🖨️ طباعة التقرير");
    private JLabel lblTotalMaintenanceCost = new JLabel("الإجمالي: 0.0 د.ل");

    // واجهة الحسابات والصلاحيات
    private DefaultTableModel usersModel;
    private JTable tblUsers;
    private JTextField txtManageUser = new JTextField(12);
    private JTextField txtManagePass = new JTextField(12);
    private JComboBox<String> cmbRole = new JComboBox<>(new String[]{"Employee", "Admin"});
    private JButton btnAddUser = new JButton("➕ إضافة حساب");
    private JButton btnUpdateUserPrivilege = new JButton("✏️ تعديل صلاحية");
    private JButton btnDeleteUser = new JButton("❌ حذف الحساب");

    private JButton btnLogout = new JButton("تسجيل الخروج ↪");

    public SquadGameSystem() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("تعذر تطبيق مظهر الـ CrossPlatform");
        }

        setTitle("منظومة صالة ألعاب - صالة سكواد قيم");
        setSize(1150, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/squad_game_db", "root", "");
        } catch (SQLException e) {
            System.out.println("تنبيه: لا يوجد اتصال بقاعدة البيانات. (البرنامج يعمل بدون قاعدة)");
        }

        usersModel = new DefaultTableModel(new String[]{"ID المستخدم", "اسم المستخدم", "كلمة المرور", "الصلاحية الحالية"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        loadUsersFromDatabase();
        initLoginScreen();
        initDashboardScreen();

        add(mainPanel);
        cardLayout.show(mainPanel, "Login");
    }

    private void initLoginScreen() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            Image bgImage = null;
            {
                java.io.File fileOutside = new java.io.File("bg.jpg.png");
                if (fileOutside.exists()) {
                    bgImage = new ImageIcon("bg.jpg.png").getImage();
                } else {
                    java.net.URL urlInPackage = getClass().getResource("/gaming/bg.jpg.png");
                    if (urlInPackage == null) {
                        urlInPackage = getClass().getResource("/bg.jpg.png");
                    }
                    if (urlInPackage != null) {
                        bgImage = new ImageIcon(urlInPackage).getImage();
                    }
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null && bgImage.getWidth(this) > 0) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(bgBlue);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblTitle = new JLabel("تسجيل الدخول - صالة سكواد قيم");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 1;
        JLabel lblUser = new JLabel("اسم المستخدم:");
        lblUser.setFont(new Font("Arial", Font.BOLD, 14));
        lblUser.setForeground(Color.WHITE);
        panel.add(lblUser, gbc);
        gbc.gridx = 0; panel.add(txtLoginUser, gbc);

        gbc.gridy = 2; gbc.gridx = 1;
        JLabel lblPass = new JLabel("كلمة المرور:");
        lblPass.setFont(new Font("Arial", Font.BOLD, 14));
        lblPass.setForeground(Color.WHITE);
        panel.add(lblPass, gbc);
        gbc.gridx = 0; panel.add(txtLoginPass, gbc);

        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 1;
        JButton btnLogin = new JButton("دخول للنظام");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setBackground(new Color(16, 185, 129));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setPreferredSize(new Dimension(130, 35));
        btnLogin.addActionListener(e -> handleLogin());
        panel.add(btnLogin, gbc);

        gbc.gridx = 1;
        JButton btnExitSystem = new JButton("خروج من النظام");
        btnExitSystem.setFont(new Font("Arial", Font.BOLD, 14));
        btnExitSystem.setBackground(new Color(239, 68, 68));
        btnExitSystem.setForeground(Color.WHITE);
        btnExitSystem.setPreferredSize(new Dimension(130, 35));
        btnExitSystem.addActionListener(e -> System.exit(0));
        panel.add(btnExitSystem, gbc);

        mainPanel.add(panel, "Login");
    }

    private void handleLogin() {
        String enteredUser = txtLoginUser.getText().trim();
        String enteredPass = new String(txtLoginPass.getPassword()).trim();
        String activeRole = null;

        for (int i = 0; i < usersModel.getRowCount(); i++) {
            String u = usersModel.getValueAt(i, 1).toString().trim();
            String p = usersModel.getValueAt(i, 2).toString().trim();
            if (u.equalsIgnoreCase(enteredUser) && p.equals(enteredPass)) {
                activeRole = usersModel.getValueAt(i, 3).toString();
                break;
            }
        }

        if (activeRole == null) {
            JOptionPane.showMessageDialog(this, "اسم المستخدم أو كلمة المرور غير صحيحة!", "فشل تسجيل الدخول", JOptionPane.ERROR_MESSAGE);
            return;
        }

        tabs.removeAll();
        tabs.addTab("🎮 إدارة الأجهزة", itemsPanel);
        tabs.addTab("📝 سجل الحجوزات النشطة", resPanel);

        if (activeRole.equalsIgnoreCase("Admin")) {
            tabs.addTab("🛠️ أرشيف الصيانة والتقارير المالية", maintenancePanel);
            tabs.addTab("👥 إدارة الحسابات والصلاحيات", usersPanel);
        }

        loadItemsFromDatabase();
        loadMaintenanceFromDatabase();

        txtLoginUser.setText("");
        txtLoginPass.setText("");
        cardLayout.show(mainPanel, "Dashboard");
    }

    private void styleTable(JTable table, JScrollPane scrollPane) {
        table.setBackground(tableBg);
        table.setForeground(textWhite);
        table.setGridColor(gridColor);
        table.setRowHeight(34);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(55, 48, 163));
        table.setSelectionForeground(textWhite);

        JTableHeader header = table.getTableHeader();
        header.setBackground(panelBlue);
        header.setForeground(textWhite);
        header.setFont(new Font("Arial", Font.BOLD, 14));

        scrollPane.getViewport().setBackground(bgBlue);
        scrollPane.setBorder(BorderFactory.createLineBorder(gridColor));
    }

    private void styleButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(textWhite);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleSidebarPanel(JPanel panel, String titleText) {
        panel.setBackground(panelBlue);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        panel.setPreferredSize(new Dimension(280, 0));
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    }

    private void addSidebarTitle(JPanel panel, String titleText) {
        JLabel lblTitle = new JLabel(titleText);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(neonTitleColor);
        lblTitle.setAlignmentX(Component.RIGHT_ALIGNMENT);
        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(20));
    }

    private void addSidebarField(JPanel panel, String labelText, JTextField field) {
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setForeground(textWhite);
        lbl.setAlignmentX(Component.RIGHT_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(5));

        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setBackground(bgBlue);
        field.setForeground(textWhite);
        field.setCaretColor(textWhite);
        field.setBorder(BorderFactory.createLineBorder(gridColor, 1));
        field.setAlignmentX(Component.RIGHT_ALIGNMENT);
        panel.add(field);
        panel.add(Box.createVerticalStrut(15));
    }

    private void addSidebarComponent(JPanel panel, JComponent comp, String labelText) {
        if (labelText != null) {
            JLabel lbl = new JLabel(labelText);
            lbl.setFont(new Font("Arial", Font.BOLD, 13));
            lbl.setForeground(textWhite);
            lbl.setAlignmentX(Component.RIGHT_ALIGNMENT);
            panel.add(lbl);
            panel.add(Box.createVerticalStrut(5));
        }
        comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        comp.setAlignmentX(Component.RIGHT_ALIGNMENT);
        panel.add(comp);
        panel.add(Box.createVerticalStrut(15));
    }

    // 🌟 دالة مخصصة لحساب الإجمالي للتقارير
    private void updateMaintenanceTotal() {
        double total = 0.0;
        for (int i = 0; i < maintenanceModel.getRowCount(); i++) {
            try {
                total += Double.parseDouble(maintenanceModel.getValueAt(i, 2).toString());
            } catch (Exception ignored) {}
        }
        lblTotalMaintenanceCost.setText("الإجمالي: " + total + " د.ل");
    }

    private void loadItemsFromDatabase() {
        itemsModel.setRowCount(0);
        if (conn == null) return;

        try {
            String sql = "SELECT * FROM items_table";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id_item");
                String name = rs.getString("name");
                String type = rs.getString("type");
                double rate = rs.getDouble("hourly_rate");
                String status = rs.getString("status");

                itemsModel.addRow(new Object[]{id, name, type, rate, status});
            }
        } catch (SQLException e) {
            System.out.println("حدث خطأ أثناء جلب الأجهزة من قاعدة البيانات: " + e.getMessage());
        }
    }

    private void loadMaintenanceFromDatabase() {
        maintenanceModel.setRowCount(0);
        maintenanceModel.addRow(new Object[]{1, "جهاز 1", 150.0, "تغيير مروحة التبريد", "2026-06-10"});
        updateMaintenanceTotal(); // 🌟 تحديث المجموع عند التحميل
    }

    private void loadUsersFromDatabase() {
        usersModel.setRowCount(0);
        if (conn == null) {
            usersModel.addRow(new Object[]{1, "admin", "123", "Admin"});
            usersModel.addRow(new Object[]{2, "omar", "1234", "Employee"});
            return;
        }

        try {
            String sql = "SELECT * FROM user_table";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role");

                usersModel.addRow(new Object[]{id, username, password, role});
            }
        } catch (SQLException e) {
            usersModel.addRow(new Object[]{1, "admin", "123", "Admin"});
            usersModel.addRow(new Object[]{2, "omar", "1234", "Employee"});
        }
    }

    private void initDashboardScreen() {
        JPanel dashboardContainer = new JPanel(new BorderLayout());
        dashboardContainer.setBackground(bgBlue);

        styleButton(btnAddItem, new Color(16, 185, 129));
        styleButton(btnUpdateItem, new Color(59, 130, 246));
        styleButton(btnDeleteItem, new Color(239, 68, 68));
        styleButton(btnStartRes, new Color(16, 185, 129));
        styleButton(btnEndRes, new Color(239, 68, 68));
        styleButton(btnAddMaintenance, new Color(16, 185, 129));
        styleButton(btnDeleteMaintenance, new Color(239, 68, 68));
        styleButton(btnPrintMaintenance, new Color(59, 130, 246)); // 🌟 زر الطباعة باللون الأزرق
        styleButton(btnAddUser, new Color(16, 185, 129));
        styleButton(btnUpdateUserPrivilege, new Color(59, 130, 246));
        styleButton(btnDeleteUser, new Color(239, 68, 68));

        // واجهة إدارة الأجهزة
        itemsPanel.setBackground(bgBlue);
        itemsModel = new DefaultTableModel(new String[]{"ID الجهاز", "اسم الجهاز", "النوع", "سعر الساعة", "الحالة"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblItems = new JTable(itemsModel);
        tblItems.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        JScrollPane scrollItems = new JScrollPane(tblItems);
        styleTable(tblItems, scrollItems);
        itemsPanel.add(scrollItems, BorderLayout.CENTER);

        JPanel itemsSidebar = new JPanel();
        styleSidebarPanel(itemsSidebar, "إجراءات الأجهزة");
        addSidebarTitle(itemsSidebar, "🕹️ التحكم بالأجهزة");
        addSidebarField(itemsSidebar, "اسم الجهاز:", txtItemName);
        addSidebarField(itemsSidebar, "النوع:", txtItemType);
        addSidebarField(itemsSidebar, "سعر الساعة:", txtItemRate);

        addSidebarComponent(itemsSidebar, btnAddItem, null);
        addSidebarComponent(itemsSidebar, btnUpdateItem, null);
        addSidebarComponent(itemsSidebar, btnDeleteItem, null);
        itemsPanel.add(itemsSidebar, BorderLayout.EAST);

        // واجهة الحجوزات
        resPanel.setBackground(bgBlue);
        resModel = new DefaultTableModel(new String[]{"رقم الحجز", "رقم الجهاز", "اسم الجهاز المربوط", "وقت البدء الفعلي", "المدة المطلوبة", "سعر الساعة", "الإجمالي المؤقت (د.ل)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblRes = new JTable(resModel);
        tblRes.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        JScrollPane scrollRes = new JScrollPane(tblRes);
        styleTable(tblRes, scrollRes);
        resPanel.add(scrollRes, BorderLayout.CENTER);

        JPanel resSidebar = new JPanel();
        styleSidebarPanel(resSidebar, "إجراءات الحجز");
        addSidebarTitle(resSidebar, "⏱️ إدارة الوقت واللعب");
        addSidebarField(resSidebar, "رقم الجهاز الهدف:", txtResDeviceID);
        addSidebarField(resSidebar, "المدة (بالساعات - 0 للمفتوح):", txtResDuration);

        addSidebarComponent(resSidebar, btnStartRes, null);
        addSidebarComponent(resSidebar, btnEndRes, null);
        resPanel.add(resSidebar, BorderLayout.EAST);

        // واجهة الصيانة
        maintenancePanel.setBackground(bgBlue);
        maintenanceModel = new DefaultTableModel(new String[]{"ID السجل", "جهاز / مادة الصيانة", "التكلفة (د.ل)", "وصف عملية الإصلاح والعطل", "تاريخ السجل"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblMaintenance = new JTable(maintenanceModel);
        tblMaintenance.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        JScrollPane scrollMaint = new JScrollPane(tblMaintenance);
        styleTable(tblMaintenance, scrollMaint);
        maintenancePanel.add(scrollMaint, BorderLayout.CENTER);

        JPanel maintSidebar = new JPanel();
        styleSidebarPanel(maintSidebar, "إجراءات الصيانة");
        addSidebarTitle(maintSidebar, "🛠️ التقارير المالية والصيانة");
        addSidebarField(maintSidebar, "الجهاز:", txtMaintDeviceID);
        addSidebarField(maintSidebar, "التكلفة:", txtMaintCost);
        addSidebarField(maintSidebar, "الوصف والعطل:", txtMaintDesc);
        addSidebarField(maintSidebar, "التاريخ:", txtMaintDate);
        txtMaintDate.setText("2026-06-25");

        // 🌟 تنسيق نص المجموع المالي
        lblTotalMaintenanceCost.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotalMaintenanceCost.setForeground(new Color(16, 185, 129)); // لون أخضر نيون يتماشى مع الستايل
        lblTotalMaintenanceCost.setAlignmentX(Component.RIGHT_ALIGNMENT);

        addSidebarComponent(maintSidebar, btnAddMaintenance, null);
        addSidebarComponent(maintSidebar, btnDeleteMaintenance, null);
        addSidebarComponent(maintSidebar, btnPrintMaintenance, null); // 🌟 إضافة زر الطباعة

        maintSidebar.add(Box.createVerticalStrut(20)); // مسافة فارغة
        maintSidebar.add(lblTotalMaintenanceCost);     // 🌟 إضافة نص المجموع أسفل الأزرار

        maintenancePanel.add(maintSidebar, BorderLayout.EAST);

        // واجهة الحسابات
        usersPanel.setBackground(bgBlue);
        tblUsers = new JTable(usersModel);
        tblUsers.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        JScrollPane scrollUsers = new JScrollPane(tblUsers);
        styleTable(tblUsers, scrollUsers);
        usersPanel.add(scrollUsers, BorderLayout.CENTER);

        JPanel usersSidebar = new JPanel();
        styleSidebarPanel(usersSidebar, "إجراءات الحسابات");
        addSidebarTitle(usersSidebar, "👥 الصلاحيات والمستخدمين");
        addSidebarField(usersSidebar, "اسم المستخدم:", txtManageUser);
        addSidebarField(usersSidebar, "كلمة المرور:", txtManagePass);

        cmbRole.setBackground(bgBlue);
        cmbRole.setForeground(textWhite);
        addSidebarComponent(usersSidebar, cmbRole, "الصلاحية الرقابية:");

        addSidebarComponent(usersSidebar, btnAddUser, null);
        addSidebarComponent(usersSidebar, btnUpdateUserPrivilege, null);
        addSidebarComponent(usersSidebar, btnDeleteUser, null);
        usersPanel.add(usersSidebar, BorderLayout.EAST);

        tblItems.getSelectionModel().addListSelectionListener(e -> {
            int row = tblItems.getSelectedRow();
            if (row != -1) {
                txtItemName.setText(tblItems.getValueAt(row, 1).toString());
                txtItemType.setText(tblItems.getValueAt(row, 2).toString());
                txtItemRate.setText(tblItems.getValueAt(row, 3).toString());
            }
        });

        tblUsers.getSelectionModel().addListSelectionListener(e -> {
            int row = tblUsers.getSelectedRow();
            if (row != -1) {
                txtManageUser.setText(tblUsers.getValueAt(row, 1).toString());
                txtManagePass.setText(tblUsers.getValueAt(row, 2).toString());
                cmbRole.setSelectedItem(tblUsers.getValueAt(row, 3).toString());
            }
        });

        btnAddUser.addActionListener(e -> {
            String username = txtManageUser.getText().trim();
            String password = txtManagePass.getText().trim();
            String role = cmbRole.getSelectedItem().toString();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "الرجاء كتابة البيانات أولاً!", "حقول فارغة", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                if (conn != null) {
                    String sql = "INSERT INTO user_table (username, password, role) VALUES (?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, username);
                    stmt.setString(2, password);
                    stmt.setString(3, role);
                    stmt.executeUpdate();
                    loadUsersFromDatabase();
                } else {
                    int nextUserId = usersModel.getRowCount() + 1;
                    usersModel.addRow(new Object[]{nextUserId, username, password, role});
                }

                txtManageUser.setText(""); txtManagePass.setText("");
                JOptionPane.showMessageDialog(this, "تم إضافة الحساب بنجاح! ✅", "عملية ناجحة", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "خطأ أثناء الحفظ في قاعدة البيانات: " + ex.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnUpdateUserPrivilege.addActionListener(e -> {
            int selectedRow = tblUsers.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "الرجاء تحديد مستخدم من الجدول أولاً!", "تنبيه", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String updatedUser = txtManageUser.getText().trim();
            String updatedPass = txtManagePass.getText().trim();
            String updatedRole = cmbRole.getSelectedItem().toString();

            if (updatedUser.isEmpty() || updatedPass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "الرجاء عدم ترك الخانات فارغة!", "خطأ", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int userId = Integer.parseInt(usersModel.getValueAt(selectedRow, 0).toString());

            try {
                if (conn != null) {
                    String sql = "UPDATE user_table SET username = ?, password = ?, role = ? WHERE id = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, updatedUser);
                    stmt.setString(2, updatedPass);
                    stmt.setString(3, updatedRole);
                    stmt.setInt(4, userId);
                    stmt.executeUpdate();
                    loadUsersFromDatabase();
                } else {
                    tblUsers.setValueAt(updatedUser, selectedRow, 1);
                    tblUsers.setValueAt(updatedPass, selectedRow, 2);
                    tblUsers.setValueAt(updatedRole, selectedRow, 3);
                }
                JOptionPane.showMessageDialog(this, "تم تعديل بيانات المستخدم بنجاح! ✨", "نجاح العملية", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "خطأ أثناء التعديل بقاعدة البيانات: " + ex.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDeleteUser.addActionListener(e -> {
            int selectedRow = tblUsers.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "الرجاء تحديد مستخدم من الجدول أولاً!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "هل أنت متأكد من حذف هذا المستخدم نهائياً؟", "تأكيد الحذف", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            int userId = Integer.parseInt(usersModel.getValueAt(selectedRow, 0).toString());

            try {
                if (conn != null) {
                    String sql = "DELETE FROM user_table WHERE id = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, userId);
                    stmt.executeUpdate();
                    loadUsersFromDatabase();
                } else {
                    usersModel.removeRow(selectedRow);
                }
                txtManageUser.setText(""); txtManagePass.setText("");
                JOptionPane.showMessageDialog(this, "تم حذف المستخدم بنجاح! ✅");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "خطأ في قاعدة البيانات: " + ex.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnAddItem.addActionListener(e -> {
            String name = txtItemName.getText().trim();
            String type = txtItemType.getText().trim();
            String rateStr = txtItemRate.getText().trim();

            if (name.isEmpty() || type.isEmpty() || rateStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "الرجاء تعبئة جميع الحقول!");
                return;
            }

            try {
                double rate = Double.parseDouble(rateStr);
                if(conn != null) {
                    String sql = "INSERT INTO items_table (name, type, hourly_rate, status) VALUES (?, ?, ?, 'Available')";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, name);
                    stmt.setString(2, type);
                    stmt.setDouble(3, rate);
                    stmt.executeUpdate();
                }

                loadItemsFromDatabase();
                txtItemName.setText(""); txtItemType.setText(""); txtItemRate.setText("");
                JOptionPane.showMessageDialog(this, "تم إضافة الجهاز بنجاح! ✅");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "سعر الساعة يجب أن يكون رقماً!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "خطأ في قاعدة البيانات: " + ex.getMessage());
            }
        });

        btnUpdateItem.addActionListener(e -> {
            int selectedRow = tblItems.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "الرجاء تحديد جهاز من الجدول أولاً!", "تنبيه", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                String updatedName = txtItemName.getText().trim();
                String updatedType = txtItemType.getText().trim();
                String updatedRateStr = txtItemRate.getText().trim();

                if (updatedName.isEmpty() || updatedType.isEmpty() || updatedRateStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "الرجاء عدم ترك الخانات فارغة!", "خطأ", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double updatedRate = Double.parseDouble(updatedRateStr);
                itemsModel.setValueAt(updatedName, selectedRow, 1);
                itemsModel.setValueAt(updatedType, selectedRow, 2);
                itemsModel.setValueAt(updatedRate, selectedRow, 3);

                JOptionPane.showMessageDialog(this, "تم تحديث بيانات الجهاز بنجاح! ✨", "نجاح العملية", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "الرجاء إدخال رقم صحيح في خانة سعر الساعة!", "خطأ في الإدخال", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDeleteItem.addActionListener(e -> {
            int row = tblItems.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "الرجاء تحديد جهاز من الجدول للحذف!");
                return;
            }

            int itemId = Integer.parseInt(itemsModel.getValueAt(row, 0).toString());

            int confirm = JOptionPane.showConfirmDialog(this, "هل أنت متأكد من حذف هذا الجهاز نهائياً؟", "تأكيد", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            try {
                if(conn != null) {
                    String sql = "DELETE FROM items_table WHERE id_item = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, itemId);
                    stmt.executeUpdate();
                }

                loadItemsFromDatabase();
                txtItemName.setText(""); txtItemType.setText(""); txtItemRate.setText("");
                JOptionPane.showMessageDialog(this, "تم الحذف بنجاح! ✅");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "خطأ في قاعدة البيانات: " + ex.getMessage());
            }
        });

        btnStartRes.addActionListener(e -> {
            String devIdStr = txtResDeviceID.getText().trim();
            String durStr = txtResDuration.getText().trim();

            if (devIdStr.isEmpty() || durStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "الرجاء إدخال رقم الجهاز والمدة!");
                return;
            }

            // 1. التحقق من وجود حجز مسبق لنفس الجهاز
            for (int i = 0; i < resModel.getRowCount(); i++) {
                String existingDeviceId = resModel.getValueAt(i, 1).toString();
                if (existingDeviceId.equals(devIdStr)) {
                    JOptionPane.showMessageDialog(this, "خطأ: هذا الجهاز محجوز مسبقاً ولا يمكن حجزه مرة أخرى!", "جهاز محجوز", JOptionPane.ERROR_MESSAGE);
                    return; // إيقاف العملية ومنع التكرار
                }
            }

            // 2. البحث عن تفاصيل الجهاز (الاسم والسعر)
            String targetDeviceName = "جهاز";
            double targetRate = 10.0;
            boolean found = false;

            try {
                int searchId = Integer.parseInt(devIdStr);
                for (int i = 0; i < itemsModel.getRowCount(); i++) {
                    if (Integer.parseInt(itemsModel.getValueAt(i, 0).toString()) == searchId) {
                        targetDeviceName = itemsModel.getValueAt(i, 1).toString();
                        targetRate = Double.parseDouble(itemsModel.getValueAt(i, 3).toString());
                        found = true;
                        break;
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "رقم الجهاز غير موجود في القائمة!");
                return;
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "الجهاز غير موجود!");
                return;
            }

            // 3. إضافة الحجز إذا كان كل شيء سليم
            try {
                double hours = Double.parseDouble(durStr);
                double total = hours * targetRate;
                LocalTime now = LocalTime.now();
                String currentTime = String.format("%02d:%02d", now.getHour(), now.getMinute());
                int nextBookingId = resModel.getRowCount() + 1;

                // إضافة الصف إلى الجدول
                resModel.addRow(new Object[]{nextBookingId, devIdStr, targetDeviceName, currentTime, (hours == 0) ? "مفتوح" : hours + " ساعات", targetRate, total});

                // مسح الحقول بعد الإضافة
                txtResDeviceID.setText("");
                txtResDuration.setText("");

                JOptionPane.showMessageDialog(this, "تم بدء الحجز بنجاح للجهاز: " + targetDeviceName);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "المدة يجب أن تكون رقماً!");
            }
        });
        btnEndRes.addActionListener(e -> {
            int selectedRow = tblRes.getSelectedRow();
            if(selectedRow != -1) resModel.removeRow(selectedRow);
        });

        btnAddMaintenance.addActionListener(e -> {
            String device = txtMaintDeviceID.getText().trim();
            String costStr = txtMaintCost.getText().trim();
            String desc = txtMaintDesc.getText().trim();
            String date = txtMaintDate.getText().trim();

            if (device.isEmpty() || costStr.isEmpty() || desc.isEmpty() || date.isEmpty()) {
                JOptionPane.showMessageDialog(this, "الرجاء تعبئة جميع خانات التقرير أولاً!", "حقول فارغة", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                double cost = Double.parseDouble(costStr);
                int nextMaintId = maintenanceModel.getRowCount() + 1;
                String formattedDevice = device.matches("\\d+") ? "جهاز " + device : device;
                maintenanceModel.addRow(new Object[]{nextMaintId, formattedDevice, cost, desc, date});

                updateMaintenanceTotal(); // 🌟 تحديث المجموع بعد الإضافة

                txtMaintDeviceID.setText("");
                txtMaintCost.setText("");
                txtMaintDesc.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "الرجاء إدخال رقم صحيح في خانة التكلفة!", "خطأ في الإدخال", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDeleteMaintenance.addActionListener(e -> {
            int selectedRow = tblMaintenance.getSelectedRow();
            if (selectedRow != -1) {
                maintenanceModel.removeRow(selectedRow);
                updateMaintenanceTotal(); // 🌟 تحديث المجموع بعد الحذف
            } else {
                JOptionPane.showMessageDialog(this, "الرجاء تحديد سطر من جدول الصيانة لحذفه!", "تنبيه", JOptionPane.WARNING_MESSAGE);
            }
        });

        // 🌟 برمجة زر الطباعة بحيث يطبع التقرير المالي بشكل احترافي
        btnPrintMaintenance.addActionListener(e -> {
            try {
                // عنوان التقرير من الأعلى والمجموع من الأسفل
                java.text.MessageFormat header = new java.text.MessageFormat("أرشيف الصيانة والمصروفات - صالة سكواد قيم");
                java.text.MessageFormat footer = new java.text.MessageFormat("إجمالي التكلفة: " + lblTotalMaintenanceCost.getText().replace("الإجمالي: ", "") + " | الصفحة {0}");

                boolean complete = tblMaintenance.print(JTable.PrintMode.FIT_WIDTH, header, footer);
                if (complete) {
                    JOptionPane.showMessageDialog(this, "تمت الطباعة بنجاح! ✅", "عملية ناجحة", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "تم إلغاء الطباعة.", "إلغاء", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "حدث خطأ أثناء الطباعة: " + ex.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
            }
        });

        tabs.setBackground(panelBlue);
        tabs.setForeground(textWhite);
        tabs.setFont(new Font("Arial", Font.BOLD, 13));
        tabs.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(panelBlue);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblHeaderTitle = new JLabel("لوحة التحكم - صالة سكواد قيم");
        lblHeaderTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblHeaderTitle.setForeground(neonTitleColor);
        headerPanel.add(lblHeaderTitle, BorderLayout.EAST);

        styleButton(btnLogout, new Color(239, 68, 68));
        btnLogout.addActionListener(e -> {
            cardLayout.show(mainPanel, "Login");
            txtLoginUser.setText("");
            txtLoginPass.setText("");
        });
        headerPanel.add(btnLogout, BorderLayout.WEST);

        dashboardContainer.add(headerPanel, BorderLayout.NORTH);
        dashboardContainer.add(tabs, BorderLayout.CENTER);

        mainPanel.add(dashboardContainer, "Dashboard");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SquadGameSystem().setVisible(true);
        });
    }
}