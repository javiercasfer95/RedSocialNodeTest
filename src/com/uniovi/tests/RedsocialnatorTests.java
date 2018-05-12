package com.uniovi.tests;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

//import com.uniovi.services.DatosEjemplo;
import com.uniovi.tests.pageobjects.PO_HomeView;
import com.uniovi.tests.pageobjects.PO_LoginView;
import com.uniovi.tests.pageobjects.PO_NavView;
import com.uniovi.tests.pageobjects.PO_PrivateView;
import com.uniovi.tests.pageobjects.PO_Properties;
import com.uniovi.tests.pageobjects.PO_RegisterView;
import com.uniovi.tests.pageobjects.PO_View;
import com.uniovi.tests.utils.SeleniumUtils;

//Ordenamos las pruebas por el nombre del método 
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RedsocialnatorTests {

	// En Windows (Debe ser la versión 46.0 y desactivar las actualizacioens
	// automáticas)):
	// static String PathFirefox = "C:\\Path\\FirefoxPortable.exe";

	// Path Joni
	//static String PathFirefox ="P:\\SDI\\P5\\Firefox46.0.win\\Firefox46.win\\FirefoxPortable.exe";

	// Path Javi MSI
	static String PathFirefox = "D:\\Programs\\Firefox46.win\\FirefoxPortable.exe";

	// Común a Windows y a MACOSX
	static WebDriver driver = getDriver(PathFirefox);

	static String URL = "http://localhost:8081";
	static String URLCliente = "http://localhost:8081/cliente.html";

	public static WebDriver getDriver(String PathFirefox) {
		// Firefox (Versión 46.0) sin geckodriver para Selenium 2.x.
		System.setProperty("webdriver.firefox.bin", PathFirefox);
		WebDriver driver = new FirefoxDriver();
		return driver;
	}

	@BeforeClass
	public static void resetDatabaseToDefault() {
		driver.navigate().to(URL);

		// Primero reiniciamos la base de datos
		/*
		 * Esta nueva base de datos tiene varios usuarios, todos con la contraseña
		 * 123456
		 * 
		 * Los usuarios principales son: javier@correo.es joni@correo.es admin@correo.es
		 */

		iniciarSesion("admin@correo.es", "123456");
		PO_NavView.clickOption(driver, "id", "reloadDatabase");
		SeleniumUtils.esperarSegundos(driver, 2);

	}

	// Antes de cada prueba se navega al URL home de la aplicaciónn
	@Before
	public void setUp() {
		driver.navigate().to(URL);
	}

	// Después de cada prueba se borran las cookies del navegador
	@After
	public void tearDown() {
		driver.manage().deleteAllCookies();
	}

	// Antes de la primera prueba
	@BeforeClass
	static public void begin() {

	}

	// Al finalizar la última prueba
	@AfterClass
	static public void end() { // Cerramos el navegador al finalizar las pruebas
		driver.quit();
	}

	/*
	 * CASOS DE PRUEBA
	 */
	// 1.1 [RegVal] Registro de Usuario con datos válidos. FUNCIONA
	@Test
	public void T01_1_RegVal() {
		PO_HomeView.checkWelcome(driver, PO_Properties.getSPANISH());
		PO_HomeView.clickOption(driver, "id", "singup");
		SeleniumUtils.esperarSegundos(driver, 1);
		PO_RegisterView.fillForm(driver, "JaviTest@email.com", "JaviTest", "Castro", "123456", "123456");
		SeleniumUtils.esperarSegundos(driver, 1);
		PO_View.checkElement(driver, "id", "tituloLogin");
	}

	// 1.2 [RegInval] Registro de Usuario con datos inv�lidos (repetici�n de
	// contrase�a invalida). FUNCIONA
	@Test
	public void T01_2_RegInval() {

		PO_HomeView.checkWelcome(driver, PO_Properties.getSPANISH());
		PO_HomeView.clickOption(driver, "id", "singup");
		SeleniumUtils.esperarSegundos(driver, 1);
		PO_RegisterView.fillForm(driver, "JaviTest@emailDistinto.com", "JaviTest", "Castro", "123456", "123456234");
		PO_View.checkElement(driver, "id", "bandaAlerta");

	}

	// 2.1 [InVal] Inicio de sesi�n con datos v�lidos.
	// home. FUNCIONA
	@Test
	public void T02_1_InVal() {
		PO_HomeView.checkWelcome(driver, PO_Properties.getSPANISH());
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "id", "login");
		// Rellenamos el formulario
		SeleniumUtils.esperarSegundos(driver, 1);
		PO_LoginView.fillForm(driver, "javier@correo.es", "123456");
		SeleniumUtils.esperarSegundos(driver, 2);
		PO_View.checkElement(driver, "id", "parrafoUsuariosSistema");
		// PO_View.checkElement(driver, "id", "bienvenidaUser");
		// Comprueba que vea la lista de usuarios
		PO_HomeView.clickOption(driver, "id", "logout");
		PO_View.checkElement(driver, "id", "bienvenidaHome");

	}

	// 2.2 [InInVal] Inicio de sesión con datos inválidos (usuario no existente en
	// la aplicación). FUNCIONA
	// Español
	@Test
	public void T02_2_InInVal() {
		PO_HomeView.checkWelcome(driver, PO_Properties.getSPANISH());
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "id", "login");
		// Rellenamos el formulario
		SeleniumUtils.esperarSegundos(driver, 1);
		PO_LoginView.fillForm(driver, "josefaNoExiste@correo.es", "123456");
		SeleniumUtils.esperarSegundos(driver, 2);
		PO_View.checkElement(driver, "id", "bandaAlerta");

	}

	// 3.1 [LisUsrVal] Acceso al listado de usuarios desde un usuario en sesion.
	// FUNCIONA
	@Test
	public void T03_1_LisUsrVal() {
		PO_HomeView.checkWelcome(driver, PO_Properties.getSPANISH());
		// Vamos al formulario de logueo.
		iniciarSesion("joni@correo.es", "123456");
		// LOGIN VALIDO
		// Una vez dentro clickamos en Usuarios y que nos lleve a la lista de usuarios
		// del
		// sistema.
		// PO_HomeView.clickOption(driver, "id", "userListPage");

		// Se comprueba que esta el texto de la pagina que dice que esos son los
		// usuarios que hay en el sistema.
		PO_View.checkElement(driver, "id", "parrafoUsuariosSistema");

	}

	// 3.2 [LisUsrInVal] Intento de acceso con URL desde un usuario no identificado
	// al listado de usuarios
	// desde un usuario en sesi�n. Debe producirse un acceso no permitido a vistas
	// privadas. FUNCIONA
	@Test
	public void T03_2_LisUsrInVal() {
		PO_HomeView.checkWelcome(driver, PO_Properties.getSPANISH());
		driver.navigate().to("http://localhost:8081/listUsers");
		// Nos deberia redirigir a la pagina del login
		PO_View.checkElement(driver, "id", "tituloLogin");
	}

	// 4.1 [BusUsrVal] Realizar una busqueda valida en el listado de usuarios desde
	// un usuario en sesion. FUNCIONA
	@Test
	public void T04_1_BusUsrVal() {
		// Login valido
		iniciarSesion("javier@correo.es", "123456");
		PO_HomeView.clickOption(driver, "id", "userListPage");
		SeleniumUtils.esperarSegundos(driver, 1);
		PO_PrivateView.searchUser(driver, "noelia");
		PO_View.checkElement(driver, "id", "filaDenoelia@correo.es");

	}

	// 4.2 [BusUsrInVal] Intento de acceso con URL a la busqueda de usuarios desde
	// un usuario no identificado. Debe producirse un acceso no permitido a vistas
	// privadas. FUNCIONA
	@Test
	public void T04_2_BusUsrInVal() {
		driver.navigate().to("http://localhost:8081/listUsers?busqueda=Zelda");
		// Nos deberia redirigir a la pagina del login
		PO_View.checkElement(driver, "id", "tituloLogin");
	}

	// 5.1 [InvVal] Enviar una invitacion de amistad a un usuario de forma valida.
	@Test
	public void T05_1_InvVal() {
		// Login valido
		iniciarSesion("javier@correo.es", "123456");
		PO_HomeView.clickOption(driver, "id", "userListPage");
		SeleniumUtils.esperarSegundos(driver, 1);
		PO_PrivateView.searchUser(driver, "noelia@correo.es");
		PO_View.checkElement(driver, "id", "filaDenoelia@correo.es");
		PO_PrivateView.clickOption(driver, "id", "btnnoelia@correo.es");
		// assertTrue(PO_View.checkElementExists(driver, "id",
		// "btnJuanFrancisco@live.com") == false);
		PO_NavView.accederPeticionesEnviadas(driver);
		// btnAshRodriguez@default.es
		PO_View.checkElement(driver, "id", "filaDenoelia@correo.es");
	}

	/*
	 * 5.2 [InvInVal] Enviar una invitacion de amistad a un usuario al que ya le
	 * habiamos invitado la invitacion previamente. No debera dejarnos enviar la
	 * invitacion, se podra ocultar el boton de enviar invitacion o notificar que ya
	 * habia sido enviada previamente.
	 * 
	 * TAMBIEN COMPRUEBA EL ACCESO POR URL
	 * 
	 * FUNCIONA
	 */
	@Test
	public void T05_2_InvInVal() {

		// Login valido
		iniciarSesion("javier@correo.es", "123456");
		PO_NavView.accederPeticionesEnviadas(driver);
		// // Ya le hemos enviado la peticion a juan?
		// assertTrue(PO_View.checkElementExists(driver, "id",
		// "filaDeJuanFrancisco@live.com") == false);

		// Ahora vamos a intentar enviar otra. Tiene que saltar la alerta
		PO_HomeView.clickOption(driver, "id", "userListPage");
		SeleniumUtils.esperarSegundos(driver, 1);
		PO_PrivateView.searchUser(driver, "noelia");
		PO_View.checkElement(driver, "id", "filaDenoelia@correo.es");
		PO_PrivateView.clickOption(driver, "id", "btnnoelia@correo.es");

		// Tenemos que comprobar que seguimos en la pagina de lista de usuarios y ha
		// salido la franja
		PO_View.checkElement(driver, "id", "parrafoUsuariosSistema");
		assertTrue(PO_View.checkElementExists(driver, "id", "bandaAlerta") == true);

		// Hacemos lo mismo pero por URL
		driver.navigate().to("http://localhost:8081/peticion/enviar/noelia@correo.es");
		PO_View.checkElement(driver, "id", "parrafoUsuariosSistema");
		assertTrue(PO_View.checkElementExists(driver, "id", "bandaAlerta") == true);

	}

	// 6.1 [LisInvVal] Listar las invitaciones recibidas por un usuario, realizar la
	// comprobacion con una lista que al menos tenga una invitacion recibida.
	@Test
	public void T06_1_LisInvVal() {
		iniciarSesion("noelia@correo.es", "123456");
		SeleniumUtils.esperarSegundos(driver, 1);
		PO_NavView.accederPeticionesRecibidas(driver);
		PO_View.checkElement(driver, "id", "filaDejavier@correo.es");
	}

	//
	// 7.1 [AcepInvVal] Aceptar una invitacion recibida
	@Test
	public void T07_1_AcepInvVal() {
		iniciarSesion("noelia@correo.es", "123456");
		PO_NavView.accederPeticionesRecibidas(driver);
		PO_PrivateView.clickOption(driver, "id", "btnjavier@correo.es");

		// Comrpobamos que nos ha enviado a la pagina de usuarios y nos dice que le ha
		// agregado como amigo
		PO_HomeView.clickOption(driver, "id", "userListPage");
		PO_View.checkElement(driver, "id", "parrafoUsuariosSistema");
	}

	// 8.1 [ListAmiVal] Listar los amigos de un usuario, realizar la comprobacion
	// con una lista que al menos tenga un amigo. FUNCIONA
	@Test
	public void T08_1_ListAmiVal() {
		iniciarSesion("javier@correo.es", "123456");
		PO_NavView.accederAmigos(driver);
		PO_View.checkElement(driver, "id", "filaDenoelia@correo.es");
	}


	
	
	
	
	
	
	//REST

	
	
	
	
	
	
	/*
	 * Una vez que ha funcionado todo, vamos a preparar las pruebas del apartado C.
	 */
	@Test
	public void TC00_0_CPreparandoEntorno() {
		SeleniumUtils.esperarSegundos(driver, 1);
		// Javier es el que va a tener los amigos.
		iniciarSesion("javier@correo.es", "123456");

		// Primer amigo es ash@correo.es
		PO_PrivateView.searchUser(driver, "ash@correo.es");
		PO_View.checkElement(driver, "id", "filaDeash@correo.es");
		PO_PrivateView.clickOption(driver, "id", "btnash@correo.es");

		// Segundo amigo es joni@correo.es
		SeleniumUtils.esperarSegundos(driver, 1);
		PO_PrivateView.searchUser(driver, "joni");
		PO_View.checkElement(driver, "id", "filaDejoni@correo.es");
		PO_PrivateView.clickOption(driver, "id", "btnjoni@correo.es");

		// Tercer amigo es pokemon@correo.es
		SeleniumUtils.esperarSegundos(driver, 1);
		PO_PrivateView.searchUser(driver, "pokemon@correo.es");
		PO_View.checkElement(driver, "id", "filaDepokemon@correo.es");
		PO_PrivateView.clickOption(driver, "id", "btnpokemon@correo.es");

		// Ademas, javier ya es amigo de noelia por las pruebas de antes.

		// Se comprueba que se han enviado las 3 peticiones
		PO_NavView.accederPeticionesEnviadas(driver);
		SeleniumUtils.esperarSegundos(driver, 1);
		PO_View.checkElement(driver, "id", "filaDeash@correo.es");
		PO_View.checkElement(driver, "id", "filaDejoni@correo.es");
		PO_View.checkElement(driver, "id", "filaDepokemon@correo.es");

		// Salimos
		PO_HomeView.clickOption(driver, "id", "logout");
		SeleniumUtils.esperarSegundos(driver, 1);
		PO_View.checkElement(driver, "id", "bienvenidaHome");

		/*
		 * Ahora que sabemos que estan enviadas, vamos a aceptar a cada usuario
		 */

		// Primero acepta Ash
		iniciarSesion("ash@correo.es", "123456");
		PO_NavView.accederPeticionesRecibidas(driver);
		PO_PrivateView.clickOption(driver, "id", "btnjavier@correo.es");
		PO_NavView.accederAmigos(driver);
		SeleniumUtils.esperarSegundos(driver, 1);
		PO_View.checkElement(driver, "id", "filaDejavier@correo.es");
		PO_HomeView.clickOption(driver, "id", "logout");
		SeleniumUtils.esperarSegundos(driver, 1);
		PO_View.checkElement(driver, "id", "bienvenidaHome");

		// Luego acepta joni
		iniciarSesion("joni@correo.es", "123456");
		PO_NavView.accederPeticionesRecibidas(driver);
		PO_PrivateView.clickOption(driver, "id", "btnjavier@correo.es");
		PO_NavView.accederAmigos(driver);
		SeleniumUtils.esperarSegundos(driver, 1);
		PO_View.checkElement(driver, "id", "filaDejavier@correo.es");
		PO_HomeView.clickOption(driver, "id", "logout");
		SeleniumUtils.esperarSegundos(driver, 1);
		PO_View.checkElement(driver, "id", "bienvenidaHome");

		// Luego acepta Palkia
		iniciarSesion("pokemon@correo.es", "123456");
		PO_NavView.accederPeticionesRecibidas(driver);
		PO_PrivateView.clickOption(driver, "id", "btnjavier@correo.es");
		PO_NavView.accederAmigos(driver);
		SeleniumUtils.esperarSegundos(driver, 1);
		PO_View.checkElement(driver, "id", "filaDejavier@correo.es");
		PO_HomeView.clickOption(driver, "id", "logout");
		SeleniumUtils.esperarSegundos(driver, 1);
		PO_View.checkElement(driver, "id", "bienvenidaHome");

		// En este punto ya deberian ser todos amigos. Lo comprobamos con Javier.
		iniciarSesion("javier@correo.es", "123456");
		PO_NavView.accederAmigos(driver);
		SeleniumUtils.esperarSegundos(driver, 1);
		PO_View.checkElement(driver, "id", "filaDeash@correo.es");
		PO_View.checkElement(driver, "id", "filaDejoni@correo.es");
		PO_View.checkElement(driver, "id", "filaDepokemon@correo.es");
		PO_View.checkElement(driver, "id", "filaDenoelia@correo.es");

	}

	// C1.1 [CInVal]  Inicio de sesión con datos válidos
    @Test
    public void TC01_1_CInVal() {
        driver.navigate().to(URLCliente);

        iniciarSesion("javier@correo.es", "123456");
        SeleniumUtils.esperarSegundos(driver, 1);
        PO_View.checkElement(driver, "id", "filaDejoni@correo.es");
    }

	//C1.2 [CInInVal] Inicio de sesión con datos inválidos 
	//(usuario no existente en la aplicación).
	@Test
	public void TC01_2_CInInVal() {
		driver.navigate().to(URLCliente);
		
		iniciarSesion("eustaquiohabichuela@correo.es", "123456");
		SeleniumUtils.esperarSegundos(driver, 1);
		PO_View.checkElement(driver, "id", "email");
	}
	
	//C.2.1 [CListAmiVal] Acceder a la lista de amigos de un usuario, que al menos tenga tres amigos.
		@Test
		public void TC02_1_CListAmiVal() {
			driver.navigate().to(URLCliente);
			
			iniciarSesion("javier@correo.es", "123456");
			//SeleniumUtils.esperarSegundos(driver, 1);
			PO_View.checkElement(driver, "id", "filaDejoni@correo.es");
			PO_View.checkElement(driver, "id", "filaDeash@correo.es");
			PO_View.checkElement(driver, "id", "filaDepokemon@correo.es");
			
		}
		
		//C.2.2 [CListAmiFil] Acceder a la lista de amigos de un usuario, y realizar un filtrado para encontrar a un
		//amigo concreto, el nombre a buscar debe coincidir con el de un amigo
		@Test
		public void TC02_2_CListAmiFil() {
			driver.navigate().to(URLCliente);
			
			iniciarSesion("javier@correo.es", "123456");
			//SeleniumUtils.esperarSegundos(driver, 1);

			
			
			
		}
		
		@Test
		public void TC03_0_PrepararMensaje() {
			driver.navigate().to(URLCliente);

	        iniciarSesion("joni@correo.es", "123456");
	        SeleniumUtils.esperarSegundos(driver, 1);
	        PO_View.checkElement(driver, "id", "filaDejavier@correo.es");
	        //Envio de mensajes
			PO_NavView.clickOption(driver, "id", "chatDejavier@correo.es");
	        PO_View.checkElement(driver, "id", "btEnviarMensaje");
	        sendMessage(driver,"Mensaje 1");
	        SeleniumUtils.esperarSegundos(driver, 1);
	        PO_View.checkElement(driver, "text", "Mensaje 1");
	        SeleniumUtils.esperarSegundos(driver, 1);   
	        sendMessage(driver,"Mensaje 2");
	        SeleniumUtils.esperarSegundos(driver, 1);
	        PO_View.checkElement(driver, "text", "Mensaje 2");
	        sendMessage(driver,"Mensaje 3");
	        SeleniumUtils.esperarSegundos(driver, 1);
	        PO_View.checkElement(driver, "text", "Mensaje 3");
	        
	        
		}
		
		//C3.1 [CListMenVal] Acceder a la lista de mensajes de un amigo “chat”, la lista debe contener al menos
		//tres mensajes.
		@Test
		public void TC03_1_CListMenVal() {
			driver.navigate().to(URLCliente);

			
	        iniciarSesion("javier@correo.es", "123456");
	        SeleniumUtils.esperarSegundos(driver, 1);
	        PO_View.checkElement(driver, "id", "filaDejoni@correo.es");
	        PO_NavView.clickOption(driver, "id", "chatDejoni@correo.es");
	        PO_View.checkElement(driver, "id", "btEnviarMensaje");
	        //Falta comprobar mensajes
	        PO_View.checkElement(driver, "text", "Mensaje 1");
	        PO_View.checkElement(driver, "text", "Mensaje 2");
	        PO_View.checkElement(driver, "text", "Mensaje 3");
	        
	        
	        
		}
	
		//C4.1 [CCrearMenVal] Acceder a la lista de mensajes de un amigo “chat” y crear un nuevo mensaje,
		//validar que el mensaje aparece en la lista de mensajes.
		@Test
		public void TC04_1_CCrearMenVal() {
			driver.navigate().to(URLCliente);

	        iniciarSesion("javier@correo.es", "123456");
	        SeleniumUtils.esperarSegundos(driver, 1);
	        PO_View.checkElement(driver, "id", "filaDejoni@correo.es");
	        PO_NavView.clickOption(driver, "id", "chatDejoni@correo.es");
	        PO_View.checkElement(driver, "id", "btEnviarMensaje");
	        sendMessage(driver,"Mensaje 1");
	        SeleniumUtils.esperarSegundos(driver, 1);
	        PO_View.checkElement(driver, "text", "Mensaje 1");
		}
		

				
		
		//C5.1 [CMenLeidoVal] Identificarse en la aplicación y enviar un mensaje a un amigo, validar que el
		//mensaje enviado aparece en el chat. Identificarse después con el usuario que recibido el mensaje y validar
		//que tiene un mensaje sin leer, entrar en el chat y comprobar que el mensaje pasa a tener el estado leído.
