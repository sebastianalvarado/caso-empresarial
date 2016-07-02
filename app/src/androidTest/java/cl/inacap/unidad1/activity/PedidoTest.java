package cl.inacap.unidad1.activity;

import android.content.ContentValues;
import android.test.ActivityUnitTestCase;
import android.test.RenamingDelegatingContext;

import java.util.ArrayList;

import cl.inacap.unidad1.clases.Cliente;
import cl.inacap.unidad1.clases.OperacionesBaseDatos;
import cl.inacap.unidad1.clases.Pedido;
import cl.inacap.unidad1.clases.Producto;

//se crea la clase de test que utilizara la instancia de una activity
//se crean los test deesta forma, ya que al utilizar sqlite se necesita obtener el contexto de la aplicacion
public class PedidoTest extends ActivityUnitTestCase<PedidoActivity> {

    private PedidoActivity testActivity;

    private Pedido pedidoTest = new Pedido();

    public PedidoTest() {
        super(PedidoActivity.class);
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


    //en este test se prueba la inserción de un pedido, al necesitar mas objetos se validara por cada uno
    //de los que se necesitan creandolo en el momento
    public void testIngresarPedido() throws Exception {

        //primero se agregará un cliente
        Cliente clienteTest = new Cliente();
        clienteTest.nombre_cliente = "Juan";
        clienteTest.estado_cliente = true;
        clienteTest.AgregarCliente();
        //se comprobara si la id del cliente ha sido actualizada y es mayor a 0
        assertEquals(true, clienteTest.id_cliente > 0);

        //segundo, se obtiene el producto que se utilizara en el pedido
        //estos ya estan registrados como solicitud del trabajo
        Producto productoTest = new Producto();
        assertNotNull(productoTest.ObtenerProducto(1));

        pedidoTest.id_cliente = clienteTest.id_cliente;
        pedidoTest.id_producto = productoTest.id_producto;
        pedidoTest.fecha_pedido = "1/1/2016";
        pedidoTest.cantidad_producto = 2;
        pedidoTest.precio_pedido = pedidoTest.cantidad_producto * productoTest.precio_producto;
        pedidoTest.estado_pedido = false;

        //una ves agregado los valores del pedido, se prueba si este ha sido agregado
        assertSame("Pedido agregado con exito",pedidoTest.AgregarPedido());


    }

    //en esta prueba se validará si el pedido ha sido modificado de forma correcta, y se marco como entregado
    public void testMarcarPedidoEntregado() throws Exception {
        pedidoTest = new Pedido();
        //primero obtendremos la lista de los pedidos, para estas pruebas es uno
        ArrayList<Pedido> listaPedidos = pedidoTest.listaPedidos();
        //se valida si se encuentra el pedido agregado anteriormente
        assertTrue(listaPedidos.size() == 1);
        //se asigna ese pedido al raiz
        pedidoTest = listaPedidos.get(0);
        //se cambia su estado a entregado
        pedidoTest.estado_pedido = true;
        //y se valida que se modifique
        assertSame("Se modifico el pedido con exito",pedidoTest.ModificarPedido());
        //a la vez se validara que realmente se haya quedado en el estado
        assertTrue(pedidoTest.estado_pedido);
    }
}