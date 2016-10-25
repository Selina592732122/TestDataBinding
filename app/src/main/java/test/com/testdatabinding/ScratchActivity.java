package test.com.testdatabinding;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import test.com.testdatabinding.widget.ScratchView;

public class ScratchActivity extends AppCompatActivity {

	private ScratchView mScratchView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scratch);
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		final RadioButton rbRed = (RadioButton) findViewById(R.id.rbRed);
		final RadioButton rbGray = (RadioButton) findViewById(R.id.rbGray);
		final RadioButton rbGreen = (RadioButton) findViewById(R.id.rbGreen);
		mScratchView = (ScratchView) findViewById(R.id.scratchView);
		findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mScratchView.postInvalidate();
			}
		});
		mScratchView.setonEraseListener(new ScratchView.OnEraseListener() {
			@Override
			public void onProgress(int progress) {
				Log.d("onProgress", "progress" + progress);
			}

			@Override
			public void onComplete(View v) {
				Toast.makeText(ScratchActivity.this, "Complete", Toast.LENGTH_SHORT).show();
			}
		});
		SeekBar seekBar = (SeekBar) findViewById(R.id.progressBar);
		seekBar.setMax(100);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
				mScratchView.setEraseSize(i);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int i) {
				if (radioGroup.getCheckedRadioButtonId() == rbRed.getId()) {
					mScratchView.setMaskColor(0xffff0000);
				}
				if (radioGroup.getCheckedRadioButtonId() == rbGray.getId()) {
					mScratchView.setMaskColor(0xffcdcdcd);
				}
				if (radioGroup.getCheckedRadioButtonId() == rbGreen.getId()) {
					mScratchView.setMaskColor(0xff00ff00);
				}
			}
		});
	}
}
