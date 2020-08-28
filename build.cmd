docker build -t=test .

docker create -ti --name dummy test bash
docker cp dummy:/tmp/target ./
docker rm -f dummy

pause