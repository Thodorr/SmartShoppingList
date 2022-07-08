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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import core.AppDatabase;
import core.Item;
import core.Supermarket;
import math.Vector2;

public class ScannerActivity extends AppCompatActivity
{
	public static String RETURN_VALUE = "item";

	private Button btnBack;
	private EditText editName;
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

	private boolean scanned;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scanner);

		setTitle("Scan Item");

		scanned = false;

		editName = findViewById(R.id.editName);
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

		initialiseDetectorsAndSources();
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
				.setRequestedPreviewSize(1920, 1440)
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
					barcodeText.post(() -> {
						if (scanned) return;

						barcodeData = barcodes.valueAt(0).displayValue;
						checkForExistence(barcodeData, (eanExists) -> {
							if (eanExists) {
								Toast.makeText(getApplicationContext(), "Product already exists", Toast.LENGTH_SHORT).show();
								Intent intent = new Intent(getApplicationContext(), MainActivity.class);
								startActivity(intent);
							}
						});

						barcodeText.setText(barcodeData);
						toneGen.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
						// TODO: make database entry
						String name = editName.getText().toString();
						name = name.isEmpty() ? "Scan" : name;
						Vector2 pos;
						try
						{
							int shelfNr = Integer.parseInt(editPosY.getText().toString());
							pos = new Supermarket().getPosition(shelfNr);
						}
						catch(NumberFormatException e)
						{
							pos = Vector2.zero;
						}

						scanned = true;
						Intent intent = new Intent(getApplicationContext(), EntryActivity.class);
						intent.putExtra("ean", barcodeData);
						startActivity(intent);
					});
				}
			}
		});
	}

	interface OnEanListener {
		void eanChecked(boolean eanExists);
	}

	private void checkForExistence(String ean, OnEanListener callback) {
		AppDatabase db = AppDatabase.getDb(getApplicationContext());
		AsyncTask.execute(() -> {
			Item existItem = db.itemDao().checkForEan(ean);
			System.out.println(existItem);
			runOnUiThread(() -> callback.eanChecked(existItem != null));
		});
	}
}