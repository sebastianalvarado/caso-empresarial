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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cl.inacap.unidad1.clases.Cliente;
import cl.inacap.unidad1.clases.Pedido;
import cl.inacap.unidad1.clases.Producto;

public class PedidoActivity extends AppCompatActivity {
    ArrayAdapter<Producto> adapter;
    //se generan las clases globales para tener mejor acceso a los compronentes del pedido
    Pedido pedido = new Pedido();
    Cliente cliente = new Cliente();
    Producto producto = new Producto();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        //se obtienen los componenetes
        TextView txv_idpedido = (TextView)findViewById(R.id.txv_idpedido);
        final TextView txt_precio_pedido = (TextView)findViewById(R.id.txv_precio_pedido);
        final EditText txt_cantidad_pedido = (EditText)findViewById(R.id.txt_cantidad_pedido);
        final EditText txt_fecha_pedido = (EditText)findViewById(R.id.txt_fecha_pedido);
        final Button btn_cliente_pedido = (Button)findViewById(R.id.btn_cliente_pedido);
        Button btn_producto_pedido = (Button)findViewById(R.id.btn_producto_pedido);
        Button btn_gestionar_pedido = (Button)findViewById(R.id.btn_gestionar_pedido);
        Button btn_mapa = (Button)findViewById(R.id.btn_mapa);
        Button btn_eliminar_pedido = (Button)findViewById(R.id.btn_eliminar_pedido);
        final CheckBox cb_estado_pedido = (CheckBox)findViewById(R.id.cb_estado_pedido);
        Button btn_volver = (Button)findViewById(R.id.btn_pedidos_cliente);

