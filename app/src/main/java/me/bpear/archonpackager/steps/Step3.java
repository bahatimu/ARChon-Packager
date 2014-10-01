package me.bpear.archonpackager.steps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.codepond.wizardroid.WizardStep;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import me.bpear.archonpackager.Globals;
import me.bpear.archonpackager.R;

public class Step3 extends WizardStep {


    Globals g = Globals.getInstance();
    private ProgressDialog pd;
    int storage = 0;
    int adb = 0;

    //You must have an empty constructor for every step
    public Step3() {
    }

    //Set your layout here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.step3, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button rb1 = (Button) getActivity().findViewById(R.id.radioPhone); //Listen to Installed App radio button
        rb1.setOnClickListener(next_Listener);

        Button rb2 = (Button) getActivity().findViewById(R.id.radioTablet); //Listen to Select an apk radio button
        rb2.setOnClickListener(next_Listener);

        Button rb3 = (Button) getActivity().findViewById(R.id.radioPortrait); //Listen to Installed App radio button
        rb3.setOnClickListener(next_Listener);

        Button rb4 = (Button) getActivity().findViewById(R.id.radioLandscape); //Listen to Select an apk radio button
        rb4.setOnClickListener(next_Listener);

        Button cb1 = (CheckBox) getActivity().findViewById(R.id.cbStorage); //Listen to Installed App radio button
        cb1.setOnClickListener(next_Listener);

