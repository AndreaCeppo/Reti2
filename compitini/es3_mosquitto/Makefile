build: clean
	mkdir out
	javac src/*.java -cp "./org.eclipse.paho.client.mqttv3-1.1.0.jar" -d out

clean:
	rm -rf out
	rm -rf lock

run: build
	java -cp "out:org.eclipse.paho.client.mqttv3-1.1.0.jar" Benchmark
