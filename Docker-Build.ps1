param ($Ver = $("latest"))


function build
{
    ($Tag = ("garrowd/admissionservice:$Ver"))
    ./gradlew clean build
    docker build -t $Tag .
    docker push $Tag
}
build