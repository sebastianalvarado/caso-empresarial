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

//clase para realizar item de arraylist Cliente personalizado
public class ClientesAdapter extends ArrayAdapter<Cliente> {

    private final Context context;
    private final ArrayList<Cliente> lista;

    public ClientesAdapter(Context context, ArrayList<Cliente> itemsArrayList) {

        super(context, R.layout.row_cliente, itemsArrayList);
        //se obtiene el contexto del que se llama
        this.context = context;
        //se obtiene la lista de los items
        this.lista = itemsArrayList;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // obtiene el layout personalizado del item "row_cliente.xml"
        View rowView = inflater.inflate(R.layout.row_cliente, parent, false);

        // obtiene los componentes de "row_cliente.xml"
        TextView txv_row_id_cliente = (TextView) rowView.findViewById(R.id.txv_row_id_cliente);
        TextView txv_row_nombre_cliente = (TextView) rowView.findViewById(R.id.txv_row_nombre_cliente);

        txv_row_id_cliente.setText(R.string.cliente + " " + String.valueOf(lista.get(position).id_cliente));
        txv_row_nombre_cliente.setText(String.valueOf(lista.get(position).nombre_cliente));

        //si el cliente no esta disponible se cambia de color el fondo
        if(!lista.get(position).estado_cliente)
            rowView.setBackgroundColor(Color.parseColor("#FED453"));
        // se devuelve el item
        return rowView;
    }
}