
import os

PROPERTIES_FILE = r"c:\Silvere\Encours\Developpement\JScience\jscience-featured-apps\src\main\resources\org\jscience\apps\ui\i18n\messages_apps.properties"
JAVA_FILE = r"c:\Silvere\Encours\Developpement\JScience\jscience-featured-apps\src\main\java\org\jscience\apps\physics\spintronics\SpinValveApp.java"

NEW_KEYS = {
    "spintronics.tab.spectrum": "RF Spectrum",
    "spintronics.chart.spectrum.title": "Magnetization RF Spectrum",
    "spintronics.label.peak": "Peak: {0} GHz",
    "spintronics.check.pma": "Enable PMA (Perpendicular)",
    "spintronics.label.sot": "SOT Physics (SHE)",
    "spintronics.check.saf": "Enable SAF (Co/Ru/Co)",
    "spintronics.btn.hysteresis": "Run Hysteresis Loop",
    "spintronics.btn.export": "Export Trace (CSV)",
    "spintronics.axis.freq": "Frequency (GHz)",
    "spintronics.axis.power": "Power (a.u.)",
    "spintronics.label.damping": "Damping (\u03B1)",
    "spintronics.label.geometry": "Geometry",
    "spintronics.label.area": "Area (nm)",
    "spintronics.label.temp": "Temperature (K)"
}

def add_keys():
    with open(PROPERTIES_FILE, 'a', encoding='utf-8') as f:
        f.write("\n# Spintronics App\n")
        for key, value in NEW_KEYS.items():
            # Escape unicode for properties file if needed, but modern java handles utf8 if configured.
            # Standard properties usually use \uXXXX. Python's ascii escape might be safest.
            # But let's assume UTF-8 for now as user seems to use it.
            f.write(f"{key}={value}\n")
    print("Keys added.")

def update_java():
    with open(JAVA_FILE, 'r', encoding='utf-8') as f:
        content = f.read()

    replacements = [
        ('new Tab("RF Spectrum")', 'new Tab(i18n.get("spintronics.tab.spectrum"))'),
        ('spectrumChart.setTitle("Magnetization RF Spectrum")', 'spectrumChart.setTitle(i18n.get("spintronics.chart.spectrum.title"))'),
        ('new Label("Peak: -- GHz")', 'new Label(java.text.MessageFormat.format(i18n.get("spintronics.label.peak"), "--"))'),
        ('new CheckBox("Enable PMA (Perpendicular)")', 'new CheckBox(i18n.get("spintronics.check.pma"))'),
        ('new Label("SOT Physics (SHE)")', 'new Label(i18n.get("spintronics.label.sot"))'),
        ('new CheckBox("Enable SAF (Co/Ru/Co)")', 'new CheckBox(i18n.get("spintronics.check.saf"))'),
        ('new Button("Run Hysteresis Loop")', 'new Button(i18n.get("spintronics.btn.hysteresis"))'),
        ('new Button("Export Trace (CSV)")', 'new Button(i18n.get("spintronics.btn.export"))'),
        ('freqAxis.setLabel("Frequency (GHz)")', 'freqAxis.setLabel(i18n.get("spintronics.axis.freq"))'),
        ('powerAxis.setLabel("Power (a.u.)")', 'powerAxis.setLabel(i18n.get("spintronics.axis.power"))'),
        ('new Label("Damping (\u03B1)")', 'new Label(i18n.get("spintronics.label.damping"))'),
        ('new Label("Geometry")', 'new Label(i18n.get("spintronics.label.geometry"))'),
        ('new Label("Area (nm)")', 'new Label(i18n.get("spintronics.label.area"))'),
        ('new Label("Temperature (K)")', 'new Label(i18n.get("spintronics.label.temp"))'),
        # Dynamic format replacement is harder.
        ('peakFreqLabel.setText(String.format("Peak: %.2f GHz", peakGHz))', 'peakFreqLabel.setText(java.text.MessageFormat.format(i18n.get("spintronics.label.peak"), String.format("%.2f", peakGHz)))')
    ]

    for old, new in replacements:
        content = content.replace(old, new)

    with open(JAVA_FILE, 'w', encoding='utf-8') as f:
        f.write(content)
    print("Java file updated.")

if __name__ == "__main__":
    add_keys()
    update_java()