//		@Test
//		public void TC05_1_CMenLeidoVal() {
//			driver.navigate().to(URLCliente);
//
//	        iniciarSesion("javier@correo.es", "123456");
//	        SeleniumUtils.esperarSegundos(driver, 1);
//	        PO_View.checkElement(driver, "id", "filaDejoni@correo.es");
//	        PO_NavView.clickOption(driver, "id", "chatDejoni@correo.es");
//	        PO_View.checkElement(driver, "id", "btEnviarMensaje");
//	        fillMessage(driver,"Mensaje 1");
//	        //No lo hace
//	        SeleniumUtils.esperarSegundos(driver, 1);
//	        PO_View.checkElement(driver, "text", "Mensaje 1");
//	        //Todavia no (?)
//	        PO_NavView.clickOption(driver, "id", "logout");
//	        iniciarSesion("joni@correo.es", "123456");
//	        PO_View.checkElement(driver, "id", "filaDejavier@correo.es");
//	        PO_NavView.clickOption(driver, "id", "chatDejavier@correo.es");
//	        PO_View.checkElement(driver, "text", "Mensaje 1");
//	        
//	        
//		}
		
		
		//C6.1 [CListaMenNoLeidoVal] Identificarse en la aplicación y enviar tres mensajes a un amigo, validar
		//que los mensajes enviados aparecen en el chat. Identificarse después con el usuario que recibido el
		//mensaje y validar que el número de mensajes sin leer aparece en la propia lista de amigos.
		//@Test
