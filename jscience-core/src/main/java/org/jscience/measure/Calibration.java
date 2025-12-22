package org.jscience.measure;

import java.time.Instant;

/**
 * Represents a calibration record for an instrument.
 *
 * @param date        The date/time of calibration.
 * @param performedBy Who performed the calibration.
 * @param result      The result/offset/status of the calibration.
 * @param nextDue     When the next calibration is due.
 */
public record Calibration(Instant date, String performedBy, String result, Instant nextDue) {
}
