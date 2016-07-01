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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

import cl.inacap.unidad1.clases.Cliente;
import cl.inacap.unidad1.clases.JsonUtil;
import cl.inacap.unidad1.clases.Producto;

public class ClientesActivity extends AppCompatActivity {

    private ArrayAdapter<Cliente> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        Button btn_nuevo_cliente = (Button)findViewById(R.id.btn_nuevo_cliente);



        if(getIntent().hasExtra("extras")) {
            btn_nuevo_cliente.setVisibility(View.GONE);
        }
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
        ArrayList<Cliente>  clientes= null;

        if(getIntent().hasExtra("extras")) {
            Bundle bundle = getIntent().getBundleExtra("extras");
            if (bundle.getString("accion").equals("clientepedido"))
                clientes = cliente.listaClientesDisponibles();
        }
        else
            clientes = cliente.listaClientes();

        adapter = new ArrayAdapter<Cliente>(getApplicationContext(), android.R.layout.simple_spinner_item, clientes);

        lv_clientes.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        lv_clientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Cliente cliente = (Cliente)parent.getAdapter().getItem(position);

                if(getIntent().hasExtra("extras")) {
                    Bundle bundle = getIntent().getBundleExtra("extras");
                    if(bundle.getString("accion").equals("clientepedido")) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("clientepedido", cliente);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                        return;
                    }
                }


                Intent intent = new Intent(ClientesActivity.this, ClienteActivity.class);
                //envia el objeto que se quiere modificar con la accion que hara la siguiente activity (Cliente)
                Bundle bundle = new Bundle();
                bundle.putString("accion","editar");
                bundle.putSerializable("cliente",(Serializable)cliente);
                intent.putExtra("extras",bundle);
                startActivity(intent);
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
