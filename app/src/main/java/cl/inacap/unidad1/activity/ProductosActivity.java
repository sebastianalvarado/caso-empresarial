package cl.inacap.unidad1.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import cl.inacap.unidad1.adapter.ProductosAdapter;
import cl.inacap.unidad1.clases.Cliente;
import cl.inacap.unidad1.clases.Producto;
import cl.inacap.unidad1.clases.Producto;

public class ProductosActivity extends AppCompatActivity {

    private ArrayAdapter<Producto> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);



        Button btn_clientes  = (Button)findViewById(R.id.btn_clientes);
        Button btn_pedidos = (Button)findViewById(R.id.btn_pedidos);

        //seteo de botones para el resto de los formularios
        btn_clientes.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProductosActivity.this, ClientesActivity.class);
                ProductosActivity.this.startActivity(intent);

            }

        });

        btn_pedidos.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProductosActivity.this, PedidosActivity.class);
                ProductosActivity.this.startActivity(intent);

            }

        });

        //se llena la lista de productos
        llenarListaProductos();




    }
    //Se pobla el listview de Producto
    private void llenarListaProductos() {


        ListView lv_producto = (ListView)findViewById(R.id.lv_productos);
        Producto producto = new Producto();
        ArrayList<Producto> productos = producto.listaProductos();

        //se pregunta si la cponsulta  ala activdad proviene de la actividad de pedidos
        //de ser asi oculta los botones que acceden al resto de los formularios
        //y se actualiza la lista de productos a los disponibles
        if (getIntent().hasExtra("extras")) {
            Bundle bundle = getIntent().getExtras().getBundle("extras");
            if(bundle.getString("accion").equals("productopedido")) {
                productos = producto.listaProductosDisponibles(); //lista de solo disponibles
                //se ocultan los botones
                Button btn_clientes  = (Button)findViewById(R.id.btn_clientes);
                Button btn_pedidos = (Button)findViewById(R.id.btn_pedidos);
                btn_clientes.setVisibility(View.GONE);
                btn_pedidos.setVisibility(View.GONE);
                //en caso de ser llamado desde pedidos los item puedes ser seleccionados par el pedido
                lv_producto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        Producto producto = (Producto) parent.getAdapter().getItem(position);
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("productopedido", producto);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                });
            }
        }
        else{
            productos = producto.listaProductos();
        }


        //adapter = new ArrayAdapter<Producto>(getApplicationContext(), android.R.layout.simple_spinner_item, productos);
        adapter = new ProductosAdapter(this,productos);
        lv_producto.setAdapter(adapter);

        adapter.notifyDataSetChanged();


    }
}
