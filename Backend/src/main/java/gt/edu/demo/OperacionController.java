package gt.edu.demo;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.json.simple.JSONObject;

@Path("/Operacion")
@Singleton
public class OperacionController {
    @GET
    @Produces("application/json")
    public String calcular(
            @QueryParam("a") double a,
            @QueryParam("b") double b,
            @QueryParam("op") String op
    ) {
        double resultado;
        String operacion;

        switch (op.toLowerCase()) {
            case "suma":
                resultado = a + b;
                operacion = "suma";
                break;
            case "resta":
                resultado = a - b;
                operacion = "resta";
                break;
            case "multiplicacion":
                resultado = a * b;
                operacion = "multiplicación";
                break;
            case "division":
                if (b == 0) {
                    JSONObject error = new JSONObject();
                    error.put("error", "No se puede dividir entre cero");
                    return error.toJSONString();
                }
                resultado = a / b;
                operacion = "división";
                break;
            default:
                JSONObject error = new JSONObject();
                error.put("error", "Operación no válida. Usa suma, resta, multiplicacion o division");
                return error.toJSONString();
        }

        JSONObject json = new JSONObject();
        json.put("operacion", operacion);
        json.put("a", a);
        json.put("b", b);
        json.put("resultado", resultado);

        return json.toJSONString();
    }
}
