package core.models.dto;

import core.models.Specialty;

/**
 * Lightweight DTO for populating doctor combo boxes.
 * Only carries the data the view needs to display — no full model objects.
 */
public record DoctorComboDTO(
        long id,
        String fullName,
        Specialty specialty
) {
    /** Returns the display label shown in combo boxes. */
    public String displayLabel() {
        return "Dr. " + fullName + " (" + specialty.name().replace("_", " ") + ") [ID: " + id + "]";
    }
}
