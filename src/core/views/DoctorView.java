/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package core.views;

import core.views.AdminView;
import core.controllers.AppointmentController;
import core.controllers.DoctorController;
import core.controllers.HospitalizationController;
import core.models.DataObserver;
import core.models.DataStore;
import core.models.Response;
import java.awt.Color;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import core.models.Administrator;
import core.models.Appointment;
import core.models.Doctor;
import core.models.Hospitalization;
import core.models.Patient;
import core.models.RoomType;
import core.models.Specialty;
import core.models.User;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author jjlora
 * @author edangulo
 */
public class DoctorView extends javax.swing.JFrame implements DataObserver {

    private int x, y;
    private User user;
    private ArrayList<User> users;
    private ArrayList<Hospitalization>hospitalizations;
    private ArrayList<Appointment>appointments;
    private Doctor doctor;
    private Patient patient;
    public DoctorView(User user,Doctor doc, ArrayList<User> users,ArrayList<Hospitalization> hospitalizations,ArrayList<Appointment> appointments) {
        initComponents();
        this.user = user;
        this.users =users;
        this.doctor = doc;
        this.hospitalizations = hospitalizations;
        this.appointments = appointments;
        if (user instanceof Administrator)
            btnBackToAdmin.setVisible(true);
        else
            btnBackToAdmin.setVisible(false);
        this.setBackground(new Color(0, 0, 0, 0));
        this.setLocationRelativeTo(null);
        // Mostrar nombre del doctor en la barra de título
        if (doc != null) {
            this.setTitle("Ospedale — Dr. " + doc.getLastname());
            lblViewTitle.setText("DOCTOR VIEW — " + doc.getFirstname() + " " + doc.getLastname());
        }
        // Hints de formato en labels
        lblRescheduleTimeLbl.setText("New time (HH:mm)");
        lblHospEntryDateLbl.setText("Date of entry (YYYY-MM-DD)");
        // Poblar dropdowns
        populateAppointmentDropdowns();
        populatePatientDropdown();
        // Pre-rellenar campos de Modify Info con datos actuales del doctor
        prefillDoctorInfo();
        // Mostrar todas las citas al abrir
        rbtnShowAllAppts.setSelected(true);
        rbtnShowAllApptsActionPerformed(null);
        // Register as observer for auto-refresh
        DataStore.getInstance().addObserver(this);
    }

    /**
     * Observer callback: auto-refreshes tables when appointments/hospitalizations change.
     */
    @Override
    public void onDataChanged(String entityType) {
        if ("appointments".equals(entityType)) {
            refreshAppointmentView();
            populateAppointmentDropdowns();
        } else if ("hospitalizations".equals(entityType)) {
            populateAppointmentDropdowns();
        }
    }

    private void populateAppointmentDropdowns() {
        cmbAcceptAppt.removeAllItems();
        cmbRescheduleAppt.removeAllItems();
        cmbCompleteAppt.removeAllItems();
        cmbPrescribeAppt.removeAllItems();
        cmbAcceptAppt.addItem("Select one");
        cmbRescheduleAppt.addItem("Select one");
        cmbCompleteAppt.addItem("Select one");
        cmbPrescribeAppt.addItem("Select one");
        // Use controller to get serialized appointment IDs — no model objects in view
        if (doctor != null) {
            AppointmentController apptCtrl = new AppointmentController();
            Response resp = apptCtrl.getDoctorAppointmentsResponse(doctor.getId(), false);
            if (resp.isSuccess() && resp.getData() != null) {
                JSONArray arr = resp.getData().optJSONArray("appointments");
                if (arr != null) {
                    for (int i = 0; i < arr.length(); i++) {
                        String id = arr.getJSONObject(i).optString("id");
                        cmbAcceptAppt.addItem(id);
                        cmbRescheduleAppt.addItem(id);
                        cmbCompleteAppt.addItem(id);
                        cmbPrescribeAppt.addItem(id);
                    }
                }
            }
        }
        // Hospitalizaciones en cmbHospCancel — via controller
        cmbHospCancel.removeAllItems();
        cmbHospCancel.addItem("Select one");
        if (doctor != null) {
            HospitalizationController hospCtrl = new HospitalizationController();
            Response hospResp = hospCtrl.getDoctorHospitalizationsResponse(doctor.getId());
            if (hospResp.isSuccess() && hospResp.getData() != null) {
                JSONArray arr = hospResp.getData().optJSONArray("hospitalizations");
                if (arr != null) {
                    for (int i = 0; i < arr.length(); i++) {
                        cmbHospCancel.addItem(arr.getJSONObject(i).optString("id"));
                    }
                }
            }
        }
    }

    private void populatePatientDropdown() {
        cmbPatientSearch.removeAllItems();
        cmbPatientSearch.addItem("Select one");
        cmbPatientHosp.removeAllItems();
        cmbPatientHosp.addItem("Select one");
        for (User u : this.users) {
            if (u instanceof Patient) {
                String display = u.getId() + " — " + u.getFirstname() + " " + u.getLastname();
                cmbPatientSearch.addItem(display);
                cmbPatientHosp.addItem(display);
            }
        }
    }

