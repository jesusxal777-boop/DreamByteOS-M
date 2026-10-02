package com.dreambyte.osm;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/** DreamByte OS M 0.1: an Android application layer prototype. */
public class MainActivity extends Activity {
    private static final int NAVY = Color.rgb(7, 21, 45);
    private static final int BLUE = Color.rgb(17, 56, 105);
    private static final int CYAN = Color.rgb(102, 217, 255);
    private static final String INDEX_URL = "https://raw.githubusercontent.com/jesusxal777-boop/DreamByte-Package-Repository/main/";
    private LinearLayout root;
    private TextView title;
    private boolean retro = false;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        showLauncher();
    }

    private TextView label(String text, int size) {
        TextView v = new TextView(this);
        v.setText(text); v.setTextColor(Color.WHITE); v.setTextSize(size);
        v.setPadding(18, 12, 18, 12); return v;
    }
    private Button action(String text, View.OnClickListener listener) {
        Button b = new Button(this); b.setText(text); b.setTextColor(Color.WHITE);
        b.setAllCaps(false); b.setOnClickListener(listener);
        b.setBackgroundColor(BLUE); b.setPadding(8, 4, 8, 4); return b;
    }
    private void base(String heading) {
        root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(18, 18, 18, 18); root.setBackgroundColor(retro ? Color.rgb(18, 28, 42) : NAVY);
        title = label(heading, 26); title.setTypeface(Typeface.DEFAULT_BOLD); title.setTextColor(CYAN);
        root.addView(title, new LinearLayout.LayoutParams(-1, -2)); setContentView(root);
    }
    private void addGap() { TextView gap = new TextView(this); root.addView(gap, new LinearLayout.LayoutParams(1, 8)); }
    private void addNav() { addGap(); root.addView(action("⌂  Back to DreamByte Launcher", v -> showLauncher())); }

    private void showLauncher() {
        base("DreamByte OS M");
        TextView welcome = label("DreamByte Modern  •  Android-based mobile layer\nDreamPhones · DreamTabs", 15);
        welcome.setTextColor(Color.LTGRAY); root.addView(welcome);
        addGap();
        root.addView(label("Apps", 18));
        LinearLayout grid = new LinearLayout(this); grid.setOrientation(LinearLayout.VERTICAL);
        String[] names = {"⌘  DreamByte Terminal", "▣  Files", "⚙  Settings", "◈  DreamShell"};
        View.OnClickListener[] actions = {v -> showTerminal(), v -> showInfo("Files", "Files integration is reserved for the next layer.\nUse Android's storage APIs; no root access is assumed."), v -> showInfo("Settings", "Settings surface prepared. Theme mode and service controls will land in 0.2."), v -> showTerminal()};
        for (int i = 0; i < names.length; i++) { Button b = action(names[i], actions[i]); grid.addView(b, new LinearLayout.LayoutParams(-1, 56)); }
        root.addView(grid);
        addGap(); root.addView(label("Widgets / spaces prepared", 16)); root.addView(label("[  Weather  ]   [  Package updates  ]   [  Device status  ]", 13));
        addGap(); root.addView(action("Switch to DreamByte Retro", v -> { retro = !retro; showLauncher(); }));
        root.addView(label("v0.1 Prototype  |  No privileged operations", 12));
    }

    private void showTerminal() {
        base("DreamByte Terminal");
        TextView output = label("DreamShell ready. Type 'help' for commands.\n", 14); output.setTypeface(Typeface.MONOSPACE);
        ScrollView scroll = new ScrollView(this); scroll.addView(output); root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));
        EditText input = new EditText(this); input.setHint("dreambyte$ command"); input.setSingleLine(true); input.setTextColor(Color.WHITE); input.setHintTextColor(Color.GRAY);
        root.addView(input, new LinearLayout.LayoutParams(-1, 56));
        Button run = action("Run", v -> { String cmd = input.getText().toString().trim(); output.append("dreambyte$ " + cmd + "\n" + Shell.execute(cmd, this) + "\n"); input.setText(""); scroll.post(() -> scroll.fullScroll(View.FOCUS_DOWN)); });
        root.addView(run); addNav();
    }

    private void showInfo(String heading, String body) { base(heading); root.addView(label(body, 16)); addNav(); }

    static final class Shell {
        static String execute(String command, Activity activity) {
            if (command.isEmpty()) return "";
            String[] parts = command.split("\\s+", 2); String name = parts[0]; String arg = parts.length > 1 ? parts[1] : "";
            switch (name) {
                case "help": return "help clear echo date time whoami version about dream theme\npkg update|search|info|install|remove|list\nPrepared (not installed): python wget curl git";
                case "clear": return "(output clear is available on the next terminal refresh)";
                case "echo": return arg;
                case "date": case "time": return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.getDefault()).format(new Date());
                case "whoami": return "dreambyte (application user; not root)";
                case "version": return "DreamByte OS M 0.1 / Android application layer";
                case "about": return "DreamByte OS M for DreamPhones and DreamTabs. Android 12/12L is the technology reference.";
                case "dream": return "DreamByte: build calmly, ship transparently.";
                case "theme": return "DreamByte Modern and DreamByte Retro are available from the launcher.";
                case "pkg": return PackageManagerShell.execute(arg);
                case "python": case "wget": case "curl": case "git": return name + ": command architecture reserved; package is not installed.";
                default: return "dreamshell: command not found: " + name;
            }
        }
    }

    static final class PackageManagerShell {
        static String execute(String args) {
            if (args.isEmpty()) return "Usage: pkg update|search|info|install|remove|list";
            String[] p = args.split("\\s+", 2); String op = p[0]; String pkg = p.length > 1 ? p[1] : "";
            switch (op) {
                case "update": return "Remote index configured: " + INDEX_URL + "\nUse the app build to fetch the index; no packages are installed automatically.";
                case "search": return pkg.isEmpty() ? "Usage: pkg search <term>" : "Search queued against DreamByte Package Repository: " + pkg;
                case "info": return pkg.isEmpty() ? "Usage: pkg info <package>" : "No local package metadata. Remote lookup prepared for: " + pkg;
                case "install": return pkg.isEmpty() ? "Usage: pkg install <package>" : "Install not available in 0.1: package payload and permissions must be verified first (requested: " + pkg + ").";
                case "remove": return pkg.isEmpty() ? "Usage: pkg remove <package>" : "No installed packages; nothing removed (requested: " + pkg + ").";
                case "list": return "No packages installed. Repository client is intentionally non-simulated.";
                default: return "Unknown pkg command: " + op;
            }
        }
    }
}