//		public void TC06_1_CListaMenNoLeidoVal() {
//			driver.navigate().to(URLCliente);
//
//	        iniciarSesion("javier@correo.es", "123456");
//	        SeleniumUtils.esperarSegundos(driver, 1);
//	        PO_View.checkElement(driver, "id", "filaDejoni@correo.es");
//	        PO_NavView.clickOption(driver, "id", "chatDejoni@correo.es");
//	        PO_View.checkElement(driver, "id", "btEnviarMensaje");
//	        fillMessage(driver,"Mensaje 1");
//	        fillMessage(driver,"Mensaje 2");
//	        fillMessage(driver,"Mensaje 3");
//	        SeleniumUtils.esperarSegundos(driver, 1);
//	        PO_View.checkElement(driver, "text", "Mensaje 1");
//	        PO_View.checkElement(driver, "text", "Mensaje 2");
//	        PO_View.checkElement(driver, "text", "Mensaje 3");
//	        //Todavia no (?)
//	        PO_NavView.clickOption(driver, "id", "logout");
//	        iniciarSesion("joni@correo.es", "123456");
//	        PO_View.checkElement(driver, "id", "filaDejoni@correo.es");
//	        PO_NavView.clickOption(driver, "id", "chatDejavier@correo.es");
//	        PO_View.checkElement(driver, "text", "Mensaje 1");
//	        PO_View.checkElement(driver, "text", "Mensaje 2");
//	        PO_View.checkElement(driver, "text", "Mensaje 3");
//	        
//		}
		
		
		//C7.1 [COrdenMenVall] Identificarse con un usuario A que al menos tenga 3 amigos, ir al chat del ultimo
		//amigo de la lista y enviarle un mensaje, volver a la lista de amigos y comprobar que el usuario al que se le
		//ha enviado el mensaje esta en primera posición. Identificarse con el usuario B y enviarle un mensaje al
		//usuario A. Volver a identificarse con el usuario A y ver que el usuario que acaba de mandarle el mensaje
		//es el primero en su lisa de amigos.
		//@Test
