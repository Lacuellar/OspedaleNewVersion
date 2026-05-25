package core.views;

import core.models.DataStore;
import core.models.Doctor;
import core.models.Patient;
import core.models.User;
import java.util.ArrayList;

/**
 * Single point of control for all view transitions in the application.
 *
 * Views must NOT instantiate other views directly — they call this class.
 * This satisfies SRP (one class owns navigation) and makes transitions testable.
 *
 * Usage:
 *   ViewNavigator.getInstance().showLogin();
 *   ViewNavigator.getInstance().showAdmin(user);
 *   ViewNavigator.getInstance().showDoctor(user, doctor);
 *   ViewNavigator.getInstance().showPatient(user, patient);
 */
public class ViewNavigator {

    private static ViewNavigator instance;

    /** The view currently displayed on screen. */
    private javax.swing.JFrame currentView;

    private ViewNavigator() {}

    public static ViewNavigator getInstance() {
        if (instance == null) {
            instance = new ViewNavigator();
        }
        return instance;
    }

    // =========================================================================
    // NAVIGATION METHODS
    // =========================================================================

    /**
     * Shows the login/registration screen and hides the current view.
     */
    public void showLogin() {
        LoginView loginView = new LoginView();
        navigate(loginView);
    }

    /**
     * Shows the administrator dashboard and hides the current view.
     *
     * @param user the authenticated administrator User object
     */
    public void showAdmin(User user) {
        DataStore ds = DataStore.getInstance();
        AdminView adminView = new AdminView(user, buildUserList(ds), ds.getHospitalizations(), ds.getAppointments());
        navigate(adminView);
    }

    /**
     * Shows the doctor dashboard and hides the current view.
     *
     * @param user   the authenticated User (may be Administrator navigating to a doctor's view)
     * @param doctor the Doctor whose dashboard to open
     */
    public void showDoctor(User user, Doctor doctor) {
        DataStore ds = DataStore.getInstance();
        DoctorView doctorView = new DoctorView(user, doctor, buildUserList(ds), ds.getHospitalizations(), ds.getAppointments());
        navigate(doctorView);
    }

    /**
     * Shows the patient dashboard and hides the current view.
     *
     * @param user    the authenticated User (may be Administrator navigating to a patient's view)
     * @param patient the Patient whose dashboard to open
     */
    public void showPatient(User user, Patient patient) {
        DataStore ds = DataStore.getInstance();
        PatientView patientView = new PatientView(user, patient, buildUserList(ds), ds.getAppointments(), ds.getHospitalizations());
        navigate(patientView);
    }

    // =========================================================================
    // INTERNAL
    // =========================================================================

    /**
     * Hides the current view, sets the new one as current, and shows it.
     */
    private void navigate(javax.swing.JFrame newView) {
        if (currentView != null) {
            currentView.setVisible(false);
        }
        currentView = newView;
        newView.setVisible(true);
    }

    /**
     * Builds the combined user list (admins + patients + doctors) from DataStore.
     * Always returns a fresh copy so views never hold stale references.
     */
    private ArrayList<User> buildUserList(DataStore ds) {
        ArrayList<User> all = new ArrayList<>();
        all.addAll(ds.getAdmins());
        all.addAll(ds.getPatients());
        all.addAll(ds.getDoctors());
        return all;
    }
}
