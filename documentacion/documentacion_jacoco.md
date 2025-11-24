# 📊 Documentación JaCoCo

## ¿Qué es JaCoCo?
JaCoCo (Java Code Coverage) es una herramienta open source para medir la cobertura de código en proyectos Java. Permite analizar qué partes del código han sido ejecutadas durante la ejecución de pruebas, ayudando a identificar áreas no cubiertas por tests.

## ¿Para qué se utiliza?
JaCoCo se utiliza principalmente para:
- Medir la cobertura de pruebas unitarias y de integración.
- Generar reportes visuales (HTML, XML, CSV) sobre el porcentaje de código cubierto.
- Mejorar la calidad del software asegurando que las funcionalidades críticas estén probadas.
- Integrarse con herramientas de CI/CD y análisis de calidad como SonarQube.

## Uso de JaCoCo en este desarrollo
En este proyecto, JaCoCo se utiliza para:
- Analizar la cobertura de los tests sobre los controladores, servicios y repositorios de la aplicación.
- Generar reportes automáticos tras la ejecución de pruebas con Maven (`mvn test`).
- Integrar los resultados de cobertura con SonarQube para visualizar métricas y detectar áreas del código que requieren más pruebas.

### Ejecución básica
Al ejecutar:
```bash
mvn test
```
JaCoCo genera un reporte en `target/site/jacoco/index.html` mostrando el porcentaje de cobertura por clase y método.

### Beneficios en este proyecto
- Permite asegurar que las funcionalidades principales (autenticación, comentarios, valoraciones, publicación de recetas) están correctamente cubiertas por tests.
- Ayuda a cumplir con los estándares de calidad exigidos en la actividad sumativa.
- Facilita la identificación de código no probado, mejorando la robustez y seguridad de la aplicación.

---

## ⚙️ Ejemplo de configuración JaCoCo en Maven

Para usar JaCoCo en este proyecto, solo necesitas agregar el plugin en el archivo `pom.xml`:

```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.jacoco</groupId>
      <artifactId>jacoco-maven-plugin</artifactId>
      <version>0.8.10</version>
      <executions>
        <execution>
          <goals>
            <goal>prepare-agent</goal>
          </goals>
        </execution>
        <execution>
          <id>report</id>
          <phase>test</phase>
          <goals>
            <goal>report</goal>
          </goals>
        </execution>
      </executions>
    </plugin>
  </plugins>
</build>
```

## 🚀 Cómo ejecutar JaCoCo

1. Ejecuta las pruebas:
   ```bash
   mvn test
   ```
2. El reporte de cobertura se genera automáticamente en:
   ```
   target/site/jacoco/index.html
   ```
   Ábrelo en tu navegador para ver el detalle de cobertura por clase y método.

## 📈 Integración con SonarQube

JaCoCo se integra automáticamente con SonarQube si tienes configurado el análisis en tu proyecto. Los resultados de cobertura se muestran en el dashboard de SonarQube junto con otras métricas de calidad.

---

**Más información:**
- [Sitio oficial JaCoCo](https://www.jacoco.org/)
- [Guía de uso en Maven](https://www.eclemma.org/jacoco/trunk/doc/maven.html)
