package mliew.com.quotesgalore;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.nfc.Tag;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.graphics.Typeface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Quotebook extends ActionBarActivity {

    int count = 0;

    private ColorWheel mColorWheel = new ColorWheel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotebook);

        RelativeLayout touch = (RelativeLayout) findViewById(R.id.touch);
        final TextView quoteText = (TextView) findViewById(R.id.quote);
        final TextView personText = (TextView) findViewById(R.id.person);

        TextView textViewCustom = (TextView) findViewById(R.id.quote);
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Berylium.ttf");
        textViewCustom.setTypeface(myCustomFont);

        int color = mColorWheel.getColor();
        touch.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);

        final ArrayList<Quote> quoteList = new ArrayList<Quote>();


        String input = loadJSONFromAsset();
        try {
            JSONObject obj = new JSONObject(input);
            JSONArray jsonArray = obj.getJSONArray("quotes");

            for (int i = 0; i < jsonArray.length(); i++) {
                String quote = jsonArray.getJSONObject(i).getString("quote");
                String name = jsonArray.getJSONObject(i).getString("name");

                Quote statement = new Quote(quote,name);
                quoteList.add(statement);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        touch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean samequote = true;
                while (samequote) {
                    int count = ThreadLocalRandom.current().nextInt(0, quoteList.size());

                    Quote q = quoteList.get(count);

                    if (q.getQuote() != quoteText.getText()) {
                        samequote = false;

                        quoteText.setText(q.getQuote());
                        personText.setText(q.getPerson());
                    }
                }

                // Random Background Color
                int color = mColorWheel.getColor();
                view.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
            }
        });

    }


    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("quotes.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quotebook, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
