package cl.inacap.unidad1.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

import cl.inacap.unidad1.adapter.PedidosAdapter;
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
        ToggleButton tbtn_mis_pedidos = (ToggleButton)findViewById(R.id.tbtn_mis_pedidos);
        Button btn_rutas = (Button)findViewById(R.id.btn_rutas);

        btn_rutas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PedidosActivity.this, RutasActivity.class);
                PedidosActivity.this.startActivity(intent);
            }
        });

        tbtn_mis_pedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llenarListaPedidos();
            }
        });

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
        llenarListaPedidos();
    }
    //se llenan los pedidos
    private void llenarListaPedidos() {
        ListView lv_pedidos = (ListView)findViewById(R.id.lv_pedidos);
        ToggleButton tbtn_mis_pedidos = (ToggleButton)findViewById(R.id.tbtn_mis_pedidos);

        Pedido pedido = new Pedido();
        ArrayList<Pedido> pedidos = null;
        if(tbtn_mis_pedidos.isChecked())
            pedidos = pedido.listaPedidosPorVendedor();
        else
            pedidos = pedido.listaPedidos();
        //en caso de venir desde cliente, se buscaran solo los pedidos asociados a el
        //y se ocultan los botones para solo mostrar la lista
        Bundle bundle = null;
        if(getIntent().hasExtra("extras")) {
            bundle = getIntent().getExtras().getBundle("extras");
            Cliente cliente = (Cliente)bundle.getSerializable("cliente");
            pedidos = pedido.listaPedidosPorCliente(cliente.id_cliente);
            Button btn_nuevo_pedido = (Button)findViewById(R.id.btn_nuevo_pedido);
            btn_nuevo_pedido.setVisibility(View.GONE);
            tbtn_mis_pedidos.setVisibility(View.GONE);
        }

        PedidosAdapter adapter = new PedidosAdapter(this,pedidos);
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

    @Override
    protected void onResume() {
        super.onResume();
        llenarListaPedidos();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //cuando cambie la localidad se centra la camara
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.layout.menu, menu);
        menu.getItem(0).setTitle("Clientes");
        menu.getItem(1).setTitle("Productos");
        return true;
    }


    //se les da las funciones a los item del menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent = null;
        switch (item.getItemId())
        {
            case R.id.menu_uno:
                intent = new Intent(PedidosActivity.this, ClientesActivity.class);
                PedidosActivity.this.startActivity(intent);
                return true;

            case R.id.menu_dos:
                intent = new Intent(PedidosActivity.this, ProductosActivity.class);
                PedidosActivity.this.startActivity(intent);
                return true;
            case R.id.menu_mis_rutas:
                intent = new Intent(PedidosActivity.this, RutasActivity.class);
                PedidosActivity.this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
