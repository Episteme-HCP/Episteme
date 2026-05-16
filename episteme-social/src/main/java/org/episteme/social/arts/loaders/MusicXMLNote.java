/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.social.arts.loaders;

public class MusicXMLNote {
    private boolean rest;
    private int duration;
    private String type;
    private MusicXMLPitch pitch;
    private String voice;
    private String staff;
    private boolean chord;
    private boolean tieStart;
    private boolean tieStop;
    private String dynamics;
    private String articulations;

    public boolean isRest() { return rest; }
    public void setRest(boolean rest) { this.rest = rest; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public MusicXMLPitch getPitch() { return pitch; }
    public void setPitch(MusicXMLPitch pitch) { this.pitch = pitch; }

    public String getVoice() { return voice; }
    public void setVoice(String voice) { this.voice = voice; }

    public String getStaff() { return staff; }
    public void setStaff(String staff) { this.staff = staff; }

    public boolean isChord() { return chord; }
    public void setChord(boolean chord) { this.chord = chord; }

    public boolean hasTieStart() { return tieStart; }
    public void setTieStart(boolean tieStart) { this.tieStart = tieStart; }

    public boolean hasTieStop() { return tieStop; }
    public void setTieStop(boolean tieStop) { this.tieStop = tieStop; }

    public String getDynamics() { return dynamics; }
    public void setDynamics(String dynamics) { this.dynamics = dynamics; }

    public String getArticulations() { return articulations; }
    public void setArticulations(String articulations) { this.articulations = articulations; }
}