//		public void TC07_1_COrdenMenVall() {
//			driver.navigate().to(URLCliente);
//
//	        iniciarSesion("javier@correo.es", "123456");
//	        SeleniumUtils.esperarSegundos(driver, 1);
//	        PO_View.checkElement(driver, "id", "filaDejoni@correo.es");
//	        PO_NavView.clickOption(driver, "id", "chatDejoni@correo.es");
//	        PO_View.checkElement(driver, "id", "btEnviarMensaje");
//	        fillMessage(driver,"Mensaje 1");
//	        SeleniumUtils.esperarSegundos(driver, 1);
//	        PO_View.checkElement(driver, "text", "Mensaje 1");
//	        //Todavia no (?)
//	        PO_NavView.clickOption(driver, "id", "logout");
//	        iniciarSesion("joni@correo.es", "123456");
//	        PO_View.checkElement(driver, "id", "filaDejoni@correo.es");
//	        PO_NavView.clickOption(driver, "id", "chatDejavier@correo.es");
//	        PO_View.checkElement(driver, "text", "Mensaje 1");
//	        
//	        
//		}
	
	private static void iniciarSesion(String email, String pass) {
		// Vamos al formulario de logueo.
		PO_NavView.clickOption(driver, "id", "login");
		SeleniumUtils.esperarSegundos(driver, 2);
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, email, pass);
		// // Comprueba que vea la lista de usuarios
		SeleniumUtils.esperarSegundos(driver, 1);
		

	}
	
	
	
	
	public static void sendMessage(WebDriver driver, String message) {
		WebElement textMessage = driver.findElement(By.name("textoMensaje"));
		textMessage.click();
		textMessage.clear();
		textMessage.sendKeys(message);

		By boton = By.id("btEnviarMensaje");
		driver.findElement(boton).click();
	}
}
