package hakaton.haze.android.hackatonapp.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class QRCode extends Activity implements ZXingScannerView.ResultHandler
{
    ZXingScannerView mScannerView;
    LinearLayout scannerLayout;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);

        mScannerView.addView(createLayout());
    }

    public View createLayout()
    {
        scannerLayout = new LinearLayout(this);
        scannerLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(200, 100);
        buttonParams.gravity = Gravity.CENTER;

        return scannerLayout;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        try
        {
            mScannerView.setResultHandler(this);
            mScannerView.setAutoFocus(true);
            mScannerView.startCamera();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        try {
            mScannerView.stopCamera();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void handleResult(final Result rawResult)
    {
        mScannerView.stopCamera();
        System.out.println(rawResult.getText());
        finish();
    }




}
