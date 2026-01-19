package org.jscience.arts;

/**
 * Constants and utility methods for the Arts module.
 * 
 * @deprecated Use {@link ArtForm} instead for art categories.
 */
public final class ArtsConstants {

    @Deprecated public static final int UNKNOWN = -1;
    @Deprecated public static final int ARCHITECTURE = 1;
    @Deprecated public static final int SCULPTURE = 2;
    @Deprecated public static final int PAINTING = 3;
    @Deprecated public static final int MUSIC = 4;
    @Deprecated public static final int POETRY = 5;
    @Deprecated public static final int DANCE = 6;
    @Deprecated public static final int CINEMA = 7;
    @Deprecated public static final int PHOTOGRAPHY = 8;
    @Deprecated public static final int COMICS = 9;
    @Deprecated public static final int TELEVISION = 10;
    @Deprecated public static final int RADIO = 11;
    @Deprecated public static final int GAMING = 12;
    @Deprecated public static final int LITERATURE = 20;
    @Deprecated public static final int THEATER = 21;
    @Deprecated public static final int CRAFTING = 22;

    private ArtsConstants() {}
    
    /**
     * @deprecated Use {@link ArtForm#toString()}
     */
    @Deprecated
    public static String getCategoryName(int value) {
        ArtForm form = ArtForm.fromValue(value);
        return form != null ? form.name() : "Unknown";
    }

    /**
     * Converts a legacy integer category to the new ArtForm enum.
     */
    public static ArtForm toArtForm(int value) {
        return ArtForm.fromValue(value);
    }
}
