package com.example.jess3e.projet2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.media.FaceDetector;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {
    private static final int MAX_FACES = 10;
    private Bitmap background_image;
    private FaceDetector.Face[] faces;
    private int face_count;
    private PointF tmp_point = new PointF();
    private Paint tmp_paint = new Paint();
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_image_activity);
        Button backButton = (Button)findViewById(R.id.button1);
        image = (ImageView)findViewById(R.id.imageView1);
        updateImage();
        Bitmap tempBitmap = Bitmap.createBitmap(background_image.getWidth(), background_image.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);
        onDraw(tempCanvas);
        image.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                closeActivity(v);
            }
        });
    }

    public void updateImage() {
        // Set internal configuration to RGB_565
        BitmapFactory.Options bitmap_options = new BitmapFactory.Options();
        bitmap_options.inPreferredConfig = Bitmap.Config.RGB_565;
        background_image = BitmapFactory.decodeResource(getResources(), R.drawable.image16, bitmap_options);
        FaceDetector face_detector = new FaceDetector(
                background_image.getWidth(), background_image.getHeight(),
                MAX_FACES);

        faces = new FaceDetector.Face[MAX_FACES];
        // The bitmap must be in 565 format (for now).
        face_count = face_detector.findFaces(background_image, faces);
        Log.d("Face_Detection", "Face Count: " + String.valueOf(face_count));
    }

    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(background_image, 0, 0, null);
        for (int i = 0; i < face_count; i++) {
            FaceDetector.Face face = faces[i];
            tmp_paint.setColor(Color.GREEN);
            tmp_paint.setAlpha(100);
            face.getMidPoint(tmp_point);
            canvas.drawCircle(tmp_point.x, tmp_point.y, face.eyesDistance(),
                    tmp_paint);
        }
    }

    public void closeActivity(View view) {
        finish();
    }
}

