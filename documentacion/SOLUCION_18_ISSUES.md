# Solución: 18 New Issues no visibles en SonarQube

## Problema Actual

- ✅ **Cobertura**: 86.3% (supera el 80% requerido)
- ❌ **New Issues**: 18 issues detectadas
- ❌ **Quality Gate**: Failed debido a las 18 issues
- ⚠️ **Visualización**: Las issues NO aparecen en el filtro "Issues in new code"

## Causa Raíz

Este problema ocurre cuando:
1. Las issues fueron generadas en un análisis anterior
2. El código fue modificado pero SonarQube no actualizó el estado
3. Hay un problema de sincronización entre el análisis y la base de datos de SonarQube

## Soluciones

### Solución 1: Limpiar Filtros y Buscar Issues Manualmente

En la interfaz de SonarQube:

1. Ve a la pestaña **"Issues"**
2. Haz clic en **"Clear All Filters"** (botón rojo en tu captura)
3. En el menú de filtros, selecciona:
   - **Status**: "Open" o "To Review"
   - **Resolution**: "Unresolved"
   - **Period**: Quita el filtro de "Issues in new code"
4. Busca por severidad:
   - **Blocker** (0 según tu reporte)
   - **Critical**
   - **Major**
   - **Minor**

### Solución 2: Ejecutar Nuevo Análisis Completo

El análisis debe ejecutarse desde Docker ya que tienes esa configuración:

```bash
# 1. Verificar que los contenedores estén corriendo
docker ps | grep sonarqube
docker ps | grep recetas_app

# 2. Si no están corriendo, iniciarlos
./sonarqube-start.sh
# Y tu contenedor de la aplicación

# 3. Ejecutar el análisis
./sonar-scan.sh
```

El script ejecutará:
```bash
mvn clean verify sonar:sonar \
  -Dsonar.projectKey=sumativa2 \
  -Dsonar.projectName='Recetas Seguras' \
  -Dsonar.host.url=http://sonarqube:9000 \
  -Dsonar.token=sqp_07544b918e1e702ae9e26cdac1984b9f411c4806
```

### Solución 3: Verificar Tipo de Issues

Las 18 issues pueden ser de diferentes tipos:

#### A. Code Smells (Mejoras de código)
- Código duplicado
- Complejidad ciclomática alta
- Métodos muy largos
- Clases con muchas responsabilidades

#### B. Bugs potenciales
- Null pointer exceptions posibles
- Resource leaks
- Logic errors

#### C. Security Vulnerabilities
- Pero según tu reporte: **0 Security Hotspots** ✅

### Solución 4: Revisar Código Nuevo desde Nov 22

Las issues están en el código marcado como "New Code Since November 22, 2025".

Archivos que probablemente tienen issues:

1. **PublicarRecetaController.java** (acabas de modificar)
   - Posibles issues: Complejidad del método `publicarReceta()`
   - Solución: Refactorizar en métodos más pequeños

2. **SecurityConfig.java**
   - Posible issue: Método `securityFilterChain()` muy largo
   - Solución: Extraer configuraciones en métodos privados

3. **Archivos de tests**
   - Posibles issues: Duplicación de código en tests
   - Assertions duplicadas
   - Magic numbers

### Solución 5: Ajustar Quality Gate (Temporal)

Si las issues no son críticas, puedes ajustar temporalmente el Quality Gate:

1. En SonarQube, ve a **Quality Gates**
2. Edita el gate activo
3. Modifica la condición de **"New Issues"**:
   - En lugar de `= 0`
   - Cambiarlo a `≤ 20` temporalmente

**⚠️ NO RECOMENDADO para producción**, solo para identificar el problema.

### Solución 6: Verificar en la Base de Datos

Si tienes acceso al contenedor de SonarQube:

```bash
# Ver issues del proyecto
docker exec sonarqube sh -c "
  psql -U sonar -d sonar -c \"
    SELECT issue_key, kee, rule_key, severity, status, resolution
    FROM issues
    WHERE project_uuid = (SELECT uuid FROM projects WHERE kee = 'sumativa2')
    AND status != 'CLOSED'
    LIMIT 20;
  \"
"
```

## Checklist de Verificación

- [ ] Verificar que no hay filtros activos en la página de Issues
- [ ] Buscar por cada tipo de severidad individualmente
- [ ] Verificar que el "New Code Period" está correctamente configurado
- [ ] Ejecutar nuevo análisis desde Docker
- [ ] Revisar los archivos modificados desde Nov 22:
  - [ ] PublicarRecetaController.java
  - [ ] SecurityConfig.java
  - [ ] WebConfig.java
  - [ ] Tests nuevos
- [ ] Verificar logs del contenedor SonarQube: `docker logs sonarqube`

## Comandos Útiles

```bash
# Ver logs de SonarQube
docker logs sonarqube --tail 100

# Ver logs de la aplicación
docker logs recetas_app --tail 100

# Reiniciar SonarQube (limpia caché)
./sonarqube-stop.sh
./sonarqube-start.sh

# Ejecutar análisis
./sonar-scan.sh
```

## Resultado Esperado

Después de ejecutar el nuevo análisis:
- **Coverage**: Debe mantenerse en 86.3% ✅
- **New Issues**: Debe bajar a 0 o mostrar issues específicas que puedas corregir
- **Quality Gate**: PASSED ✅

## Archivos a Revisar Manualmente

Si las issues persisten, revisa estos patrones comunes:

### 1. Complejidad Ciclomática
```java
// ❌ Método muy complejo (muchos if/else anidados)
public String publicarReceta(...) {
    if (condicion1) {
        if (condicion2) {
            if (condicion3) {
                // ...
            }
        }
    }
}

// ✅ Refactorizar en métodos más pequeños
public String publicarReceta(...) {
    validarUsuario();
    procesarReceta();
    guardarArchivos();
}
```

### 2. Duplicación de Código
```java
// ❌ Código duplicado en tests
assertEquals("redirect:/recetas/publicar", view);
assertTrue(redirect.getFlashAttributes().containsKey("mensajeError"));

// ✅ Extraer a método helper
private void assertRedirectConError(String view, RedirectAttributes redirect) {
    assertEquals("redirect:/recetas/publicar", view);
    assertTrue(redirect.getFlashAttributes().containsKey("mensajeError"));
}
```

### 3. Magic Numbers
```java
// ❌ Magic number
if (media.getSize() > 10 * 1024 * 1024) {

// ✅ Constante con nombre
private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
if (media.getSize() > MAX_FILE_SIZE) {
```

## Próximos Pasos

1. **Ejecutar**: `./sonar-scan.sh`
2. **Esperar** a que termine el análisis (5-10 minutos)
3. **Revisar** en http://localhost:9000/dashboard?id=sumativa2
4. **Si persisten las issues**: Tomar captura de pantalla de las issues específicas y reportar

---

**Fecha**: 24 de Noviembre, 2025
**Estado**: Cobertura OK (86.3%), Issues pendientes (18)
**Objetivo**: Reducir issues a 0 para pasar Quality Gate
