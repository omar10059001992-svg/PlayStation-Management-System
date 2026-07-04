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
            System.out.println("نمط المحاكاة النشط قيد العمل حالياً.");
        }

        // قفل التعديل المباشر على خلايا جدول المستخدمين
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

    private void loadItemsFromDatabase() {
        itemsModel.setRowCount(0);
        itemsModel.addRow(new Object[]{1, "plystation 5", "sony", 15.0, "Available"});
        itemsModel.addRow(new Object[]{2, "xbox siries", "xbox", 10.0, "Available"});
        itemsModel.addRow(new Object[]{3, "xbox 1s", "xbox", 10.0, "Available"});
        itemsModel.addRow(new Object[]{4, "plystation 4", "sony", 5.0, "Available"});
    }

    private void loadMaintenanceFromDatabase() {
        maintenanceModel.setRowCount(0);
        maintenanceModel.addRow(new Object[]{1, "جهاز 1", 150.0, "تغيير مروحة التبريد", "2026-06-10"});
    }

    private void loadUsersFromDatabase() {
        usersModel.setRowCount(0);
        usersModel.addRow(new Object[]{1, "admin", "123", "Admin"});
        usersModel.addRow(new Object[]{2, "omar", "1234", "Employee"});
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
        styleButton(btnAddUser, new Color(16, 185, 129));
        styleButton(btnUpdateUserPrivilege, new Color(59, 130, 246));
        styleButton(btnDeleteUser, new Color(239, 68, 68));

        // واجهة إدارة الأجهزة وقفل خلايا الجدول الخاص بها
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

        // واجهة الحجوزات وقفل خلايا الجدول الخاص بها
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

        // واجهة الصيانة وقفل خلايا الجدول الخاص بها
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

        addSidebarComponent(maintSidebar, btnAddMaintenance, null);
        addSidebarComponent(maintSidebar, btnDeleteMaintenance, null);
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

        // الحدث عند اختيار عنصر من جدول الأجهزة لتعبئة الحقول تلقائياً
        tblItems.getSelectionModel().addListSelectionListener(e -> {
            int row = tblItems.getSelectedRow();
            if (row != -1) {
                txtItemName.setText(tblItems.getValueAt(row, 1).toString());
                txtItemType.setText(tblItems.getValueAt(row, 2).toString());
                txtItemRate.setText(tblItems.getValueAt(row, 3).toString());
            }
        });

        // الحدث عند اختيار حساب من جدول الحسابات لتعبئة الحقول تلقائياً
        tblUsers.getSelectionModel().addListSelectionListener(e -> {
            int row = tblUsers.getSelectedRow();
            if (row != -1) {
                txtManageUser.setText(tblUsers.getValueAt(row, 1).toString());
                txtManagePass.setText(tblUsers.getValueAt(row, 2).toString());
                cmbRole.setSelectedItem(tblUsers.getValueAt(row, 3).toString());
            }
        });

        // حدث إضافة حساب جديد
        btnAddUser.addActionListener(e -> {
            String username = txtManageUser.getText().trim();
            String password = txtManagePass.getText().trim();
            String role = cmbRole.getSelectedItem().toString();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "الرجاء كتابة البيانات أولاً!", "حقول فارغة", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int nextUserId = usersModel.getRowCount() + 1;
            usersModel.addRow(new Object[]{nextUserId, username, password, role});
            txtManageUser.setText(""); txtManagePass.setText("");
        });

        // حدث تعديل صلاحيات حساب مستخدم
        btnUpdateUserPrivilege.addActionListener(e -> {
            int selectedRow = tblUsers.getSelectedRow();
            if (selectedRow == -1) return;
            tblUsers.setValueAt(txtManageUser.getText().trim(), selectedRow, 1);
            tblUsers.setValueAt(txtManagePass.getText().trim(), selectedRow, 2);
            tblUsers.setValueAt(cmbRole.getSelectedItem().toString(), selectedRow, 3);
        });

        // حدث حذف حساب
        btnDeleteUser.addActionListener(e -> {
            int selectedRow = tblUsers.getSelectedRow();
            if (selectedRow != -1) usersModel.removeRow(selectedRow);
        });

        // حدث إضافة جهاز جديد
        btnAddItem.addActionListener(e -> {
            String name = txtItemName.getText().trim();
            String type = txtItemType.getText().trim();
            String rateStr = txtItemRate.getText().trim();

            if (name.isEmpty() || type.isEmpty() || rateStr.isEmpty()) return;

            try {
                double rate = Double.parseDouble(rateStr);
                int nextId = itemsModel.getRowCount() + 1;
                itemsModel.addRow(new Object[]{nextId, name, type, rate, "Available"});
                txtItemName.setText(""); txtItemType.setText(""); txtItemRate.setText("");
            } catch (Exception ex) {}
        });

        // 🛠️ حدث تعديل بيانات الجهاز (الكود الجديد والمحمي لمنع التداخل البرمجي)
        btnUpdateItem.addActionListener(e -> {
            int selectedRow = tblItems.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "الرجاء تحديد جهاز من الجدول أولاً!", "تنبيه", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                // 1. تخزين القيم المكتوبة حالياً في متغيرات لحمايتها قبل التحديث
                String updatedName = txtItemName.getText().trim();
                String updatedType = txtItemType.getText().trim();
                String updatedRateStr = txtItemRate.getText().trim();

                if (updatedName.isEmpty() || updatedType.isEmpty() || updatedRateStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "الرجاء عدم ترك الخانات فارغة!", "خطأ", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double updatedRate = Double.parseDouble(updatedRateStr);

                // 2. تحديث الموديل مباشرة بنجاح دون أي تجميد
                itemsModel.setValueAt(updatedName, selectedRow, 1);
                itemsModel.setValueAt(updatedType, selectedRow, 2);
                itemsModel.setValueAt(updatedRate, selectedRow, 3);

                JOptionPane.showMessageDialog(this, "تم تحديث بيانات الجهاز بنجاح! ✨", "نجاح العملية", JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "الرجاء إدخال رقم صحيح في خانة سعر الساعة!", "خطأ في الإدخال", JOptionPane.ERROR_MESSAGE);
            }
        });

        // حدث حذف جهاز
        btnDeleteItem.addActionListener(e -> {
            int row = tblItems.getSelectedRow();
            if (row != -1) itemsModel.removeRow(row);
        });

        // حدث بدء حجز اللعب
        btnStartRes.addActionListener(e -> {
            String devIdStr = txtResDeviceID.getText().trim();
            String durStr = txtResDuration.getText().trim();
            if(devIdStr.isEmpty() || durStr.isEmpty()) return;

            String targetDeviceName = "جهاز";
            double targetRate = 10.0;
            try {
                int searchId = Integer.parseInt(devIdStr);
                for(int i=0; i<itemsModel.getRowCount(); i++) {
                    if(Integer.parseInt(itemsModel.getValueAt(i, 0).toString()) == searchId) {
                        targetDeviceName = itemsModel.getValueAt(i, 1).toString();
                        targetRate = Double.parseDouble(itemsModel.getValueAt(i, 3).toString());
                        break;
                    }
                }
            } catch(Exception ex) {}

            try {
                double hours = Double.parseDouble(durStr);
                double total = hours * targetRate;
                LocalTime now = LocalTime.now();
                String currentTime = String.format("%02d:%02d", now.getHour(), now.getMinute());
                int nextBookingId = resModel.getRowCount() + 1;
                resModel.addRow(new Object[]{nextBookingId, devIdStr, targetDeviceName, currentTime, (hours == 0) ? "مفتوح" : hours + " ساعات", targetRate, (hours == 0) ? "جاري اللعب..." : total});
                txtResDeviceID.setText(""); txtResDuration.setText("");
            } catch (Exception ex) {}
        });

        // حدث إنهاء حجز والتشيك
        btnEndRes.addActionListener(e -> {
            int selectedRow = tblRes.getSelectedRow();
            if(selectedRow != -1) resModel.removeRow(selectedRow);
        });

        // 🛠️ [الكود الجديد المضاف]: أحداث واجهة الصيانة والتقارير المالية لتفعيل زر الإضافة والحذف
        btnAddMaintenance.addActionListener(e -> {
            String device = txtMaintDeviceID.getText().trim();
            String costStr = txtMaintCost.getText().trim();
            String desc = txtMaintDesc.getText().trim();
            String date = txtMaintDate.getText().trim();

            // التحقق من الحقول الفارغة
            if (device.isEmpty() || costStr.isEmpty() || desc.isEmpty() || date.isEmpty()) {
                JOptionPane.showMessageDialog(this, "الرجاء تعبئة جميع خانات التقرير أولاً!", "حقول فارغة", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // التحقق من صحة إدخال التكلفة كرقم وحساب الـ ID التالي تلقائياً
                double cost = Double.parseDouble(costStr);
                int nextMaintId = maintenanceModel.getRowCount() + 1;

                // في حالة إدخال رقم جهاز مجرد (مثل "1")، يتم صياغته تلقائياً ليطابق مظهر الجدول كـ "جهاز 1"
                String formattedDevice = device.matches("\\d+") ? "جهاز " + device : device;

                // إضافة السطر الجديد لموديل الجدول لتحديثه في الواجهة فوراً
                maintenanceModel.addRow(new Object[]{nextMaintId, formattedDevice, cost, desc, date});

                // تفريغ الحقول بعد الإضافة الناجحة وإبقاء التاريخ الافتراضي
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
            } else {
                JOptionPane.showMessageDialog(this, "الرجاء تحديد سطر من جدول الصيانة لحذفه!", "تنبيه", JOptionPane.WARNING_MESSAGE);
            }
        });

        tabs.setBackground(panelBlue);
        tabs.setForeground(textWhite);
        tabs.setFont(new Font("Arial", Font.BOLD, 13));
        tabs.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        dashboardContainer.add(tabs, BorderLayout.CENTER);

        // الشريط العلوي لشريط تسجيل الخروج
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(bgBlue);
        topBar.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        btnLogout.setFont(new Font("Arial", Font.BOLD, 13));
        btnLogout.setBackground(new Color(239, 68, 68));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setPreferredSize(new Dimension(140, 32));
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(e -> cardLayout.show(mainPanel, "Login"));

        topBar.add(btnLogout, BorderLayout.WEST);
        dashboardContainer.add(topBar, BorderLayout.NORTH);
        mainPanel.add(dashboardContainer, "Dashboard");
    }
}