        //se setea un boton volver
        btn_volver.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();

            }
        });


        txt_cantidad_pedido.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String _cantidad = s.toString();
                int cantidad = Integer.parseInt(_cantidad.length() == 0 ? "0" : _cantidad);
                int valor = cantidad * producto.precio_producto;
                txt_precio_pedido.setText(String.valueOf(valor));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //se setea el boton para buscar al cliente
        btn_cliente_pedido.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PedidoActivity.this, ClientesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("accion","clientepedido");
                intent.putExtra("extras",bundle);
                //http://stackoverflow.com/questions/920306/sending-data-back-to-the-main-activity-in-android
                PedidoActivity.this.startActivityForResult( intent, 1);

            }

        });

        //se setea el boton para buscar al producto
        btn_producto_pedido.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PedidoActivity.this, ProductosActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("accion","productopedido");
                intent.putExtra("extras",bundle);
                //http://stackoverflow.com/questions/920306/sending-data-back-to-the-main-activity-in-android
                PedidoActivity.this.startActivityForResult( intent, 2);

            }

        });

        btn_eliminar_pedido.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String resultado = pedido.EliminarPedido();
                Toast.makeText(PedidoActivity.this,resultado, Toast.LENGTH_LONG).show();
            }

        });

        //se setea el boton para el mapa
        btn_mapa.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PedidoActivity.this, MapaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("accion","clientepedido");
                bundle.putSerializable("pedido",pedido);
                intent.putExtra("extras",bundle);
                //http://stackoverflow.com/questions/920306/sending-data-back-to-the-main-activity-in-android
                PedidoActivity.this.startActivityForResult( intent, 3);


            }

        });


        //se busca que accion se hara
        Bundle bundle = null;
        String accion = "";
        if(getIntent().hasExtra("extras")) {
            bundle = getIntent().getExtras().getBundle("extras");
            accion = bundle.getString("accion");
        }
        if(accion.equals("editar"))
        {
            btn_gestionar_pedido.setText("Editar");
            pedido = (Pedido)bundle.getSerializable("pedido");
            cliente.ObtenerCliente(pedido.id_cliente);
            producto.ObtenerProducto(pedido.id_producto);
            //se llenan los componentes
            txv_idpedido.setText(String.valueOf(pedido.id_pedido));
            txt_cantidad_pedido.setText(String.valueOf(pedido.cantidad_producto));
            txt_fecha_pedido.setText(pedido.fecha_pedido);
            txt_precio_pedido.setText(String.valueOf(pedido.precio_pedido));
            cb_estado_pedido.setChecked(pedido.estado_pedido);
            btn_cliente_pedido.setText(cliente.toString());
            btn_producto_pedido.setText(producto.toString());
            btn_mapa.setText(pedido.direccion_pedido);

            btn_gestionar_pedido.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //se llenan los valores del pedido, al ser global se utilza tanto para ingresar y modificar.
                    ModificarValores();
                    String resultado = pedido.ModificarPedido();
                    Toast.makeText(PedidoActivity.this,resultado, Toast.LENGTH_LONG).show();

                }
            });

            btn_eliminar_pedido.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    String resultado = pedido.EliminarPedido();
                    Toast.makeText(PedidoActivity.this,resultado, Toast.LENGTH_LONG).show();

                }
            });

        }
        if(accion.equals("nuevo"))
        {
            pedido = new Pedido();
            btn_eliminar_pedido.setVisibility(View.GONE);
            btn_gestionar_pedido.setText("Nuevo");
            //se indica que hará el boton de gestión
            btn_gestionar_pedido.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    ModificarValores();
                    String resultado = pedido.AgregarPedido(); //agrega un nuevo cliente
                    Toast.makeText(PedidoActivity.this,resultado, Toast.LENGTH_LONG).show();
                    //se vuelve a inicializar la activity con el nuevo cliente para gestionar su modificacion
                    actualizarActivity();

                }

            });
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //se utiliza para el llamado de datos de clientes y productos
        super.onActivityResult(requestCode, resultCode, data);
        //request 1 es el del cliente
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                //una vez recibido el valor se setea en la variable globlar
                cliente = (Cliente)data.getExtras().getSerializable("clientepedido");
                Button btn_cliente_pedido = (Button)findViewById(R.id.btn_cliente_pedido);
                btn_cliente_pedido.setText(cliente.toString());
                pedido.id_cliente = cliente.id_cliente;
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        //request 2 es el del producto
        if (requestCode == 2 ) {
            if(resultCode == Activity.RESULT_OK){
                //una vez recibido el valor se setea en la variable globlar
                producto = (Producto)data.getExtras().getSerializable("productopedido");
                Button btn_producto_pedido = (Button)findViewById(R.id.btn_producto_pedido);
                EditText txt_cantidad_pedido = (EditText)findViewById(R.id.txt_cantidad_pedido);
                TextView txv_precio_pedido = (TextView)findViewById(R.id.txv_precio_pedido);

                //aqui se pregunta si ya se ingreso la cantidad de productos,
                // para una vez insertado el producto se calcule automaticamente
                btn_producto_pedido.setText(producto.toString());
                pedido.id_producto = producto.id_producto;
                String _cantidad = txt_cantidad_pedido.getText().toString();
                if(_cantidad.length() > 0) {
                    int cantidad = Integer.parseInt(_cantidad.length() == 0 ? "0" : _cantidad);
                    int valor = cantidad * producto.precio_producto;
                    txv_precio_pedido.setText(String.valueOf(valor));
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }


        //request 3 es el del mapa
        if (requestCode == 3 ) {
            if(resultCode == Activity.RESULT_OK){
                Button btn_mapa = (Button)findViewById(R.id.btn_mapa);
                //una vez recibido el valor se setea en la variable globlar
                String[] mapa = data.getExtras().getStringArray("mapapedido");
                pedido.longitud_pedido = Double.parseDouble(mapa[0]);
                pedido.latitud_pedido = Double.parseDouble(mapa[1]);
                pedido.direccion_pedido = mapa[2];
                btn_mapa.setText(mapa[2]);
                //aqui se pregunta si ya se ingreso la cantidad de productos,
                // para una vez insertado el producto se calcule automaticamente

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

    }

    //funcionalidad para actualizar la actividad una vez que se agrega un pedido
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

    //funcion para la moficiacion del pedido, utilizado tanto para agregar uno nuevo o modificar uno ya existente
    void ModificarValores()
    {
        CheckBox cb_estado_pedido = (CheckBox)findViewById(R.id.cb_estado_pedido);
        EditText txt_fecha_pedido = (EditText)findViewById(R.id.txt_fecha_pedido);
        EditText txv_cantidad_pedido = (EditText)findViewById(R.id.txt_cantidad_pedido);
        TextView txv_precio_pedido = (TextView)findViewById(R.id.txv_precio_pedido);

        pedido.fecha_pedido = txt_fecha_pedido.getText().toString();
        pedido.estado_pedido = cb_estado_pedido.isChecked();
        String cantidad = txv_cantidad_pedido.getText().toString();
        pedido.cantidad_producto = Integer.parseInt(cantidad);
        String precio = txv_precio_pedido.getText().toString();
        pedido.precio_pedido = Integer.parseInt(precio);
        pedido.id_cliente = cliente.id_cliente;
        pedido.id_producto = producto.id_producto;
    }

}
