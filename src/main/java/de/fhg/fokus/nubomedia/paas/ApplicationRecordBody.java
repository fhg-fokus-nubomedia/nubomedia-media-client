package de.fhg.fokus.nubomedia.paas;

public class ApplicationRecordBody {
	private int points;
	private String extAppId;
	
	// Getters and setters are not required for this example.
    // GSON sets the fields directly using reflection.

    public ApplicationRecordBody(String string, int i) {
		points = i;
		extAppId = string;
	}

	@Override
    public String toString() {
        return points + " - " + extAppId;
    }
}
