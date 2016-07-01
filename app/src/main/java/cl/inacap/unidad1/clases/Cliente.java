package cl.inacap.unidad1.clases;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.Nullable;
import android.view.Display;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Cliente implements Serializable {

    public int id_cliente;
    public String nombre_cliente;
    public boolean estado_cliente;

    //valor string del cliente
    public String toString()
    {
        return String.valueOf(this.id_cliente) + " : " + this.nombre_cliente + " (" + (this.estado_cliente ? "disponible" : "no disponible")+ ")";
    }

    //se traen todos los clientes
    public ArrayList<Cliente> listaClientes()
    {
        ArrayList<Cliente> lista = new ArrayList<Cliente>();

        JSONArray jsonArray = JsonUtil._clientes;
        if(jsonArray != null)
        {
            try {
                //se recorre el json array de clientes para asignarlos al tipo Clientes
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Cliente cliente = new Cliente();
                    cliente.id_cliente = jsonObject.getInt("id_cliente");
                    cliente.nombre_cliente = jsonObject.getString("nombre_cliente");
                    cliente.estado_cliente = jsonObject.getBoolean("estado_cliente");
                    lista.add(cliente);
                }
                return lista;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return lista;
    }

    //se traen todos los clientes disponibles (que no han sido eliminados lógicamente)
    public ArrayList<Cliente> listaClientesDisponibles()
    {


        ArrayList<Cliente> lista = new ArrayList<Cliente>();

        JSONArray jsonArray = JsonUtil._clientes;
        if(jsonArray != null)
        {
            try {
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Cliente cliente = new Cliente();
                    cliente.id_cliente = jsonObject.getInt("id_cliente");
                    cliente.nombre_cliente = jsonObject.getString("nombre_cliente");
                    cliente.estado_cliente = jsonObject.getBoolean("estado_cliente");
                    if(cliente.estado_cliente)
                        lista.add(cliente);
                }
                return lista;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return lista;
    }
    //Agrega un nuevo cliente ('C'RUD)
    public String AgregarCliente() {
        try {
            //se revisa si exiten clientes registrados
            int size = (JsonUtil._clientes != null) ? JsonUtil._clientes.length() : 0;
            JSONObject jsonObject = null;
            if (size == 0) {
                //en caso de no existir se asigna el cliente numero 1
                this.id_cliente = 1;
                jsonObject = new JSONObject();
                jsonObject.put("id_cliente", this.id_cliente);
                jsonObject.put("nombre_cliente", this.nombre_cliente);
                jsonObject.put("estado_cliente", this.estado_cliente);
            } else {
                //en caso de existir, se asigna el id del ultimo cliente +1
                jsonObject = JsonUtil._clientes.getJSONObject(size - 1);
                int id_ultimo_cliente = jsonObject.getInt("id_cliente");
                jsonObject = new JSONObject();
                this.id_cliente = id_ultimo_cliente + 1;
                jsonObject.put("id_cliente", this.id_cliente);
                jsonObject.put("nombre_cliente", this.nombre_cliente);
                jsonObject.put("estado_cliente", this.estado_cliente);
            }
            //se agrega al json array de clientes
            JsonUtil._clientes.put(jsonObject);
            //se escribe en el documento json de clientes
            new JsonUtil().escribirJsonCliente(JsonUtil._clientes);
            return "El cliente se agregó con éxito";

        } catch (JSONException e) {
            e.printStackTrace();
            return "Error al intentar verificar clientes";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al intentar agregar el cliente";

        }
    }


    //Obtiene (lee) un cliente (C'R'UD)
    public Cliente ObtenerCliente(int id_cliente)
    {
        ArrayList<Cliente> lista = this.listaClientes();
        //se recorre la lista en busqueda del cliente
        for(int i = 0; i < lista.size(); i++) {
            if(lista.get(i).id_cliente == id_cliente) {
                //se actualizan los datos del mismo cliente
                this.id_cliente = lista.get(i).id_cliente;
                this.nombre_cliente = lista.get(i).nombre_cliente;
                this.estado_cliente = lista.get(i).estado_cliente;
                return this;
            }
        }
        //en caso de no encontrarlo retorna nulo
        return null;
    }

    //Modifica un cliente (CR'U'D)
    public String ModificarCliente() {
        try {
            //se recorre el json array de clientes en busca del cliente a modificar
            for (int i = 0; i < JsonUtil._clientes.length(); i++) {
                JSONObject obj = JsonUtil._clientes.getJSONObject(i);
                Cliente cliente = new Cliente();
                cliente.id_cliente = obj.getInt("id_cliente");
                cliente.nombre_cliente = obj.getString("nombre_cliente");
                cliente.estado_cliente = obj.getBoolean("estado_cliente");
                if (cliente.id_cliente == this.id_cliente) {
                    //en caso de encontrarlo validara si se han hecho cambios en el objeto
                    if(!cliente.nombre_cliente.equals(this.nombre_cliente) || cliente.estado_cliente != this.estado_cliente){
                        JSONObject object = JsonUtil._clientes.getJSONObject(i);
                        object.remove("estado_cliente");
                        object.remove("nombre_cliente");
                        object.put("estado_cliente",this.estado_cliente);
                        object.put("nombre_cliente",this.nombre_cliente);
                        //si hay cambios se actualizara el jsonobject y se guarda en el documento
                        new JsonUtil().escribirJsonCliente(JsonUtil._clientes);
                        return "El cliente se modificó con éxito";
                    }
                    else
                        return "El cliente no tiene cambios";
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "Error al intentar recuperar el cliente";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al intentar modificar el cliente";
        }

        return "No se modifico el cliente ";
    }

    //Eliminar un cliente (CRU'D') (De manera lógica)
    public String EliminarCliente()
    {
        //pregunta el estado del cliente
        if(this.estado_cliente){
            //si este esta vigente procedera a la eliminación
            this.estado_cliente = false;
            String resultado = this.ModificarCliente();
            if(resultado == "El cliente se modificó con éxito")
                return "El cliente se eliminó con exito  (lógico)";
            else
                return resultado;
        }
        else
            return "El cliente ya ha sido eliminado (lógico)";
    }

}
