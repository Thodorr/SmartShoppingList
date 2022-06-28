package ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.smartshoppinglist.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import core.Item;

public class ScannerActivity extends AppCompatActivity
{
	public static String RETURN_VALUE = "item";

	private Button btnBack;
	private EditText editName;
	private EditText editPosX;
	private EditText editPosY;

	private SurfaceView surfaceView;
	private BarcodeDetector barcodeDetector;
	private CameraSource cameraSource;
	private static final int REQUEST_CAMERA_PERMISSION = 201;
	//This class provides methods to play DTMF tones
	private ToneGenerator toneGen;
	private TextView barcodeText;
	private String barcodeData;

	private Item scannedItem;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scanner);

		editName = findViewById(R.id.editName);
		editPosX = findViewById(R.id.editPosX);
		editPosY = findViewById(R.id.editPosY);

		btnBack = findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				onBackPressed();
			}
		});

		toneGen = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);

		surfaceView = findViewById(R.id.surfaceView);
		barcodeText = findViewById(R.id.textCode);
	}

	@Override
	public void onBackPressed()
	{
		System.out.println("Back pressed");
		Intent in = new Intent();
		in.putExtra(RETURN_VALUE, scannedItem);
		setResult(Activity.RESULT_OK, in);
		super.onBackPressed();
	}

	private void initialiseDetectorsAndSources()
	{
		//Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

		barcodeDetector = new BarcodeDetector.Builder(this)
				.setBarcodeFormats(Barcode.ALL_FORMATS)
				.build();

		cameraSource = new CameraSource.Builder(this, barcodeDetector)
				.setRequestedPreviewSize(1920, 1080)
				.setAutoFocusEnabled(true) //you should add this feature
				.build();

		surfaceView.getHolder().addCallback(new SurfaceHolder.Callback()
		{
			@Override
			public void surfaceCreated(SurfaceHolder holder)
			{
				try
				{
					if (ActivityCompat.checkSelfPermission(ScannerActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
					{
						cameraSource.start(surfaceView.getHolder());
					}
					else
					{
						ActivityCompat.requestPermissions(ScannerActivity.this, new
								String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
					}

				}
				catch (IOException e)
				{
					e.printStackTrace();
				}


			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
			{
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder)
			{
				cameraSource.stop();
			}
		});


		barcodeDetector.setProcessor(new Detector.Processor<Barcode>()
		{
			@Override
			public void release()
			{
				// Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void receiveDetections(Detector.Detections<Barcode> detections)
			{
				final SparseArray<Barcode> barcodes = detections.getDetectedItems();
				if (barcodes.size() != 0)
				{
					barcodeText.post(new Runnable()
					{
						@Override
						public void run()
						{
							if (barcodes.valueAt(0).email != null)
							{
								barcodeText.removeCallbacks(null);
								barcodeData = barcodes.valueAt(0).email.address;
								barcodeText.setText(barcodeData);
								//toneGen.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
							}
							else
							{
								barcodeData = barcodes.valueAt(0).displayValue;
								barcodeText.setText(barcodeData);
								//toneGen.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
								// TODO: make database entry
								String name = editName.getText().toString();
								name = name.isEmpty() ? "Scan" : name;
								float posX = Float.parseFloat(editPosX.getText().toString());
								float posY = Float.parseFloat(editPosY.getText().toString());

								scannedItem = new Item(name, posX, posY);
							}
						}
					});
				}
			}
		});
	}
}