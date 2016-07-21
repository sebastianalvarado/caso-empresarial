package cl.inacap.unidad1.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cl.inacap.unidad1.adapter.PedidosAdapter;
import cl.inacap.unidad1.adapter.ProductosAdapter;
import cl.inacap.unidad1.adapter.RutasAdapter;
import cl.inacap.unidad1.clases.Cliente;
import cl.inacap.unidad1.clases.DirectionsJSONParser;
import cl.inacap.unidad1.clases.Pedido;
import cl.inacap.unidad1.clases.Producto;

//esta es la actividad de las rutas del vendedor
public class RutasActivity extends FragmentActivity implements LocationListener {

    //asigno las variables globales
    private GoogleMap mMap;
    private LatLng mDestino = null;
    private LatLng mOrigen = null;
    private ArrayList<Pedido> pedidos = null;
    private LocationManager locationManager = null;
    private Polyline mPolyline = null;
    private ArrayAdapter<Pedido> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutas);
        ListView lv_rutas = (ListView)findViewById(R.id.lv_rutas);



        //inicializamos el mapa de google
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.rutas_map))
                .getMap();

        //se agregan las direcciones encontradas en la lista
        pedidos = new Pedido().listaPedidosPorVendedorSinEntregar();
        adapter = new RutasAdapter(this,pedidos);
        lv_rutas.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lv_rutas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Pedido pedido = (Pedido)parent.getAdapter().getItem(position);
                LatLng coordenada = new LatLng(pedido.latitud_pedido,pedido.longitud_pedido);
                //al seleccionar una direccion de la lista nos dirige al marcador de la direccion
                mMap.moveCamera(CameraUpdateFactory.newLatLng(coordenada));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }
        });


        //se comprueban los permisos del sistema para poder obtener la localidad actual del dispositivo
        if (ActivityCompat.checkSelfPermission(RutasActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(RutasActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(RutasActivity.this,R.string.sin_permisos_localidad,Toast.LENGTH_LONG);
        }

        //permite obtener la localidad actual del dispositivo
        mMap.setMyLocationEnabled(true);
        //setea el objeto de localizacion
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //si este esta disponible, se dirige a esta direccion
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location != null){
            LatLng origin = new LatLng(location.getLatitude(),location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }
            else
            {
                Toast.makeText(getApplicationContext(),R.string.localidad_no_disponible,Toast.LENGTH_SHORT);
            }
        }

        //aqui seteamos si el usuario presiona el boton para marcar la localidad actual
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                //se validan nuevamente los permisos si el boton es pulsado
                if (ActivityCompat.checkSelfPermission(RutasActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(RutasActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(RutasActivity.this,R.string.sin_permisos_localidad,Toast.LENGTH_LONG);
                }
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    //si no esta disponible genera una alerta permitiendo habilitar el servicio de localidad
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    Intent gpsOptionsIntent = new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    RutasActivity.this.startActivity(gpsOptionsIntent);
                                    break;
                            }
                        }
                    };
                    //si presiona si, se accedera al menu de localidad, si preciosa no o se devuelve, no ocurre nada
                    AlertDialog.Builder builder = new AlertDialog.Builder(RutasActivity.this);
                    builder.setMessage(R.string.debe_activar_gps + ".\n" + R.string.desea_activar);
                    builder.setNegativeButton(R.string.no, null)
                            .setPositiveButton(R.string.si, dialogClickListener);
                    builder.show();
                    return false;
                }
                else {
                    //si esta habilitado hara un seguimiento del movimiento
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 0, RutasActivity.this);
                    return false;
                }
            }
        });

        //si existen pedidos creara las marcas en el mapa de cada una de las direcciones
        if (pedidos.size() > 0) {
            for (Pedido item :
                    pedidos) {

                Cliente cliente = new Cliente();
                Producto producto = new Producto ();
                cliente.ObtenerCliente(item.id_cliente);
                producto.ObtenerProducto(item.id_producto);
                LatLng point = new LatLng(item.latitud_pedido, item.longitud_pedido);
                mMap.addMarker(new MarkerOptions()
                        .position(point)
                        .title(item.direccion_pedido)
                        .snippet(String.valueOf(cliente.nombre_cliente + " : " + producto.nombre_producto + " x" + item.cantidad_producto)));
            }

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker arg0) {
                        //si la ruta no esta vacia, limpiara la marca del mapa
                        if(mPolyline != null)
                            mPolyline.remove();
                        //si no esta disponible la localizacion del dispositivo no hara nada
                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                            return true;
                        //se consultan los permisos nuevamente, ya que la marca se hace desde
                        //la localidad del dispositivo hasta la marca pulsada
                        if (ActivityCompat.checkSelfPermission(RutasActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(RutasActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(RutasActivity.this,R.string.sin_permisos_localidad,Toast.LENGTH_LONG);
                            return true;
                        }
                        // obtiene la localidad actual del gps
                        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        //se setea la localidad de origen y de destino
                        mOrigen = new LatLng(location.getLatitude(),location.getLongitude());
                        mDestino = arg0.getPosition();
                        // obtiene las direcciones desde googleapi
                        String url = getDirectionsUrl(mOrigen, mDestino);
                        DownloadTask downloadTask = new DownloadTask();
                        // empieza a descargar la informacion de la googleapi
                        downloadTask.execute(url);
                        //se dirige la camara al punto de destino
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(mDestino));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                        return false;
                    }
                });
        }


    }


    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // origen de la ruta
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // destno de la ruta
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // dice si el sensor esta habilitado
        String sensor = "sensor=true";

        // construye los parametros para la consulta
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // se setea el formato de salida
        String output = "json";

        // construye la url del servicio
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    // metodo que descarga la informacion de la url en json
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // crea la conexion http para comunicarse por la url
            urlConnection = (HttpURLConnection) url.openConnection();

            // conecta la url
            urlConnection.connect();

            // se lee la informacion obtenida
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception dwnlng url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

// esta clase descargara los datos desde la direccion generada
private class DownloadTask extends AsyncTask<String, Void, String> {

    // descarga la informacion
    @Override
    protected String doInBackground(String... url) {

        String data = "";

        try{
            data = downloadUrl(url[0]);
        }catch(Exception e){
            Log.d("Background Task",e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        ParserTask parserTask = new ParserTask();

        // se executa la transformacion del archivo
        parserTask.execute(result);

    }
}

// esta clase transforma los datos json */
private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try{
            jObject = new JSONObject(jsonData[0]);
            DirectionsJSONParser parser = new DirectionsJSONParser();

            routes = parser.parse(jObject);
        }catch(Exception e){
            e.printStackTrace();
        }
        return routes;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        ArrayList points = null;
        PolylineOptions lineOptions = null;
        // explora las rutas
        for(int i=0;i<result.size();i++){
            points = new ArrayList();
            lineOptions = new PolylineOptions();

            List<HashMap<String, String>> path = result.get(i);

            for(int j=0;j <path.size();j++){
                HashMap<String,String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            // agrega todos los puntos para crear la ruta
            lineOptions.addAll(points);
            lineOptions.width(5);
            lineOptions.color(Color.BLUE);

        }

        // dibuja el polyline en el mapa de la aplicacion
        mPolyline = mMap.addPolyline(lineOptions);
    }
}

    @Override
    public void onLocationChanged(Location location) {
        //cuando cambie la localidad se centra la camara
        mOrigen = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mOrigen));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}