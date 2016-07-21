package cl.inacap.unidad1.clases;


import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import cl.inacap.unidad1.activity.R;

public class Pedido implements Serializable {

    public int id_pedido;
    public int id_cliente;
    public int id_producto;
    public String vendedor;
    public int cantidad_producto;
    public String fecha_pedido;
    public Double precio_pedido;
    public boolean estado_pedido;
    public double longitud_pedido;
    public double latitud_pedido;
    public String direccion_pedido;


    //nombre de las columnas de la tabla
    private String COL_ID_PEDIDO = "id_pedido";
    private String COL_ID_CLIENTE = "id_cliente";
    private String COL_ID_PRODUCTO = "id_producto";
    private String COL_VENDEDOR = "vendedor";
    private String COL_CANTIDAD_PRODUCTO = "cantidad_producto";
    private String COL_FECHA_PEDIDO = "fecha_pedido";
    private String COL_PRECIO_PEDIDO = "precio_pedido";
    private String COL_ESTADO_PEDIDO= "estado_pedido";

    private String COL_LONGITUD_PEDIDO= "longitud_pedido";
    private String COL_LATITUD_PEDIDO= "latitud_pedido";
    private String COL_DIRECCION_PEDIDO= "direccion_pedido";
    //se juntan las columnas para generar los query con todas las columnas
    private String[] columnas = {
            this.COL_ID_PEDIDO,
            this.COL_ID_CLIENTE,
            this.COL_ID_PRODUCTO,
            this.COL_VENDEDOR,
            this.COL_CANTIDAD_PRODUCTO,
            this.COL_FECHA_PEDIDO,
            this.COL_PRECIO_PEDIDO,
            this.COL_ESTADO_PEDIDO,
            this.COL_LONGITUD_PEDIDO,
            this.COL_LATITUD_PEDIDO,
            this.COL_DIRECCION_PEDIDO
    };
    //se genera el nombre de la tabla para las llamadas a query
    private String nombreTabla = "pedido";


    public String toString()
    {
        return String.valueOf(this.id_pedido) + " : "
                + "C: " + String.valueOf(this.id_cliente)
                + "; P: " + String.valueOf(this.id_producto)
                + "\nUnd: " + String.valueOf(this.cantidad_producto)
                + "; V: " + String.valueOf(precio_pedido)
                + "; Fecha: " + this.fecha_pedido
                + "; (" + (this.estado_pedido ? obtenerRecursoString(R.string.entregado)
                                            : obtenerRecursoString(R.string.no_entregado) ) + ")";
    }

    //funcion para transformar un cursor a la clase tipo
    private Pedido cursorToPedido(Cursor cursor) {

        Pedido pedido = new Pedido();
        pedido.id_pedido = cursor.getInt(cursor.getColumnIndex(this.COL_ID_PEDIDO));
        pedido.id_cliente = cursor.getInt(cursor.getColumnIndex(this.COL_ID_CLIENTE));
        pedido.id_producto = cursor.getInt(cursor.getColumnIndex(this.COL_ID_PRODUCTO));
        pedido.vendedor = cursor.getString(cursor.getColumnIndex(this.COL_VENDEDOR));
        pedido.cantidad_producto = cursor.getInt(cursor.getColumnIndex(this.COL_CANTIDAD_PRODUCTO));
        pedido.fecha_pedido = cursor.getString(cursor.getColumnIndex(this.COL_FECHA_PEDIDO));
        pedido.precio_pedido = cursor.getDouble(cursor.getColumnIndex(this.COL_PRECIO_PEDIDO));
        //en la base de datos el estado es int, por lo tanto se transforma el valor de la clase (booleano) con su equivalente en int
        pedido.estado_pedido = cursor.getInt(cursor.getColumnIndex(this.COL_ESTADO_PEDIDO)) == 1 ? true : false;

        pedido.longitud_pedido = cursor.getDouble(cursor.getColumnIndex(this.COL_LONGITUD_PEDIDO));
        pedido.latitud_pedido = cursor.getDouble(cursor.getColumnIndex(this.COL_LATITUD_PEDIDO));
        pedido.direccion_pedido = cursor.getString(cursor.getColumnIndex(this.COL_DIRECCION_PEDIDO));

        return pedido;


    }

    //lista de todos los pedidos
    public ArrayList<Pedido> listaPedidos()
    {
        try {
            ArrayList<Pedido> pedidos = new ArrayList<Pedido>();
            //se genera la query para la consulta de todos los datos
            Cursor cursor = OperacionesBaseDatos.obtenerInstancia().query(this.nombreTabla,this.columnas , null, null, null, null, null);
            cursor.moveToFirst();
            //recorre el restultado de los datos
            while (!cursor.isAfterLast()) {
                //utiliza la funcion para transformar el cursor en la clase
                Pedido pedido = cursorToPedido(cursor);
                pedidos.add(pedido);
                cursor.moveToNext();
            }
            cursor.close();
            return pedidos;

        }
        catch (Exception e)
        {
            Log.e("SegundaAplicacion",e.toString());
            e.printStackTrace();
            return null;
        }
    }

    //lista de todos los pedidos del vendedor
    public ArrayList<Pedido> listaPedidosPorVendedor()
    {
        try {
        ArrayList<Pedido> pedidos = new ArrayList<Pedido>();
        //se genera la consulta se los pedidos por cliente
        Cursor cursor = OperacionesBaseDatos.obtenerInstancia().query(this.nombreTabla,this.columnas , this.COL_VENDEDOR + " = '"+ OperacionesBaseDatos.login_vendedor + "'", null, null, null, null);
        cursor.moveToFirst();
        //se recorre el cursor
        while (!cursor.isAfterLast()) {
            //se utiliza la funcion para pasar de cursor a la clase
            Pedido pedido = cursorToPedido(cursor);
            pedidos.add(pedido);
            cursor.moveToNext();
        }
        cursor.close();
        return pedidos;

    }
    catch (Exception e)
    {
        e.printStackTrace();
        return null;
    }
    }

