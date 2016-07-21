package cl.inacap.unidad1.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cl.inacap.unidad1.clases.GeocodeJSONParser;
import cl.inacap.unidad1.clases.Pedido;

public class MapaActivity extends Activity {

    static LatLng latLng = null;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        final EditText txt_direccion_pedido = (EditText) findViewById(R.id.txt_direccion_pedido);
        Button btn_buscar_direccion = (Button) findViewById(R.id.btn_buscar_direccion);
        Button btn_asignar_direccion_pedido = (Button) findViewById(R.id.btn_asignar_direccion_pedido);


        Bundle bundle = null;
        String accion = "";
        Pedido pedido = null;
        if (getIntent().hasExtra("extras")) {
            bundle = getIntent().getExtras().getBundle("extras");
            accion = bundle.getString("accion");
            pedido = (Pedido) bundle.getSerializable("pedido");
        }


        //agrega los valores que se devolveran al pedido
        btn_asignar_direccion_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] direccion = new String[3];
                if (txt_direccion_pedido.getText().toString().length() > 0 && latLng != null) {
                    direccion[0] = String.valueOf(latLng.longitude);
                    direccion[1] = String.valueOf(latLng.latitude);
                    direccion[2] = txt_direccion_pedido.getText().toString();

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("mapapedido", direccion);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();

                } else {
                    return;
                }
            }
        });

        // obtiene la direccion buscada
        btn_buscar_direccion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String location = txt_direccion_pedido.getText().toString();

                if (location == null || location.equals("")) {
                    Toast.makeText(getBaseContext(), R.string.lugar_no_ingresado, Toast.LENGTH_SHORT).show();
                    return;
                }

                String url = "https://maps.googleapis.com/maps/api/geocode/json?";

                try {
                    // encoding los caracteres especiales
                    location = URLEncoder.encode(location, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String address = "address=" + location;

                String sensor = "sensor=false";

                // se genera la url
                url = url + address + "&" + sensor;

                //se ejecuta la consula de la direccion
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);
            }
        });

        //se setea el mapa
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        //si el pedido esta lleno se dirige a la ubicacion seleccionada dejando la marca
        if(pedido != null){
            LatLng latlng_pedido = new LatLng(pedido.latitud_pedido, pedido.longitud_pedido);
            txt_direccion_pedido.setText(pedido.direccion_pedido);
            if(latlng_pedido != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng_pedido));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng_pedido, 15));
            }
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // crea la conexion del url
            urlConnection = (HttpURLConnection) url.openConnection();

            // conecta la url
            urlConnection.connect();

            // lee la data de la url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            br.close();

        } catch (Exception e) {
            Log.d("Excp while dwnlding url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }

    // esta clase descargara los datos desde la direccion generada
    private class DownloadTask extends AsyncTask<String, Integer, String> {

        String data = null;

        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {

            ParserTask parserTask = new ParserTask();

            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Geocoding Places in non-ui thread
     */
    class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            GeocodeJSONParser parser = new GeocodeJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                // obtiene los datos trasnpasados desde un arraylist
                places = parser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {

            // limpia los marcadores
            mMap.clear();

            for (int i = 0; i < list.size(); i++) {

                // crea un marcador
                MarkerOptions markerOptions = new MarkerOptions();

                // obtiene los lugares para guardarlos en una lista
                HashMap<String, String> hmPlace = list.get(i);

                //obtiene la latitud del lugar
                double lat = Double.parseDouble(hmPlace.get("lat"));

                // obtiene la longitud del lugar
                double lng = Double.parseDouble(hmPlace.get("lng"));

                // obtiene el nombre
                String name = hmPlace.get("formatted_address");

                latLng = new LatLng(lat, lng);

                // setea la posicion del lugar
                markerOptions.position(latLng);

                // setea el titulo del menu
                markerOptions.title(name);

                // agrega la marca al mapa
                mMap.addMarker(markerOptions);


                if (i == 0) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                }
            }
        }
    }

}