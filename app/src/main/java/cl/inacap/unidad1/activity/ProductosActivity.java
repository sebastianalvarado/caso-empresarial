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

import cl.inacap.unidad1.clases.Cliente;
import cl.inacap.unidad1.clases.Producto;
import cl.inacap.unidad1.clases.Producto;

public class ProductosActivity extends AppCompatActivity {

    private ArrayAdapter<Producto> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);



        /*
        //Para efectos de evaluacion, funcionalidad no requerida
        Button btn_pedidos = (Button)findViewById(R.id.btn_nuevo_producto);


        btn_pedidos.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProductosActivity.this, ProductoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("accion","nuevo");
                intent.putExtra("extras",bundle);
                ProductosActivity.this.startActivity(intent);

            }

        });
        */

        llenarListaProductos();



    }
    //Se pobla el listview de Producto
    private void llenarListaProductos() {

        ListView lv_producto = (ListView)findViewById(R.id.lv_productos);
        Producto producto = new Producto();
        ArrayList<Producto> productos = producto.listaProductos();

        adapter = new ArrayAdapter<Producto>(getApplicationContext(), android.R.layout.simple_spinner_item, productos);

        lv_producto.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        lv_producto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Producto producto = (Producto)parent.getAdapter().getItem(position);

                if(getIntent().hasExtra("extras")) {
                    Bundle bundle = getIntent().getBundleExtra("extras");
                    if(bundle.getString("accion").equals("productopedido")) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("productopedido", producto);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                        return;
                    }
                }
            }
        });
        /*
        //No se requiere ya que para efectos de evaluación, crear productos por sistema no se requiere
        lv_producto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Producto producto = (Producto)parent.getAdapter().getItem(position);
                Intent intent = new Intent(ProductosActivity.this, ProductoActivity.class);
                //envia el objeto que se quiere modificar con la accion que hara la siguiente activity (Producto)
                Bundle bundle = new Bundle();
                bundle.putString("accion","editar");
                bundle.putSerializable("producto",(Serializable)producto);
                intent.putExtra("extras",bundle);
                startActivity(intent);
            }
        });
    */
    }
}