    //lista de todos los pedidos del vendedor que aun no se entregan
    public ArrayList<Pedido> listaPedidosPorVendedorSinEntregar()
    {
        try {
            ArrayList<Pedido> pedidos = new ArrayList<Pedido>();
            //se genera la consulta se los pedidos por cliente
            Cursor cursor = OperacionesBaseDatos.obtenerInstancia().query(this.nombreTabla,this.columnas ,
                    this.COL_VENDEDOR + " = '"+ OperacionesBaseDatos.login_vendedor + "'"
                    + " AND " + this.COL_ESTADO_PEDIDO + " = 0", null, null, null, null);
            cursor.moveToFirst();
            //se recorre el cursor
            while (!cursor.isAfterLast()) {
                //se utiliza la funcion para pasar de cursor a la clase
                Pedido pedido = cursorToPedido(cursor);
                pedidos.add(pedido);
                cursor.moveToNext();
            }
            cursor.close();
            return pedidos;

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    //lista de los pedidos por cliente
    public ArrayList<Pedido> listaPedidosPorCliente(int id_cliente)
    {
        try {
            ArrayList<Pedido> pedidos = new ArrayList<Pedido>();
            //se genera la consulta se los pedidos por cliente
            Cursor cursor = OperacionesBaseDatos.obtenerInstancia().query(this.nombreTabla,this.columnas , this.COL_ID_CLIENTE + " = "+ id_cliente, null, null, null, null);
            cursor.moveToFirst();
            //se recorre el cursor
            while (!cursor.isAfterLast()) {
                //se utiliza la funcion para pasar de cursor a la clase
                Pedido pedido = cursorToPedido(cursor);
                pedidos.add(pedido);
                cursor.moveToNext();
            }
            cursor.close();
            return pedidos;

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


    //Agrega un nuevo pedido ('C'RUD)
    public String AgregarPedido() {
        try {
            //se utiliza un contenedor de valores para la insercion de los datos a la tabla
            ContentValues values = new ContentValues();
            values.put(this.COL_ID_CLIENTE, this.id_cliente);
            values.put(this.COL_ID_PRODUCTO, (this.id_producto));
            values.put(this.COL_VENDEDOR, (OperacionesBaseDatos.login_vendedor));
            values.put(this.COL_CANTIDAD_PRODUCTO, this.cantidad_producto);
            values.put(this.COL_FECHA_PEDIDO, (this.fecha_pedido));
            values.put(this.COL_PRECIO_PEDIDO, this.precio_pedido);
            //se trasnforma el booleando a int que es el tipo de datos de la base de datos
            values.put(this.COL_ESTADO_PEDIDO, (this.estado_pedido ? 1 : 0));

            values.put(this.COL_LONGITUD_PEDIDO, this.longitud_pedido);
            values.put(this.COL_LATITUD_PEDIDO, this.latitud_pedido);
            values.put(this.COL_DIRECCION_PEDIDO, this.direccion_pedido);

            //se genera la query de inserción reconociendo errores
            long insert = OperacionesBaseDatos.escribirInstancia().insertOrThrow(this.nombreTabla, null, values);
            if (insert > 0){
                //si la inserción ha sido con exito, se registra el id del pedido en la clase
                this.id_pedido = Integer.parseInt("" + insert);
                return obtenerRecursoString(R.string.pedido_agrega_exito);
            }
            else
                return obtenerRecursoString(R.string.pedido_agrega_error);

        } catch (Exception e) {
            e.printStackTrace();
            return obtenerRecursoString(R.string.pedido_agrega_error_exp);
        }
    }

    public String ModificarPedido() {
        try {
            //se crea un contenedor para los valores que se modificaran en la base de datos
            ContentValues values = new ContentValues();
            values.put(this.COL_ID_CLIENTE, this.id_cliente);
            values.put(this.COL_ID_PRODUCTO, this.id_producto);
            values.put(this.COL_CANTIDAD_PRODUCTO, this.cantidad_producto);
            values.put(this.COL_FECHA_PEDIDO, this.fecha_pedido);
            values.put(this.COL_PRECIO_PEDIDO, this.precio_pedido);
            //se trasnforma el booleando a int que es el tipo de datos de la base de datos
            values.put(this.COL_ESTADO_PEDIDO, this.estado_pedido ? 1 : 0);


            values.put(this.COL_LONGITUD_PEDIDO, this.longitud_pedido);
            values.put(this.COL_LATITUD_PEDIDO, this.latitud_pedido);
            values.put(this.COL_DIRECCION_PEDIDO, this.direccion_pedido);

            //se genera la consulta de update
            OperacionesBaseDatos.escribirInstancia().update(this.nombreTabla, values, "id_pedido = " + this.id_pedido, null);
            return obtenerRecursoString(R.string.pedido_modifica_exito);

        } catch (Exception e) {
            Log.e("SegundaAplicacion",e.toString());
            e.printStackTrace();
            return obtenerRecursoString(R.string.pedido_modifica_error_exp);
        }

    }

    public String EliminarPedido() {
        return null;
    }


    private String obtenerRecursoString(int recurso)
    {
        return OperacionesBaseDatos.mi_contexto.getResources().getString(recurso);
    }
}