        Button cb2 = (CheckBox) getActivity().findViewById(R.id.cbAdb); //Listen to Select an apk radio button
        cb2.setOnClickListener(next_Listener);
    }

    private View.OnClickListener next_Listener = new View.OnClickListener() {
        public void onClick(View v) {
            //find out which radio button has been checked
            RadioButton rb1 = (RadioButton) getActivity().findViewById(R.id.radioPhone);
            RadioButton rb2 = (RadioButton) getActivity().findViewById(R.id.radioTablet);
            RadioButton rb3 = (RadioButton) getActivity().findViewById(R.id.radioPortrait);
            RadioButton rb4 = (RadioButton) getActivity().findViewById(R.id.radioLandscape);
            CheckBox cb1 = (CheckBox) getActivity().findViewById(R.id.cbStorage);
            CheckBox cb2 = (CheckBox) getActivity().findViewById(R.id.cbAdb);
            if (rb1.isChecked()) { // If button 1 is checked set device mode to phone
                g.setdevice("phone");
            }
            if (rb2.isChecked()) {
                g.setdevice("tablet");
            }
            if (rb3.isChecked()) { // If button 3 is checked set to portrait mode.
                g.setrotate("portrait");
            }
            if (rb4.isChecked()) {
                g.setrotate("landscape");
            }
            if (cb1.isChecked()) { // If button 3 is checked set to portrait mode.
                storage = 1;
            }
            if (cb2.isChecked()) {
                adb = 1;
            }
        }
    };

    public void checkjson() {

        File myFile = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "/ChromeAPKS/" + g.getSelectedAppName() + "/manifest.json");
        if (myFile.exists())
            myFile.delete();

        File myFile2 = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "/ChromeAPKS/" + g.getSelectedAppName() + ".zip");
        if (myFile2.exists())
            myFile2.delete();
    }

    public void makeManifest() {
        try {
            checkjson();
            FileWriter fbw = new FileWriter(Environment.getExternalStorageDirectory().toString() + File.separator + "/ChromeAPKS/" + g.getSelectedAppName() + "/manifest.json", true); //the true will append the new data
            fbw.write("{\n");
            fbw.write("  \"app\": {\n");
            fbw.write("    \"background\": {\n");
            fbw.write("      \"page\": \"app_main.html\"\n");
            fbw.write("    }\n");
            fbw.write("  },\n");
            fbw.write("  \"arc_metadata\": {\n");
            fbw.write("    \"apkList\": [\n");
            fbw.write("      \"app-release\"\n");
            fbw.write("    ],\n");
            fbw.write("    \"enableExternalDirectory\": false,\n");
            fbw.write("    \"formFactor\": \"");
            fbw.write(g.getdevice());
            fbw.write("\",\n");
            fbw.write("    \"name\": \"");
            fbw.write(g.getPackageName());
            fbw.write("\",\n");
            fbw.write("    \"orientation\": \"");
            fbw.write(g.getrotate());
            fbw.write("\",\n");
            fbw.write("\"packageName\": \"");
            fbw.write(g.getPackageName());
            fbw.write("\",\n");
            fbw.write("    \"useGoogleContactsSyncAdapter\": false,\n");
            fbw.write("    \"usePlayServices\": [\n");
            fbw.write("      \"gcm\"\n");
            if(storage == 0 && adb == 0) {
                fbw.write("    ]\n");
            }
            if(storage == 1 || adb == 1) {
                fbw.write("    ],\n");
            }
            if(storage == 1) {
                fbw.write("\"enableExternalDirectory\": true");
                if(adb == 1) {
                    fbw.write(",\n");
                }
                if(adb == 0) {
                    fbw.write("\n");
                }
            }
            if(adb == 1) {
                fbw.write("\"enableAdb\": true\n");
            }
            fbw.write("  },\n");
            fbw.write("  \"default_locale\": \"en\",\n");
            fbw.write("  \"icons\": {\n");
            fbw.write("    \"16\": \"icon.png\",\n");
            fbw.write("    \"128\": \"icon.png\"\n");
            fbw.write("  },\n");
            fbw.write("  \"import\": [\n");
            fbw.write("    {\n");
            fbw.write("      \"id\": \"mfaihdlpglflfgpfjcifdjdjcckigekc\"\n");
            fbw.write("    }\n");
            fbw.write("  ],\n");
            fbw.write("  \"manifest_version\": 2,\n");
            fbw.write("  \"name\": \"");
            fbw.write(g.getSelectedAppName());
            fbw.write("\",\n");
            fbw.write("  \"oauth2\": {\n");
            fbw.write("    \"client_id\": \"133701689125-jj0hr4gb0ff4ulsbrn0uk2i4th946d4c.apps.googleusercontent.com\",\n");
            fbw.write("    \"scopes\": []\n");
            fbw.write("  },\n");
            fbw.write("  \"offline_enabled\": true,\n");
            fbw.write("  \"permissions\": [\n");
            fbw.write("    \"gcm\",\n");
            fbw.write("    {\n");
            fbw.write("      \"socket\": [\n");
            fbw.write("        \"tcp-connect\",\n");
            fbw.write("        \"tcp-listen\",\n");
            fbw.write("        \"udp-bind\",\n");
            fbw.write("        \"udp-send-to\",\n");
            fbw.write("        \"resolve-host\"\n");
            fbw.write("      ]\n");
            fbw.write("    },\n");
            fbw.write("    \"unlimitedStorage\",\n");
            fbw.write("    \"notifications\",\n");
            fbw.write("    \"clipboardRead\",\n");
            fbw.write("    {\n");
            fbw.write("      \"fileSystem\": [\n");
            if (storage == 0){
                fbw.write("        \"write\"\n");
            }
            if (storage == 1){
                fbw.write("        \"write\", \"directory\"\n");
            }
            fbw.write("      ]\n");
            fbw.write("    },\n");
            fbw.write("    \"https://clients2.google.com/\",\n");
            fbw.write("    \"videoCapture\",\n");
            fbw.write("    \"clipboardWrite\",\n");
            fbw.write("    \"identity.email\",\n");
            fbw.write("    \"alarms\",\n");
            fbw.write("    \"storage\",\n");
            fbw.write("    \"identity\",\n");
            fbw.write("    \"audioCapture\"\n");
            fbw.write("  ],\n");
            fbw.write("  \"requirements\": {\n");
            fbw.write("    \"3D\": {\n");
            fbw.write("      \"features\": [\n");
            fbw.write("        \"webgl\"\n");
            fbw.write("      ]\n");
            fbw.write("    }\n");
            fbw.write("  },\n");
            fbw.write("  \"update_url\": \"https://localhost\",\n");
            fbw.write("  \"version\": \"1337\"\n");
            fbw.write("}");
            fbw.close();
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }

        archiveDir(Environment.getExternalStorageDirectory() + "/ChromeAPKS/" + g.getSelectedAppName());
    }

    private void archiveDir(String path) {
        try {

            // Initiate ZipFile object with the path/name of the zip file.
            String zipname = g.getSelectedAppName();

            ZipFile zipFile = new ZipFile(Environment.getExternalStorageDirectory() + "/ChromeAPKS/" + zipname + ".zip");

            // Folder to add
            String folderToAdd = path;

            // Initiate Zip Parameters which define various properties such
            // as compression method, etc.
            ZipParameters parameters = new ZipParameters();

            // set compression method to store compression
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

            // Set the compression level
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

            // Add folder to the zip file
            zipFile.addFolder(folderToAdd, parameters);

            ArrayList filesToAdd = new ArrayList();
            filesToAdd.add(new File(path + "app_main.html"));
            filesToAdd.add(new File(path + "icon.png"));

            zipFile.addFiles(filesToAdd, parameters);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void DeleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                DeleteRecursive(child);

        fileOrDirectory.delete();
    }

    public void cleanUp() { // Clean up directories that are not needed after zipping chrome app

        File appFolder = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "/ChromeAPKS/" + g.getSelectedAppName());
        File apkFolder = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "/ChromeAPKS/Pulled");
        DeleteRecursive(appFolder);
        DeleteRecursive(apkFolder);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File dir = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "ChromeAPKS" );
            Uri contentUri = Uri.fromFile(dir);
            mediaScanIntent.setData(contentUri);
            getActivity().sendBroadcast(mediaScanIntent);
        }
        else
        {
            getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }

    }


    /**
     * Called whenever the wizard proceeds to the next step or goes back to the previous step
     */

    @Override
    public void onExit(int exitCode) {
        switch (exitCode) {
            case WizardStep.EXIT_NEXT:
                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() { //Start progress dialog and run task in background

                    @Override
                    protected void onPreExecute() {
                        pd = new ProgressDialog(getActivity());
                        pd.setTitle("Processing...");
                        pd.setMessage("Please wait.");
                        pd.setCancelable(false);
                        pd.setIndeterminate(true);
                        pd.show();
                    }

                    @Override
                    protected Void doInBackground(Void... arg0) {
                        makeManifest(); // Generate Chrome manifest.json
                        cleanUp();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        if (pd != null) {
                            pd.dismiss();
                        }
                    }

                };
                task.execute((Void[]) null);
                bindDataFields();
                break;
            case WizardStep.EXIT_PREVIOUS:
                //Do nothing...
                break;
        }
    }

    private void bindDataFields() {


    }
}
