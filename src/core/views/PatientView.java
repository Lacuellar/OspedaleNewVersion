/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package core.views;

import core.views.AdminView;
import core.controllers.AppointmentController;
import core.controllers.HospitalizationController;
import core.controllers.PatientController;
import core.models.Response;
import java.awt.Color;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import core.models.Administrator;
import core.models.Appointment;
import core.models.AppointmentStatus;
import core.models.Doctor;
import core.models.Hospitalization;
import core.models.Patient;
import core.models.RoomType;
import core.models.Specialty;
import core.models.User;
import core.models.DataStore;

/**
 *
 * @author jjlora
 * @author edangulo
 */
public class PatientView extends javax.swing.JFrame {

    private int x, y;
    private User user;
    private ArrayList<User> users;
    private Patient patient;
    private ArrayList<Appointment> appointments;
    private ArrayList<Hospitalization> hospitalizations;

    public PatientView(User user,Patient patient, ArrayList<User> users, ArrayList<Appointment>appointments, ArrayList<Hospitalization> hospitalizations) {
        initComponents();
        this.user = user;
        this.users = users;
        this.patient = patient;
        this.hospitalizations = hospitalizations;
        this.appointments = appointments;
        if (user instanceof Administrator) {
            PatientView_Back_Button.setVisible(true);
        } else {
            PatientView_Back_Button.setVisible(false);
        }
        this.setBackground(new Color(0, 0, 0, 0));
        this.setLocationRelativeTo(null);
        // Mostrar nombre del paciente en la barra de título
        this.setTitle("Ospedale — " + patient.getFirstname() + " " + patient.getLastname());
        PatientView_Label.setText("PATIENT VIEW — " + patient.getFirstname() + " " + patient.getLastname());
        // Hints de formato en labels
        ReqCan_ReqMedApp_AppDate_Label.setText("Appointment date (YYYY-MM-DD)");
        ReqCan_ReqMedApp_AppTime_Label.setText("Appointment time (HH:mm)");
        ReqCan_Hosp_EstDateAdmission_Label.setText("Estimated admission date (YYYY-MM-DD)");
        PatientView_ModInfo_Birthday_Label.setText("Birthdate (YYYY-MM-DD)");
        // Poblar dropdowns
        populateDoctorDropdown();
        populateRoomTypeDropdown();
        populateCancelAppointmentDropdown();
        // Pre-rellenar campos de Modify Info con datos actuales
        prefillPatientInfo();
        // Cargar historial de citas automáticamente
        PatientView_Refresh_ButtonActionPerformed(null);
    }

    private void populateDoctorDropdown() {
        ReqCan_Hosp_AttendingDoctor_Dropdown.removeAllItems();
        ReqCan_Hosp_AttendingDoctor_Dropdown.addItem("Select one");
        for (User u : this.users) {
            if (u instanceof Doctor) {
                ReqCan_Hosp_AttendingDoctor_Dropdown.addItem(u.getId() + " — " + u.getFirstname() + " " + u.getLastname());
            }
        }
    }

    /** Extrae el ID numérico del formato "id — Name" de los comboboxes. */
    private long extractId(String item) {
        if (item == null || item.contains("Select")) throw new NumberFormatException("No selection");
        return Long.parseLong(item.split(" — ")[0].trim());
    }

    private void populateRoomTypeDropdown() {
        ReqCan_Hosp_DesiredRoomType_Dropdown.removeAllItems();
        for (RoomType rt : RoomType.values()) {
            ReqCan_Hosp_DesiredRoomType_Dropdown.addItem(rt.name());
        }
    }

    private void prefillPatientInfo() {
        if (patient == null) return;
        PatientView_ModInfo_Firstname_Field.setText(patient.getFirstname());
        PatientView_ModInfo_Lastname_Field.setText(patient.getLastname());
        PatientView_ModInfo_email_Field.setText(patient.getEmail() != null ? patient.getEmail() : "");
        PatientView_ModInfo_Address_Field.setText(patient.getAddress() != null ? patient.getAddress() : "");
        if (patient.getPhone() != 0) PatientView_ModInfo_Phone_Field.setText(String.valueOf(patient.getPhone()));
        if (patient.getBirthdate() != null) PatientView_ModInfo_Birthday_Field.setText(patient.getBirthdate().toString());
        PatientView_ModInfo_EnterUser_Field.setText(patient.getUsername());
        // 0=Select one, 1=Female, 2=Male
        PatientView_ModInfo_Gender_Dropdown.setSelectedIndex(patient.getGender() ? 2 : 1);
    }

