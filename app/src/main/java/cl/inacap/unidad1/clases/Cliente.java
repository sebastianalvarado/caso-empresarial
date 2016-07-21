package cl.inacap.unidad1.clases;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
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
import android.database.Cursor;
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
import android.util.Log;
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

import cl.inacap.unidad1.activity.R;

public class Cliente implements Serializable {

    public int id_cliente;
    public String nombre_cliente;
    public boolean estado_cliente;

    //nombre de las columnas de la tabla cliente
    private String COL_ID_CLIENTE = "id_cliente";
    private String COL_NOMBRE_CLIENTE = "nombre_cliente";
    private String COL_ESTADO_CLIENTE = "estado_cliente";
    //se juntan las columnas para generar los query con todas las columnas
    private String[] columnas = { this.COL_ID_CLIENTE, this.COL_NOMBRE_CLIENTE, this.COL_ESTADO_CLIENTE };

    //se genera el nombre de la tabla para las llamadas a query
    private String nombreTabla = "cliente";

    //valor string del cliente
    public String toString()
    {
        return String.valueOf(this.id_cliente) + " : "
                + this.nombre_cliente
                + " (" + (this.estado_cliente ? obtenerRecursoString(R.string.disponible)
                                            : obtenerRecursoString(R.string.no_disponible) )+ ")";
    }

