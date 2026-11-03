# Kata 3 - Transformación de datos a JSON

## Objetivo de la entrega
El programa lee una lista de usuarios en formato JSON (de un fichero o de la API de randomuser.me), comprueba cuáles son correctos, los convierte en objetos de Java y genera un JSON nuevo con los usuarios válidos y un resumen (metadatos).

La idea de la kata es separar el programa en piezas que hacen una sola cosa cada una: **leer, parsear, transformar (y validar) y escribir el JSON**. Así, si cambia el origen de los datos, el resto del programa no se toca.

## Dependencias y versión de JDK
- **JDK:** Java 26
- **Maven** para gestionar las dependencias
- **Gson 2.14.0:** librería para leer y escribir JSON (así no tengo que construir el JSON a mano)
- **JUnit 5.13.4:** para los tests
- **Surefire 3.5.3:** el plugin de Maven que ejecuta los tests con `mvn test`
- **IDE:** IntelliJ IDEA

## Cómo compilar y ejecutar
1. Clonar el repositorio y abrirlo en IntelliJ (carga el `pom.xml` solo):
   ```bash
   git clone https://github.com/ICAB6002/kata3.git
   ```
2. Ejecutar `Main.java`. Se puede elegir la entrada con un argumento (*Run → Edit Configurations → Program arguments*):
    - Sin argumentos: lee `users.json`
    - `users-invalid.json`: lee el fichero con usuarios erróneos
    - `https://randomuser.me/api/?results=5&seed=kata3&inc=gender,name,email,dob`: lee de la API
3. El resultado se guarda en `output.json`, en la carpeta del proyecto.
4. Tests: clic derecho en `src/test/java` → *Run 'Tests in java'*, o en terminal `mvn test`.

La rama principal es **`master`**.

## Estructura y clases principales
| Paquete | Clases | Qué hacen |
|---|---|---|
| `domain.model` | `User` | El usuario con el que trabaja el programa (nombre, fecha de nacimiento, email y género). No sabe nada de JSON. |
| `io` | `TextSource`, `ResourceTextSource`, `HttpTextSource` | Leen el texto de un fichero o de la API. |
| `io` | `UserParser`, `GsonUserParser` | Pasan el texto a objetos `UserDto` (copia del JSON tal cual, sin comprobar nada). |
| `transform` | `UserTransformer`, `TransformResult` | Comprueban cada usuario, descartan los erróneos y crean los `User` válidos. También cuentan los descartados y los usuarios por género. |
| `io` | `UserSerializer`, `GsonUserSerializer`, `UserReportDto` | Escriben el JSON de salida con los usuarios y los metadatos. |
| raíz | `Main` | Elige qué piezas usar y las une. |

## Ejemplo de entrada y salida
- Entrada: `src/main/resources/users.json` (5 usuarios) y `users-invalid.json` (2 correctos y 3 con errores: email vacío, fecha `"ayer"` y sin fecha).
- Salida esperada: `src/test/resources/expected-output.json`.

Resumen de la salida con `users-invalid.json`:
```json
"metadata": {
  "generatedAt": "2026-11-02T18:51:28Z",
  "processed": 5,
  "valid": 2,
  "discarded": 3,
  "byGender": { "male": 2 }
}
```

## Repeticiones de la kata
Cada intento está en su propia rama, que sale de `master`, y tiene un commit por cada pieza:

| Rama | De dónde lee | Variación |
|---|---|---|
| `first-try` | Fichero `users.json` | Versión base |
| `second-try` | API de randomuser | Solo cambia el lector. La salida es la misma que en `first-try` (gracias a `seed=kata3`), lo que demuestra que cambiar el origen no obliga a tocar lo demás. |
| `third-try` | Los dos (según el argumento) | Nuevo metadato `byGender` (usuarios por género) y tests. |

`develop` parte de `third-try` (la versión final), se le añade este README y se une a `master` con un merge.

## Verificación
Hay 12 tests con JUnit, que cubren lo que pide el enunciado:

| Comprobación | Test |
|---|---|
| La salida es igual al JSON esperado | `valid_input_produces_the_expected_json` |
| Los registros mal formados se descartan y se cuentan | `malformed_records_are_discarded_and_counted` |
| Dataset vacío (texto vacío o sin usuarios) | `blank_text_is_an_empty_dataset`, `json_without_users_is_an_empty_dataset`, `empty_dataset_produces_valid_json_with_zero_counts` |
| El JSON generado se puede volver a leer | `generated_json_can_be_parsed_again` |
| Cambiar el origen no cambia el resultado | `changing_the_source_does_not_change_the_output` |

Para que la salida siempre sea igual en los tests, el serializador usa un reloj fijo (`Clock`) en lugar de la hora real.

## Vídeo explicativo
Enlace: PENDIENTE