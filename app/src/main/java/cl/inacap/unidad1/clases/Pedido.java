package cl.inacap.unidad1.clases;


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
    public int cantidad_producto;
    public String fecha_pedido;
    public int precio_pedido;
    public boolean estado_pedido;

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

    public ArrayList<Pedido> listaPedidos()
    {
        ArrayList<Pedido> lista = new ArrayList<Pedido>();

        JSONArray jsonArray = JsonUtil._pedidos;
        if(jsonArray != null)
        {
            try {
                //se recorre el json array de Productos para asignarlos al tipo pedidos
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Pedido pedido = new Pedido();
                    pedido.id_pedido = jsonObject.getInt("id_pedido");
                    pedido.id_cliente = jsonObject.getInt("id_cliente");
                    pedido.id_producto = jsonObject.getInt("id_producto");
                    pedido.cantidad_producto = jsonObject.getInt("cantidad_producto");
                    pedido.fecha_pedido = jsonObject.getString("fecha_pedido");
                    pedido.precio_pedido = jsonObject.getInt("precio_pedido");
                    pedido.estado_pedido = jsonObject.getBoolean("estado_pedido");
                    lista.add(pedido);
                }
                return lista;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return lista;
    }
    
    public ArrayList<Pedido> listaPedidosPorCliente(int id_cliente)
    {
        ArrayList<Pedido> lista = new ArrayList<>();
        for (Pedido p: this.listaPedidos()) {
            if(p.id_cliente == id_cliente)
                lista.add(p);
        }
        return lista;
    }



    //Agrega un nuevo pedido ('C'RUD)
    public String AgregarPedido() {
        try {
            //se revisa si exiten pedido registrados
            int size = (JsonUtil._pedidos != null) ? JsonUtil._pedidos.length() : 0;
            JSONObject jsonObject = null;
            if (size == 0) {
                //en caso de no existir se asigna el Producto numero 1
                this.id_pedido = 1;
                jsonObject = new JSONObject();
                jsonObject.put("id_pedido ", this.id_pedido );
                jsonObject.put("id_cliente", this.id_cliente);
                jsonObject.put("id_producto", this.id_producto);
                jsonObject.put("cantidad_producto", this.cantidad_producto);
                jsonObject.put("fecha_pedido", this.fecha_pedido);
                jsonObject.put("precio_pedido", this.precio_pedido);
                jsonObject.put("estado_pedido", this.estado_pedido);
            } else {
                //en caso de existir, se asigna el id del ultimo Producto +1
                jsonObject = JsonUtil._pedidos.getJSONObject(size - 1);
                int id_ultimo_pedido = jsonObject.getInt("id_pedido");
                jsonObject = new JSONObject();
                this.id_pedido = id_ultimo_pedido + 1;
                jsonObject.put("id_producto", this.id_producto);
                jsonObject.put("id_pedido ", this.id_pedido );
                jsonObject.put("id_cliente", this.id_cliente);
                jsonObject.put("id_producto", this.id_producto);
                jsonObject.put("cantidad_producto", this.cantidad_producto);
                jsonObject.put("fecha_pedido", this.fecha_pedido);
                jsonObject.put("precio_pedido", this.precio_pedido);
                jsonObject.put("estado_pedido", this.estado_pedido);
            }
            //se agrega al json array de pedido
            JsonUtil._pedidos.put(jsonObject);
            //se escribe en el documento json de pedido
            new JsonUtil().escribirJsonPedido(JsonUtil._pedidos);
            return "El pedido se agregó con éxito";

        } catch (JSONException e) {
            e.printStackTrace();
            return "Error al intentar verificar pedido";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al intentar agregar el pedido";

        }
    }


    //Obtiene (lee) un pedido (C'R'UD)
    public Pedido ObtenerPedido(int id_pedido)
    {
        ArrayList<Pedido> lista = this.listaPedidos();
        //se recorre la lista en busqueda del Producto
        for(int i = 0; i < lista.size(); i++) {
            if(lista.get(i).id_pedido == id_pedido) {
                //se actualizan los datos del mismo Producto
                this.id_pedido = lista.get(i).id_pedido;
                this.id_cliente = lista.get(i).id_cliente;
                this.id_producto = lista.get(i).id_producto;
                this.cantidad_producto = lista.get(i).cantidad_producto;
                this.fecha_pedido = lista.get(i).fecha_pedido;
                this.precio_pedido = lista.get(i).precio_pedido;
                this.estado_pedido = lista.get(i).estado_pedido;
                return this;
            }
        }
        //en caso de no encontrarlo retorna nulo
        return null;
    }

    //Modifica un pedido (CR'U'D)
    public String ModificarPedido() {
        try {
            //se recorre el json array de pedido en busca del pedido a modificar
            for (int i = 0; i < JsonUtil._productos.length(); i++) {
                JSONObject obj = JsonUtil._productos.getJSONObject(i);
                Pedido pedido = new Pedido();

                pedido.id_pedido =  obj.getInt("id_producto");
                pedido.id_producto = obj.getInt("id_producto");
                pedido.id_cliente = obj.getInt("id_cliente");
                pedido.id_producto = obj.getInt("id_producto");
                pedido.cantidad_producto = obj.getInt("cantidad_producto");
                pedido.fecha_pedido = obj.getString("fecha_pedido");
                pedido.precio_pedido = obj.getInt("precio_pedido");
                pedido.estado_pedido = obj.getBoolean("estado_pedido");


                if (pedido.id_pedido == this.id_pedido) {
                    //en caso de encontrarlo validara si se han hecho cambios en el objeto
                    if(pedido.id_producto != this.id_producto
                        || pedido.id_cliente != this.id_cliente
                        || pedido.id_producto != this.id_producto
                        || pedido.cantidad_producto != this.cantidad_producto
                        || pedido.fecha_pedido.equals(this.fecha_pedido)
                        || pedido.precio_pedido != this.precio_pedido
                        || pedido.estado_pedido != this.estado_pedido){
                        JSONObject object = JsonUtil._productos.getJSONObject(i);
                        object.remove("id_producto");
                        object.remove("id_cliente");
                        object.remove("id_producto");
                        object.remove("cantidad_producto");
                        object.remove("fecha_pedido");
                        object.remove("precio_pedido");
                        object.remove("estado_pedido");

                        object.put("id_producto", this.id_producto);
                        object.put("id_cliente", this.id_cliente);
                        object.put("id_producto", this.id_producto);
                        object.put("cantidad_producto", this.cantidad_producto);
                        object.put("fecha_pedido", this.fecha_pedido);
                        object.put("precio_pedido", this.precio_pedido);
                        object.put("estado_pedido", this.estado_pedido);


                        //si hay cambios se actualizara el jsonobject y se guarda en el documento
                        new JsonUtil().escribirJsonPedido(JsonUtil._pedidos);
                        return "El pedido se modificó con éxito";
                    }
                    else
                        return "El pedido no tiene cambios";
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "Error al intentar recuperar el pedido";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al intentar modificar el pedido";
        }

        return "No se modifico el pedido ";
    }

    //Eliminar un Producto (CRU'D') (De manera lógica)
    public String EliminarProducto()
    {
        /*
        //pregunta el estado del Producto
        if(this.estado_pedido){
            //si este esta vigente procedera a la eliminación
            this.estado_producto = false;
            String resultado = this.ModificarProducto();
            if(resultado == "El Producto se modificó con éxito")
                return "El Producto se eliminó con exito  (lógico)";
            else
                return resultado;
        }
        else
            return "El Producto ya ha sido eliminado (lógico)";*/
        return "";
    }


}