    //se traen todos los clientes
    public ArrayList<Cliente> listaClientes()
    {
        try {

            ArrayList<Cliente> clientes = new ArrayList<Cliente>();
            //se consulta a la base de datos todos los clientes existentes
            Cursor cursor = OperacionesBaseDatos.obtenerInstancia().query(this.nombreTabla, this.columnas, null, null, null, null, null);
            //se mueve al primer resultado
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                //pasamos el valor por el lector del cursor
                Cliente cliente = cursorToCliente(cursor);
                clientes.add(cliente);
                //se continua al siguiente resultado
                cursor.moveToNext();
            }
            cursor.close();
            return clientes;

        }
        catch (Exception e)
        {
            Log.e("SegundaAplicacion",e.toString());
            e.printStackTrace();
            return null;
        }
    }


    //funcion para transformar un cursor en un cliente
    private Cliente cursorToCliente(Cursor cursor) {

        Cliente cliente = new Cliente();
        cliente.id_cliente = cursor.getInt(cursor.getColumnIndex(this.COL_ID_CLIENTE));
        cliente.nombre_cliente = cursor.getString(cursor.getColumnIndex(this.COL_NOMBRE_CLIENTE));
        //en la base de datos el estado es int, por lo tanto se transforma el valor del cliente (booleano) a su equivalente en int
        cliente.estado_cliente = (cursor.getInt(cursor.getColumnIndex(this.COL_ESTADO_CLIENTE))) == 1 ? true : false;

        return cliente;


    }

    //Se genera y obtiene la lista de usuarios
    //se traen todos los clientes disponibles (que no han sido eliminados lógicamente) para el pedido
    public ArrayList<Cliente> listaClientesDisponibles(){
        ArrayList<Cliente> clientes = new ArrayList<Cliente>();
        //se genera la consulta a todos los clientes que no han sido eliminados logicamente agregando al query un where
        Cursor cursor = OperacionesBaseDatos.obtenerInstancia().query(this.nombreTabla,this.columnas , this.COL_ESTADO_CLIENTE + "= 1 ", null, null, null, null);
        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Cliente cliente = cursorToCliente(cursor);
                clientes.add(cliente);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return clientes;
    }


    //Agrega un nuevo cliente ('C'RUD)
    public String AgregarCliente() {
        try {
            //con esta consulta buscaremos si existe algun cliente con el mismo nombre para no volver a agregarlo.
            Cursor cursor = OperacionesBaseDatos.obtenerInstancia().query(this.nombreTabla,this.columnas , this.COL_NOMBRE_CLIENTE + "='" + this.nombre_cliente+"'", null, null, null, null);
            Cliente cliente;
            //si existe resultado entrará al cursor indicando que ya existe un cliente con ese nombre
            if(cursor.moveToFirst()) {
                cliente = cursorToCliente(cursor);
                return obtenerRecursoString(R.string.cliente_agrega_existe);
            }
            else {
                //se crea un contenedor de valores para agregarlo a la base de datos
                ContentValues values = new ContentValues();

                values.put(this.COL_NOMBRE_CLIENTE, this.nombre_cliente);
                values.put(this.COL_ESTADO_CLIENTE, (this.estado_cliente ? 1 : 0));
                //se inserta con funcion que arroja errores para evitar problemas
                long insert = OperacionesBaseDatos.escribirInstancia().insertOrThrow(this.nombreTabla,null,values);
                if(insert > 0) {
                    //se entrega el id del cliente para su reconocimiento en los pedidos
                    this.id_cliente = Integer.parseInt("" + insert);
                    return  obtenerRecursoString(R.string.cliente_agrega_exito);
                }
                else
                    return obtenerRecursoString(R.string.cliente_agrega_error);
            }
        } catch (Exception e) {
            Log.e("SegundaAplicacion",e.toString());
            e.printStackTrace();
            return obtenerRecursoString(R.string.cliente_agrega_error_exp);
        }
    }


    //Obtiene (lee) un cliente (C'R'UD)
    public Cliente ObtenerCliente(int id_cliente)
    {
        //(Se mantiene primera solucion al problema)
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
            //al igual que al agregar un nuevo cliente, se verifica que este nombre esta en la base de datos
            Cursor cursor = OperacionesBaseDatos.obtenerInstancia().query(this.nombreTabla,this.columnas , this.COL_NOMBRE_CLIENTE + "='" + this.nombre_cliente+"'", null, null, null, null);
            Cliente cliente;
            //en caso de que el cursor tenga resultado entrará al if indicando que se encontró un cliente con ese nombre
            if(cursor.moveToFirst()) {
                cliente = cursorToCliente(cursor);
                return obtenerRecursoString(R.string.cliente_agrega_existe);
            }
            else {
                //si no se encuentra, se crea el contenedor de datos para enviar una actualizacion
                ContentValues values = new ContentValues();
                values.put(this.COL_NOMBRE_CLIENTE, this.nombre_cliente);
                //en la base de datos el estado es int, por lo tanto se transforma el valor del cliente (booleano) a su equivalente en int
                values.put(this.COL_ESTADO_CLIENTE, this.estado_cliente);

                //se mandan los datos para modificar el registro
                OperacionesBaseDatos.escribirInstancia().update(this.nombreTabla, values, "id_cliente = " + this.id_cliente, null);
                return obtenerRecursoString(R.string.cliente_modifica_exito);
            }
        } catch (Exception e) {
            Log.e("SegundaAplicacion",e.toString());
            e.printStackTrace();
            return obtenerRecursoString(R.string.cliente_modifica_error_exp);
        }
    }

    //Eliminar un cliente (CRU'D') (De manera lógica)
    public String EliminarCliente() {
        try {
            //pregunta el estado del cliente
            if (this.estado_cliente) {

                this.estado_cliente = false;
                ContentValues values = new ContentValues();
                //en la base de datos el estado es int, por lo tanto se transforma el valor del cliente (booleano) a su equivalente en int
                values.put(this.COL_ESTADO_CLIENTE, this.estado_cliente ? 1 : 0);
                //se genera la actualizacion del registro marcando al cliente con estado falso, indicando que no estie
                OperacionesBaseDatos.escribirInstancia().update(this.nombreTabla, values, "id_cliente = " + this.id_cliente, null);
                return obtenerRecursoString(R.string.cliente_elimina_exito);
            }
            else
                return obtenerRecursoString(R.string.cliente_elimina_error);
        } catch (Exception e) {
            Log.e("SegundaAplicacion",e.toString());
            e.printStackTrace();
            return obtenerRecursoString(R.string.cliente_elimina_error_exp);
        }
    }

    private String obtenerRecursoString(int recurso)
    {
        return OperacionesBaseDatos.mi_contexto.getResources().getString(recurso);
    }

}
