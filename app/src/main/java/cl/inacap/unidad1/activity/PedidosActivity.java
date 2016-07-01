package cl.inacap.unidad1.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

import cl.inacap.unidad1.clases.Cliente;
import cl.inacap.unidad1.clases.Pedido;
import cl.inacap.unidad1.clases.Producto;

public class PedidosActivity extends AppCompatActivity {

    ArrayAdapter<Pedido> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);
        Button btn_nuevo_pedido = (Button)findViewById(R.id.btn_nuevo_pedido);


        btn_nuevo_pedido.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PedidosActivity.this, PedidoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("accion","nuevo");
                intent.putExtra("extras",bundle);
                PedidosActivity.this.startActivity(intent);

            }

        });
        Bundle bundle = null;
        if(getIntent().hasExtra("extras")) {
            bundle = getIntent().getExtras().getBundle("extras");

        }
        llenarListaPedidos();



    }

    private void llenarListaPedidos() {
        ListView lv_pedidos = (ListView)findViewById(R.id.lv_pedidos);

        Pedido pedido = new Pedido();
        ArrayList<Pedido> pedidos = pedido.listaPedidos();

        adapter = new ArrayAdapter<Pedido>(getApplicationContext(), android.R.layout.simple_spinner_item, pedidos);

        lv_pedidos.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        lv_pedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Pedido pedido = (Pedido)parent.getAdapter().getItem(position);
                Intent intent = new Intent(PedidosActivity.this, PedidoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("accion","editar");
                bundle.putSerializable("pedido",(Serializable)pedido);
                intent.putExtra("extras",bundle);
                startActivity(intent);
            }
        });


    }
}
