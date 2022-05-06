git pull
call mvn clean -Dmaven.test.skip=true
call mvn compile -Dmaven.test.skip=true
call mvn package -Dmaven.test.skip=true
docker build -t sgfinancas . 
docker tag sgfinancas 192.168.0.51:8082/sgfinancas:latest
docker push 192.168.0.51:8082/sgfinancas:latest
