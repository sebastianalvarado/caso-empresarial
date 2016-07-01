package cl.inacap.unidad1.clases;


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

    //forma string del producto
    public String toString()
    {
        return String.valueOf(this.id_producto) + " : " + this.nombre_producto
                + " ($" + this.precio_producto+ " : "
                + (this.estado_producto ? "disponible" : "no disponible" ) +")";
    }


    //se traen todos los Productos
    public ArrayList<Producto> listaProductos()
    {
        ArrayList<Producto> lista = new ArrayList<Producto>();

        JSONArray jsonArray = JsonUtil._productos;
        if(jsonArray != null)
        {
            try {
                //se recorre el json array de Productos para asignarlos al tipo Productos
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Producto producto = new Producto();
                    producto.id_producto = jsonObject.getInt("id_producto");
                    producto.nombre_producto = jsonObject.getString("nombre_producto");
                    producto.precio_producto = jsonObject.getInt("precio_producto");
                    producto.estado_producto = jsonObject.getBoolean("estado_producto");
                    lista.add(producto);
                }
                return lista;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return lista;
    }

    //se traen todos los Productos disponibles (que no han sido eliminados lógicamente)
    public ArrayList<Producto> listaProductosDisponibles()
    {


        ArrayList<Producto> lista = new ArrayList<Producto>();

        JSONArray jsonArray = JsonUtil._productos;
        if(jsonArray != null)
        {
            try {
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Producto producto = new Producto();
                    producto.id_producto = jsonObject.getInt("id_producto");
                    producto.nombre_producto = jsonObject.getString("nombre_producto");
                    producto.precio_producto = jsonObject.getInt("precio_producto");
                    producto.estado_producto = jsonObject.getBoolean("estado_producto");
                    if(producto.estado_producto)
                        lista.add(producto);
                }
                return lista;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return lista;
    }

    //Agrega un nuevo Producto ('C'RUD)
    public String AgregarProducto() {
        try {
            //se revisa si exiten Productos registrados
            int size = (JsonUtil._productos != null) ? JsonUtil._productos.length() : 0;
            JSONObject jsonObject = null;
            if (size == 0) {
                //en caso de no existir se asigna el Producto numero 1
                this.id_producto = 1;
                jsonObject = new JSONObject();
                jsonObject.put("id_producto", this.id_producto);
                jsonObject.put("nombre_producto", this.nombre_producto);
                jsonObject.put("precio_producto", this.precio_producto);
                jsonObject.put("estado_producto", this.estado_producto);
            } else {
                //en caso de existir, se asigna el id del ultimo Producto +1
                jsonObject = JsonUtil._productos.getJSONObject(size - 1);
                int id_ultimo_producto = jsonObject.getInt("id_producto");
                jsonObject = new JSONObject();
                this.id_producto = id_ultimo_producto + 1;
                jsonObject.put("id_producto", this.id_producto);
                jsonObject.put("nombre_producto", this.nombre_producto);
                jsonObject.put("precio_producto", this.precio_producto);
                jsonObject.put("estado_producto", this.estado_producto);
            }
            //se agrega al json array de Productos
            JsonUtil._productos.put(jsonObject);
            //se escribe en el documento json de Productos
            new JsonUtil().escribirJsonProducto(JsonUtil._productos);
            return "El Producto se agregó con éxito";

        } catch (JSONException e) {
            e.printStackTrace();
            return "Error al intentar verificar Productos";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al intentar agregar el Producto";

        }
    }


    //Obtiene (lee) un Producto (C'R'UD)
    public Producto ObtenerProducto(int id_producto)
    {
        ArrayList<Producto> lista = this.listaProductos();
        //se recorre la lista en busqueda del Producto
        for(int i = 0; i < lista.size(); i++) {
            if(lista.get(i).id_producto == id_producto) {
                //se actualizan los datos del mismo Producto
                this.id_producto = lista.get(i).id_producto;
                this.nombre_producto = lista.get(i).nombre_producto;
                this.precio_producto = lista.get(i).precio_producto;
                this.estado_producto = lista.get(i).estado_producto;
                return this;
            }
        }
        //en caso de no encontrarlo retorna nulo
        return null;
    }

    //Modifica un Producto (CR'U'D)
    public String ModificarProducto() {
        try {
            //se recorre el json array de Productos en busca del Producto a modificar
            for (int i = 0; i < JsonUtil._productos.length(); i++) {
                JSONObject obj = JsonUtil._productos.getJSONObject(i);
                Producto producto = new Producto();
                producto.id_producto = obj.getInt("id_producto");
                producto.nombre_producto = obj.getString("nombre_producto");
                producto.precio_producto = obj.getInt("precio_producto");
                producto.estado_producto = obj.getBoolean("estado_producto");
                if (producto.id_producto == this.id_producto) {
                    //en caso de encontrarlo validara si se han hecho cambios en el objeto
                    if(!producto.nombre_producto.equals(this.nombre_producto) || producto.estado_producto != this.estado_producto){
                        JSONObject object = JsonUtil._productos.getJSONObject(i);
                        object.remove("estado_producto");
                        object.remove("nombre_producto");
                        object.remove("precio_producto");
                        object.put("estado_producto",this.estado_producto);
                        object.put("nombre_producto",this.nombre_producto);
                        object.put("precio_producto",this.precio_producto);
                        //si hay cambios se actualizara el jsonobject y se guarda en el documento
                        new JsonUtil().escribirJsonProducto(JsonUtil._productos);
                        return "El Producto se modificó con éxito";
                    }
                    else
                        return "El Producto no tiene cambios";
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "Error al intentar recuperar el Producto";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al intentar modificar el Producto";
        }

        return "No se modifico el Producto ";
    }

    //Eliminar un Producto (CRU'D') (De manera lógica)
    public String EliminarProducto()
    {
        //pregunta el estado del Producto
        if(this.estado_producto){
            //si este esta vigente procedera a la eliminación
            this.estado_producto = false;
            String resultado = this.ModificarProducto();
            if(resultado == "El Producto se modificó con éxito")
                return "El Producto se eliminó con exito  (lógico)";
            else
                return resultado;
        }
        else
            return "El Producto ya ha sido eliminado (lógico)";
    }


}
