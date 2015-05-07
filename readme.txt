Requerimientos para ejecutar el programa:
	-Las librerias de weka 3.7.10.
	-La máquina virtual de java 1.7.
	-Programa compilador como eclipse o netbeans.
	-Código fuente adjunto.
	-Archivos *.arff a usar.

Pasos para la compilación del programa: (si se desea usar el programa compilado adjunto, omitir)
	1: Crear un nuevo proyecto con un package llamado 'mineriadatos'.
	2: Importar el codigo fuente (archivos *.java) dentro del package.
	3: Referenciar las librerias de weka (en eclipse, build path > add external jar).
	4: Ejecutar. Añadir los parámetros especificados más adelante.

Pasos para la ejecución del programa:
	1: Incluir en una carpeta los archivos *.arff y el archivo *.jar adjunto o creado.
	2: Abrir una consola o terminar y moverse hasta la carpeta del paso anterior.
	3: Ejecutar el comando java para la ejecución:
		java -jar Clustering.jar a k m i p c c

Pasos para usar el script de pruebas: (solo funciona en terminal)
	1: Incluir en una carpeta los archivos *.arff, el archivo *.jar y el script *.sh
	2: Modificar el script para ajustarse a los archivos arff y jar.
	3: Abrir terminal y moverse hasta la carpeta del paso 1.
	4: Darle permisos de ejecución al archivo
		chmod u+x archivo.sh
	5. Ejecutar el script y redirigir la salida para una lectura posterior.
		./archivo.sh > log.txt

Parametros a tener en cuenta:
	a = archivo *.arff a utilizar.
	k = número de clusters a realizar.
	m = parámetro de la métrica de minkowski.
	i = criterio de inicializacion.
	p = criterio de parada.
	c = constante de parada.
	c = parametro a indicar la existencia de la clase en el archivo.
