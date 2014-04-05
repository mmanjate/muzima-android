package com.muzima.view.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.muzima.MuzimaApplication;
import com.muzima.R;
import com.muzima.api.model.Encounter;
import com.muzima.api.model.Notification;
import com.muzima.api.model.Patient;
import com.muzima.api.model.Person;
import com.muzima.controller.EncounterController;
import com.muzima.controller.NotificationController;
import com.muzima.controller.ObservationController;
import com.muzima.model.observation.EncounterWithObservations;
import com.muzima.model.observation.Encounters;
import com.muzima.utils.Constants;
import com.muzima.view.BaseActivity;
import com.muzima.view.patients.ObservationsActivity;

import java.util.List;

public class NotificationActivity extends BaseActivity {

    public static final String TAG = "NotificationActivity";
    public static final String NOTIFICATION = "Notification";
    public static final String PATIENT = "patient";
    private Notification notification;
    private Encounter notificationEncounter;
    private View viewEncounterButton;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);
        Intent intent = getIntent();
        notification = (Notification) intent.getSerializableExtra(NOTIFICATION);
        Patient notificationPatient = (Patient) intent.getSerializableExtra(PATIENT);
        if (notification != null) {

            if (notificationPatient != null)
                getNotificationEncounter(notificationPatient);

            displayNotification();
            markAsRead();
        }
	}

    private void displayNotification()  {
        TextView subjectView  = (TextView) findViewById(R.id.subject);
        subjectView.setText(notification.getSubject());

        TextView notificationDate = (TextView) findViewById(R.id.dateSent);
        notificationDate.setText("Sent:"  + notification.getDateCreated().toString());

        TextView sentBy = (TextView) findViewById(R.id.sentBy);
        Person person = notification.getSender();
        if (person != null)
            sentBy.setText(person.getDisplayName());

        TextView details = (TextView) findViewById(R.id.notificationDetail);
        details.setText(notification.getPayload());

        //hide view form button if form is not available
        if (notificationEncounter == null) {
            viewEncounterButton = findViewById(R.id.viewEncounter);
            viewEncounterButton.setVisibility(View.GONE);
        }

    }

    private void markAsRead() {
        NotificationController notificationController = ((MuzimaApplication) getApplicationContext()).getNotificationController();
        notification.setStatus(Constants.NotificationStatusConstants.NOTIFICATION_READ);
        try {
            notificationController.saveNotification(notification);
        } catch (NotificationController.NotificationSaveException e) {
            Log.e(TAG, "Error updating notification " + e.getMessage());
        }
    }

    /**
     * Called when the user clicks the Forms area
     */
    public void encounterView(View view) {
        if (notificationEncounter != null) {
            Intent intent = new Intent(this, ObservationsActivity.class);
            intent.putExtra(PATIENT, notificationEncounter.getPatient());
            startActivity(intent);
        }else
            Toast.makeText(this, "Could not load form", Toast.LENGTH_SHORT).show();
    }

    private void getNotificationEncounter(Patient patient) {
        EncounterController encounterController = ((MuzimaApplication) getApplicationContext()).getEncounterController();
        try {
            List<Encounter> encounters = encounterController.getEncountersByPatientUuid(patient.getUuid());
            for (Encounter encounter : encounters) {
                String formDataUuid = encounter.getFormDataUuid();
                if (formDataUuid != null && formDataUuid.equals(notification.getUuid()))
                    notificationEncounter =  encounter;
            }
        } catch (EncounterController.DownloadEncounterException e) {
            Log.e(TAG, "Error getting encounter data " + e.getMessage());
        }
    }
}
