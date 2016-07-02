package cl.inacap.unidad1.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;

import cl.inacap.unidad1.adapter.ClientesAdapter;
import cl.inacap.unidad1.clases.Cliente;

public class ClientesActivity extends AppCompatActivity {

    private ArrayAdapter<Cliente> adapter;
    private static boolean clientePedido = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        Button btn_nuevo_cliente = (Button)findViewById(R.id.btn_nuevo_cliente);
        Button btn_pedidos = (Button)findViewById(R.id.btn_pedidos);
        Button btn_productos = (Button)findViewById(R.id.btn_clientes);

        //Se declaran las funciones de los botones
        btn_productos.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ClientesActivity.this, ProductosActivity.class);
                ClientesActivity.this.startActivity(intent);

            }

        });


        btn_pedidos.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ClientesActivity.this, PedidosActivity.class);
                ClientesActivity.this.startActivity(intent);

            }

        });


        //se obtiene el valor enviado para saber desde que actividad proviene
        if(getIntent().hasExtra("extras")) {
            Bundle bundle = getIntent().getExtras().getBundle("extras");
            //si proviene de pedido se ocultan los botones para solo mostrar la lista
            if(bundle.getString("accion").equals("clientepedido")) {
                btn_nuevo_cliente.setVisibility(View.GONE);
                btn_pedidos.setVisibility(View.GONE);
                btn_productos.setVisibility(View.GONE);
                //se setea el valor, conociendo que viene desde pedidos para reconocerlo en toda la clase
                clientePedido = true;
            }
        }
        //se le asigna la funcion al boton nuevo
        btn_nuevo_cliente.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                //Se llama a la activity Cliente para crear un nuevo cliente
                Intent intent = new Intent(ClientesActivity.this, ClienteActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("accion","nuevo");
                intent.putExtra("extras",bundle);
                ClientesActivity.this.startActivity(intent);

            }

        });


        llenarListaClientes();
    }
    //Se pobla el listview de clientes
    private void llenarListaClientes() {

        ListView lv_clientes = (ListView)findViewById(R.id.lv_clientes);
        Cliente cliente = new Cliente();
        ArrayList<Cliente>  clientes = new ArrayList<>();


        //si proviene de pedido se cargaran solo los clientes disponibles, en caso contrario, se cargan todos

        if(clientePedido)
            clientes = cliente.listaClientesDisponibles();
        else
            clientes = cliente.listaClientes();
        adapter = new ClientesAdapter(this,clientes);

        if(getIntent().hasExtra("extras")) {
            Bundle bundle = getIntent().getBundleExtra("extras");
            if (bundle.getString("accion").equals("clientepedido"))
                clientes = cliente.listaClientesDisponibles();
        }
        else
            clientes = cliente.listaClientes();


        lv_clientes.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        lv_clientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Cliente cliente = (Cliente)parent.getAdapter().getItem(position);
                //si proviene de pedido se obtiene el cliente y se retorna, en caso contrario se dirige a la modificacion del cliente
                if (clientePedido) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("clientepedido", cliente);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                    //finishActivityFromChild(ClientesActivity.this,1);
                } else {

                    Intent intent = new Intent(ClientesActivity.this, ClienteActivity.class);
                    //envia el objeto que se quiere modificar con la accion que hara la siguiente activity (Cliente)
                    Bundle bundle = new Bundle();
                    bundle.putString("accion", "editar");
                    bundle.putSerializable("cliente", (Serializable) cliente);
                    intent.putExtra("extras", bundle);
                    startActivity(intent);
                }
            }
        });

    }
    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        //refresca la tabla en caso de volver de siguiente ventana (Cliente)
        llenarListaClientes();
    }
}
