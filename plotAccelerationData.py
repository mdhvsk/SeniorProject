import os.path
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from scipy.signal import find_peaks
import matplotlib.font_manager as fm
from tkinter import filedialog as fd
import tkinter as tk

def open_csv():
    # Need this to remove the small GUI that pops up when calling askopenfilenames()
    root = tk.Tk()
    root.withdraw()
    while True:
        filetypes = (
        ('csv files', '*.csv'),
        ('All files', '*.*'))
        filename = fd.askopenfilename(
            initialdir='/',
            filetypes=filetypes)
        if os.path.exists(filename):
            df = pd.read_csv(filename)
            return df
        else:
            print("Invalid file path. Please provide a valid path.")

# Call the function to open the CSV file
df = open_csv()
df.columns = ["X", "Y", "Z", "Time"]
df['Magnitude'] = np.sqrt(df['X']**2 + df['Y']**2 + df['Z']**2)
# Filter the DataFrame using boolean indexing
df = df[df['Magnitude'] <= 22]

# Apply a moving average filter to smooth the signal
window_size = 5
df['Magnitude'] = df['Magnitude'].rolling(window_size, min_periods=1).mean()

# Calculate the time interval
dt = np.diff(df['Time']).mean()

if np.isclose(dt, 0):
    raise ValueError("Time interval is zero or too small.")

