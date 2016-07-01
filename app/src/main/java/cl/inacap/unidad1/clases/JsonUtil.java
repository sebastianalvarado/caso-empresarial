package cl.inacap.unidad1.clases;

import android.renderscript.ScriptGroup;
import android.util.JsonWriter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class JsonUtil {

    //Clase de ayuda para lectura y guardado de archivos

    //Se declaran los json array que se utilizaran en el sistema
    public static JSONArray _vendedores;
    public static JSONArray _clientes;
    public static JSONArray _productos;
    public static JSONArray _pedidos;

    //se declaran los archivos para grabar las modificaciones de los componentes
    private static OutputStream _archivo_clientes;
    private static OutputStream _archivo_productos;
    private static OutputStream _archivo_pedidos;




    /*Los siguientes metodos tienen un Exception de tipo IOException y/o JSONException que se
    controlaran en sus respectivos objetos para facilitar el manejo de mensajes
    */

    //Se poblan los json array del sistema
    public void poblarJsonArray(InputStream is_vendedores,
                                InputStream is_clientes,
                                InputStream is_productos,
                                InputStream is_pedidos) throws IOException, JSONException
    {

        JsonUtil._vendedores = this.leerJson(is_vendedores);
        JsonUtil._clientes = this.leerJson(is_clientes);
        JsonUtil._productos = this.leerJson(is_productos);
        JsonUtil._pedidos = this.leerJson(is_pedidos);
    }

    //se asignan los documentos
    public void asignarArchivos(OutputStream os_clientes,
                                OutputStream os_productos,
                                OutputStream os_pedidos)
    {
        this._archivo_clientes = os_clientes;
        this._archivo_productos = os_productos;
        this._archivo_pedidos = os_pedidos;
    }

    //Se entrega el json para el json array
    public JSONArray leerJson(InputStream inputStream) throws IOException, JSONException {
        String json = null;
        JSONArray jsonArray = null;

        //se parte leyendo el documento json
        int size = inputStream.available();
        byte[] buffer = new byte[size];
        inputStream.read(buffer);
        inputStream.close();
        json = new String(buffer, "UTF-8");
        Log.d("_STATE leer json", json);
        //una vez leido se asigna al array que se devolverá
        jsonArray = new JSONArray(json);

        return jsonArray;
    }

    //se guarda el json de clientes
    public void escribirJsonCliente(JSONArray clientes) throws IOException {

        //es entrega por consola el json a guardar
        Log.d("_STATE escribir cliente", clientes.toString());
        JsonUtil._archivo_clientes.write(clientes.toString().getBytes());
        JsonUtil._archivo_clientes.close();

    }

    //se guarda el json de productos
    public void escribirJsonProducto(JSONArray productos) throws IOException {

        //es entrega por consola el json a guardar
        Log.d("_STATE escribir prod", productos.toString());
        JsonUtil._archivo_productos.write(productos.toString().getBytes());
    }

    //se guarda el json de pedidos
    public void escribirJsonPedido(JSONArray pedidos) throws IOException {

        //es entrega por consola el json a guardar
        Log.d("_STATE escribir pedidos", pedidos.toString());
        JsonUtil._archivo_pedidos.write(pedidos.toString().getBytes());
        JsonUtil._archivo_pedidos.close();
    }
}
