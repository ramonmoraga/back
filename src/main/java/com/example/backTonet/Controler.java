package com.example.backTonet;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.RequestContext;

import com.google.protobuf.Timestamp;
@RestController
public class Controler {
@CrossOrigin(origins = "*")
@ResponseStatus(HttpStatus.CREATED)
 @PostMapping("/guardar-ubicaciones")
    public int guardarUbicaciones(
            @RequestParam Double latitud,
            @RequestParam Double longitud,
            @RequestParam String Nombre
            ) {
                System.out.println(Nombre);
                System.out.println(longitud);
                System.out.println(latitud);
            String URL = "jdbc:mysql://localhost:3306/tonet";
            String USUARIO = "root";
            String CONTRASEÑA = "";
            try{
        // Obtener todas las ubicaciones almacenadas en la base de datos
            Connection conexion = null;
            ResultSet resultSet = null;
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
            Class.forName("com.mysql.cj.jdbc.Driver");
            String sql = "INSERT INTO ubicacion (nombre,latitud, longitud) VALUES ('" + Nombre +"' , " + latitud + "," + longitud + ")"; 
            try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.executeUpdate();
            }
         catch (SQLException e) {
            e.printStackTrace();
            
        }
            }catch (Exception e) {
            e.printStackTrace();
            }
            return 1;
            }   
            @CrossOrigin(origins = "*")
            @PostMapping("/register")
            @ResponseStatus(HttpStatus.CREATED)
            public void register(@RequestParam String name, @RequestParam(required = false) Integer number){
                String URL = "jdbc:mysql://localhost:3306/tonet";
                String USUARIO = "root";
                String CONTRASEÑA = "";
                try{
                    if (number == null) {
                        Random rand = new Random();
                        number= rand.nextInt(9000) + 1000;
                        }
            // Obtener todas las ubicaciones almacenadas en la base de datos
                Connection conexion = null;
                ResultSet resultSet = null;
                conexion = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
                Class.forName("com.mysql.cj.jdbc.Driver");
                String sql = "INSERT INTO Trabajador (nombre, id) VALUES ('" + name + "'," + number + ")"; 
                try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                statement.executeUpdate();
                }
             catch (SQLException e) {
                e.printStackTrace();
                
            }
                }catch (Exception e) {
                e.printStackTrace();
                }

            }

                @CrossOrigin(origins = "*")
                @PutMapping("/unregister")
                @ResponseStatus(HttpStatus.CREATED)
                public void unregister(@RequestParam String name){
                    String URL = "jdbc:mysql://localhost:3306/tonet";
                    System.out.println("1");
                    String USUARIO = "root";
                    String CONTRASEÑA = "";
                    
                // Obtener todas las ubicaciones almacenadas en la base de datos
                    Connection conexion = null;
                    ResultSet resultSet = null;
                    try{
                    conexion = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    String sql = "Delete FROM Trabajador WHERE nombre='"+name+"'"; 
            
                    PreparedStatement statement = conexion.prepareStatement(sql);
                    statement.executeUpdate();
                    
                }catch(Exception e){
                    e.printStackTrace();
                }

                }









            @CrossOrigin(origins = "*")
            @GetMapping("/usuarios")
            @ResponseStatus(HttpStatus.OK)
            public ArrayList<String[]> obtener_usuarios() {
                String URL = "jdbc:mysql://localhost:3306/tonet";
                String USUARIO = "root";
                String CONTRASEÑA = "";
                ArrayList <String[]> datos=new ArrayList<>();
                try{
                    // Obtener todas las ubicaciones almacenadas en la base de datos
                        Connection conexion = null;
                        PreparedStatement statement = null;
                        ResultSet resultSet = null;
                        conexion = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        // Paso 3: Crear una consulta SQL
                        String consulta = "SELECT * FROM trabajador ";
                        statement = conexion.prepareStatement(consulta);
                        resultSet = statement.executeQuery();
                       
                        String Nombre="";
                        int id=0;
                        while (resultSet.next()) {
                            
                             Nombre= resultSet.getString("Nombre");
                             id = resultSet.getInt("id");
                             String[] dato=new String[2];
                             dato[0]=Nombre;
                             dato[1]=id+"";
                             datos.add(dato);

                            
                        }
                    
                        
                    
                        }catch(Exception e){
                            System.out.println(e.getMessage());
                        }
                   return datos;
                
            }

            @CrossOrigin(origins = "*")
            public ArrayList<String[]> registros(@RequestParam(required = false) String edificio, 
            @RequestParam(required = false) String user, 
            @RequestParam(required = false) String fecha,
            @RequestParam(required = false) LocalDate fechaan,
            @RequestParam(required = false) LocalDate fechafin) {
String URL = "jdbc:mysql://localhost:3306/tonet";
String USUARIO = "root";
String CONTRASEÑA = "";
ArrayList<String[]> datos = new ArrayList<>();
double total = 0;

try {
if ("este mes".equals(fecha)) {
LocalDate today = LocalDate.now();
YearMonth currentMonth = YearMonth.of(today.getYear(), today.getMonth());
fechaan = currentMonth.atDay(1);
fechafin = currentMonth.atEndOfMonth();
} else if ("este año".equals(fecha)) {
LocalDate today = LocalDate.now();
YearMonth currentMonth = YearMonth.of(today.getYear(), 1);
fechaan = currentMonth.atDay(1);
currentMonth = YearMonth.of(today.getYear(), 12);
fechafin =  currentMonth.atEndOfMonth();
}

Class.forName("com.mysql.cj.jdbc.Driver");
Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);

StringBuilder consulta = new StringBuilder("SELECT persona.nombre AS nombre_persona, edificio.nombre AS nombre_ubicacion, horario.dia, horario.horaentrada, horario.horasalida FROM horario INNER JOIN trabajador ON horario.idper = trabajador.id INNER JOIN persona ON trabajador.id = persona.id INNER JOIN ubicacion ON horario.idub = ubicacion.id INNER JOIN edificio ON ubicacion.id = edificio.id WHERE 1=1");

if (user != null && !user.isEmpty()) {
consulta.append(" AND persona.nombre = ?");
}
if (edificio != null && !edificio.isEmpty()) {
consulta.append(" AND edificio.nombre = ?");
}
if (fechaan != null) {
consulta.append(" AND horario.dia >= ?");
}
if (fechafin != null) {
consulta.append(" AND horario.dia <= ?");
}

PreparedStatement statement = conexion.prepareStatement(consulta.toString());

int index = 1;
if (user != null && !user.isEmpty()) {
statement.setString(index++, user);
}
if (edificio != null && !edificio.isEmpty()) {
statement.setString(index++, edificio);
}
if (fechaan != null) {
statement.setDate(index++, java.sql.Date.valueOf(fechaan));
}
if (fechafin != null) {
statement.setDate(index++, java.sql.Date.valueOf(fechafin));
}

ResultSet resultSet = statement.executeQuery();

while (resultSet.next()) {
String[] fila = new String[7];
fila[0] = resultSet.getString("nombre_persona");
fila[1] = resultSet.getString("nombre_ubicacion");
fila[2] = resultSet.getString("dia");
fila[3] = resultSet.getString("horaentrada");
fila[4] = resultSet.getString("horasalida");
String horaEntrada = resultSet.getString("horaentrada");
String horaSalida = resultSet.getString("horasalida");
double horasTrabajadas = calcularHorasTrabajadas(horaEntrada, horaSalida);
fila[5] = String.valueOf(horasTrabajadas); 
total += horasTrabajadas;
fila[6] = String.valueOf(total);
datos.add(fila);
}

resultSet.close();
statement.close();
conexion.close();
} catch (SQLException | ClassNotFoundException e) {
e.printStackTrace();
}

