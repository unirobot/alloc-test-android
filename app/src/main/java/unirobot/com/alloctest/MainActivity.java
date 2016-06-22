package unirobot.com.alloctest;

import android.app.ActivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    final String TAG = getClass().getSimpleName();
    public static final int BLOCK_SIZE = 1024 * 1024 * 10;
    private ArrayList<ByteBuffer> blocks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button allocButton =  (Button) findViewById(R.id.alloc_button);
        allocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allocateBlock();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //consumeMemory();
        updateMemInfo();
    }

    private void consumeMemory() {
        Log.d(TAG,"Start allocating large memory.");
        blocks = new ArrayList<>();
        try {
            for (int count = 0;;count++) {
                allocateBlock();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Allocation failed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void allocateBlock() {
        try {
            blocks.add(ByteBuffer.allocate(BLOCK_SIZE));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Allocation failed.", Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG, getAllocatedMemString() + " allocated.");
        updateMemInfo();
    }

    private String getAllocatedMemString() {
        return humanReadableByteCount(blocks.size() * BLOCK_SIZE);
    }

    private void updateMemInfo() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService( ACTIVITY_SERVICE );
        ActivityManager.MemoryInfo mInfo = new ActivityManager.MemoryInfo ();
        activityManager.getMemoryInfo( mInfo );

        String memoryInfoString = "Memory:\n";
        memoryInfoString += " - allocated: " + getAllocatedMemString() + "\n";
        memoryInfoString += " - mem class: " + activityManager.getMemoryClass() + " MB\n";
        memoryInfoString += " - large mem class: " + activityManager.getLargeMemoryClass() + " MB\n";
        memoryInfoString += " - minfo.availMem: " + humanReadableByteCount(mInfo.availMem) + "\n";
        memoryInfoString += " - minfo.lowMemory: " + mInfo.lowMemory + "\n";
        memoryInfoString += " - minfo.threshold: " + humanReadableByteCount(mInfo.threshold) + "\n";

        TextView mainText = (TextView) findViewById(R.id.main_text);
        mainText.setText(memoryInfoString);
    }

    public static String humanReadableByteCount(long bytes) {
        return humanReadableByteCount(bytes, true);
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format(Locale.getDefault(), "%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
