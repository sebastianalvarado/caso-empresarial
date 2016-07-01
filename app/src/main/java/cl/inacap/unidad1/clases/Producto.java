package cl.inacap.unidad1.clases;


import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Producto implements Serializable {

    public int id_producto;
    public String nombre_producto;
    public int precio_producto;
    public boolean estado_producto;

    //nombre de las columnas de la tabla
    private String COL_ID_PRODUCTO = "id_producto";
    private String COL_NOMBRE_PRODUCTO = "nombre_producto";
    private String COL_PRECIO_PRODUCTO = "precio_producto";
    private String COL_ESTADO_PRODUCTO = "estado_producto";
    //se juntan las columnas para generar los query con todas las columnas
    private String[] columnas = { COL_ID_PRODUCTO, COL_NOMBRE_PRODUCTO, COL_PRECIO_PRODUCTO, COL_ESTADO_PRODUCTO };
    private String nombreTabla = "producto";

    //forma string del producto
    public String toString()
    {
        return String.valueOf(this.id_producto) + " : " + this.nombre_producto
                + " ($" + this.precio_producto+ " : "
                + (this.estado_producto ? "disponible" : "no disponible" ) +")";
    }

    //funcion para transformar un cursor a la clase tipo
    private Producto cursorToProducto(Cursor cursor) {

        Producto producto = new Producto();
        producto.id_producto = cursor.getInt(cursor.getColumnIndex(COL_ID_PRODUCTO));
        producto.nombre_producto = cursor.getString(cursor.getColumnIndex(COL_NOMBRE_PRODUCTO));
        producto.precio_producto = cursor.getInt(cursor.getColumnIndex(COL_PRECIO_PRODUCTO));
        producto.estado_producto = (cursor.getInt(cursor.getColumnIndex(COL_ESTADO_PRODUCTO))) == 1 ? true : false;

        return producto;


    }

    //se traen todos los Productos
    public ArrayList<Producto> listaProductos()
    {
        try {
            ArrayList<Producto> productos = new ArrayList<Producto>();
            //se genera la query para la consulta de todos los datos
            Cursor cursor = OperacionesBaseDatos.obtenerInstancia().query(this.nombreTabla,columnas , null, null, null, null, null);
            cursor.moveToFirst();
            //recorre el restultado de los datos
            while (!cursor.isAfterLast()) {
                //se transforma el cursor a la clase
                Producto producto = cursorToProducto(cursor);
                productos.add(producto);
                cursor.moveToNext();
            }
            cursor.close();
            return productos;

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    //se traen todos los Productos disponibles (que no han sido eliminados lógicamente)
    public ArrayList<Producto> listaProductosDisponibles()
    {
        try {
            ArrayList<Producto> productos = new ArrayList<Producto>();
            Cursor cursor = OperacionesBaseDatos.obtenerInstancia().query(this.nombreTabla,columnas , COL_ESTADO_PRODUCTO + "= 1 ", null, null, null, null);
            cursor.moveToFirst();
            //recorre el restultado de los datos
            while (!cursor.isAfterLast()) {
                //se transforma el cursor a la clase
                Producto producto = cursorToProducto(cursor);
                productos.add(producto);
                cursor.moveToNext();
            }
            cursor.close();
            return productos;

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


    //Obtiene (lee) un Producto (C'R'UD)
    public Producto ObtenerProducto(int id_producto)
    {
        //ArrayList<Producto> lista = this.listaProductos();
        Cursor cursor = OperacionesBaseDatos.obtenerInstancia().query(this.nombreTabla,columnas , COL_ID_PRODUCTO+ "=" + id_producto, null, null, null, null);
        Producto producto;
        //se consulta si existe un registro
        if(cursor.moveToFirst()) {
            producto = cursorToProducto(cursor);
            this.id_producto = producto.id_producto;
            this.nombre_producto = producto.nombre_producto;
            this.precio_producto = producto.precio_producto;
            this.estado_producto = producto.estado_producto;
            cursor.close();
            return this;
        }
        return null;

    }

    //Eliminar un Producto (CRU'D') (De manera lógica)
    public String EliminarProducto()
    {
        try {
            //pregunta el estado del producto
            if (this.estado_producto) {

                this.estado_producto = false;
                //se transforma modifica el valor del estado para la elikminacion logica del producto
                ContentValues values = new ContentValues();
                values.put(COL_ESTADO_PRODUCTO, this.estado_producto);
                //se genera la query para actualizar el producto
                OperacionesBaseDatos.escribirInstancia().update(this.nombreTabla, values, "id_producto = " + this.id_producto, null);
                return "Se elminó el producto con exito (lógico)";

            }
            else
                return "El producto ya ha sido eliminado (lógico)";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al intentar elminar el producto";
        }
    }

}
