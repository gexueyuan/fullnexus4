/**
 * 
 */
package com.leoly.fullnexus4.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;

import com.leoly.fullnexus4.R;

/**
 * @author culin003
 * 
 */
public class ChangLogActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changlog);
        EditText text = (EditText) findViewById(R.id.changeLogText);
        text.setEnabled(false);
        text.setFocusable(false);
        text.setTextSize(20);
        text.setBackgroundColor(Color.WHITE);
        text.setTextColor(Color.BLUE);
        InputStream in = ChangLogActivity.class
                .getResourceAsStream("changlog.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String temp = null;
        try
        {
            while ((temp = reader.readLine()) != null)
            {
                text.append(temp);
                text.append("\n");
            }
        } catch (Exception e)
        {
            text.append("Empty Change Log!");
        } finally
        {
            try
            {
                reader.close();
            } catch (IOException e)
            {
            }
        }
    }
}