return datos;
}

private double calcularHorasTrabajadas(String horaEntrada, String horaSalida) {
    try {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date dateEntrada = format.parse(horaEntrada);
        Date dateSalida = format.parse(horaSalida);
        long diferencia = dateSalida.getTime() - dateEntrada.getTime();
        double horas = (double) diferencia / (1000 * 60 * 60); // Convertimos la diferencia de milisegundos a horas
        if (horas == Math.floor(horas)) {
            // No hay decimales, devolver las horas como un valor entero
            return horas;
        } else {
            // Hay decimales, redondear a dos decimales y devolver
            return Math.round(horas * 100.0) / 100.0;
        }
    } catch (ParseException e) {
        e.printStackTrace();
        return 0; // En caso de error, devolvemos 0 horas
    }
}
            
            
            @CrossOrigin(origins = "*")
            @PostMapping("/login")
            @ResponseStatus(HttpStatus.OK)
            public void login(@RequestParam String username, @RequestParam String password) {
                if(!(username.equals("Tonet")) && password.equals("12345")){
                    System.out.print("Error");
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Username or password incorrect");
                }else{
                    System.out.print("Sí");
                }
            }
    @GetMapping("/comparar-ubicaciones")
    public String compararUbicaciones(
            @RequestParam Double latitud,
            @RequestParam Double longitud) {
            String URL = "jdbc:mysql://localhost:3306/tonet";
            String USUARIO = "root";
            String CONTRASEÑA = "";
            try{
        // Obtener todas las ubicaciones almacenadas en la base de datos
            Connection conexion = null;
            PreparedStatement statement = null;
            ResultSet resultSet = null;
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Paso 3: Crear una consulta SQL
            String consulta = "SELECT latitud,longitud FROM ubicaciones where id=1 ";
            statement = conexion.prepareStatement(consulta);
            resultSet = statement.executeQuery();
            Double lati=0.0;
            Double lon=0.0;
            while (resultSet.next()) {
                // Procesar cada fila de resultados
                 lati = resultSet.getDouble("latitud");
                 lon = resultSet.getDouble("longitud");
                // ... hacer algo con los datos obtenidos
            }
            System.out.println(lati);
            System.out.println(lon);
            Double a= calcularDistancia(latitud,longitud,lati,lon);
            System.out.println(a);
            if(a<50){
                System.out.println("menor");
            }else{
                System.out.println("mayor");
            }
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
       return "aa";
    }
    public double calcularDistancia(
            @RequestParam Double latitud1,
            @RequestParam Double longitud1,
            @RequestParam Double latitud2,
            @RequestParam Double longitud2) {

        // Calcular la distancia utilizando la fórmula de Haversine
        return calcularDistanciaHaversine(latitud1, longitud1, latitud2, longitud2);
    }

    private double calcularDistanciaHaversine(double latitud1, double longitud1, double latitud2, double longitud2) {
        // Convertir las coordenadas de grados a radianes
        double lat1Rad = Math.toRadians(latitud1);
        double lon1Rad = Math.toRadians(longitud1);
        double lat2Rad = Math.toRadians(latitud2);
        double lon2Rad = Math.toRadians(longitud2);

        // Radio de la Tierra en metros
        final double RADIO_TIERRA = 6371000; // aproximadamente 6371 km

        // Calcular la diferencia de latitud y longitud
        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        // Calcular la distancia utilizando la fórmula de Haversine
        double a = Math.pow(Math.sin(deltaLat / 2), 2) +
                   Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                   Math.pow(Math.sin(deltaLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return RADIO_TIERRA * c;
    }


    @CrossOrigin(origins = "*")
@ResponseStatus(HttpStatus.OK)
 
@GetMapping("/obtener-ubicaciones")

    public ArrayList<String[]> obtenerUbicaciones(
            ) {
            String URL = "jdbc:mysql://localhost:3306/tonet";
            String USUARIO = "root";
            ArrayList<String[]> resultado= new ArrayList<>();
            String CONTRASEÑA = "";
            try{
        // Obtener todas las ubicaciones almacenadas en la base de datos
            Connection conexion = null;
           
            ResultSet resultSet = null;
            String[] campo=new String[4];
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
            Class.forName("com.mysql.cj.jdbc.Driver");
            String sql = "SELECT * FROM  ubicacion"; 
            try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                resultSet = statement.executeQuery(sql);

                while (resultSet.next()) {
                    campo=new String[4];
                    // Leer los valores de cada columna
                    campo[1]=resultSet.getString("Nombre");
                    campo[2]=resultSet.getDouble("latitud")+"";
                    campo[3]=resultSet.getDouble("longitud")+"";
                    campo[0]=resultSet.getInt("id")+"";
                    
                    // Y así sucesivamente para cada columna que desees recuperar
                    resultado.add(campo);
                    // Hacer algo con los valores recuperados, por ejemplo, imprimirlos
                    
                }
            }
         catch (SQLException e) {
            e.printStackTrace();
            
        }
            }catch (Exception e) {
            e.printStackTrace();
            }
            return resultado;
            }   






            @CrossOrigin(origins = "*")
            @ResponseStatus(HttpStatus.OK)
             @PutMapping("/eliminar-ubicaciones")
                public ArrayList<String[]> eliminarubicaciones(
                        @RequestParam int id
                        ) {
                        String URL = "jdbc:mysql://localhost:3306/tonet";
                        String USUARIO = "root";
                        ArrayList<String[]> resultado= new ArrayList<>();
                        String CONTRASEÑA = "";
                        try{
                    // Obtener todas las ubicaciones almacenadas en la base de datos
                        Connection conexion = null;
                       
                        int resultSet = 0;
                        String[] campo=new String[4];
                        conexion = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        String sql = "Delete FROM  ubicaciones where id= " + id; 
                        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                            resultSet = statement.executeUpdate(sql);
            
                          
                        }
                     catch (SQLException e) {
                        e.printStackTrace();
                        
                    }
                        }catch (Exception e) {
                        e.printStackTrace();
                        }
                        return resultado;
                        }  
}