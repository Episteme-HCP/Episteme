package org.jscience.arts;

/**
 * Enumeration of the different forms of art, categorized by traditional 
 * and modern classifications.
 * 
 * References:
 * - "The Seven Arts" (traditional classification)
 * - UNESCO Framework for Cultural Statistics
 */
public enum ArtForm {
    // The Traditional Seven Arts
    ARCHITECTURE(1),
    SCULPTURE(2),
    PAINTING(3),
    MUSIC(4),
    POETRY(5), // Literature
    DANCE(6),
    CINEMA(7), // Added in early 20th century as the "7th Art"

    // Modern and Expanded Categories
    PHOTOGRAPHY(8),
    COMICS(9), // BD
    TELEVISION(10),
    RADIO(11),
    GAMING(12), // Video games
    CRAFTING(13),
    GASTRONOMY(14), // Culinary Arts
    THEATER(15),
    DIGITAL_ART(16),
    PERFORMANCE_ART(17);

    private final int value;

    ArtForm(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    /**
     * Returns the ArtForm corresponding to the given integer value.
     */
    public static ArtForm fromValue(int value) {
        for (ArtForm form : ArtForm.values()) {
            if (form.value == value) {
                return form;
            }
        }
        return null;
    }
}