    private void populateCancelAppointmentDropdown() {
        ReqCan_CancelAppointment_IDApp_Dropdown.removeAllItems();
        ReqCan_CancelAppointment_IDApp_Dropdown.addItem("Select one");
        for (Appointment a : patient.getAppointments()) {
            ReqCan_CancelAppointment_IDApp_Dropdown.addItem(a.getId());
        }
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
        PatientView_Close_Button = new javax.swing.JButton();
        PatientView_Label = new javax.swing.JLabel();
        PatientView_Back_Button = new javax.swing.JButton();
        PatientView_Tab = new javax.swing.JTabbedPane();
        PatientView_AppointmentHist_Tab = new javax.swing.JPanel();
        PatientView_Appointments_Details_Panel = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        PatientView_Refresh_Button = new javax.swing.JButton();
        PatientView_LogOut_Button = new javax.swing.JButton();
        PatientView_ModifyInfo_Tab = new javax.swing.JPanel();
        PatientView_ModInfo_Fistname_Label = new javax.swing.JLabel();
        PatientView_ModInfo_Firstname_Field = new javax.swing.JTextField();
        PatientView_ModInfo_Lastname_Label = new javax.swing.JLabel();
        PatientView_ModInfo_Lastname_Field = new javax.swing.JTextField();
        PatientView_ModInfo_Birthday_Label = new javax.swing.JLabel();
        PatientView_ModInfo_Birthday_Field = new javax.swing.JTextField();
        PatientView_ModInfo_Gender_Label = new javax.swing.JLabel();
        PatientView_ModInfo_email_Label = new javax.swing.JLabel();
        PatientView_ModInfo_email_Field = new javax.swing.JTextField();
        PatientView_ModInfo_Phone_Label = new javax.swing.JLabel();
        PatientView_ModInfo_Phone_Field = new javax.swing.JTextField();
        PatientView_ModInfo_Address_Label = new javax.swing.JLabel();
        PatientView_ModInfo_Address_Field = new javax.swing.JTextField();
        PatientView_ModInfo_EnterPassword_Field = new javax.swing.JTextField();
        PatientView_ModInfo_Password_Label = new javax.swing.JLabel();
        PatientView_ModInfo_PasswordConfirm_Label = new javax.swing.JLabel();
        PatientView_ModInfo_EnterPasswordConf_Field = new javax.swing.JTextField();
        PatientView_ModInfo_SAVE_Button = new javax.swing.JButton();
        PatientView_ModInfo_User_Label = new javax.swing.JLabel();
        PatientView_ModInfo_EnterUser_Field = new javax.swing.JTextField();
        PatientView_ModInfo_Gender_Dropdown = new javax.swing.JComboBox<>();
        PatientView_ReqCan_Tab = new javax.swing.JPanel();
        ReqCan_ReqMedAppointment_Label = new javax.swing.JLabel();
        ReqCan_ReqMedApp_Specialty_Button = new javax.swing.JRadioButton();
        ReqCan_ReqMedApp_Specialty_ButtonReqCan_ReqMedApp_Doctor_Button = new javax.swing.JRadioButton();
        ReqCan_Separator = new javax.swing.JSeparator();
        ReqCan_ReqMedApp_AppDate_Label = new javax.swing.JLabel();
        ReqCan_ReqMedApp_AppDate_Field = new javax.swing.JTextField();
        ReqCan_ReqMedApp_AppTime_Field = new javax.swing.JTextField();
        ReqCan_ReqMedApp_AppTime_Label = new javax.swing.JLabel();
        ReqCan_ReqMedApp_AppType_Label = new javax.swing.JLabel();
        ReqCan_ReqMedApp_AppReason_Label = new javax.swing.JLabel();
        ReqCan_ReqMedApp_AppType_Dropdown = new javax.swing.JComboBox<>();
        ReqCan_ReqMedApp_Create_Button = new javax.swing.JButton();
        ReqHosp_CanApp_Separator = new javax.swing.JSeparator();
        ReqCan_ReqHospitalization_Label = new javax.swing.JLabel();
        ReqCan_ReqHospReason_Label = new javax.swing.JLabel();
        ReqCan_Hosp_AttendingDoctor_Label = new javax.swing.JLabel();
        ReqCan_Hosp_AttendingDoctor_Dropdown = new javax.swing.JComboBox<>();
        ReqCan_Hosp_EstDateAdmission_Field = new javax.swing.JTextField();
        ReqCan_Hosp_EstDateAdmission_Label = new javax.swing.JLabel();
        ReqCan_Hosp_DesiredRoomType_Label = new javax.swing.JLabel();
        ReqCan_Hosp_DesiredRoomType_Dropdown = new javax.swing.JComboBox<>();
        ReqCan_Hosp_Observations_Label = new javax.swing.JLabel();
        PatientView_ReqCan_ReqHospObservationField_Scroll = new javax.swing.JScrollPane();
        ReqCan_Hosp_Observations_Field = new javax.swing.JTextArea();
        ReqCan_ReqHosp_Create_Button = new javax.swing.JButton();
        ReqCan_CancelAppointment_Label = new javax.swing.JLabel();
        ReqCan_CancelAppointment_IDApp_Label = new javax.swing.JLabel();
        ReqCan_CancelAppointment_Observations_Label = new javax.swing.JLabel();
        PatientView_ReqCan_CanAppObservationField_Scroll = new javax.swing.JScrollPane();
        ReqCan_CancelAppointment_Observations_Field = new javax.swing.JTextArea();
        ReqCan_CancelAppointment_Cancel_Button = new javax.swing.JButton();
        PatientView_ReqCan_ReqHosp_HospReasonField_Scroll = new javax.swing.JScrollPane();
        ReqCan_ReqHospReason_Field = new javax.swing.JTextArea();
        ReqCan_ReqMedApp_AppReason_Label_Scroll = new javax.swing.JScrollPane();
        ReqCan_ReqMedApp_AppReason_Field = new javax.swing.JTextArea();
        ReqCan_CancelAppointment_IDApp_Dropdown = new javax.swing.JComboBox<>();
        ReqCan_AppType_Dropdown = new javax.swing.JComboBox<>();

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

        PatientView_Close_Button.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        PatientView_Close_Button.setText("X");
        PatientView_Close_Button.setBorderPainted(false);
        PatientView_Close_Button.setContentAreaFilled(false);
        PatientView_Close_Button.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        PatientView_Close_Button.setFocusable(false);
        PatientView_Close_Button.setRequestFocusEnabled(false);
        PatientView_Close_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PatientView_Close_ButtonActionPerformed(evt);
            }
        });

        PatientView_Label.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        PatientView_Label.setText("PATIENT VIEW");

        PatientView_Back_Button.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        PatientView_Back_Button.setText("Back");
        PatientView_Back_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PatientView_Back_ButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelRound2Layout = new javax.swing.GroupLayout(panelRound2);
        panelRound2.setLayout(panelRound2Layout);
        panelRound2Layout.setHorizontalGroup(
            panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRound2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(PatientView_Label)
                .addGap(29, 29, 29)
                .addComponent(PatientView_Back_Button)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(PatientView_Close_Button)
                .addGap(19, 19, 19))
        );
        panelRound2Layout.setVerticalGroup(
            panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRound2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(PatientView_Close_Button))
            .addGroup(panelRound2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PatientView_Back_Button)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(PatientView_Label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        PatientView_Appointments_Details_Panel.setViewportView(jTable1);

        PatientView_Refresh_Button.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        PatientView_Refresh_Button.setText("Refresh");
        PatientView_Refresh_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PatientView_Refresh_ButtonActionPerformed(evt);
            }
        });

        PatientView_LogOut_Button.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        PatientView_LogOut_Button.setText("Logout");
        PatientView_LogOut_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PatientView_LogOut_ButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PatientView_AppointmentHist_TabLayout = new javax.swing.GroupLayout(PatientView_AppointmentHist_Tab);
        PatientView_AppointmentHist_Tab.setLayout(PatientView_AppointmentHist_TabLayout);
        PatientView_AppointmentHist_TabLayout.setHorizontalGroup(
            PatientView_AppointmentHist_TabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PatientView_AppointmentHist_TabLayout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(PatientView_Appointments_Details_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, 1167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(51, Short.MAX_VALUE))
            .addGroup(PatientView_AppointmentHist_TabLayout.createSequentialGroup()
                .addGap(602, 602, 602)
                .addComponent(PatientView_Refresh_Button)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(PatientView_LogOut_Button)
                .addGap(78, 78, 78))
        );
        PatientView_AppointmentHist_TabLayout.setVerticalGroup(
            PatientView_AppointmentHist_TabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PatientView_AppointmentHist_TabLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(PatientView_Appointments_Details_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58)
                .addGroup(PatientView_AppointmentHist_TabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PatientView_Refresh_Button)
                    .addComponent(PatientView_LogOut_Button))
                .addContainerGap(71, Short.MAX_VALUE))
        );

        PatientView_Tab.addTab("Appointment history", PatientView_AppointmentHist_Tab);

        PatientView_ModInfo_Fistname_Label.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        PatientView_ModInfo_Fistname_Label.setText("Firstname");

        PatientView_ModInfo_Firstname_Field.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        PatientView_ModInfo_Lastname_Label.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        PatientView_ModInfo_Lastname_Label.setText("Lastname");

        PatientView_ModInfo_Lastname_Field.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        PatientView_ModInfo_Birthday_Label.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        PatientView_ModInfo_Birthday_Label.setText("Birthdate");

        PatientView_ModInfo_Birthday_Field.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        PatientView_ModInfo_Gender_Label.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        PatientView_ModInfo_Gender_Label.setText("Gender");

        PatientView_ModInfo_email_Label.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        PatientView_ModInfo_email_Label.setText("Email");

        PatientView_ModInfo_email_Field.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        PatientView_ModInfo_Phone_Label.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        PatientView_ModInfo_Phone_Label.setText("Phone");

        PatientView_ModInfo_Phone_Field.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        PatientView_ModInfo_Address_Label.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        PatientView_ModInfo_Address_Label.setText("Address");

        PatientView_ModInfo_Address_Field.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        PatientView_ModInfo_EnterPassword_Field.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        PatientView_ModInfo_Password_Label.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        PatientView_ModInfo_Password_Label.setText("Password");

        PatientView_ModInfo_PasswordConfirm_Label.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        PatientView_ModInfo_PasswordConfirm_Label.setText("Password confirmation");

        PatientView_ModInfo_EnterPasswordConf_Field.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        PatientView_ModInfo_SAVE_Button.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        PatientView_ModInfo_SAVE_Button.setText("Save");
        PatientView_ModInfo_SAVE_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PatientView_ModInfo_SAVE_ButtonActionPerformed(evt);
            }
        });

        PatientView_ModInfo_User_Label.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        PatientView_ModInfo_User_Label.setText("User");

        PatientView_ModInfo_EnterUser_Field.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        PatientView_ModInfo_Gender_Dropdown.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        PatientView_ModInfo_Gender_Dropdown.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one", "Female", "Male" }));

        javax.swing.GroupLayout PatientView_ModifyInfo_TabLayout = new javax.swing.GroupLayout(PatientView_ModifyInfo_Tab);
        PatientView_ModifyInfo_Tab.setLayout(PatientView_ModifyInfo_TabLayout);
        PatientView_ModifyInfo_TabLayout.setHorizontalGroup(
            PatientView_ModifyInfo_TabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PatientView_ModifyInfo_TabLayout.createSequentialGroup()
                .addGap(81, 81, 81)
                .addComponent(PatientView_ModInfo_Fistname_Label)
                .addGap(18, 18, 18)
                .addComponent(PatientView_ModInfo_Firstname_Field, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(PatientView_ModInfo_Lastname_Label)
                .addGap(18, 18, 18)
                .addGroup(PatientView_ModifyInfo_TabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PatientView_ModifyInfo_TabLayout.createSequentialGroup()
                        .addComponent(PatientView_ModInfo_Phone_Label)
                        .addGap(18, 18, 18)
                        .addComponent(PatientView_ModInfo_Phone_Field, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(PatientView_ModInfo_Address_Label)
                        .addGap(18, 18, 18)
                        .addComponent(PatientView_ModInfo_Address_Field, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(PatientView_ModifyInfo_TabLayout.createSequentialGroup()
                        .addComponent(PatientView_ModInfo_Lastname_Field, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(PatientView_ModInfo_Birthday_Label)
                        .addGap(18, 18, 18)
                        .addComponent(PatientView_ModInfo_Birthday_Field, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(PatientView_ModInfo_Gender_Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PatientView_ModInfo_Gender_Dropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(PatientView_ModInfo_email_Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addComponent(PatientView_ModInfo_email_Field, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(141, 141, 141))
            .addGroup(PatientView_ModifyInfo_TabLayout.createSequentialGroup()
                .addGap(516, 516, 516)
                .addGroup(PatientView_ModifyInfo_TabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PatientView_ModifyInfo_TabLayout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(PatientView_ModInfo_SAVE_Button))
                    .addGroup(PatientView_ModifyInfo_TabLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(PatientView_ModInfo_EnterPasswordConf_Field, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(PatientView_ModInfo_PasswordConfirm_Label)
                    .addGroup(PatientView_ModifyInfo_TabLayout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(PatientView_ModInfo_Password_Label))
                    .addGroup(PatientView_ModifyInfo_TabLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(PatientView_ModifyInfo_TabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(PatientView_ModifyInfo_TabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(PatientView_ModInfo_EnterUser_Field, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(PatientView_ModifyInfo_TabLayout.createSequentialGroup()
                                    .addGap(39, 39, 39)
                                    .addComponent(PatientView_ModInfo_User_Label)))
                            .addComponent(PatientView_ModInfo_EnterPassword_Field, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PatientView_ModifyInfo_TabLayout.setVerticalGroup(
            PatientView_ModifyInfo_TabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PatientView_ModifyInfo_TabLayout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addGroup(PatientView_ModifyInfo_TabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PatientView_ModInfo_Fistname_Label)
                    .addComponent(PatientView_ModInfo_Firstname_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PatientView_ModInfo_Lastname_Label)
                    .addComponent(PatientView_ModInfo_Lastname_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PatientView_ModInfo_Birthday_Label)
                    .addComponent(PatientView_ModInfo_Birthday_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PatientView_ModInfo_Gender_Label)
                    .addComponent(PatientView_ModInfo_email_Label)
                    .addComponent(PatientView_ModInfo_email_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PatientView_ModInfo_Gender_Dropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PatientView_ModifyInfo_TabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PatientView_ModInfo_Phone_Label)
                    .addComponent(PatientView_ModInfo_Phone_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PatientView_ModInfo_Address_Label)
                    .addComponent(PatientView_ModInfo_Address_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(66, 66, 66)
                .addComponent(PatientView_ModInfo_User_Label)
                .addGap(18, 18, 18)
                .addComponent(PatientView_ModInfo_EnterUser_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(PatientView_ModInfo_Password_Label)
                .addGap(18, 18, 18)
                .addComponent(PatientView_ModInfo_EnterPassword_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(PatientView_ModInfo_PasswordConfirm_Label)
                .addGap(18, 18, 18)
                .addComponent(PatientView_ModInfo_EnterPasswordConf_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(PatientView_ModInfo_SAVE_Button)
                .addContainerGap(68, Short.MAX_VALUE))
        );

        PatientView_Tab.addTab("Modify info", PatientView_ModifyInfo_Tab);

        ReqCan_ReqMedAppointment_Label.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        ReqCan_ReqMedAppointment_Label.setText("Request medical appointment");

        ReqCan_ReqMedApp_Specialty_Button.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        ReqCan_ReqMedApp_Specialty_Button.setText("Specialty");
        ReqCan_ReqMedApp_Specialty_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReqCan_ReqMedApp_Specialty_ButtonActionPerformed(evt);
            }
        });

        ReqCan_ReqMedApp_Specialty_ButtonReqCan_ReqMedApp_Doctor_Button.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        ReqCan_ReqMedApp_Specialty_ButtonReqCan_ReqMedApp_Doctor_Button.setText("Doctor");
        ReqCan_ReqMedApp_Specialty_ButtonReqCan_ReqMedApp_Doctor_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReqCan_ReqMedApp_Specialty_ButtonReqCan_ReqMedApp_Doctor_ButtonActionPerformed(evt);
            }
        });

        ReqCan_Separator.setOrientation(javax.swing.SwingConstants.VERTICAL);

        ReqCan_ReqMedApp_AppDate_Label.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        ReqCan_ReqMedApp_AppDate_Label.setText("Appointment date");

        ReqCan_ReqMedApp_AppDate_Field.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        ReqCan_ReqMedApp_AppTime_Field.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        ReqCan_ReqMedApp_AppTime_Label.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        ReqCan_ReqMedApp_AppTime_Label.setText("Appointment time");

        ReqCan_ReqMedApp_AppType_Label.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        ReqCan_ReqMedApp_AppType_Label.setText("Appointment type");

        ReqCan_ReqMedApp_AppReason_Label.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        ReqCan_ReqMedApp_AppReason_Label.setText("Appointment reason");

        ReqCan_ReqMedApp_AppType_Dropdown.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        ReqCan_ReqMedApp_AppType_Dropdown.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one", "Remote", "In-person" }));

        ReqCan_ReqMedApp_Create_Button.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        ReqCan_ReqMedApp_Create_Button.setText("Create");
        ReqCan_ReqMedApp_Create_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReqCan_ReqMedApp_Create_ButtonActionPerformed(evt);
            }
        });

        ReqHosp_CanApp_Separator.setOrientation(javax.swing.SwingConstants.VERTICAL);

        ReqCan_ReqHospitalization_Label.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        ReqCan_ReqHospitalization_Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ReqCan_ReqHospitalization_Label.setText("Request hospitalization");

        ReqCan_ReqHospReason_Label.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        ReqCan_ReqHospReason_Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ReqCan_ReqHospReason_Label.setText("Hospitalization reason");

        ReqCan_Hosp_AttendingDoctor_Label.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        ReqCan_Hosp_AttendingDoctor_Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ReqCan_Hosp_AttendingDoctor_Label.setText("Attending doctor");

        ReqCan_Hosp_AttendingDoctor_Dropdown.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        ReqCan_Hosp_AttendingDoctor_Dropdown.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one" }));

        ReqCan_Hosp_EstDateAdmission_Field.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        ReqCan_Hosp_EstDateAdmission_Label.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        ReqCan_Hosp_EstDateAdmission_Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ReqCan_Hosp_EstDateAdmission_Label.setText("Estimated date of admission");
        ReqCan_Hosp_EstDateAdmission_Label.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        ReqCan_Hosp_DesiredRoomType_Label.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        ReqCan_Hosp_DesiredRoomType_Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ReqCan_Hosp_DesiredRoomType_Label.setText("Desired room type");

        ReqCan_Hosp_DesiredRoomType_Dropdown.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        ReqCan_Hosp_DesiredRoomType_Dropdown.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one" }));

        ReqCan_Hosp_Observations_Label.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        ReqCan_Hosp_Observations_Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ReqCan_Hosp_Observations_Label.setText("Observations");

        ReqCan_Hosp_Observations_Field.setColumns(20);
        ReqCan_Hosp_Observations_Field.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        ReqCan_Hosp_Observations_Field.setRows(5);
        PatientView_ReqCan_ReqHospObservationField_Scroll.setViewportView(ReqCan_Hosp_Observations_Field);

        ReqCan_ReqHosp_Create_Button.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        ReqCan_ReqHosp_Create_Button.setText("Create");
        ReqCan_ReqHosp_Create_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReqCan_ReqHosp_Create_ButtonActionPerformed(evt);
            }
        });

        ReqCan_CancelAppointment_Label.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        ReqCan_CancelAppointment_Label.setText("Cancel appointment");

        ReqCan_CancelAppointment_IDApp_Label.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        ReqCan_CancelAppointment_IDApp_Label.setText("ID appointment");

        ReqCan_CancelAppointment_Observations_Label.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        ReqCan_CancelAppointment_Observations_Label.setText("Observations");

        ReqCan_CancelAppointment_Observations_Field.setColumns(20);
        ReqCan_CancelAppointment_Observations_Field.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        ReqCan_CancelAppointment_Observations_Field.setRows(5);
        PatientView_ReqCan_CanAppObservationField_Scroll.setViewportView(ReqCan_CancelAppointment_Observations_Field);

        ReqCan_CancelAppointment_Cancel_Button.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        ReqCan_CancelAppointment_Cancel_Button.setText("Cancel");
        ReqCan_CancelAppointment_Cancel_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReqCan_CancelAppointment_Cancel_ButtonActionPerformed(evt);
            }
        });

        ReqCan_ReqHospReason_Field.setColumns(20);
        ReqCan_ReqHospReason_Field.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        ReqCan_ReqHospReason_Field.setRows(5);
        PatientView_ReqCan_ReqHosp_HospReasonField_Scroll.setViewportView(ReqCan_ReqHospReason_Field);

        ReqCan_ReqMedApp_AppReason_Field.setColumns(20);
        ReqCan_ReqMedApp_AppReason_Field.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        ReqCan_ReqMedApp_AppReason_Field.setRows(5);
        ReqCan_ReqMedApp_AppReason_Label_Scroll.setViewportView(ReqCan_ReqMedApp_AppReason_Field);

        ReqCan_CancelAppointment_IDApp_Dropdown.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        ReqCan_CancelAppointment_IDApp_Dropdown.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one" }));

        ReqCan_AppType_Dropdown.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        ReqCan_AppType_Dropdown.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one" }));
        ReqCan_AppType_Dropdown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReqCan_AppType_DropdownActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PatientView_ReqCan_TabLayout = new javax.swing.GroupLayout(PatientView_ReqCan_Tab);
        PatientView_ReqCan_Tab.setLayout(PatientView_ReqCan_TabLayout);
        PatientView_ReqCan_TabLayout.setHorizontalGroup(
            PatientView_ReqCan_TabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PatientView_ReqCan_TabLayout.createSequentialGroup()
                .addGroup(PatientView_ReqCan_TabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PatientView_ReqCan_TabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(PatientView_ReqCan_TabLayout.createSequentialGroup()
                            .addGap(44, 44, 44)
                            .addGroup(PatientView_ReqCan_TabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(PatientView_ReqCan_TabLayout.createSequentialGroup()
                                    .addComponent(ReqCan_ReqMedApp_Specialty_Button)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(ReqCan_ReqMedApp_Specialty_ButtonReqCan_ReqMedApp_Doctor_Button))
                                .addGroup(PatientView_ReqCan_TabLayout.createSequentialGroup()
                                    .addGap(63, 63, 63)
                                    .addComponent(ReqCan_ReqMedApp_AppDate_Field, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(PatientView_ReqCan_TabLayout.createSequentialGroup()
                                    .addGap(47, 47, 47)
                                    .addGroup(PatientView_ReqCan_TabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(ReqCan_ReqMedApp_AppTime_Label)
                                        .addComponent(ReqCan_ReqMedApp_AppDate_Label)
                                        .addComponent(ReqCan_AppType_Dropdown, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(PatientView_ReqCan_TabLayout.createSequentialGroup()
                                    .addGap(63, 63, 63)
                                    .addComponent(ReqCan_ReqMedApp_AppTime_Field, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(PatientView_ReqCan_TabLayout.createSequentialGroup()
                                    .addGap(38, 38, 38)
                                    .addComponent(ReqCan_ReqMedApp_AppReason_Label))
                                .addGroup(PatientView_ReqCan_TabLayout.createSequentialGroup()
                                    .addGap(46, 46, 46)
                                    .addComponent(ReqCan_ReqMedApp_AppType_Label))
                                .addGroup(PatientView_ReqCan_TabLayout.createSequentialGroup()
                                    .addGap(55, 55, 55)
                                    .addComponent(ReqCan_ReqMedApp_AppType_Dropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(PatientView_ReqCan_TabLayout.createSequentialGroup()
                            .addGap(42, 42, 42)
                            .addComponent(ReqCan_ReqMedAppointment_Label)))
                    .addGroup(PatientView_ReqCan_TabLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(ReqCan_ReqMedApp_AppReason_Label_Scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PatientView_ReqCan_TabLayout.createSequentialGroup()
                        .addGap(122, 122, 122)
                        .addComponent(ReqCan_ReqMedApp_Create_Button)))
                .addGap(69, 69, 69)
                .addComponent(ReqCan_Separator, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(PatientView_ReqCan_TabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PatientView_ReqCan_TabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(PatientView_ReqCan_TabLayout.createSequentialGroup()
                            .addGap(211, 211, 211)
                            .addComponent(ReqCan_ReqHosp_Create_Button))
                        .addGroup(PatientView_ReqCan_TabLayout.createSequentialGroup()
                            .addGap(127, 127, 127)
                            .addGroup(PatientView_ReqCan_TabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(ReqCan_ReqHospReason_Label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(PatientView_ReqCan_ReqHosp_HospReasonField_Scroll, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(ReqCan_ReqHospitalization_Label, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                                .addComponent(ReqCan_Hosp_AttendingDoctor_Label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PatientView_ReqCan_TabLayout.createSequentialGroup()
                            .addGap(127, 127, 127)
                            .addGroup(PatientView_ReqCan_TabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(ReqCan_Hosp_Observations_Label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(ReqCan_Hosp_EstDateAdmission_Label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(PatientView_ReqCan_ReqHospObservationField_Scroll, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(ReqCan_Hosp_DesiredRoomType_Label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(PatientView_ReqCan_TabLayout.createSequentialGroup()
                        .addGap(190, 190, 190)
                        .addComponent(ReqCan_Hosp_AttendingDoctor_Dropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PatientView_ReqCan_TabLayout.createSequentialGroup()
                        .addGap(200, 200, 200)
                        .addComponent(ReqCan_Hosp_EstDateAdmission_Field, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PatientView_ReqCan_TabLayout.createSequentialGroup()
                        .addGap(191, 191, 191)
                        .addComponent(ReqCan_Hosp_DesiredRoomType_Dropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 126, Short.MAX_VALUE)
                .addComponent(ReqHosp_CanApp_Separator, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63)
                .addGroup(PatientView_ReqCan_TabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PatientView_ReqCan_CanAppObservationField_Scroll, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PatientView_ReqCan_TabLayout.createSequentialGroup()
                        .addGroup(PatientView_ReqCan_TabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PatientView_ReqCan_TabLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(ReqCan_CancelAppointment_Label))
                            .addGroup(PatientView_ReqCan_TabLayout.createSequentialGroup()
                                .addGap(77, 77, 77)
                                .addComponent(ReqCan_CancelAppointment_Cancel_Button))
                            .addGroup(PatientView_ReqCan_TabLayout.createSequentialGroup()
                                .addGap(47, 47, 47)
                                .addGroup(PatientView_ReqCan_TabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(ReqCan_CancelAppointment_IDApp_Dropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ReqCan_CancelAppointment_IDApp_Label)))
                            .addGroup(PatientView_ReqCan_TabLayout.createSequentialGroup()
                                .addGap(60, 60, 60)
                                .addComponent(ReqCan_CancelAppointment_Observations_Label)))
                        .addGap(49, 49, 49)))
                .addGap(81, 81, 81))
        );
        PatientView_ReqCan_TabLayout.setVerticalGroup(
            PatientView_ReqCan_TabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ReqCan_Separator)
            .addComponent(ReqHosp_CanApp_Separator)
            .addGroup(PatientView_ReqCan_TabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PatientView_ReqCan_TabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PatientView_ReqCan_TabLayout.createSequentialGroup()
                        .addComponent(ReqCan_ReqHospitalization_Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                        .addComponent(ReqCan_ReqHospReason_Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PatientView_ReqCan_ReqHosp_HospReasonField_Scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ReqCan_Hosp_AttendingDoctor_Label)
                        .addGap(18, 18, 18)
                        .addComponent(ReqCan_Hosp_AttendingDoctor_Dropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(ReqCan_Hosp_EstDateAdmission_Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ReqCan_Hosp_EstDateAdmission_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(ReqCan_Hosp_DesiredRoomType_Label)
                        .addGap(18, 18, 18)
                        .addComponent(ReqCan_Hosp_DesiredRoomType_Dropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(ReqCan_Hosp_Observations_Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(PatientView_ReqCan_ReqHospObservationField_Scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(ReqCan_ReqHosp_Create_Button)
                        .addGap(15, 15, 15))
                    .addGroup(PatientView_ReqCan_TabLayout.createSequentialGroup()
                        .addGroup(PatientView_ReqCan_TabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PatientView_ReqCan_TabLayout.createSequentialGroup()
                                .addComponent(ReqCan_ReqMedAppointment_Label)
                                .addGap(18, 18, 18)
                                .addGroup(PatientView_ReqCan_TabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(ReqCan_ReqMedApp_Specialty_Button)
                                    .addComponent(ReqCan_ReqMedApp_Specialty_ButtonReqCan_ReqMedApp_Doctor_Button))
                                .addGap(18, 18, 18)
                                .addComponent(ReqCan_AppType_Dropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(ReqCan_ReqMedApp_AppDate_Label)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(ReqCan_ReqMedApp_AppDate_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(13, 13, 13)
                                .addComponent(ReqCan_ReqMedApp_AppTime_Label)
                                .addGap(18, 18, 18)
                                .addComponent(ReqCan_ReqMedApp_AppTime_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ReqCan_ReqMedApp_AppReason_Label)
                                .addGap(24, 24, 24)
                                .addComponent(ReqCan_ReqMedApp_AppReason_Label_Scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(PatientView_ReqCan_TabLayout.createSequentialGroup()
                                .addComponent(ReqCan_CancelAppointment_Label)
                                .addGap(39, 39, 39)
                                .addComponent(ReqCan_CancelAppointment_IDApp_Label)
                                .addGap(18, 18, 18)
                                .addComponent(ReqCan_CancelAppointment_IDApp_Dropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(ReqCan_CancelAppointment_Observations_Label)
                                .addGap(18, 18, 18)
                                .addComponent(PatientView_ReqCan_CanAppObservationField_Scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(56, 56, 56)
                                .addComponent(ReqCan_CancelAppointment_Cancel_Button)))
                        .addGap(18, 18, 18)
                        .addComponent(ReqCan_ReqMedApp_AppType_Label)
                        .addGap(18, 18, 18)
                        .addComponent(ReqCan_ReqMedApp_AppType_Dropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(ReqCan_ReqMedApp_Create_Button)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        PatientView_Tab.addTab("Request/Cancel", PatientView_ReqCan_Tab);

        javax.swing.GroupLayout panelRound1Layout = new javax.swing.GroupLayout(panelRound1);
        panelRound1.setLayout(panelRound1Layout);
        panelRound1Layout.setHorizontalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRound2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(PatientView_Tab)
        );
        panelRound1Layout.setVerticalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addComponent(panelRound2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PatientView_Tab))
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

    private void PatientView_Close_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PatientView_Close_ButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_PatientView_Close_ButtonActionPerformed

    private void ReqCan_CancelAppointment_Cancel_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReqCan_CancelAppointment_Cancel_ButtonActionPerformed
        String idAppointment = (String) ReqCan_CancelAppointment_IDApp_Dropdown.getSelectedItem();
        if (idAppointment == null || "Select one".equals(idAppointment)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select an appointment to cancel.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Delegate to AppointmentController (enforces COMPLETED/CANCELED guard)
        AppointmentController apptController = new AppointmentController();
        Response response = apptController.cancelAppointment(idAppointment);

        if (!response.isSuccess()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "[" + response.getStatus() + "] " + response.getMessage(),
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        ReqCan_CancelAppointment_IDApp_Dropdown.removeItem(idAppointment);
        PatientView_Refresh_ButtonActionPerformed(null);
        javax.swing.JOptionPane.showMessageDialog(this,
                response.getMessage(), "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_ReqCan_CancelAppointment_Cancel_ButtonActionPerformed

    private void PatientView_ModInfo_SAVE_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PatientView_ModInfo_SAVE_ButtonActionPerformed
        String firstname   = PatientView_ModInfo_Firstname_Field.getText().trim();
        String lastname    = PatientView_ModInfo_Lastname_Field.getText().trim();
        String birth       = PatientView_ModInfo_Birthday_Field.getText().trim();
        String address     = PatientView_ModInfo_Address_Field.getText().trim();
        String phoneStr    = PatientView_ModInfo_Phone_Field.getText().trim();
        String email       = PatientView_ModInfo_email_Field.getText().trim();
        String password    = PatientView_ModInfo_EnterPassword_Field.getText();
        String comPassword = PatientView_ModInfo_EnterPasswordConf_Field.getText();
        // index 0="Select one", 1="Female", 2="Male"
        int genderIdx = PatientView_ModInfo_Gender_Dropdown.getSelectedIndex();
        String genderStr = (genderIdx == 0) ? "" : (genderIdx == 2 ? "Male" : "Female");

        // Delegate ALL validation and update to PatientController
        PatientController patientController = new PatientController();
        Response response = patientController.updatePatient(
                this.patient.getId(), firstname, lastname,
                password, comPassword, email, birth, genderStr, phoneStr, address);

        if (!response.isSuccess()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "[" + response.getStatus() + "] " + response.getMessage(),
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Clear password fields for security
        PatientView_ModInfo_EnterPassword_Field.setText("");
        PatientView_ModInfo_EnterPasswordConf_Field.setText("");
        // Update title to reflect name changes
        PatientView_Label.setText("PATIENT VIEW — " + this.patient.getFirstname() + " " + this.patient.getLastname());
        this.setTitle("Ospedale — " + this.patient.getFirstname() + " " + this.patient.getLastname());
        javax.swing.JOptionPane.showMessageDialog(this,
                response.getMessage(), "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_PatientView_ModInfo_SAVE_ButtonActionPerformed

    private void PatientView_LogOut_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PatientView_LogOut_ButtonActionPerformed
        LoginView login = new LoginView();
        this.setVisible(false);
        login.setVisible(true);
    }//GEN-LAST:event_PatientView_LogOut_ButtonActionPerformed

    private void PatientView_Back_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PatientView_Back_ButtonActionPerformed
        AdminView admin = new AdminView(user, users,hospitalizations, appointments);
        this.setVisible(false);
        admin.setVisible(true);
    }//GEN-LAST:event_PatientView_Back_ButtonActionPerformed

    private void ReqCan_ReqMedApp_Specialty_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReqCan_ReqMedApp_Specialty_ButtonActionPerformed
        if (ReqCan_ReqMedApp_Specialty_ButtonReqCan_ReqMedApp_Doctor_Button.isSelected()) {
            ReqCan_ReqMedApp_Specialty_ButtonReqCan_ReqMedApp_Doctor_Button.setSelected(false);
        }

        ReqCan_AppType_Dropdown.removeAllItems();

        ReqCan_AppType_Dropdown.addItem("Select one");
        for (Specialty spec : Specialty.values()) {
            ReqCan_AppType_Dropdown.addItem(specialtyToDisplay(spec));
        }
    }//GEN-LAST:event_ReqCan_ReqMedApp_Specialty_ButtonActionPerformed

    private void ReqCan_ReqMedApp_Specialty_ButtonReqCan_ReqMedApp_Doctor_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReqCan_ReqMedApp_Specialty_ButtonReqCan_ReqMedApp_Doctor_ButtonActionPerformed
        if (ReqCan_ReqMedApp_Specialty_Button.isSelected()) {
            ReqCan_ReqMedApp_Specialty_Button.setSelected(false);
        }
        ReqCan_AppType_Dropdown.removeAllItems();

        ReqCan_AppType_Dropdown.addItem("Select one");
        for (User doc : this.users) {
            if (doc instanceof Doctor) {
                ReqCan_AppType_Dropdown.addItem(doc.getFirstname() + " " + doc.getLastname());
            }
        }
    }//GEN-LAST:event_ReqCan_ReqMedApp_Specialty_ButtonReqCan_ReqMedApp_Doctor_ButtonActionPerformed

    private void ReqCan_ReqMedApp_Create_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReqCan_ReqMedApp_Create_ButtonActionPerformed
        try {
            String appointDate = ReqCan_ReqMedApp_AppDate_Field.getText();
            if (appointDate.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Please enter a date (YYYY-MM-DD).", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            LocalDate appointmentDate = LocalDate.of(Integer.parseInt(appointDate.substring(0, 4)), Integer.parseInt(appointDate.substring(5, 7)), Integer.parseInt(appointDate.substring(8)));
            if (appointmentDate.isBefore(LocalDate.now())) {
                javax.swing.JOptionPane.showMessageDialog(this, "Appointment date must be today or in the future.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            String timeStr = ReqCan_ReqMedApp_AppTime_Field.getText();
            if (timeStr.isEmpty()) timeStr = "08:00";
            LocalTime appointmentHour = LocalTime.of(Integer.parseInt(timeStr.substring(0, 2)), Integer.parseInt(timeStr.substring(3)));
            LocalDateTime appointDateTime = LocalDateTime.of(appointmentDate, appointmentHour);
            String appointmentReason = ReqCan_ReqMedApp_AppReason_Field.getText();

            // Si se seleccionó por Doctor, el item es el ID del doctor (número)
            // Si se seleccionó por Specialty, el item es nombre de specialty
            String selected = ReqCan_AppType_Dropdown.getItemAt(ReqCan_AppType_Dropdown.getSelectedIndex());
            if ("Select one".equals(selected)) {
                javax.swing.JOptionPane.showMessageDialog(this, "Please select a doctor or specialty.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            Doctor doctor = null;
            Specialty specialty = null;
            if (ReqCan_ReqMedApp_Specialty_ButtonReqCan_ReqMedApp_Doctor_Button.isSelected()) {
                // El dropdown muestra "Firstname Lastname" del doctor
                for (User use : this.users) {
                    if (use instanceof Doctor) {
                        String fullName = use.getFirstname() + " " + use.getLastname();
                        if (fullName.equals(selected)) {
                            doctor = (Doctor) use;
                            specialty = doctor.getSpecialty();
                            break;
                        }
                    }
                }
            } else {
                // El dropdown muestra nombre legible de specialty (ej: "General Medicine", "Traumatology & Orthopedics")
                // parseSpecialty: reemplazar " & " → "_", " " → "_", todo mayúsculas
                String specName = selected.replaceAll(" & ", "_").replaceAll(" ", "_").toUpperCase();
                try {
                    specialty = Specialty.valueOf(specName);
                } catch (IllegalArgumentException ex) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Could not match specialty: " + selected, "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Buscar primer doctor disponible con esa specialty
                for (User use : this.users) {
                    if (use instanceof Doctor && ((Doctor)use).getSpecialty() == specialty) {
                        doctor = (Doctor) use;
                        break;
                    }
                }
            }
            if (doctor == null) {
                javax.swing.JOptionPane.showMessageDialog(this, "No doctor found for the selection.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            // index 0="Select one", 1="Remote"(false), 2="In-person"(true)
            boolean appointmentType = (ReqCan_ReqMedApp_AppType_Dropdown.getSelectedIndex() == 2);
            String appointId = DataStore.getInstance().generateAppointmentId(patient.getId());
            Appointment newAppointment = new Appointment(appointId, patient, doctor, specialty, appointDateTime, appointmentReason, appointmentType);
            DataStore.getInstance().addAppointment(newAppointment);
            this.appointments.add(newAppointment);
            // Actualizar dropdown de cancelar
            ReqCan_CancelAppointment_IDApp_Dropdown.addItem(appointId);
            // Limpiar campos tras crear cita
            ReqCan_ReqMedApp_AppDate_Field.setText("");
            ReqCan_ReqMedApp_AppTime_Field.setText("");
            ReqCan_ReqMedApp_AppReason_Field.setText("");
            ReqCan_AppType_Dropdown.setSelectedIndex(0);
            ReqCan_ReqMedApp_AppType_Dropdown.setSelectedIndex(0);
            javax.swing.JOptionPane.showMessageDialog(this, "Appointment created successfully!\nID: " + appointId + "\nDoctor: " + doctor.getFirstname() + " " + doctor.getLastname(), "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error creating appointment: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_ReqCan_ReqMedApp_Create_ButtonActionPerformed


    private void PatientView_Refresh_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PatientView_Refresh_ButtonActionPerformed
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        if (this.patient == null) return;
        for (Appointment a : this.patient.getAppointments()) {
            model.addRow(new Object[]{
                a.getId(),
                a.getDatetime().toString(),
                a.getDoctor().getFirstname() + " " + a.getDoctor().getLastname(),
                a.getSpecialty().name(),
                a.isType() ? "In-person" : "Remote",
                a.getStatus().name()
            });
        }
        // Actualizar también dropdown de cancelar
        ReqCan_CancelAppointment_IDApp_Dropdown.removeAllItems();
        ReqCan_CancelAppointment_IDApp_Dropdown.addItem("Select one");
        for (Appointment a : this.patient.getAppointments()) {
            if (!a.getStatus().equals(AppointmentStatus.CANCELED)) {
                ReqCan_CancelAppointment_IDApp_Dropdown.addItem(a.getId());
            }
        }
    }//GEN-LAST:event_PatientView_Refresh_ButtonActionPerformed

    private void ReqCan_ReqHosp_Create_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReqCan_ReqHosp_Create_ButtonActionPerformed
        try {
            String hospitalizationReason = ReqCan_ReqHospReason_Field.getText();
            String selectedDoctor = ReqCan_Hosp_AttendingDoctor_Dropdown.getItemAt(ReqCan_Hosp_AttendingDoctor_Dropdown.getSelectedIndex());
            if (selectedDoctor == null || "Select one".equals(selectedDoctor)) {
                javax.swing.JOptionPane.showMessageDialog(this, "Please select a doctor.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            long idDoctor = extractId(selectedDoctor);
            Doctor doc = null;
            for(User use : this.users){
                if (use.getId() == idDoctor && use instanceof Doctor){
                    doc = (Doctor) use;
                }
            }
            if (doc == null) {
                javax.swing.JOptionPane.showMessageDialog(this, "Doctor not found.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            String dateStr = ReqCan_Hosp_EstDateAdmission_Field.getText();
            if (dateStr.isEmpty()) dateStr = java.time.LocalDate.now().toString();
            LocalDate stimateDate = LocalDate.of(Integer.parseInt(dateStr.substring(0, 4)), Integer.parseInt(dateStr.substring(5, 7)), Integer.parseInt(dateStr.substring(8)));
            String roomStr = ReqCan_Hosp_DesiredRoomType_Dropdown.getItemAt(ReqCan_Hosp_DesiredRoomType_Dropdown.getSelectedIndex());
            RoomType desireRoom;
            try {
                desireRoom = RoomType.valueOf(roomStr.toUpperCase());
            } catch (Exception e) {
                desireRoom = RoomType.IMC;
            }
            String observations = ReqCan_Hosp_Observations_Field.getText();
            String hospId = DataStore.getInstance().generateHospitalizationId(patient.getId());
            Hospitalization newHosp = new Hospitalization(hospId, this.patient, doc, stimateDate, hospitalizationReason, desireRoom, observations);
            DataStore.getInstance().addHospitalization(newHosp);
            this.hospitalizations.add(newHosp);
            // Limpiar campos tras crear hospitalización
            ReqCan_ReqHospReason_Field.setText("");
            ReqCan_Hosp_EstDateAdmission_Field.setText("");
            ReqCan_Hosp_Observations_Field.setText("");
            ReqCan_Hosp_AttendingDoctor_Dropdown.setSelectedIndex(0);
            javax.swing.JOptionPane.showMessageDialog(this, "Hospitalization requested successfully!\nID: " + hospId + "\nDoctor: " + doc.getFirstname() + " " + doc.getLastname(), "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_ReqCan_ReqHosp_Create_ButtonActionPerformed

    private void ReqCan_AppType_DropdownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReqCan_AppType_DropdownActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ReqCan_AppType_DropdownActionPerformed

    /**
     * Convierte un Specialty enum a nombre legible para mostrar en la UI.
     */
    private String specialtyToDisplay(core.models.Specialty spec) {
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PatientView_AppointmentHist_Tab;
    private javax.swing.JScrollPane PatientView_Appointments_Details_Panel;
    private javax.swing.JButton PatientView_Back_Button;
    private javax.swing.JButton PatientView_Close_Button;
    private javax.swing.JLabel PatientView_Label;
    private javax.swing.JButton PatientView_LogOut_Button;
    private javax.swing.JTextField PatientView_ModInfo_Address_Field;
    private javax.swing.JLabel PatientView_ModInfo_Address_Label;
    private javax.swing.JTextField PatientView_ModInfo_Birthday_Field;
    private javax.swing.JLabel PatientView_ModInfo_Birthday_Label;
    private javax.swing.JTextField PatientView_ModInfo_EnterPasswordConf_Field;
    private javax.swing.JTextField PatientView_ModInfo_EnterPassword_Field;
    private javax.swing.JTextField PatientView_ModInfo_EnterUser_Field;
    private javax.swing.JTextField PatientView_ModInfo_Firstname_Field;
    private javax.swing.JLabel PatientView_ModInfo_Fistname_Label;
    private javax.swing.JComboBox<String> PatientView_ModInfo_Gender_Dropdown;
    private javax.swing.JLabel PatientView_ModInfo_Gender_Label;
    private javax.swing.JTextField PatientView_ModInfo_Lastname_Field;
    private javax.swing.JLabel PatientView_ModInfo_Lastname_Label;
    private javax.swing.JLabel PatientView_ModInfo_PasswordConfirm_Label;
    private javax.swing.JLabel PatientView_ModInfo_Password_Label;
    private javax.swing.JTextField PatientView_ModInfo_Phone_Field;
    private javax.swing.JLabel PatientView_ModInfo_Phone_Label;
    private javax.swing.JButton PatientView_ModInfo_SAVE_Button;
    private javax.swing.JLabel PatientView_ModInfo_User_Label;
    private javax.swing.JTextField PatientView_ModInfo_email_Field;
    private javax.swing.JLabel PatientView_ModInfo_email_Label;
    private javax.swing.JPanel PatientView_ModifyInfo_Tab;
    private javax.swing.JButton PatientView_Refresh_Button;
    private javax.swing.JScrollPane PatientView_ReqCan_CanAppObservationField_Scroll;
    private javax.swing.JScrollPane PatientView_ReqCan_ReqHospObservationField_Scroll;
    private javax.swing.JScrollPane PatientView_ReqCan_ReqHosp_HospReasonField_Scroll;
    private javax.swing.JPanel PatientView_ReqCan_Tab;
    private javax.swing.JTabbedPane PatientView_Tab;
    private javax.swing.JComboBox<String> ReqCan_AppType_Dropdown;
    private javax.swing.JButton ReqCan_CancelAppointment_Cancel_Button;
    private javax.swing.JComboBox<String> ReqCan_CancelAppointment_IDApp_Dropdown;
    private javax.swing.JLabel ReqCan_CancelAppointment_IDApp_Label;
    private javax.swing.JLabel ReqCan_CancelAppointment_Label;
    private javax.swing.JTextArea ReqCan_CancelAppointment_Observations_Field;
    private javax.swing.JLabel ReqCan_CancelAppointment_Observations_Label;
    private javax.swing.JComboBox<String> ReqCan_Hosp_AttendingDoctor_Dropdown;
    private javax.swing.JLabel ReqCan_Hosp_AttendingDoctor_Label;
    private javax.swing.JComboBox<String> ReqCan_Hosp_DesiredRoomType_Dropdown;
    private javax.swing.JLabel ReqCan_Hosp_DesiredRoomType_Label;
    private javax.swing.JTextField ReqCan_Hosp_EstDateAdmission_Field;
    private javax.swing.JLabel ReqCan_Hosp_EstDateAdmission_Label;
    private javax.swing.JTextArea ReqCan_Hosp_Observations_Field;
    private javax.swing.JLabel ReqCan_Hosp_Observations_Label;
    private javax.swing.JTextArea ReqCan_ReqHospReason_Field;
    private javax.swing.JLabel ReqCan_ReqHospReason_Label;
    private javax.swing.JButton ReqCan_ReqHosp_Create_Button;
    private javax.swing.JLabel ReqCan_ReqHospitalization_Label;
    private javax.swing.JTextField ReqCan_ReqMedApp_AppDate_Field;
    private javax.swing.JLabel ReqCan_ReqMedApp_AppDate_Label;
    private javax.swing.JTextArea ReqCan_ReqMedApp_AppReason_Field;
    private javax.swing.JLabel ReqCan_ReqMedApp_AppReason_Label;
    private javax.swing.JScrollPane ReqCan_ReqMedApp_AppReason_Label_Scroll;
    private javax.swing.JTextField ReqCan_ReqMedApp_AppTime_Field;
    private javax.swing.JLabel ReqCan_ReqMedApp_AppTime_Label;
    private javax.swing.JComboBox<String> ReqCan_ReqMedApp_AppType_Dropdown;
    private javax.swing.JLabel ReqCan_ReqMedApp_AppType_Label;
    private javax.swing.JButton ReqCan_ReqMedApp_Create_Button;
    private javax.swing.JRadioButton ReqCan_ReqMedApp_Specialty_Button;
    private javax.swing.JRadioButton ReqCan_ReqMedApp_Specialty_ButtonReqCan_ReqMedApp_Doctor_Button;
    private javax.swing.JLabel ReqCan_ReqMedAppointment_Label;
    private javax.swing.JSeparator ReqCan_Separator;
    private javax.swing.JSeparator ReqHosp_CanApp_Separator;
    private javax.swing.JTable jTable1;
    private core.controllers.PanelRound panelRound1;
    private core.controllers.PanelRound panelRound2;
    // End of variables declaration//GEN-END:variables
}
