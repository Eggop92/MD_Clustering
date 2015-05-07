echo Con distintos clusters:
echo k=1
java -jar Clustering.jar food.arff 1 2 a i 5 n
echo k=2
java -jar Clustering.jar food.arff 2 2 a i 5 n
echo k=3
java -jar Clustering.jar food.arff 3 2 a i 5 n
echo k=5
java -jar Clustering.jar food.arff 5 2 a i 5 n
echo k=10
java -jar Clustering.jar food.arff 10 2 a i 5 n
echo
echo Con distintas metricas
echo m=1
java -jar Clustering.jar food.arff 3 1 a i 5 n
echo m=2
java -jar Clustering.jar food.arff 3 2 a i 5 n
echo m=4
java -jar Clustering.jar food.arff 3 4 a i 5 n
echo para m=7.5 sabemos que no funciona por restricciones
echo m=10
java -jar Clustering.jar food.arff 3 10 a i 5 n
echo
echo Con distintas inicializaciones
echo inicializacion aleatoria
java -jar Clustering.jar food.arff 3 2 a i 5 n
echo inicializacion por division de espacios
java -jar Clustering.jar food.arff 3 2 d i 5 n
echo
echo Con distintos criterios de parada
echo parada por 2 iteraciones
java -jar Clustering.jar food.arff 3 2 a i 2 n
echo parada por 5 iteraciones
java -jar Clustering.jar food.arff 3 2 a i 5 n
echo parada por 10 iteraciones
java -jar Clustering.jar food.arff 3 2 a i 10 n
echo parada por 15 iteraciones
java -jar Clustering.jar food.arff 3 2 a i 15 n
echo parada por 25 iteraciones
java -jar Clustering.jar food.arff 3 2 a i 25 n
echo "parada por disimilitud <0.1"
java -jar Clustering.jar food.arff 3 2 a d 0.1 n
echo "parada por disimilitud < 0.01"
java -jar Clustering.jar food.arff 3 2 a d 0.01 n
echo "parada por disimilitud <0.05"
java -jar Clustering.jar food.arff 3 2 a d 0.05 n
echo "parada por disimilitud <0.001"
java -jar Clustering.jar food.arff 3 2 a d 0.001 n
