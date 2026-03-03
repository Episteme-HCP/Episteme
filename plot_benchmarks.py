import json
import matplotlib.pyplot as plt
import pandas as pd
import seaborn as sns
from matplotlib.backends.backend_pdf import PdfPages
import os
import sys

def plot_benchmarks(json_path, output_pdf=None):
    if not os.path.exists(json_path):
        print(f"Error: {json_path} not found.")
        return

    # If no output path, use JSON folder + .pdf
    if output_pdf is None:
        base, _ = os.path.splitext(json_path)
        output_pdf = base + ".pdf"
    
    with open(json_path, 'r', encoding='utf-8') as f:
        data = json.load(f)

    # Convert to DataFrame
    runs = data.get('runs', [])
    if not runs:
        print("No benchmark runs found in JSON.")
        return

    df = pd.DataFrame(runs)
    
    # Filter only successful runs with positive score
    df = df[df['status'] == 'SUCCESS']
    if df.empty:
        print("No successful benchmark runs to plot.")
        return

    # Create PDF
    with PdfPages(output_pdf) as pdf:
        # 1. Overview Page
        plt.figure(figsize=(11, 8.5))
        plt.axis('off')
        plt.text(0.5, 0.9, "Episteme Benchmark Report", fontsize=24, ha='center', weight='bold')
        
        ctx = data.get('context', {})
        meta_text = f"Date: {ctx.get('timestamp', 'N/A')}\n"
        meta_text += f"OS: {ctx.get('os_name', 'N/A')} ({ctx.get('os_arch', 'N/A')})\n"
        meta_text += f"Java: {ctx.get('java_version', 'N/A')}\n"
        meta_text += f"Processors: {ctx.get('processors', 'N/A')}\n"
        
        plt.text(0.5, 0.7, meta_text, fontsize=14, ha='center', linespacing=1.8)
        
        summary_text = f"Total Benchmarks: {len(runs)}\n"
        summary_text += f"Successful: {len(df)}\n"
        plt.text(0.5, 0.5, summary_text, fontsize=14, ha='center', weight='semibold')
        
        pdf.savefig()
        plt.close()

        # 2. Plots by Domain
        domains = df['domain'].unique()
        for domain in domains:
            domain_df = df[df['domain'] == domain].copy()
            
            # Sort by ops_per_sec
            domain_df = domain_df.sort_values('result', ascending=False)
            
            plt.figure(figsize=(12, 7))
            sns.set_theme(style="whitegrid")
            
            # Extract numerical value from result (e.g. "123.45 ops/s")
            def extract_score(s):
                try:
                    # Handle "N/A" or status strings
                    parts = s.split(' ')
                    if not parts: return 0.0
                    return float(parts[0])
                except:
                    return 0.0
            
            domain_df['score_val'] = domain_df['result'].apply(extract_score)
            domain_df = domain_df[domain_df['score_val'] > 0] # Filter out failures
            
            if domain_df.empty: continue
            
            # Create labels for bars: Name + Library
            domain_df['label'] = domain_df['name'] + "\n(" + domain_df['library'] + ")"
            
            ax = sns.barplot(x='score_val', y='label', data=domain_df, palette='viridis')
            
            plt.title(f"Throughput: {domain}", fontsize=16, weight='bold')
            plt.xlabel("Operations per Second (Higher is Better)", fontsize=12)
            plt.ylabel("")
            
            # Add values on bars
            for p in ax.patches:
                width = p.get_width()
                plt.text(width, p.get_y() + p.get_height()/2, f'{width:.2f}', va='center', fontsize=10)

            plt.tight_layout()
            pdf.savefig()
            plt.close()

    print(f"Report generated: {output_pdf}")

if __name__ == "__main__":
    path = sys.argv[1] if len(sys.argv) > 1 else "benchmark-results.json"
    plot_benchmarks(path)
