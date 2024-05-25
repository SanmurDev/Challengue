import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        int opcion = 0;
        String monedaInicial = "";
        String monedaFinal = "";
        Double monto = 0.0;
        Double total = 0.0;
        String direccion = "";

        Scanner teclado = new Scanner(System.in);

        try {
            while (opcion != 7) {
                System.out.println("""
                        *******************************************************************************************
                        BIENVENIDOS
                        ******************************************************************************************
                        Seleccione el tipo de cambio que desea consultar:
                        1 - Colones     ->      Dolares
                        2 - Dolares     ->      Colones
                        3 - Colones     ->      Euros
                        4 - Euros       ->      Colones
                        5 - Dolares     ->      Euros
                        6 - Euros       ->      Dolares
                        7 - Salir
                        ********************************************************************************************
                        """);

                opcion = teclado.nextInt();

                switch (opcion) {
                    case 1:
                        monedaInicial = String.valueOf(TipoDeMoneda.CRC);
                        monedaFinal = String.valueOf(TipoDeMoneda.USD);
                        break;
                    case 2:
                        monedaInicial = String.valueOf(TipoDeMoneda.USD);
                        monedaFinal = String.valueOf(TipoDeMoneda.CRC);
                        break;
                    case 3:
                        monedaInicial = String.valueOf(TipoDeMoneda.CRC);
                        monedaFinal = String.valueOf(TipoDeMoneda.EUR);
                        break;
                    case 4:
                        monedaInicial = String.valueOf(TipoDeMoneda.EUR);
                        monedaFinal = String.valueOf(TipoDeMoneda.CRC);
                        break;
                    case 5:
                        monedaInicial = String.valueOf(TipoDeMoneda.USD);
                        monedaFinal = String.valueOf(TipoDeMoneda.EUR);
                        break;
                    case 6:
                        monedaInicial = String.valueOf(TipoDeMoneda.EUR);
                        monedaFinal = String.valueOf(TipoDeMoneda.USD);
                        break;
                    case 7:
                        System.out.println("\nGracias por utilizar nuestros servicios.");
                        break;
                }
                if (opcion < 7 && opcion > 0) {
                    System.out.println("Elija el monto de " + monedaInicial + " que desea convertir a " + monedaFinal);
                    monto = teclado.nextDouble();

                    direccion = ("https://v6.exchangerate-api.com/v6/8dbebf5cd86c5080f218a610/pair/" + monedaInicial + "/" + monedaFinal + "/" + monto);

                    try {
                        HttpClient client = HttpClient.newHttpClient();
                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(direccion))
                                .build();
                        HttpResponse<String> response = client
                                .send(request, HttpResponse.BodyHandlers.ofString());

                        String json = response.body();

                        Conversion convertirMoneda = new Gson().fromJson(json, Conversion.class);
                        total = Double.valueOf(convertirMoneda.conversion_result());
                        System.out.println("\n" + monto + " " + monedaInicial + " son " + total + " " + monedaFinal);

                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    if (opcion != 7) {
                        System.out.println("Por favor ingrese una opción válida.");
                    }
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("ERROR.No se ha ingresado un número válido.");
        }
    }
}
