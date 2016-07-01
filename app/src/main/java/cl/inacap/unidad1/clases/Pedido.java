package cl.inacap.unidad1.clases;


import android.content.ContentValues;
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

public class Pedido implements Serializable {

    public int id_pedido;
    public int id_cliente;
    public int id_producto;
    public String vendedor;
    public int cantidad_producto;
    public String fecha_pedido;
    public int precio_pedido;
    public boolean estado_pedido;


    //nombre de las columnas de la tabla
    private String COL_ID_PEDIDO = "id_pedido";
    private String COL_ID_CLIENTE = "id_cliente";
    private String COL_ID_PRODUCTO = "id_producto";
    private String COL_VENDEDOR = "vendedor";
    private String COL_CANTIDAD_PRODUCTO = "cantidad_producto";
    private String COL_FECHA_PEDIDO = "fecha_pedido";
    private String COL_PRECIO_PEDIDO = "precio_pedido";
    private String COL_ESTADO_PEDIDO= "estado_pedido";
    //se juntan las columnas para generar los query con todas las columnas
    private String[] columnas = {
            this.COL_ID_PEDIDO,
            this.COL_ID_CLIENTE,
            this.COL_ID_PRODUCTO,
            this.COL_VENDEDOR,
            this.COL_CANTIDAD_PRODUCTO,
            this.COL_FECHA_PEDIDO,
            this.COL_PRECIO_PEDIDO,
            this.COL_ESTADO_PEDIDO
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
                + "; (" + (this.estado_pedido ? "Entregado" : "No Entregado" ) + ")";
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
        pedido.precio_pedido = cursor.getInt(cursor.getColumnIndex(this.COL_PRECIO_PEDIDO));
        //en la base de datos el estado es int, por lo tanto se transforma el valor de la clase (booleano) con su equivalente en int
        pedido.estado_pedido = cursor.getInt(cursor.getColumnIndex(this.COL_ESTADO_PEDIDO)) == 1 ? true : false;

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
            //se genera la query de inserción reconociendo errores
            long insert = OperacionesBaseDatos.escribirInstancia().insertOrThrow(this.nombreTabla, null, values);
            if (insert > 0){
                //si la inserción ha sido con exito, se registra el id del pedido en la clase
                this.id_pedido = Integer.parseInt("" + insert);
                return "Pedido agregado con exito";
            }
            else
                return "Error al intentar agregar pedido";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error al intentar agregar un predido";
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
            //se genera la consulta de update
            OperacionesBaseDatos.escribirInstancia().update(this.nombreTabla, values, "id_pedido = " + this.id_pedido, null);
            return "Se modifico el pedido con exito";

        } catch (Exception e) {
            Log.e("SegundaAplicacion",e.toString());
            e.printStackTrace();
            return "Error al intentar recuperar el pedido";
        }

    }

    public String EliminarPedido() {
        return null;
    }

}
