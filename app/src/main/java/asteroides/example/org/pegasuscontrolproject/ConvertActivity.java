package asteroides.example.org.pegasuscontrolproject;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class ConvertActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etDegree;
    private Button btnConvert;
    private TextView tvDegree;
    private Button btnInf;
    Conversion conv = new Conversion();
    private String resFaren;
    public static String celsius;
    String converted;
    SoapPrimitive result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);

        etDegree = findViewById(R.id.etConvert);
        btnConvert = findViewById(R.id.btnConvert);
        tvDegree = findViewById(R.id.tvDegree);
        btnInf = findViewById(R.id.btnInf);

        btnConvert.setOnClickListener(this);
        btnInf.setOnClickListener(this);

    }

    /**
     * Método que es llamado por el hilo asíncrono, que obtiene la respuesta del servicio
     * @param celsius
     * @return
     */
    private String convert(String celsius) {
        /*
        String NAMESPACE = "http://www.w3schools.com/webservices/";
        String URL = "http://www.w3schools.com/webservices/tempconvert.asmx";
        String SOAP_ACTION = "http://www.w3schools.com/webservices/CelsiusToFahrenheit";
        String METHOD_NAME = "CelsiusToFahrenheit";
*/
        String NAMESPACE = "https://www.w3schools.com/xml/";
        String URL = "https://www.w3schools.com/xml/tempconvert.asmx";
        String SOAP_ACTION = "https://www.w3schools.com/xml/CelsiusToFahrenheit";
        String METHOD_NAME = "CelsiusToFahrenheit";

        try{
            SoapObject request =new SoapObject(NAMESPACE,METHOD_NAME);
            SoapSerializationEnvelope serializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            request.addProperty("Celsius",celsius);
            serializationEnvelope.dotNet=true;
            serializationEnvelope.setOutputSoapObject(request);

            HttpTransportSE transport= new HttpTransportSE(URL);
            transport.call(SOAP_ACTION,serializationEnvelope);

            result=(SoapPrimitive)serializationEnvelope.getResponse();
            System.out.println(result.toString());
            resFaren=result.toString();
            Toast.makeText(this, "El resultado es: "+resFaren, Toast.LENGTH_SHORT).show();
            return result.toString();
        }catch(Exception err){
            System.err.println("Error: "+err.toString());
            return null;
        }
    }

    /**
     * Método sobreescrito de la clase OnClickListener que define la lógia que tendrá al presinar
     * cada botón.
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnConvert:
                celsius=etDegree.getText().toString();
                Toast.makeText(this, "Cargando", Toast.LENGTH_LONG).show();

                if (etDegree.getText().toString().equals("")) {
                    Toast.makeText(this, "Favor de ingresar un valor en °Celsius", Toast.LENGTH_SHORT).show();
                } else {
                    Conversion conv = new Conversion();
                    conv.execute();
                }
                break;
            case R.id.btnInf:

                break;
        }
    }

    /**
     * Clase Conversión que es ejectuada en otro hilo diferente al principap
     * para poder realizar peticiones y editar las vistas de UI
     */
    private class Conversion extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            convert(celsius);
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            System.out.println("post");
            System.out.println(resFaren);
            tvDegree.setText(resFaren);
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            tvDegree.setText(resFaren);
        }
    }

}
