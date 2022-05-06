git pull 
git checkout release
git pull
call mvn clean -Dmaven.test.skip=true
call mvn compile -Dmaven.test.skip=true
call mvn package -Dmaven.test.skip=true
docker build -t sgfinancas . 
docker tag sgfinancas villefort/sgfinancas:latest
docker push villefort/sgfinancas:latest
