package cl.inacap.unidad1.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.nio.BufferUnderflowException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cl.inacap.unidad1.clases.Cliente;
import cl.inacap.unidad1.clases.Pedido;
import cl.inacap.unidad1.clases.Producto;

public class PedidoActivity extends AppCompatActivity {
    ArrayAdapter<Producto> adapter;
    Pedido pedido;
    Cliente cliente;
    Producto producto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        final LinearLayout ll_idpedido = (LinearLayout)findViewById(R.id.ll_idpedido);
        final TextView txv_idpedido = (TextView)findViewById(R.id.txv_idpedido);

        TextView txv_cliente_pedido = (TextView)findViewById(R.id.txv_cliente_pedido);
        TextView txv_producto_pedido = (TextView)findViewById(R.id.txv_producto_pedido);
        final TextView txv_precio_pedido = (TextView)findViewById(R.id.txv_precio_pedido);
        final EditText txt_fecha_pedido = (EditText)findViewById(R.id.txt_fecha_pedido);
        final EditText txt_cantidad_pedido = (EditText)findViewById(R.id.txt_cantidad_pedido);
        CheckBox cb_estado_pedido = (CheckBox)findViewById(R.id.cb_estado_pedido);
        final Button btn_cliente_pedido = (Button)findViewById(R.id.btn_cliente_pedido);
        Button btn_producto_pedido = (Button)findViewById(R.id.btn_producto_pedido);
        Button btn_gestionar_pedido = (Button)findViewById(R.id.btn_gestionar_pedido);
        Button btn_eliminar_pedido = (Button)findViewById(R.id.btn_eliminar_pedido);

        txt_cantidad_pedido.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(txt_cantidad_pedido.getText().toString().length() <= 0) {
                    txv_precio_pedido.setText("Total: "+String.valueOf(0));
                    return;
                }
                int precio = producto.precio_producto;
                String _cantidad = txt_cantidad_pedido.getText().toString();
                int cantidad  = pedido.cantidad_producto = Integer.parseInt(_cantidad);
                int valor = precio * cantidad;
                txv_precio_pedido.setText("Total: "+String.valueOf(valor));
            }
        });

        btn_cliente_pedido.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PedidoActivity.this, ClientesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("accion","clientepedido");
                intent.putExtra("extras",bundle);
                PedidoActivity.this.startActivityForResult( intent, 1);

            }

        });

        btn_producto_pedido.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PedidoActivity.this, ProductosActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("accion","productopedido");
                intent.putExtra("extras",bundle);
                PedidoActivity.this.startActivityForResult( intent, 2);

            }

        });

        btn_eliminar_pedido.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String resultado = "";
                resultado = pedido.EliminarProducto();
                Toast.makeText(PedidoActivity.this,resultado, Toast.LENGTH_LONG).show();
                actualizarActivity();
            }

        });


        Bundle bundle = null;
        String accion = "";
        if(getIntent().hasExtra("extras")) {
            bundle = getIntent().getExtras().getBundle("extras");
            accion = bundle.getString("accion");
        }
        if(accion.equals("editar"))
        {
            pedido = (Pedido)bundle.getSerializable("pedido");
            cliente = new Cliente();
            producto = new Producto();
            cliente.ObtenerCliente(pedido.id_cliente);
            producto.ObtenerProducto(pedido.id_producto);

            btn_gestionar_pedido.setText("Editar");
            txv_cliente_pedido.setText(cliente.toString());
            txv_producto_pedido.setText(producto.toString());
            txt_cantidad_pedido.setText(String.valueOf(pedido.cantidad_producto));
            txv_precio_pedido.setText(String.valueOf(pedido.precio_pedido));
            cb_estado_pedido.setChecked(pedido.estado_pedido);
            cb_estado_pedido.setEnabled(true);
            txt_cantidad_pedido.setEnabled(true);

            if(pedido.estado_pedido)
                btn_eliminar_pedido.setVisibility(View.GONE);

            btn_gestionar_pedido.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    String respuesta = "";
                    respuesta = pedido.ModificarPedido();
                    pedido.fecha_pedido = txt_fecha_pedido.getText().toString();
                    String cantidad = txt_cantidad_pedido.getText().toString();
                    pedido.cantidad_producto = Integer.parseInt(cantidad);
                    Toast.makeText(PedidoActivity.this,respuesta, Toast.LENGTH_LONG).show();
                    actualizarActivity();
            }

            });


        }
        else if(accion.equals("nuevo"))
        {
            pedido = new Pedido();
            Calendar cal = Calendar.getInstance();
            Date fecha = cal.getTime();
            String fechaHoy = new SimpleDateFormat("dd/MM/yyyy").format(fecha);
            assert txt_fecha_pedido != null;
            txt_fecha_pedido.setText(fechaHoy);
            btn_gestionar_pedido.setText("Nuevo");
            ll_idpedido.setVisibility(LinearLayout.GONE);
            btn_eliminar_pedido.setVisibility(View.GONE);
            cb_estado_pedido.setEnabled(false);

            btn_gestionar_pedido.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    String respuesta = "";
                    String _cantidad = txt_cantidad_pedido.getText().toString();
                    pedido.cantidad_producto = Integer.parseInt(_cantidad);
                    pedido.precio_pedido = Integer.parseInt(_cantidad) * producto.precio_producto;
                    pedido.fecha_pedido = txt_fecha_pedido.getText().toString();
                    respuesta = pedido.AgregarPedido();
                    Toast.makeText(PedidoActivity.this,respuesta, Toast.LENGTH_LONG).show();
                    if(respuesta.equals("El pedido se agregó con éxito"))
                        actualizarActivity();

                }

            });

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 1:
                    if (resultCode == Activity.RESULT_OK) {
                        Cliente _cliente = (Cliente) data.getExtras().getSerializable("clientepedido");
                        TextView txv_cliente_pedido = (TextView) findViewById(R.id.txv_cliente_pedido);
                        this.cliente = _cliente ;
                        pedido.id_cliente = _cliente .id_cliente;
                        txv_cliente_pedido.setText(this.cliente.toString());
                        Toast.makeText(this,"Cliente nuevo seleccionado", Toast.LENGTH_LONG).show();

                    }
                    if (resultCode == Activity.RESULT_CANCELED) {
                        Toast.makeText(PedidoActivity.this,"No se selecciono cliente nuevo", Toast.LENGTH_LONG).show();

                    }

                break;
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    Producto producto = (Producto) data.getExtras().getSerializable("productopedido");
                    TextView txv_producto_pedido = (TextView) findViewById(R.id.txv_producto_pedido);
                    EditText txt_cantidad_pedido = (EditText) findViewById(R.id.txt_cantidad_pedido);
                    txt_cantidad_pedido.setEnabled(true);
                    this.producto = producto;
                    pedido.id_producto = producto.id_producto;
                    txv_producto_pedido.setText(producto.toString());

                    Toast.makeText(PedidoActivity.this,"Producto nuevo seleccionado", Toast.LENGTH_LONG).show();

                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(PedidoActivity.this,"No se selecciono producto nuevo", Toast.LENGTH_LONG).show();

                }

                break;
        }
    }


    //se vuelve a inicializar la activity
    void actualizarActivity()
    {
        Intent intent = new Intent(PedidoActivity.this, PedidoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("accion","editar");
        bundle.putSerializable("pedido",(Serializable)pedido);
        intent.putExtra("extras",bundle);
        startActivity(intent);
        finish();
    }

}
