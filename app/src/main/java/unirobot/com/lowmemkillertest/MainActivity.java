package unirobot.com.lowmemkillertest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        consumeMemory();
    }

    private void consumeMemory() {
        Log.d(TAG,"Start allocating large memory.");
        int blockSize = 1024 * 1024;
        ArrayList<ByteBuffer> blocks = new ArrayList<>();
        for (int count = 0;;count++) {
            blocks.add(ByteBuffer.allocate(blockSize));
            Log.d(TAG,String.format("%d blocks allocated.", count));
        }
    }
}
