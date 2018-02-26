package idv.kuma.app.samplerenderscript;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private static final int LOOP_COUNT = 50000;

    private RenderScript rs;
    private ScriptField_Plus scriptFieldPlus;

    private EditText editText;
    private long spentTime = 0;

    private int currentLoopCount = LOOP_COUNT;
    private int count = 0;
    private int[] countArr = new int[]{ 0 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rs = RenderScript.create(this);
        // Sample 5
//        initRS();

        editText = findViewById(R.id.editText);
        editText.setText(String.valueOf(currentLoopCount));

        FloatingActionButton fabRs = findViewById(R.id.fab_rs);
        fabRs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countArr[0] = 0;
                spentTime = System.currentTimeMillis();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        currentLoopCount = Integer.parseInt(editText.getText().toString());
                        loopRS();
                        spentTime = System.currentTimeMillis() - spentTime;
                        Log.d("SpentTime", "time: " + spentTime);
                        Log.d("Count", "count: " + countArr[0]);
                    }
                }).start();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 0;
                spentTime = System.currentTimeMillis();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        currentLoopCount = Integer.parseInt(editText.getText().toString());
                        loop();
                        spentTime = System.currentTimeMillis() - spentTime;
                        Log.d("SpentTime", "time: " + spentTime);
                        Log.d("Count", "count: " + count);
                    }
                }).start();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    private void initRS() {
        scriptFieldPlus = new ScriptField_Plus(rs, currentLoopCount);
        // Sample 4
    }

    private void loop() {
        // Sample 1
        for (int i = 0; i < currentLoopCount; i++) {
            // Sample 2
            count++;
        }
    }

    private void loopRS() {
        initRS();
        Allocation dataOut = Allocation.createSized(rs, Element.I32(rs), currentLoopCount);
        ScriptC_Compute computeScript = new ScriptC_Compute(rs);
        // Sample 3
//        computeScript.set_count(200);
        count = computeScript.get_count();

        // Sample 4
        Allocation arrAll = Allocation.createSized(rs, Element.I32(rs), 1);
        arrAll.copyFrom(countArr);

        computeScript.bind_keyCount(arrAll);
        computeScript.forEach_root(scriptFieldPlus.getAllocation(), dataOut);

        arrAll.copyTo(countArr);

//        int c = countArr[0];
//        Log.v("CountArr", "arr[0]: " + c);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
