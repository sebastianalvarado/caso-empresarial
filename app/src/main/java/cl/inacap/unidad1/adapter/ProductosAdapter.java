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
import cl.inacap.unidad1.clases.Configuracion;
import cl.inacap.unidad1.clases.Producto;

//clase para realizar item de arraylist Productos personalizado
public class ProductosAdapter extends ArrayAdapter<Producto> {

    private final Context context;
    private final ArrayList<Producto> lista;
    private Configuracion configuracion = new Configuracion().ObtenerConfiguracion();

    public ProductosAdapter(Context context, ArrayList<Producto> itemsArrayList) {

        super(context, R.layout.row_producto, itemsArrayList);
        //se obtiene el contexto del que se llama
        this.context = context;
        //se obtiene la lista de los items
        this.lista = itemsArrayList;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // obtiene el layout personalizado del item "row_producto.xml"
        View rowView = inflater.inflate(R.layout.row_producto, parent, false);

        // obtiene los componentes de "row_producto.xml"
        TextView txv_row_id_producto = (TextView) rowView.findViewById(R.id.txv_row_id_producto);
        TextView txv_row_nombre_producto = (TextView) rowView.findViewById(R.id.txv_row_nombre_producto);
        TextView txv_row_valor_producto = (TextView) rowView.findViewById(R.id.txv_row_valor_producto);
        TextView txv_row_estado_producto = (TextView) rowView.findViewById(R.id.txv_row_estado_producto);


        txv_row_id_producto.setText("N: " + String.valueOf(lista.get(position).id_producto));
        txv_row_nombre_producto.setText(String.valueOf(lista.get(position).nombre_producto));
        txv_row_valor_producto.setText(configuracion.moneda_formato + " " +
                " " + String.valueOf(lista.get(position).precio_producto * (configuracion.moneda_personal ? configuracion.moneda_valor : 1)));
        boolean estado = lista.get(position).estado_producto;
        txv_row_estado_producto.setText(estado ? "("+R.string.disponible+")" : "("+R.string.no_disponible+")" );

        //si no esta disponible, se cambia el color de fondo
        if(!estado)
            rowView.setBackgroundColor(Color.parseColor("#FED453"));
        // se devuelve el item
        return rowView;
    }
}