    /** Extrae el ID numérico del formato "id — Name" de los comboboxes. */
    private long extractId(String item) {
        if (item == null || item.contains("Select")) throw new NumberFormatException("No selection");
        return Long.parseLong(item.split(" — ")[0].trim());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelRound1 = new core.controllers.PanelRound();
        panelRound2 = new core.controllers.PanelRound();
        btnClose = new javax.swing.JButton();
        lblViewTitle = new javax.swing.JLabel();
        btnBackToAdmin = new javax.swing.JButton();
        tabbedPaneDoctor = new javax.swing.JTabbedPane();
        panelHospitalize = new javax.swing.JPanel();
        rbtnShowAllAppts = new javax.swing.JRadioButton();
        scrollHospObservations = new javax.swing.JScrollPane();
        tblAppointments = new javax.swing.JTable();
        rbtnPendingOnly = new javax.swing.JRadioButton();
        btnLogout = new javax.swing.JButton();
        panelPrescribe = new javax.swing.JPanel();
        cmbPatientSearch = new javax.swing.JComboBox<>();
        lblDoc38 = new javax.swing.JLabel();
        scrollDiagnosis = new javax.swing.JScrollPane();
        tblPatientHistory = new javax.swing.JTable();
        btnSearchPatientHistory = new javax.swing.JButton();
        panelPatientHistory = new javax.swing.JPanel();
        lblDoc02 = new javax.swing.JLabel();
        txtProfileFirstname = new javax.swing.JTextField();
        lblFirstnameLbl = new javax.swing.JLabel();
        txtProfileLastname = new javax.swing.JTextField();
        lblLastnameLbl = new javax.swing.JLabel();
        lblSpecialtyLbl = new javax.swing.JLabel();
        txtProfileLicence = new javax.swing.JTextField();
        lblLicenceLbl = new javax.swing.JLabel();
        txtProfileUsername = new javax.swing.JTextField();
        lblOfficeLbl = new javax.swing.JLabel();
        txtProfileOffice = new javax.swing.JTextField();
        txtProfilePassword = new javax.swing.JTextField();
        lblPasswordLbl = new javax.swing.JLabel();
        lblConfirmPwdLbl = new javax.swing.JLabel();
        txtProfileConfirmPwd = new javax.swing.JTextField();
        cmbProfileSpecialty = new javax.swing.JComboBox<>();
        btnUpdateProfile = new javax.swing.JButton();
        panelAppointments = new javax.swing.JPanel();
        lblPatientSearchLbl = new javax.swing.JLabel();
        lblAcceptApptLbl = new javax.swing.JLabel();
        cmbAcceptAppt = new javax.swing.JComboBox<>();
        sepHeader = new javax.swing.JSeparator();
        btnAcceptAppointment = new javax.swing.JButton();
        lblRescheduleApptLbl = new javax.swing.JLabel();
        lblRescheduleReasonLbl = new javax.swing.JLabel();
        cmbRescheduleAppt = new javax.swing.JComboBox<>();
        btnRescheduleAppointment = new javax.swing.JButton();
        lblRescheduleTimeLbl = new javax.swing.JLabel();
        txtRescheduleTime = new javax.swing.JTextField();
        lblDoc18 = new javax.swing.JLabel();
        txtRescheduleReason = new javax.swing.JTextField();
        sepContent = new javax.swing.JSeparator();
        lblDiagnosisLbl = new javax.swing.JLabel();
        lblObservationsLbl = new javax.swing.JLabel();
        cmbCompleteAppt = new javax.swing.JComboBox<>();
        lblTreatmentLbl = new javax.swing.JLabel();
        lblFollowUpLbl = new javax.swing.JLabel();
        lblCompleteApptLbl = new javax.swing.JLabel();
        lblDoc24 = new javax.swing.JLabel();
        btnCompleteAppointment = new javax.swing.JButton();
        lblHospPatientLbl = new javax.swing.JLabel();
        lblHospObsLbl = new javax.swing.JLabel();
        lblHospEntryDateLbl = new javax.swing.JLabel();
        txtHospEntryDate = new javax.swing.JTextField();
        lblHospEstDurLbl = new javax.swing.JLabel();
        txtHospEstDuration = new javax.swing.JTextField();
        lblDoc30 = new javax.swing.JLabel();
        scrollPrescriptionsSaved = new javax.swing.JScrollPane();
        txtaHospObservations = new javax.swing.JTextArea();
        btnRequestHospitalization = new javax.swing.JButton();
        cmbHospCancel = new javax.swing.JComboBox<>();
        rbtnInPersonAppt = new javax.swing.JRadioButton();
        rbtnVirtualAppt = new javax.swing.JRadioButton();
        scrollObservations = new javax.swing.JScrollPane();
        txtaDiagnosis = new javax.swing.JTextArea();
        scrollTreatment = new javax.swing.JScrollPane();
        txtaObservations = new javax.swing.JTextArea();
        scrollFollowUp = new javax.swing.JScrollPane();
        txtaTreatment = new javax.swing.JTextArea();
        scrollHospReason = new javax.swing.JScrollPane();
        txtaFollowUp = new javax.swing.JTextArea();
        sepProfileSection = new javax.swing.JSeparator();
        btnCancelHospitalization = new javax.swing.JButton();
        cmbPatientHosp = new javax.swing.JComboBox<>();
        scrollPatientHistory = new javax.swing.JScrollPane();
        txtaHospReason = new javax.swing.JTextArea();
        panelMedications = new javax.swing.JPanel();
        lblPrescribeApptLbl = new javax.swing.JLabel();
        lblMedNameLbl = new javax.swing.JLabel();
        txtMedName = new javax.swing.JTextField();
        lblMedDoseLbl = new javax.swing.JLabel();
        txtMedDose = new javax.swing.JTextField();
        lblMedRouteLbl = new javax.swing.JLabel();
        txtMedRoute = new javax.swing.JTextField();
        lblMedFreqLbl = new javax.swing.JLabel();
        txtMedFrequency = new javax.swing.JTextField();
        lblMedDurationLbl = new javax.swing.JLabel();
        txtMedDuration = new javax.swing.JTextField();
        lblDoc37 = new javax.swing.JLabel();
        txtMedInstructions = new javax.swing.JTextField();
        scrollPrescriptionStaging = new javax.swing.JScrollPane();
        tblPrescriptions = new javax.swing.JTable();
        btnAddPrescription = new javax.swing.JButton();
        btnFinalizePrescriptions = new javax.swing.JButton();
        cmbPrescribeAppt = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        panelRound1.setRadius(50);

        panelRound2.setRadius(50);
        panelRound2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                panelRound2MouseDragged(evt);
            }
        });
        panelRound2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelRound2MousePressed(evt);
            }
        });

        btnClose.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnClose.setText("X");
        btnClose.setBorderPainted(false);
        btnClose.setContentAreaFilled(false);
        btnClose.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnClose.setFocusable(false);
        btnClose.setRequestFocusEnabled(false);
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        lblViewTitle.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        lblViewTitle.setText("DOCTOR VIEW");

        btnBackToAdmin.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnBackToAdmin.setText("Back");
        btnBackToAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackToAdminActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelRound2Layout = new javax.swing.GroupLayout(panelRound2);
        panelRound2.setLayout(panelRound2Layout);
        panelRound2Layout.setHorizontalGroup(
            panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRound2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblViewTitle)
                .addGap(32, 32, 32)
                .addComponent(btnBackToAdmin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnClose)
                .addGap(19, 19, 19))
        );
        panelRound2Layout.setVerticalGroup(
            panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblViewTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnBackToAdmin))
        );

        rbtnShowAllAppts.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        rbtnShowAllAppts.setText("Total appointments");
        rbtnShowAllAppts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnShowAllApptsActionPerformed(evt);
            }
        });

        tblAppointments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Date", "Patient", "Specialty", "Type", "Status"
            }
        ));
        scrollHospObservations.setViewportView(tblAppointments);

        rbtnPendingOnly.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        rbtnPendingOnly.setText("Pending appointments");
        rbtnPendingOnly.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnPendingOnlyActionPerformed(evt);
            }
        });

        btnLogout.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnLogout.setText("Logout");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(panelHospitalize);
        panelHospitalize.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnLogout)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addGap(16, 16, 16)
                            .addComponent(rbtnShowAllAppts)
                            .addGap(18, 18, 18)
                            .addComponent(rbtnPendingOnly))
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addGap(108, 108, 108)
                            .addComponent(scrollHospObservations, javax.swing.GroupLayout.PREFERRED_SIZE, 1035, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(152, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtnShowAllAppts)
                    .addComponent(rbtnPendingOnly))
                .addGap(18, 18, 18)
                .addComponent(scrollHospObservations, javax.swing.GroupLayout.PREFERRED_SIZE, 504, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(btnLogout)
                .addGap(23, 23, 23))
        );

        tabbedPaneDoctor.addTab("Appointments visualization", panelHospitalize);

        cmbPatientSearch.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cmbPatientSearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one" }));

        lblDoc38.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDoc38.setText("Patient");

        tblPatientHistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Date", "Doctor", "Specialty", "Type", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollDiagnosis.setViewportView(tblPatientHistory);

        btnSearchPatientHistory.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnSearchPatientHistory.setText("Search");
        btnSearchPatientHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchPatientHistoryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(panelPrescribe);
        panelPrescribe.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(lblDoc38)
                        .addGap(18, 18, 18)
                        .addComponent(cmbPatientSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(scrollDiagnosis, javax.swing.GroupLayout.PREFERRED_SIZE, 1133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(99, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnSearchPatientHistory)
                .addGap(601, 601, 601))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDoc38)
                    .addComponent(cmbPatientSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(scrollDiagnosis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(btnSearchPatientHistory)
                .addContainerGap(67, Short.MAX_VALUE))
        );

        tabbedPaneDoctor.addTab("History Appointments of a patient", panelPrescribe);

        lblDoc02.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDoc02.setText("Firstname");

        txtProfileFirstname.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblFirstnameLbl.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblFirstnameLbl.setText("Lastname");

        txtProfileLastname.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblLastnameLbl.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblLastnameLbl.setText("Specialty");

        lblSpecialtyLbl.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblSpecialtyLbl.setText("License Number");

        txtProfileLicence.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblLicenceLbl.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblLicenceLbl.setText("Assigned office");

        txtProfileUsername.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblOfficeLbl.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblOfficeLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOfficeLbl.setText("User");

        txtProfileOffice.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        txtProfilePassword.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblPasswordLbl.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblPasswordLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPasswordLbl.setText("Password");

        lblConfirmPwdLbl.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblConfirmPwdLbl.setText("Password confirmation");

        txtProfileConfirmPwd.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        cmbProfileSpecialty.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cmbProfileSpecialty.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one", "General Medicine", "Cardiology", "Pediatrics", "Neurology", "Traumatology & Orthopedics", "Gynecology & Obstetrics", "Dermatology", "Psychiatry", "Oncology", "Ophthalmology", "Internal Medicine" }));

        btnUpdateProfile.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnUpdateProfile.setText("Save");
        btnUpdateProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateProfileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(panelPatientHistory);
        panelPatientHistory.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(211, 211, 211)
                        .addComponent(lblDoc02)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtProfileFirstname, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblFirstnameLbl)
                        .addGap(18, 18, 18)
                        .addComponent(txtProfileLastname, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblLastnameLbl)
                        .addGap(18, 18, 18)
                        .addComponent(cmbProfileSpecialty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(351, 351, 351)
                        .addComponent(lblSpecialtyLbl)
                        .addGap(18, 18, 18)
                        .addComponent(txtProfileLicence, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblLicenceLbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtProfileOffice, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(558, 558, 558)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtProfilePassword, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtProfileUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                                .addComponent(lblOfficeLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblPasswordLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(521, 521, 521)
                        .addComponent(lblConfirmPwdLbl))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(576, 576, 576)
                        .addComponent(btnUpdateProfile))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(561, 561, 561)
                        .addComponent(txtProfileConfirmPwd, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(269, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDoc02)
                    .addComponent(txtProfileFirstname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFirstnameLbl)
                    .addComponent(txtProfileLastname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLastnameLbl)
                    .addComponent(cmbProfileSpecialty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSpecialtyLbl)
                    .addComponent(txtProfileLicence, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProfileOffice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLicenceLbl))
                .addGap(30, 30, 30)
                .addComponent(lblOfficeLbl)
                .addGap(18, 18, 18)
                .addComponent(txtProfileUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblPasswordLbl)
                .addGap(27, 27, 27)
                .addComponent(txtProfilePassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblConfirmPwdLbl)
                .addGap(18, 18, 18)
                .addComponent(txtProfileConfirmPwd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(btnUpdateProfile)
                .addContainerGap(161, Short.MAX_VALUE))
        );

        tabbedPaneDoctor.addTab("Modify info", panelPatientHistory);

        lblPatientSearchLbl.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblPatientSearchLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPatientSearchLbl.setText("Appointment ID");

        lblAcceptApptLbl.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblAcceptApptLbl.setText("Accept medical appointment");

        cmbAcceptAppt.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cmbAcceptAppt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one" }));

        sepHeader.setOrientation(javax.swing.SwingConstants.VERTICAL);

        btnAcceptAppointment.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnAcceptAppointment.setText("Accept");
        btnAcceptAppointment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcceptAppointmentActionPerformed(evt);
            }
        });

        lblRescheduleApptLbl.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblRescheduleApptLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRescheduleApptLbl.setText("Reschedule medical appointment");

        lblRescheduleReasonLbl.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblRescheduleReasonLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRescheduleReasonLbl.setText("Appointment");

        cmbRescheduleAppt.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cmbRescheduleAppt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one" }));

        btnRescheduleAppointment.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnRescheduleAppointment.setText("Accept");
        btnRescheduleAppointment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRescheduleAppointmentActionPerformed(evt);
            }
        });

        lblRescheduleTimeLbl.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblRescheduleTimeLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRescheduleTimeLbl.setText("New time appointment");

        txtRescheduleTime.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblDoc18.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDoc18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDoc18.setText("Reason for appointment");

        txtRescheduleReason.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        sepContent.setOrientation(javax.swing.SwingConstants.VERTICAL);

        lblDiagnosisLbl.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDiagnosisLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDiagnosisLbl.setText("Complete medical appointment");

        lblObservationsLbl.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblObservationsLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblObservationsLbl.setText("Appointment");

        cmbCompleteAppt.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cmbCompleteAppt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one" }));

        lblTreatmentLbl.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblTreatmentLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTreatmentLbl.setText("Diagnosis");

        lblFollowUpLbl.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblFollowUpLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFollowUpLbl.setText("Observations");

        lblCompleteApptLbl.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblCompleteApptLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCompleteApptLbl.setText("Recommended treatment");

        lblDoc24.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDoc24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDoc24.setText("Follow-up indication");

        btnCompleteAppointment.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnCompleteAppointment.setText("Complete");
        btnCompleteAppointment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCompleteAppointmentActionPerformed(evt);
            }
        });

        lblHospPatientLbl.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblHospPatientLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHospPatientLbl.setText("Hospitalization");

        lblHospObsLbl.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblHospObsLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHospObsLbl.setText("Reason for hospitalization");

        lblHospEntryDateLbl.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblHospEntryDateLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHospEntryDateLbl.setText("Date of entry");

        txtHospEntryDate.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblHospEstDurLbl.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblHospEstDurLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHospEstDurLbl.setText("Estimated duration");

        txtHospEstDuration.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblDoc30.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDoc30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDoc30.setText("Observations");

        txtaHospObservations.setColumns(20);
        txtaHospObservations.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        txtaHospObservations.setRows(5);
        scrollPrescriptionsSaved.setViewportView(txtaHospObservations);

        btnRequestHospitalization.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnRequestHospitalization.setText("Generate");
        btnRequestHospitalization.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRequestHospitalizationActionPerformed(evt);
            }
        });

        cmbHospCancel.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cmbHospCancel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one" }));

        rbtnInPersonAppt.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        rbtnInPersonAppt.setText("Requests");

        rbtnVirtualAppt.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        rbtnVirtualAppt.setText("Patient ID");

        txtaDiagnosis.setColumns(20);
        txtaDiagnosis.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        txtaDiagnosis.setRows(5);
        scrollObservations.setViewportView(txtaDiagnosis);

        txtaObservations.setColumns(20);
        txtaObservations.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        txtaObservations.setRows(5);
        scrollTreatment.setViewportView(txtaObservations);

        txtaTreatment.setColumns(20);
        txtaTreatment.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        txtaTreatment.setRows(5);
        scrollFollowUp.setViewportView(txtaTreatment);

        txtaFollowUp.setColumns(20);
        txtaFollowUp.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        txtaFollowUp.setRows(5);
        scrollHospReason.setViewportView(txtaFollowUp);

        sepProfileSection.setOrientation(javax.swing.SwingConstants.VERTICAL);

        btnCancelHospitalization.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnCancelHospitalization.setText("Cancel");
        btnCancelHospitalization.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelHospitalizationActionPerformed(evt);
            }
        });

        cmbPatientHosp.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cmbPatientHosp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one" }));

        txtaHospReason.setColumns(20);
        txtaHospReason.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        txtaHospReason.setRows(5);
        scrollPatientHistory.setViewportView(txtaHospReason);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(panelAppointments);
        panelAppointments.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(btnAcceptAppointment)
                                        .addGap(87, 87, 87))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(cmbAcceptAppt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(67, 67, 67))))
                            .addComponent(lblPatientSearchLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(sepHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(lblAcceptApptLbl)
                        .addGap(22, 22, 22)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblRescheduleApptLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblRescheduleReasonLbl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblRescheduleTimeLbl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblDoc18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(90, 90, 90)
                                    .addComponent(cmbRescheduleAppt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(99, 99, 99)
                                    .addComponent(txtRescheduleTime, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(98, 98, 98)
                                    .addComponent(txtRescheduleReason, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(112, 112, 112)
                                    .addComponent(btnRescheduleAppointment)))
                            .addGap(91, 91, 91))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sepContent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(112, 112, 112)
                        .addComponent(btnCompleteAppointment)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(lblObservationsLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblDiagnosisLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(99, 99, 99)
                                        .addComponent(cmbCompleteAppt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 25, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblTreatmentLbl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblFollowUpLbl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(lblDoc24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblCompleteApptLbl, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(42, 42, 42)
                                        .addComponent(scrollObservations, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(41, 41, 41)
                                        .addComponent(scrollTreatment, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(42, 42, 42)
                                        .addComponent(scrollFollowUp, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(43, 43, 43)
                                        .addComponent(scrollHospReason, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(sepProfileSection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblHospPatientLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblHospEntryDateLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblHospEstDurLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblDoc30, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(121, 121, 121)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtHospEntryDate, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtHospEstDuration, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnCancelHospitalization)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnRequestHospitalization))
                            .addComponent(scrollPrescriptionsSaved, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(56, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(cmbHospCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addComponent(rbtnInPersonAppt)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(rbtnVirtualAppt, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(19, 19, 19))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(cmbPatientHosp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblHospObsLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(scrollPatientHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sepHeader)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sepContent)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(lblDiagnosisLbl)
                        .addGap(10, 10, 10)
                        .addComponent(lblObservationsLbl)
                        .addGap(18, 18, 18)
                        .addComponent(cmbCompleteAppt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblTreatmentLbl)
                        .addGap(18, 18, 18)
                        .addComponent(scrollObservations, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblFollowUpLbl)
                        .addGap(18, 18, 18)
                        .addComponent(scrollTreatment, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblCompleteApptLbl)
                        .addGap(18, 18, 18)
                        .addComponent(scrollFollowUp, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblDoc24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollHospReason, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCompleteAppointment)
                        .addGap(12, 12, 12))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(lblAcceptApptLbl)
                                .addGap(18, 18, 18)
                                .addComponent(lblPatientSearchLbl)
                                .addGap(18, 18, 18)
                                .addComponent(cmbAcceptAppt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(btnAcceptAppointment))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(lblRescheduleApptLbl)
                                .addGap(18, 18, 18)
                                .addComponent(lblRescheduleReasonLbl)
                                .addGap(18, 18, 18)
                                .addComponent(cmbRescheduleAppt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblRescheduleTimeLbl)
                                .addGap(18, 18, 18)
                                .addComponent(txtRescheduleTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblDoc18)
                                .addGap(18, 18, 18)
                                .addComponent(txtRescheduleReason, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(btnRescheduleAppointment)))
                        .addGap(18, 18, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblHospPatientLbl)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtnInPersonAppt)
                    .addComponent(rbtnVirtualAppt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbHospCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbPatientHosp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(lblHospObsLbl)
                .addGap(16, 16, 16)
                .addComponent(scrollPatientHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblHospEntryDateLbl)
                .addGap(18, 18, 18)
                .addComponent(txtHospEntryDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblHospEstDurLbl)
                .addGap(18, 18, 18)
                .addComponent(txtHospEstDuration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblDoc30)
                .addGap(18, 18, 18)
                .addComponent(scrollPrescriptionsSaved, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRequestHospitalization)
                    .addComponent(btnCancelHospitalization))
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(sepProfileSection, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        tabbedPaneDoctor.addTab("Request/Appointments", panelAppointments);

        lblPrescribeApptLbl.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblPrescribeApptLbl.setText("Appointment ID");

        lblMedNameLbl.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblMedNameLbl.setText("Medication name");

        txtMedName.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblMedDoseLbl.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblMedDoseLbl.setText("Dose");

        txtMedDose.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblMedRouteLbl.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblMedRouteLbl.setText("Administration route");

        txtMedRoute.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblMedFreqLbl.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblMedFreqLbl.setText("Frecuency");

        txtMedFrequency.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblMedDurationLbl.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblMedDurationLbl.setText("Treatment duration");

        txtMedDuration.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblDoc37.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDoc37.setText("Additional instructions");

        txtMedInstructions.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        tblPrescriptions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Appointment ID", "Medication name", "Dose", "Administration route", "Treatment duration", "Additional instructions", "Frecuency"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollPrescriptionStaging.setViewportView(tblPrescriptions);

        btnAddPrescription.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnAddPrescription.setText("Add");
        btnAddPrescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPrescriptionActionPerformed(evt);
            }
        });

        btnFinalizePrescriptions.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnFinalizePrescriptions.setText("Prescribe");
        btnFinalizePrescriptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinalizePrescriptionsActionPerformed(evt);
            }
        });

        cmbPrescribeAppt.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cmbPrescribeAppt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(panelMedications);
        panelMedications.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(scrollPrescriptionStaging, javax.swing.GroupLayout.PREFERRED_SIZE, 1125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(lblPrescribeApptLbl)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmbPrescribeAppt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(9, 9, 9)
                                        .addComponent(lblMedNameLbl))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(lblMedDurationLbl)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtMedDuration, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(lblDoc37)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtMedInstructions, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblMedFreqLbl)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtMedFrequency, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(txtMedName, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(lblMedDoseLbl)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtMedDose, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(lblMedRouteLbl)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtMedRoute, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAddPrescription))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(583, 583, 583)
                        .addComponent(btnFinalizePrescriptions)))
                .addContainerGap(108, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPrescribeApptLbl)
                    .addComponent(lblMedNameLbl)
                    .addComponent(txtMedName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMedDoseLbl)
                    .addComponent(txtMedDose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMedRouteLbl)
                    .addComponent(txtMedRoute, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddPrescription)
                    .addComponent(cmbPrescribeAppt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMedDurationLbl)
                    .addComponent(txtMedDuration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDoc37)
                    .addComponent(txtMedInstructions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMedFreqLbl)
                    .addComponent(txtMedFrequency, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(scrollPrescriptionStaging, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(btnFinalizePrescriptions)
                .addContainerGap(64, Short.MAX_VALUE))
        );

        tabbedPaneDoctor.addTab("Prescribe medications", panelMedications);

        javax.swing.GroupLayout panelRound1Layout = new javax.swing.GroupLayout(panelRound1);
        panelRound1.setLayout(panelRound1Layout);
        panelRound1Layout.setHorizontalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelRound2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tabbedPaneDoctor))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelRound1Layout.setVerticalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addComponent(panelRound2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabbedPaneDoctor))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRound1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRound1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void panelRound2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRound2MousePressed
        x = evt.getX();
        y = evt.getY();
    }//GEN-LAST:event_panelRound2MousePressed

    private void panelRound2MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRound2MouseDragged
        this.setLocation(this.getLocation().x + evt.getX() - x, this.getLocation().y + evt.getY() - y);
    }//GEN-LAST:event_panelRound2MouseDragged

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void rbtnPendingOnlyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        rbtnShowAllAppts.setSelected(false);
        DefaultTableModel model = (DefaultTableModel) tblAppointments.getModel();
        model.setRowCount(0);
        if (doctor == null) return;
        AppointmentController apptController = new AppointmentController();
        Response resp = apptController.getDoctorAppointmentsResponse(doctor.getId(), true);
        if (resp.isSuccess() && resp.getData() != null) {
            JSONArray arr = resp.getData().optJSONArray("appointments");
            if (arr != null) {
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject a = arr.getJSONObject(i);
                    model.addRow(new Object[]{
                        a.optString("id"),
                        a.optString("datetime"),
                        a.optString("patientName"),
                        a.optString("specialty"),
                        a.optString("type"),
                        a.optString("status")
                    });
                }
            }
        }
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void btnUpdateProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        if (doctor == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "No doctor profile to modify.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        String firstname      = txtProfileFirstname.getText().trim();
        String lastname       = txtProfileLastname.getText().trim();
        String spec           = (String) cmbProfileSpecialty.getSelectedItem();
        String licenseNumber  = txtProfileLicence.getText().trim();
        String assignedOffice = txtProfileOffice.getText().trim();
        String password       = txtProfilePassword.getText();
        String comPassword    = txtProfileConfirmPwd.getText();

        // Parse specialty (optional — only update if not "Select one")
        Specialty specialty = "Select one".equals(spec) ? null : parseSpecialty(spec);

        // Delegate update to DoctorController
        DoctorController doctorController = new DoctorController();
        Response response = doctorController.updateDoctor(
                doctor.getId(), firstname, lastname, password, comPassword,
                specialty, licenseNumber, assignedOffice);

        if (!response.isSuccess()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "[" + response.getStatus() + "] " + response.getMessage(),
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Clear password fields for security
        txtProfilePassword.setText(""); txtProfileConfirmPwd.setText("");
        // Update title bar to reflect name changes
        lblViewTitle.setText("DOCTOR VIEW — " + doctor.getFirstname() + " " + doctor.getLastname());
        this.setTitle("Ospedale — Dr. " + doctor.getLastname());
        javax.swing.JOptionPane.showMessageDialog(this,
                response.getMessage(), "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        ViewNavigator.getInstance().showLogin();
    }//GEN-LAST:event_jButton12ActionPerformed

    /** Refresca los dropdowns de citas y la tabla actualmente visible. */
    private void refreshAppointmentView() {
        populateAppointmentDropdowns();
        if (rbtnShowAllAppts.isSelected()) rbtnShowAllApptsActionPerformed(null);
        else if (rbtnPendingOnly.isSelected()) rbtnPendingOnlyActionPerformed(null);
        else { rbtnShowAllAppts.setSelected(true); rbtnShowAllApptsActionPerformed(null); }
    }

    private void btnBackToAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        ViewNavigator.getInstance().showAdmin(user);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void btnCancelHospitalizationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        String selectedId = (String) cmbHospCancel.getSelectedItem();
        if (selectedId == null || "Select one".equals(selectedId)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a hospitalization to cancel.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Delegate to HospitalizationController (enforces REQUESTED→CANCELED rule)
        HospitalizationController hospController = new HospitalizationController();
        Response response = hospController.cancelHospitalization(selectedId);

        if (!response.isSuccess()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "[" + response.getStatus() + "] " + response.getMessage(),
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        javax.swing.JOptionPane.showMessageDialog(this,
                response.getMessage(), "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void btnRequestHospitalizationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        String selectedPatientStr = (String) cmbPatientHosp.getSelectedItem();
        if (selectedPatientStr == null || "Select one".equals(selectedPatientStr)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a patient.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        long targetPatientId;
        try {
            targetPatientId = extractId(selectedPatientStr);
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Invalid patient selection.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        String reason            = txtaHospReason.getText().trim();
        String estimatedDuration = txtHospEstDuration.getText().trim();
        String baseObservations  = txtaHospObservations.getText().trim();
        String observations      = baseObservations + (estimatedDuration.isEmpty() ? "" : " | Est. duration: " + estimatedDuration + " days");
        String entDate           = txtHospEntryDate.getText().trim();
        if (entDate.isEmpty()) entDate = LocalDate.now().toString();

        // Delegate to HospitalizationController
        HospitalizationController hospController = new HospitalizationController();
        Response response = hospController.requestHospitalization(
                targetPatientId, this.doctor.getId(), entDate, reason, RoomType.IMC, observations);

        if (!response.isSuccess()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "[" + response.getStatus() + "] " + response.getMessage(),
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Observer pattern auto-refreshes cmbHospCancel via onDataChanged("hospitalizations")
        txtaHospReason.setText(""); txtaHospObservations.setText(""); txtHospEntryDate.setText(""); txtHospEstDuration.setText("");
        javax.swing.JOptionPane.showMessageDialog(this,
                response.getMessage(), "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void btnSearchPatientHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        String selectedPid = (String) cmbPatientSearch.getSelectedItem();
        if (selectedPid == null || "Select one".equals(selectedPid)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a patient.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        long patientId;
        try {
            patientId = extractId(selectedPid);
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Invalid patient ID.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        AppointmentController apptController = new AppointmentController();
        Response resp = apptController.getPatientAppointmentsResponse(patientId);

        DefaultTableModel model = (DefaultTableModel) tblPatientHistory.getModel();
        model.setRowCount(0);

        if (resp.isSuccess() && resp.getData() != null) {
            JSONArray arr = resp.getData().optJSONArray("appointments");
            if (arr != null) {
                if (arr.length() == 0) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Patient has no appointments.", "Info", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject a = arr.getJSONObject(i);
                    model.addRow(new Object[]{
                        a.optString("id"),
                        a.optString("datetime"),
                        a.optString("doctorName"),
                        a.optString("specialty"),
                        a.optString("type"),
                        a.optString("status")
                    });
                }
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Patient not found or has no appointments.", "Info", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void rbtnShowAllApptsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        rbtnPendingOnly.setSelected(false);
        DefaultTableModel model = (DefaultTableModel) tblAppointments.getModel();
        model.setRowCount(0);
        if (doctor == null) return;
        AppointmentController apptController = new AppointmentController();
        Response resp = apptController.getDoctorAppointmentsResponse(doctor.getId(), false);
        if (resp.isSuccess() && resp.getData() != null) {
            JSONArray arr = resp.getData().optJSONArray("appointments");
            if (arr != null) {
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject a = arr.getJSONObject(i);
                    model.addRow(new Object[]{
                        a.optString("id"),
                        a.optString("datetime"),
                        a.optString("patientName"),
                        a.optString("specialty"),
                        a.optString("type"),
                        a.optString("status")
                    });
                }
            }
        }
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void btnAcceptAppointmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        String idAppointment = (String) cmbAcceptAppt.getSelectedItem();
        if (idAppointment == null || "Select one".equals(idAppointment)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select an appointment to accept.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Delegate to AppointmentController (enforces REQUESTED→PENDING rule)
        AppointmentController apptController = new AppointmentController();
        Response response = apptController.acceptAppointment(idAppointment);

        if (!response.isSuccess()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "[" + response.getStatus() + "] " + response.getMessage(),
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        refreshAppointmentView();
        javax.swing.JOptionPane.showMessageDialog(this,
                response.getMessage(), "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnCompleteAppointmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        String idAppointment = (String) cmbCompleteAppt.getSelectedItem();
        if (idAppointment == null || "Select one".equals(idAppointment)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select an appointment to complete.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        String diagnosis    = txtaDiagnosis.getText();
        String observations = txtaObservations.getText();
        String treatment    = txtaTreatment.getText();
        String followUp     = txtaFollowUp.getText();

        // Delegate to AppointmentController (enforces PENDING→COMPLETED rule)
        AppointmentController apptController = new AppointmentController();
        Response response = apptController.completeAppointment(
                idAppointment, diagnosis, observations, treatment, followUp);

        if (!response.isSuccess()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "[" + response.getStatus() + "] " + response.getMessage(),
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        txtaDiagnosis.setText(""); txtaObservations.setText(""); txtaTreatment.setText(""); txtaFollowUp.setText("");
        refreshAppointmentView();
        javax.swing.JOptionPane.showMessageDialog(this,
                response.getMessage(), "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void btnFinalizePrescriptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // This button clears the staging table — prescriptions were already saved via the 'Add' button.
        DefaultTableModel model = (DefaultTableModel) tblPrescriptions.getModel();
        int rowCount = model.getRowCount();
        if (rowCount == 0) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "No pending prescriptions. Use the 'Add' button to stage medications first.",
                    "Info", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        model.setRowCount(0);
        javax.swing.JOptionPane.showMessageDialog(this,
                "Prescription session complete — " + rowCount + " medication(s) were saved.",
                "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void btnAddPrescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        String appointmentId  = (String) cmbPrescribeAppt.getSelectedItem();
        if (appointmentId == null || "Select one".equals(appointmentId)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select an appointment.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        String medicationName = txtMedName.getText().trim();
        String doseStr        = txtMedDose.getText().trim();
        String route          = txtMedRoute.getText().trim();
        String durationStr    = txtMedDuration.getText().trim();
        String instructions   = txtMedInstructions.getText().trim();
        String frequencyStr   = txtMedFrequency.getText().trim();

        // Delegate to AppointmentController (enforces PENDING-only rule)
        AppointmentController apptController = new AppointmentController();
        Response response = apptController.prescribe(
                appointmentId, medicationName, doseStr, route, durationStr, instructions, frequencyStr);

        if (!response.isSuccess()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "[" + response.getStatus() + "] " + response.getMessage(),
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Reflect in staging table
        DefaultTableModel model = (DefaultTableModel) tblPrescriptions.getModel();
        model.addRow(new Object[]{appointmentId, medicationName, doseStr, route, durationStr, instructions, frequencyStr});
        // Clear fields
        txtMedName.setText(""); txtMedDose.setText(""); txtMedRoute.setText("");
        txtMedFrequency.setText(""); txtMedDuration.setText(""); txtMedInstructions.setText("");
        javax.swing.JOptionPane.showMessageDialog(this,
                response.getMessage(), "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void btnRescheduleAppointmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        String appointmentId = (String) cmbRescheduleAppt.getSelectedItem();
        if (appointmentId == null || "Select one".equals(appointmentId)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select an appointment to reschedule.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        // txtRescheduleTime contains "HH:mm" (new time); txtRescheduleReason contains reason
        // For reschedule, we keep the original date and only change the time.
        String newTime = txtRescheduleTime.getText().trim();
        String reason  = txtRescheduleReason.getText().trim();

        // Get current appointment date via controller (no direct model access in view)
        AppointmentController apptController = new AppointmentController();
        Response apptResp = apptController.getAppointmentResponse(appointmentId);
        if (!apptResp.isSuccess()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Appointment not found.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        String existingDate = apptResp.getData().getJSONObject("appointment").optString("date");

        // Delegate all validation (minutes, future, availability) to controller
        Response response = apptController.rescheduleAppointment(
                appointmentId, existingDate, newTime, reason);

        if (!response.isSuccess()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "[" + response.getStatus() + "] " + response.getMessage(),
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        txtRescheduleTime.setText(""); txtRescheduleReason.setText("");
        refreshAppointmentView();
        javax.swing.JOptionPane.showMessageDialog(this,
                response.getMessage(), "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButton4ActionPerformed




    /** Pre-rellena los campos de "Modify Info" con los datos actuales del doctor. */
    private void prefillDoctorInfo() {
        if (doctor == null) return;
        txtProfileFirstname.setText(doctor.getFirstname());
        txtProfileLastname.setText(doctor.getLastname());
        txtProfileLicence.setText(doctor.getLicenceNumber() != null ? doctor.getLicenceNumber() : "");
        txtProfileOffice.setText(doctor.getAssignedOffice() != null ? doctor.getAssignedOffice() : "");
        txtProfileUsername.setText(doctor.getUsername());
        // Seleccionar la specialty actual en el combobox
        if (doctor.getSpecialty() != null) {
            String displayName = specialtyToDisplay(doctor.getSpecialty());
            cmbProfileSpecialty.setSelectedItem(displayName);
        }
    }

    /**
     * Convierte un Specialty enum a nombre legible para mostrar en la UI.
     */
    private String specialtyToDisplay(Specialty spec) {
        switch (spec) {
            case GENERAL_MEDICINE:          return "General Medicine";
            case CARDIOLOGY:                return "Cardiology";
            case PEDIATRICS:                return "Pediatrics";
            case NEUROLOGY:                 return "Neurology";
            case TRAUMATOLOGY_ORTHOPEDICS:  return "Traumatology & Orthopedics";
            case GYNECOLOGY_OBSTETRICS:     return "Gynecology & Obstetrics";
            case DERMATOLOGY:               return "Dermatology";
            case PSYCHIATRY:                return "Psychiatry";
            case ONCOLOGY:                  return "Oncology";
            case OPHTHALMOLOGY:             return "Ophthalmology";
            case INTERNAL_MEDICINE:         return "Internal Medicine";
            default:                        return spec.name();
        }
    }

    /**
     * Convierte el texto del combobox de specialty al enum Specialty.
     * Formato entrada: "General Medicine", "Traumatology & Orthopedics", etc.
     */
    private Specialty parseSpecialty(String displayName) {
        if (displayName == null || "Select one".equals(displayName)) return null;
        try {
            String enumName = displayName.toUpperCase()
                .replaceAll(" & ", "_")
                .replaceAll("&", "_")
                .replaceAll(" ", "_");
            return Specialty.valueOf(enumName);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnFinalizePrescriptions;
    private javax.swing.JButton btnBackToAdmin;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnCancelHospitalization;
    private javax.swing.JButton btnAcceptAppointment;
    private javax.swing.JButton btnRescheduleAppointment;
    private javax.swing.JButton btnCompleteAppointment;
    private javax.swing.JButton btnRequestHospitalization;
    private javax.swing.JButton btnAddPrescription;
    private javax.swing.JButton btnSearchPatientHistory;
    private javax.swing.JButton btnUpdateProfile;
    private javax.swing.JComboBox<String> cmbProfileSpecialty;
    private javax.swing.JComboBox<String> cmbAcceptAppt;
    private javax.swing.JComboBox<String> cmbRescheduleAppt;
    private javax.swing.JComboBox<String> cmbCompleteAppt;
    private javax.swing.JComboBox<String> cmbPatientSearch;
    private javax.swing.JComboBox<String> cmbHospCancel;
    private javax.swing.JComboBox<String> cmbPrescribeAppt;
    private javax.swing.JComboBox<String> cmbPatientHosp;
    private javax.swing.JLabel lblViewTitle;
    private javax.swing.JLabel lblPasswordLbl;
    private javax.swing.JLabel lblConfirmPwdLbl;
    private javax.swing.JLabel lblAcceptApptLbl;
    private javax.swing.JLabel lblPatientSearchLbl;
    private javax.swing.JLabel lblRescheduleApptLbl;
    private javax.swing.JLabel lblRescheduleReasonLbl;
    private javax.swing.JLabel lblRescheduleTimeLbl;
    private javax.swing.JLabel lblDoc18;
    private javax.swing.JLabel lblDiagnosisLbl;
    private javax.swing.JLabel lblDoc02;
    private javax.swing.JLabel lblObservationsLbl;
    private javax.swing.JLabel lblTreatmentLbl;
    private javax.swing.JLabel lblFollowUpLbl;
    private javax.swing.JLabel lblCompleteApptLbl;
    private javax.swing.JLabel lblDoc24;
    private javax.swing.JLabel lblHospPatientLbl;
    private javax.swing.JLabel lblHospObsLbl;
    private javax.swing.JLabel lblHospEntryDateLbl;
    private javax.swing.JLabel lblHospEstDurLbl;
    private javax.swing.JLabel lblFirstnameLbl;
    private javax.swing.JLabel lblDoc30;
    private javax.swing.JLabel lblPrescribeApptLbl;
    private javax.swing.JLabel lblMedNameLbl;
    private javax.swing.JLabel lblMedDoseLbl;
    private javax.swing.JLabel lblMedRouteLbl;
    private javax.swing.JLabel lblMedFreqLbl;
    private javax.swing.JLabel lblMedDurationLbl;
    private javax.swing.JLabel lblDoc37;
    private javax.swing.JLabel lblDoc38;
    private javax.swing.JLabel lblLastnameLbl;
    private javax.swing.JLabel lblSpecialtyLbl;
    private javax.swing.JLabel lblLicenceLbl;
    private javax.swing.JLabel lblOfficeLbl;
    private javax.swing.JPanel panelAppointments;
    private javax.swing.JPanel panelMedications;
    private javax.swing.JPanel panelPatientHistory;
    private javax.swing.JPanel panelHospitalize;
    private javax.swing.JPanel panelPrescribe;
    private javax.swing.JRadioButton rbtnShowAllAppts;
    private javax.swing.JRadioButton rbtnPendingOnly;
    private javax.swing.JRadioButton rbtnInPersonAppt;
    private javax.swing.JRadioButton rbtnVirtualAppt;
    private javax.swing.JScrollPane scrollPrescriptionsSaved;
    private javax.swing.JScrollPane scrollPatientHistory;
    private javax.swing.JScrollPane scrollPrescriptionStaging;
    private javax.swing.JScrollPane scrollHospObservations;
    private javax.swing.JScrollPane scrollDiagnosis;
    private javax.swing.JScrollPane scrollObservations;
    private javax.swing.JScrollPane scrollTreatment;
    private javax.swing.JScrollPane scrollFollowUp;
    private javax.swing.JScrollPane scrollHospReason;
    private javax.swing.JSeparator sepHeader;
    private javax.swing.JSeparator sepContent;
    private javax.swing.JSeparator sepProfileSection;
    private javax.swing.JTabbedPane tabbedPaneDoctor;
    private javax.swing.JTable tblPrescriptions;
    private javax.swing.JTable tblAppointments;
    private javax.swing.JTable tblPatientHistory;
    private javax.swing.JTextArea txtaHospObservations;
    private javax.swing.JTextArea txtaDiagnosis;
    private javax.swing.JTextArea txtaObservations;
    private javax.swing.JTextArea txtaTreatment;
    private javax.swing.JTextArea txtaFollowUp;
    private javax.swing.JTextArea txtaHospReason;
    private javax.swing.JTextField txtProfileFirstname;
    private javax.swing.JTextField txtProfileConfirmPwd;
    private javax.swing.JTextField txtRescheduleTime;
    private javax.swing.JTextField txtRescheduleReason;
    private javax.swing.JTextField txtProfileLastname;
    private javax.swing.JTextField txtHospEntryDate;
    private javax.swing.JTextField txtHospEstDuration;
    private javax.swing.JTextField txtMedName;
    private javax.swing.JTextField txtMedDose;
    private javax.swing.JTextField txtMedRoute;
    private javax.swing.JTextField txtMedFrequency;
    private javax.swing.JTextField txtMedDuration;
    private javax.swing.JTextField txtMedInstructions;
    private javax.swing.JTextField txtProfileLicence;
    private javax.swing.JTextField txtProfileUsername;
    private javax.swing.JTextField txtProfileOffice;
    private javax.swing.JTextField txtProfilePassword;
    private core.controllers.PanelRound panelRound1;
    private core.controllers.PanelRound panelRound2;
    // End of variables declaration//GEN-END:variables
}
