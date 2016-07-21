package cl.inacap.unidad1.activity;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.internal.fa;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.SimpleFormatter;

import cl.inacap.unidad1.clases.Configuracion;
import cl.inacap.unidad1.clases.GeocodeJSONParser;
import cl.inacap.unidad1.clases.OperacionesBaseDatos;

public class ConfiguracionActivity extends AppCompatActivity {
    TextView txv_convertido = null;
    Configuracion configuracion = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        //se obtienen los valores del layout
        Button btn_configuracion_editar = (Button)findViewById(R.id.btn_configuracion_editar);
        final CheckBox cb_fecha_defecto = (CheckBox) findViewById(R.id.cb_fecha_defecto);
        final RadioGroup rg_radio_fecha = (RadioGroup) findViewById(R.id.rg_radio_fecha);
        final RadioGroup rg_radio_moneda = (RadioGroup) findViewById(R.id.rg_radio_moneda);
        final EditText txt_valor_moneda = (EditText)findViewById(R.id.txt_valor_moneda);


        cb_fecha_defecto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    for (int i = 0; i < rg_radio_fecha.getChildCount(); i++) {
                        View view = rg_radio_fecha.getChildAt(i);
                        view.setEnabled(false);
                    }
                else
                    for (int i = 0; i < rg_radio_fecha.getChildCount(); i++) {
                        View view = rg_radio_fecha.getChildAt(i);
                        view.setEnabled(true);
                    }
            }
        });



        //se llenan los datos de la configuracion
        configuracion = new Configuracion().ObtenerConfiguracion();
        cb_fecha_defecto.setChecked(!configuracion.fecha_personal);
        int id_fecha = getResources().getIdentifier(configuracion.fecha_formato,"id",getPackageName());
        int id_moneda = getResources().getIdentifier(configuracion.moneda_formato,"id",getPackageName());

        RadioButton rb_fecha  = (RadioButton)findViewById(id_fecha);
        rb_fecha.setChecked(true);
        RadioButton rb_moneda  = (RadioButton)findViewById(id_moneda);
        rb_moneda.setChecked(true);
        txt_valor_moneda.setText(configuracion.moneda_valor.toString());

        //se guarda la configuracion
        btn_configuracion_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RadioButton rb_fecha  = (RadioButton)findViewById(rg_radio_fecha.getCheckedRadioButtonId());
                RadioButton rb_moneda = (RadioButton)findViewById(rg_radio_moneda.getCheckedRadioButtonId());

                configuracion.fecha_personal = !cb_fecha_defecto.isChecked();

                //se obtiene el valor del id de la opcion para definir
                String sfecha_formato = getResources().getResourceName(rb_fecha.getId());
                configuracion.fecha_formato = sfecha_formato.substring(sfecha_formato.lastIndexOf("/")+1);
                //se obtiene el valor del id de la opcion para definir
                String smoneda_formato = getResources().getResourceName(rb_moneda.getId());
                configuracion.moneda_formato = smoneda_formato.substring(smoneda_formato.lastIndexOf("/")+1);;

                configuracion.moneda_valor = Double.parseDouble(txt_valor_moneda.getText().toString());

                String resultado = configuracion.ModificarConfiguracion();

                Toast.makeText(getApplicationContext(),resultado,Toast.LENGTH_SHORT).show();

            }
        });

    }


}





