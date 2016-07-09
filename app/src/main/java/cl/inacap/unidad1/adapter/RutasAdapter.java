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

//clase para realizar item de arraylist de las rutas, personalizado
public class RutasAdapter extends ArrayAdapter<Pedido> {

    private final Context context;
    private final ArrayList<Pedido> lista;

    public RutasAdapter(Context context, ArrayList<Pedido> itemsArrayList) {

        super(context, R.layout.row_rutas, itemsArrayList);
        //se obtiene el contexto del que se llama
        this.context = context;
        //se obtiene la lista de los items
        this.lista = itemsArrayList;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // obtiene el layout personalizado del item "row_rutas.xml"
        View rowView = inflater.inflate(R.layout.row_rutas, parent, false);

        // obtiene los componentes de "row_rutas.xml"
        TextView txv_row_ruta = (TextView) rowView.findViewById(R.id.txv_row_ruta);
        txv_row_ruta.setText(lista.get(position).direccion_pedido);
        return rowView;
    }
}