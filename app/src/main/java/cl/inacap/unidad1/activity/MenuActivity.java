package cl.inacap.unidad1.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import cl.inacap.unidad1.clases.JsonUtil;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button btn_productos = (Button)findViewById(R.id.btn_productos);
        Button btn_clientes  = (Button)findViewById(R.id.btn_clientes);
        Button btn_pedidos = (Button)findViewById(R.id.btn_pedidos);

        //Se declaran las funciones de los botones
        btn_productos.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MenuActivity.this, ProductosActivity.class);
                MenuActivity.this.startActivity(intent);

            }

        });

        btn_clientes.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MenuActivity.this, ClientesActivity.class);
                MenuActivity.this.startActivity(intent);

            }

        });

        btn_pedidos.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MenuActivity.this, PedidosActivity.class);
                MenuActivity.this.startActivity(intent);

            }

        });

    }
}
