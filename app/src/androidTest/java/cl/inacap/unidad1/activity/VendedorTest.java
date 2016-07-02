package cl.inacap.unidad1.activity;

import android.test.ActivityUnitTestCase;
import android.test.RenamingDelegatingContext;

import java.util.ArrayList;

import cl.inacap.unidad1.clases.Cliente;
import cl.inacap.unidad1.clases.OperacionesBaseDatos;
import cl.inacap.unidad1.clases.Pedido;
import cl.inacap.unidad1.clases.Producto;
import cl.inacap.unidad1.clases.Vendedor;

//se crea la clase de test que utilizara la instancia de una activity
//se crean los test deesta forma, ya que al utilizar sqlite se necesita obtener el contexto de la aplicacion
public class VendedorTest extends ActivityUnitTestCase<LoginActivity> {

    private LoginActivity testActivity;

    private Vendedor vendedorTest;

    public VendedorTest() {
        super(LoginActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        //se inicializa la activity en la clase de test
        testActivity = getActivity();
        //se obtiene el contexto para ser usado en test
        RenamingDelegatingContext context = new RenamingDelegatingContext(getInstrumentation().getTargetContext(), "test_");
        //se instancia la base de datos que se utilizara con el contexto de test
        OperacionesBaseDatos.instanciarContexto(context);
    }


    //el objetivo de esta prueba es emular el mismo ejemplo que se ha entregado para el inicio
    //de sesion del usuario
    public void testLoginVendedor() throws Exception {
        vendedorTest = new Vendedor();
        assertTrue(vendedorTest.validarLogin("user1","user1"));

    }
    //en este caso se entregara un usuario incorrecto
    public void testLoginVendedorIncorrecto() throws Exception {
        vendedorTest = new Vendedor();
        assertTrue(vendedorTest.validarLogin("user1","user"));

    }
}