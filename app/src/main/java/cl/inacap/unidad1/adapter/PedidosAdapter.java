package cl.inacap.unidad1.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cl.inacap.unidad1.activity.R;
import cl.inacap.unidad1.clases.Cliente;
import cl.inacap.unidad1.clases.Pedido;
import cl.inacap.unidad1.clases.Producto;

//clase para realizar item de arraylist Pedido personalizado
public class PedidosAdapter extends ArrayAdapter<Pedido> {

    private final Context context;
    private final ArrayList<Pedido> lista;

    public PedidosAdapter(Context context, ArrayList<Pedido> itemsArrayList) {

        super(context, R.layout.row_pedido, itemsArrayList);
        //se obtiene el contexto del que se llama
        this.context = context;
        //se obtiene la lista de los items
        this.lista = itemsArrayList;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // obtiene el layout personalizado del item "row_pedidos.xml"
        View rowView = inflater.inflate(R.layout.row_pedido, parent, false);

        // obtiene los componentes de "row_pedidos.xml"
        TextView txv_row_id_pedido = (TextView) rowView.findViewById(R.id.txv_row_id_pedido);
        TextView txv_row_cliente = (TextView) rowView.findViewById(R.id.txv_row_cliente);
        TextView txv_row_producto = (TextView) rowView.findViewById(R.id.txv_row_producto);
        TextView txv_row_precio_pedido = (TextView) rowView.findViewById(R.id.txv_row_precio_pedido);
        TextView txv_row_catidad_producto = (TextView) rowView.findViewById(R.id.txv_row_catidad_producto);
        TextView txv_row_vendedor = (TextView) rowView.findViewById(R.id.txv_row_vendedor);


        // se le asigna el valor a mostrar en los componentes
        txv_row_id_pedido.setText(String.valueOf(lista.get(position).id_pedido));
        //se busca el cliente del pedido para mostrarlo en la lista
        Cliente cliente = new Cliente();
        cliente.ObtenerCliente(lista.get(position).id_cliente);
        txv_row_cliente.setText(cliente.toString());
        //se busca el producto para mostrarlo en la lista
        Producto producto = new Producto();
        producto.ObtenerProducto(lista.get(position).id_producto);

        txv_row_producto.setText(producto.toString());
        txv_row_precio_pedido.setText(String.valueOf(lista.get(position).precio_pedido));
        txv_row_catidad_producto.setText(String.valueOf(lista.get(position).cantidad_producto));
        txv_row_vendedor.setText(lista.get(position).vendedor);

        //si no esta entregada, se cambia el color de fondo
        if(!lista.get(position).estado_pedido)
            rowView.setBackgroundColor(Color.parseColor("#FED453"));
        // se devuelve el item
        return rowView;
    }
}