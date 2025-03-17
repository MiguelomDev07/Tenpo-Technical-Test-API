# Guia de la Prueba Tecnica

### Endpoints de la API para gestionar el Servidor Externo Mockeado.

1. POST "/mock-service/stop":  Este Endpoint permite detener el servicio externo simulado.
2. POST "/mock-service/start":  Este Endpoint permite iniciar el servicio externo simulado.
3. GET  "/mock-service/status":  Este Endpoint permite recuperar el estado del servicio externo simulado.


### Endpoints de la API para consmuir los servicios de la Prueba Técnica de Tenpo.

1. GET  "/tenpo/api/v1/get-operating-calculation":   Este Endpoint permite realizar una recuperación de la lista paginada del historial de peticiones.
2. GET  "/tenpo/api/v1/get-request-history":        Este Endpoint permite realizar un cálculo de la operación entre dos números y aplicarle un porcentaje determinado.


### Notas.

* Borrado de Cache: reiniciar el servicio desde docker o desde el IDE de su preferencia.
	- Comando CMD para ver el ID del Contenedor:   "docker ps"
	- Comando CMD para reiniciar el servicio: 		"docker restart <nombre_o_ID_del_contenedor>"

* Creacion de tabla en PostgresQL DB:   Ejecutar el siguiente Script.sql que tambien podran encontrar en la raiz del repositorio publico en GitHub.

	CREATE TABLE requesthistory (
  		id integer not null,
  		endpoint VARCHAR(50) not null,
  		parameters VARCHAR(31) not null,
  		response VARCHAR(100) not null,
  		creationdate DATE not null
	);

* Documentacion API: fue usada la libreria "OpenAPI-SWagger-UI".
	- Desplegar documentacion grafica de la API en el navegador con "http://localhost:8080/documentation" o "http://localhost:8080/swagger-ui/index.html".

* Servidor Externo Simulado: se levanta automaticamente en conjunto con la API Java SpringBoot. Use los Endpoints del Servidor Externo Mockeado 
en la parte superior de la documentacion grafica desplegada con "OpenAPI-SWagger-UI".

* Repositorio publico en la nube
	- pueden descargar el repositorio de la prueba tecnica desde mi perfil de cuenta en GitHub:  https://github.com/MiguelomDev07/Tenpo-Technical-Test-API

* docker-compose.yml/Imagen Docker publica en la nube
	- pueden descargar la imagen docker y el archivo docker-compose.yml de la prueba tecnica desde mi perfil de cuenta en DockerHub:  https://hub.docker.com/repository/docker/miguelom7/tenpo-technical-test-container-image/general