# Calculate the Fourier transform and associated frequencies
n = len(df['Magnitude'])
frequency = np.fft.fftfreq(n, d=dt)[:n//2]
df['Frequency'] = pd.Series(frequency, index=df.index[:n//2])
magnitude = np.abs(np.fft.fft(df['Magnitude']))[:n//2]
df['Fourier Transform Magnitude'] = pd.Series(magnitude, index=df.index[:n//2])



# Plot 1: Acceleration vs. Time
plt.figure()
ax1 = sns.lineplot(data=df, x="Time", y="Magnitude")
ax1.set_xlabel("Time (s)")
ax1.set_ylabel(r"Magnitude ($\frac{m}{s^2}$)")
ax1.set_title("Acceleration vs. Time")
sns.despine()

sampling_rate = 1/dt
print(sampling_rate)
total_samples = len(df["Fourier Transform Magnitude"])
# Bounds for what I expect 
lower_bound = .01
upper_bound = 5

# ---- Get the power density spectrum ----
# Calculate the power spectrum
df['Power Spectrum'] = df['Fourier Transform Magnitude'] ** 2

# Normalize to get the power spectral density 
df['Power Spectral Density'] = df['Power Spectrum'] / (total_samples * sampling_rate ** 2)

# ---- Create a dataframe for closer inspecition in desired range of operation (.1-5Hz) ----
filtered_df = df[(df['Frequency'] >= lower_bound) & (df['Frequency'] <= upper_bound)]

# Plot 2: Fourier Transform
plt.figure()
ax2 = sns.lineplot(data=df, x="Frequency", y="Fourier Transform Magnitude")
ax2.set_xlabel("Frequency (Hz)")
ax2.set_ylabel(r"Magnitude ($\frac{m}{s^2}$)")
ax2.set_title("Fourier Transform")
sns.despine()

# Plot 3: Fourier Transform (0.1 Hz - 5 Hz)
plt.figure()
ax3 = sns.lineplot(data=filtered_df, x="Frequency", y="Fourier Transform Magnitude")
ax3.set_xlabel("Frequency (Hz)")
ax3.set_ylabel(r"Magnitude($\frac{m}{s^2}$)")
ax3.set_title("Fourier Transform (0.1 Hz - 5 Hz)")
sns.despine()

# Plot 4: Power Spectral Density
plt.figure()
ax4 = sns.lineplot(data=df, x="Frequency", y="Power Spectral Density")
ax4.set_xlabel("Frequency (Hz)")
ax4.set_ylabel(r"Power Spectral Density ($\frac{m^2}{s^4}$ per Hz)")
ax4.set_title("Power Spectral Density")
sns.despine()

# Plot the Power Spectral Density in the operational range

plt.figure()
ax5 = sns.lineplot(data = filtered_df, x="Frequency", y="Power Spectral Density")
ax5.set_xlabel("Frequency (Hz)")
ax5.set_ylabel(r"Power Spectral Density ($\frac{m^2}{s^4}$ per Hz)")
ax5.set_title("Power Spectral Density (0.1 Hz - 5 Hz)")
sns.despine()

# Calculate Important Values
aoc = np.trapz(filtered_df['Power Spectral Density'], x=filtered_df["Frequency"])
zero_value = df['Fourier Transform Magnitude'][np.abs(df['Frequency']) == 0].item()
fMag_units = r"($\frac{m}{s^2}$)"
pds_units = r"($\frac{m^2}{s^4}$) per Hz "
important_values = {
    "Label": ["0 Hz Fourier Magnitude", "Area Under the PSD Curve"],
    "Value": [f"{round(zero_value, 3)} {fMag_units}", f"{round(aoc, 3)} {pds_units}"]
}
important_values_df = pd.DataFrame(data= important_values)
# print(f"Area under curve is: {aoc}")

# Set a threshold to get the peak
threshold_factor = .5
global_max = filtered_df["Fourier Transform Magnitude"].max()
threshold = threshold_factor * global_max
# Set a Minimum Distance between Peaks
min_distance = 36

# Get the peaks for the fourier transform
peaks, peak_dict = find_peaks(filtered_df["Fourier Transform Magnitude"], height=threshold, distance=min_distance)
if len(peaks) != 0:
    df_max = filtered_df.iloc[peaks][['Frequency', 'Fourier Transform Magnitude']]
else:
    df_max = pd.DataFrame({'Frequency': [filtered_df['Frequency'].iloc[filtered_df['Fourier Transform Magnitude'].idxmax()]],
                           'Fourier Transform Magnitude': [global_max]})
df_max = df_max.round({'Frequency': 2, 'Fourier Transform Magnitude': 2})

plt.figure()
sns.lineplot(data=filtered_df, x="Frequency", y="Fourier Transform Magnitude")
plt.xlabel("Frequency (Hz)")
plt.ylabel(r"Magnitude($\frac{m}{s^2}$)")
plt.title("Fourier Transform (0.1 Hz - 5 Hz) with Peaks")
plt.plot(df_max['Frequency'], df_max['Fourier Transform Magnitude'], 'ro', markersize=5)
sns.despine()

# Create a new figure for displaying the table
fig, (ax1, ax2) = plt.subplots(nrows=2, ncols=1, figsize=(6, 8))
# Remove axis from the subplots
ax1.axis('off')
ax2.axis('off')
# Display the first DataFrame
table1 = ax1.table(cellText=df_max.values, colLabels=df_max.columns, loc='center', rowLoc='center', cellLoc='center')
table1.scale(1, 2)
table1.set_fontsize(16)
ax1.set_title("Peak Frequencies", fontweight = 'bold', fontsize= 20)
# Display the second DataFrame
table2 = ax2.table(cellText=important_values_df.values, colLabels=important_values_df.columns, loc='center', rowLoc='center', cellLoc='center')
table2.scale(1, 2)
table2.set_fontsize(16)
ax2.set_title("Key Values", fontweight = 'bold', fontsize= 20)
# Adjust spacing between subplots
plt.subplots_adjust(hspace=0.5)
#Bold the column headers

header_cells1 = table1.get_celld()
for key in header_cells1:
    if key[0] == 0:
        cell = header_cells1[key]
        cell.set_text_props(fontproperties=fm.FontProperties(weight='bold'))
header_cells2 = table2.get_celld()
for key in header_cells2:
    if key[0] == 0:
        cell = header_cells2[key]
        cell.set_text_props(fontproperties=fm.FontProperties(weight='bold'))

# Show all the plots
plt.show()
