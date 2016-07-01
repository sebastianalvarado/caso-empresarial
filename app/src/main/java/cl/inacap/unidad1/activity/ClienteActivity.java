package cl.inacap.unidad1.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

import cl.inacap.unidad1.clases.Cliente;

public class ClienteActivity extends AppCompatActivity {

    private Cliente cliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);


        //se obtienen los componenetes
        LinearLayout ll_idcliente = (LinearLayout)findViewById(R.id.ll_idcliente);
        TextView txv_idcliente = (TextView)findViewById(R.id.txv_idcliente);
        final EditText txt_nombre_cliente = (EditText)findViewById(R.id.txt_nombre_cliente);
        final CheckBox cb_estado_cliente = (CheckBox)findViewById(R.id.cb_estado_cliente);
        Button btn_gestionar_cliente  = (Button)findViewById(R.id.btn_gestionar_cliente);
        Button btn_eliminar_cliente  = (Button)findViewById(R.id.btn_eliminar_cliente);
        Button btn_pedidos_cliente = (Button)findViewById(R.id.btn_pedidos_cliente);
        Button btn_volver = (Button)findViewById(R.id.btn_volver);

        //se setea el boton para ver los pedidos del cliente
        btn_pedidos_cliente.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClienteActivity.this, PedidosActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("accion","listarpedidos");
                bundle.putSerializable("cliente",(Serializable)cliente);
                intent.putExtra("extras",bundle);
                startActivity(intent);
                finish();

            }
        });
        //se setea el boton para ver los pedidos del cliente
        btn_eliminar_cliente.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClienteActivity.this, PedidosActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("accion","listarpedidos");
                bundle.putSerializable("cliente",(Serializable)cliente);
                intent.putExtra("extras",bundle);
                startActivity(intent);
                finish();

            }
        });

        //se setea un boton volver
        btn_volver.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();

            }
        });

        //se genera la funcion de eliminar
        btn_eliminar_cliente.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String resultado = cliente.EliminarCliente();
                Toast.makeText(ClienteActivity.this,resultado, Toast.LENGTH_LONG).show();
                actualizarActivity();

            }
        });

        //se obtiene los valores enviados a la actividad
        Bundle bundle = getIntent().getExtras().getBundle("extras");
        String accion = bundle.getString("accion"); //se recibe la accion que empleara esta activity

        if(accion.equals("editar")) {
            //le asigna nombre al boton de gestion
            btn_gestionar_cliente.setText("EDITAR");
            cliente = (Cliente) bundle.getSerializable("cliente");
            txv_idcliente.setText(String.valueOf(cliente.id_cliente));
            txt_nombre_cliente.setText(String.valueOf(cliente.nombre_cliente));
            cb_estado_cliente.setChecked(cliente.estado_cliente);
            cb_estado_cliente.setClickable(!cliente.estado_cliente);
            //se indica que hará el boton de gestión
            btn_gestionar_cliente.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    cliente.nombre_cliente = txt_nombre_cliente.getText().toString();
                    cliente.estado_cliente = cb_estado_cliente.isChecked();
                    String respuesta = cliente.ModificarCliente(); //se modifica el cliente
                    Toast.makeText(ClienteActivity.this,respuesta, Toast.LENGTH_LONG).show();
                    actualizarActivity();
                }

            });
        }
        else if(accion.equals("nuevo"))
        {
            ll_idcliente.setVisibility(LinearLayout.GONE);
            btn_eliminar_cliente.setVisibility(View.GONE);
            btn_pedidos_cliente.setVisibility(View.GONE);
            btn_gestionar_cliente.setText("Nuevo");

            //se indica que hará el boton de gestión
            btn_gestionar_cliente.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    cliente = new Cliente();
                    cliente.nombre_cliente = txt_nombre_cliente.getText().toString();
                    cliente.estado_cliente = cb_estado_cliente.isChecked();

                    String resultado = cliente.AgregarCliente(); //agrega un nuevo cliente
                    Toast.makeText(ClienteActivity.this,resultado, Toast.LENGTH_LONG).show();
                    //se vuelve a inicializar la activity con el nuevo cliente para gestionar su modificacion
                    actualizarActivity();

                }

            });

        }
        else {
            Toast.makeText(ClienteActivity.this,"OPCION ERRONEA CLIENTE", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    //se vuelve a inicializar la activity
    void actualizarActivity()
    {
        Intent intent = new Intent(ClienteActivity.this, ClienteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("accion","editar");
        bundle.putSerializable("cliente",(Serializable)cliente);
        intent.putExtra("extras",bundle);
        startActivity(intent);
        finish();
    }

}
